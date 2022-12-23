package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.DataOmzetList;
import com.example.baktiar.myapplication.R;
import com.example.baktiar.myapplication.View.XYMarkerView;
import com.github.mikephil.charting.charts.HorizontalBarChart;
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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RankingChartActivity extends AppCompatActivity {
    HorizontalBarChart barChart;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    BarDataSet barDataSet = new BarDataSet(barEntries, "");
    ArrayList<String> theDates = new ArrayList<>();
    ProgressDialog p;
    public static String rslt = "";
    TextView txtShowText;
    //    private ArrayList<DataOmzetList> dataOmzetList;
    String strQueryPassing, strTitle;
    ArrayList<BarDataSet> xAbsis = new ArrayList<>();
    private ArrayList<DataOmzetList> dataOmzetList = new ArrayList<DataOmzetList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_chart);
        strQueryPassing = getIntent().getStringExtra("strQueryPassing");
        strTitle = getIntent().getStringExtra("strTitle");
        barChart = (HorizontalBarChart) findViewById(R.id.bargraph);
        txtShowText = (TextView) findViewById(R.id.txtShowText);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        GetData ourData = new GetData();
        ourData.execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Data Rank");
        }

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
                System.out.println("KIMONO " + theDates.get((int) barChart.getHighlightByTouchPoint(me.getX(), me.getY()).getX()));
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

    private class GetData extends AsyncTask<String, String, String> {
        Connection connect;
        String ConnectionResult = "";
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(RankingChartActivity.this, R.style.MyTheme);
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

                d.strQuery = strQueryPassing;
                System.out.println("RICHARD " + strQueryPassing);
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
                    System.out.println("RIFAI " + rslt);
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {

                            JSONObject e = json.getJSONObject(i);
                            dataOmzetList.add(new DataOmzetList(e.getString("XValue"), e.getLong("Total")));
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
//                txtShowText.setVisibility(View.INVISIBLE);
//                barChart.setVisibility(View.INVISIBLE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(RankingChartActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(RankingChartActivity.this);
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

                    Graphic();
                } catch (Exception ex) {

                }

            }

        }
    }


    public void Graphic() {
        txtShowText.setText(strTitle);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(theDates));
        xAxis.setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setSpaceTop(5f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
//        rightAxis.setSpaceTop(5f);
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
        l.setEnabled(false);


        BarDataSet setComp1 = new BarDataSet(barEntries, "");

        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(setComp1);

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

        final BarData theData = new BarData(dataSets);
        barChart.animateY(1000);
        barChart.setData(theData);


        barChart.notifyDataSetChanged();

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                System.out.println("Yahoo "+ value);
//                System.out.println("Yahoo "+ theDates.get((int)value));
                return theDates.get((int) value % theDates.size());

            }

            // we don't draw numbers, so no decimal digits needed

            public int getDecimalDigits() {
                return 0;
            }
        };


        theData.setDrawValues(false);
        barChart.setDrawValueAboveBar(false);
        XYMarkerView mv = new XYMarkerView(this, formatter);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv);

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
