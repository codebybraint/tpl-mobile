package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.baktiar.myapplication.R;

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

public class FilterBottom extends BottomSheetDialogFragment {
    private FilterBottom.BottomSheetListener mListener;
    CheckBox cBoxPOID, cBoxDate;
    TextView txtTglAwal, txtTglAkhir, txtCloseGraph;
    EditText eTxtCustomer;
    String strCompanyFilter = "", strBulanFilter = "", strTahunFilter = "", strJudulFilter = "";
    ProgressDialog p;
    int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    ArrayList<String> listBulanView = new ArrayList<>();
    ArrayList<String> listyearView = new ArrayList<>();
    ArrayList<String> listCompanyView = new ArrayList<>();
    Spinner  spnCompany;
    Object response = null;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.filter_bottom, container, false);
        txtCloseGraph = v.findViewById(R.id.txtCloseGraph);


//        spnBulan = v.findViewById(R.id.spnBulan);
//        spnTahun = v.findViewById(R.id.spnTahun);
        spnCompany = v.findViewById(R.id.spnCompany);
        Button btnFilterGraph = v.findViewById(R.id.btnFilterGraph);
        listCompanyView.clear();

//        for (int i = 0; i < months.length; i++) {
//            Calendar cal = Calendar.getInstance();
//            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
//            cal.set(Calendar.MONTH, months[i]);
//            String month_name = month_date.format(cal.getTime());
//            listBulanView.add(month_name);
//        }
//        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
//        for (int i = thisYear - 5; i <= thisYear; i++) {
//            listyearView.add(Integer.toString(i));
//        }

//        listCompanyView.add("TBM");
//        listCompanyView.add("TAA");
        listCompanyView.add("ALL");
        SyncData syncData = new SyncData();
        syncData.execute();
//        listCompanyView.add("TPMJ");

//        ArrayAdapter<String> bulan_adapter = new ArrayAdapter<String>(getContext(),
//                R.layout.spinner_value, listBulanView);
//        ArrayAdapter<String> tahun_adapter = new ArrayAdapter<String>(getContext(),
//                R.layout.spinner_value, listyearView);
        ArrayAdapter<String> company_adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_value, listCompanyView);
        spnCompany.setAdapter(company_adapter);
//        spnTahun.setAdapter(tahun_adapter);
//        spnTahun.setSelection(5);
//        spnBulan.setAdapter(bulan_adapter);


        txtCloseGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnFilterGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (spnCompany.getSelectedItemPosition() == 2) {
//                    strCompanyFilter = "TPT";
//                } else {
//                    strCompanyFilter = spnCompany.getSelectedItem().toString();
//                }
//                strBulanFilter = String.valueOf(spnBulan.getSelectedItemPosition() + 1);
//                strTahunFilter = spnTahun.getSelectedItem().toString();
////                txtShowText.setText(spnCompany.getSelectedItem().toString() + " " + spnBulan.getSelectedItem().toString() + " " + spnTahun.getSelectedItem().toString());
//                strJudulFilter = "Report " + spnCompany.getSelectedItem().toString() + "\n" + spnBulan.getSelectedItem().toString() + " " + spnTahun.getSelectedItem().toString();
                mListener.onButtonClickedFilter(spnCompany.getSelectedItem().toString());
                dismiss();
            }
        });
        return v;
    }

    public interface BottomSheetListener {
        void onButtonClickedFilter(String strCompanyFilter);
    }
    public class SyncData extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(getContext(), R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            SoapObject request = new SoapObject("http://tempuri.org/", "Company");




            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
                httpTransport.call("http://tempuri.org/Company", envelope);
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
                            listCompanyView.add(e.getString("Com_Code"));

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setCancelable(false);
                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else if (isSuccess == "fail") {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setCancelable(false);
                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            } else {


            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "asdasd");

        }

    }

}