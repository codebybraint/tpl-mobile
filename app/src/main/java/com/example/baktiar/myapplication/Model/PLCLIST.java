package com.example.baktiar.myapplication.Model;

/**
 * Created by TRIPILAR on 26/10/2018.
 */

public class PLCLIST {
    String Tanggal;
    double Total,Asbes,Semen,Kertas,Silica;

    public PLCLIST(String tanggal, double total, double asbes, double semen, double kertas, double silica) {
        Tanggal = tanggal;
        Total = total;
        Asbes = asbes;
        Semen = semen;
        Kertas = kertas;
        Silica = silica;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public double getAsbes() {
        return Asbes;
    }

    public void setAsbes(double asbes) {
        Asbes = asbes;
    }

    public double getSemen() {
        return Semen;
    }

    public void setSemen(double semen) {
        Semen = semen;
    }

    public double getKertas() {
        return Kertas;
    }

    public void setKertas(double kertas) {
        Kertas = kertas;
    }

    public double getSilica() {
        return Silica;
    }

    public void setSilica(double silica) {
        Silica = silica;
    }
}
