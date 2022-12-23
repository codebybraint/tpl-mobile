package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.DataOmzetList;
import com.example.baktiar.myapplication.Model.DataPenjualanList;
import com.example.baktiar.myapplication.Model.SalesListName;
import com.example.baktiar.myapplication.R;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
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
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProduksiActivity extends BaseActivity implements FilterBottom.BottomSheetListener {

    boolean[] checkedItemGudang;
    String[] namesStringArrayGudang;
    ArrayList<Integer> numCheckedArrayGudang = new ArrayList<>();
    ArrayList<String> listGudangChecked = new ArrayList<>();
    ArrayList<String> listGudang = new ArrayList<>();
    RadioButton RBTahun, RBBulan, RBTonase, RBLembar;
    public static ResultSet rs;
    RadioGroup radioGroupOutput2, radioGroupOutput;
    ProgressDialog p;
    public static String rslt = "";
    Button buttonShow;
    Spinner spinnerMesin, spinnerTahun, thnRangetgl, spinnerblnAwal, spinnerblnAkhir;
    CheckBox CBMesin, CBGudangCheck;
    TextView sd, txtTahun, txtJenisLaporan, txtBulan, txtBulan1;
    //    private ArrayList<SalesListName> salesListName;
    View line1, line2;
    Object response = null;
    String flagMesin, flagJenisLaporan, strCompany = "ALL", strTglAwal, strTglAkhir, tempWaktu2, tempWaktu, tempMesin = "", tempTahun, flagGrafik = "", bulanAwal, bulanAkhir, tahun, flagTitle1, flagTitle2, flagTitle3, flagTitle4, flagTitle5, flagSatuan = " ", strTitle1 = " ", strGudangPilih = "";
    int iFlagGudang = 0;
    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listJenisLaporan = new ArrayList<>();
    ArrayList<String> listSalesName = new ArrayList<>();
    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    int flagDistributor;
    Button btnFilter;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produksi);
        sd = (TextView) findViewById(R.id.sd);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        txtJenisLaporan = (TextView) findViewById(R.id.txtJenisLaporan);
        txtBulan = (TextView) findViewById(R.id.txtBulan);
        txtBulan1 = (TextView) findViewById(R.id.txtTahun1);
        spinnerMesin = (Spinner) findViewById(R.id.spinnerMesin);
        spinnerTahun = (Spinner) findViewById(R.id.spinnerTahun);
        spinnerblnAwal = (Spinner) findViewById(R.id.blnAwal);
        spinnerblnAkhir = (Spinner) findViewById(R.id.blnAKhir);
        thnRangetgl = (Spinner) findViewById(R.id.thnRangetgl);
        radioGroupOutput = (RadioGroup) findViewById(R.id.radioGroupOutput);
        radioGroupOutput2 = (RadioGroup) findViewById(R.id.radioGroupOutput2);
        RBTahun = (RadioButton) findViewById(R.id.RBTahun);
        RBBulan = (RadioButton) findViewById(R.id.RBBulan);
        RBTonase = (RadioButton) findViewById(R.id.RBTonase);
        RBLembar = (RadioButton) findViewById(R.id.RBLembar);
        line1 = (View) findViewById(R.id.line1);
        line2 = (View) findViewById(R.id.line2);
        buttonShow = (Button) findViewById(R.id.buttonShow);
        CBMesin = (CheckBox) findViewById(R.id.CBMesin);
        CBGudangCheck = (CheckBox) findViewById(R.id.CBDepartemen);
//        salesListName = new ArrayList<SalesListName>();
        btnFilter = (Button) findViewById(R.id.btnFilter);
        invisible();
        loadBulan();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Produksi ");
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ProduksiActivity.this);
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

                    condition2();
                }
            }
        });
        radioGroupOutput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBLembar) {
                    flagGrafik = "Lembar";
                } else if (i == R.id.RBTonase) {
                    flagGrafik = "TONASE";
                }
            }
        });
        radioGroupOutput2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBTahun) {

                    spinnerblnAkhir.setVisibility(View.INVISIBLE);
                    spinnerblnAwal.setVisibility(View.INVISIBLE);
                    sd.setVisibility(View.INVISIBLE);
                    thnRangetgl.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    spinnerTahun.setVisibility(View.VISIBLE);
                    txtBulan1.setVisibility(View.VISIBLE);
                    flagJenisLaporan = "tahunan";
                } else if (i == R.id.RBBulan) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    spinnerblnAwal.setVisibility(View.VISIBLE);
                    spinnerblnAkhir.setVisibility(View.VISIBLE);
                    sd.setVisibility(View.VISIBLE);
                    thnRangetgl.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.VISIBLE);
                    txtBulan1.setVisibility(View.INVISIBLE);
                    flagJenisLaporan = "rangeBulan";
                }
            }
        });
    }


    public void condition2() {
        if (flagGrafik == "Lembar") {
            flagSatuan = "ribu lembar";
        } else {
            flagSatuan = "ton";
        }
        if (flagMesin == "Mesin") {
            tempMesin = spinnerMesin.getSelectedItem().toString();

            strTitle1 = " ";
        } else {
            tempMesin = "";

            strTitle1 = "Semua";
        }

        if (flagJenisLaporan == "rangeBulan") {
            tempWaktu = String.valueOf(spinnerblnAwal.getSelectedItemPosition() + 1).toString();
            tempWaktu2 = String.valueOf(spinnerblnAkhir.getSelectedItemPosition() + 1).toString();
            bulanAwal = spinnerblnAwal.getSelectedItem().toString();
            bulanAkhir = spinnerblnAkhir.getSelectedItem().toString();
            tempTahun = thnRangetgl.getSelectedItem().toString();
            strTglAwal = String.format("%s/1/%s", tempWaktu, tempTahun);
            strTglAkhir = String.format("%s/1/%s", tempWaktu2, tempTahun);
            flagTitle1 = "Laporan produksi ";
            flagTitle2 = String.format("%s Mesin %s %s s/d %s %s (%s)", strTitle1, tempMesin, spinnerblnAwal.getSelectedItem().toString(), spinnerblnAkhir.getSelectedItem().toString(), tempTahun, flagSatuan);
        } else if (flagJenisLaporan == "tahunan") {
            tempTahun = spinnerTahun.getSelectedItem().toString();
            strTglAwal = String.format("1/1/%s", tempTahun);
            strTglAkhir = "";
            flagTitle1 = "Laporan produksi ";
            flagTitle2 = String.format("%s Mesin %s %s (%s)", strTitle1, tempMesin, tempTahun, flagSatuan);
        } else {


        }
        if (flagMesin == "Mesin") {

            flagTitle3 = "Komposisi produksi mesin";
            flagTitle4 = "Mesin " + tempMesin + " Bulan ";
            flagTitle5 = " Tahun " + tempTahun;

        } else {

            flagTitle3 = "Komposisi produksi ";
            flagTitle4 = "Semua Mesin Bulan ";
            flagTitle5 = " Tahun " + tempTahun;

        }

        Intent intent = new Intent(ProduksiActivity.this, GrafikLineActivity.class);

        intent.putExtra("flagTitle1", flagTitle1);
        intent.putExtra("flagTitle2", flagTitle2);
        intent.putExtra("flagTitle3", flagTitle3);
        intent.putExtra("flagTitle4", flagTitle4);
        intent.putExtra("flagTitle5", flagTitle5);
        intent.putExtra("Tahun", tempTahun);
        intent.putExtra("tempBlnAwal", bulanAwal);
        intent.putExtra("tempBlnAKhir", bulanAkhir);


        intent.putExtra("strCompany", strCompany);
        intent.putExtra("strJenisSatuan", flagGrafik);
        intent.putExtra("strMesin", tempMesin);
        intent.putExtra("iGudang", iFlagGudang);
        intent.putExtra("strGudang", strGudangPilih);
        intent.putExtra("strTglAwal", strTglAwal);
        intent.putExtra("strTglAkhir", strTglAkhir);
        intent.putExtra("strParamAct", "Produksi");

        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        CBMesin.setChecked(false);
        spinnerMesin.setEnabled(false);
        flagDistributor = 0;
        flagMesin = "";
    }

    //ketika load awal, invisible beberapa menu
    public void invisible() {
        spinnerblnAkhir.setVisibility(View.INVISIBLE);
        spinnerblnAwal.setVisibility(View.INVISIBLE);
        sd.setVisibility(View.INVISIBLE);
        txtTahun.setVisibility(View.INVISIBLE);
        txtBulan.setVisibility(View.INVISIBLE);
        thnRangetgl.setVisibility(View.INVISIBLE);
        RBTahun.setChecked(true);
        RBLembar.setChecked(true);
        spinnerTahun.setVisibility(View.VISIBLE);
        txtBulan1.setVisibility(View.VISIBLE);
        flagJenisLaporan = "tahunan";
        flagGrafik = "Lembar";
        strTitle1 = "Semua";
    }

    //ketika load awal, load bbrp data untuk spinner
    public void loadBulan() {
        listJenisLaporan.add("range bulan");
        listJenisLaporan.add("tahunan");
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
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(ProduksiActivity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(ProduksiActivity.this,
                R.layout.spinner_value, listyearView);
        ArrayAdapter<String> jenisLaporan_adapter = new ArrayAdapter<String>(ProduksiActivity.this,
                R.layout.spinner_value, listJenisLaporan);
        spinnerTahun.setAdapter(tahun_adapter);
        thnRangetgl.setAdapter(tahun_adapter);
        spinnerblnAwal.setAdapter(bulan_adapter);
        spinnerblnAkhir.setAdapter(bulan_adapter);
        spinnerblnAkhir.setSelection(months.length - 1);
        spinnerTahun.setSelection(5);
        thnRangetgl.setSelection(5);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(ProduksiActivity.this);
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
            case R.id.CBMesin:
                if (checked) {
                    flagMesin = "Mesin";
                    flagDistributor = 1;
                    spinnerMesin.setEnabled(true);
                } else {
                    spinnerMesin.setEnabled(false);
                    flagDistributor = 0;
                    flagMesin = "";
                }
                break;
            case R.id.CBDepartemen:
                if (checked) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ProduksiActivity.this);
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

    @Override
    public void onButtonClickedFilter(String strCompanyFilter) {
        strCompany = strCompanyFilter;
        if (!strCompany.equals("ALL")) {
            SyncData mydata = new SyncData();
            mydata.execute();
            CBGudangCheck.setVisibility(View.VISIBLE);
            CBMesin.setVisibility(View.VISIBLE);
            spinnerMesin.setVisibility(View.VISIBLE);
        } else {
            CBGudangCheck.setVisibility(View.GONE);
            CBMesin.setVisibility(View.GONE);
            spinnerMesin.setVisibility(View.GONE);
        }
    }

    private class SyncData extends AsyncTask<String, String, String> {
        Connection connect;
        String ConnectionResult = "";
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(ProduksiActivity.this, R.style.MyTheme);
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
                            if (!e.getString("SalesName").equals("null")) {
                                listSalesName.add((e.getString("SalesName")));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProduksiActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProduksiActivity.this);
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
//                    for (int x = 0; x < salesListName.size(); x++) {
//                        listSalesName.add(salesListName.get(x).getSalesName());
//                    }
                    ArrayAdapter<String> dist_adapter = new ArrayAdapter<String>(ProduksiActivity.this, R.layout.spinner_value, listSalesName);
                    spinnerMesin.setAdapter(dist_adapter);

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
