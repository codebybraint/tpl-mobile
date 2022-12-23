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
import com.example.baktiar.myapplication.Model.Departemen;
import com.example.baktiar.myapplication.Model.ReturList;
import com.example.baktiar.myapplication.R;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReturActivity extends BaseActivity implements FilterBottom.BottomSheetListener {
    boolean[] checkedItemGudang;
    String[] namesStringArrayGudang;
    ArrayList<Integer> numCheckedArrayGudang = new ArrayList<>();
    ArrayList<String> listGudangChecked = new ArrayList<>();
    ProgressDialog p;
    Spinner thnRangetgl, spinnerblnAwal, spinnerblnAkhir;
    Button buttonShow;
    TextView sd, txtTahun, txtJenisLaporan, txtBulan;
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    BarDataSet barDataSet = new BarDataSet(barEntries, " ");
    ArrayList<BarDataSet> xAbsis = new ArrayList<>();

    ArrayList<String> theDates = new ArrayList<>();
    View line1;
    RadioButton RBTahun, RBBulan, RBBulanRange, RBPenjualan, RBProduksi, RBSemua, RBPresentase, RBLembar, RBHandling, RBTonase;
    RadioGroup radioGroupOutput2, radioGroupOutput3, radioGroupOutput4;
    String strTglAwal = "", strTglAkhir = "", strCompany = "ALL", flagJenisLaporan, tempWaktu2, tempWaktu, tempMesin, tempTahun, flagOutput, flagReturGrafik, bulanAwal, bulanAkhir, tahun, flagIndikator, flagTitle1, flagTitle2, flagTitle3, flagTitle4, flagTitle5;
    String strSatuan = "", strGudangPilih = " ", strJenisData = "Bulanan", strJenis = "Handling";
    public static String rslt = "";
    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listJenisLaporan = new ArrayList<>();
    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    CheckBox CBGudangCheck;
    int iFlagGudang = 0;
    private ArrayList<Departemen> departemen;
    Button btnFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retur);
        sd = (TextView) findViewById(R.id.sd);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        //txtTahun1 = (TextView) findViewById(R.id.txtTahun1);
        txtBulan = (TextView) findViewById(R.id.txtBulan);
        txtJenisLaporan = (TextView) findViewById(R.id.txtJenisLaporan);
        radioGroupOutput2 = (RadioGroup) findViewById(R.id.radioGroupOutput2);
        radioGroupOutput3 = (RadioGroup) findViewById(R.id.radioGroupOutput3);
        radioGroupOutput4 = (RadioGroup) findViewById(R.id.radioGroupOutput4);
        spinnerblnAwal = (Spinner) findViewById(R.id.blnAwal);
        spinnerblnAkhir = (Spinner) findViewById(R.id.blnAKhir);
        thnRangetgl = (Spinner) findViewById(R.id.thnRangetgl);
        RBTahun = (RadioButton) findViewById(R.id.RBTahun);
        RBBulan = (RadioButton) findViewById(R.id.RBBulan);
        RBSemua = (RadioButton) findViewById(R.id.RBSemua);
        RBPenjualan = (RadioButton) findViewById(R.id.RBPenjualan);
        RBProduksi = (RadioButton) findViewById(R.id.RBProduksi);
        RBHandling = (RadioButton) findViewById(R.id.RBHandling);
        RBBulanRange = (RadioButton) findViewById(R.id.RBBulanRange);
        RBPresentase = (RadioButton) findViewById(R.id.RBPresentase);
        RBLembar = (RadioButton) findViewById(R.id.RBLembar);
        RBTonase = (RadioButton) findViewById(R.id.RBTonase);
        CBGudangCheck = (CheckBox) findViewById(R.id.CBDepartemen);
        line1 = (View) findViewById(R.id.line1);
        departemen = new ArrayList<Departemen>();

        buttonShow = (Button) findViewById(R.id.buttonShow);
        btnFilter = (Button) findViewById(R.id.btnFilter);

        final android.app.AlertDialog ad = new android.app.AlertDialog.Builder(this).create();
        loadBulan();
        invisible();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Barang reject");
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ReturActivity.this);
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
                    condition1();
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
                    thnRangetgl.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.INVISIBLE);
                    flagJenisLaporan = "TAHUNAN";
                    strJenisData = "Tahunan";
                } else if (i == R.id.RBBulanRange) {
                    spinnerblnAwal.setVisibility(View.VISIBLE);
                    spinnerblnAkhir.setVisibility(View.VISIBLE);
                    sd.setVisibility(View.VISIBLE);
                    thnRangetgl.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.VISIBLE);
                    flagJenisLaporan = "RANGE";
                    strJenisData = "Bulanan";
                } else if (i == R.id.RBBulan) {
                    spinnerblnAwal.setVisibility(View.VISIBLE);
                    spinnerblnAkhir.setVisibility(View.INVISIBLE);
                    sd.setVisibility(View.INVISIBLE);
                    thnRangetgl.setVisibility(View.VISIBLE);
                    txtTahun.setVisibility(View.VISIBLE);
                    txtBulan.setVisibility(View.VISIBLE);
                    flagJenisLaporan = "BULANAN";
                    strJenisData = "Bulanan";
                }
            }
        });

        radioGroupOutput3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBPenjualan) {
                    flagReturGrafik = "Retur Penjualan";
                    strJenis = "Penjualan";
                } else if (i == R.id.RBProduksi) {
                    flagReturGrafik = "Retur Produksi";
                    strJenis = "Produksi";
                } else if (i == R.id.RBSemua) {
                    flagReturGrafik = "Retur Semua";
                } else if (i == R.id.RBHandling) {
                    flagReturGrafik = "Retur Handling";
                    strJenis = "Handling";
                }
            }
        });

        radioGroupOutput4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RBPresentase) {
                    flagOutput = "Presentase";
                } else if (i == R.id.RBLembar) {
                    flagOutput = "Lembar";
                } else if (i == R.id.RBTonase) {
                    flagOutput = "Tonase";
                }
            }
        });
    }

    public void Select(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.CBDepartemen:
                if (checked) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ReturActivity.this);
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
                                listGudangChecked.add(namesStringArrayGudang[numCheckedArrayGudang.get(i)]);
                                iFlagGudang++;
                            }

                            strGudangPilih = ("" + listGudangChecked + "").replace("[", "(").replace("]", ")");

                            for (int y = 0; y < listGudangChecked.size(); y++) {

                                for (int x = 0; x < departemen.size(); x++) {
                                    if (listGudangChecked.get(y).equals(departemen.get(x).getDepName())) {
                                        if (departemen.get(x).getJns_Key() == 3) {
                                        }
                                    }
                                }
                            }

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

    //ketika load awal, invisible beberapa menu
    public void invisible() {
        txtBulan.setVisibility(View.VISIBLE);
        spinnerblnAwal.setVisibility(View.VISIBLE);
        spinnerblnAkhir.setVisibility(View.VISIBLE);
        sd.setVisibility(View.VISIBLE);
        thnRangetgl.setVisibility(View.VISIBLE);
        txtTahun.setVisibility(View.VISIBLE);
        RBBulanRange.setChecked(true);
        flagJenisLaporan = "RANGE";
        RBHandling.setChecked(true);
        strJenis = "Handling";
        flagReturGrafik = "Retur Handling";
        RBPresentase.setChecked(true);
        flagOutput = "Presentase";
    }


    public void condition1() {
        if (flagReturGrafik == "Retur Handling") {
            strJenis = "Handling";
            flagTitle1 = "Reject - Handling";
        } else if (flagReturGrafik == "Retur Produksi") {
            strJenis = "Produksi";
            flagTitle1 = "Reject - Produksi";
        } else if (flagReturGrafik == "Retur Penjualan") {
            strJenis = "Penjualan";
            flagTitle1 = "Reject - Penjualan";
        }

        if (flagOutput == "Lembar") {
            strSatuan = "lembar";
        } else if (flagOutput == "Tonase") {
            strSatuan = "ton";
        } else if (flagOutput == "Presentase") {
            strSatuan = "%";
        }
        if (flagJenisLaporan == "RANGE") {
            tempWaktu = String.valueOf(spinnerblnAwal.getSelectedItemPosition() + 1).toString();
            tempWaktu2 = String.valueOf(spinnerblnAkhir.getSelectedItemPosition() + 1).toString();
            bulanAwal = spinnerblnAwal.getSelectedItem().toString();
            bulanAkhir = spinnerblnAkhir.getSelectedItem().toString();
            tempTahun = thnRangetgl.getSelectedItem().toString();
            strJenisData = "Bulanan";
            flagTitle2 = String.format("%s s/d %s %s (%s)", bulanAwal, bulanAkhir, tempTahun, strSatuan);

            strTglAwal = String.format("%s/1/%s", tempWaktu, tempTahun);
            strTglAkhir = String.format("%s/1/%s", tempWaktu2, tempTahun);
        } else if (flagJenisLaporan == "TAHUNAN") {
            tempTahun = thnRangetgl.getSelectedItem().toString();
            strJenisData = "Tahunan";
            flagTitle2 = String.format("Tahun %s (%s)", tempTahun, strSatuan);
            strTglAwal = tempTahun;

        } else if (flagJenisLaporan == "BULANAN") {
            tempWaktu = String.valueOf(spinnerblnAwal.getSelectedItemPosition() + 1).toString();
            tempWaktu2 = String.valueOf(spinnerblnAkhir.getSelectedItemPosition() + 1).toString();
            bulanAwal = spinnerblnAwal.getSelectedItem().toString();
            bulanAkhir = spinnerblnAkhir.getSelectedItem().toString();
            tempTahun = thnRangetgl.getSelectedItem().toString();
            strJenisData = "Bulanan";
            strTglAwal = bulanAwal;
            strTglAkhir = tempTahun;
            flagTitle2 = String.format("Bulan %s Tahun %s (%s)", bulanAwal, tempTahun, strSatuan);
        }

        if (flagReturGrafik == "Retur Produksi") {

            flagTitle3 = "Komposisi Barang Reject";
            flagTitle4 = "Retur Produksi Bulan ";
            flagTitle5 = " Tahun " + tempTahun;
        } else if (flagReturGrafik == "Retur Penjualan") {

            flagTitle3 = "Komposisi Barang Reject";
            flagTitle4 = "Retur Penjualan Bulan ";
            flagTitle5 = " Tahun " + tempTahun;
        } else {
            flagTitle3 = "Komposisi Barang Reject";
            flagTitle4 = "Retur Handling Bulan ";
            flagTitle5 = " Tahun " + tempTahun;
        }
        Intent intent = new Intent(ReturActivity.this, GrafikBarActivity.class);

        intent.putExtra("strCompany", strCompany);
        intent.putExtra("strJenisSatuan", flagOutput);
        intent.putExtra("strMesin", "");
        intent.putExtra("iGudang", 0);
        intent.putExtra("strGudang", "");
        intent.putExtra("strTglAwal", strTglAwal);
        intent.putExtra("strTglAkhir", strTglAkhir);
        intent.putExtra("strFlagTgl", flagJenisLaporan);
        intent.putExtra("strJenisData", strJenisData);
        intent.putExtra("strJenis", strJenis);

        intent.putExtra("flagIndikator", flagIndikator);
        intent.putExtra("flagTitle1", flagTitle1);
        intent.putExtra("flagTitle2", flagTitle2);
        intent.putExtra("flagTitle3", flagTitle3);
        intent.putExtra("flagTitle4", flagTitle4);
        intent.putExtra("flagTitle5", flagTitle5);
        intent.putExtra("flagReturGrafik", flagReturGrafik);
        intent.putExtra("flagOutput", flagOutput);

        //flag act
        intent.putExtra("flagActivity", "Retur");
        //data-data inputan
        intent.putExtra("Tahun", tempTahun);
        intent.putExtra("tempTglAwal", bulanAwal);
        intent.putExtra("tempTglAKhir", bulanAkhir);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    //ketika load awal, load bbrp data untuk spinner
    public void loadBulan() {
        listJenisLaporan.add("range bulan");
        listJenisLaporan.add("tahunan");
        listJenisLaporan.add("bulanan");

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear - 5; i <= thisYear; i++) {
            listyearView.add(Integer.toString(i));
        }

        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            //I DO SOMETHING HERE
            SimpleDateFormat month_date = new SimpleDateFormat("MM/dd/yyyy");
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            listBulanView.add(month_name);
        }
        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(ReturActivity.this,
                R.layout.spinner_value, listBulanView);
        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(ReturActivity.this,
                R.layout.spinner_value, listyearView);
        thnRangetgl.setAdapter(tahun_adapter);
        spinnerblnAwal.setAdapter(bulan_adapter);
        spinnerblnAkhir.setAdapter(bulan_adapter);
        spinnerblnAkhir.setSelection(months.length - 1);
        thnRangetgl.setSelection(5);
        CBGudangCheck.setVisibility(View.GONE);
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
//        if (strCompany.equals("ALL")){
            CBGudangCheck.setVisibility(View.GONE);
//        }
//        else{
//            CBGudangCheck.setVisibility(View.VISIBLE);
//        }
    }
}
