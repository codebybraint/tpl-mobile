package com.example.baktiar.myapplication.Control;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.baktiar.myapplication.Control.LaporanHarianActivity;
import com.example.baktiar.myapplication.Model.ApiClient;
import com.example.baktiar.myapplication.Model.ApiInterface;
import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.RequestNotificaton;
import com.example.baktiar.myapplication.Model.SendNotificationModel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * Created by Baktiar Krisna on 29/06/2018.
 */

public class NotificationServiceSyncInterval extends IntentService {
    private static final String CHANNEL_ID = "com.example.baktiarkrisna.testtimerkilled";
    String query;
    ApiInterface apiService;
    ProgressDialog p;
    Double data2;
    String strJumlahOmzet,strJumlahNota;
    Integer intJumlahLembar;
    SharedPreferences mPrefs;
    public static String rslt = "";
    public NotificationServiceSyncInterval() {
        super("Tracker");
    }

    public NotificationServiceSyncInterval(String paramString) {
        super(paramString);
    }

    @Override
    protected void onHandleIntent(Intent intent1) {
        //ToDo: put what you want to do here
        Intent intent = new Intent(this, LaporanHarianActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        Log.d("TAG", "Handler call");
        System.out.println("Kuda " );
        FirebaseMessaging.getInstance().subscribeToTopic("Sam");
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Notif")
//                .setContentText("Testing")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent );
//        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            mBuilder.setChannelId(CHANNEL_ID);
////        }
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            NotificationChannel channel = new NotificationChannel(
////                    CHANNEL_ID,
////                    "NotificationDemo",
////                    IMPORTANCE_DEFAULT
////            );
////            notificationManager.createNotificationChannel(channel);
////        }
//        notificationManager.notify(0, mBuilder.build());
//
////        query = "SELECT SalesName\n" +
////                "FROM mSalesman";
////        SyncData mydata = new SyncData();
////        mydata.execute();
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        final String todayString = formatter.format(todayDate);
        query = "SELECT ISNULL(sum((HarSat*jumlah*1.1)/1000000),0) as Gross,  ISNULL(Count(Distinct A.JLFaktur),0) as JumlahNota, ISNULL(sum(jumlah),0) as JumlahLembar\n" +
                "FROM mJualNota A\n" +
                "INNER JOIN mJualItem B on A.JLFaktur=B.JLFaktur where datediff(day,JLTanggal,'" + todayString + "')=0 and ISA2=0";
         mPrefs = getSharedPreferences("pesan", 0);

        String mString = mPrefs.getString("tag", "Belum terkirim");
        if (mString.equals("Sudah Terkirim")==false ){
            GetData mydata = new GetData();
            mydata.execute();
        }

//        sendNotificationToPatner();
    }
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
                            strJumlahOmzet = f.format(data2);
                            strJumlahNota = e.getString("JumlahNota");
                            intJumlahLembar = e.getInt("JumlahLembar");
//                            System.out.println("HALOOOOO888 "+JumlahOmzet);
//                            System.out.println("1111111111 "+JumlahOmzet);
//                            System.out.println("2222222222 "+JumlahNota);
//                            System.out.println("3333333333 "+JumlahLembar);

//                            JumlahLembarData.setText(e.getInt("JumlahLembar") + " Lembar");
//                            JumlahNotaData.setText(e.getInt("JumlahNota") + " Lembar");
//                            JumlahOmzetData.setText(formattedValue + " Juta");
//                            data2.setValueFormatter(new PercentageFormatter());
//                            dataOmzetList.add(new DataOmzetList(CurrentDate, e.getLong("Total")));
//                        }
                        }
                        if (json.length() >= 1) {
                            if (strJumlahNota.equals("10")){
                            msg = "Found";
                            isSuccess = "true";
                                SharedPreferences mPrefs = getSharedPreferences("pesan", 0);
                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                mEditor.putString("tag", "Sudah Terkirim").commit();
//                                mEditor.putString("tag", "Sudah Terkirim");
//                                mEditor.apply();
                            }
                            else {msg = "No Data found!";
                            isSuccess = "false";}
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
                requestNotificaton.setToken("/topics/Sam");
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
                requestNotificaton.setToken("/topics/Sam");
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
            else  {
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

        SendNotificationModel sendNotificationModel = new SendNotificationModel("Jumlah Lembar = "+intJumlahLembar+"\nJumlah Nota = "+strJumlahNota+"\nJumlah Gross = "+strJumlahOmzet+"juta", "Notif Sam");
        RequestNotificaton requestNotificaton = new RequestNotificaton();
        requestNotificaton.setSendNotificationModel(sendNotificationModel);
        //token is id , whom you want to send notification ,
        requestNotificaton.setToken("/topics/Sam");
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