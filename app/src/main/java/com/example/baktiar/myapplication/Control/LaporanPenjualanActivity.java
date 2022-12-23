package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

//import com.github.mikephil.charting.charts.BarChart;
import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.DataPenjualanList;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
import com.example.baktiar.myapplication.Model.SalesListName;
import com.example.baktiar.myapplication.R;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DataSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LaporanPenjualanActivity extends BaseActivity implements FilterBottom.BottomSheetListener {

    ProgressDialog p;
    RadioGroup radioGroupOutput, radioGroupOutput2;
    RadioButton RBSatuan, RBRupiah, RBTahun, RBBulan;

    boolean[] checkedItemGudang;
    String[] namesStringArrayGudang;
    ArrayList<Integer> numCheckedArrayGudang = new ArrayList<>();


    Spinner spinnerDist, spinnerTahun, spinnerRupiah, thnRangetgl, spinnerblnAwal, spinnerblnAkhir, spinnerSatuan;
    CheckBox CBDist, CBPPN, CBGudangCheck, CBPenjualanTBM;
    Button buttonShow;
    TextView sd, txtTahun, txtJenisLaporan, txtTahun1, txtBulan;

    View line1, line2, line3;
    int iFlagGudang = 0;
    Object response = null;

    String strFlagDist, strFlagJenisLaporan, strTempWaktu2 = "", strTempWaktu = "", strTempDist = "", strTempTahun, strFlagJenisData, strFlagTitle1, strFlagTitle2, strFlagTitle3, strFlagTitle4, strFlagTitle5, strBulanAwal, strBulanAkhir;
    String strSatuan = "", strFlagPPN = "", strGudangPilih = "";
    ArrayList<String> listGudangChecked = new ArrayList<>();
    ArrayList<String> listGudang = new ArrayList<>();
    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listJenisLaporan = new ArrayList<>();
    ArrayList<String> listSpinnerRupiah = new ArrayList<>();
    ArrayList<String> listSalesName = new ArrayList<>();
    ArrayList<String> listSpinnerSatuan = new ArrayList<>();

    String strRole, strArea_key = "", strCompany = "ALL",  strJenisSatuan = "", strPenjualanTBM = "";

    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    int flagDistributor;
    Button btnFilter;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penjualan);


        sd = (TextView) findViewById(R.id.sd);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        txtBulan = (TextView) findViewById(R.id.txtBulan);
        txtTahun1 = (TextView) findViewById(R.id.txtTahun1);
        txtJenisLaporan = (TextView) findViewById(R.id.txtJenisLaporan);

        spinnerDist = (Spinner) findViewById(R.id.spinnerDist);
        spinnerTahun = (Spinner) findViewById(R.id.spinnerTahun);
        spinnerblnAwal = (Spinner) findViewById(R.id.blnAwal);
        spinnerblnAkhir = (Spinner) findViewById(R.id.blnAKhir);
        spinnerRupiah = (Spinner) findViewById(R.id.spinnerRupiah);
        spinnerSatuan = (Spinner) findViewById(R.id.spinnerSatuan);
        thnRangetgl = (Spinner) findViewById(R.id.thnRangetgl);

        line1 = (View) findViewById(R.id.line1);
        line2 = (View) findViewById(R.id.line2);
        line3 = (View) findViewById(R.id.line3);

        buttonShow = (Button) findViewById(R.id.buttonShow);
        CBPPN = (CheckBox) findViewById(R.id.CBPPN);
        CBDist = (CheckBox) findViewById(R.id.CBDist);
        CBGudangCheck = (CheckBox) findViewById(R.id.CBDepartemen);

        RBSatuan = (RadioButton) findViewById(R.id.RBSatuan);
        RBRupiah = (RadioButton) findViewById(R.id.RBRupiah);
        RBTahun = (RadioButton) findViewById(R.id.RBTahun);
        RBBulan = (RadioButton) findViewById(R.id.RBBulan);
        radioGroupOutput2 = (RadioGroup) findViewById(R.id.radioGroupOutput2);
        radioGroupOutput = (RadioGroup) findViewById(R.id.radioGroupOutput);

        btnFilter = (Button) findViewById(R.id.btnFilter);


        CBPenjualanTBM = (CheckBox) findViewById(R.id.CBPenjualanTBM);
        CBPenjualanTBM.setChecked(true);
        strRole = getIntent().getStringExtra("role");

        if (strRole.equals("Area Manager")) {
            strArea_key = getIntent().getStringExtra("area_key");
            CBPenjualanTBM.setVisibility(View.GONE);
        } else {
            CBPenjualanTBM.setVisibility(View.VISIBLE);
        }

        CheckPenjualanTBM();
        invisible();
        loadBulan();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Penjualan bulanan");
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
                if (spinnerblnAwal.getSelectedItemPosition() > spinnerblnAkhir.getSelectedItemPosition()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanPenjualanActivity.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("Bulan Awal > Bulan Akhir");
                    dialog.setMessage("Coba cek data bulan anda");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                } else {
                    CheckData();
                }
            }
        });

        RBSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    spinnerblnAkhir.setVisibility(View.INVISIBLE);
                    spinnerblnAwal.setVisibility(View.INVISIBLE);
                    sd.setVisibility(View.INVISIBLE);
                    thnRangetgl.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    txtTahun1.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    strFlagJenisLaporan = "tahunan";
                } else if (i == R.id.RBBulan) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    spinnerblnAwal.setVisibility(View.VISIBLE);
                    spinnerblnAkhir.setVisibility(View.VISIBLE);
                    sd.setVisibility(View.VISIBLE);
                    thnRangetgl.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    txtTahun1.setVisibility(View.INVISIBLE);
                    txtBulan.setVisibility(View.VISIBLE);
                    strFlagJenisLaporan = "rangeBulan";
                }
            }
        });
    }

    //kondisi untuk checkbox dan radiobutton
    public void CheckData() {
        if (strFlagDist == "Distributor") {
            strTempDist = spinnerDist.getSelectedItem().toString();
        } else {
            strTempDist = "";
            strFlagDist = "";
        }
        if (strFlagPPN == "PPN") {

        } else {

        }
        if (strFlagJenisData == "Rupiah") {
            if (spinnerRupiah.getSelectedItem().toString() == "Net") {
                strJenisSatuan = "Net";
                strFlagTitle1 = "Laporan penjualan net";
            } else {
                strJenisSatuan = "Gross";
                strFlagTitle1 = "Laporan penjualan gross";
            }
            strSatuan = "juta rupiah";

        } else {
            if (spinnerSatuan.getSelectedItem().toString() == "Lembar") {
                strJenisSatuan = "Lembar";
                strSatuan = "lembar";
            } else {
                strJenisSatuan = "Tonase";
                strSatuan = "ton";
            }
            strFlagTitle1 = "Laporan penjualan";

        }

        if (strFlagJenisLaporan == "rangeBulan") {
            strTempWaktu = String.valueOf(spinnerblnAwal.getSelectedItemPosition() + 1).toString();
            strTempWaktu2 = String.valueOf(spinnerblnAkhir.getSelectedItemPosition() + 1).toString();
            strBulanAwal = spinnerblnAwal.getSelectedItem().toString();
            strBulanAkhir = spinnerblnAkhir.getSelectedItem().toString();
            strTempTahun = thnRangetgl.getSelectedItem().toString();
            strFlagTitle2 = String.format("%s %s Bulan %s s/d %s %s (%s)", strFlagDist, strTempDist, strBulanAwal, strBulanAkhir, strTempTahun, strSatuan);

        } else if (strFlagJenisLaporan == "tahunan") {
            strTempTahun = spinnerTahun.getSelectedItem().toString();
            strFlagTitle2 = String.format("%s %s Tahun %s (%s)", strFlagDist, strTempDist, strTempTahun, strSatuan);
        } else {
        }

        if (strFlagDist == "Distributor") {
            strFlagTitle3 = "Komposisi penjualan";
            strFlagTitle4 = "Distributor " + strTempDist + " Bulan ";
            strFlagTitle5 = " Tahun " + strTempTahun;
        } else {
            strFlagTitle3 = "Komposisi penjualan";
            strFlagTitle4 = "Semua Distributor Bulan ";
            strFlagTitle5 = " Tahun " + strTempTahun;

        }


        Intent intent = new Intent(LaporanPenjualanActivity.this, GrafikLineActivity.class);
        intent.putExtra("strCompany", strCompany);
        intent.putExtra("strRole", strRole);
        intent.putExtra("strArea", strArea_key);
        intent.putExtra("strJenisData", strFlagJenisData);
        intent.putExtra("strJenisSatuan", strJenisSatuan);
        intent.putExtra("strPPN", strFlagPPN);
        intent.putExtra("strDist", strTempDist);
        intent.putExtra("iGudang", iFlagGudang);
        intent.putExtra("strGudang", strGudangPilih);
        intent.putExtra("strBulanAwal", strTempWaktu);
        intent.putExtra("strBulanAkhir", strTempWaktu2);
        intent.putExtra("strTahun", strTempTahun);
        intent.putExtra("strPenjualanTBM", strPenjualanTBM);
//        System.out.println("KRISNA iGUDANG " + iFlagGudang);
//        System.out.println("KRISNA strGudang " + strGudangPilih);

        intent.putExtra("flagTitle1", strFlagTitle1);
        intent.putExtra("flagTitle2", strFlagTitle2);
        intent.putExtra("flagTitle3", strFlagTitle3);
        intent.putExtra("flagTitle4", strFlagTitle4);
        intent.putExtra("flagTitle5", strFlagTitle5);
        intent.putExtra("Tahun", strTempTahun);
        intent.putExtra("tempBlnAwal", strBulanAwal);
        intent.putExtra("tempBlnAKhir", strBulanAkhir);
        intent.putExtra("strParamAct", "Penjualan");
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);

    }

    //ketika load awal, invisible beberapa menu
    public void invisible() {
        spinnerTahun.setVisibility(View.VISIBLE);
        spinnerblnAkhir.setVisibility(View.INVISIBLE);
        spinnerblnAwal.setVisibility(View.INVISIBLE);
        sd.setVisibility(View.INVISIBLE);
        thnRangetgl.setVisibility(View.INVISIBLE);
        txtTahun.setVisibility(View.INVISIBLE);
        txtTahun1.setVisibility(View.VISIBLE);
        txtBulan.setVisibility(View.INVISIBLE);
        strFlagJenisLaporan = "tahunan";

        RBTahun.setChecked(true);
        RBRupiah.setChecked(true);
        strFlagJenisData = "Rupiah";


        strFlagPPN = "";

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

    //ketika load awal, load bbrp data untuk spinner
    public void loadBulan() {
        listJenisLaporan.add("range bulan");
        listJenisLaporan.add("tahunan");
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear - 5; i <= thisYear; i++) {
            listyearView.add(Integer.toString(i));
        }
        listSpinnerRupiah.add("Net");
        listSpinnerRupiah.add("Gross");

        listSpinnerSatuan.add("Lembar");
        listSpinnerSatuan.add("Tonase");

        checkConnection();

        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            listBulanView.add(month_name);
        }
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(LaporanPenjualanActivity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(LaporanPenjualanActivity.this,
                R.layout.spinner_value, listyearView);
        ArrayAdapter<String> jenisLaporan_adapter = new ArrayAdapter<String>(LaporanPenjualanActivity.this,
                R.layout.spinner_value, listJenisLaporan);
        ArrayAdapter<String> jenisLaporanRupiah_adapter = new ArrayAdapter<String>(LaporanPenjualanActivity.this,
                R.layout.spinner_value, listSpinnerRupiah);
        ArrayAdapter<String> jenisSatuan_adapter = new ArrayAdapter<String>(LaporanPenjualanActivity.this,
                R.layout.spinner_value, listSpinnerSatuan);
        spinnerSatuan.setAdapter(jenisSatuan_adapter);
        spinnerTahun.setAdapter(tahun_adapter);
        thnRangetgl.setAdapter(tahun_adapter);
        spinnerblnAwal.setAdapter(bulan_adapter);
        spinnerblnAkhir.setAdapter(bulan_adapter);
        spinnerRupiah.setAdapter(jenisLaporanRupiah_adapter);
        spinnerblnAkhir.setSelection(months.length - 1);
        spinnerTahun.setSelection(5);
        thnRangetgl.setSelection(5);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }


    //click checkbox
    public void Select(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.CBDist:
                if (checked) {
                    strFlagDist = "Distributor";
                    flagDistributor = 1;
                    spinnerDist.setEnabled(true);
                } else {
                    spinnerDist.setEnabled(false);
                    flagDistributor = 0;
                    strFlagDist = " ";
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LaporanPenjualanActivity.this);
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

            AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanPenjualanActivity.this);
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

    public class SyncData extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(LaporanPenjualanActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
//            SoapObject request = new SoapObject("http://tempuri.org/", "LoadDataAwal");
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
            pi.setValue("Penjualan");
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
//                httpTransport.call("http://tempuri.org/LoadDataAwal", envelope);
                httpTransport.call("http://tempuri.org/LoadDataAwal", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }

            if (response.toString() != "[]") {
                try {
                    JSONArray json = new JSONArray(response.toString());
                    System.out.println("KRISNA  " + response.toString());
                    if (json != null) {
//                        salesListName.clear();
                        listSalesName.clear();
                        listGudang.clear();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            if (!e.getString("SalesName").equals("null")) {
                                listSalesName.add(e.getString("SalesName"));
                            }
                            if (!strCompany.equals("ALL")) {
                                if (!e.getString("Dep_name").equals("null")) {
                                    if (!listGudang.contains(e.getString("Dep_name"))) {
                                        listGudang.add(e.getString("Dep_name"));
                                    }
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
//                    System.out.println("KRISNBB " + e.toString());
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanPenjualanActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanPenjualanActivity.this);
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


                    ArrayAdapter<String> dist_adapter = new ArrayAdapter<String>(LaporanPenjualanActivity.this, R.layout.spinner_value, listSalesName);
                    spinnerDist.setAdapter(dist_adapter);

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
