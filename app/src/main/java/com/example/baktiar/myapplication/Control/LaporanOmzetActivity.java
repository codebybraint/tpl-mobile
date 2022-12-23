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
import com.example.baktiar.myapplication.Model.Dist_City;
import com.example.baktiar.myapplication.Model.DataOmzetList;
import com.example.baktiar.myapplication.Model.ListBarGrafik;
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

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class LaporanOmzetActivity extends BaseActivity implements FilterBottom.BottomSheetListener {

    Object response = null;
    boolean[] checkedItemGudang;
    String[] namesStringArrayGudang;
    ArrayList<Integer> numCheckedArrayGudang = new ArrayList<>();
    ArrayList<String> listGudangChecked = new ArrayList<>();
    ArrayList<String> listGudang = new ArrayList<>();
    ProgressDialog p;

    private ArrayList<Dist_City> dist_City;


    int jsonsize = 0;

    Spinner spinnerBulan, spinnerTahun, spinnerBulanTahun, spinnerRupiah, spinnerSatuan;
    RadioButton RBTahun, RBBulan, RBTanggal;
    RadioGroup radioGroupOutput, radioGroupOutput2;
    CheckBox CBDistCheck, CBPPN, CBProvinsi, CBGudangCheck, CBPenjualanTBM;
    Button buttonShow;
    int iFlagGudang = 0;
    EditText tglAwal, tglAkhir;
    TextView txtTahun, sd, txtLaporan, txtTahun1, txtBulan;
    String queryHasilSales = "", strSatuan = "", queryHasilProvinsi = "", strGudangPilih = " ";

    RadioButton RBSatuan, RBRupiah;
    View line1, line2;
    ProgressDialog progressDialog;
    private TextView mDisplayDate1, mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateListener1, mDateListener2;
    BarData theData = new BarData();

    String strFlagTitle1, strFlagTitle2, strFlagJenisLaporan = "tahunan", strTempTglAwal = "", strTglAwal = "", strTglAkhir = "", strTempTglAKhir = "", strFlagJenisData, strTahun, strBulan, strFlagIndikator, strFlagTitle3, strFlagTitle5;
    String strFlagPPN = "", strPenjualanTBM = "";


    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listSpinnerRupiah = new ArrayList<>();
    ArrayList<String> listSpinnerSatuan = new ArrayList<>();
    ArrayList<String> listJenisLaporan = new ArrayList<>();
    ArrayList<Integer> numCheckedArrayDist = new ArrayList<>();
    ArrayList<Integer> numCheckedArrayProv = new ArrayList<>();
    ArrayList<String> Sales = new ArrayList<>();
    ArrayList<String> Provinsi = new ArrayList<>();
    ArrayList<String> listDistributor = new ArrayList<>();
    ArrayList<String> listProvinsi = new ArrayList<>();
    ArrayList<Long> listTotalCity = new ArrayList<>();
    ArrayList<String> listCityName = new ArrayList<>();
    ArrayList<String> listCityProvinsiName = new ArrayList<>();


    ArrayList<String> listProvinsiName = new ArrayList<>();
    ArrayList<Long> listTotalProvinsi = new ArrayList<>();

    ArrayList<String> listSalesName = new ArrayList<>();
    ArrayList<Long> listTotalSales = new ArrayList<>();

    String[] namesStringArrayDist, namesStringArrayProv;
    boolean[] checkedItemsDist, checkedItemsProv;
    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    String strRole, strArea_key = "", strCompany = "ALL", strFlagTgl = "TAHUNAN", strJenisData = "", strJenisSatuan = "";
    Button btnFilter;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_omzet);

        RBSatuan = (RadioButton) findViewById(R.id.RBSatuan);
        RBRupiah = (RadioButton) findViewById(R.id.RBRupiah);
        radioGroupOutput = (RadioGroup) findViewById(R.id.radioGroupOutput);
        radioGroupOutput2 = (RadioGroup) findViewById(R.id.radioGroupOutput2);
        RBTahun = (RadioButton) findViewById(R.id.RBTahun);
        RBBulan = (RadioButton) findViewById(R.id.RBBulan);
        RBTanggal = (RadioButton) findViewById(R.id.RBTanggal);
        mDisplayDate1 = (TextView) findViewById(R.id.tglAwal);
        mDisplayDate2 = (TextView) findViewById(R.id.tglAkhir);
        sd = (TextView) findViewById(R.id.sd);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        txtLaporan = (TextView) findViewById(R.id.txtLaporan);
        txtTahun1 = (TextView) findViewById(R.id.txtTahun1);
        txtBulan = (TextView) findViewById(R.id.txtBulan);

        spinnerBulan = (Spinner) findViewById(R.id.spinnerBulan);
        spinnerTahun = (Spinner) findViewById(R.id.spinnerTahun);
        spinnerBulanTahun = (Spinner) findViewById(R.id.spinnerBulanTahun);
        spinnerRupiah = (Spinner) findViewById(R.id.spinnerRupiah);
        spinnerSatuan = (Spinner) findViewById(R.id.spinnerSatuan);

        line1 = (View) findViewById(R.id.line1);
        line2 = (View) findViewById(R.id.line2);

        tglAwal = (EditText) findViewById(R.id.tglAwal);
        tglAkhir = (EditText) findViewById(R.id.tglAkhir);
        btnFilter = (Button) findViewById(R.id.btnFilter);

        dist_City = new ArrayList<Dist_City>();

        buttonShow = (Button) findViewById(R.id.buttonShow);
        CBProvinsi = (CheckBox) findViewById(R.id.CBProvinsi);
        CBDistCheck = (CheckBox) findViewById(R.id.CBDistCheck);
        CBPPN = (CheckBox) findViewById(R.id.CBPPN);
        CBGudangCheck = (CheckBox) findViewById(R.id.CBDepartemen);
        strRole = getIntent().getStringExtra("role");
        CBPenjualanTBM = (CheckBox) findViewById(R.id.CBPenjualanTBM);
        CBPenjualanTBM.setChecked(true);

        if (strRole.equals("Area Manager")) {
            CBPenjualanTBM.setVisibility(View.GONE);
            strArea_key = getIntent().getStringExtra("area_key");
        } else {
            CBPenjualanTBM.setVisibility(View.VISIBLE);
        }


        invisible();
        loadBulan();
        CheckPenjualanTBM();
        checkConnection();
        progressDialog = new ProgressDialog(LaporanOmzetActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setTitle("Fetching Data");
        loadTanggal();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Omzet per distributor");
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
        RBSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strJenisData = "Lembar";
                strFlagJenisData = "Satuan";

                spinnerRupiah.setEnabled(false);
                spinnerSatuan.setEnabled(true);
                RBRupiah.setChecked(false);
                RBSatuan.setChecked(true);
            }
        });
        RBRupiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strJenisData = "Rupiah";
                strFlagJenisData = "Rupiah";
                spinnerRupiah.setEnabled(true);
                spinnerSatuan.setEnabled(false);
                RBSatuan.setChecked(false);
                RBRupiah.setChecked(true);
            }
        });
        radioGroupOutput2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBTahun) {
                    spinnerTahun.setVisibility(View.VISIBLE);
                    spinnerBulan.setVisibility(View.INVISIBLE);
                    tglAkhir.setVisibility(View.INVISIBLE);
                    tglAwal.setVisibility(View.INVISIBLE);
                    sd.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    txtTahun1.setVisibility(View.VISIBLE);
                    spinnerBulanTahun.setVisibility(View.INVISIBLE);
                    strFlagJenisLaporan = "tahunan";
                    strFlagTgl = "TAHUNAN";
                } else if (i == R.id.RBBulan) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    spinnerBulan.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.VISIBLE);
                    txtTahun1.setVisibility(View.INVISIBLE);
                    spinnerBulanTahun.setVisibility(View.VISIBLE);
                    tglAkhir.setVisibility(View.INVISIBLE);
                    tglAwal.setVisibility(View.INVISIBLE);
                    sd.setVisibility(View.INVISIBLE);
                    strFlagJenisLaporan = "bulanan";
                    strFlagTgl = "BULANAN";
                } else if (i == R.id.RBTanggal) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    spinnerBulan.setVisibility(View.INVISIBLE);
                    tglAkhir.setVisibility(View.VISIBLE);
                    tglAwal.setVisibility(View.VISIBLE);
                    sd.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    txtTahun1.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    spinnerBulanTahun.setVisibility(View.INVISIBLE);
                    strFlagJenisLaporan = "range tanggal";
                    strFlagTgl = "RANGE";
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

        if (strFlagJenisData == "Rupiah") {
            if (spinnerRupiah.getSelectedItem().toString() == "Net") {
                strJenisSatuan = "Net";
                strFlagTitle1 = "Omzet net distributor ";
                strSatuan = "juta rupiah";
            } else {
                strJenisSatuan = "Gross";
                strFlagTitle1 = "Omzet gross distributor ";
                strSatuan = "juta rupiah";
            }
        } else {
            if (spinnerSatuan.getSelectedItem().toString() == "Lembar") {
                strJenisSatuan = "Lembar";
                strFlagTitle1 = "Omzet distributor ";
                strSatuan = "lembar";
            } else {
                strJenisSatuan = "TONASE";
                strFlagTitle1 = "Omzet distributor ";
                strSatuan = "ton";
            }
        }
        if (strFlagJenisLaporan == "bulanan") {
            strTglAwal = spinnerBulan.getSelectedItem().toString();
            strTglAkhir = spinnerBulanTahun.getSelectedItem().toString();
            strBulan = spinnerBulan.getSelectedItem().toString();
            strTahun = spinnerBulanTahun.getSelectedItem().toString();

            strFlagIndikator = "bulanan";
            strFlagTitle2 = String.format("Bulan %s %s (%s)", strBulan, strTahun, strSatuan);
        } else if (strFlagJenisLaporan == "range tanggal") {
            strTglAwal = tglAwal.getText().toString();
            strTglAkhir = tglAkhir.getText().toString();
            strTempTglAwal = tglAwal.getText().toString();
            strTempTglAKhir = tglAkhir.getText().toString();

            strFlagIndikator = "range tanggal";
            strFlagTitle2 = String.format("%s s/d %s (%s)", strTempTglAwal, strTempTglAKhir, strSatuan);
        } else if (strFlagJenisLaporan == "tahunan") {
            strTglAwal = spinnerTahun.getSelectedItem().toString();
            strTahun = spinnerTahun.getSelectedItem().toString();

            strFlagIndikator = "tahunan";
            strFlagTitle2 = String.format("Tahun %s (%s)", strTahun, strSatuan);
        } else {
        }

        if (strFlagIndikator == "bulanan") {

            strFlagTitle3 = "Komposisi penjualan";
            strFlagTitle5 = " Bulan " + strBulan + " Tahun " + strTahun;
        } else if (strFlagIndikator == "tahunan") {


            strFlagTitle3 = "Komposisi penjualan";
            strFlagTitle5 = " Tahun " + strTahun;
        } else if (strFlagIndikator == "range tanggal") {

            strFlagTitle3 = "Komposisi penjualan";
            strFlagTitle5 = " " + strTempTglAwal + " s/d " + strTempTglAKhir;
        } else {

        }
        SyncDataPassing mydataPassing = new SyncDataPassing();
        mydataPassing.execute();
    }

    public void CheckPenjualanTBM() {
        if (strCompany.equals("TBM")) {
            CBPenjualanTBM.setVisibility(View.GONE);
            CBGudangCheck.setVisibility(View.VISIBLE);
        } else if (strCompany.equals("TAA")) {
            CBPenjualanTBM.setVisibility(View.VISIBLE);
            CBGudangCheck.setVisibility(View.VISIBLE);
        } else {
            CBPenjualanTBM.setVisibility(View.GONE);
            CBGudangCheck.setVisibility(View.GONE);
        }
    }


    public void invisible() {
        spinnerTahun.setVisibility(View.VISIBLE);
        spinnerBulan.setVisibility(View.INVISIBLE);
        tglAkhir.setVisibility(View.INVISIBLE);
        tglAwal.setVisibility(View.INVISIBLE);
        sd.setVisibility(View.INVISIBLE);
        txtTahun.setVisibility(View.INVISIBLE);
        txtBulan.setVisibility(View.INVISIBLE);
        txtTahun1.setVisibility(View.VISIBLE);
        spinnerBulanTahun.setVisibility(View.INVISIBLE);
        strFlagJenisLaporan = "tahunan";
        RBTahun.setChecked(true);
        RBRupiah.setChecked(true);
        strFlagJenisData = "Rupiah";
        strJenisData = "Rupiah";
        strFlagPPN = "";
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
                        LaporanOmzetActivity.this,
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
                        LaporanOmzetActivity.this,
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
                String date2 = month2 + "/" + day2 + "/" + year2;
                mDisplayDate2.setText(date2);
            }
        };

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

            AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanOmzetActivity.this);
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

    public void loadBulan() {
        listJenisLaporan.add("tahunan");
        listJenisLaporan.add("bulanan");
        listJenisLaporan.add("range tanggal");

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear - 5; i <= thisYear; i++) {
            listyearView.add(Integer.toString(i));
        }

        listSpinnerRupiah.add("Net");
        listSpinnerRupiah.add("Gross");
        listSpinnerSatuan.add("Lembar");
        listSpinnerSatuan.add("Tonase");

        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            listBulanView.add(month_name);
        }
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(LaporanOmzetActivity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(LaporanOmzetActivity.this,
                R.layout.spinner_value, listyearView);
        ArrayAdapter<String> jenisLaporan_adapter = new ArrayAdapter<String>(LaporanOmzetActivity.this,
                R.layout.spinner_value, listJenisLaporan);
        ArrayAdapter<String> jenisLaporanRupiah_adapter = new ArrayAdapter<String>(LaporanOmzetActivity.this,
                R.layout.spinner_value, listSpinnerRupiah);
        ArrayAdapter<String> jenisSatuan_adapter = new ArrayAdapter<String>(LaporanOmzetActivity.this,
                R.layout.spinner_value, listSpinnerSatuan);
        spinnerSatuan.setAdapter(jenisSatuan_adapter);
        spinnerTahun.setAdapter(tahun_adapter);
        spinnerBulanTahun.setAdapter(tahun_adapter);
        spinnerBulan.setAdapter(bulan_adapter);
        spinnerRupiah.setAdapter(jenisLaporanRupiah_adapter);
        spinnerBulanTahun.setSelection(5);
        spinnerTahun.setSelection(5);
    }


    public void Select(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {

            case R.id.CBDistCheck:
                if (checked) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LaporanOmzetActivity.this);
                    builder.setTitle("Pilih Distributor");
                    builder.setMultiChoiceItems(namesStringArrayDist, checkedItemsDist, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                            if (isChecked) {
                                //add the position that the user clicked on
                                numCheckedArrayDist.add(position);
                            } else if (numCheckedArrayDist.contains(position)) {
                                //remove the position that the user clicked on
                                numCheckedArrayDist.remove(Integer.valueOf(position));
                            }
                        }
                    });
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Sales.clear();
                            queryHasilSales = "";

                            //add selected items into selected item array      still need to figure out how to remove it from the array
                            for (int i = 0; i < numCheckedArrayDist.size(); i++) {
                                Sales.add("'" + namesStringArrayDist[numCheckedArrayDist.get(i)] + "'");
                            }
                            queryHasilSales = ("" + Sales + "").replace("[", "(").replace("]", ")");

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CBDistCheck.setChecked(false);
                        }
                    });
                    builder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            for (int i = 0; i < checkedItemsDist.length; i++) {
                                checkedItemsDist[i] = false;
                                numCheckedArrayDist.clear();
//                                selectedItems.clear();
                            }
                            Sales.clear();
                            queryHasilSales = "";

                            CBDistCheck.setChecked(false);
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    queryHasilSales = "";
                }
                break;

            case R.id.CBProvinsi:
                if (checked) {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(LaporanOmzetActivity.this);
                    builder1.setTitle("Pilih Provinsi");
                    builder1.setMultiChoiceItems(namesStringArrayProv, checkedItemsProv, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                            if (isChecked) {
                                //add the position that the user clicked on
                                numCheckedArrayProv.add(position);
                            } else if (numCheckedArrayProv.contains(position)) {
                                //remove the position that the user clicked on
                                numCheckedArrayProv.remove(Integer.valueOf(position));
                            }
                        }
                    });
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Provinsi.clear();
                            queryHasilProvinsi = "";
                            //add selected items into selected item array      still need to figure out how to remove it from the array
                            for (int i = 0; i < numCheckedArrayProv.size(); i++) {
                                Provinsi.add("'" + namesStringArrayProv[numCheckedArrayProv.get(i)] + "'");
                            }

                            queryHasilProvinsi = ("" + Provinsi + "").replace("[", "(").replace("]", ")");

                        }
                    });

                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CBProvinsi.setChecked(false);
                        }
                    });
                    builder1.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            for (int i = 0; i < checkedItemsProv.length; i++) {
                                checkedItemsProv[i] = false;
                                numCheckedArrayProv.clear();
                            }
                            Provinsi.clear();
                            queryHasilProvinsi = "";
                            CBProvinsi.setChecked(false);
                        }
                    });
                    AlertDialog dialog = builder1.create();
                    dialog.show();
                } else {
                    queryHasilProvinsi = "";
                }
                break;
            case R.id.CBPPN:
                if (checked) {
                    strFlagPPN = "PPN";
                } else {
                    strFlagPPN = "";
                }
                break;
            case R.id.CBPenjualanTBM:
                if (checked) {
                    strPenjualanTBM = "";

                } else {
                    strPenjualanTBM = "True";
                }
                break;
            case R.id.CBDepartemen:
                if (checked) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LaporanOmzetActivity.this);
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

                            listGudangChecked.clear();
                            strGudangPilih = " ";

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
        CheckPenjualanTBM();

        SyncData mydata = new SyncData();
        mydata.execute();
    }


    private class SyncData extends AsyncTask<String, String, String> {
        Connection connect;
        String ConnectionResult = "";
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(LaporanOmzetActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {

            SoapObject request = new SoapObject("http://tempuri.org/", "LoadDataAwal");

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
            pi.setValue(strArea_key);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strAct");
            pi.setValue("Omzet");
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
                httpTransport.call("http://tempuri.org/LoadDataAwal", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }

            if (response.toString() != "[]") {
                try {

                    JSONArray json = new JSONArray(response.toString());
                    if (json != null) {
                        listProvinsi.clear();
                        listDistributor.clear();
                        listGudang.clear();
                        jsonsize = json.length();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);

                            if (!e.getString("ProvinsiName").equals("null")) {
                                if (!listProvinsi.contains(e.getString("ProvinsiName"))) {
                                    listProvinsi.add(e.getString("ProvinsiName"));
                                }
                            }
                            if (!e.getString("SalesName").equals("null")) {
                                if (!listDistributor.contains(e.getString("SalesName"))) {
                                    listDistributor.add(e.getString("SalesName"));
                                }
                            }
                            if (!strCompany.equals("ALL")) {
                                if (!e.getString("Dep_name").equals("null")) {
                                    if (!listGudang.contains(e.getString("Dep_name"))) {
                                        listGudang.add(e.getString("Dep_name"));
                                    }
                                }
                            }
                            if (isCancelled()) break;

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
            super.onPostExecute(msg);
            p.dismiss();
            if (isSuccess == "false") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanOmzetActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanOmzetActivity.this);
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
                    Collections.sort(listProvinsi, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return s1.compareTo(s2);
                        }
                    });
                    namesStringArrayDist = new String[listDistributor.size()];
                    namesStringArrayProv = new String[listProvinsi.size()];
                    namesStringArrayGudang = new String[listGudang.size()];
                    for (int i = 0; i < listDistributor.size(); i++) {
                        namesStringArrayDist[i] = listDistributor.get(i);
                    }
                    for (int i = 0; i < listProvinsi.size(); i++) {
                        namesStringArrayProv[i] = listProvinsi.get(i);
                    }
                    for (int i = 0; i < listGudang.size(); i++) {
                        namesStringArrayGudang[i] = listGudang.get(i);
                    }
                    checkedItemsProv = new boolean[listProvinsi.size()];
                    checkedItemsDist = new boolean[listDistributor.size()];
                    checkedItemGudang = new boolean[listGudang.size()];

                } catch (Exception ex) {

                }

            }
        }
    }

    private class SyncDataPassing extends AsyncTask<String, String, String> {
        Connection connect;
        String ConnectionResult = "";
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(LaporanOmzetActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();

            listSalesName.clear();
            listTotalSales.clear();

            listProvinsiName.clear();
            listTotalProvinsi.clear();

            listCityName.clear();
            listCityProvinsiName.clear();
            listTotalCity.clear();

            dist_City.clear();

        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {

            SoapObject request = new SoapObject("http://tempuri.org/", "ViewLaporanPenjualanDist");

            System.out.println("KRISNA strCompany " + strCompany);
            System.out.println("KRISNA strRole" + strRole);
            System.out.println("KRISNA strArea_key" + strArea_key);
            System.out.println("KRISNA strJenisData" + strJenisData);
            System.out.println("KRISNA strJenisSatuan" + strJenisSatuan);
            System.out.println("KRISNA strFlagPPN" + strFlagPPN);
            System.out.println("KRISNA iFlagGudang" + iFlagGudang);
            System.out.println("KRISNA strGudangPilih" + strGudangPilih);
            System.out.println("KRISNA strTglAwal" + strTglAwal);
            System.out.println("KRISNA strTglAkhir" + strTglAkhir);
            System.out.println("KRISNA strFlagTgl" + strFlagTgl);
            System.out.println("KRISNA strPenjualanTBM" + strPenjualanTBM);
            System.out.println("KRISNA queryHasilSales" + queryHasilSales);
            System.out.println("KRISNA queryHasilProvinsi" + queryHasilProvinsi);

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
            pi.setValue(strArea_key);
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
            pi.setValue(strFlagPPN);
            pi.setType(String.class);
            request.addProperty(pi);


            pi = new PropertyInfo();
            pi.setName("iGudang");
            pi.setValue(iFlagGudang);
            pi.setType(Integer.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strGudang");
            pi.setValue(strGudangPilih);
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
            pi.setValue(queryHasilSales);
            pi.setType(String.class);
            request.addProperty(pi);

            pi = new PropertyInfo();
            pi.setName("strProv");
            pi.setValue(queryHasilProvinsi);
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
                httpTransport.call("http://tempuri.org/ViewLaporanPenjualanDist", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }


            if (response.toString() != "[]") {
                try {
                    int iTotalProvinsi = 0, iTotalSales = 0, iTotalCity = 0;
                    System.out.println("KRISNA QRY " + response.toString());
                    JSONArray json = new JSONArray(response.toString());
                    if (json != null) {
                        jsonsize = json.length();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);


                            if (listProvinsiName.contains(e.getString("ProvinsiName")) && listCityName.contains(e.getString("City_Name"))) {
                                iTotalCity = listCityName.indexOf(e.getString("City_Name"));
                                listTotalCity.set(iTotalCity, listTotalCity.get(iTotalCity) + e.getLong("Total"));
                            } else {
                                listTotalCity.add(e.getLong("Total"));
                                listCityName.add(e.getString("City_Name"));
                                listCityProvinsiName.add(e.getString("ProvinsiName"));
                            }
                            if (listProvinsiName.contains(e.getString("ProvinsiName"))) {
                                iTotalProvinsi = listProvinsiName.indexOf(e.getString("ProvinsiName"));
                                listTotalProvinsi.set(iTotalProvinsi, listTotalProvinsi.get(iTotalProvinsi) + e.getLong("Total"));
                            } else {
                                listTotalProvinsi.add(e.getLong("Total"));
                                listProvinsiName.add(e.getString("ProvinsiName"));
                            }
                            if (listSalesName.contains(e.getString("SalesName"))) {
                                iTotalSales = listSalesName.indexOf(e.getString("SalesName"));
                                listTotalSales.set(iTotalSales, listTotalSales.get(iTotalSales) + e.getLong("Total"));
                            } else {
                                listTotalSales.add(e.getLong("Total"));
                                listSalesName.add(e.getString("SalesName"));
                            }

                            dist_City.add(new Dist_City(e.getString("SalesName"), e.getString("City_Name"), e.getLong("Total")));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanOmzetActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanOmzetActivity.this);
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
                    Intent intent = new Intent(LaporanOmzetActivity.this, ListViewBarChartActivity.class);
                    intent.putExtra("listSalesName", listSalesName);
                    intent.putExtra("listTotalSales", listTotalSales);

                    intent.putExtra("listProvinsiName", listProvinsiName);
                    intent.putExtra("listTotalProvinsi", listTotalProvinsi);

                    intent.putExtra("listCityName", listCityName);
                    intent.putExtra("listCityProvinsiName", listCityProvinsiName);
                    intent.putExtra("listTotalCity", listTotalCity);

                    intent.putExtra("strFlagTitle2", strFlagTitle2);

                    intent.putExtra("strActivity", "strActivity");
                    intent.putExtra("dist_City", dist_City);

                    //Query 3+2 = Tonase || Query 1 + 2 = Lembaran


                    intent.putExtra("flagTitle3", strFlagTitle3);
                    intent.putExtra("flagTitle5", strFlagTitle5);

                    intent.putExtra("strCompany", strCompany);
                    intent.putExtra("strRole", strRole);
                    intent.putExtra("strArea", strArea_key);
                    intent.putExtra("strJenisSatuan", strJenisSatuan);
                    intent.putExtra("iGudang", iFlagGudang);
                    intent.putExtra("strGudang", strGudangPilih);
                    intent.putExtra("strTglAwal", strTglAwal);
                    intent.putExtra("strTglAkhir", strTglAkhir);
                    intent.putExtra("strFlagTgl", strFlagTgl);
                    intent.putExtra("strPenjualanTBM", strPenjualanTBM);
//                    intent.putExtra("strFlagParam", strFlagParam);
//                    intent.putExtra("strParam", strParam);
                    intent.putExtra("strParamAct", "LoadListBar");


                    startActivity(intent);

                } catch (Exception ex) {
                }

            }
        }
    }

}
