package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.ApiClient;
import com.example.baktiar.myapplication.Model.ApiInterface;
import com.example.baktiar.myapplication.Model.RequestNotificaton;
import com.example.baktiar.myapplication.Model.SendNotificationModel;
import com.example.baktiar.myapplication.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class TestingTopic extends AppCompatActivity {
    Spinner spinnerDist;
    Button buttonShow,buttonSend;
//    ApiInterface apiService;
    ArrayList<String> listSpinnerRupiah = new ArrayList<>();
    String query;
    ApiInterface apiService;
    ProgressDialog p;
    Double data2;
    String JumlahOmzet,JumlahNota;
    Integer JumlahLembar;
    public static String rslt = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_topic);
        spinnerDist = (Spinner) findViewById(R.id.spinnerDist);
        buttonShow = (Button) findViewById(R.id.buttonShow);
        buttonSend= (Button) findViewById(R.id.buttonSend);
        listSpinnerRupiah.add("Net");
        listSpinnerRupiah.add("Gross");
        listSpinnerRupiah.add("Failed");

        Date todayDate = Calendar.getInstance().getTime();

//        int thisYear = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
//        System.out.print("KUCING123 = " +thisYear);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        Date myDate = dateFormat.parse(dateString);


//        SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
//        Calendar currentCal = Calendar.getInstance();
//         currentDate=dateFormat.format(currentCal.getTime());
//        currentCal.add(Calendar.DATE, -6);
//         weekAgo=dateFormat.format(currentCal.getTime());
//        System.out.println("TODAY " + currentDate);
//        System.out.println("TODAY2 " + weekAgo);


//        Date todayDate = Calendar.getInstance().getTime();
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//        final String todayString = formatter.format(todayDate);
        ArrayAdapter<String> jenisLaporanRupiah_adapter = new ArrayAdapter<String>(TestingTopic.this,
                R.layout.spinner_value, listSpinnerRupiah);
        spinnerDist.setAdapter(jenisLaporanRupiah_adapter);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerDist.getSelectedItem().toString()=="Net"){
                    FirebaseMessaging.getInstance().subscribeToTopic(spinnerDist.getSelectedItem().toString());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Gross");
                }
                else if (spinnerDist.getSelectedItem().toString()=="Gross"){
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Gross");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Test1");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Net");
                }
                else if (spinnerDist.getSelectedItem().toString()=="Failed"){
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Gross");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Net");
                    FirebaseMessaging.getInstance().subscribeToTopic("Test1");
                }
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // sendNotificationToPatner();
                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                final String todayString = formatter.format(todayDate);
                query = "SELECT sum((HarSat*jumlah*1.1)/1000000) as Gross,  Count(Distinct A.JLFaktur) as JumlahNota, sum(jumlah) as JumlahLembar\n" +
                        "FROM mJualNota A\n" +
                        "INNER JOIN mJualItem B on A.JLFaktur=B.JLFaktur where datediff(day,JLTanggal,'" + todayString + "')=0 and ISA2=0";
//                sendNotificationToPatner();
                GetData mydata = new GetData();
                mydata.execute();
            }
        });
    }
//    private void sendNotificationToPatner() {
//
//        SendNotificationModel sendNotificationModel = new SendNotificationModel("Cek Notif Bosku", "kepala dan bukan ekor");
//        RequestNotificaton requestNotificaton = new RequestNotificaton();
//        requestNotificaton.setSendNotificationModel(sendNotificationModel);
//        //token is id , whom you want to send notification ,
//        requestNotificaton.setToken("/topics/Sam");
////        requestNotificaton.setToken("dHxLVg0-VT4:APA91bEwUpFSbyodkd2zOrIfi7543g0_11WCCNPq1Ut0NiJRjOyE0ud8TxdjRVgVwhJbVIEJhW6TlwMO33RfLdbbTHEACieIdx8B4iCwLs90QG1OhFl8cdqfJ60M_aNItrbM-M_XB1GI");
////        final  Call<ResponseBody>
//        apiService = ApiClient.getClient().create(ApiInterface.class);
//        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);
//
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                Log.d("kkkk", "done");
//                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
////                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
////                FirebaseMessaging.getInstance().subscribeToTopic("all");
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
//                Log.d("kkkk", "failed");
//            }
//        });
//    }
private class GetData extends AsyncTask<String, String, String> {
    Calendar calendar = Calendar.getInstance();
    String CurrentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    Connection connect;
    String ConnectionResult = "";
    String isSuccess = "false";
    String msg;


    @Override
    protected void onPreExecute() //Starts the progress dailog
    {
//            p = new ProgressDialog(NotificationServiceSyncInterval.this, R.style.MyTheme);
//            p.setMessage("Loading");
//            p.setIndeterminate(false);
//            p.setCancelable(false);
//            p.show();
    }

    @Override
    protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
    {

        try {
            rslt = "START";
            Caller d = new Caller();
            d.strQuery = query;
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
                        System.out.println("HALOOOOO "+e);
                        System.out.println("HALOOOOO "+e.getLong("Gross"));
                        data2 = e.getDouble("Gross");
                        DecimalFormat f = new DecimalFormat("#.##");
                        JumlahOmzet = f.format(data2);
                        JumlahNota = e.getString("JumlahNota");
                        JumlahLembar = e.getInt("JumlahLembar");
                        System.out.println("HALOOOOO888 "+JumlahOmzet);
                        System.out.println("1111111111 "+JumlahOmzet);
                        System.out.println("2222222222 "+JumlahNota);
                        System.out.println("3333333333 "+JumlahLembar);

//                            JumlahLembarData.setText(e.getInt("JumlahLembar") + " Lembar");
//                            JumlahNotaData.setText(e.getInt("JumlahNota") + " Lembar");
//                            JumlahOmzetData.setText(formattedValue + " Juta");
//                            data2.setValueFormatter(new PercentageFormatter());
//                            dataOmzetList.add(new DataOmzetList(CurrentDate, e.getLong("Total")));
//                        }
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
//            p.dismiss();
        if (isSuccess == "false") {SendNotificationModel sendNotificationModel = new SendNotificationModel("Data Tidak ada silahkan cek lagi", "Notif for Admin");
            RequestNotificaton requestNotificaton = new RequestNotificaton();
            requestNotificaton.setSendNotificationModel(sendNotificationModel);
            //token is id , whom you want to send notification ,
            requestNotificaton.setToken("/topics/Test1");
//        requestNotificaton.setToken("dHxLVg0-VT4:APA91bEwUpFSbyodkd2zOrIfi7543g0_11WCCNPq1Ut0NiJRjOyE0ud8TxdjRVgVwhJbVIEJhW6TlwMO33RfLdbbTHEACieIdx8B4iCwLs90QG1OhFl8cdqfJ60M_aNItrbM-M_XB1GI");
//        final  Call<ResponseBody>
            apiService = ApiClient.getClient().create(ApiInterface.class);
            retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);

            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    Log.d("kkkk", "done");
                    Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
//                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
//                FirebaseMessaging.getInstance().subscribeToTopic("all");
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.d("kkkk", "failed");
                }
            });
        }else if(isSuccess == "fail"){
            SendNotificationModel sendNotificationModel = new SendNotificationModel("Jaringan Bermasalah silahkan cek lagi", "Notif for Admin");
            RequestNotificaton requestNotificaton = new RequestNotificaton();
            requestNotificaton.setSendNotificationModel(sendNotificationModel);
            //token is id , whom you want to send notification ,
            requestNotificaton.setToken("/topics/Test1");
//        requestNotificaton.setToken("dHxLVg0-VT4:APA91bEwUpFSbyodkd2zOrIfi7543g0_11WCCNPq1Ut0NiJRjOyE0ud8TxdjRVgVwhJbVIEJhW6TlwMO33RfLdbbTHEACieIdx8B4iCwLs90QG1OhFl8cdqfJ60M_aNItrbM-M_XB1GI");
//        final  Call<ResponseBody>
            apiService = ApiClient.getClient().create(ApiInterface.class);
            retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);

            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    Log.d("kkkk", "done");
                    Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
//                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
//                FirebaseMessaging.getInstance().subscribeToTopic("all");
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.d("kkkk", "failed");
                }
            });
        }
        else {
            try {
                sendNotificationToPatner();
//                    JumlahLembarData.setText(JumlahLembar+ " Lembar");
//                    JumlahNotaData.setText(JumlahNota + " Lembar");
//                    JumlahOmzetData.setText(JumlahOmzet + " Juta");
////                    barEntries.clear();
////                    theDates.clear();
////                    for (int x = 0; x < dataOmzetList.size(); x++) {
////                        barEntries.add(new BarEntry(x, (Long) dataOmzetList.get(x).getTotal()));
////                        theDates.add(dataOmzetList.get(x).getSalesName());
//                    }
//                    // txtShowText.setText("Laporan Penjualan  Bulan " +spinnerblnAwal.getSelectedItem().toString()+" s/d "+spinnerblnAkhir.getSelectedItem().toString()+" Tahun " +tempTahun);
//                    barChart.setVisibility(View.VISIBLE);
//                    txtShowText.setVisibility(View.VISIBLE);
//                    Graphic();
            } catch (Exception ex) {

            }

        }
//            return dataPenjualanList;
    }
}
    private void sendNotificationToPatner() {

        SendNotificationModel sendNotificationModel = new SendNotificationModel("Jumlah Lembar = "+JumlahLembar+"\nJumlah Nota = "+JumlahNota+"\nJumlah Gross = "+JumlahOmzet+"juta", "Notif Sam");
        RequestNotificaton requestNotificaton = new RequestNotificaton();
        requestNotificaton.setSendNotificationModel(sendNotificationModel);
        //token is id , whom you want to send notification ,
        requestNotificaton.setToken("/topics/Test1");
//        requestNotificaton.setToken("dHxLVg0-VT4:APA91bEwUpFSbyodkd2zOrIfi7543g0_11WCCNPq1Ut0NiJRjOyE0ud8TxdjRVgVwhJbVIEJhW6TlwMO33RfLdbbTHEACieIdx8B4iCwLs90QG1OhFl8cdqfJ60M_aNItrbM-M_XB1GI");
//        final  Call<ResponseBody>
        apiService = ApiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("kkkk", "done");
                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
//                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
//                FirebaseMessaging.getInstance().subscribeToTopic("all");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.d("kkkk", "failed");
            }
        });
    }
}
