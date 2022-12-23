package com.example.baktiar.myapplication.Control;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.example.baktiar.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LaporanRankingActivity extends AppCompatActivity {
    Spinner spinnerBulan, spinnerTahun, spinnerBulanTahun, spinnerRupiah, spinnerSatuan, spinnerRanking;
    RadioButton RBTahun, RBBulan, RBTanggal;
    RadioGroup radioGroupOutput, radioGroupOutput2;
    RadioButton RBSatuan, RBRupiah;
    View line1, line2;
    ProgressDialog progressDialog;
    private TextView mDisplayDate1, mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateListener1, mDateListener2;
    Button buttonShow;
    EditText tglAwal, tglAkhir;
    TextView txtTahun, sd, txtLaporan, txtTahun1, txtBulan;
    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    String strQueryPassing, strSatuan, strJenis, strWaktu, strQueryTime,strFlagTitle1, strQueryTop, strQueryGroup,  strFlagJenisLaporan, strTempTglAwal, strTempTglAKhir, strFlagJenisData,  strTahun, strQuerySum = "", strQueryJoin = "";
    int flagShowGraph, flagData;

    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listSpinnerRupiah = new ArrayList<>();
    ArrayList<String> listSpinnerSatuan = new ArrayList<>();
    ArrayList<String> listJenisLaporan = new ArrayList<>();
    ArrayList<String> listSpinnerRanking = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_ranking);
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
        spinnerRanking = (Spinner) findViewById(R.id.spinnerRanking);

        line1 = (View) findViewById(R.id.line1);
        line2 = (View) findViewById(R.id.line2);

        tglAwal = (EditText) findViewById(R.id.tglAwal);
        tglAkhir = (EditText) findViewById(R.id.tglAkhir);

        buttonShow = (Button) findViewById(R.id.buttonShow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Data Rank");
        }
        invisible();
        loadBulan();
        loadTanggal();

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition1();
            }
        });
        RBSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strFlagJenisData = "Satuan";
                flagData = 2;
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
                flagData = 1;
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
                    flagShowGraph = 0;
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
                    flagShowGraph = 1;
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
                    flagShowGraph = 2;
                }
            }
        });
    }

    public void condition1() {
        if (strFlagJenisData == "Rupiah") {
            if (spinnerRupiah.getSelectedItem().toString() == "Net") {
                strQuerySum = String.format(" Sum(JLTotal/1000000) AS Total ");
                strQueryJoin = " ";
//                strFlagTitle1 = "Omzet net distributor ";

            } else {
                strQuerySum = String.format(" sum((C.HarSat*C.jumlah)/1000000) as Total ");
                strQueryJoin = " INNER JOIN mJualItem C on A.JLFaktur=C.JLFAKTUR  INNER JOIN mProducts D on C.Brg_Key=D.Brg_Key INNER JOIN mKat G on G.Kat_Key = D.Kat_Key ";
//                strQueryJoin = " INNER JOIN mJualItem C on A.JLFaktur=C.JLFAKTUR INNER JOIN mProducts D on C.Brg_Key=D.Brg_Key ";
//                strFlagTitle1 = "Omzet gross distributor ";
//                strSatuan = "juta rupiah";
            }
            strSatuan = "(juta rupiah)";
        } else {
            if (spinnerSatuan.getSelectedItem().toString() == "Lembar") {
                strQuerySum = " sum(C.Jumlah) as Total ";
                strQueryJoin = " INNER JOIN mJualItem C on A.JLFaktur=C.JLFAKTUR  INNER JOIN mProducts D on C.Brg_Key=D.Brg_Key INNER JOIN mKat G on G.Kat_Key = D.Kat_Key ";
//                strQueryJoin = " INNER JOIN mJualItem C on A.JLFaktur=C.JLFAKTUR INNER JOIN mProducts D on C.Brg_Key=D.Brg_Key ";
//                strFlagTitle1 = "Omzet distributor ";
                strSatuan = "(lembar)";
            } else {
                strQuerySum = " Sum(C.Jumlah*D.brg_berat)/1000 AS Total ";
                strQueryJoin = " INNER JOIN mJualItem C on A.JLFaktur=C.JLFAKTUR  INNER JOIN mProducts D on C.Brg_Key=D.Brg_Key INNER JOIN mKat G on G.Kat_Key = D.Kat_Key ";
//                strQueryJoin = " INNER JOIN mJualItem C on A.JLFaktur=C.JLFAKTUR INNER JOIN mProducts D on C.Brg_Key=D.Brg_Key ";
//                strFlagTitle1 = "Omzet distributor ";
                strSatuan = "(ton)";
            }
        }

        if("Distributor".equals(spinnerRanking.getSelectedItem().toString())){
            strQueryTop = " TOP 10 SalesName as XValue, ";
            strQueryGroup = " SalesName ";
            strJenis = "Top 10 Dist";
        }
        else if("Provinsi".equals(spinnerRanking.getSelectedItem().toString())){
            strQueryTop = " TOP 10 ProvinsiName as XValue, ";
            strQueryGroup = " ProvinsiName ";
            strJenis = "Top 10 Provinsi";
        }
        else if("Kota".equals(spinnerRanking.getSelectedItem().toString())){
            strQueryTop = " TOP 10 City_Name as XValue, ";
            strQueryGroup = " City_Name ";
            strJenis = "Top 10 Kota";
        }
        else {
            strQueryTop = " TOP 5 Kat_Kelompok as XValue, ";
            strQueryGroup = " Kat_Kelompok ";
            strJenis = "Top 5 Barang";
            if (strFlagJenisData == "Rupiah") {
                if (spinnerRupiah.getSelectedItem().toString() == "Net") {
                    strQuerySum = String.format(" sum((C.HarSat*C.jumlah)/1000000) as Total ");
                    strQueryJoin = " INNER JOIN mJualItem C on A.JLFaktur=C.JLFAKTUR  INNER JOIN mProducts D on C.Brg_Key=D.Brg_Key INNER JOIN mKat G on G.Kat_Key = D.Kat_Key ";
                }
            }
        }

        if (strFlagJenisLaporan == "bulanan") {
            strWaktu = " " +spinnerBulan.getSelectedItem().toString() +" " + spinnerBulanTahun.getSelectedItem().toString() + " ";
            strQueryTime = " AND DATEDIFF(MONTH,TglFaktur,'"+( spinnerBulan.getSelectedItemPosition()+1)+"/1/"+spinnerBulanTahun.getSelectedItem().toString()+"')=0 ";
        } else if (strFlagJenisLaporan == "range tanggal") {
//            strTempTglAwal = tglAwal.getText().toString();
//            strTempTglAKhir = tglAkhir.getText().toString();
            strWaktu = " " + tglAwal.getText().toString() + " s/d " +  tglAkhir.getText().toString() + " ";
            strQueryTime = " AND DATEDIFF(DAY,TglFaktur,'"+tglAwal.getText().toString()+"')<=0  AND  DATEDIFF(DAY,TglFaktur,'"+tglAkhir.getText().toString()+"')>=0";
        } else if (strFlagJenisLaporan == "tahunan") {
//            strTahun = spinnerTahun.getSelectedItem().toString();
            strWaktu = " Tahun " + spinnerTahun.getSelectedItem().toString() + " ";
            strQueryTime = " AND DATEDIFF(YEAR,TglFaktur,'1/1/"+spinnerTahun.getSelectedItem().toString()+"')=0 ";

        } else {
        }

        strQueryPassing = String.format("select %s %s FROM mJualNota A\n" +
                "INNER JOIN  mSalesman B on A.Sales_Key =B.Sales_key %s\n" +
                "INNER JOIN mCity E ON A.City_Key = E.City_Key\n" +
                "INNER JOIN mProvinsi F on E.Provinsi_Key = F.Provinsi_Key\n" +
                "WHERE ISA2=0 %s\n" +
                "GROUP BY %s\n" +
                "order by Total ",strQueryTop,strQuerySum, strQueryJoin, strQueryTime, strQueryGroup);
        Intent intent = new Intent(LaporanRankingActivity.this, RankingChartActivity.class);
        intent.putExtra("strQueryPassing", strQueryPassing);
        intent.putExtra("strTitle", strJenis + strWaktu + strSatuan);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
//        System.out.println("KRISNAA "+ strQueryPassing);
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
                        LaporanRankingActivity.this,
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
                        LaporanRankingActivity.this,
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
        flagShowGraph = 0;
        RBRupiah.setChecked(true);
        strFlagJenisData = "Rupiah";
        flagData = 1;

    }

    public void loadBulan() {
        listJenisLaporan.add("tahunan");
        listJenisLaporan.add("bulanan");
        listJenisLaporan.add("range tanggal");

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear - 2; i <= thisYear; i++) {
            listyearView.add(Integer.toString(i));
        }

        listSpinnerRupiah.add("Net");
        listSpinnerRupiah.add("Gross");

        listSpinnerSatuan.add("Lembar");
        listSpinnerSatuan.add("Tonase");

        listSpinnerRanking.add("Distributor");
        listSpinnerRanking.add("Provinsi");
        listSpinnerRanking.add("Kota");
        listSpinnerRanking.add("Jenis Barang");

        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            listBulanView.add(month_name);
        }
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(LaporanRankingActivity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(LaporanRankingActivity.this,
                R.layout.spinner_value, listyearView);
        ArrayAdapter<String> jenisLaporan_adapter = new ArrayAdapter<String>(LaporanRankingActivity.this,
                R.layout.spinner_value, listJenisLaporan);
        ArrayAdapter<String> jenisLaporanRupiah_adapter = new ArrayAdapter<String>(LaporanRankingActivity.this,
                R.layout.spinner_value, listSpinnerRupiah);
        ArrayAdapter<String> jenisSatuan_adapter = new ArrayAdapter<String>(LaporanRankingActivity.this,
                R.layout.spinner_value, listSpinnerSatuan);
        ArrayAdapter<String> jenisRanking_adapter = new ArrayAdapter<String>(LaporanRankingActivity.this,
                R.layout.spinner_value, listSpinnerRanking);
        spinnerSatuan.setAdapter(jenisSatuan_adapter);
        spinnerRanking.setAdapter(jenisRanking_adapter);
        spinnerTahun.setAdapter(tahun_adapter);
        spinnerBulanTahun.setAdapter(tahun_adapter);
        spinnerBulan.setAdapter(bulan_adapter);
        spinnerRupiah.setAdapter(jenisLaporanRupiah_adapter);
        spinnerBulanTahun.setSelection(2);
        spinnerTahun.setSelection(2);
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
