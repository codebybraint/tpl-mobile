package com.example.baktiar.myapplication.Control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.baktiar.myapplication.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PerbandinganHarianActivity extends BaseActivity {
    private SlidrInterface slidr;
    RadioGroup radioGroupOutput2;
    Button buttonShow;
    RadioButton RBMingguan, RBMTD, RBYTD;
    TextView txtBulan, txtTahun;
    Spinner spinnerTahun, spinnerBulan;
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listBulanView = new ArrayList<>();
    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    String strFlagHarian, strCompany, strFlagLaporan, strCurrentDate, strJenisData, strTglAwal, strTglAkhir, strWeekAgo, strQuery, strQuery1, strQuery2, strQuery3, strFlagTitle1, strFlagTitle2, strFlagTitle3, strFlagTitle4, strFlagTitle5;
    String strQuerySum,  strArea, strJenisSatuan, strPenjualanTBM,strQueryInnerJoin, strSatuan, strWhereAM,strWhereAMPIE,strRole, strArea_key,strSalesTBM, strWhereSalesTBM,strWhereSalesTBMPIE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perbandingan_harian);
        slidr = Slidr.attach(this);
        buttonShow = (Button) findViewById(R.id.buttonShow);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        txtBulan = (TextView) findViewById(R.id.txtBulan);
        radioGroupOutput2 = (RadioGroup) findViewById(R.id.radioGroupOutput2);
        RBMingguan = (RadioButton) findViewById(R.id.RBMingguan);
        RBMTD = (RadioButton) findViewById(R.id.RBMTD);
        RBYTD = (RadioButton) findViewById(R.id.RBYTD);
        spinnerBulan = (Spinner) findViewById(R.id.spinnerBulan);
        spinnerTahun = (Spinner) findViewById(R.id.spinnerTahun);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Laporan harian");
        }
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition();
            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar currentCal = Calendar.getInstance();
        strCurrentDate = dateFormat.format(currentCal.getTime());
        currentCal.add(Calendar.DATE, -6);
        strWeekAgo = dateFormat.format(currentCal.getTime());

        System.out.println("TODAY " + strCurrentDate);
        System.out.println("TODAY2 " + strWeekAgo);
        strFlagHarian = getIntent().getStringExtra("flagHarian");
        strRole = getIntent().getStringExtra("role");
        strSalesTBM = getIntent().getStringExtra("salesTBM");
        strArea = getIntent().getStringExtra("strArea");
        strJenisSatuan = getIntent().getStringExtra("strJenisSatuan");
        strPenjualanTBM = getIntent().getStringExtra("strPenjualanTBM");
        strCompany = getIntent().getStringExtra("strCompany");

//        intent.putExtra("strCompany", strCompany);
//        intent.putExtra("strRole", strRole);
//        intent.putExtra("strArea", strArea_key);
//        intent.putExtra("strJenisSatuan", strFlagHarian);
//        intent.putExtra("strPenjualanTBM", strPenjualanTBM);

        if(strSalesTBM.equals("TRUE")){
            strWhereSalesTBM = " AND  D.Sales_Key not in (31,36) ";
            strWhereSalesTBMPIE =  " AND  G.Sales_Key not in (31,36) ";
        }else {
            strWhereSalesTBM = " ";
            strWhereSalesTBMPIE = " ";
        }
        if (strRole.equals("Area Manager")) {
            strArea_key = getIntent().getStringExtra("area_key");
            strWhereAM = "\t  AND ((D.AM_Key =" + strArea_key + "  AND D.isMultiAM=0) OR (F.AM_Key =" + strArea_key + " AND D.isMultiAM=1))\n";
            strWhereAMPIE =  "\t  AND ((G.AM_Key =" +strArea_key+ " AND G.isMultiAM=0) OR (I.AM_Key ="+strArea_key+"  AND G.isMultiAM=1))\n";
        } else {
            strWhereAM = " ";
            strWhereAMPIE = " ";
        }
        invisible();
        loadBulan();

        radioGroupOutput2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBMingguan) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    spinnerBulan.setVisibility(View.INVISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    strFlagLaporan = "Mingguan";
                } else if (i == R.id.RBMTD) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    spinnerBulan.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.VISIBLE);
                    strFlagLaporan = "Bulanan";
                } else if (i == R.id.RBYTD) {
                    spinnerTahun.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    spinnerBulan.setVisibility(View.INVISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    strFlagLaporan = "Tahunan";
                }
            }
        });
    }

    public void invisible() {
        spinnerTahun.setVisibility(View.INVISIBLE);
        txtTahun.setVisibility(View.INVISIBLE);
        spinnerBulan.setVisibility(View.VISIBLE);
        txtBulan.setVisibility(View.VISIBLE);
        RBMTD.setChecked(true);
        strFlagLaporan = "Bulanan";

    }

    public void condition() {

        // System.out.println("BEaut Proteksi= " + strFlagLaporan);
        if (strFlagHarian.equals("Gross") == true) {
            strQuerySum = "sum((HarSat*jumlah*1.1)/1000000)  as Total";
            strQueryInnerJoin = " INNER JOIN mJualItem B on A.JLFaktur=B.JLFaktur INNER JOIN mProducts C on B.Brg_Key=C.Brg_Key ";
            strSatuan = "juta rupiah";
        } else if (strFlagHarian.equals("Nota") == true) {
            strQuerySum = "count(*) Total";
            strQueryInnerJoin = " ";
            strSatuan = "lembar";
        } else if (strFlagHarian.equals("Lembar") == true) {
            strQuerySum = "sum(jumlah) as Total";
            strQueryInnerJoin = " INNER JOIN mJualItem B on A.JLFaktur=B.JLFaktur INNER JOIN mProducts C on B.Brg_Key=C.Brg_Key ";
            strSatuan = "lembar";
        } else if (strFlagHarian.equals("Tonase") == true) {
            strQuerySum = "(sum(jumlah*brg_berat))/1000 as Total";
            strQueryInnerJoin = " INNER JOIN mJualItem B on A.JLFaktur=B.JLFaktur INNER JOIN mProducts C on B.Brg_Key=C.Brg_Key ";
            strSatuan = "ton";
        }
        if (strFlagLaporan == "Mingguan") {

            strQuery = String.format("SELECT CONVERT(date,JLTanggal) as Tanggal, %s  \n" +
                    "FROM mJualNota A\n" +
                    " %s\n" +
                    "INNER JOIN mSalesman D ON A.Sales_Key=D.Sales_Key\n" +
                    "LEFT JOIN mAM E ON D.AM_Key=E.AM_Key\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    ") F ON A.CustAddr_Key=F.CustAddr_Key\n" +
                    "WHERE JLTanggal between '%s' and '%s'and ISA2=0 %s %s\n" +
                    "GROUP BY JLTanggal\n" +
                    "order by Tanggal", strQuerySum, strQueryInnerJoin, strWeekAgo, strCurrentDate,strWhereAM,strWhereSalesTBM);
     strTglAwal = strWeekAgo;
            strFlagTitle1 = "Laporan Mingguan";
            // strFlagTitle2 = String.format("Perbandingan %s (%s)",strFlagHarian,strSatuan);
        } else if (strFlagLaporan == "Bulanan") {

            strQuery = String.format("SELECT DATEPART(month, JLTanggal) TanggalNo,datename(month,JLTanggal) as Tanggal, %s  \n" +
                    "FROM mJualNota A\n" +
                    " %s\n" +
                    "INNER JOIN mSalesman D ON A.Sales_Key=D.Sales_Key\n" +
                    "LEFT JOIN mAM E ON D.AM_Key=E.AM_Key\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    ") F ON A.CustAddr_Key=F.CustAddr_Key\n" +
                    "WHERE JLTanggal between '%s/1/%s' and '%s'and ISA2=0 %s %s\n" +
                    "GROUP BY DATEPART(month, JLTanggal),datename(month,JLTanggal)\n" +
                    "order by TanggalNo",strQuerySum, strQueryInnerJoin, String.valueOf(spinnerBulan.getSelectedItemPosition() + 1).toString(), Calendar.getInstance().get(Calendar.YEAR), strCurrentDate,strWhereAM,strWhereSalesTBM);
            System.out.println("MONTH TO "+ strQuery);
            strFlagTitle1 = "Laporan Bulanan";
            strTglAwal = String.format("%s/1/%s",String.valueOf(spinnerBulan.getSelectedItemPosition() + 1).toString(),Calendar.getInstance().get(Calendar.YEAR));
            //  strFlagTitle2 = String.format("Perbandingan %s (%s)",strFlagHarian,strSatuan);
        } else if (strFlagLaporan == "Tahunan") {

            strQuery = String.format("SELECT DATEPART(year, JLTanggal) Tanggal, %s  \n" +
                    "FROM mJualNota A\n" +
                    " %s\n" +
                    "INNER JOIN mSalesman D ON A.Sales_Key=D.Sales_Key\n" +
                    "LEFT JOIN mAM E ON D.AM_Key=E.AM_Key\n" +
                    "LEFT JOIN (\n" +
                    "\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    ") F ON A.CustAddr_Key=F.CustAddr_Key\n" +
                    "WHERE JLTanggal between '1/1/%s' and '%s'and ISA2=0 %s %s\n" +
                    "GROUP BY DATEPART(year, JLTanggal)\n" +
                    "order by Tanggal", strQuerySum, strQueryInnerJoin, spinnerTahun.getSelectedItem(), strCurrentDate, strWhereAM, strWhereSalesTBM);

            strFlagTitle1 = "Laporan Tahunan";
strTglAwal = String.format("1/1/%s",spinnerTahun.getSelectedItem());
        }
        strFlagTitle2 = String.format("Perbandingan %s (%s) - %s company (DO Date)", strFlagHarian, strSatuan, strCompany);


        condition4();
        Intent intent = new Intent(PerbandinganHarianActivity.this, GrafikBarActivity.class);
        intent.putExtra("query", strQuery);
        intent.putExtra("query1", strQuery1);
        intent.putExtra("query2", strQuery2);
        intent.putExtra("query3", strQuery3);
        intent.putExtra("flagTitle1", strFlagTitle1);
        intent.putExtra("flagTitle2", strFlagTitle2);
        intent.putExtra("flagRBHarian", strFlagLaporan);
        intent.putExtra("flagActivity", "LaporanHarian");
        intent.putExtra("flagTitle3", strFlagTitle3);
        intent.putExtra("flagTitle4", strFlagTitle4);
        intent.putExtra("flagTitle5", strFlagTitle5);

        intent.putExtra("strFlagTgl", strFlagLaporan);
        intent.putExtra("strCompany", strCompany);
        intent.putExtra("strRole", strRole);
        intent.putExtra("strArea", strArea);
        intent.putExtra("strJenisData", strFlagLaporan);
        intent.putExtra("strJenisSatuan", strJenisSatuan);
        intent.putExtra("strTglAwal", strTglAwal);
        intent.putExtra("strTglAkhir", strCurrentDate);
        intent.putExtra("strPenjualanTBM", strPenjualanTBM);
;
        startActivity(intent);

        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void condition4() {
        if (strFlagLaporan == "Mingguan") {

            strQuery1 ="WITH cte AS (\n" +
                    "      SELECT ROW_NUMBER() OVER (ORDER BY (SUM(Jumlah)) DESC) AS num, \n" +
                    "      Kat_Kelompok as BULAN, ISNULL(SUM(Jumlah),0) AS Total\n" +
                    "      FROM mJUalItem A \n" +
                    "      INNER JOIN mJualNota B ON A.JLFaktur=B.JLFaktur\n" +
                    "      INNER JOIN mProducts C ON A.Brg_Key=C.Brg_Key\n" +
                    "      INNER JOIN mKat D ON C.Kat_Key=D.Kat_Key\n" +
                    "      INNER JOIN mSalesman G ON B.Sales_Key=G.Sales_Key\n" +
                    "      LEFT JOIN mAM H ON G.AM_Key=H.AM_Key\n" +
                    "      LEFT JOIN (\n" +
                    "\t\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\t\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\t\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\t\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    "\t) I ON B.CustAddr_Key=I.CustAddr_Key\n" +
                    "      WHERE  ISA2=0 and datediff(day, tglFaktur, '";
            strQuery3="WITH cte AS (\n" +
                    "      SELECT ROW_NUMBER() OVER (ORDER BY (SUM(Jumlah)) DESC) AS num, \n" +
                    "      Kat_Kelompok as BULAN, ISNULL(SUM(Jumlah*brg_berat),0) AS Total\n" +
                    "      FROM mJUalItem A \n" +
                    "      INNER JOIN mJualNota B ON A.JLFaktur=B.JLFaktur\n" +
                    "      INNER JOIN mProducts C ON A.Brg_Key=C.Brg_Key\n" +
                    "      INNER JOIN mKat D ON C.Kat_Key=D.Kat_Key\n" +
                    "      INNER JOIN mSalesman G ON B.Sales_Key=G.Sales_Key\n" +
                    "      LEFT JOIN mAM H ON G.AM_Key=H.AM_Key\n" +
                    "      LEFT JOIN (\n" +
                    "\t\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\t\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\t\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\t\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    "\t) I ON B.CustAddr_Key=I.CustAddr_Key\n" +
                    "      WHERE  ISA2=0 and datediff(day, tglFaktur, '";
            strQuery2 = "')=0 and Kat_Kelompok !='GP' " + strWhereAMPIE+ strWhereSalesTBMPIE+
                    "\n  GROUP BY Kat_Kelompok\n" +
                    "    )\n" +
                    "    SELECT BULAN, Total FROM cte WHERE num BETWEEN 1 AND 4 \n" +
                    "    UNION ALL\n" +
                    "    SELECT  BULAN='Other', ISNULL(SUM(Total),0) FROM cte WHERE num > 4 ";
            strFlagTitle3 = "Komposisi penjualan";
            strFlagTitle4 = "Semua Distributor Tanggal ";
            strFlagTitle5 = "  (DO Date)";
        } else if (strFlagLaporan == "Bulanan") {
            strQuery1 ="WITH cte AS (\n" +
                    "      SELECT ROW_NUMBER() OVER (ORDER BY (SUM(Jumlah)) DESC) AS num, \n" +
                    "      Kat_Kelompok as BULAN, isnull(SUM(isnull(Jumlah,0)),0) AS Total\n" +
                    "      FROM mJUalItem A \n" +
                    "      INNER JOIN mJualNota B ON A.JLFaktur=B.JLFaktur\n" +
                    "      INNER JOIN mProducts C ON A.Brg_Key=C.Brg_Key\n" +
                    "      INNER JOIN mKat D ON C.Kat_Key=D.Kat_Key\n" +
                    "      INNER JOIN mSalesman G ON B.Sales_Key=G.Sales_Key\n" +
                    "      LEFT JOIN mAM H ON G.AM_Key=H.AM_Key\n" +
                    "      LEFT JOIN (\n" +
                    "\t\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\t\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\t\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\t\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    "\t) I ON B.CustAddr_Key=I.CustAddr_Key\n" +
                    "      WHERE  ISA2=0 and  datename(month, TglFaktur)='";
            strQuery3 ="WITH cte AS (\n" +
                    "      SELECT ROW_NUMBER() OVER (ORDER BY (SUM(Jumlah)) DESC) AS num, \n" +
                    "      Kat_Kelompok as BULAN, isnull(SUM(isnull(Jumlah*brg_berat,0)),0) AS Total\n" +
                    "      FROM mJUalItem A \n" +
                    "      INNER JOIN mJualNota B ON A.JLFaktur=B.JLFaktur\n" +
                    "      INNER JOIN mProducts C ON A.Brg_Key=C.Brg_Key\n" +
                    "      INNER JOIN mKat D ON C.Kat_Key=D.Kat_Key\n" +
                    "      INNER JOIN mSalesman G ON B.Sales_Key=G.Sales_Key\n" +
                    "      LEFT JOIN mAM H ON G.AM_Key=H.AM_Key\n" +
                    "      LEFT JOIN (\n" +
                    "\t\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\t\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\t\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\t\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    "\t) I ON B.CustAddr_Key=I.CustAddr_Key\n" +
                    "      WHERE  ISA2=0 and  datename(month, TglFaktur)='";
            strQuery2 = "' and year(TglFaktur)='" + Calendar.getInstance().get(Calendar.YEAR) + "' and ISA2=0 and Kat_Kelompok !='GP' " +strWhereAMPIE+ strWhereSalesTBMPIE+
                    "\nGROUP BY Kat_Kelompok\n" +
                    ")\n" +
                    "SELECT BULAN, Total FROM cte WHERE num BETWEEN 1 AND 4\n" +
                    "UNION ALL\n" +
                    "SELECT  BULAN='Other', isnull(SUM(isnull(Total,0)),0) FROM cte WHERE num > 4";
            strFlagTitle3 = "Komposisi penjualan";
            strFlagTitle4 = "Semua Distributor Bulan ";
            strFlagTitle5 = " Tahun " + Calendar.getInstance().get(Calendar.YEAR) +" (DO Date)";

        } else if (strFlagLaporan == "Tahunan") {

            strQuery1 = "WITH cte AS (\n" +
                    "      SELECT ROW_NUMBER() OVER (ORDER BY (SUM(Jumlah)) DESC) AS num, \n" +
                    "      Kat_Kelompok as BULAN, ISNULL(SUM(Jumlah),0) AS Total\n" +
                    "      FROM mJUalItem A \n" +
                    "      INNER JOIN mJualNota B ON A.JLFaktur=B.JLFaktur\n" +
                    "      INNER JOIN mProducts C ON A.Brg_Key=C.Brg_Key\n" +
                    "      INNER JOIN mKat D ON C.Kat_Key=D.Kat_Key\n" +
                    "      INNER JOIN mSalesman G ON B.Sales_Key=G.Sales_Key\n" +
                    "      LEFT JOIN mAM H ON G.AM_Key=H.AM_Key\n" +
                    "      LEFT JOIN (\n" +
                    "\t\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\t\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\t\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\t\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    "\t) I ON B.CustAddr_Key=I.CustAddr_Key\n" +
                    "      WHERE  ISA2=0 and datediff(year, tglFaktur, '1/31/";
            strQuery3 = "WITH cte AS (\n" +
                    "      SELECT ROW_NUMBER() OVER (ORDER BY (SUM(Jumlah)) DESC) AS num, \n" +
                    "      Kat_Kelompok as BULAN, ISNULL(SUM(Jumlah*brg_berat),0) AS Total\n" +
                    "      FROM mJUalItem A \n" +
                    "      INNER JOIN mJualNota B ON A.JLFaktur=B.JLFaktur\n" +
                    "      INNER JOIN mProducts C ON A.Brg_Key=C.Brg_Key\n" +
                    "      INNER JOIN mKat D ON C.Kat_Key=D.Kat_Key\n" +
                    "      INNER JOIN mSalesman G ON B.Sales_Key=G.Sales_Key\n" +
                    "      LEFT JOIN mAM H ON G.AM_Key=H.AM_Key\n" +
                    "      LEFT JOIN (\n" +
                    "\t\tSELECT B.AM_Name, A.*, E.CustAddr_Key, C.ProvinsiName \n" +
                    "\t\tFROM mAM_Prov A, mAM B, mProvinsi C, mCity D, mCustKirim E\n" +
                    "\t\tWHERE A.AM_Key=B.AM_Key AND A.Provinsi_Key=C.Provinsi_Key AND C.Provinsi_Key=D.Provinsi_Key AND D.City_Key=E.City_Key\n" +
                    "\t\tAND DATEDIFF(DAY, Tanggal, (SELECT DISTINCT TOP 1 Tanggal FROM mAM_Prov WHERE DATEDIFF(DAY, Tanggal, GETDATE())>=0 ORDER BY Tanggal DESC))=0\n" +
                    "\t) I ON B.CustAddr_Key=I.CustAddr_Key\n" +
                    "      WHERE  ISA2=0 and datediff(year, tglFaktur, '1/31/";
            strQuery2 = " ')=0 and Kat_Kelompok !='GP' " +strWhereAMPIE+ strWhereSalesTBMPIE+
                    "\n                      GROUP BY Kat_Kelompok\n" +
                    "                        )\n" +
                    "                        SELECT BULAN, Total FROM cte WHERE num BETWEEN 1 AND 4 \n" +
                    "                        UNION ALL\n" +
                    "                        SELECT  BULAN='Other', ISNULL(SUM(Total),0) FROM cte WHERE num > 4 ";
            strFlagTitle3 = "Komposisi penjualan";
            strFlagTitle4 = "Semua Distributor Tahun ";
            strFlagTitle5 = " (DO Date)";
        }
    }

    public void loadBulan() {

        int thisYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
        for (int i = thisYear - 2; i <= thisYear; i++) {
            listyearView.add(Integer.toString(i));
        }

        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            listBulanView.add(month_name);
        }
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(PerbandinganHarianActivity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(PerbandinganHarianActivity.this,
                R.layout.spinner_value, listyearView);

        spinnerTahun.setAdapter(tahun_adapter);
        spinnerBulan.setAdapter(bulan_adapter);
        spinnerTahun.setSelection(2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return super.onOptionsItemSelected(item);
    }
}
