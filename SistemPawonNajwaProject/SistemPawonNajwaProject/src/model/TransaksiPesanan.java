/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author USER
 */
public class TransaksiPesanan {
    private String idTransaksi;
    private String namaPelanggan;
    private String nomorMeja;
    private String ringkasanItem;
    private int total;
    private String metodeBayar;
    private String waktu;
    private String status = "Baru";
    private java.util.List<ItemPesanan> items = new java.util.ArrayList<>();

    public TransaksiPesanan(String idTransaksi, String namaPelanggan, String nomorMeja,
                            String ringkasanItem, int total, String metodeBayar) {
        this.idTransaksi = idTransaksi;
        this.namaPelanggan = namaPelanggan;
        this.nomorMeja = nomorMeja;
        this.ringkasanItem = ringkasanItem;
        this.total = total;
        this.metodeBayar = metodeBayar;
        this.waktu = new SimpleDateFormat("HH:mm:ss").format(new Date());
        this.status = "Menunggu Diproses";
    }

    public String getIdTransaksi() { return idTransaksi; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public String getNomorMeja() { return nomorMeja; }
    public String getRingkasanItem() { return ringkasanItem; }
    public int getTotal() { return total; }
    public String getMetodeBayar() { return metodeBayar; }
    public String getWaktu() { return waktu; }
    public String getStatus() { return status; }

    public java.util.List<ItemPesanan> getItems() { return items; }
    public void setItems(java.util.List<ItemPesanan> items) { this.items = items; }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
