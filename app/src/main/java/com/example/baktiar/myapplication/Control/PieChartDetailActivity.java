package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.DataPenjualanList;
import com.example.baktiar.myapplication.View.PercentageFormatter;
import com.example.baktiar.myapplication.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.sql.Connection;
import java.util.ArrayList;

public class PieChartDetailActivity extends BaseActivity {
    TextView txtShowText;
    Object response = null;
    private SlidrInterface slidr;
    RadioGroup radioGroupOutput4;
    RadioButton RBLembar, RBTonase;
    CheckBox CBGP;
    ProgressDialog p;
    PieChart pieChart;
    String query = null, strFlagPercentage = "False", flagDetail, flagTitle3, query4 = null;
    private ArrayList<DataPenjualanList> dataPenjualanList;
    ArrayList<PieEntry> pieEntries = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<Integer>();
    public static String rslt = "";
    PieData theData;
    String strQueryPie, strParamAct, strSatuan, strTglAwal, strTglAkhir, strFlagTgl, strCity, strFlagParam, strParam;
    String strJenis = "", strSales = "", strMesin = "", strTahun = "", strBulan = "", strRole, strArea = "", strCompany = "", strDist = "", strGudang, strJenisData = "", strJenisSatuan = "", strPenjualanTBM = "", strBulanAwal = "", strBulanAkhir = "";
    int iGudang = 0;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_detail);
        slidr = Slidr.attach(this);

        CBGP = (CheckBox) findViewById(R.id.CBGP);
        txtShowText = (TextView) findViewById(R.id.txtShowText);
        pieChart = (PieChart) findViewById(R.id.chart);
        radioGroupOutput4 = (RadioGroup) findViewById(R.id.radioGroupOutput4);
        RBLembar = (RadioButton) findViewById(R.id.RBLembar);
        RBTonase = (RadioButton) findViewById(R.id.RBTonase);
        dataPenjualanList = new ArrayList<DataPenjualanList>();


        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setUsePercentValues(true);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        txtShowText.setTypeface(Typeface.DEFAULT_BOLD);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
//        l.setEnabled(false);


        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtShowText.setTypeface(Typeface.DEFAULT_BOLD);
        Intent myIntent = getIntent(); // this is just for example purpose

        query = getIntent().getStringExtra("query");
        query4 = getIntent().getStringExtra("query4");

        strParamAct = getIntent().getStringExtra("strParamAct");

//        if (strParamAct.equals("LoadListLine"))
//        if (strParamAct.equals("LoadListLine"))
//        {
//            strParamAct = "LoadListLinePie";
        strJenisSatuan = getIntent().getStringExtra("strJenisSatuan");
        strDist = getIntent().getStringExtra("strSales");
        strBulan = getIntent().getStringExtra("strBulan");
        strTahun = getIntent().getStringExtra("strTahun");
        strMesin = getIntent().getStringExtra("strMesin");
        strJenis = getIntent().getStringExtra("strJenis");
//        }
//        else if (strParamAct.equals("LoadListBar")){
//            strParamAct = "LoadListBarPie";
//            strSatuan = getIntent().getStringExtra("strSatuan");
        strTglAwal = getIntent().getStringExtra("strTglAwal");
        strTglAkhir = getIntent().getStringExtra("strTglAkhir");
        strSales = getIntent().getStringExtra("strSales");
        strFlagTgl = getIntent().getStringExtra("strFlagTgl");
        strCity = getIntent().getStringExtra("strCity");
        strFlagParam = getIntent().getStringExtra("strFlagParam");
        strParam = getIntent().getStringExtra("strParam");

//        }

        strCompany = getIntent().getStringExtra("strCompany");
        strRole = getIntent().getStringExtra("strRole");
        strArea = getIntent().getStringExtra("strArea");
        iGudang = getIntent().getIntExtra("iGudang", 0);
        strGudang = getIntent().getStringExtra("strGudang");
        strPenjualanTBM = getIntent().getStringExtra("strPenjualanTBM");


//        intent.putExtra("strCompany", strCompany);
//        intent.putExtra("strRole", strRole);
//        intent.putExtra("strArea", strArea);
//        intent.putExtra("strSatuan", strSatuan);
//        intent.putExtra("iGudang", iGudang);
//        intent.putExtra("strGudang", strGudang);
//        intent.putExtra("strTglAwal",strTglAwal);
//        intent.putExtra("strTglAkhir", strTglAkhir);
//        intent.putExtra("strPenjualanTBM", strPenjualanTBM);
//        intent.putExtra("strSales", strNama);
//        intent.putExtra("strFlagTgl", strFlagTgl);
//        intent.putExtra("strCity", strIndikatorKota);
//        intent.putExtra("strFlagParam", strFlagParam);
//        intent.putExtra("strParam", strIndikator);
//        intent.putExtra("strParamAct", strParamAct);


        flagDetail = getIntent().getStringExtra("flagDetail");
        flagTitle3 = getIntent().getStringExtra("flagTitle3");
        System.out.print("ini query " + query);
        System.out.print("ini query4 " + query4);
        //passingTxtShowText = myIntent.getStringExtra("passingTxtShowText");
        txtShowText.setText(flagDetail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(flagTitle3);
        }
        // p.dismiss();

        radioGroupOutput4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBTonase) {

                    strJenisSatuan = "TONASE";

                    strQueryPie = query4;
                } else if (i == R.id.RBLembar) {

                    strJenisSatuan = "LEMBAR";
                    strQueryPie = query;

                }

                if (strParamAct.equals("PenjualanLine")) {
                    GetDataPenjualanLine getDataPenjualanLine = new GetDataPenjualanLine();
                    getDataPenjualanLine.execute();
                } else if (strParamAct.equals("LaporanHarian")) {
                    GetDataLaporanHarian getDataLaporanHarian = new GetDataLaporanHarian();
                    getDataLaporanHarian.execute();
                } else if (strParamAct.equals("LoadListBar")) {
                    System.out.println("KRISNA1 strCompany " + strCompany);
                    System.out.println("KRISNA2 strRole " + strRole);
                    System.out.println("KRISNA3 strArea " + strArea);
                    System.out.println("KRISNA4 strJenisSatuan " + strJenisSatuan);
                    System.out.println("KRISNA5 iGudang " + iGudang);
                    System.out.println("KRISNA6 strGudang " + strGudang);
                    System.out.println("KRISNA7 strTglAwal " + strTglAwal);
                    System.out.println("KRISNA8 strTglAkhir " + strTglAkhir);
                    System.out.println("KRISNA9 strFlagTgl " + strFlagTgl);
                    System.out.println("KRISNA10 strPenjualanTBM " + strPenjualanTBM);
                    System.out.println("KRISNA11 strSales " + strSales);
                    System.out.println("KRISNA12 strCity " + strCity);
                    System.out.println("KRISNA13 strFlagParam " + strFlagParam);
                    System.out.println("KRISNA14 strParam " + strParam);
                    System.out.println("KRISNA15 strParamAct " + strParamAct);
                    GetDataListBar getDataListBar = new GetDataListBar();
                    getDataListBar.execute();
                } else if (strParamAct.equals("ProduksiLine")) {
                    GetDataProduksiLine getDataProduksiLine = new GetDataProduksiLine();
                    getDataProduksiLine.execute();
                } else if (strParamAct.equals("ProduksiBar")) {
                    GetDataProduksiBar getDataProduksiBar = new GetDataProduksiBar();
                    getDataProduksiBar.execute();
                } else if (strParamAct.equals("Retur")) {
                    GetDataRetur getDataRetur = new GetDataRetur();
                    getDataRetur.execute();
                }

            }
        });
        RBLembar.setChecked(true);
        pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//                System.out.println("Krisna 3412");
                System.out.println("GestureStart Krisna");
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
//                System.out.println("Krisna 341");
                System.out.println("GestureEnd Krisna");
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
//                System.out.println("Krisna 3224");
                System.out.println("Long Krisna");
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                System.out.println("Double Krisna");

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                System.out.println("Single Krisna");
                if (strFlagPercentage.equals("True")) {
                    pieChart.setUsePercentValues(false);
                    Graphic2();
//                    theData.setValueFormatter(false);
                    pieChart.notifyDataSetChanged();
                    strFlagPercentage = "False";
                } else {
                    pieChart.setUsePercentValues(true);
                    Graphic();
                    pieChart.notifyDataSetChanged();
                    strFlagPercentage = "True";
                }
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                System.out.println("Fling Krisna");
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                System.out.println("Scale Krisna");
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                System.out.println("Translate Krisna");
            }
        });
        CBGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    strQueryPie = strQueryPie.replace("and Kat_Kelompok !='GP'", "and 1=1");
                } else {
                    strQueryPie = strQueryPie.replace("and 1=1", "and Kat_Kelompok !='GP'");
                }
//
                System.out.println("KRISNA1 strCompany " + strCompany);
                System.out.println("KRISNA2 strRole " + strRole);
                System.out.println("KRISNA3 strArea " + strArea);
                System.out.println("KRISNA4 strJenisSatuan " + strJenisSatuan);
                System.out.println("KRISNA5 iGudang " + iGudang);
                System.out.println("KRISNA6 strGudang " + strGudang);
                System.out.println("KRISNA7 strTglAwal " + strTglAwal);
                System.out.println("KRISNA8 strTglAkhir " + strTglAkhir);
                System.out.println("KRISNA9 strFlagTgl " + strFlagTgl);
                System.out.println("KRISNA10 strPenjualanTBM " + strPenjualanTBM);
                System.out.println("KRISNA11 strSales " + strSales);
                System.out.println("KRISNA12 strCity " + strCity);
                System.out.println("KRISNA13 strFlagParam " + strFlagParam);
                System.out.println("KRISNA14 strParam " + strParam);
                System.out.println("KRISNA15 strParamAct " + strParamAct);

                if (strParamAct.equals("PenjualanLine")) {
                    GetDataPenjualanLine getDataPenjualanLine = new GetDataPenjualanLine();
                    getDataPenjualanLine.execute();
                } else if (strParamAct.equals("LaporanHarian")) {
                    GetDataLaporanHarian getDataLaporanHarian = new GetDataLaporanHarian();
                    getDataLaporanHarian.execute();
                } else if (strParamAct.equals("LoadListBar")) {
                    GetDataListBar getDataListBar = new GetDataListBar();
                    getDataListBar.execute();
                } else if (strParamAct.equals("ProduksiLine")) {
                    GetDataProduksiLine getDataProduksiLine = new GetDataProduksiLine();
                    getDataProduksiLine.execute();
                } else if (strParamAct.equals("ProduksiBar")) {
                    GetDataProduksiBar getDataProduksiBar = new GetDataProduksiBar();
                    getDataProduksiBar.execute();
                } else if (strParamAct.equals("Retur")) {
                    GetDataRetur getDataRetur = new GetDataRetur();
                    getDataRetur.execute();
                }
//                GetDataListBar getDataListBar = new GetDataListBar();
//                getDataListBar.execute();
//
//                GetDataPenjualanLine getDataPenjualanLine = new GetDataPenjualanLine();
//                getDataPenjualanLine.execute();
            }
        });

//
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void Graphic() {

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
        pieDataSet.setValueLinePart1Length(0.4f);
        pieDataSet.setValueLinePart2Length(0.4f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setSliceSpace(5f);
        theData = new PieData(pieDataSet);
        theData.setValueFormatter(new PercentageFormatter());
        theData.setValueTextSize(15f);
        theData.setValueTextColor(Color.BLACK);
        pieDataSet.setColors(colors);
        pieChart.setData(theData);
        pieChart.animateY(1000);
        dataPenjualanList.clear();

    }

    public void Graphic2() {

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
        pieDataSet.setValueLinePart1Length(0.4f);
        pieDataSet.setValueLinePart2Length(0.4f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setSliceSpace(5f);
        theData = new PieData(pieDataSet);
//        theData.setValueFormatter(new PercentageFormatter());
        theData.setValueTextSize(15f);
        theData.setValueTextColor(Color.BLACK);
        pieDataSet.setColors(colors);
        pieChart.setData(theData);
        pieChart.animateY(1000);
        dataPenjualanList.clear();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return super.onOptionsItemSelected(item);
    }

    private class GetDataLaporanHarian extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(PieChartDetailActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "LaporanHarianPie");

            System.out.println("KRISNA Harian strCompany "+ strCompany);
            System.out.println("KRISNA Harian strRole "+ strRole);
            System.out.println("KRISNA Harian strArea "+ strArea);
            System.out.println("KRISNA Harian strSatuan "+ strJenisSatuan);
            System.out.println("KRISNA Harian strFlagTgl "+ strFlagTgl);
            System.out.println("KRISNA Harian strTglAwal "+ strTglAwal);
            System.out.println("KRISNA Harian strTglAkhir "+ strTglAkhir);
            System.out.println("KRISNA Harian strPenjualanTBM "+ strPenjualanTBM);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("strCompany");
            pi.setValue(strCompany);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strRole");
            pi.setValue(strRole);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strArea");
            pi.setValue(strArea);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strSatuan");
            pi.setValue(strJenisSatuan);
            pi.setType(String.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName("strFlagTgl");
            pi.setValue(strFlagTgl);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTglAwal");
            pi.setValue(strTglAwal);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTglAkhir");
            pi.setValue(strTglAkhir);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strPenjualanTBM");
            pi.setValue(strPenjualanTBM);
            pi.setType(String.class);
            request.addProperty(pi);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
                httpTransport.call("http://tempuri.org/LaporanHarianPie", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if (response.toString() != "[]") {
                try {
                    JSONArray json = new JSONArray(response.toString());
                    System.out.println("KRISNA " + response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);

                            dataPenjualanList.add(new DataPenjualanList(e.getString("BULAN"), e.getLong("Total")));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {
                try {

                    pieEntries.clear();
                    for (int x = 0; x < dataPenjualanList.size(); x++) {
                        if (dataPenjualanList.get(x).getTotal() == 0) {
                            x++;
                        } else {
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getTotal());
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getBulan());
                            pieEntries.add(new PieEntry((Long) dataPenjualanList.get(x).getTotal(), dataPenjualanList.get(x).getBulan()));
                        }
                    }
//                    Graphic();
                    if (strFlagPercentage.equals("True")) {
                        pieChart.setUsePercentValues(false);
                        Graphic2();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "False";

                    } else {
                        pieChart.setUsePercentValues(true);
                        Graphic();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "True";
                    }
                } catch (Exception ex) {

                }

            }

        }
    }

    private class GetDataPenjualanLine extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(PieChartDetailActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualanPie");

            PropertyInfo pi = new PropertyInfo();
            pi.setName("strCompany");
            pi.setValue(strCompany);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strRole");
            pi.setValue(strRole);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strArea");
            pi.setValue(strArea);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strSatuan");
            pi.setValue(strJenisSatuan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("iGudang");
            pi.setValue(iGudang);
            pi.setType(Integer.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strGudang");
            pi.setValue(strGudang);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strBulan");
            pi.setValue(strBulan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTahun");
            pi.setValue(strTahun);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strPenjualanTBM");
            pi.setValue(strPenjualanTBM);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strSales");
            pi.setValue(strSales);
            pi.setType(String.class);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
                httpTransport.call("http://tempuri.org/ViewLaporanPenjualanPie", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if (response.toString() != "[]") {
                try {
                    JSONArray json = new JSONArray(response.toString());
                    System.out.println("KRISNA " + response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);

                            dataPenjualanList.add(new DataPenjualanList(e.getString("BULAN"), e.getLong("Total")));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {
                try {

                    pieEntries.clear();
                    for (int x = 0; x < dataPenjualanList.size(); x++) {
                        if (dataPenjualanList.get(x).getTotal() == 0) {
                            x++;
                        } else {
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getTotal());
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getBulan());
                            pieEntries.add(new PieEntry((Long) dataPenjualanList.get(x).getTotal(), dataPenjualanList.get(x).getBulan()));
                        }
                    }
//                    Graphic();
                    if (strFlagPercentage.equals("True")) {
                        pieChart.setUsePercentValues(false);
                        Graphic2();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "False";

                    } else {
                        pieChart.setUsePercentValues(true);
                        Graphic();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "True";
                    }
                } catch (Exception ex) {

                }

            }

        }
    }

    private class GetDataProduksiLine extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(PieChartDetailActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "ViewProduksiPie");

            PropertyInfo pi = new PropertyInfo();
            pi.setName("strCompany");
            pi.setValue(strCompany);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strSatuan");
            pi.setValue(strJenisSatuan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("iGudang");
            pi.setValue(iGudang);
            pi.setType(Integer.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strGudang");
            pi.setValue(strGudang);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strBulan");
            pi.setValue(strBulan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTahun");
            pi.setValue(strTahun);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strMesin");
            pi.setValue(strMesin);
            pi.setType(String.class);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
                httpTransport.call("http://tempuri.org/ViewProduksiPie", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if (response.toString() != "[]") {
                try {
                    JSONArray json = new JSONArray(response.toString());
                    System.out.println("KRISNA " + response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);

                            dataPenjualanList.add(new DataPenjualanList(e.getString("BULAN"), e.getLong("Total")));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {
                try {

                    pieEntries.clear();
                    for (int x = 0; x < dataPenjualanList.size(); x++) {
                        if (dataPenjualanList.get(x).getTotal() == 0) {
                            x++;
                        } else {
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getTotal());
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getBulan());
                            pieEntries.add(new PieEntry((Long) dataPenjualanList.get(x).getTotal(), dataPenjualanList.get(x).getBulan()));
                        }
                    }
//                    Graphic();
                    if (strFlagPercentage.equals("True")) {
                        pieChart.setUsePercentValues(false);
                        Graphic2();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "False";

                    } else {
                        pieChart.setUsePercentValues(true);
                        Graphic();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "True";
                    }
                } catch (Exception ex) {

                }

            }

        }
    }

    private class GetDataProduksiBar extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(PieChartDetailActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "ViewProduksiProduksiPie");

            PropertyInfo pi = new PropertyInfo();
            pi.setName("strCompany");
            pi.setValue(strCompany);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strSatuan");
            pi.setValue(strJenisSatuan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("iGudang");
            pi.setValue(iGudang);
            pi.setType(Integer.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strGudang");
            pi.setValue(strGudang);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTglAwal");
            pi.setValue(strTglAwal);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTglAkhir");
            pi.setValue(strTglAkhir);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strMesin");
            pi.setValue(strMesin);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strFlagTgl");
            pi.setValue(strFlagTgl);
            pi.setType(String.class);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
                httpTransport.call("http://tempuri.org/ViewProduksiProduksiPie", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if (response.toString() != "[]") {
                try {
                    JSONArray json = new JSONArray(response.toString());
                    System.out.println("KRISNA " + response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);

                            dataPenjualanList.add(new DataPenjualanList(e.getString("BULAN"), e.getLong("Total")));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {
                try {

                    pieEntries.clear();
                    for (int x = 0; x < dataPenjualanList.size(); x++) {
                        if (dataPenjualanList.get(x).getTotal() == 0) {
                            x++;
                        } else {
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getTotal());
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getBulan());
                            pieEntries.add(new PieEntry((Long) dataPenjualanList.get(x).getTotal(), dataPenjualanList.get(x).getBulan()));
                        }
                    }
//                    Graphic();
                    if (strFlagPercentage.equals("True")) {
                        pieChart.setUsePercentValues(false);
                        Graphic2();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "False";

                    } else {
                        pieChart.setUsePercentValues(true);
                        Graphic();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "True";
                    }
                } catch (Exception ex) {

                }

            }

        }
    }

    private class GetDataListBar extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(PieChartDetailActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualanDistPie");

            PropertyInfo pi = new PropertyInfo();
            pi.setName("strCompany");
            pi.setValue(strCompany);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strRole");
            pi.setValue(strRole);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strArea");
            pi.setValue(strArea);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strSatuan");
            pi.setValue(strSatuan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("iGudang");
            pi.setValue(iGudang);
            pi.setType(Integer.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strGudang");
            pi.setValue(strGudang);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTglAwal");
            pi.setValue(strTglAwal);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTglAkhir");
            pi.setValue(strTglAkhir);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strFlagTgl");
            pi.setValue(strFlagTgl);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strPenjualanTBM");
            pi.setValue(strPenjualanTBM);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strSales");
            pi.setValue(strSales);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strCity");
            pi.setValue(strCity);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strFlagParam");
            pi.setValue(strFlagParam);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strParam");
            pi.setValue(strParam);
            pi.setType(String.class);
            request.addProperty(pi);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            Object response = null;
            try {
                httpTransport.call("http://tempuri.org/ViewLaporanPenjualanDistPie", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if (response.toString() != "[]") {
                try {
                    JSONArray json = new JSONArray(response.toString());
                    System.out.println("KRISNA " + response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);

                            dataPenjualanList.add(new DataPenjualanList(e.getString("BULAN"), e.getLong("Total")));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {
                try {

                    pieEntries.clear();
                    for (int x = 0; x < dataPenjualanList.size(); x++) {
                        if (dataPenjualanList.get(x).getTotal() == 0) {
                            x++;
                        } else {
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getTotal());
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getBulan());
                            pieEntries.add(new PieEntry((Long) dataPenjualanList.get(x).getTotal(), dataPenjualanList.get(x).getBulan()));
                        }
                    }
//                    Graphic();
                    if (strFlagPercentage.equals("True")) {
                        pieChart.setUsePercentValues(false);
                        Graphic2();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "False";

                    } else {
                        pieChart.setUsePercentValues(true);
                        Graphic();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "True";
                    }
                } catch (Exception ex) {

                }

            }

        }
    }

    private class GetDataRetur extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(PieChartDetailActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "ViewReturPie");
            System.out.println("KRISNA RETUR strCompany "+strCompany);
            System.out.println("KRISNA RETUR strSatuan "+strJenisSatuan);
            System.out.println("KRISNA RETUR strBulan "+strBulan);
            System.out.println("KRISNA RETUR strTahun "+strTahun);
            System.out.println("KRISNA RETUR strJenis "+strJenis);

            PropertyInfo pi = new PropertyInfo();
            pi.setName("strCompany");
            pi.setValue(strCompany);
            pi.setType(String.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName("strSatuan");
            pi.setValue(strJenisSatuan);
            pi.setType(String.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName("strBulan");
            pi.setValue(strBulan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strTahun");
            pi.setValue(strTahun);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strJenis");
            pi.setValue(strJenis);
            pi.setType(String.class);
            request.addProperty(pi);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            Object response = null;
            try {
                httpTransport.call("http://tempuri.org/ViewReturPie", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if (response.toString() != "[]") {
                try {
                    JSONArray json = new JSONArray(response.toString());
                    System.out.println("KRISNA " + response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);

                            dataPenjualanList.add(new DataPenjualanList(e.getString("BULAN"), e.getLong("Total")));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PieChartDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {
                try {

                    pieEntries.clear();
                    for (int x = 0; x < dataPenjualanList.size(); x++) {
                        if (dataPenjualanList.get(x).getTotal() == 0) {
                            x++;
                        } else {
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getTotal());
//                            System.out.println("GET TOTAL " + dataPenjualanList.get(x).getBulan());
                            pieEntries.add(new PieEntry((Long) dataPenjualanList.get(x).getTotal(), dataPenjualanList.get(x).getBulan()));
                        }
                    }
//                    Graphic();
                    if (strFlagPercentage.equals("True")) {
                        pieChart.setUsePercentValues(false);
                        Graphic2();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "False";

                    } else {
                        pieChart.setUsePercentValues(true);
                        Graphic();
                        pieChart.notifyDataSetChanged();
                        strFlagPercentage = "True";
                    }
                } catch (Exception ex) {

                }

            }

        }
    }

}
