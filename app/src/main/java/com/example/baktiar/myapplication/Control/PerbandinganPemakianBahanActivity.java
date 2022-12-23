package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.CallerPLC;
import com.example.baktiar.myapplication.Model.DataOmzetList;
import com.example.baktiar.myapplication.Model.PLCLIST;
import com.example.baktiar.myapplication.R;
import com.example.baktiar.myapplication.View.XYMarkerView;
import com.example.baktiar.myapplication.View.XYMarkerViewReturPenjualan;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PerbandinganPemakianBahanActivity extends AppCompatActivity {
    BarChart mChart, mChart2;
    TextView txtShowText;
    ProgressDialog p;
    List<BarEntry> entriesGroup1 = new ArrayList<>();
    List<BarEntry> entriesGroup2 = new ArrayList<>();
    public static String rslt = "";
    List<Double> dBahnBaku = new ArrayList<Double>();
    List<Double> dTotalBahan = new ArrayList<Double>();
    List<Double> dTotalPerhitungan = new ArrayList<Double>();
    List<Double> dM2n = new ArrayList<Double>();
    List<Integer> iJmlProd = new ArrayList<Integer>();

    List<String> strTgl = new ArrayList<String>();
    List<String> strTglData = new ArrayList<String>();
    List<String> strTglPLC = new ArrayList<String>();

    TableRow tableRowM2N, tableRowBorosIrit, tableRowRMY, tableRowProduksi;
    //  private SlidrInterface slidr;
    float[] valOne, valTwo;
    String Queque;
    Button buttonProduksi, buttonRMY, buttonM2N, buttonBoros;
    private ArrayList<PLCLIST> plclists;
    ArrayList<BarEntry> barOne = new ArrayList<>();
    ArrayList<BarEntry> barTwo = new ArrayList<>();
    ArrayList<BarEntry> barThree = new ArrayList<>();
    ArrayList<String> theDates = new ArrayList<>();
    String strQuery, strFlagTitle1, strQueryPLC, strQueryPLC2;
    private ArrayList<DataOmzetList> dataOmzetList;
    Integer iTotalBahan;
    String strM2n, strRMY, strProduksi, strBorosIrit, strTempTglAwal, strTempTglAkhir, strFlagJenisLaporan, strflagButton;
    TextView txtBoros, txtJmlBoros, txtM2N, txtJmlM2N, txtRMY, txtJmlRMY, txtProduksi, txtJmlProduksi, txtTitle;
    private DecimalFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perbandingan_pemakian_bahan);
        mChart = (BarChart) findViewById(R.id.bargraph);
        mChart2 = (BarChart) findViewById(R.id.bargraph2);
        plclists = new ArrayList<PLCLIST>();
        dataOmzetList = new ArrayList<DataOmzetList>();
        txtShowText = (TextView) findViewById(R.id.txtShowText);

        txtJmlProduksi = (TextView) findViewById(R.id.txtJmlProduksi);
        txtJmlRMY = (TextView) findViewById(R.id.txtJmlRMY);
        txtJmlM2N = (TextView) findViewById(R.id.txtJmlM2N);
        txtJmlBoros = (TextView) findViewById(R.id.txtJmlBoros);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        buttonProduksi = (Button) findViewById(R.id.buttonProduksi);
        buttonRMY = (Button) findViewById(R.id.buttonRMY);
        buttonM2N = (Button) findViewById(R.id.buttonM2N);
        buttonBoros = (Button) findViewById(R.id.buttonBoros);

        tableRowM2N = (TableRow) findViewById(R.id.tableRowM2N);
        tableRowBorosIrit = (TableRow) findViewById(R.id.tableRowBorosIrit);
        tableRowRMY = (TableRow) findViewById(R.id.tableRowRMY);
        tableRowProduksi = (TableRow) findViewById(R.id.tableRowProduksi);

        //  slidr = Slidr.attach(this);
//        format = new DecimalFormat("#.###");

        strM2n = getIntent().getStringExtra("strM2n");
        strRMY = getIntent().getStringExtra("strRMY");
        strProduksi = getIntent().getStringExtra("strProduksi");
        strBorosIrit = getIntent().getStringExtra("strBorosIrit");
        strTempTglAwal = getIntent().getStringExtra("strTempTglAwal");
        strTempTglAkhir = getIntent().getStringExtra("strTempTglAkhir");
        strFlagJenisLaporan = getIntent().getStringExtra("strFlagJenisLaporan");
//        iTotalBahan = Integer.parseInt(getIntent().getStringExtra("iTotalBahan"));
        iTotalBahan = (int) getIntent().getExtras().getDouble("iTotalBahan");
//
//
//        System.out.println(" format.format(strProduksi) " +  format.format(strProduksi));
//        System.out.println("format.format(strM2n) " + format.format(strM2n));

        txtJmlProduksi.setText(   strProduksi);
        txtJmlM2N.setText(strM2n );
        txtJmlRMY.setText(strRMY);
        txtJmlBoros.setText(strBorosIrit);

        strQuery = getIntent().getStringExtra("query");
        strQueryPLC = getIntent().getStringExtra("queryPLC");
        strFlagTitle1 = getIntent().getStringExtra("strFlagTitle1");
        txtTitle.setText(strFlagTitle1);
        mChart2.setVisibility(View.GONE);
        mChart.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Pemakaian Bahan Produksi");
        }

        QueryLoad();
        buttonM2N.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
                QueryLoad();
                mChart2.setVisibility(View.VISIBLE);
                mChart.setVisibility(View.INVISIBLE);
                txtShowText.setText("Grafik M2N");
                strflagButton = "M2N";
                SyncData mydata = new SyncData();
                mydata.execute();


            }
        });
        buttonProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("QUERY " + strQuery);
                clearData();
                QueryLoad();
                mChart2.setVisibility(View.VISIBLE);
                mChart.setVisibility(View.INVISIBLE);
                txtShowText.setText("Grafik Jumlah Produksi (Lembar)");
                strflagButton = "Produksi";
                SyncData mydata = new SyncData();
                mydata.execute();
            }
        });
        buttonRMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
                QueryLoad();
                txtShowText.setText("Grafik RMY");
//                System.out.println("QUERY PLC " +strQueryPLC);
                mChart2.setVisibility(View.VISIBLE);
                mChart.setVisibility(View.INVISIBLE);
                strflagButton = "RMY";
//                SyncData mydata = new SyncData();
//                mydata.execute();
                SyncDataPLC syncDataPLC = new SyncDataPLC();
                syncDataPLC.execute();
            }
        });
        buttonBoros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChart2.setVisibility(View.INVISIBLE);
                mChart.setVisibility(View.VISIBLE);
                txtShowText.setText("Grafik Bahan Produksi");
                clearData();
                if (strFlagJenisLaporan.equals("bulanan")) {
                    strQuery = "select  Bahan_KEY, SUM((Jumlah*Brg_Berat*Presentase)/TOTAL) as TOTAL_BAHAN from mProduksi A\n" +
                            "INNER JOIN mProdDetail B ON A.ProdID = B.ProdID\n" +
                            "INNER JOIN mProducts C ON B.Brg_Key = C.Brg_Key\n" +
                            "INNER JOIN mKatKomposisi ON mKatKomposisi.Kat_Key = C.Kat_Key\n" +
                            "INNER JOIN mKomposisi ON mKomposisi.Komposisi_Key = mKatKomposisi.Komposisi_Key \n" +
                            "INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON mKomposisi.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Mesin_Key = 2 AND\n" +
                            "DATEDIFF(MONTH,ProdTgl," + strTempTglAwal + ") <=0 and DATEDIFF(MONTH,ProdTgl," + strTempTglAkhir + ") >=0 \n" +
                            "GROUP BY Bahan_KEY \n" +
                            "ORDER BY Bahan_Key";
                    strQueryPLC2 = "select Bahan_Key, SUM(Jumlah) as Jumlah from ( \n" +
                            "SELECT CASE WHEN C.Bahan_Key = 17416 THEN  22695 ELSE C.Bahan_Key END as Bahan_Key,(Presentase*COUNT(*)) as Jumlah FROM mPLC P \n" +
                            "INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key \n" +
                            "INNER JOIN mKomposisi C ON C.Komposisi_Key = P.Komposisi_Key\n" +
                            "INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 13115\n" +
                            "GROUP BY C.Bahan_Key,Presentase\n" +
                            ") A group by Bahan_Key\n" +
                            "ORDER BY Bahan_Key";
                    strQueryPLC = "SELECT CAST(ISNULL([Silica],0) as DECIMAL(15,2))as Silica,CAST(ISNULL([Kertas],0) AS DECIMAL(15,2)) as Kertas,CAST(ISNULL([Semen],0) AS DECIMAL(15,2)) as Semen,CAST(ISNULL([Asbes],0) AS DECIMAL(15,2)) as Asbes,CAST(ISNULL([HWG],0) AS DECIMAL(15,2)) as HWG FROM\n" +
                            //"SELECT  ISNULL([Silica],0) as Silica,ISNULL([Kertas],0) as Kertas,ISNULL([Semen],0) as Semen,ISNULL([Asbes],0) as Asbes,ISNULL([HWG],0) as HWG FROM\n" +
                            "(SELECT ISNULL('Silica',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 10468\n" +
                            " group by Brg_Nama\n" +
                            "UNION ALL\n" +
                            "SELECT ISNULL('Kertas',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 12166\n" +
                            " group by Brg_Nama\n" +
                            "UNION ALL\n" +
                            "SELECT ISNULL('Semen',0) AS Jenis,SUM(Jumlah)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 13115\n" +
                            " group by Brg_Nama\n" +
                            "UNION ALL\n" +
                            "SELECT ISNULL('HWG',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL))  \n" +
                            "and B.Brg_Key = 17416\n" +
                            "group by Brg_Nama\n" +
                            "UNION ALL\n" +
                            "SELECT  ISNULL('Asbes',0) AS Jenis,SUM(Jumlah*(100-Konsentrasi)/100)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 17723\n" +
                            " group by Brg_Nama) A\n" +
                            "PIVOT\n" +
                            "(Sum(Jumlah) FOR Jenis IN([Silica],[Kertas],[Semen],[Asbes], [HWG])) AS pvt";

                } else {

                    strQuery = "select  Bahan_KEY, SUM((Jumlah*Brg_Berat*Presentase)/TOTAL) as TOTAL_BAHAN from mProduksi A\n" +
                            "INNER JOIN mProdDetail B ON A.ProdID = B.ProdID\n" +
                            "INNER JOIN mProducts C ON B.Brg_Key = C.Brg_Key\n" +
                            "INNER JOIN mKatKomposisi ON mKatKomposisi.Kat_Key = C.Kat_Key\n" +
                            "INNER JOIN mKomposisi ON mKomposisi.Komposisi_Key = mKatKomposisi.Komposisi_Key \n" +
                            "INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON mKomposisi.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Mesin_Key = 2 AND\n" +
                            "DATEDIFF(DAY,ProdTgl," + strTempTglAwal + ") <=0 and DATEDIFF(DAY,ProdTgl," + strTempTglAkhir + ") >=0 \n" +
                            "GROUP BY Bahan_KEY \n" +
                            "ORDER BY Bahan_Key";
                    strQueryPLC2 = "select Bahan_Key, SUM(Jumlah) as Jumlah from ( \n" +
                            "SELECT CASE WHEN C.Bahan_Key = 17416 THEN  22695 ELSE C.Bahan_Key END as Bahan_Key,(Presentase*COUNT(*)) as Jumlah FROM mPLC P \n" +
                            "INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key \n" +
                            "INNER JOIN mKomposisi C ON C.Komposisi_Key = P.Komposisi_Key\n" +
                            "INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 13115\n" +
                            "GROUP BY C.Bahan_Key,Presentase\n" +
                            ") A group by Bahan_Key\n" +
                            "ORDER BY Bahan_Key";
                    strQueryPLC = "SELECT CAST(ISNULL([Silica],0) as DECIMAL(15,2))as Silica,CAST(ISNULL([Kertas],0) AS DECIMAL(15,2)) as Kertas,CAST(ISNULL([Semen],0) AS DECIMAL(15,2)) as Semen,CAST(ISNULL([Asbes],0) AS DECIMAL(15,2)) as Asbes,CAST(ISNULL([HWG],0) AS DECIMAL(15,2)) as HWG FROM\n" +
//                            "SELECT  ISNULL([Silica],0) as Silica,ISNULL([Kertas],0) as Kertas,ISNULL([Semen],0) as Semen,ISNULL([Asbes],0) as Asbes,ISNULL([HWG],0) as HWG FROM\n" +
                            "(SELECT ISNULL('Silica',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 10468\n" +
                            "group by Brg_Nama\n" +
                            "UNION ALL\n" +
                            "SELECT ISNULL('Kertas',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 12166\n" +
                            "group by Brg_Nama\n" +
                            "UNION ALL\n" +
                            "SELECT ISNULL('Semen',0) AS Jenis,SUM(Jumlah)as Jumlah FROM mPLC P \n" +
                            "INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 13115\n" +
                            "group by Brg_Nama \n" +
                            "UNION ALL\n" +
                            "SELECT ISNULL('HWG',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL))  \n" +
                            "and B.Brg_Key = 17416\n" +
                            "group by Brg_Nama \n" +
                            "UNION ALL\n" +
                            "SELECT  ISNULL('Asbes',0) AS Jenis,SUM(Jumlah*(100-Konsentrasi)/100)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                            "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                            "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                            "and B.Brg_Key = 17723\n" +
                            "group by Brg_Nama ) A\n" +
                            "PIVOT\n" +
                            "(Sum(Jumlah) FOR Jenis IN([Silica],[Kertas],[Semen],[Asbes], [HWG])) AS pvt";

                }
                strflagButton = "Boros";

                SyncDataPLC syncDataPLC = new SyncDataPLC();
                syncDataPLC.execute();
//                SyncData mydata = new SyncData();
//                mydata.execute();

            }
        });
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent motionEvent) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent motionEvent) {
                mChart2.setVisibility(View.VISIBLE);
                mChart.setVisibility(View.INVISIBLE);
                mChart.clear();
                mChart2.clear();

//                mChart2.setdef
//                mChart.destroyDrawingCache();
//                mChart2.destroyDrawingCache();

                barTwo.clear();
                barOne.clear();
                barThree.clear();
                theDates.clear();


                txtShowText.setText("Grafik Penggunaan Bahan");
                theDates.add("Silica");
                theDates.add("Kertas");
                theDates.add("Semen");
                theDates.add("Asbes");
                theDates.add("HWG");
                for (int x = 0; x < dBahnBaku.size(); x++) {
                    barOne.add(new BarEntry(x, (int) ((dTotalBahan.get(x) - dBahnBaku.get(x)) / iTotalBahan * 100)));
                    System.out.println("Total bahan " + dTotalBahan.get(x));
                    System.out.println("bahan Baku  " + dBahnBaku.get(x));
                    System.out.println("iTotalBahan  " + iTotalBahan);
//                    barOne.add(new BarEntry(x, (int) ((dTotalBahan.get(x) - dBahnBaku.get(x)) / dTotalBahan.get(x) * -100)));
                }
//                barOne.add(new BarEntry(0, (int) (20) ));
//                barOne.add(new BarEntry(1, (int) (-20) ));
//                barOne.add(new BarEntry(2, (int) (15) ));
//                barOne.add(new BarEntry(3, (int) (-15) ));
                GraphSingle();
            }

            @Override
            public void onChartSingleTapped(MotionEvent motionEvent) {

            }

            @Override
            public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

            }

            @Override
            public void onChartScale(MotionEvent motionEvent, float v, float v1) {

            }

            @Override
            public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {

            }
        });

    }

    public void QueryLoad() {
        if (strFlagJenisLaporan.equals("bulanan")) {

//            strQuery = "SELECT MONTH(ProdTgl) as TanggalNo,DATENAME(month, ProdTgl) as Tanggal,SUM(B.Jumlah*(1*D.Lebar*D.Panjang*D.Tebal*D.Variabel)/500000)  AS m2n, sum(jumlah) AS Produksi, SUM(b.Jumlah*Brg_Berat) AS Berat FROM mProduksi A\n" +
            strQuery = "SELECT MONTH(ProdTgl) as TanggalNo,DATENAME(month, ProdTgl) as Tanggal,SUM(B.Jumlah*(1*D.Lebar*D.Panjang*D.Tebal)/500000)  AS m2n, sum(jumlah) AS Produksi, SUM(b.Jumlah*Brg_Berat) AS Berat FROM mProduksi A\n" +
                    "INNER JOIN mProdDetail B ON A.ProdID = B.ProdID\n" +
                    "INNER JOIN mProducts C ON B.Brg_Key = C.Brg_Key\n" +
                    "INNER JOIN mUkuran D ON C.Ukuran_Key = D.Ukuran_Key\n" +
                    "WHERE Mesin_Key = 2 and \n" +
                    "DATEDIFF(MONTH,ProdTgl," + strTempTglAwal + ") <=0 and DATEDIFF(MONTH,ProdTgl," + strTempTglAkhir + ") >=0 \n" +
                    "group by MONTH(ProdTgl),DATENAME(month, ProdTgl)\n" +
                    "Order by MONTH(ProdTgl)";
            strQueryPLC2 = "SELECT \n" +
                    "CASE WHEN C.Bahan_Key = 17416 THEN  22695 ELSE C.Bahan_Key END as Bahan_Key,(Presentase*COUNT(*)) as Jumlah FROM mPLC P \n" +
                    "INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key \n" +
                    "INNER JOIN mKomposisi C ON C.Komposisi_Key = P.Komposisi_Key\n" +
                    "INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 13115\n" +
                    "GROUP BY C.Bahan_Key,Presentase\n" +
                    "ORDER BY Bahan_Key";
            strQueryPLC = "SELECT  Tanggal,CAST(ISNULL([Silica],0) as DECIMAL(15,2))as Silica,CAST(ISNULL([Kertas],0) AS DECIMAL(15,2)) as Kertas,CAST(ISNULL([Semen],0) AS DECIMAL(15,2)) as Semen,CAST(ISNULL([Asbes],0) AS DECIMAL(15,2)) as Asbes,CAST(ISNULL([HWG],0) AS DECIMAL(15,2)) as HWG FROM\n" +
//                    "SELECT  Tanggal,ISNULL([Silica],0) as Silica,ISNULL([Kertas],0) as Kertas,ISNULL([Semen],0) as Semen,ISNULL([Asbes],0) as Asbes,ISNULL([HWG],0) as HWG FROM\n" +
                    "(SELECT CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END  as Tanggal,ISNULL('Silica',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 10468\n" +
                    " group by Brg_Nama,CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END\n" +
                    "UNION ALL\n" +
                    "SELECT CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END  as Tanggal,ISNULL('Kertas',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 12166\n" +
                    " group by Brg_Nama,CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END\n" +
                    "UNION ALL\n" +
                    "SELECT CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END  as Tanggal,ISNULL('Semen',0) AS Jenis,SUM(Jumlah)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 13115\n" +
                    " group by Brg_Nama,CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END\n" +
                    "UNION ALL\n" +
                    "SELECT CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END  as Tanggal,ISNULL('HWG',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL))  \n" +
                    "and B.Brg_Key = 17416\n" +
                    "group by Brg_Nama,CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END\n" +
                    "UNION ALL\n" +
                    "SELECT CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END  as Tanggal, ISNULL('Asbes',0) AS Jenis,SUM(Jumlah*(100-Konsentrasi)/100)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(MONTH,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (MONTH,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (MONTH,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 17723\n" +
                    " group by Brg_Nama,CASE WHEN TGTrans is not null THEN MONTH(TGTrans) ELSE MONTH(TGPLC) END) A\n" +
                    "PIVOT\n" +
                    "(Sum(Jumlah) FOR Jenis IN([Silica],[Kertas],[Semen],[Asbes], [HWG])) AS pvt\n" +
                    "order by Tanggal\n";
        } else {
//            strQuery = "SELECT CONVERT(VARCHAR(10),ProdTgl,110) TanggalNo,CONVERT(VARCHAR(10),ProdTgl,103) as Tanggal,SUM(B.Jumlah*(1*D.Lebar*D.Panjang*D.Tebal*D.Variabel)/500000)  AS m2n, sum(jumlah) AS Produksi, SUM(b.Jumlah*Brg_Berat) AS Berat FROM mProduksi A\n" +
            strQuery = "SELECT CONVERT(VARCHAR(10),ProdTgl,110) TanggalNo,CONVERT(VARCHAR(10),ProdTgl,103) as Tanggal,SUM(B.Jumlah*(1*D.Lebar*D.Panjang*D.Tebal)/500000)  AS m2n, sum(jumlah) AS Produksi, SUM(b.Jumlah*Brg_Berat) AS Berat FROM mProduksi A\n" +
                    "INNER JOIN mProdDetail B ON A.ProdID = B.ProdID\n" +
                    "INNER JOIN mProducts C ON B.Brg_Key = C.Brg_Key\n" +
                    "INNER JOIN mUkuran D ON C.Ukuran_Key = D.Ukuran_Key\n" +
                    "WHERE Mesin_Key = 2 and \n" +
                    "DATEDIFF(DAY,ProdTgl," + strTempTglAwal + ") <=0 and DATEDIFF(DAY,ProdTgl," + strTempTglAkhir + ") >=0 \n" +
                    "group by CONVERT(VARCHAR(10),ProdTgl,110) , CONVERT(VARCHAR(10),ProdTgl,103)\n" +
                    "order by CONVERT(VARCHAR(10),ProdTgl,110) ";
            strQueryPLC2 = "SELECT \n" +
                    "CASE WHEN C.Bahan_Key = 17416 THEN  22695 ELSE C.Bahan_Key END as Bahan_Key,(Presentase*COUNT(*)) as Jumlah FROM mPLC P \n" +
                    "INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key \n" +
                    "INNER JOIN mKomposisi C ON C.Komposisi_Key = P.Komposisi_Key\n" +
                    "INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 13115\n" +
                    "GROUP BY C.Bahan_Key,Presentase\n" +
                    "ORDER BY Bahan_Key";
            strQueryPLC = "SELECT  Tanggal,CAST(ISNULL([Silica],0) as DECIMAL(15,2))as Silica,CAST(ISNULL([Kertas],0) AS DECIMAL(15,2)) as Kertas,CAST(ISNULL([Semen],0) AS DECIMAL(15,2)) as Semen,CAST(ISNULL([Asbes],0) AS DECIMAL(15,2)) as Asbes,CAST(ISNULL([HWG],0) AS DECIMAL(15,2)) as HWG FROM\n" +
                    "(SELECT CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END AS Tanggal,ISNULL('Silica',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 10468\n" +
                    "group by Brg_Nama,CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END \n" +
                    "UNION ALL\n" +
                    "SELECT CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END AS Tanggal,ISNULL('Kertas',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 12166\n" +
                    "group by Brg_Nama,CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END \n" +
                    "UNION ALL\n" +
                    "SELECT CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END AS Tanggal,ISNULL('Semen',0) AS Jenis,SUM(Jumlah)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 13115\n" +
                    "group by Brg_Nama,CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END \n" +
                    "UNION ALL\n" +
                    "SELECT CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END AS Tanggal,ISNULL('HWG',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL))  \n" +
                    "and B.Brg_Key = 17416\n" +
                    "group by Brg_Nama,CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END \n" +
                    "UNION ALL\n" +
                    "SELECT CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END AS Tanggal, ISNULL('Asbes',0) AS Jenis,SUM(Jumlah*(100-Konsentrasi)/100)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                    "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans," + strTempTglAwal + ") <= 0 AND DATEDIFF(DAY,TGTrans," + strTempTglAkhir + ") >= 0)  OR \n" +
                    "(DATEDIFF (DAY,TGPLC," + strTempTglAwal + ") <= 0 AND DATEDIFF (DAY,TGPLC," + strTempTglAkhir + ") >= 0 AND TGTrans IS NULL)) \n" +
                    "and B.Brg_Key = 17723\n" +
                    "group by Brg_Nama,CASE WHEN TGTrans is not null THEN CONVERT(VARCHAR(10),TGTrans,110) ELSE CONVERT(VARCHAR(10),TGPLC,110) END ) A\n" +
                    "PIVOT\n" +
                    "(Sum(Jumlah) FOR Jenis IN([Silica],[Kertas],[Semen],[Asbes], [HWG])) AS pvt\n" +
                    "order by Tanggal";
        }
    }

    public void clearData() {
        dBahnBaku.clear();
        strTglPLC.clear();
        iJmlProd.clear();
        strTgl.clear();
        strTglData.clear();
        dM2n.clear();
        dTotalBahan.clear();
        dTotalPerhitungan.clear();
        barTwo.clear();
        barOne.clear();
        barThree.clear();
        theDates.clear();
        mChart.clear();
        mChart2.clear();
    }

    public class SyncData extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        //private final ProgressDialog p = new ProgressDialog(PerbandinganPemakianBahanActivity.this, R.style.MyTheme);
        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(PerbandinganPemakianBahanActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                rslt = "START";
                Caller d = new Caller();
                d.strQuery = strQuery;
                d.join();
                d.start();

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);

                    } catch (Exception ex) {
                    }
                }

            } catch (Exception ex) {
            }
            if (rslt != "[]") {
                try {

                    JSONArray json = new JSONArray(rslt);

                    if (json != null) {

                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            if (strflagButton.equals("Boros")) {
                                dTotalBahan.add(e.getDouble("TOTAL_BAHAN"));
                            } else {
                                dM2n.add(e.getDouble("m2n"));
                                strTglData.add(e.getString("TanggalNo"));
                                strTgl.add(e.getString("Tanggal"));
                                iJmlProd.add(e.getInt("Produksi"));
                            }
                        }
                        if (json.length() >= 1) {
                            msg = "Found";
                            isSuccess = "true";
                        } else {
                            msg = "No Data found!";
                            isSuccess = "false";
                        }
                        //  isSuccess = true;
                    } else {
                        msg = "No Data found!";
                        isSuccess = "false";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isSuccess = "fail";
                }
            } else {
                isSuccess = "false";
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
        {
            p.dismiss();
            if (isSuccess == "false") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PerbandinganPemakianBahanActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PerbandinganPemakianBahanActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {
                try {
                    barTwo.clear();
                    barOne.clear();
                    barThree.clear();
                    theDates.clear();

                    if (strflagButton.equals("Produksi")) {
                        for (int x = 0; x < strTglData.size(); x++) {
                            barOne.add(new BarEntry(x, (int) iJmlProd.get(x)));
                            theDates.add(strTgl.get(x));
                        }
                        GraphSingle();
                    } else if (strflagButton.equals("M2N")) {
                        for (int x = 0; x < strTglData.size(); x++) {

                            barOne.add(new BarEntry(x, (int) (dM2n.get(x) / 1)));
                            theDates.add(strTgl.get(x));
                        }

                        GraphSingle();
                    } else {
//                        SyncDataPLC syncDataPLC = new SyncDataPLC();
//                        syncDataPLC.execute();
                    }
                } catch (Exception ex) {

                }

            }
        }
    }

    public class SyncDataPLC extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        private final ProgressDialog p = new ProgressDialog(PerbandinganPemakianBahanActivity.this, R.style.MyTheme);

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            // p = new ProgressDialog(UploadImageActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {

            try {
                rslt = "START";
                CallerPLC d = new CallerPLC();
//                d.strQuery = strQueryPLC;
                System.out.println("strQuery "+ strQuery);
                System.out.println("strQueryPLC "+ strQueryPLC);
                System.out.println("strQueryPLC2 "+ strQueryPLC2);
                d.strQuery = strQuery;
                d.strQueryPLC = strQueryPLC;
                d.strQueryPLC2 = strQueryPLC2;
                d.join();
                d.start();
//
                while (rslt == "START") {
                    try {
                        Thread.sleep(10);

                    } catch (Exception ex) {
                        System.out.print("Susah2 " + ex);
                    }
                }

            } catch (Exception ex) {
            }
            if (rslt != "[]") {
                try {
                    JSONObject jsnobject = new JSONObject(rslt);
                    JSONArray json = jsnobject.getJSONArray("PERTAMA");
                    JSONArray json2 = jsnobject.getJSONArray("KEDUA");
                    JSONArray json3 = jsnobject.getJSONArray("KETIGA");
                    if (json != null) {

                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            if (strflagButton.equals("Boros")) {
                                dBahnBaku.add(e.getDouble("Silica"));
                                dBahnBaku.add(e.getDouble("Kertas"));
                                dBahnBaku.add(e.getDouble("Semen"));
                                dBahnBaku.add(e.getDouble("Asbes"));
                                dBahnBaku.add(e.getDouble("HWG"));

                            } else {
                                dBahnBaku.add(e.getDouble("Asbes") + e.getDouble("Silica") + e.getDouble("Semen") + e.getDouble("Kertas") + e.getDouble("HWG"));
                                strTglPLC.add(e.getString("Tanggal"));
                            }
                        }

                        for (int i = 0; i < json3.length(); i++) {
                            JSONObject e = json3.getJSONObject(i);
                            if (strflagButton.equals("Boros")) {
                                System.out.println("Total bahan " +e.getDouble("TOTAL_BAHAN"));
                                dTotalBahan.add(e.getDouble("TOTAL_BAHAN"));
                            } else {
                                dM2n.add(e.getDouble("m2n"));
                                strTglData.add(e.getString("TanggalNo"));
                                strTgl.add(e.getString("Tanggal"));
                                iJmlProd.add(e.getInt("Produksi"));
                            }

                        }
                        if (strflagButton.equals("Boros")) {
                            for (int i = 0; i < json2.length(); i++) {

                                JSONObject e = json2.getJSONObject(i);
                                dTotalPerhitungan.add(e.getDouble("Jumlah"));
                            }
                        }
                        if (json.length() >= 1) {
                            msg = "Found";

                            isSuccess = "true";
                        } else {
                            msg = "No Data found!";

                            isSuccess = "false";
                        }
                        //  isSuccess = true;
                    } else {
                        msg = "No Data found!";
                        isSuccess = "false";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isSuccess = "fail";
                    System.out.print("Susah " + e);
                }
            } else {
                isSuccess = "false";
            }

            return msg;

        }

        @Override
        protected void onPostExecute(String strImgRespon) // disimissing progress dialoge, showing error and setting up my listview
        {
//            super.onPostExecute(strImgRespon);
            if (this.p.isShowing()) {
                p.dismiss();
            }


            if (isSuccess.equals("false") == true) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PerbandinganPemakianBahanActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage(strImgRespon.toString());
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();

            } else if (isSuccess.equals("true") == true) {

                barTwo.clear();
                barOne.clear();
                barThree.clear();
                theDates.clear();

                if (strflagButton.equals("RMY")) {
                    if (strTglData.size() > strTglPLC.size()) {
                        for (int x = 0; x < strTglPLC.size(); x++) {
                            for (int y = 0; y < strTglData.size(); y++) {
                                if (strTglPLC.get(x).equals(strTglData.get(y))) {
                                    barOne.add(new BarEntry(x, (int) (dM2n.get(y) / (dBahnBaku.get(x) / 1000))));
                                    theDates.add(strTgl.get(x));
                                }
                            }
                        }
                    } else {
                        for (int x = 0; x < strTglData.size(); x++) {
                            for (int y = 0; y < strTglPLC.size(); y++) {
                                if (strTglData.get(x).equals(strTglPLC.get(y))) {
                                    barOne.add(new BarEntry(x, (int) (dM2n.get(x) / (dBahnBaku.get(y) / 1000))));
                                    theDates.add(strTgl.get(x));
                                }
                            }
                        }
                    }

                    GraphSingle();
                } else if (strflagButton.equals("Boros")) {
                    theDates.add("Silica");
                    theDates.add("Kertas");
                    theDates.add("Semen");
                    theDates.add("Asbes");
                    theDates.add("HWG");

                    for (int x = 0; x < dBahnBaku.size(); x++) {
                        barOne.add(new BarEntry(x, (int) (dTotalBahan.get(x) / 1)));
                        barTwo.add(new BarEntry(x, (int) (dBahnBaku.get(x) / 1)));
                        barThree.add(new BarEntry(x, (int) (dTotalPerhitungan.get(x) / 1)));
                    }

                    GraphDouble();
                }

            }

        }
    }

    public void GraphSingle() {


        mChart2.setDrawBarShadow(false);
        mChart2.getDescription().setEnabled(false);
        mChart2.setPinchZoom(false);
        mChart2.setDrawGridBackground(true);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return theDates.get((int) value % theDates.size());
            }

            // we don't draw numbers, so no decimal digits needed

            public int getDecimalDigits() {
                return 0;
            }
        };
        mChart2.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart2.setPinchZoom(false);

        mChart2.setDrawGridBackground(false);
        XAxis xAxis = mChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
//        xAxis.setValueFormatter(formatter);
//        xAxis.setValueFormatter(new LargeValueFormatter());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(theDates));

        YAxis leftAxis = mChart2.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart2.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart2.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
//        l.setEnabled(false);

        mChart2.getAxisRight().setEnabled(false);
        mChart2.getLegend().setEnabled(true);
//        mChart.getDescription().setEnabled(false);

        BarDataSet set1 = new BarDataSet(barOne, "Pemakaian Riil");
        set1.setColor(ColorTemplate.JOYFUL_COLORS[4]);
//        BarDataSet set2 = new BarDataSet(barOne, "Pemakaian Standar");
//        set2.setColor(ColorTemplate.JOYFUL_COLORS[1]);

        set1.setDrawValues(false);
//        set2.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
//        dataSets.add(set2);

        BarData data = new BarData(dataSets);
        float groupSpace = 0.4f;
        float barSpace = 0f;
//        float barWidth = 0.3f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
//        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(theDates.size());
        //Untuk menghilangkan kotak warna
        mChart2.getLegend().setEnabled(false);
        mChart2.setData(data);
        mChart2.setScaleEnabled(false);
        mChart2.setVisibleXRangeMaximum(4f);
        mChart2.invalidate();


//
//        XYMarkerView mv = new XYMarkerView(this, formatter);
//        mv.setChartView(mChart2); // For bounds control
//        mChart2.setMarker(mv); // Set the marker to the chart
        if (strflagButton.equals("Boros")) {
//            mChart2.getAxisLeft().setStartAtZero(false);
//            mChart2.getAxisRight().setStartAtZero(false);
            XYMarkerViewReturPenjualan mv = new XYMarkerViewReturPenjualan(this, formatter);
            mv.setChartView(mChart2); // For bounds control
            mChart2.setMarker(mv);
        } else {
            XYMarkerView mv = new XYMarkerView(this, formatter);
            mv.setChartView(mChart2); // For bounds control
            mChart2.setMarker(mv); // Set the marker to the chart
        }
    }

    public void GraphDouble() {


        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(true);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return theDates.get((int) value % theDates.size());
            }

            // we don't draw numbers, so no decimal digits needed

        };

        XAxis xAxis = mChart.getXAxis();

        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
//        xAxis.setAxisLineWidth(2f);
//        xAxis.setLabelCount(15, true);
        xAxis.setAxisLineColor(Color.WHITE);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0f);
//        xAxis.setValueFormatter(new LargeValueFormatter());
//        xAxis.setValueFormatter(formatter);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(theDates));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
//        leftAxis.setGranularity(1);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(true);

        BarDataSet set1 = new BarDataSet(barOne, "Standar");
        set1.setColor(ColorTemplate.JOYFUL_COLORS[4]);
        BarDataSet set2 = new BarDataSet(barTwo, "Riil");
        set2.setColor(ColorTemplate.JOYFUL_COLORS[1]);
        BarDataSet set3 = new BarDataSet(barThree, "Perhitungan");
        set3.setColor(ColorTemplate.JOYFUL_COLORS[3]);

        set1.setDrawValues(false);
        set2.setDrawValues(false);
        set3.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        BarData data = new BarData(dataSets);
        float groupSpace = 0.25f;
        float barSpace = 0f;
        float barWidth = 0.25f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(theDates.size());

        mChart.setData(data);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(4f);
        mChart.groupBars(0f, groupSpace, barSpace);
        mChart.invalidate();

        XYMarkerView mv = new XYMarkerView(this, formatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
    }

    //    public void GraphSingle() {
//
//
//        mChart.setDrawBarShadow(false);
//        mChart.getDescription().setEnabled(false);
//        mChart.setPinchZoom(false);
//        mChart.setDrawGridBackground(true);
//
//        IAxisValueFormatter formatter = new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return theDates.get((int) value % theDates.size());
//            }
//
//            // we don't draw numbers, so no decimal digits needed
//
//            public int getDecimalDigits() {
//                return 0;
//            }
//        };
//        mChart.setMaxVisibleValueCount(60);
//        // scaling can now only be done on x- and y-axis separately
//        mChart.setPinchZoom(false);
//
//        mChart.setDrawGridBackground(false);
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f); // only intervals of 1 day
//        xAxis.setLabelCount(7);
////        xAxis.setValueFormatter(new LargeValueFormatter());
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(theDates));
//
//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setLabelCount(8, false);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setSpaceTop(15f);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        Legend l = mChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);
//
////        XAxis xAxis = mChart.getXAxis();
////        xAxis.setCenterAxisLabels(true);
////        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
////        xAxis.setDrawGridLines(true);
////        xAxis.setGranularity(1f); // only intervals of 1 day
////        xAxis.setTextColor(Color.BLACK);
////        xAxis.setTextSize(12);
////        xAxis.setAxisLineColor(Color.WHITE);
////        xAxis.setAxisMinimum(0f);
////        xAxis.setValueFormatter(new LargeValueFormatter());
//////        xAxis.setValueFormatter(formatter);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(theDates));
////        YAxis leftAxis = mChart.getAxisLeft();
////        leftAxis.setTextColor(Color.BLACK);
////        leftAxis.setTextSize(12);
////        leftAxis.setAxisLineColor(Color.WHITE);
////        leftAxis.setDrawGridLines(true);
//////        leftAxis.setGranularity(1);
////        leftAxis.setAxisMinimum(0f);
////        leftAxis.setLabelCount(8, true);
////        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//
//        mChart.getAxisRight().setEnabled(false);
//        mChart.getLegend().setEnabled(true);
//
//
//        BarDataSet set1 = new BarDataSet(barOne, "Pemakaian Riil");
//        set1.setColor(ColorTemplate.JOYFUL_COLORS[4]);
////        BarDataSet set2 = new BarDataSet(barOne, "Pemakaian Standar");
////        set2.setColor(ColorTemplate.JOYFUL_COLORS[1]);
//
//        set1.setDrawValues(false);
////        set2.setDrawValues(false);
//
//        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//        dataSets.add(set1);
////        dataSets.add(set2);
//
//        BarData data = new BarData(dataSets);
//        float groupSpace = 0.4f;
//        float barSpace = 0f;
////        float barWidth = 0.3f;
//        // (barSpace + barWidth) * 2 + groupSpace = 1
////        data.setBarWidth(barWidth);
//        // so that the entire chart is shown when scrolled from right to left
//        xAxis.setAxisMaximum(theDates.size());
//
//        mChart.setData(data);
//        mChart.setScaleEnabled(false);
//        mChart.setVisibleXRangeMaximum(4f);
////        mChart.groupBars(0f, groupSpace, barSpace);
//        mChart.invalidate();
//
//
//        XYMarkerView mv = new XYMarkerView(this, formatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart
//    }
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