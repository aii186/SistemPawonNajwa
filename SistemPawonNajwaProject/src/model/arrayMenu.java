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

    public arrayMenu(int ukuran) {
        this.listMenu = new Menu[ukuran];
    }

    public void isiMenu() {
        this.listMenu[this.jumlahAwal++] = new Makan("Indomie Goreng", 12000, 25);
        this.listMenu[this.jumlahAwal++] = new Makan("Indomie Kuah", 10000, 20);
        this.listMenu[this.jumlahAwal++] = new Makan("Nasi Goreng", 15000, 15);
        this.listMenu[this.jumlahAwal++] = new Makan("Mie Dok-Dok", 14000, 18);

        this.listMenu[this.jumlahAwal++] = new Minum("Es Teh", 5000, 30);
        this.listMenu[this.jumlahAwal++] = new Minum("Kopi Hitam", 4000, 25);
        this.listMenu[this.jumlahAwal++] = new Minum("Es Jeruk", 6000, 20);
    }

    public boolean tambahMenu(Menu menuBaru) {
        if (this.jumlahAwal >= this.listMenu.length) {
            Menu[] baru = new Menu[this.listMenu.length + 10];
            System.arraycopy(this.listMenu, 0, baru, 0, this.listMenu.length);
            this.listMenu = baru;
        }
        this.listMenu[this.jumlahAwal++] = menuBaru;
        return true;
    }

    public Menu[] getSemuaMenu() {
        Menu[] hasil = new Menu[this.jumlahAwal];
        System.arraycopy(this.listMenu, 0, hasil, 0, this.jumlahAwal);
        return hasil;
    }

    public Menu getMenuAt(int index) {
        if (index >= 0 && index < this.jumlahAwal) {
            return this.listMenu[index];
        }
        return null;
    }

    public Menu[] getDaftarMakanan() {
        int banyak = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (this.listMenu[i] instanceof Makan) banyak++;
        }
        Menu[] hasil = new Menu[banyak];
        int index = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (this.listMenu[i] instanceof Makan) hasil[index++] = this.listMenu[i];
        }
        return hasil;
    }

    public Menu[] getDaftarMinuman() {
        int banyak = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (this.listMenu[i] instanceof Minum) banyak++;
        }
        Menu[] hasil = new Menu[banyak];
        int index = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (this.listMenu[i] instanceof Minum) hasil[index++] = this.listMenu[i];
        }
        return hasil;
    }

    public Menu pilihMakanan(int pilihan) {
        int nomor = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (this.listMenu[i] instanceof Makan) {
                nomor++;
                if (nomor == pilihan) return this.listMenu[i];
            }
        }
        return null;
    }

    public Menu pilihMinuman(int pilihan) {
        int nomor = 0;
        for (int i = 0; i < this.jumlahAwal; i++) {
            if (this.listMenu[i] instanceof Minum) {
                nomor++;
                if (nomor == pilihan) return this.listMenu[i];
            }
        }
        return null;
    }
}
