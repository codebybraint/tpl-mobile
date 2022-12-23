package com.example.baktiar.myapplication.Model;

/**
 * Created by Baktiar Krisna on 22/10/2020.
 */

public class Departemen {

    String GudangName;
    String DepName;
    int Gudang_Key;
    int Jns_Key;
    int Dep_Key;


    public Departemen(String gudangName, String depName, int gudang_Key, int jns_Key, int dep_Key) {
        GudangName = gudangName;
        DepName = depName;
        Gudang_Key = gudang_Key;
        Jns_Key = jns_Key;
        Dep_Key = dep_Key;
    }

    public String getGudangName() {
        return GudangName;
    }

    public void setGudangName(String gudangName) {
        GudangName = gudangName;
    }

    public String getDepName() {
        return DepName;
    }

    public void setDepName(String depName) {
        DepName = depName;
    }

    public int getGudang_Key() {
        return Gudang_Key;
    }

    public void setGudang_Key(int gudang_Key) {
        Gudang_Key = gudang_Key;
    }

    public int getJns_Key() {
        return Jns_Key;
    }

    public void setJns_Key(int jns_Key) {
        Jns_Key = jns_Key;
    }

    public int getDep_Key() {
        return Dep_Key;
    }

    public void setDep_Key(int dep_Key) {
        Dep_Key = dep_Key;
    }



}
