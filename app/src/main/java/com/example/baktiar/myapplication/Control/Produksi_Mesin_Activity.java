package com.example.baktiar.myapplication.Control;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.DataOmzetList;
import com.example.baktiar.myapplication.Model.SalesListName;
import com.example.baktiar.myapplication.R;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DataSnapshot;

public class Produksi_Mesin_Activity extends BaseActivity implements FilterBottom.BottomSheetListener  {
    boolean[] checkedItemGudang;
    String[] namesStringArrayGudang;
    ArrayList<Integer> numCheckedArrayGudang = new ArrayList<>();
    ArrayList<String> listGudangChecked = new ArrayList<>();
    ArrayList<String> listGudang = new ArrayList<>();
    int iFlagGudang = 0;
    ProgressDialog p;

    Spinner spinnerBulan, spinnerTahun, spinnerBulanTahun;

    RadioGroup radioGroupOutput, radioGroupOutput2;
    CheckBox CBMesinCheck, CBGudangCheck;
    EditText tglAwal, tglAkhir;
    TextView txtTahun, sd, txtLaporan, txtTahun1, txtBulan;
    String  queryHasil = "",  strGudangPilih = "";
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    BarDataSet barDataSet = new BarDataSet(barEntries, "Total dalam juta rupiah");
    public static String rslt = "";
    Button buttonShow;
    RadioButton RBTahun, RBBulan, RBTanggal, RBLembar, RBTonase;
    View line1, line2;
    BarData theData = new BarData();
    ProgressDialog progressDialog;
    Object response = null;
    String strTglAwal ="", strTglAkhir="", strFlagTgl="", strCompany = "ALL", flagJenisLaporan, flagTitle1, flagTitle2, tempTglAwal, tempTglAKhir, flagJenisData,Tahun, Bulan, flagIndikator, flagTitle3, flagTitle5, flagGrafik,flagSatuan;
    ArrayList<BarDataSet> xAbsis = new ArrayList<>();
    private TextView mDisplayDate1, mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateListener1, mDateListener2;
    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> selectedItems = new ArrayList<>();
    ArrayList<Integer> numCheckedArray = new ArrayList<>();
    ArrayList<String> SalesName = new ArrayList<>();
    ArrayList<String> listSalesName = new ArrayList<>();
    String[] namesStringArray;
    boolean[] checkedItems;
    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    int  flagData, flagDistCheck;
    Button btnFilter;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produksi__mesin_);
        radioGroupOutput = (RadioGroup) findViewById(R.id.radioGroupOutput);
        radioGroupOutput2 = (RadioGroup) findViewById(R.id.radioGroupOutput2);
        mDisplayDate1 = (TextView) findViewById(R.id.tglAwal);
        mDisplayDate2 = (TextView) findViewById(R.id.tglAkhir);
        sd = (TextView) findViewById(R.id.sd);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        txtTahun1 = (TextView) findViewById(R.id.txtTahun1);
        txtBulan = (TextView) findViewById(R.id.txtBulan);
        txtLaporan = (TextView) findViewById(R.id.txtLaporan);
        RBTahun = (RadioButton) findViewById(R.id.RBTahun);
        RBBulan = (RadioButton) findViewById(R.id.RBBulan);
        RBTanggal = (RadioButton) findViewById(R.id.RBTanggal);
        RBTonase = (RadioButton) findViewById(R.id.RBTonase);
        RBLembar = (RadioButton) findViewById(R.id.RBLembar);
        spinnerBulan = (Spinner) findViewById(R.id.spinnerBulan);
        spinnerTahun = (Spinner) findViewById(R.id.spinnerTahun);
        spinnerBulanTahun = (Spinner) findViewById(R.id.spinnerBulanTahun);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        line1 = (View) findViewById(R.id.line1);
        line2 = (View) findViewById(R.id.line2);

        tglAwal = (EditText) findViewById(R.id.tglAwal);
        tglAkhir = (EditText) findViewById(R.id.tglAkhir);



        CBMesinCheck = (CheckBox) findViewById(R.id.CBMesinCheck);
        CBGudangCheck = (CheckBox) findViewById(R.id.CBDepartemen);
        buttonShow = (Button) findViewById(R.id.buttonShow);


        barDataSet.setColors(Color.RED);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Produksi per mesin");
        }
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterBottom filterBottom = new FilterBottom();
                filterBottom.show(getSupportFragmentManager(), "exampleBottomSort");
            }
        });
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition1();
            }
        });
        invisible();
        loadBulan();
        loadTanggal();
        radioGroupOutput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBLembar){
                    flagGrafik ="Lembar";
                }
                else if (i == R.id.RBTonase){
                    flagGrafik="TONASE";
                }
            }
        });
        radioGroupOutput2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBTahun) {
                    spinnerTahun.setVisibility(View.VISIBLE);
                    txtTahun1.setVisibility(View.VISIBLE);
                    spinnerBulan.setVisibility(View.INVISIBLE);
                    tglAkhir.setVisibility(View.INVISIBLE);
                    tglAwal.setVisibility(View.INVISIBLE);
                    sd.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    spinnerBulanTahun.setVisibility(View.INVISIBLE);
                    flagJenisLaporan = "tahunan";
                } else if (i == R.id.RBBulan) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    txtTahun1.setVisibility(View.INVISIBLE);
                    spinnerBulan.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.VISIBLE);
                    spinnerBulanTahun.setVisibility(View.VISIBLE);
                    tglAkhir.setVisibility(View.INVISIBLE);
                    tglAwal.setVisibility(View.INVISIBLE);
                    sd.setVisibility(View.INVISIBLE);
                    flagJenisLaporan = "bulanan";
                } else if (i == R.id.RBTanggal) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    spinnerBulan.setVisibility(View.INVISIBLE);
                    tglAkhir.setVisibility(View.VISIBLE);
                    tglAwal.setVisibility(View.VISIBLE);
                    sd.setVisibility(View.VISIBLE);
                    txtTahun1.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    spinnerBulanTahun.setVisibility(View.INVISIBLE);
                    flagJenisLaporan = "range tanggal";
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void condition1() {
        if (flagGrafik =="Lembar"){
            flagSatuan = "lembar";
        }
        else {
            flagSatuan = "ton";
        }
        if (flagJenisLaporan == "bulanan") {
            Bulan = spinnerBulan.getSelectedItem().toString();
            Tahun = spinnerBulanTahun.getSelectedItem().toString();

            flagIndikator = "bulanan";
            flagTitle1 = "Produksi per mesin";
            strTglAwal = Bulan;
            strTglAkhir = Tahun;
            strFlagTgl = "BULANAN";
            //flagTitle2 = "Bulan " + Bulan + " " + Tahun + " (lembar)";
            flagTitle2 = String.format("Bulan %s %s (%s)",Bulan,Tahun,flagSatuan);
        } else if (flagJenisLaporan == "range tanggal") {
            tempTglAwal = tglAwal.getText().toString();
            tempTglAKhir = tglAkhir.getText().toString();

            flagIndikator = "range tanggal";
            flagTitle1 = "Produksi per mesin";
            strTglAwal = tempTglAwal;
            strTglAkhir = tempTglAKhir;
            strFlagTgl = "RANGE";
            //flagTitle2 = tempTglAwal + " s/d " + tempTglAKhir + " (lembar)";
            flagTitle2 =  String.format("%s s/d %s (%s)",tempTglAwal,tempTglAKhir,flagSatuan);
        } else if (flagJenisLaporan == "tahunan") {
            Tahun = spinnerTahun.getSelectedItem().toString();

            strTglAwal = Tahun;
            strTglAkhir = "";
            strFlagTgl = "TAHUNAN";
            flagIndikator = "tahunan";
            flagTitle1 = "Produksi per mesin";
            //flagTitle2 = "Tahun " + Tahun + " (lembar)";
            flagTitle2 = String.format("Tahun %s (%s)",Tahun,flagSatuan);
        } else {
        }
        if (flagIndikator == "bulanan") {
            flagTitle3 = "Komposisi produksi mesin";
            flagTitle5 = " Bulan " + Bulan + " Tahun " + Tahun;
        } else if (flagIndikator == "tahunan") {
            flagTitle3 = "Komposisi produksi mesin";
            flagTitle5 = " Tahun " + Tahun;
        } else if (flagIndikator == "range tanggal") {
            flagTitle3 = "Komposisi produksi mesin";
            flagTitle5 = " " + tempTglAwal + " s/d " + tempTglAKhir;
        } else {

        }
        Intent intent = new Intent(Produksi_Mesin_Activity.this, GrafikBarActivity.class);

        intent.putExtra("flagIndikator", flagIndikator);
        intent.putExtra("flagTitle1", flagTitle1);
        intent.putExtra("flagTitle2", flagTitle2);
        intent.putExtra("flagTitle3", flagTitle3);
        intent.putExtra("flagTitle5", flagTitle5);
        intent.putExtra("flagActivity", "produksi_per_mesin");
        intent.putExtra("Tahun", Tahun);
        intent.putExtra("Bulan", Bulan);
        intent.putExtra("tempTglAwal", tempTglAwal);
        intent.putExtra("tempTglAKhir", tempTglAKhir);


        intent.putExtra("strCompany", strCompany);
        intent.putExtra("strJenisSatuan", flagGrafik);
        intent.putExtra("strJenisData", "");
        intent.putExtra("strMesin", queryHasil);
        intent.putExtra("iGudang", iFlagGudang);
        intent.putExtra("strGudang", strGudangPilih);
        intent.putExtra("strTglAwal", strTglAwal);
        intent.putExtra("strTglAkhir", strTglAkhir);
        intent.putExtra("strFlagTgl", strFlagTgl);

        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }



    public void invisible() {
        flagJenisLaporan = "tahunan";
        flagGrafik = "Lembar";
        flagSatuan = "lembar";
        spinnerBulan.setVisibility(View.INVISIBLE);
        tglAkhir.setVisibility(View.INVISIBLE);
        tglAwal.setVisibility(View.INVISIBLE);
        sd.setVisibility(View.INVISIBLE);
        txtTahun.setVisibility(View.INVISIBLE);
        spinnerBulanTahun.setVisibility(View.INVISIBLE);
        txtBulan.setVisibility(View.INVISIBLE);
        flagJenisData = "Rupiah";
        flagData = 1;
        RBTahun.setChecked(true);
        RBLembar.setChecked(true);

    }

    public void loadTanggal() {
        mDisplayDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year1 = cal.get(Calendar.YEAR);
                int month1 = cal.get(Calendar.MONTH);
                int day1 = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Produksi_Mesin_Activity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener1,
                        year1, month1, day1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                month1 = month1 + 1;
                Log.d("TAG", "onDateSet : date : " + day1 + "/" + month1 + "/" + year1);
                String date = month1 + "/" + day1 + "/" + year1;
                mDisplayDate1.setText(date);
            }
        };

        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year2 = cal.get(Calendar.YEAR);
                int month2 = cal.get(Calendar.MONTH);
                int day2 = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Produksi_Mesin_Activity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener2,
                        year2, month2, day2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year2, int month2, int day2) {
                month2 = month2 + 1;
                // Log.d("TAG", "onDateSet2 : date2 : " +month2 + "/" + day2  + "/" + year2);
                String date2 = month2 + "/" + day2 + "/" + year2;
                mDisplayDate2.setText(date2);
            }
        };

    }

    public void loadBulan() {
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear - 5; i <= thisYear; i++) {
            listyearView.add(Integer.toString(i));
        }
        checkConnection();
        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());

            listBulanView.add(month_name);
        }
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(Produksi_Mesin_Activity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(Produksi_Mesin_Activity.this,
                R.layout.spinner_value, listyearView);
        spinnerTahun.setAdapter(tahun_adapter);
        spinnerBulanTahun.setAdapter(tahun_adapter);
        spinnerBulan.setAdapter(bulan_adapter);
        spinnerBulanTahun.setSelection(5);
        spinnerTahun.setSelection(5);

    }

    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }

    public void checkConnection() {

        if (isOnline()) {

            SyncData mydata = new SyncData();
            mydata.execute();


        } else {

            AlertDialog.Builder dialog = new AlertDialog.Builder(Produksi_Mesin_Activity.this);
            dialog.setCancelable(false);
            dialog.setMessage("Koneksi Internet bermasalah, silahkan cek lagi..");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();

        }

    }

    public void Select(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {

            case R.id.CBMesinCheck:
                if (checked) {


                    final AlertDialog.Builder builder = new AlertDialog.Builder(Produksi_Mesin_Activity.this);
                    builder.setTitle("Pilih Distributor");
                    builder.setMultiChoiceItems(namesStringArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                            if (isChecked) {
                                //add the position that the user clicked on
                                numCheckedArray.add(position);
                            } else if (numCheckedArray.contains(position)) {
                                //remove the position that the user clicked on
                                numCheckedArray.remove(Integer.valueOf(position));
                            }
                        }
                    });
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            flagDistCheck = 1;
                            SalesName.clear();
                            queryHasil = "";

                            //add selected items into selected item array      still need to figure out how to remove it from the array
                            for (int i = 0; i < numCheckedArray.size(); i++) {
                                SalesName.add("'" + namesStringArray[numCheckedArray.get(i)] + "'");
                            }

                            queryHasil = ("" + SalesName + "").replace("[", "(").replace("]", ")");

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CBMesinCheck.setChecked(false);
                        }
                    });
                    builder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            for (int i = 0; i < checkedItems.length; i++) {
                                checkedItems[i] = false;
                                numCheckedArray.clear();
                                selectedItems.clear();
                            }
                            SalesName.clear();
                            queryHasil = "";

                            CBMesinCheck.setChecked(false);
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    queryHasil = "";
                    flagDistCheck = 0;
                }
                break;
            case R.id.CBDepartemen:
                if (checked) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Produksi_Mesin_Activity.this);
                    builder.setTitle("Pilih Departemen");
                    builder.setMultiChoiceItems(namesStringArrayGudang, checkedItemGudang, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                            if (isChecked) {
                                //add the position that the user clicked on
                                numCheckedArrayGudang.add(position);
                            } else if (numCheckedArrayGudang.contains(position)) {
                                //remove the position that the user clicked on
                                numCheckedArrayGudang.remove(Integer.valueOf(position));
                            }
                        }
                    });
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                            strFlagJenisDistributor = "dipilih";
                            listGudangChecked.clear();
                            strGudangPilih = "";
                            iFlagGudang = 0;
                            for (int i = 0; i < numCheckedArrayGudang.size(); i++) {
                                listGudangChecked.add("'" + namesStringArrayGudang[numCheckedArrayGudang.get(i)] + "'");
                                iFlagGudang++;
                            }

                            strGudangPilih = ("" + listGudangChecked + "").replace("[", "(").replace("]", ")");


                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CBGudangCheck.setChecked(false);
                        }
                    });
                    builder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            for (int i = 0; i < checkedItemGudang.length; i++) {
                                checkedItemGudang[i] = false;
                                numCheckedArrayGudang.clear();

                            }
                            listGudangChecked.clear();
                            strGudangPilih = "";

                            iFlagGudang = 0;

                            CBGudangCheck.setChecked(false);
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    strGudangPilih = "";
                    iFlagGudang = 0;
                }
                break;
        }
    }


    public ArrayList<String> getList(Calendar startDate, Calendar endDate) {
        ArrayList<String> list = new ArrayList<String>();
        while (startDate.compareTo(endDate) <= 0) {
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public String getDate(Calendar cld) {
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                + cld.get(Calendar.DAY_OF_MONTH);
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curDate;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClickedFilter(String strCompanyFilter) {
        strCompany = strCompanyFilter;
        if (!strCompany.equals("ALL")) {
            SyncData mydata = new SyncData();
            mydata.execute();
            CBGudangCheck.setVisibility(View.VISIBLE);
            CBMesinCheck.setVisibility(View.VISIBLE);
        }
        else{
            CBGudangCheck.setVisibility(View.GONE);
            CBMesinCheck.setVisibility(View.GONE);
        }
    }


    private class SyncData extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(Produksi_Mesin_Activity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "LoadProduksi");

            PropertyInfo pi = new PropertyInfo();
            pi.setName("strCompany");
            pi.setValue(strCompany);
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
                httpTransport.call("http://tempuri.org/LoadProduksi", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }

            if (response.toString() != "[]") {
                try {

                    JSONArray json = new JSONArray(response.toString());
                    if (json != null) {
                        listSalesName.clear();
                        listGudang.clear();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            String JLFaktur = e.getString("SalesName");


                            if (!e.getString("SalesName").equals("null")) {
                                listSalesName.add(e.getString("SalesName"));
                            }

                            if (!e.getString("Dep_name").equals("null")) {
                                if (!listGudang.contains(e.getString("Dep_name"))) {
                                    listGudang.add(e.getString("Dep_name"));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(Produksi_Mesin_Activity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(Produksi_Mesin_Activity.this);
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
                    namesStringArray = new String[listSalesName.size()];
                    for (int i = 0; i < listSalesName.size(); i++) {
                        namesStringArray[i] = listSalesName.get(i);
                    }
                    checkedItems = new boolean[listSalesName.size()];


                    namesStringArrayGudang = new String[listGudang.size()];
                    for (int i = 0; i < listGudang.size(); i++) {
                        namesStringArrayGudang[i] = listGudang.get(i);
                    }

                    checkedItemGudang = new boolean[listGudang.size()];
                } catch (Exception ex) {

                }

            }
        }
    }




}
