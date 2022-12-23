package com.example.baktiar.myapplication.Model;

/**
 * Created by BK on 12/8/2017.
 */

public class User {
    public User(String email, String password, String name, String phone, String apk, String role, String tbm_key, String taa_key, String tpi_key) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.apk = apk;
        this.role = role;
        this.tbm_key = tbm_key;
        this.taa_key = taa_key;
        this.tpi_key = tpi_key;
    }

    private String email;
    private String password;
    private String name;
    private String phone;
    private String apk;
    private String role;
    private String tbm_key;
    private String taa_key;
    private String tpi_key;
    public String getTbm_key() {
        return tbm_key;
    }

    public void setTbm_key(String tbm_key) {
        this.tbm_key = tbm_key;
    }

    public String getTaa_key() {
        return taa_key;
    }

    public void setTaa_key(String taa_key) {
        this.taa_key = taa_key;
    }

    public String getTpi_key() {
        return tpi_key;
    }

    public void setTpi_key(String tpi_key) {
        this.tpi_key = tpi_key;
    }


//    public User(String email, String password, String name, String phone,String apk, String role) {
//        this.email = email;
//        this.password = password;
//        this.name = name;
//        this.phone = phone;
//        this.apk = apk;
//        this.role = role;
//    }



    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
