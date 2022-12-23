package com.example.baktiar.myapplication.Model;

/**
 * Created by Baktiar Krisna on 02/04/2018.
 */

public class DataPenjualanList {
    String Bulan;
    Long Total;

    public String getBulan() {
        return Bulan;
    }

    public Long getTotal() {
        return Total;
    }

    public DataPenjualanList(String bulan, Long total) {
        this.Bulan = bulan;
        this.Total = total;
    }
}
