package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.DataPenjualanList;
import com.example.baktiar.myapplication.R;
import com.example.baktiar.myapplication.View.XYMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
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
import java.util.Collections;
import java.util.List;

public class GrafikLineActivity extends BaseActivity {
    private SlidrInterface slidr;
    Object response = null;
    LineChart lineChart;
    String strQuery = null , strQuery1 = null , strQuery2 = null,strQuery3 = null,strQuery4 , strFlagTitle1, strFlagTitle2, strTempBlnAwal, strTempBlnAKhir, strTahun, strFlagTitle3,strFlagTitle4,strFlagTitle5,strFlagDetail;
    public static String rslt = "";
    ArrayList<Entry> lineEntries = new ArrayList<Entry>();
//    LineDataSet lineDataSet = new LineDataSet(lineEntries, "Grafik");
    ArrayList<String> theDates = new ArrayList<>();
    TextView txtShowText;
    ProgressDialog p;
    List<Long> myList = new ArrayList<Long>();
    private ArrayList<DataPenjualanList> dataPenjualanList;
    String strPPN = "", strParamAct="", strMesin="", strTglAwal, strTglAkhir,strRole, strArea = "", strCompany = "", strDist="", strGudang, strJenisData="", strJenisSatuan="", strPenjualanTBM="", strBulanAwal="", strBulanAkhir="";
    int iGudang = 0;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_line);
        lineChart = (LineChart) findViewById(R.id.chart);
        slidr = Slidr.attach(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtShowText = (TextView) findViewById(R.id.txtShowText);
        dataPenjualanList = new ArrayList<DataPenjualanList>();
        setSupportActionBar(toolbar);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Intent myIntent = getIntent(); // this is just for example purpose
        strParamAct = getIntent().getStringExtra("strParamAct");

        strCompany = getIntent().getStringExtra("strCompany");
        strRole = getIntent().getStringExtra("strRole");
        strArea = getIntent().getStringExtra("strArea");
        strJenisData = getIntent().getStringExtra("strJenisData");
        strJenisSatuan = getIntent().getStringExtra("strJenisSatuan");
        strPPN = getIntent().getStringExtra("strPPN");
        strDist = getIntent().getStringExtra("strDist");
        strGudang = getIntent().getStringExtra("strGudang");
        strBulanAwal = getIntent().getStringExtra("strBulanAwal");
        strBulanAkhir = getIntent().getStringExtra("strBulanAkhir");
        strTahun = getIntent().getStringExtra("strTahun");
        strPenjualanTBM = getIntent().getStringExtra("strPenjualanTBM");
        iGudang = getIntent().getIntExtra("iGudang",0);

        strMesin = getIntent().getStringExtra("strMesin");
        strTglAwal = getIntent().getStringExtra("strTglAwal");
        strTglAkhir = getIntent().getStringExtra("strTglAkhir");

        System.out.println("KRISNA strCompany "+ strCompany);
        System.out.println("KRISNA strJenisSatuan "+strJenisSatuan );
        System.out.println("KRISNA strMesin "+strMesin);
        System.out.println("KRISNA iGudang " +iGudang);
        System.out.println("KRISNA strGudang "+ strGudang);
        System.out.println("KRISNA strTglAwal " + strTglAwal);
        System.out.println("KRISNA strTglAkhir " + strTglAkhir);
        System.out.println("KRISNA strParamAct " + strParamAct);

//        intent.putExtra("strCompany", strCompany);
//        intent.putExtra("strRole", strRole);
//        intent.putExtra("strArea", strArea_key);
//        intent.putExtra("strJenisData", strJenisData);
//        intent.putExtra("strJenisSatuan", strJenisSatuan);
//        intent.putExtra("strPPN", strFlagPPN);
//        intent.putExtra("strDist", strTempDist);
//        intent.putExtra("iGudang", iFlagGudang);
//        intent.putExtra("strGudang", strGudangPilih);
//        intent.putExtra("strBulanAwal", strTempWaktu);
//        intent.putExtra("strBulanAKhir", strTempWaktu2);
//        intent.putExtra("strTahun", strTempTahun);
//        intent.putExtra("strPenjualanTBM", strGudangPilih);

        strQuery = getIntent().getStringExtra("query");
        strQuery1 = getIntent().getStringExtra("query1");
        strQuery2 = getIntent().getStringExtra("query2");
        strQuery3 = getIntent().getStringExtra("query3");
        lineChart.getDescription().setEnabled(false);
        strFlagTitle1 = getIntent().getStringExtra("flagTitle1");
        strFlagTitle2 = getIntent().getStringExtra("flagTitle2");
        strFlagTitle3 = getIntent().getStringExtra("flagTitle3");
        strFlagTitle4 = getIntent().getStringExtra("flagTitle4");
        strFlagTitle5 = getIntent().getStringExtra("flagTitle5");
        strTahun = getIntent().getStringExtra("Tahun");
        strTempBlnAwal = getIntent().getStringExtra("tempBlnAwal");
        strTempBlnAKhir = getIntent().getStringExtra("tempBlnAKhir");
        txtShowText.setText(strFlagTitle2);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(strFlagTitle1);
        }
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                Intent intent = new Intent(GrafikLineActivity.this, PieChartDetailActivity.class);
                String Bulan = theDates.get((int) lineChart.getHighlightByTouchPoint(me.getX(), me.getY()).getX());
                strQuery = strQuery1 + Bulan + strQuery2;
                strQuery4 = strQuery3 + Bulan + strQuery2;
                strFlagDetail = strFlagTitle4+Bulan+strFlagTitle5;



//                strCompany, strRole, strArea, strJenisSatuan, iGudang,strGudang, strBulan, strTahun, strPenjualanTBM, strSales);
                intent.putExtra("strCompany", strCompany);
                intent.putExtra("strRole", strRole);
                intent.putExtra("strArea", strArea);
                intent.putExtra("strJenisSatuan", strJenisSatuan);
                intent.putExtra("iGudang", iGudang);
                intent.putExtra("strGudang", strGudang);
                intent.putExtra("strBulan", theDates.get((int) lineChart.getHighlightByTouchPoint(me.getX(), me.getY()).getX()));
                intent.putExtra("strTahun", strTahun);
                intent.putExtra("strPenjualanTBM", strPenjualanTBM);
                intent.putExtra("strSales", strDist);
                intent.putExtra("strMesin", strMesin);
//                intent.putExtra("strParamAct", "LoadListLine");
                intent.putExtra("query", strQuery);
                intent.putExtra("query4", strQuery4);

                if (strParamAct.equals("Penjualan")) {
                    intent.putExtra("strParamAct", "PenjualanLine");
                }
                else if(strParamAct.equals("Produksi")){
                    intent.putExtra("strParamAct", "ProduksiLine");
                }

//                System.out.print("ini hasil querynya kawan "+strQuery);
//                System.out.print("ini hasil query1111nya kawan "+strQuery4);
                intent.putExtra("flagDetail", strFlagDetail);
                intent.putExtra("flagTitle3", strFlagTitle3);
//                intent.putExtra("passingTxtShowText", passingTxtShowText);

                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
//                query1=null;
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
        if (strParamAct.equals("Penjualan")) {
            GetDataPenjualan getDataPenjualan = new GetDataPenjualan();
            getDataPenjualan.execute();
        }
        else if(strParamAct.equals("Produksi")){
            GetDataProduksi getDataProduksi = new GetDataProduksi();
            getDataProduksi.execute();
        }
    }

    public void Graphic() {
        int minIndex = myList.indexOf(Collections.min(myList));

        // xAbsis.add(lineDataSet);
        LineDataSet setComp1 = new LineDataSet(lineEntries, "Company 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setDrawFilled(true);
        setComp1.setCircleColor(Color.BLACK);
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        LineData theData = new LineData(dataSets);
        int redColorValue = Color.WHITE;
        theData.setValueTextColor(redColorValue);

        lineChart.animateY(100);
        lineChart.setData(theData);
        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(myList.get(minIndex)-(myList.get(minIndex)/2));
//        theData.setDrawValues(false);
        lineChart.invalidate();
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
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(7);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1


        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(myList.get(minIndex)-(myList.get(minIndex)/2)); // this replaces setStartAtZero(true)

        Legend l = lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);
        xAxis.setValueFormatter(formatter);
        XYMarkerView mv = new XYMarkerView(this, formatter);
       // mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart
        myList.clear();

    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return super.onOptionsItemSelected(item);
    }
    private class GetDataProduksi extends AsyncTask<String, String, String> {

        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(GrafikLineActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "ViewProduksi");

            PropertyInfo pi = new PropertyInfo();
            pi.setName("strCompany");
            pi.setValue(strCompany);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strJenisSatuan");
            pi.setValue(strJenisSatuan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strMesin");
            pi.setValue(strMesin);
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

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
                httpTransport.call("http://tempuri.org/ViewProduksi", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if ( response.toString() != "[]") {
                try {

                    JSONArray json = new JSONArray( response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            dataPenjualanList.add(new DataPenjualanList(e.getString("Bulan"), e.getLong("Total")));
                            myList.add(e.getLong("Total"));
                        }
                        if (json.length() >= 1) {
                            msg = "Found";
                            isSuccess = "true";
                        } else {
                            msg = "No Data found!";
                            isSuccess = "false";
                        }
                    } else {
                        msg = "No Data found!";
                        isSuccess = "false";
                    }
//                    isSuccess = true;
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
                txtShowText.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikLineActivity.this);
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
            } else if(isSuccess == "fail"){
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikLineActivity.this);
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
//                    System.out.println("KRISNA StQ3 " +dataPenjualanList.size());
                    theDates.clear();
                    lineEntries.clear();
                    theDates = new ArrayList<>();

                    if (dataPenjualanList.size() == 1){
                        for (int x = 0; x < 2; x++) {
                            lineEntries.add(new Entry(x, (Long) dataPenjualanList.get(0).getTotal()));
                            theDates.add(dataPenjualanList.get(0).getBulan());
                        }
                    }
                    else
                    {
                        for (int x = 0; x < dataPenjualanList.size(); x++) {
                            lineEntries.add(new Entry(x, (Long) dataPenjualanList.get(x).getTotal()));
                            theDates.add(dataPenjualanList.get(x).getBulan());
                        }
                    }

                    txtShowText.setVisibility(View.VISIBLE);
                    lineChart.setVisibility(View.VISIBLE);
                    Graphic();
                    dataPenjualanList.clear();

                } catch (Exception ex) {

                }

            }
//            return ;
        }
    }

    private class GetDataPenjualan extends AsyncTask<String, String, String> {

        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(GrafikLineActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            System.out.println("KRISNA strCompany " +strCompany);
            System.out.println("KRISNA strRole " +strRole);
            System.out.println("KRISNA strArea " +strArea);
            System.out.println("KRISNA strJenisData " +strJenisData);
            System.out.println("KRISNA strJenisSatuan " +strJenisSatuan);
            System.out.println("KRISNA strPPN " +strPPN);
            System.out.println("KRISNA strDist " +strDist);
            System.out.println("KRISNA iGudang " +iGudang);
            System.out.println("KRISNA strGudang " +strGudang);
            System.out.println("KRISNA strBulanAwal " +strBulanAwal);
            System.out.println("KRISNA strBulanAkhir " +strBulanAkhir);
            System.out.println("KRISNA strTahun " +strTahun);
            System.out.println("KRISNA strPenjualanTBM " +strPenjualanTBM);


//            SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualan");
            SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualan");

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
            pi.setName("strJenisData");
            pi.setValue(strJenisData);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strJenisSatuan");
            pi.setValue(strJenisSatuan);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strPPN");
            pi.setValue(strPPN);
            pi.setType(String.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName("strDist");
            pi.setValue(strDist);
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
            pi.setName("strBulanAwal");
            pi.setValue(strBulanAwal);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strBulanAKhir");
            pi.setValue(strBulanAkhir);
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

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
//                httpTransport.call("http://tempuri.org/ViewLaporanPenjualan", envelope);
                httpTransport.call("http://tempuri.org/ViewLaporanPenjualan", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if ( response.toString() != "[]") {
                try {
                    System.out.println("KRISNA StQ2 " +response.toString());
                    JSONArray json = new JSONArray( response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            dataPenjualanList.add(new DataPenjualanList(e.getString("Bulan"), e.getLong("Total")));
                            myList.add(e.getLong("Total"));
                        }
                        if (json.length() >= 1) {
                            msg = "Found";
                            isSuccess = "true";
                        } else {
                            msg = "No Data found!";
                            isSuccess = "false";
                        }
                    } else {
                        msg = "No Data found!";
                        isSuccess = "false";
                    }
//                    isSuccess = true;
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
                txtShowText.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikLineActivity.this);
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
            } else if(isSuccess == "fail"){
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikLineActivity.this);
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
//                    System.out.println("KRISNA StQ3 " +dataPenjualanList.size());
                    theDates.clear();
                    lineEntries.clear();
                    theDates = new ArrayList<>();

                    if (dataPenjualanList.size() == 1){
                        for (int x = 0; x < 2; x++) {
                            lineEntries.add(new Entry(x, (Long) dataPenjualanList.get(0).getTotal()));
                            theDates.add(dataPenjualanList.get(0).getBulan());
                        }
                    }
                    else
                    {
                        for (int x = 0; x < dataPenjualanList.size(); x++) {
                            lineEntries.add(new Entry(x, (Long) dataPenjualanList.get(x).getTotal()));
                            theDates.add(dataPenjualanList.get(x).getBulan());
                        }
                    }

                    txtShowText.setVisibility(View.VISIBLE);
                    lineChart.setVisibility(View.VISIBLE);
                    Graphic();
                    dataPenjualanList.clear();

                } catch (Exception ex) {

                }

            }
//            return ;
        }
    }


}
