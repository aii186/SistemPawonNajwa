/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class arrayPesanan {
    private ItemPesanan[] daftar;
    private int jumlahItem = 0;

    public arrayPesanan(int ukuran) {
        this.daftar = new ItemPesanan[ukuran];
    }

    public boolean tambah(Menu menu, int jumlah) {
        for (int i = 0; i < this.jumlahItem; i++) {
            if (this.daftar[i].getMenu() == menu) {
                this.daftar[i].tambahJumlah(jumlah);
                return true;
            }
        }
        if (this.jumlahItem >= this.daftar.length) return false;
        this.daftar[this.jumlahItem++] = new ItemPesanan(menu, jumlah);
        return true;
    }

    public int getJumlahMenu(Menu menu) {
        for (int i = 0; i < this.jumlahItem; i++) {
            if (this.daftar[i].getMenu() == menu) {
                return this.daftar[i].getJumlah();
            }
        }
        return 0;
    }
    
    public int getJumlahDipesanUntukMenu(Menu menu) {
        for (int i = 0; i < jumlahItem; i++) {
            if (daftar[i].getMenu() == menu) return daftar[i].getJumlah();
        }
            return 0;
        }

    public int getJumlahItem() { return this.jumlahItem; }
    public ItemPesanan getItem(int index) { return this.daftar[index]; }

    public void kosongkan() {
        for (int i = 0; i < this.jumlahItem; i++) {
            this.daftar[i] = null;
        }
        this.jumlahItem = 0;
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < this.jumlahItem; i++) {
            total += this.daftar[i].getSubtotal();
        }
        return total;
    }
}
