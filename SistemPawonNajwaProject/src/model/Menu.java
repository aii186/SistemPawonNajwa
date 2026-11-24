/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class Menu {
    protected String nama;
    protected int harga;
    protected int stok;

    public Menu() {
    }

    public Menu(String nama, int harga) {
        this(nama, harga, 20);
    }

    public Menu(String nama, int harga, int stok) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public void setNama(String nama) { this.nama = nama; }
    public void setHarga(int harga) { this.harga = harga; }
    public void setStok(int stok) { this.stok = Math.max(0, stok); }

    public void tambahStok(int tambahan) {
        this.stok += tambahan;
    }

    public boolean kurangiStok(int jumlah) {
        if (jumlah > 0 && this.stok >= jumlah) {
            this.stok -= jumlah;
            return true;
        }
        return false;
    }

    public String getNama() { return this.nama; }
    public int getHarga() { return this.harga; }
    public int getStok() { return this.stok; }
}
