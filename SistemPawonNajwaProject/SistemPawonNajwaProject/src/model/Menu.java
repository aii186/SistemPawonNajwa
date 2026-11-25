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
    private int databaseId;

    public Menu() {
    }

    public Menu(String nama, int harga, int stok) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok < 0 ? 0 : stok;
    }

    public void setNama(String nama) { this.nama = nama; }
    public void setHarga(int harga) { this.harga = harga; }
    public String getNama() { return this.nama; }
    public int getHarga() { return this.harga; }

    public int getStok() { return this.stok; }
    public int getDatabaseId() { return databaseId; }
    public void setDatabaseId(int databaseId) { this.databaseId = databaseId; }

    public void setStok(int stok) { this.stok = stok; }
    
    public void tambahStok(int jumlah) {
        if (jumlah > 0) {
            this.stok += jumlah;
        }
    }

    public void kurangiStok(int jumlah) {
        this.stok -= jumlah;
        if (this.stok < 0) {
            this.stok = 0;
        }
    }
}
