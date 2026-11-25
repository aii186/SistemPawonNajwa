/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import model.ItemPesanan;
import model.Makan;
import model.Menu;
import model.Minum;
import model.TransaksiPesanan;
import model.arrayMenu;
import model.arrayPesanan;
import model.Database;
import java.sql.SQLException;
import model.pembayaran.BankAccount;
import model.pembayaran.EWalletAccount;
import model.pembayaran.QrisAccount;

/**
 *
 * @author USER
 */
public class PawonNajwaController {
    private static PawonNajwaController instance;

    private arrayMenu daftarMenu;
    private arrayPesanan keranjang;
    private List<TransaksiPesanan> daftarTransaksi = new ArrayList<>();
    private int nomorOrder = 1;
    private long totalPendapatan = 0;

    public PawonNajwaController() {
        this.daftarMenu = new arrayMenu(30);
        this.daftarMenu.isiMenu();
        try {
            Database.initialize();
            List<Menu> saved = Database.loadMenu();
            if (saved.isEmpty()) {
                Database.saveMenu(java.util.Arrays.asList(daftarMenu.getSemuaMenu()));
            } else {
                daftarMenu.gantiSemua(saved.toArray(new Menu[0]));
            }
            daftarTransaksi = Database.loadOrders();
            for (TransaksiPesanan t : daftarTransaksi) if (!ST_DIBATALKAN.equals(t.getStatus())) totalPendapatan += t.getTotal();
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Tidak bisa terhubung ke MySQL. Jalankan MySQL di Laragon, impor database/pawon_najwa.sql, "
                + "dan tambahkan MySQL Connector/J ke Libraries.\n\nDetail: " + ex.getMessage(),
                "Database Pawon Najwa", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        this.keranjang = new arrayPesanan(30);
    }

    public static PawonNajwaController getInstance() {
        if (instance == null) {
            instance = new PawonNajwaController();
        }
        return instance;
    }

    public Menu[] getDaftarMakanan() {
        return daftarMenu.getDaftarMakanan();
    }

    public Menu[] getDaftarMinuman() {
        return daftarMenu.getDaftarMinuman();
    }

    public Menu[] getSemuaMenu() {
        return daftarMenu.getSemuaMenu();
    }

    public arrayPesanan getKeranjang() {
        return keranjang;
    }

    public List<TransaksiPesanan> getDaftarTransaksi() {
        return daftarTransaksi;
    }

    public long getTotalPendapatan() {
        return totalPendapatan;
    }

    public String formatRupiah(long angka) {
        return "Rp" + String.format(Locale.US, "%,d", angka).replace(',', '.');
    }

    public String tambahKeKeranjang(int kategori, int nomorPilihan, int jumlah) {
        if (nomorPilihan <= 0) {
            return "Pilih menu terlebih dahulu dari tabel!";
        }
        Menu pesanan = (kategori == 1)
                ? daftarMenu.pilihMakanan(nomorPilihan)
                : daftarMenu.pilihMinuman(nomorPilihan);

        if (pesanan == null) {
            return "Menu tidak ditemukan!";
        }
        if (jumlah <= 0) {
            return "Jumlah pesanan minimal 1!";
        }
        if (pesanan.getStok() <= 0) {
            return "Maaf, stok " + pesanan.getNama() + " sedang habis!";
        }

        int sudahDiKeranjang = keranjang.getJumlahDipesanUntukMenu(pesanan);
        if (sudahDiKeranjang + jumlah > pesanan.getStok()) {
            return "Stok tidak mencukupi! Sisa stok " + pesanan.getNama() + ": " + pesanan.getStok();
        }

        if (!keranjang.tambah(pesanan, jumlah)) {
            return "Keranjang sudah penuh!";
        }
        return "OK";
    }

    public void kosongkanKeranjang() {
        keranjang.kosongkan();
    }

    public String prosesPembayaran(String namaPelanggan, String nomorMeja,
                                   String metode, String noRekAtauHp, String securityCode) {
        if (keranjang.getJumlahItem() == 0) {
            return "GAGAL: Keranjang masih kosong!";
        }

        int total = keranjang.getTotal();
        String detailBayar = "";

        if ("BANK".equalsIgnoreCase(metode)) {
            if (!semuaAngka(noRekAtauHp)) return "GAGAL: Nomor rekening harus angka!";
            BankAccount bank = new BankAccount(noRekAtauHp, 1000000, securityCode);
            if (!bank.charge(total)) {
                return "GAGAL: Pembayaran Bank gagal! Cek security code (3 digit) atau saldo.";
            }
            detailBayar = "Metode     : Bank Account\nSisa Saldo : " + formatRupiah((long) bank.getBalance());

        } else if ("EWALLET".equalsIgnoreCase(metode)) {
            noRekAtauHp = (noRekAtauHp == null) ? "" : noRekAtauHp.trim();
            if (!semuaAngka(noRekAtauHp)) return "GAGAL: Nomor HP harus angka!";
            EWalletAccount ew = new EWalletAccount(noRekAtauHp, 1000000);
            if (!ew.charge(total)) {
                return "GAGAL: Nomor HP minimal 10 digit (yang terbaca: "
                        + noRekAtauHp.length() + " digit, isi: \"" + noRekAtauHp + "\").";
            }
            detailBayar = "Metode     : E-Wallet\nSisa Saldo : " + formatRupiah((long) ew.getBalance());
        } else {
            QrisAccount qris = new QrisAccount("PAWONNAJWA001", "QRIS-PAWON-NAJWA", 0, true);
            if (!qris.receive(total)) {
                return "GAGAL: Pembayaran QRIS gagal!";
            }
            detailBayar = "Metode     : QRIS Sistem Pawon Najwa\nDana Masuk : " + formatRupiah(total);
        }

        StringBuilder ringkasan = new StringBuilder();
        List<ItemPesanan> salinan = new ArrayList<>();
        StringBuilder struk = new StringBuilder();
        String idOrder = String.format("PN-%03d-%05d", nomorOrder++, (System.currentTimeMillis() % 100000));
        String pelanggan = (namaPelanggan == null || namaPelanggan.trim().isEmpty()) ? "Pelanggan" : namaPelanggan.trim();

        struk.append("====== STRUK SISTEM PAWON NAJWA ======\n");
        struk.append("No. Order : ").append(idOrder).append("\n");
        struk.append("Pelanggan : ").append(pelanggan).append(" (").append(nomorMeja).append(")\n");
        struk.append("--------------------------------------\n");

        for (int i = 0; i < keranjang.getJumlahItem(); i++) {
            ItemPesanan item = keranjang.getItem(i);
            item.getMenu().kurangiStok(item.getJumlah());
            salinan.add(new ItemPesanan(item.getMenu(), item.getJumlah()));

            if (i > 0) ringkasan.append(", ");
            ringkasan.append(item.getMenu().getNama()).append(" x").append(item.getJumlah());

            struk.append(i + 1).append(". ").append(item.getMenu().getNama()).append("\n");
            struk.append("   ").append(item.getJumlah()).append(" x ")
                 .append(formatRupiah(item.getMenu().getHarga()))
                 .append(" = ").append(formatRupiah(item.getSubtotal())).append("\n");
        }

        struk.append("--------------------------------------\n");
        struk.append("TOTAL : ").append(formatRupiah(total)).append("\n");
        struk.append("======================================\n");
        struk.append(detailBayar).append("\n");
        struk.append("Status: LUNAS (Diteruskan ke Kasir)");

        TransaksiPesanan transaksiBaru = new TransaksiPesanan(idOrder, pelanggan, nomorMeja, ringkasan.toString(), total, metode);
        transaksiBaru.setItems(salinan);
        daftarTransaksi.add(0, transaksiBaru);
        try { Database.saveOrder(transaksiBaru); }
        catch (SQLException ex) { daftarTransaksi.remove(transaksiBaru); return "GAGAL: Pesanan tidak dapat diteruskan ke kasir karena gagal disimpan ke database: " + ex.getMessage(); }
        simpanMenu();
        totalPendapatan += total;
        keranjang.kosongkan();

        return struk.toString();
    }
    
    public static final String ST_MENUNGGU = "Menunggu Diproses";
    public static final String ST_DITERIMA = "Diterima";
    public static final String ST_DIPROSES = "Diproses";
    public static final String ST_SELESAI = "Selesai";
    public static final String ST_DIBATALKAN = "Dibatalkan";

    public String ubahStatusPesanan(int indexBarisTabel, String statusBaru) {
        if (indexBarisTabel < 0 || indexBarisTabel >= daftarTransaksi.size()) {
            return "Pilih pesanan terlebih dahulu!";
        }
        TransaksiPesanan t = daftarTransaksi.get(indexBarisTabel);
        String s = t.getStatus();

        if (statusBaru.equals(ST_DITERIMA) && !s.equals(ST_MENUNGGU)) {
            return "Hanya pesanan 'Menunggu Diproses' yang bisa diterima!";
        }
        if (statusBaru.equals(ST_DIPROSES) && !(s.equals(ST_MENUNGGU) || s.equals(ST_DITERIMA))) {
            return "Hanya pesanan 'Menunggu Diproses' atau 'Diterima' yang bisa diproses!";
        }
        if (statusBaru.equals(ST_SELESAI) && !s.equals(ST_DIPROSES)) {
            return "Pesanan harus diproses dulu sebelum diselesaikan!";
        }
        if (statusBaru.equals(ST_DIBATALKAN)) {
            if (s.equals(ST_SELESAI) || s.equals(ST_DIBATALKAN)) {
                return "Pesanan yang sudah selesai / dibatalkan tidak bisa dibatalkan!";
            }
            for (ItemPesanan item : t.getItems()) {
                item.getMenu().tambahStok(item.getJumlah());   // kembalikan stok
            }
            simpanMenu();
            totalPendapatan -= t.getTotal();                    // kembalikan pendapatan
        }
        t.setStatus(statusBaru);
        try { Database.updateOrderStatus(t.getIdTransaksi(), statusBaru); }
        catch (SQLException ex) { return "Status berubah di layar, tetapi gagal disimpan: " + ex.getMessage(); }
        return "OK";
    }

    public String tambahMenuBaru(String kategori, String nama, int harga, int stokAwal) {
        if (nama == null || nama.trim().isEmpty()) {
            return "Nama menu tidak boleh kosong!";
        }
        if (harga <= 0 || stokAwal < 0) {
            return "Harga dan stok harus valid!";
        }
        Menu menuBaru = "Makanan".equalsIgnoreCase(kategori)
                ? new Makan(nama.trim(), harga, stokAwal)
                : new Minum(nama.trim(), harga, stokAwal);
        if (!daftarMenu.tambahMenu(menuBaru)) return "Daftar menu sudah penuh!";
        return simpanMenu() ? "OK" : "Menu berubah di memori, tetapi gagal disimpan ke database.";
    }

    public String ubahMenu(int index, String nama, int harga, int stok) {
        Menu menu = daftarMenu.getMenuAt(index);
        if (menu == null) return "Pilih menu dari tabel terlebih dahulu!";
        if (nama == null || nama.trim().isEmpty()) return "Nama menu tidak boleh kosong!";
        if (harga <= 0 || stok < 0) return "Harga harus lebih dari 0 dan stok tidak boleh minus!";
        menu.setNama(nama.trim());
        menu.setHarga(harga);
        menu.setStok(stok);
        return simpanMenu() ? "OK" : "Gagal menyimpan perubahan ke database.";
    }

    public String hapusMenu(int index) {
        if (daftarMenu.getMenuAt(index) == null) return "Pilih menu dari tabel terlebih dahulu!";
        daftarMenu.hapusMenu(index);
        return simpanMenu() ? "OK" : "Gagal menyimpan perubahan ke database.";
    }

    public String tambahStokMenu(int indexBarisTabel, int tambahanStok) {
        Menu m = daftarMenu.getMenuAt(indexBarisTabel);
        if (m == null) return "Pilih menu dari tabel terlebih dahulu!";
        if (tambahanStok <= 0) return "Jumlah tambahan stok minimal 1!";
        m.tambahStok(tambahanStok);
        return simpanMenu() ? "OK" : "Gagal menyimpan perubahan ke database.";
    }

    public String setStokMenu(int indexBarisTabel, int stokBaru) {
        Menu m = daftarMenu.getMenuAt(indexBarisTabel);
        if (m == null) return "Pilih menu dari tabel terlebih dahulu!";
        if (stokBaru < 0) return "Stok tidak boleh minus!";
        m.setStok(stokBaru);
        return simpanMenu() ? "OK" : "Gagal menyimpan perubahan ke database.";
    }

    private boolean simpanMenu() {
        try { Database.saveMenu(java.util.Arrays.asList(daftarMenu.getSemuaMenu())); return true; }
        catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public void muatUlangMenu() {
        try {
            List<Menu> menus = Database.loadMenu();
            daftarMenu.gantiSemua(menus.toArray(new Menu[0]));
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public void muatUlangTransaksi() {
        try {
            daftarTransaksi = Database.loadOrders();
            totalPendapatan = 0;
            for (TransaksiPesanan t : daftarTransaksi) if (!ST_DIBATALKAN.equals(t.getStatus())) totalPendapatan += t.getTotal();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private boolean semuaAngka(String teks) {
        if (teks == null || teks.trim().isEmpty()) return false;
        String s = teks.trim();
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }
}
