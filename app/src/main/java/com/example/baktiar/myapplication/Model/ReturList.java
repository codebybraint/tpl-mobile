package com.example.baktiar.myapplication.Model;

/**
 * Created by Baktiar Krisna on 02/05/2018.
 */

public class ReturList {
    String Bulan;
    Long Produksi;
    Long Penjualan;

    public String getBulan() {
        return Bulan;
    }

    public void setBulan(String bulan) {
        Bulan = bulan;
    }

    public Long getProduksi() {
        return Produksi;
    }

    public void setProduksi(Long produksi) {
        Produksi = produksi;
    }

    public Long getPenjualan() {
        return Penjualan;
    }

    public void setPenjualan(Long penjualan) {
        Penjualan = penjualan;
    }

    public ReturList(String bulan, Long produksi, Long penjualan) {
        Bulan = bulan;
        Produksi = produksi;
        Penjualan = penjualan;
    }
}
