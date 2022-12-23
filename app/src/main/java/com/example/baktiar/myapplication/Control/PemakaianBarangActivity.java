package com.example.baktiar.myapplication.Control;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.CallerPLC;
import com.example.baktiar.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PemakaianBarangActivity extends AppCompatActivity {
    RadioButton RBTahun, RBBulan, RBTanggal;
    String strFlagJenisLaporan, strQuery, strBulanAwal, strBulanAkhir, strTahunAwal, strTahunAkhir, strTempTglAwal, strTempTglAkhir, strFlagTitle1, strQueryPLC, queryConverter1, queryConverter2, queryHasilShift;
    String strM2n, strRMY, strProduksi, strBorosIrit, strBerat;
    Spinner spinnerBulanAwal, spinnerBulanAkhir, spinnerTahunAwal, spinnerTahunAkhir;
    ProgressDialog p;
    //    CheckBox CBShift;
    EditText tglAwal, tglAkhir;
    TextView txtTahunAwal, txtTahunAkhir, sd, txtLaporan, txtBulanAwal, txtBulanAkhir, mDisplayDate1, mDisplayDate2;
    //    TextView txtBoros, txtJmlBoros, txtM2N, txtJmlM2N, txtRMY, txtJmlRMY, txtProduksi, txtJmlProduksi;
    View line1, line2;
    Button buttonShow;
    Double dM2n, dProduksi, dBerat, dBahanBaku;
    private DatePickerDialog.OnDateSetListener mDateListener1, mDateListener2;
    RadioGroup radioGroupOutput, radioGroupOutput2;
    ArrayList<String> Shift = new ArrayList<>();
    ArrayList<Integer> numCheckedArrayShift = new ArrayList<>();
    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listShift = new ArrayList<>();
    ArrayList<String> listSpinnerRupiah = new ArrayList<>();
    ArrayList<String> listJenisLaporan = new ArrayList<>();
    ArrayList<String> selectedItems = new ArrayList<>();
    ArrayList<Integer> numCheckedArray = new ArrayList<>();
    ArrayList<String> SalesName = new ArrayList<>();
    ArrayList<String> listSalesName = new ArrayList<>();
    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    String[] namesStringArrayShift;
    String strFlagLimitDate;
    boolean[] checkedItemsShift;
    int iTglAwal, iTglAkhir, iFlagTglAwal, iFlagTglAkhir;
    public static String rslt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemakaian_barang);

        spinnerBulanAwal = (Spinner) findViewById(R.id.spinnerBulanAwal);
        spinnerBulanAkhir = (Spinner) findViewById(R.id.spinnerBulanAkhir);
        spinnerTahunAwal = (Spinner) findViewById(R.id.spinnerTahunAwal);
        spinnerTahunAkhir = (Spinner) findViewById(R.id.spinnerTahunAkhir);

        RBBulan = (RadioButton) findViewById(R.id.RBBulan);
        RBTanggal = (RadioButton) findViewById(R.id.RBTanggal);

        tglAwal = (EditText) findViewById(R.id.tglAwal);
        tglAkhir = (EditText) findViewById(R.id.tglAkhir);

        mDisplayDate1 = (TextView) findViewById(R.id.tglAwal);
        mDisplayDate2 = (TextView) findViewById(R.id.tglAkhir);
//        txtJmlProduksi = (TextView) findViewById(R.id.txtJmlProduksi);
//        txtJmlRMY = (TextView) findViewById(R.id.txtJmlRMY);
//        txtJmlM2N = (TextView) findViewById(R.id.txtJmlM2N);
//        txtJmlBoros = (TextView) findViewById(R.id.txtJmlBoros);
        sd = (TextView) findViewById(R.id.sd);

        txtTahunAwal = (TextView) findViewById(R.id.txtTahunAwal);
        txtTahunAkhir = (TextView) findViewById(R.id.txtTahunAkhir);
        txtLaporan = (TextView) findViewById(R.id.txtLaporan);
        txtBulanAwal = (TextView) findViewById(R.id.txtBulanAwal);
        txtBulanAkhir = (TextView) findViewById(R.id.txtBulanAkhir);
        line1 = (View) findViewById(R.id.line1);
        line2 = (View) findViewById(R.id.line2);

        radioGroupOutput2 = (RadioGroup) findViewById(R.id.radioGroupOutput2);

        buttonShow = (Button) findViewById(R.id.buttonShow);

        invisible();
        loadBulan();
        loadTanggal();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Pemakaian Bahan Produksi");
        }
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int iTglAwal = spinnerBulanAwal.getSelectedItemPosition() + spinnerTahunAwal.getSelectedItemPosition()*12;
//                int iTglAkhir = spinnerBulanAkhir.getSelectedItemPosition() +  spinnerTahunAkhir.getSelectedItemPosition()*12;
//                int iTglAwal =
//                int iTglAkhir =

                if (strFlagJenisLaporan == "bulanan") {
                    iTglAwal = spinnerBulanAwal.getSelectedItemPosition() + spinnerTahunAwal.getSelectedItemPosition() * 12;
                    iTglAkhir = spinnerBulanAkhir.getSelectedItemPosition() + spinnerTahunAkhir.getSelectedItemPosition() * 12;
                    if(iTglAwal>=32){
                        strFlagLimitDate ="Ada";
                    }
                    else {
                        strFlagLimitDate ="Tidak Ada";
                    }
                } else {
                    iTglAkhir = iFlagTglAkhir;
                    iTglAwal = iFlagTglAwal;
                    if(iTglAwal>=737215){
                        strFlagLimitDate ="Ada";
                    }
                    else {
                        strFlagLimitDate ="Tidak Ada";
                    }
                }
                System.out.println("iTglAwal" + iTglAwal);
                System.out.println("iTglAkhir" + iTglAkhir);
                if(strFlagLimitDate.equals("Tidak Ada")){
//                    System.out.println( "iTglAwal"+ iTglAwal);
//                    System.out.println( "iTglAkhir"+ iTglAkhir);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PemakaianBarangActivity.this);
                    dialog.setCancelable(false);
                    dialog.setMessage("Tanggal minimal 1 September 2019 ");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
//                            finish();
//                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                }
                else if(iTglAwal > iTglAkhir){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PemakaianBarangActivity.this);
                    dialog.setCancelable(false);
                    dialog.setMessage("Pastikan range tanggal Anda benar ! ");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
//                            finish();
//                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    });
                    final AlertDialog alert = dialog.create();
                    alert.show();
                }
                else {
                    if (strFlagJenisLaporan == "range tanggal") {
                        strTempTglAwal = tglAwal.getText().toString();
                        strTempTglAkhir = tglAkhir.getText().toString();
                        strQuery = "SELECT SUM(B.Jumlah*(1*D.Lebar*D.Panjang*D.Tebal)/500000)  AS m2n, sum(jumlah) AS Produksi, SUM(b.Jumlah*Brg_Berat) AS Berat FROM mProduksi A\n" +
                                "INNER JOIN mProdDetail B ON A.ProdID = B.ProdID\n" +
                                "INNER JOIN mProducts C ON B.Brg_Key = C.Brg_Key\n" +
                                "INNER JOIN mUkuran D ON C.Ukuran_Key = D.Ukuran_Key\n" +
                                "WHERE Mesin_Key = 2 and \n" +
                                "DATEDIFF(DAY,ProdTgl,'" + strTempTglAwal + "') <=0 and DATEDIFF(DAY,ProdTgl,'" + strTempTglAkhir + "') >=0 ";

                        strQueryPLC = "SELECT CAST(ISNULL([Silica],0) as DECIMAL(15,2))as Silica,CAST(ISNULL([Kertas],0) AS DECIMAL(15,2)) as Kertas,CAST(ISNULL([Semen],0) AS DECIMAL(15,2)) as Semen,CAST(ISNULL([Asbes],0) AS DECIMAL(15,2)) as Asbes,CAST(ISNULL([HWG],0) AS DECIMAL(15,2)) as HWG FROM\n" +
                                //"SELECT [Silica],[Kertas],[Semen],[Asbes],ISNULL([HWG],0) as HWG FROM\n" +
                                "(SELECT ISNULL('Silica',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans,'" + strTempTglAwal + "') <= 0 AND DATEDIFF(DAY,TGTrans,'" + strTempTglAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (DAY,TGPLC,'" + strTempTglAwal + "') <= 0 AND DATEDIFF (DAY,TGPLC,'" + strTempTglAkhir + "') >= 0 AND TGTrans IS NULL)) \n" +
                                "and B.Brg_Key = 10468\n" +
                                "group by Brg_Nama\n" +
                                "UNION ALL\n" +
                                "SELECT ISNULL('Kertas',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans,'" + strTempTglAwal + "') <= 0 AND DATEDIFF(DAY,TGTrans,'" + strTempTglAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (DAY,TGPLC,'" + strTempTglAwal + "') <= 0 AND DATEDIFF (DAY,TGPLC,'" + strTempTglAkhir + "') >= 0 AND TGTrans IS NULL)) \n" +
                                "and B.Brg_Key = 12166\n" +
                                "group by Brg_Nama\n" +
                                "UNION ALL\n" +
                                "SELECT ISNULL('Semen',0) AS Jenis,SUM(Jumlah)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans,'" + strTempTglAwal + "') <= 0 AND DATEDIFF(DAY,TGTrans,'" + strTempTglAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (DAY,TGPLC,'" + strTempTglAwal + "') <= 0 AND DATEDIFF (DAY,TGPLC,'" + strTempTglAkhir + "') >= 0 AND TGTrans IS NULL)) \n" +
                                "and B.Brg_Key = 13115\n" +
                                "group by Brg_Nama\n" +
                                "UNION ALL\n" +
                                "SELECT ISNULL('HWG',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans,'" + strTempTglAwal + "') <= 0 AND DATEDIFF(DAY,TGTrans,'" + strTempTglAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (DAY,TGPLC,'" + strTempTglAwal + "') <= 0 AND DATEDIFF (DAY,TGPLC,'" + strTempTglAkhir + "') >= 0 AND TGTrans IS NULL))  \n" +
                                "and B.Brg_Key = 17416\n" +
                                "group by Brg_Nama\n" +
                                "UNION ALL\n" +
                                "SELECT  ISNULL('Asbes',0) AS Jenis,SUM(Jumlah*(100-Konsentrasi)/100)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(DAY,TGTrans,'" + strTempTglAwal + "') <= 0 AND DATEDIFF(DAY,TGTrans,'" + strTempTglAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (DAY,TGPLC,'" + strTempTglAwal + "') <= 0 AND DATEDIFF (DAY,TGPLC,'" + strTempTglAkhir + "') >= 0 AND TGTrans IS NULL)) \n" +
                                "and B.Brg_Key = 17723\n" +
                                "group by Brg_Nama) A\n" +
                                "PIVOT\n" +
                                "(Sum(Jumlah) FOR Jenis IN([Silica],[Kertas],[Semen],[Asbes], [HWG])) AS pvt";

                        strFlagTitle1 = strTempTglAwal + " s/d " + strTempTglAkhir ;
                        strTempTglAwal = "'" + strTempTglAwal + "'";
                        strTempTglAkhir = "'" + strTempTglAkhir + "'";
                    } else if (strFlagJenisLaporan == "bulanan") {

//                    strBulan = spinnerBulan.getSelectedItem().toString();
                        strTahunAwal = spinnerTahunAwal.getSelectedItem().toString();
                        strTahunAkhir = spinnerTahunAkhir.getSelectedItem().toString();
                        strBulanAwal = String.valueOf(spinnerBulanAwal.getSelectedItemPosition() + 1);
                        strBulanAkhir = String.valueOf(spinnerBulanAkhir.getSelectedItemPosition() + 1);
                        strQuery = "SELECT SUM(B.Jumlah*(1*D.Lebar*D.Panjang*D.Tebal)/500000)  AS m2n, sum(jumlah) AS Produksi, SUM(b.Jumlah*Brg_Berat) AS Berat FROM mProduksi A\n" +
                                "INNER JOIN mProdDetail B ON A.ProdID = B.ProdID\n" +
                                "INNER JOIN mProducts C ON B.Brg_Key = C.Brg_Key\n" +
                                "INNER JOIN mUkuran D ON C.Ukuran_Key = D.Ukuran_Key\n" +
                                "WHERE Mesin_Key = 2 and \n" +
                                "DATEDIFF(MONTH,ProdTgl,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0 AND  DATEDIFF(MONTH,ProdTgl,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0";
                        strQueryPLC = "SELECT CAST(ISNULL([Silica],0) as DECIMAL(15,2))as Silica,CAST(ISNULL([Kertas],0) AS DECIMAL(15,2)) as Kertas,CAST(ISNULL([Semen],0) AS DECIMAL(15,2)) as Semen,CAST(ISNULL([Asbes],0) AS DECIMAL(15,2)) as Asbes,CAST(ISNULL([HWG],0) AS DECIMAL(15,2)) as HWG FROM\n" +
                                //"SELECT [Silica],[Kertas],[Semen],[Asbes],ISNULL([HWG],0) as HWG FROM\n" +
                                "(SELECT ISNULL('Silica',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF(MONTH,TGTrans,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (MONTH,TGPLC,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF (MONTH,TGPLC,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0 AND TGTrans IS NULL)) \n" +
                                "and B.Brg_Key = 10468\n" +
                                "group by Brg_Nama\n" +
                                "UNION ALL\n" +
                                "SELECT ISNULL('Kertas',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF(MONTH,TGTrans,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (MONTH,TGPLC,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF (MONTH,TGPLC,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0 AND TGTrans IS NULL)) \n" +
                                "and B.Brg_Key = 12166\n" +
                                "group by Brg_Nama\n" +
                                "UNION ALL\n" +
                                "SELECT ISNULL('Semen',0) AS Jenis,SUM(Jumlah)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF(MONTH,TGTrans,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (MONTH,TGPLC,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF (MONTH,TGPLC,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0 AND TGTrans IS NULL)) \n" +
                                "and B.Brg_Key = 13115\n" +
                                "group by Brg_Nama\n" +
                                "UNION ALL\n" +
                                "SELECT ISNULL('HWG',0) AS Jenis,SUM(((Jumlah*Konsentrasi)/TOTAL))as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key INNER JOIN (Select Komposisi_key, SUM (Presentase) As TOTAL FROm mKomposisi group by komposisi_Key) as TOTAL ON P.komposisi_key = TOTAL.Komposisi_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF(MONTH,TGTrans,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (MONTH,TGPLC,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF (MONTH,TGPLC,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0 AND TGTrans IS NULL))  \n" +
                                "and B.Brg_Key = 17416\n" +
                                "group by Brg_Nama\n" +
                                "UNION ALL\n" +
                                "SELECT  ISNULL('Asbes',0) AS Jenis,SUM(Jumlah*(100-Konsentrasi)/100)as Jumlah FROM mPLC P INNER JOIN mBahan B ON P.Brg_Key = B.Brg_Key\n" +
                                "where  Jumlah > 0 AND ((DATEDIFF(MONTH,TGTrans,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF(MONTH,TGTrans,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0)  OR \n" +
                                "(DATEDIFF (MONTH,TGPLC,'" + strBulanAwal + "/1/" + strTahunAwal + "') <= 0 AND DATEDIFF (MONTH,TGPLC,'" + strBulanAkhir + "/1/" + strTahunAkhir + "') >= 0 AND TGTrans IS NULL)) \n" +
                                "and B.Brg_Key = 17723\n" +
                                "group by Brg_Nama) A\n" +
                                "PIVOT\n" +
                                "(Sum(Jumlah) FOR Jenis IN([Silica],[Kertas],[Semen],[Asbes], [HWG])) AS pvt";
                        strTempTglAwal = "'" + strBulanAwal + "/1/" + strTahunAwal + "'";
                        strTempTglAkhir = "'" + strBulanAkhir + "/1/" + strTahunAkhir + "'";
                        strFlagTitle1 = spinnerBulanAwal.getSelectedItem().toString() + " " + spinnerTahunAwal.getSelectedItem().toString()
                                + " s/d " + spinnerBulanAkhir.getSelectedItem().toString() + " " + spinnerTahunAkhir.getSelectedItem().toString();

                    }

//                    System.out.println("STRQUERY " +strQuery);
//                    System.out.println("STRQUERYPLC " +strQueryPLC);
//                    SyncData mydata = new SyncData();
//                    mydata.execute();
                    SyncDataPLC syncDataPLC = new SyncDataPLC();
                    syncDataPLC.execute();
                }

            }
        });
        radioGroupOutput2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBBulan) {

//                    spinnerTahun.setVisibility(View.INVISIBLE);
                    spinnerBulanAwal.setVisibility(View.VISIBLE);
                    spinnerBulanAkhir.setVisibility(View.VISIBLE);
                    txtTahunAwal.setVisibility(View.VISIBLE);
                    txtTahunAkhir.setVisibility(View.VISIBLE);
                    spinnerTahunAwal.setVisibility(View.VISIBLE);
                    spinnerTahunAkhir.setVisibility(View.VISIBLE);
                    txtBulanAwal.setVisibility(View.VISIBLE);
                    txtBulanAkhir.setVisibility(View.VISIBLE);

                    tglAkhir.setVisibility(View.INVISIBLE);
                    tglAwal.setVisibility(View.INVISIBLE);
                    sd.setVisibility(View.INVISIBLE);
                    strFlagJenisLaporan = "bulanan";
                    // flagShowGraph = 1;

                } else if (i == R.id.RBTanggal) {

                    spinnerBulanAwal.setVisibility(View.INVISIBLE);
                    spinnerBulanAkhir.setVisibility(View.INVISIBLE);
                    tglAkhir.setVisibility(View.VISIBLE);
                    tglAwal.setVisibility(View.VISIBLE);
                    sd.setVisibility(View.VISIBLE);
                    txtBulanAwal.setVisibility(View.INVISIBLE);
                    txtBulanAkhir.setVisibility(View.INVISIBLE);

                    txtTahunAwal.setVisibility(View.INVISIBLE);
                    txtTahunAkhir.setVisibility(View.INVISIBLE);
                    spinnerTahunAkhir.setVisibility(View.INVISIBLE);
                    spinnerTahunAwal.setVisibility(View.INVISIBLE);
                    strFlagJenisLaporan = "range tanggal";


                }
            }
        });
    }


    public void Select(View view) {
//        boolean checked = ((CheckBox) view).isChecked();
//        switch (view.getId()) {
//            case R.id.CBShift:
//                if (checked) {
//                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(PemakaianBarangActivity.this);
//                    builder1.setTitle("Pilih Provinsi");
//                    builder1.setMultiChoiceItems(namesStringArrayShift, checkedItemsShift, new DialogInterface.OnMultiChoiceClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int position, boolean isChecked) {
//                            if (isChecked) {
//                                //add the position that the user clicked on
//                                numCheckedArrayShift.add(position);
//                            } else if (numCheckedArrayShift.contains(position)) {
//                                //remove the position that the user clicked on
//                                numCheckedArrayShift.remove(Integer.valueOf(position));
//                            }
//                        }
//                    });
//                    builder1.setCancelable(false);
//                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            strFlagJenisProvinsi = "dipilih";
//                            Shift.clear();
//                            queryHasilShift = " ";
//                            queryConverter1 = null;
//                            queryConverter2 = null;
//                            //add selected items into selected item array      still need to figure out how to remove it from the array
//                            for (int i = 0; i < numCheckedArrayShift.size(); i++) {
//                                Shift.add("'" + namesStringArrayShift[numCheckedArrayShift.get(i)] + "'");
//                            }
//                            queryConverter1 = "" + Shift + "";
//                            queryConverter2 = queryConverter1.replace("[", "(");
//                            queryHasilShift = queryConverter2.replace("]", ")");
//                        }
//                    });
//
//                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            CBShift.setChecked(false);
//                        }
//                    });
//                    builder1.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int which) {
//                            for (int i = 0; i < checkedItemsShift.length; i++) {
//                                checkedItemsShift[i] = false;
//                                numCheckedArrayShift.clear();
//                            }
//                            Shift.clear();
//                            queryHasilShift = " ";
//                            queryConverter1 = null;
//                            queryConverter2 = null;
//                            CBShift.setChecked(false);
//                        }
//                    });
////                    builder1.setCancelable(false);
////                    builder1.show();
////                    builder1.show();
//                    AlertDialog dialog = builder1.create();
//                    dialog.show();
//                }
//                else{
//
//                }
//                break;
//        }
    }

    public class SyncData extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        //private final ProgressDialog p = new ProgressDialog(PerbandinganPemakianBahanActivity.this, R.style.MyTheme);
        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(PemakaianBarangActivity.this, R.style.MyTheme);
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
                d.strQuery = strQuery;
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
                    if (json != null) {

                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            dM2n = e.getDouble("m2n");
                            dBerat = e.getDouble("Berat");
                            dProduksi = e.getDouble("Produksi");

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(PemakaianBarangActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(PemakaianBarangActivity.this);
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
                    SyncDataPLC syncDataPLC = new SyncDataPLC();
                    syncDataPLC.execute();
                } catch (Exception ex) {

                }

            }
        }
    }

    public class SyncDataPLC extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        private final ProgressDialog p = new ProgressDialog(PemakaianBarangActivity.this, R.style.MyTheme);

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            // p = new ProgressDialog(UploadImageActivity.this, R.style.MyTheme);
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
                CallerPLC d = new CallerPLC();
                d.strQuery = strQuery;
                d.strQueryPLC = strQueryPLC;
                d.strQueryPLC2 = strQueryPLC;
                d.join();
                d.start();

                while (rslt == "START") {
                    try {
                        Thread.sleep(10);

                    } catch (Exception ex) {
                        System.out.print("Susah2 " + ex);
                    }
                }

            } catch (Exception ex) {
            }
            if (rslt != "[]") {
                try {
                    JSONObject jsnobject = new JSONObject(rslt);
//                    JSONArray json = jsnobject.getJSONArray("Table");

                    JSONArray json = jsnobject.getJSONArray("PERTAMA");
                    JSONArray json2 = jsnobject.getJSONArray("KETIGA");

//                    JSONArray json3 = jsnobject.getJSONArray("KEDUA");
//                    System.out.println("json "+json);
//                    System.out.println("json2 "+json2);
//                    System.out.println("json3 "+json3);
                    if (json != null && json2 !=null) {


                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            dBahanBaku = e.getDouble("Asbes") + e.getDouble("Silica") + e.getDouble("Semen") + e.getDouble("Kertas") + e.getDouble("HWG");
                        }
                        for (int i = 0; i < json2.length(); i++) {
                            JSONObject e = json2.getJSONObject(i);
                            dM2n = e.getDouble("m2n");
                            dBerat = e.getDouble("Berat");
                            dProduksi = e.getDouble("Produksi");

                        }

                        if (json.length() >= 1 && json2.length()>=1) {
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
                    System.out.print("Susah " + e);
                }
            } else {
                isSuccess = "false";
            }

            return msg;

        }

        @Override
        protected void onPostExecute(String strImgRespon) // disimissing progress dialoge, showing error and setting up my listview
        {
//            super.onPostExecute(strImgRespon);
            if (this.p.isShowing()) {
                p.dismiss();
            }


            if (isSuccess.equals("false") == true) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PemakaianBarangActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage(strImgRespon.toString());
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();

            } else if (isSuccess.equals("true") == true) {
                DecimalFormat formatRibuan = new DecimalFormat("#,###.00");
                DecimalFormat formatRibuan2 = new DecimalFormat("#,###");
                DecimalFormat formatKoma = new DecimalFormat("#.##");
                strProduksi = formatRibuan2.format(dProduksi);
                strM2n = formatRibuan.format(dM2n);
                strRMY = formatRibuan.format(dM2n / (dBahanBaku / 1000));
                strBorosIrit = formatRibuan.format(((dBerat - dBahanBaku) / dBerat) * 100);
                strBerat = formatKoma.format(dBerat);

                Intent intent = new Intent(PemakaianBarangActivity.this, PerbandinganPemakianBahanActivity.class);
                intent.putExtra("strProduksi", strProduksi);
                intent.putExtra("strM2n", strM2n);
                intent.putExtra("strRMY", strRMY);
                intent.putExtra("strBorosIrit", strBorosIrit);
                intent.putExtra("strTempTglAwal", strTempTglAwal);
                intent.putExtra("strTempTglAkhir", strTempTglAkhir);
                intent.putExtra("strFlagJenisLaporan", strFlagJenisLaporan);
                intent.putExtra("strFlagTitle1", strFlagTitle1);
                intent.putExtra("iTotalBahan", dBerat);

                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

//                System.out.println("RINTO " + strBulanAkhir + " " + strTahunAkhir);
                System.out.println("strProduksi " + strProduksi);
                System.out.println("strM2n " + strM2n);
                System.out.println("strRMY " + strRMY);
                System.out.println("strBorosIrit " + strBorosIrit);
//                System.out.println("dBahanBaku " + dBahanBaku);
                System.out.println("strBerat " + strBerat);

            }

        }
    }

    public void invisible() {
        spinnerBulanAkhir.setVisibility(View.VISIBLE);
        spinnerBulanAwal.setVisibility(View.VISIBLE);
        tglAkhir.setVisibility(View.INVISIBLE);
        tglAwal.setVisibility(View.INVISIBLE);
        sd.setVisibility(View.INVISIBLE);
        txtTahunAwal.setVisibility(View.VISIBLE);
        txtTahunAkhir.setVisibility(View.VISIBLE);
        txtBulanAwal.setVisibility(View.VISIBLE);
        txtBulanAkhir.setVisibility(View.VISIBLE);

        spinnerTahunAwal.setVisibility(View.VISIBLE);
        spinnerTahunAkhir.setVisibility(View.VISIBLE);
        strFlagJenisLaporan = "bulanan";
        RBBulan.setChecked(true);


    }

    public void loadBulan() {
        listJenisLaporan.add("tahunan");
        listJenisLaporan.add("bulanan");
        listJenisLaporan.add("range tanggal");

        listShift.add("Shift 1");
        listShift.add("Shift 2");
        listShift.add("Shift 3");
        namesStringArrayShift = new String[listShift.size()];
        for (int i = 0; i < listShift.size(); i++) {
            namesStringArrayShift[i] = listShift.get(i);
        }
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
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(PemakaianBarangActivity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(PemakaianBarangActivity.this,
                R.layout.spinner_value, listyearView);
        ArrayAdapter<String> jenisLaporan_adapter = new ArrayAdapter<String>(PemakaianBarangActivity.this,
                R.layout.spinner_value, listJenisLaporan);


        spinnerTahunAwal.setAdapter(tahun_adapter);
        spinnerTahunAkhir.setAdapter(tahun_adapter);
        spinnerBulanAwal.setAdapter(bulan_adapter);
        spinnerBulanAkhir.setAdapter(bulan_adapter);

        spinnerTahunAwal.setSelection(5);
        spinnerTahunAkhir.setSelection(5);
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
                        PemakaianBarangActivity.this,
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
                iFlagTglAwal = (month1 * 31) + day1 + (year1 * 365);
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
                        PemakaianBarangActivity.this,
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
                iFlagTglAkhir = (month2 * 31) + day2 + (year2 * 365);
                mDisplayDate2.setText(date2);
            }
        };

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
