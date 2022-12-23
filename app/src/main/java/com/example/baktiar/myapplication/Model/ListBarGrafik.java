package com.example.baktiar.myapplication.Model;

/**
 * Created by TRIPILAR on 23/04/2019.
 */

public class ListBarGrafik {
    public String ProvinsiName;
    public String SalesName;
    public String City_Name;
    Long Total;

    public ListBarGrafik(String provinsiName, String salesName, String city_Name, Long total) {
        ProvinsiName = provinsiName;
        SalesName = salesName;
        City_Name = city_Name;
        Total = total;
    }

    public String getProvinsiName() {
        return ProvinsiName;
    }

    public void setProvinsiName(String provinsiName) {
        ProvinsiName = provinsiName;
    }

    public String getSalesName() {
        return SalesName;
    }

    public void setSalesName(String salesName) {
        SalesName = salesName;
    }

    public String getCity_Name() {
        return City_Name;
    }

    public void setCity_Name(String city_Name) {
        City_Name = city_Name;
    }

    public Long getTotal() {
        return Total;
    }

    public void setTotal(Long total) {
        Total = total;
    }
}
