package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.baktiar.myapplication.Model.DataOmzetList;
import com.example.baktiar.myapplication.Model.ReturList;
import com.example.baktiar.myapplication.R;
import com.example.baktiar.myapplication.Model.ReturListDecimal;
import com.example.baktiar.myapplication.View.XYMarkerView;
import com.example.baktiar.myapplication.View.XYMarkerViewRetur;
import com.example.baktiar.myapplication.View.XYMarkerViewReturLembar;
import com.example.baktiar.myapplication.View.XYMarkerViewReturLembarPenjualan;
import com.example.baktiar.myapplication.View.XYMarkerViewReturPenjualan;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GrafikBarActivity extends BaseActivity {
    private SlidrInterface slidr;

    BarChart barChart;
    ProgressDialog p;
    public static String rslt = "";
    private ArrayList<DataOmzetList> dataOmzetList;
    private ArrayList<ReturList> returLists;
    private ArrayList<ReturListDecimal> returListDecimals;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    BarDataSet barDataSet = new BarDataSet(barEntries, "");
    ArrayList<String> theDates = new ArrayList<>();
    ArrayList<BarDataSet> xAbsis = new ArrayList<>();
    String strQuery, strQuery1, strCompany, strMesin = "", strJenis = "",strFlagTgl = "", strGudang = "", strRole, strArea, strJenisData, strJenisSatuan, strTglAwal, strTglAkhir, strPenjualanTBM, strQuery2, strQuery3, strQuery4, strFlagReturGrafik, strFlagIndikator, strFlagIndikator1, strFlagTitle1, strFlagTitle2, strTahun, strBulan, strTempTglAwal, strTempTglAKhir, strFlagActivity, strFlagTitle3, strFlagTitle4, strFlagTitle5, strFlagDetail, strFlagOutput, strRBHarian;
    TextView txtShowText;
    Object response = null;
    Calendar currentCal = Calendar.getInstance();
    Integer iGudang = 0;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_bar);
        slidr = Slidr.attach(this);
        barChart = (BarChart) findViewById(R.id.bargraph);
        txtShowText = (TextView) findViewById(R.id.txtShowText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        returLists = new ArrayList<ReturList>();
        returListDecimals = new ArrayList<ReturListDecimal>();
        dataOmzetList = new ArrayList<DataOmzetList>();
        barChart.getDescription().setEnabled(false);
        Intent myIntent = getIntent(); // this is just for example purpose
        strQuery = getIntent().getStringExtra("query");
//        System.out.println("KRISNAA Grafikbar "+ strQuery);
        strQuery1 = getIntent().getStringExtra("query1");
        strQuery2 = getIntent().getStringExtra("query2");
        strQuery3 = getIntent().getStringExtra("query3");
        strFlagIndikator = getIntent().getStringExtra("flagIndikator");
        strFlagTitle1 = getIntent().getStringExtra("flagTitle1");
        strFlagTitle2 = getIntent().getStringExtra("flagTitle2");
        strFlagTitle3 = getIntent().getStringExtra("flagTitle3");
        strFlagTitle4 = getIntent().getStringExtra("flagTitle4");
        strFlagTitle5 = getIntent().getStringExtra("flagTitle5");
        strFlagOutput = getIntent().getStringExtra("flagOutput");
        strRBHarian = getIntent().getStringExtra("flagRBHarian");
        strFlagActivity = getIntent().getStringExtra("flagActivity");
        strFlagReturGrafik = getIntent().getStringExtra("flagReturGrafik");

        strCompany = getIntent().getStringExtra("strCompany");
        strRole = getIntent().getStringExtra("strRole");
        strArea = getIntent().getStringExtra("strArea");
        strJenis = getIntent().getStringExtra("strJenis");
        strJenisData = getIntent().getStringExtra("strJenisData");
        strJenisSatuan = getIntent().getStringExtra("strJenisSatuan");
        strTglAwal = getIntent().getStringExtra("strTglAwal");
        strTglAkhir = getIntent().getStringExtra("strTglAkhir");
        strPenjualanTBM = getIntent().getStringExtra("strPenjualanTBM");

        strMesin = getIntent().getStringExtra("strMesin");
        strGudang = getIntent().getStringExtra("strGudang");
        strFlagTgl = getIntent().getStringExtra("strFlagTgl");
        iGudang = getIntent().getIntExtra("iGudang", 0);

//        intent.putExtra("strCompany", strCompany);
//        intent.putExtra("strJenisSatuan", strJenisSatuan);
//        intent.putExtra("strMesin", strMesin);
//        intent.putExtra("iGudang", iGudang);
//        intent.putExtra("strGudang", strGudang);
//        intent.putExtra("strTglAwal", strTglAwal);
//        intent.putExtra("strTglAkhir", strTglAkhir);
//        intent.putExtra("strFlagTgl", strFlagTgl);

//        System.out.println("KRISNA1 strCompany " + strCompany);
//        System.out.println("KRISNA2 strRole " + strRole);
//        System.out.println("KRISNA3 strArea " + strArea);
//        System.out.println("KRISNA4 strJenisSatuan " + strJenisSatuan);
//        System.out.println("KRISNA7 strTglAwal " + strTglAwal);
//        System.out.println("KRISNA8 strTglAkhir " + strTglAkhir);
//        System.out.println("KRISNA10 strPenjualanTBM " + strPenjualanTBM);
//        System.out.println("KRISNA11 strJenisData " + strJenisData);


//        intent.putExtra("strCompany", strCompany);
//        intent.putExtra("strRole", strRole);
//        intent.putExtra("strArea", strArea);
//        intent.putExtra("strJenisData", strJenisData);
//        intent.putExtra("strJenisSatuan", strFlagLaporan);
//        intent.putExtra("strTglAwal", strCurrentDate);
//        intent.putExtra("strTglAkhir", strCurrentDate);
//        intent.putExtra("strPenjualanTBM", strPenjualanTBM);


        if (strFlagActivity.equals("Retur") == true) {
            GetDataRetur getDataRetur = new GetDataRetur();
            getDataRetur.execute();
//            if (strFlagReturGrafik.equals("Retur Penjualan") == true || strFlagReturGrafik.equals("Retur Handling") == true || strFlagReturGrafik.equals("Retur Produksi") == true) {
//                GetData2 ourData2 = new GetData2();
//                ourData2.execute();
//
//            } else if (strFlagReturGrafik.equals("Retur Semua") == true) {
//                GetData1 ourData1 = new GetData1();
//                ourData1.execute();
//            }
        } else if (strFlagActivity.equals("LaporanHarian") == true) {

            GetDataPenjualanHarian getDataPenjualanHarian = new GetDataPenjualanHarian();
            getDataPenjualanHarian.execute();
        } else {
            GetDataMesin getDataMesin = new GetDataMesin();
            getDataMesin.execute();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(strFlagTitle1);
        }
        strTahun = getIntent().getStringExtra("Tahun");
        strBulan = getIntent().getStringExtra("Bulan");
        strTempTglAwal = getIntent().getStringExtra("tempTglAwal");
        strTempTglAKhir = getIntent().getStringExtra("tempTglAKhir");
        txtShowText.setText(strFlagTitle2);
        strFlagIndikator1 = strFlagIndikator;
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setOnChartGestureListener(new OnChartGestureListener() {
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
                String mesinName = theDates.get((int) barChart.getHighlightByTouchPoint(me.getX(), me.getY()).getX());
                Intent intent = new Intent(GrafikBarActivity.this, PieChartDetailActivity.class);
                if (strFlagActivity.equals("LaporanHarian") == true) {
                    strQuery = strQuery1 + mesinName + strQuery2;
                    strQuery4 = strQuery3 + mesinName + strQuery2;
                } else {
                    strQuery = strQuery1 + mesinName + strQuery2;
                    strQuery4 = strQuery3 + mesinName + strQuery2;
                }

                if (strFlagActivity.equals("Retur") == true) {
                    strFlagDetail = strFlagTitle4 + mesinName + strFlagTitle5;
                } else if (strFlagActivity.equals("LaporanHarian") == true) {
                    strFlagDetail = strFlagTitle4 + mesinName + strFlagTitle5;
                } else {
                    strFlagDetail = mesinName + strFlagTitle5;
                }
                System.out.println("QUERNYAA 1 " + strQuery);
                System.out.println("QUERNYAA 2 " + strQuery4);
                intent.putExtra("query", strQuery);
                intent.putExtra("query4", strQuery4);
                intent.putExtra("flagTitle3", strFlagTitle3);
                intent.putExtra("flagDetail", strFlagDetail);

                if (strJenisData.equals("Bulanan")) {
                    strBulan = theDates.get((int) barChart.getHighlightByTouchPoint(me.getX(), me.getY()).getX());
                    strTahun = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

                } else if (strJenisData.equals("Tahunan")) {
                    strBulan = "";
                    strTahun = theDates.get((int) barChart.getHighlightByTouchPoint(me.getX(), me.getY()).getX());
                }
                intent.putExtra("strCompany", strCompany);
                intent.putExtra("strRole", strRole);
                intent.putExtra("strArea", strArea);
                intent.putExtra("strJenisSatuan", "LEMBAR");
                intent.putExtra("iGudang", 0);
                intent.putExtra("strGudang", "");
                intent.putExtra("strBulan", strBulan);
                intent.putExtra("strTahun", strTahun);
                intent.putExtra("strTglAwal", strTglAwal);
                intent.putExtra("strTglAkhir", strTglAkhir);
                intent.putExtra("strMesin", strMesin);
                intent.putExtra("strFlagTgl", strFlagTgl);
                intent.putExtra("strPenjualanTBM", strPenjualanTBM);
                intent.putExtra("strSales", "");
                intent.putExtra("strJenis", strJenis);

//                intent.putExtra("strJenis", theDates.get((int) barChart.getHighlightByTouchPoint(me.getX(), me.getY()).getX()));
                if (strFlagActivity.equals("LaporanHarian") == true) {
                    intent.putExtra("strParamAct", "LaporanHarian");
                }else if (strFlagActivity.equals("Retur") == true){
                    intent.putExtra("strParamAct", "Retur");
                }
                else {
                    intent.putExtra("strParamAct", "ProduksiBar");
                }
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void Graphic() {
        Date currentTime4 = Calendar.getInstance().getTime();
        xAbsis.add(barDataSet);
        // xAbsis.add(lineDataSet);

        barChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawGridBackground(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        if (strFlagActivity.equals("Retur") == true) {
            l.setEnabled(true);
            xAxis.setEnabled(true);
        } else if (strFlagActivity.equals("produksi_per_mesin") == true) {
            xAxis.setEnabled(true);
            l.setEnabled(false);
        } else if (strFlagActivity.equals("LaporanHarian") == true) {
            if (strRBHarian.equals("Mingguan")) {
                xAxis.setEnabled(false);
            } else {
                xAxis.setEnabled(true);
            }
            l.setEnabled(false);

        } else {
            l.setEnabled(false);
            xAxis.setEnabled(false);
        }

        BarDataSet setComp1 = new BarDataSet(barEntries, " ");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        if (strFlagActivity.equals("Retur") == true) {
            if (strFlagReturGrafik.equals("Retur Semua") == true) {
                setComp1.setColors(getColors());
            } else if (strFlagReturGrafik.equals("Retur Penjualan") == true || strFlagReturGrafik.equals("Retur Handling") == true || strFlagReturGrafik.equals("Retur Produksi") == true) {
                ArrayList<Integer> colors = new ArrayList<Integer>();

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
                setComp1.setColors(colors);
                l.setEnabled(false);
                xAxis.setEnabled(true);
            }

        } else {
            ArrayList<Integer> colors = new ArrayList<Integer>();

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
            setComp1.setColors(colors);
        }

        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(setComp1);
        BarData theData = new BarData(dataSets);

        setComp1.setStackLabels(new String[]{"Produksi", "Retur Penjualan"});
//        setComp1.setColors(getColors());
        barChart.animateY(1000);
        barChart.setData(theData);


        barChart.setFitBars(true);
        barChart.invalidate();
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.notifyDataSetChanged();

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

        if (strFlagActivity.equals("Retur") == true) {
            if (strFlagReturGrafik.equals("Retur Semua") == true) {
                if (strFlagOutput.equals("Presentase") == true) {
                    theData.setDrawValues(false);
                    barChart.setDrawValueAboveBar(false);
                    theData.setValueFormatter(new DecimalRemover(new DecimalFormat("#.#")));
                    XYMarkerViewRetur mv = new XYMarkerViewRetur(this, formatter);
                    mv.setChartView(barChart); // For bounds control
                    barChart.setMarker(mv); // Set the marker to the chart
//                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
//                xAxis.setValueFormatter(formatter1);
                } else {
                    theData.setDrawValues(false);
                    XYMarkerViewReturLembar mv1 = new XYMarkerViewReturLembar(this, formatter);
                    mv1.setChartView(barChart); // For bounds control
                    barChart.setMarker(mv1); // Set the marker to the chart
                    // Date currentTime5 = Calendar.getInstance().getTime();
                    //Date currentTime5 = Calendar.getInstance().getTime();

                }
            } else {
                if (strFlagOutput.equals("Presentase") == true) {
                    theData.setDrawValues(false);
                    barChart.setDrawValueAboveBar(false);
                    theData.setValueFormatter(new DecimalRemover(new DecimalFormat("#.###")));
                    XYMarkerViewReturPenjualan mv = new XYMarkerViewReturPenjualan(this, formatter);
                    mv.setChartView(barChart); // For bounds control
                    barChart.setMarker(mv); // Set the marker to the chart
//                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
//                xAxis.setValueFormatter(formatter1);
                } else if (strFlagOutput.equals("Lembar") == true) {
                    theData.setDrawValues(false);
                    XYMarkerViewReturLembarPenjualan mv1 = new XYMarkerViewReturLembarPenjualan(this, formatter);
                    mv1.setChartView(barChart); // For bounds control
                    barChart.setMarker(mv1); // Set the marker to the chart
                    // Date currentTime5 = Calendar.getInstance().getTime();
                    //Date currentTime5 = Calendar.getInstance().getTime();

                } else {
                    theData.setDrawValues(false);
                    XYMarkerView mv = new XYMarkerView(this, formatter);
                    mv.setChartView(barChart); // For bounds control
                    barChart.setMarker(mv);
                }
            }

//            theData.setValueFormatter(new MyValueFormatter());
        } else {
            theData.setDrawValues(false);
            XYMarkerView mv = new XYMarkerView(this, formatter);
            mv.setChartView(barChart); // For bounds control
            barChart.setMarker(mv); // Set the marker to the chart
            // Date currentTime5 = Calendar.getInstance().getTime();
//            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
//            xAxis.setValueFormatter(formatter);
        }

        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        //returLists.clear();
    }

    public class DecimalRemover extends PercentFormatter {

        protected DecimalFormat mFormat;

        public DecimalRemover(DecimalFormat format) {
            this.mFormat = format;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + " %";
        }
    }

    private int[] getColors() {

        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.LIBERTY_COLORS[i];
        }

        return colors;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return super.onOptionsItemSelected(item);
    }



    private class GetDataPenjualanHarian extends AsyncTask<String, String, String> {
        Calendar calendar = Calendar.getInstance();
        String CurrentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        Connection connect;
        String ConnectionResult = "";
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(GrafikBarActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {

            SoapObject request = new SoapObject("http://tempuri.org/", "PerbandinganLaporanHarian");

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
                httpTransport.call("http://tempuri.org/PerbandinganLaporanHarian", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }

            if (response.toString() != "[]") {
                try {

                    JSONArray json = new JSONArray(response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {

                            JSONObject e = json.getJSONObject(i);
//                            if(strRBHarian.equals("Mingguan")) {
//                                dataOmzetList.add(new DataOmzetList(e.getString("Tanggal").substring(3, 5), e.getLong("Total")));
//                            }
//                            else {
                            dataOmzetList.add(new DataOmzetList(e.getString("Tanggal"), e.getLong("Total")));
//                        }
                            //                            dataOmzetList.add(new DataOmzetList(CurrentDate, e.getLong("Total")));
//                        }
                        }
//                        if (json.length() >= 1) {
                        msg = "Found";
                        isSuccess = "true";
//                        } else {
//                            msg = "No Data found!";
//                            isSuccess = false;
//                        }
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
                txtShowText.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.INVISIBLE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikBarActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikBarActivity.this);
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
                    barEntries.clear();
                    theDates.clear();
                    for (int x = 0; x < dataOmzetList.size(); x++) {
                        barEntries.add(new BarEntry(x, (Long) dataOmzetList.get(x).getTotal()));
                        theDates.add(dataOmzetList.get(x).getSalesName());
                    }
                    // txtShowText.setText("Laporan Penjualan  Bulan " +spinnerblnAwal.getSelectedItem().toString()+" s/d "+spinnerblnAkhir.getSelectedItem().toString()+" Tahun " +tempTahun);
                    barChart.setVisibility(View.VISIBLE);
                    txtShowText.setVisibility(View.VISIBLE);
                    Graphic();
                } catch (Exception ex) {

                }

            }
//            return dataPenjualanList;
        }
    }

    private class GetDataMesin extends AsyncTask<String, String, String> {
        Connection connect;
        String ConnectionResult = "";
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(GrafikBarActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {

            SoapObject request = new SoapObject("http://tempuri.org/", "ViewProduksiMesin");

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
                httpTransport.call("http://tempuri.org/ViewProduksiMesin", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }

            if (response.toString() != "[]") {
                try {

                    JSONArray json = new JSONArray(response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {

                            JSONObject e = json.getJSONObject(i);
//                            String JLFaktur = e.getString("SalesName");
//                            String JLFaktur2 = e.getString("Total");
//                            if (flagReturGrafik.equals("Retur Penjualan")==true){
//                                dataOmzetList.add(new DataOmzetList(e.getString("Bulan"), e.getLong("Penjualan")));
//                            }
//                            else if (flagReturGrafik.equals("Retur Produksi")==true){
//                                dataOmzetList.add(new DataOmzetList(e.getString("Bulan"), e.getLong("Produksi")));
//                            }
//                            else {
                            dataOmzetList.add(new DataOmzetList(e.getString("SalesName"), e.getLong("Total")));
//                        }
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
                txtShowText.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.INVISIBLE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikBarActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikBarActivity.this);
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
                    barEntries.clear();
                    theDates.clear();
                    for (int x = 0; x < dataOmzetList.size(); x++) {
                        barEntries.add(new BarEntry(x, (Long) dataOmzetList.get(x).getTotal()));
                        theDates.add(dataOmzetList.get(x).getSalesName());
                    }
                    // txtShowText.setText("Laporan Penjualan  Bulan " +spinnerblnAwal.getSelectedItem().toString()+" s/d "+spinnerblnAkhir.getSelectedItem().toString()+" Tahun " +tempTahun);
                    barChart.setVisibility(View.VISIBLE);
                    txtShowText.setVisibility(View.VISIBLE);
                    Graphic();
                } catch (Exception ex) {

                }

            }
//            return dataPenjualanList;
        }
    }

    private class GetDataRetur extends AsyncTask<String, String, String> {
        Connection connect;
        String ConnectionResult = "";
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(GrafikBarActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "ViewRetur");

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

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
                httpTransport.call("http://tempuri.org/ViewRetur", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }

            if (response.toString() != "[]") {
                try {
System.out.println("KESELURUHAN "+response.toString());
                    JSONArray json = new JSONArray(response.toString());
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            if (strFlagReturGrafik.equals("Retur Penjualan") == true || strFlagReturGrafik.equals("Retur Handling") == true || strFlagReturGrafik.equals("Retur Produksi") == true) {

                                if (strFlagOutput.equals("Presentase") == true) {
                                    if (strFlagReturGrafik.equals("Retur Produksi") == true) {
                                        returListDecimals.add(new ReturListDecimal(e.getString("Bulan"), e.getDouble("Produksi"), e.getDouble("Produksi")));
                                    } else if (strFlagReturGrafik.equals("Retur Handling") == true) {
                                        returListDecimals.add(new ReturListDecimal(e.getString("Bulan"), e.getDouble("Handling"), e.getDouble("Handling")));
                                    } else {
                                        returListDecimals.add(new ReturListDecimal(e.getString("Bulan"), e.getDouble("Penjualan"), e.getDouble("Penjualan")));
                                    }
                                } else {
                                    if (strFlagReturGrafik.equals("Retur Produksi") == true) {
                                        dataOmzetList.add(new DataOmzetList(e.getString("Bulan"), e.getLong("Produksi")));
                                    } else if (strFlagReturGrafik.equals("Retur Penjualan") == true) {
                                        dataOmzetList.add(new DataOmzetList(e.getString("Bulan"), e.getLong("Penjualan")));
                                    } else {
                                        dataOmzetList.add(new DataOmzetList(e.getString("Bulan"), e.getLong("Handling")));
                                    }
                                }


                            } else if (strFlagReturGrafik.equals("Retur Semua") == true) {
                                if (strFlagOutput.equals("Presentase") == true) {
                                    returListDecimals.add(new ReturListDecimal(e.getString("Bulan"), e.getDouble("Produksi"), e.getDouble("Penjualan")));
                                } else {
                                    returLists.add(new ReturList(e.getString("Bulan"), e.getLong("Produksi"), e.getLong("Penjualan")));
                                }
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
                txtShowText.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.INVISIBLE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikBarActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(GrafikBarActivity.this);
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
                    theDates.clear();
                    barEntries.clear();
//                    theDates = new ArrayList<>();
//                    System.out.print("BUKTI " + strFlagOutput);


                    if (strFlagReturGrafik.equals("Retur Penjualan") == true || strFlagReturGrafik.equals("Retur Handling") == true || strFlagReturGrafik.equals("Retur Produksi") == true) {
//                        barEntries.clear();
//                        theDates.clear();


                        if (strFlagOutput.equals("Presentase") == true) {
                            for (int x = 0; x < returListDecimals.size(); x++) {
                                barEntries.add(new BarEntry(x, (float) returListDecimals.get(x).getPenjualan().floatValue()));
                                theDates.add(returListDecimals.get(x).getBulan());
                            }
                        } else {
                            for (int x = 0; x < dataOmzetList.size(); x++) {
                                barEntries.add(new BarEntry(x, (Long) dataOmzetList.get(x).getTotal()));
                                theDates.add(dataOmzetList.get(x).getSalesName());
                            }
                        }

                        barChart.setVisibility(View.VISIBLE);
                        txtShowText.setVisibility(View.VISIBLE);

                    } else if (strFlagReturGrafik.equals("Retur Semua") == true) {

                        if (strFlagOutput.equals("Presentase") == true) {
                            for (int x = 0; x < returListDecimals.size(); x++) {
                                barEntries.add(new BarEntry(
                                        x,
                                        new float[]{(float) returListDecimals.get(x).getProduksi().floatValue(), (float) returListDecimals.get(x).getPenjualan().floatValue()}));

                                theDates.add(returListDecimals.get(x).getBulan());
                            }
                        } else {
                            for (int x = 0; x < returLists.size(); x++) {

                                barEntries.add(new BarEntry(
                                        x,
                                        new float[]{(Long) returLists.get(x).getProduksi(), (Long) returLists.get(x).getPenjualan()}));

                                theDates.add(returLists.get(x).getBulan());
                            }
                        }
//                        Graphic();
                    }
                    Graphic();
                } catch (Exception ex) {

                }

            }

        }
    }

}
