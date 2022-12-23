package com.example.baktiar.myapplication.Model;

/**
 * Created by TRIPILAR on 21/11/2018.
 */

public class ReturListDecimal {
    public String getBulan() {
        return Bulan;
    }

    public void setBulan(String bulan) {
        Bulan = bulan;
    }

    public Double getProduksi() {
        return Produksi;
    }

    public void setProduksi(Double produksi) {
        Produksi = produksi;
    }

    public Double getPenjualan() {
        return Penjualan;
    }

    public void setPenjualan(Double penjualan) {
        Penjualan = penjualan;
    }

    String Bulan;
    Double Produksi;
    Double Penjualan;

    public ReturListDecimal(String bulan, Double produksi, Double penjualan) {
        Bulan = bulan;
        Produksi = produksi;
        Penjualan = penjualan;
    }
}
