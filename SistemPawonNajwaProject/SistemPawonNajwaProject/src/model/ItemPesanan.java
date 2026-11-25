/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class ItemPesanan {
    private Menu menu;
    private int jumlah;

    public ItemPesanan(Menu menu, int jumlah) {
        this.menu = menu;
        this.jumlah = jumlah;
    }

    public Menu getMenu() { return menu; }
    public int getJumlah() { return jumlah; }
    public void tambahJumlah(int tambahan) { this.jumlah += tambahan; }
    public int getSubtotal() { return menu.getHarga() * jumlah; }
}
