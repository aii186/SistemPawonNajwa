/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class arrayMenu {
    private Menu[] listMenu;
    private int jumlahAwal = 0;
    private int ukuran;

    public arrayMenu(int ukuran) {
        this.ukuran = ukuran;
        this.listMenu = new Menu[ukuran];
    }

    public void isiMenu() {
        this.listMenu[this.jumlahAwal++] = new Makan("Indomie Goreng", 10000, 20);
        this.listMenu[this.jumlahAwal++] = new Makan("Indomie Kuah", 10000, 20);
        this.listMenu[this.jumlahAwal++] = new Makan("Mie Dok-Dok", 13000, 15);
        this.listMenu[this.jumlahAwal++] = new Makan("Mie Tek-Tek", 13000, 15);
        this.listMenu[this.jumlahAwal++] = new Makan("Nasi Goreng", 15000, 10);

        this.listMenu[this.jumlahAwal++] = new Minum("Es Teh Manis", 5000, 10);
        this.listMenu[this.jumlahAwal++] = new Minum("Es Nutrisari", 5000, 10);
        this.listMenu[this.jumlahAwal++] = new Minum("Es Kelapa Muda", 7000, 15);
        this.listMenu[this.jumlahAwal++] = new Minum("Es Extrajoss Susu", 7000, 20);
    }

    public void gantiSemua(Menu[] menus) {
        Menu[] sebelumnya = this.listMenu;
        int jumlahSebelumnya = this.jumlahAwal;
        this.listMenu = new Menu[this.ukuran];
        this.jumlahAwal = 0;
        for (Menu baru : menus) {
            if (this.jumlahAwal == this.ukuran) break;
            Menu dipakai = null;
            for (int i = 0; i < jumlahSebelumnya; i++) {
                Menu lama = sebelumnya[i];
                if (lama != null && lama.getNama().equals(baru.getNama())
                        && (lama instanceof Makan) == (baru instanceof Makan)) {
                    dipakai = lama;
                    break;
                }
            }
            if (dipakai != null) {
                dipakai.setNama(baru.getNama());
                dipakai.setHarga(baru.getHarga());
                dipakai.setStok(baru.getStok());
                dipakai.setDatabaseId(baru.getDatabaseId());
                baru = dipakai;
            }
            this.listMenu[this.jumlahAwal++] = baru;
        }
    }

    private boolean cocok(Menu m, boolean makanan) {
        return makanan ? (m instanceof Makan) : (m instanceof Minum);
    }

    private Menu[] ambilDaftar(boolean makanan) {
        int banyak = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (cocok(this.listMenu[i], makanan)) {
                banyak++;
            }
        }

        Menu[] hasil = new Menu[banyak];
        int index = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (cocok(this.listMenu[i], makanan)) {
                hasil[index++] = this.listMenu[i];
            }
        }
        return hasil;
    }

    private Menu pilih(int pilihan, boolean makanan) {
        int nomor = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (cocok(this.listMenu[i], makanan)) {
                nomor++;
                if (nomor == pilihan) {
                    return this.listMenu[i];
                }
            }
        }
        return null;
    }

    public Menu[] getDaftarMakanan() { return ambilDaftar(true); }
    public Menu[] getDaftarMinuman() { return ambilDaftar(false); }
    public Menu pilihMakanan(int pilihan) { return pilih(pilihan, true); }
    public Menu pilihMinuman(int pilihan) { return pilih(pilihan, false); }

    // ---------- Untuk Kasir/Admin ----------

    public int getJumlahMenu() {
        return this.jumlahAwal;
    }

    public Menu getMenu(int index) {
        return this.listMenu[index];
    }
    
    public Menu[] getSemuaMenu() {
        Menu[] hasil = new Menu[jumlahAwal];
        for (int i = 0; i < jumlahAwal; i++) hasil[i] = listMenu[i];
        return hasil;
    }

    public boolean tambahMenu(Menu menuBaru) {
        if (this.jumlahAwal >= this.ukuran) {
            return false;
        }
        this.listMenu[this.jumlahAwal++] = menuBaru;
        return true;
    }
    
    public Menu getMenuAt(int index) {
    if (index < 0 || index >= jumlahAwal) return null;
        return listMenu[index];
    }

    public void ubahMenu(int index, String nama, int harga) {
        this.listMenu[index].setNama(nama);
        this.listMenu[index].setHarga(harga);
    }

    public void hapusMenu(int index) {
        for (int i = index; i < this.jumlahAwal - 1; i++) {
            this.listMenu[i] = this.listMenu[i + 1];
        }
        this.listMenu[--this.jumlahAwal] = null;
    }
    
    public void tambahStok(int index, int jumlah) {
        this.listMenu[index].tambahStok(jumlah);
    }
}
