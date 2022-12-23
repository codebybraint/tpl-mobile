package com.example.baktiar.myapplication.Model;

import android.app.AlertDialog;

import com.example.baktiar.myapplication.Control.GrafikBarActivity;
import com.example.baktiar.myapplication.Control.GrafikLineActivity;
import com.example.baktiar.myapplication.Control.LaporanHarianActivity;
import com.example.baktiar.myapplication.Control.LaporanOmzetActivity;
import com.example.baktiar.myapplication.Control.LaporanOmzetPieActivity;
import com.example.baktiar.myapplication.Control.LaporanPenjualanActivity;
import com.example.baktiar.myapplication.Control.PemakaianBarangActivity;
import com.example.baktiar.myapplication.Control.PerbandinganPemakianBahanActivity;
import com.example.baktiar.myapplication.Control.PieChartDetailActivity;
import com.example.baktiar.myapplication.Control.ProduksiActivity;
import com.example.baktiar.myapplication.Control.Produksi_Mesin_Activity;
import com.example.baktiar.myapplication.Control.RankingChartActivity;
import com.example.baktiar.myapplication.Control.RegisterActivity;
import com.example.baktiar.myapplication.Control.NotificationServiceSyncInterval;
import com.example.baktiar.myapplication.Control.ReturActivity;
import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by Baktiar Krisna on 17/05/2018.
 */

public class Caller extends Thread {
    public CallSoap cs;
    public String strUsername, strPassword, strQuery, bTBM, strCompany, strArea, strRole, strParamAct, strCity, strParam, strFlagParam;
    public AlertDialog ad;
    public String strJenisData, strJenisSatuan, strPPN, strDist, strGudang, strBulanAwal, strBulanAKhir, strTahun, strPenjualanTBM, strSales, strBulan, strTglAwal, strTglAkhir, strFlagTgl, strProv;
    public Integer iGudang;

    public void run() {
        try {
            cs = new CallSoap();
//            String resp = cs.Call(strUsername, bTBM, strQuery);
//            String resp_reg = cs.Register(strCompany, strQuery);
//            Produksi_Mesin_Activity.rslt = resp;
//            GrafikBarActivity.rslt = resp;
////            PieChartDetailActivity.rslt = resp;
////            LaporanOmzetActivity.rslt = resp;
//            ProduksiActivity.rslt = resp;
////            GrafikLineActivity.rslt = resp;
////            LaporanPenjualanActivity.rslt = resp;
//            LaporanHarianActivity.rslt = resp;
//            NotificationServiceSyncInterval.rslt = resp;
//            LaporanOmzetPieActivity.rslt = resp;
////            RegisterActivity.rslt = resp_reg;
//            PerbandinganPemakianBahanActivity.rslt = resp;
//            PemakaianBarangActivity.rslt = resp;
//            RankingChartActivity.rslt = resp;
//            ReturActivity.rslt = resp;
//            ViewLaporanPenjualanPie(String strCompany, String strRole, String strArea,  String strJenisSatuan, Integer iGudang, String strGudang, String strBulan, String strTahun, String strPenjualanTBM, String strSales)
//            (ByVal strCompany As String, ByVal strRole As String, ByVal strArea As String, ByVal strJenisData As String, ByVal strJenisSatuan As String, ByVal strPPN As String, ByVal iGudang As Integer, ByVal strGudang As String, ByVal strTglAwal As String, ByVal strTglAkhir As String, ByVal strFlagTgl As String, ByVal strPenjualanTBM As String, ByVal strSales As String, ByVal strProv As String) As String
//            if (strParamAct.equals("LoadListBar")) {
//                LaporanOmzetActivity.rsltChart = cs.ViewLaporanPenjualanDist(strCompany, strRole, strArea, strJenisData, strJenisSatuan, strPPN, iGudang, strGudang, strTglAwal, strTglAkhir, strFlagTgl, strPenjualanTBM, strSales, strProv);
//            } else if (strParamAct.equals("LoadAwalOmzet")) {
//                LaporanOmzetActivity.rsltOmzet = cs.LoadLaporanPenjualanDist(strCompany, strRole, strArea);
//            } else if (strParam.equals("LoadListLinePie")) {
//                PieChartDetailActivity.rslt = cs.ViewLaporanPenjualanPie(strCompany, strRole, strArea, strJenisSatuan, iGudang, strGudang, strBulan, strTahun, strPenjualanTBM, strSales);
//            } else if (strParam.equals("LoadListBarPie")) {
//                PieChartDetailActivity.rslt = cs.ViewLaporanPenjualanDistPie(strCompany, strRole, strArea, strJenisSatuan, iGudang, strGudang, strTglAwal, strTglAkhir, strFlagTgl, strPenjualanTBM, strSales, strCity, strFlagParam, strParam);
//            } else if (strParam.equals("RegisterAct")) {
//                RegisterActivity.rslt = cs.LoadDataRegister();
//            } else if (strParam.equals("LaporanPenjualanAct")) {
//                LaporanPenjualanActivity.rslt = cs.LoadLaporanPenjualan(strCompany, strRole, strArea);
//            } else if (strParam.equals("GrafikLineAct")) {
//                GrafikLineActivity.rslt = cs.ViewLaporanPenjualan(strCompany, strRole, strArea, strJenisData, strJenisSatuan, strPPN, strDist, iGudang, strGudang, strBulanAwal, strBulanAKhir, strTahun, strPenjualanTBM);
//            }
        } catch (Exception ex) {


//            Produksi_Mesin_Activity.rslt = ex.toString();
//            GrafikBarActivity.rslt = ex.toString();
//            NotificationServiceSyncInterval.rslt = ex.toString();
////            PieChartDetailActivity.rslt = ex.toString();
////            LaporanOmzetActivity.rslt = ex.toString();
//            ProduksiActivity.rslt = ex.toString();
////            GrafikLineActivity.rslt = ex.toString();
////            LaporanPenjualanActivity.rslt = ex.toString();
//            RankingChartActivity.rslt = ex.toString();
//            LaporanHarianActivity.rslt = ex.toString();
////            RegisterActivity.rslt = ex.toString();
//            PerbandinganPemakianBahanActivity.rslt = ex.toString();
//            PemakaianBarangActivity.rslt = ex.toString();
//            LaporanOmzetPieActivity.rslt = ex.toString();
//            ReturActivity.rslt = ex.toString();

//            if (strParamAct.equals("LoadListBar")) {
//                LaporanOmzetActivity.rsltChart = ex.toString();
//            } else if (strParamAct.equals("LoadAwalOmzet")) {
//                LaporanOmzetActivity.rsltOmzet = ex.toString();
//            } else if (strParamAct.equals("LoadListLinePie") | strParamAct.equals("LoadListBarPie")) {
//                PieChartDetailActivity.rslt = ex.toString();
//            } else if (strParam.equals("RegisterAct")) {
//                RegisterActivity.rslt = ex.toString();
//            } else if (strParam.equals("LaporanPenjualanAct")) {
//                LaporanPenjualanActivity.rslt = ex.toString();
//            } else if (strParam.equals("GrafikLineAct")) {
//                GrafikLineActivity.rslt = ex.toString();
//            }
        }
    }


}