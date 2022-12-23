//package com.example.baktiar.myapplication.Control;
package com.example.baktiar.myapplication.Control;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Dist_City;
import com.example.baktiar.myapplication.R;
import com.example.baktiar.myapplication.View.XYMarkerView;
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

import java.util.ArrayList;
import java.util.List;

public class ListViewBarChartActivity extends BaseActivity {
    //    protected Typeface tfLight;
    int iFlag = 0;
    ArrayList<String> listProvinsiName = new ArrayList<>();
    ArrayList<String> listSalesName = new ArrayList<>();
    ArrayList<String> listTItle = new ArrayList<>();
    ArrayList<Long> listTotalProvinsi = new ArrayList<Long>();
    ArrayList<Long> listTotalSales = new ArrayList<>();

    ArrayList<Long> listTotalCity = new ArrayList<>();
    ArrayList<String> listCityName = new ArrayList<>();
    ArrayList<String> listCityProvinsiName = new ArrayList<>();

    ArrayList<Integer> listIndikator = new ArrayList<>();
//    ArrayList<String> listDistributorName = new ArrayList<>();
//    ArrayList<String> listCityDistributorName = new ArrayList<>();

    IAxisValueFormatter formatter;
    private ArrayList<Dist_City> dist_City = new ArrayList<Dist_City>();
    static ArrayList<ArrayList<String>> listXaxis = new ArrayList<ArrayList<String>>();
    String strActivity = "", strFlagIndikatorTitle = "", strQuery1, strFlagDetail, strQuery2, strQuery3, strFlagTitle3, strFlagTitle5, strQueryPassing1, strQueryPassing2, strTitleInfo = "";
    String strCompany, strRole, strArea, strJenisSatuan, strGudang, strFlagParam="",strFlagTgl, strPenjualanTBM,strParamAct, strTglAwal, strTglAkhir;
    Integer iGudang = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_view_bar_chart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_listview_chart);
        setContentView(R.layout.activity_list_view_bar_chart);
        setTitle("ListViewBarChartActivity");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listXaxis.clear();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Omzet Penjualan");
        }
        listProvinsiName = getIntent().getStringArrayListExtra("listProvinsiName");
        listSalesName = getIntent().getStringArrayListExtra("listSalesName");
        listTotalProvinsi = (ArrayList<Long>) getIntent().getSerializableExtra("listTotalProvinsi");
        listTotalSales = (ArrayList<Long>) getIntent().getSerializableExtra("listTotalSales");
        strFlagIndikatorTitle = getIntent().getStringExtra("strFlagTitle2");
        strActivity = getIntent().getStringExtra("strActivity");

        listCityName = getIntent().getStringArrayListExtra("listCityName");
        listCityProvinsiName = getIntent().getStringArrayListExtra("listCityProvinsiName");
        listTotalCity = (ArrayList<Long>) getIntent().getSerializableExtra("listTotalCity");

        dist_City = (ArrayList<Dist_City>) getIntent().getSerializableExtra("dist_City");


        strCompany = getIntent().getStringExtra("strCompany");
        strRole = getIntent().getStringExtra("strRole");
        strArea = getIntent().getStringExtra("strArea");
        strJenisSatuan = getIntent().getStringExtra("strJenisSatuan");
        iGudang = getIntent().getIntExtra("iGudang",0);
        strGudang = getIntent().getStringExtra("strGudang");
        strFlagTgl = getIntent().getStringExtra("strFlagTgl");
        strTglAwal = getIntent().getStringExtra("strTglAwal");
        strTglAkhir = getIntent().getStringExtra("strTglAkhir");
        strPenjualanTBM = getIntent().getStringExtra("strPenjualanTBM");
        strParamAct = getIntent().getStringExtra("strParamAct");

//        intent.putExtra("strCompany", strCompany);
//        intent.putExtra("strRole", strRole);
//        intent.putExtra("strArea", strArea_key);
//        intent.putExtra("strSatuan", strSatuan);
//        intent.putExtra("iGudang", iFlagGudang);
//        intent.putExtra("strGudang", strGudangPilih);
////                    intent.putExtra("strTglAwal", strTglAwal);
////                    intent.putExtra("strTglAkhir", strTglAkhir);
//        intent.putExtra("strFlagTgl", strFlagTgl);
//        intent.putExtra("strPenjualanTBM", strPenjualanTBM);
////                    intent.putExtra("strFlagParam", strFlagParam);
////                    intent.putExtra("strParam", strParam);
//        intent.putExtra("strParamAct", "LoadListBar");

        strQuery1 = getIntent().getStringExtra("query1");
        strQuery2 = getIntent().getStringExtra("query2");
        strQuery3 = getIntent().getStringExtra("query3");

        strFlagTitle3 = getIntent().getStringExtra("flagTitle3");
        strFlagTitle5 = getIntent().getStringExtra("flagTitle5");



        ListView lv = findViewById(R.id.listView1);

        ArrayList<BarData> list = new ArrayList<>();


        // 20 items
        if (strActivity.equals("strActivity")) {
            for (int i = 0; i < 2; i++) {
                list.add(generateData(i + 1));
            }
        }

        //PROVINSI CITY
        else if (strActivity.equals("prov")) {
            for (int i = 0; i < listTotalProvinsi.size(); i++) {
                list.add(generateData(i));
            }
        }

        //DISTRIBUTOR CITY
        else if (strActivity.equals("dist")) {
            for (int i = 0; i < listSalesName.size(); i++) {
                list.add(generateData(i));
            }
        } else {
            for (int i = 0; i < listSalesName.size(); i++) {
                list.add(generateData(i));
            }
        }


        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
    }


    private class ChartDataAdapter extends ArrayAdapter<BarData> {

        ChartDataAdapter(Context context, List<BarData> objects) {
            super(context, 0, objects);
        }

        @SuppressLint("InflateParams")
        @SuppressWarnings("unchecked")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            BarData data = getItem(position);

            final ViewHolder holder;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item_barchart, null);
                holder.chart = convertView.findViewById(R.id.chart);
                holder.txtJudul = convertView.findViewById(R.id.txtJudul);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // apply styling
            if (data != null) {
//                data.setValueTypeface(tfLight);
                data.setValueTextColor(Color.BLACK);
            }
            holder.txtJudul.setText(listTItle.get(position) + " " + strFlagIndikatorTitle);
            holder.chart.getDescription().setEnabled(false);
            holder.chart.setDrawGridBackground(false);
            holder.chart.setDoubleTapToZoomEnabled(false);



            holder.chart.setOnChartGestureListener(new OnChartGestureListener() {

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

                    if (strActivity.equals("strActivity")) {
                        Intent intent = new Intent(ListViewBarChartActivity.this, ListViewBarChartActivity.class);
                        intent.putExtra("listSalesName", listSalesName);
                        intent.putExtra("listTotalSales", listTotalSales);

                        intent.putExtra("listProvinsiName", listProvinsiName);
                        intent.putExtra("listTotalProvinsi", listTotalProvinsi);

                        intent.putExtra("listCityName", listCityName);
                        intent.putExtra("listCityProvinsiName", listCityProvinsiName);
                        intent.putExtra("listTotalCity", listTotalCity);

                        intent.putExtra("dist_City", dist_City);
                        intent.putExtra("strFlagTitle2", strFlagIndikatorTitle);
                        intent.putExtra("query1", strQuery1);
                        intent.putExtra("query2", strQuery2);
                        intent.putExtra("query3", strQuery3);

                        intent.putExtra("flagTitle3", strFlagTitle3);
                        intent.putExtra("flagTitle5", strFlagTitle5);

                        intent.putExtra("strCompany", strCompany);
                        intent.putExtra("strRole", strRole);
                        intent.putExtra("strArea", strArea);
                        intent.putExtra("strJenisSatuan", strJenisSatuan);
                        intent.putExtra("iGudang", iGudang);
                        intent.putExtra("strGudang", strGudang);
                        intent.putExtra("strTglAwal",strTglAwal);
                        intent.putExtra("strTglAkhir", strTglAkhir);
                        intent.putExtra("strPenjualanTBM", strPenjualanTBM);

                        intent.putExtra("strFlagTgl", strFlagTgl);

                        intent.putExtra("strFlagParam", strFlagParam);

                        intent.putExtra("strParamAct", strParamAct);

                        if (position == 0) {
                            intent.putExtra("strActivity", "prov");
                        } else {
                            intent.putExtra("strActivity", "dist");
                        }
                        startActivity(intent);


                    } else {
                        String strNama = "", strIndikator = "", strFlagName = "", strIndikatorKota = "";

                        strFlagName = " Kota : " + listXaxis.get(position).get((int) holder.chart.getHighlightByTouchPoint(motionEvent.getX(), motionEvent.getY()).getX());
                        strIndikatorKota = listXaxis.get(position).get((int) holder.chart.getHighlightByTouchPoint(motionEvent.getX(), motionEvent.getY()).getX());
                        if (strActivity.equals("dist")) {
                            strFlagParam = "SALES";

                            strNama = " Sales : " + listSalesName.get(position);
                            strIndikator = listSalesName.get(position);
                            strQueryPassing1 = strQuery1 + strIndikatorKota + "' AND SalesName = '" + strIndikator + strQuery2;
                            strQueryPassing2 = strQuery3 + strIndikatorKota + "' AND SalesName = '" + strIndikator + strQuery2;

                        } else {
                            strFlagParam = "PROVINSI";

                            strNama = " Provinsi : " + listProvinsiName.get(position);
                            strIndikator = listProvinsiName.get(position);
                            strQueryPassing1 = strQuery1 + strIndikatorKota + "' AND G.ProvinsiName = '" + strIndikator + strQuery2;
                            strQueryPassing2 = strQuery3 + strIndikatorKota + "' AND G.ProvinsiName = '" + strIndikator + strQuery2;

                        }
                        Intent intent = new Intent(ListViewBarChartActivity.this, PieChartDetailActivity.class);

                        intent.putExtra("strCompany", strCompany);
                        intent.putExtra("strRole", strRole);
                        intent.putExtra("strArea", strArea);
                        intent.putExtra("strJenisSatuan", strJenisSatuan);
                        intent.putExtra("iGudang", iGudang);
                        intent.putExtra("strGudang", strGudang);
                        intent.putExtra("strTglAwal",strTglAwal);
                        intent.putExtra("strTglAkhir", strTglAkhir);
                        intent.putExtra("strPenjualanTBM", strPenjualanTBM);
                        intent.putExtra("strSales", strNama);
                        intent.putExtra("strFlagTgl", strFlagTgl);
                        intent.putExtra("strCity", strIndikatorKota);
                        intent.putExtra("strFlagParam", strFlagParam);
                        intent.putExtra("strParam", strIndikator);
                        intent.putExtra("strParamAct", strParamAct);



                        strFlagDetail = strNama + "\n" + strFlagName + "\n" + strFlagTitle5;
                        intent.putExtra("query", strQueryPassing1);
                        intent.putExtra("query4", strQueryPassing2);
                        intent.putExtra("flagTitle3", strFlagTitle3);
                        intent.putExtra("flagDetail", strFlagDetail);
                        startActivity(intent);

                    }
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

            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f);


            if (strActivity.equals("strActivity")) {
                if (position == 0) {
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(listProvinsiName));
                    formatter = new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {

                            return listProvinsiName.get((int) value % listProvinsiName.size());
                        }
                    };
                } else {
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(listSalesName));
                    formatter = new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {

                            return listSalesName.get((int) value % listSalesName.size());
                        }
                    };
                }
            } else {

                xAxis.setValueFormatter(new IndexAxisValueFormatter(listXaxis.get(position)));
                formatter = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {

                        return listXaxis.get(position).get((int) value % listXaxis.get(position).size());
                    }
                };


            }

            XYMarkerView mv = new XYMarkerView(getApplicationContext(), formatter);
            mv.setChartView(holder.chart); // For bounds control
            holder.chart.setMarker(mv);
            YAxis leftAxis = holder.chart.getAxisLeft();

            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f);

            YAxis rightAxis = holder.chart.getAxisRight();
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f);

            // set data
            holder.chart.setData(data);
            holder.chart.setFitBars(true);


            holder.chart.animateY(700);

            Legend l = holder.chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);
            l.setEnabled(false);
            xAxis.setEnabled(false);


            return convertView;
        }

        private class ViewHolder {

            BarChart chart;
            TextView txtJudul;

        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Bar data
     */
    private BarData generateData(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        String strKelompok = "", strProvinsi = "";
        int iFlag = 0;
        BarDataSet d;

        ArrayList<String> optionlist = new ArrayList<String>();

        if (strActivity.equals("strActivity")) {
            if (cnt == 1) {
                for (int i = 0; i < listProvinsiName.size(); i++) {
//                (Long) dataOmzetList.get(x).getTotal()
                    entries.add(new BarEntry(i, (Long) listTotalProvinsi.get(i)));
                }
                strKelompok = "Provinsi";
                strTitleInfo = "Berdasarkan Provinsi";
            } else {
                for (int i = 0; i < listSalesName.size(); i++) {
                    entries.add(new BarEntry(i, (Long) listTotalSales.get(i)));
                }
                strKelompok = "SalesName";
                strTitleInfo = "Berdasarkan Distributor";
            }
            listTItle.add(strTitleInfo);
            d = new BarDataSet(entries, strKelompok);
        }

        //PROVINSI CITY
        else if (strActivity.equals("prov")) {

            //PROVINSI CITY
            for (int x = 0; x < listCityProvinsiName.size(); x++) {
                if (listCityProvinsiName.get(x).equals(listProvinsiName.get(cnt))) {
                    entries.add(new BarEntry(iFlag, (Long) listTotalCity.get(x)));
                    optionlist.add(listCityName.get(x));
                    iFlag++;
                }
            }
            listIndikator.add(iFlag);
            listXaxis.add(optionlist);
            d = new BarDataSet(entries, listProvinsiName.get(cnt));
            strTitleInfo = listProvinsiName.get(cnt);
            listTItle.add(strTitleInfo);
        }

        //DISTRIBUTOR CITY
        else if (strActivity.equals("dist")) {

            for (int x = 0; x < dist_City.size(); x++) {
                if (dist_City.get(x).getDistName().equals(listSalesName.get(cnt))) {
                    entries.add(new BarEntry(iFlag, (Long) dist_City.get(x).getTotal()));
                    optionlist.add(dist_City.get(x).getCityName());
                    iFlag++;
                }
            }


            listXaxis.add(optionlist);
            d = new BarDataSet(entries, listSalesName.get(cnt));
            strTitleInfo = listSalesName.get(cnt);
            listTItle.add(strTitleInfo);
        } else {

            for (int x = 0; x < dist_City.size(); x++) {
                if (dist_City.get(x).getDistName().equals(listSalesName.get(cnt))) {
                    entries.add(new BarEntry(iFlag, (Long) dist_City.get(x).getTotal()));
                    optionlist.add(dist_City.get(x).getCityName());
                    iFlag++;
                }
            }

            listXaxis.add(optionlist);
            d = new BarDataSet(entries, listSalesName.get(cnt));
            strTitleInfo = listSalesName.get(cnt);
            listTItle.add(strTitleInfo);
        }

        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setBarShadowColor(Color.rgb(203, 203, 203));
        d.setDrawValues(false);
        ArrayList<IBarDataSet> sets = new ArrayList<>();
        sets.add(d);

        BarData cd = new BarData(sets);
        cd.setBarWidth(0.9f);
        return cd;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return super.onOptionsItemSelected(item);
    }
}
