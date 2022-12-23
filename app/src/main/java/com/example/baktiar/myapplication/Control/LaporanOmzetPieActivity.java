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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.DataPenjualanList;
import com.example.baktiar.myapplication.R;
import com.github.mikephil.charting.data.PieEntry;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LaporanOmzetPieActivity extends BaseActivity implements FilterBottom.BottomSheetListener {
    public static String rslt = "";
    Object response = null;
    ProgressDialog p;
    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listJenisLaporan = new ArrayList<>();
    boolean[] checkedItemGudang;
    String[] namesStringArrayGudang;
    ArrayList<Integer> numCheckedArrayGudang = new ArrayList<>();
    ArrayList<String> listGudangChecked = new ArrayList<>();
    ArrayList<String> listGudang = new ArrayList<>();

    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    Spinner spinnerBulan, spinnerTahun, spinnerBulanTahun;
    View line1, line2;
    TextView txtTahun, txtBulan, txtTahun1;
    String strPenjualanTBM = "", strRole, strArea_key, strCompany = "ALL", strFlagJenisLaporan, strTempWaktu, strTahun, strBulan, strFlagTitle3, strFlagDetail, strGudangPilih = " ";
    Button buttonShow;
    int iFlagGudang = 0;

    RadioButton RBTahun, RBBulan;
    RadioGroup radioGroupOutput2;
    CheckBox CBGudangCheck, CBPenjualanTBM;
    Button btnFilter;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_omzet_pie);
        spinnerBulan = (Spinner) findViewById(R.id.spinnerBulan);
        spinnerTahun = (Spinner) findViewById(R.id.spinnerTahun);
        spinnerBulanTahun = (Spinner) findViewById(R.id.spinnerBulanTahun);

        line1 = (View) findViewById(R.id.line1);
        line2 = (View) findViewById(R.id.line2);

        radioGroupOutput2 = (RadioGroup) findViewById(R.id.radioGroupOutput2);
        RBTahun = (RadioButton) findViewById(R.id.RBTahun);
        RBBulan = (RadioButton) findViewById(R.id.RBBulan);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        txtBulan = (TextView) findViewById(R.id.txtBulan);
        txtTahun1 = (TextView) findViewById(R.id.txtTahun1);

        buttonShow = (Button) findViewById(R.id.buttonShow);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        CBGudangCheck = (CheckBox) findViewById(R.id.CBDepartemen);

        CBPenjualanTBM = (CheckBox) findViewById(R.id.CBPenjualanTBM);
        CBPenjualanTBM.setChecked(true);

        strRole = getIntent().getStringExtra("role");
        if (strRole.equals("Area Manager")) {
            CBPenjualanTBM.setVisibility(View.GONE);
            strArea_key = getIntent().getStringExtra("area_key");
        } else {
            CBPenjualanTBM.setVisibility(View.VISIBLE);
        }
        CheckPenjualanTBM();
        invisible();
        loadBulan();


        SyncDataGudang dataGudang = new SyncDataGudang();
        dataGudang.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Penjualan per produk ");
        }
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterBottom filterBottom = new FilterBottom();
                filterBottom.show(getSupportFragmentManager(), "exampleBottomSort");
            }
        });

        CBPenjualanTBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    strPenjualanTBM = "";
                } else {
                    strPenjualanTBM = "True";
                }

            }
        });
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition2();
            }
        });
        radioGroupOutput2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBTahun) {
                    spinnerTahun.setVisibility(View.VISIBLE);
                    spinnerBulan.setVisibility(View.INVISIBLE);
                    txtTahun.setVisibility(View.INVISIBLE);
                    txtTahun1.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    spinnerBulanTahun.setVisibility(View.INVISIBLE);
                    strFlagJenisLaporan = "tahunan";
                } else if (i == R.id.RBBulan) {
                    spinnerTahun.setVisibility(View.INVISIBLE);
                    spinnerBulan.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    spinnerBulanTahun.setVisibility(View.VISIBLE);
                    txtTahun1.setVisibility(View.INVISIBLE);
                    txtBulan.setVisibility(View.VISIBLE);
                    strFlagJenisLaporan = "bulanan";
                }
            }
        });
    }


    public void loadBulan() {

        listJenisLaporan.add("tahunan");
        listJenisLaporan.add("bulanan");
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear - 5; i <= thisYear; i++) {
            listyearView.add(Integer.toString(i));
        }

        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            listBulanView.add(month_name);
        }
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(LaporanOmzetPieActivity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(LaporanOmzetPieActivity.this,
                R.layout.spinner_value, listyearView);
        ArrayAdapter<String> jenisLaporan_adapter = new ArrayAdapter<String>(LaporanOmzetPieActivity.this,
                R.layout.spinner_value, listJenisLaporan);
        spinnerTahun.setAdapter(tahun_adapter);
        spinnerBulanTahun.setAdapter(tahun_adapter);
        spinnerBulan.setAdapter(bulan_adapter);
        spinnerTahun.setSelection(5);
        spinnerBulanTahun.setSelection(5);

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
        spinnerTahun.setVisibility(View.INVISIBLE);
        spinnerBulan.setVisibility(View.VISIBLE);
        txtTahun.setVisibility(View.VISIBLE);
        spinnerBulanTahun.setVisibility(View.VISIBLE);
        txtTahun1.setVisibility(View.INVISIBLE);
        txtBulan.setVisibility(View.VISIBLE);
        RBBulan.setChecked(true);
        strFlagJenisLaporan = "bulanan";
    }


    public void condition2() {
        if (strFlagJenisLaporan == "bulanan") {
            strTempWaktu = String.valueOf(spinnerBulan.getSelectedItemPosition() + 1).toString();
            strTahun = spinnerBulanTahun.getSelectedItem().toString();
            strBulan = spinnerBulan.getSelectedItem().toString();
            strFlagTitle3 = "Komposisi penjualan";
            strFlagDetail = "Bulan " + strBulan + " Tahun " + strTahun;
        } else if (strFlagJenisLaporan == "tahunan") {
            strTahun = spinnerTahun.getSelectedItem().toString();
            strFlagTitle3 = "Komposisi penjualan";
            strFlagDetail = "Tahun " + strTahun;
        } else {
        }
        Intent intent = new Intent(LaporanOmzetPieActivity.this, PieChartDetailActivity.class);
        intent.putExtra("flagDetail", strFlagDetail);
        intent.putExtra("flagTitle3", strFlagTitle3);

        intent.putExtra("strCompany", strCompany);
        intent.putExtra("strRole", strRole);
        intent.putExtra("strArea", strArea_key);
        intent.putExtra("strJenisSatuan", "LEMBAR");
        intent.putExtra("iGudang", iFlagGudang);
        intent.putExtra("strGudang", strGudangPilih);
        intent.putExtra("strBulan", strBulan);
        intent.putExtra("strTahun", strTahun);
        intent.putExtra("strPenjualanTBM", strPenjualanTBM);
        intent.putExtra("strSales", "");
        intent.putExtra("strParamAct", "PenjualanLine");

        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void Select(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.CBDepartemen:
                if (checked) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LaporanOmzetPieActivity.this);
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
                            strGudangPilih = " ";
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
    public void onButtonClickedFilter(String strCompanyFilter) {
        strCompany = strCompanyFilter;
        CheckPenjualanTBM();

        if (!strCompany.equals("ALL")) {
            SyncDataGudang dataGudang = new SyncDataGudang();
            dataGudang.execute();
        }

    }

    public class SyncDataGudang extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(LaporanOmzetPieActivity.this, R.style.MyTheme);
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
            pi.setValue("");
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
                        listGudang.clear();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            if (!listGudang.contains(e.getString("Dep_name"))) {
                                listGudang.add(e.getString("Dep_name"));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanOmzetPieActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanOmzetPieActivity.this);
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
