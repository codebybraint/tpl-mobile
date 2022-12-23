package com.example.baktiar.myapplication.Control;

import android.app.NotificationManager;
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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.R;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LaporanHarianActivity extends BaseActivity implements FilterBottom.BottomSheetListener {
    public static String rslt;

    TextView JumlahOmzetData, JumlahLembarData, JumlahNotaData, JumlahTonaseData;
    Button buttonGross, buttonNota, buttonLembar, buttonTonase;
    ProgressDialog p;
    String strJumlahOmzet, strCompany = "ALL", strPenjualanTBM = "", strJumlahNota, strFlagHarian, strJumlahTonase, strRole, strArea_key = "";
    Double doubleData2, doubleData;
    Integer intJumlahLembar;
    CheckBox CBPenjualanTBM;
    Object response = null;
    Intent intent;
    Button btnFilter;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_harian);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        buttonGross = (Button) findViewById(R.id.buttonGross);
        buttonLembar = (Button) findViewById(R.id.buttonLembar);
        buttonNota = (Button) findViewById(R.id.buttonNota);
        buttonTonase = (Button) findViewById(R.id.buttonTonase);
        JumlahOmzetData = (TextView) findViewById(R.id.JumlahOmzetData);
        JumlahLembarData = (TextView) findViewById(R.id.JumlahLembarData);
        JumlahNotaData = (TextView) findViewById(R.id.JumlahNotaData);
        JumlahTonaseData = (TextView) findViewById(R.id.JumlahTonaseData);
        strRole = getIntent().getStringExtra("role");
        CBPenjualanTBM = (CheckBox) findViewById(R.id.CBPenjualanTBM);
        CBPenjualanTBM.setChecked(true);
        if (strRole.equals("Area Manager")) {
            CBPenjualanTBM.setVisibility(View.GONE);
            strArea_key = getIntent().getStringExtra("area_key");

        } else {

            CBPenjualanTBM.setVisibility(View.VISIBLE);
        }


        CheckPenjualanTBM();
        checkConnection();
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Laporan harian");
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
                checkConnection();
            }
        });

        buttonNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LaporanHarianActivity.this, PerbandinganHarianActivity.class);
                strFlagHarian = "Nota";
                passingData();
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonLembar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LaporanHarianActivity.this, PerbandinganHarianActivity.class);
                strFlagHarian = "Lembar";
                passingData();
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonGross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LaporanHarianActivity.this, PerbandinganHarianActivity.class);
                strFlagHarian = "Gross";
                passingData();
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        buttonTonase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(LaporanHarianActivity.this, PerbandinganHarianActivity.class);
                strFlagHarian = "Tonase";
                passingData();
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


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

    public void passingData() {

        intent.putExtra("flagHarian", strFlagHarian);
        intent.putExtra("role", strRole);
        if (strRole.equals("Area Manager")) {
            intent.putExtra("area_key", strArea_key);
        }
        if (strPenjualanTBM.equals("")) {
            intent.putExtra("salesTBM", "FALSE");
        } else {
            intent.putExtra("salesTBM", "TRUE");
        }
        intent.putExtra("strCompany", strCompany);
        intent.putExtra("strRole", strRole);
        intent.putExtra("strArea", strArea_key);
        intent.putExtra("strJenisSatuan", strFlagHarian);
        intent.putExtra("strPenjualanTBM", strPenjualanTBM);
    }

    public void CheckPenjualanTBM() {
        if (strCompany.equals("TBM")) {
            CBPenjualanTBM.setVisibility(View.GONE);
        } else if (strCompany.equals("TAA")) {
            CBPenjualanTBM.setVisibility(View.VISIBLE);
        }
        else {
            CBPenjualanTBM.setVisibility(View.GONE);
        }
    }

    public void checkConnection() {
        if (isOnline()) {
            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            final String todayString = formatter.format(todayDate);

            GetData mydata = new GetData();
            mydata.execute();
        } else {

            AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanHarianActivity.this);
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
    public void onButtonClickedFilter(String strCompanyFilter) {
        strCompany = strCompanyFilter;
        CheckPenjualanTBM();
        GetData mydata = new GetData();
        mydata.execute();
    }

    private class GetData extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(LaporanHarianActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {

            SoapObject request = new SoapObject("http://tempuri.org/", "LaporanHarian");

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
                httpTransport.call("http://tempuri.org/LaporanHarian", envelope);
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
                            doubleData2 = e.getDouble("Gross");
                            doubleData = e.getDouble("Tonase");
                            DecimalFormat f = new DecimalFormat("#.##");
                            strJumlahOmzet = f.format(doubleData2);
                            strJumlahTonase = f.format(doubleData);
                            strJumlahNota = e.getString("JumlahNota");
                            intJumlahLembar = e.getInt("JumlahLembar");
                        }
                        if (json.length() >= 1) {
                            msg = "Found";
                            isSuccess = "true";
                        } else {
                            msg = "No Data found!";
                            isSuccess = "false";
                        }
                        isSuccess = "true";
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanHarianActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(LaporanHarianActivity.this);
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
                    JumlahLembarData.setText(intJumlahLembar + " Lembar");
                    JumlahNotaData.setText(strJumlahNota + " Lembar");
                    JumlahOmzetData.setText(strJumlahOmzet + " Juta");
                    JumlahTonaseData.setText(strJumlahTonase + " Ton");
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

}
