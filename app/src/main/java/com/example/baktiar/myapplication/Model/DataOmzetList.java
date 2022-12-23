package com.example.baktiar.myapplication.Model;

/**
 * Created by Baktiar Krisna on 29/03/2018.
 */

public class DataOmzetList {
    String SalesName;
    Long Total;

    public String getSalesName() {
        return SalesName;
    }

    public Long getTotal() {
        return Total;
    }

    public DataOmzetList(String salesName, Long total) {
        this.SalesName = salesName;
        this.Total = total;
    }
}
