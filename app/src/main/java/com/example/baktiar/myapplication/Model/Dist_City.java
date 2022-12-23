package com.example.baktiar.myapplication.Model;

import java.io.Serializable;

/**
 * Created by TRIPILAR on 25/04/2019.
 */

public class Dist_City implements Serializable{

    String DistName;
    String CityName;
    Long Total;

    public Dist_City(String distName, String cityName, Long total) {
        DistName = distName;
        CityName = cityName;
        Total = total;
    }

    public String getDistName() {
        return DistName;
    }

    public void setDistName(String distName) {
        DistName = distName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public Long getTotal() {
        return Total;
    }

    public void setTotal(Long total) {
        Total = total;
    }
}
