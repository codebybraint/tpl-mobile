package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.baktiar.myapplication.Model.ApiClient;
import com.example.baktiar.myapplication.Model.ApiInterface;
import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.RequestNotificaton;
import com.example.baktiar.myapplication.Model.SendNotificationModel;
import com.example.baktiar.myapplication.Model.User;
import com.example.baktiar.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Callback;

import static com.example.baktiar.myapplication.R.id.spnTPI;

public class RegisterActivity extends AppCompatActivity {
    Intent intent;
    Button btn_signin, btn_register, btnShowPassword, resetPassword, byPin, registerPin, btn_loginByFinger, btn_loginByPin;
    EditText edtEmail, edtPassword;
    CheckBox CBKeepLogin;
    FirebaseAuth auth;
    FirebaseDatabase db;
    RelativeLayout rootLayout;
    DatabaseReference users;
    ApiInterface apiService;
    SharedPreferences sp, spNotif;
    SharedPreferences.Editor mEditor;
    DataSnapshot dataSnapshot;
    ArrayList<String> listSpinnerApk = new ArrayList<>();
    ArrayList<String> listSpinnerRole = new ArrayList<>();
    ArrayList<String> listDatabase = new ArrayList<>();
    ArrayList<String> aListUserNameTBM = new ArrayList<>();
    ArrayList<String> aListUserNameTAA = new ArrayList<>();
    ArrayList<String> aListUserNameTPMJ = new ArrayList<>();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static String rslt = "";
    ProgressDialog p;
    Double data2;
    String strJumlahOmzet, strJumlahNota;
    Integer intJumlahLembar;
    String query, strCompany;
    String strQuery;
    Integer iFlagTimer = 0;
    public final String SOAP_ADDRESS = "http://ext2.triarta.co.id/wcf/MoBI.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iFlagTimer = getIntent().getIntExtra("iFlagTimer", 0);
        if (iFlagTimer == 0) {
            setContentView(R.layout.activity_register);
            spNotif = getSharedPreferences("Notif", 0);
            mEditor = spNotif.edit();


            sp = getSharedPreferences("login", MODE_PRIVATE);
            if (sp.getBoolean("logged", false)) {


//                String mString = sp.getString("lastUser", "Tidak ada");
//                String mArea = sp.getString("lastArea_Key", "Tidak ada");
//                Intent intent = new Intent(RegisterActivity.this, MainMenuActivity.class);
//                intent.putExtra("role", mString);
//                if (mString.equals("Area Manager")) {
//                    intent.putExtra("area_key", mArea);
//                }
//                startActivity(intent);
//                finish();


                overridePendingTransition(R.anim.enter, R.anim.exit);
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("Users");
                DatabaseReference zone1Ref = zonesRef.child(currentFirebaseUser.getUid());
                DatabaseReference zone1NameRef = zone1Ref.child("role");


                zone1Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(RegisterActivity.this, MainMenuActivity.class);
                        intent.putExtra("role", dataSnapshot.child("role").getValue().toString());
                        if(dataSnapshot.child("role").getValue().toString().equals("Area Manager"))
                        {
                            intent.putExtra("area_key", dataSnapshot.child("taa_key").getValue().toString());
                        }
                        startActivity(intent);

                        finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setCancelable(false);
                        dialog.setMessage("User anda tidak aktif, silahkan hubungi Admin.");
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
                });
            }


            if (spNotif.getBoolean("Notif On", true)) {
                System.out.println("KRISNA NOTIF ON");
//            Toast.makeText(this, "SUDAH ON", Toast.LENGTH_SHORT).show();
                mEditor.putBoolean("Notif On", true).commit();
//                FirebaseMessaging.getInstance().subscribeToTopic("TAA1");
//            FirebaseMessaging.getInstance().subscribeToTopic("TBM1");
            FirebaseMessaging.getInstance().subscribeToTopic("TES");
//            spNotif.edit().putBoolean("Notif On", true).apply();
//            FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.notif_app_name));
            } else {
                //            spNotif.edit().putBoolean("Notif On", false).apply();
//            Toast.makeText(this, "TIDAK ON", Toast.LENGTH_SHORT).show();
                System.out.println("KRISNA NOTIF OFF");
                mEditor.putBoolean("Notif On", false).commit();
//                FirebaseMessaging.getInstance().unsubscribeFromTopic("TAA1");
//            FirebaseMessaging.getInstance().unsubscribeFromTopic("TBM1");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("TES");
//            FirebaseMessaging.getInstance().unsubscribeFromTopic(getResources().getString(R.string.notif_app_name));
            }

            CBKeepLogin = (CheckBox) findViewById(R.id.CBKeepLogin);
            btn_loginByFinger = (Button) findViewById(R.id.btn_loginByFinger);
            byPin = (Button) findViewById(R.id.byPin);
            resetPassword = (Button) findViewById(R.id.resetPassword);
            btn_register = (Button) findViewById(R.id.btn_register);
            btnShowPassword = (Button) findViewById(R.id.btnShowPassword);
            btn_signin = (Button) findViewById(R.id.btn_signin);
            rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
            auth = FirebaseAuth.getInstance();
            db = FirebaseDatabase.getInstance();
            users = db.getReference("Users");
            edtEmail = (EditText) findViewById(R.id.edtEmail);
            edtPassword = (EditText) findViewById(R.id.edtPassword);
            CBKeepLogin.setVisibility(View.GONE);
            btn_register.setVisibility(View.GONE);
            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
            edtPassword.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);


            final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(RegisterActivity.this, R.style.AlertDialogCustom));
            resetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showResetPass();

                }
            });

            btn_loginByFinger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (android.os.Build.VERSION.SDK_INT < 23) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setCancelable(false);
                        dialog.setMessage("Tidak memiliki fitur fingerprint..");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
//                            finish();
//                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                        final AlertDialog alert = dialog.create();
                        alert.show();
                    } else {
                        startActivity(new Intent(RegisterActivity.this, FingerPrintActivity.class));
                        overridePendingTransition(R.anim.enter, R.anim.exit);

                    }
                }
            });

            byPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences mPrefs = getSharedPreferences("label", 0);
                    String mString = mPrefs.getString("tag", "Tidak ada");


                    if (mString.equals("Tidak ada") == true) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setCancelable(false);
                        dialog.setMessage("Pin tidak ada..");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
//                            finish();
//                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                        final AlertDialog alert = dialog.create();
                        alert.show();
                    } else {
                        if (CBKeepLogin.isChecked() == true) {
                            sp.edit().putBoolean("logged", true).apply();
                        } else {
                            sp.edit().putBoolean("logged", false).apply();
                        }
                        startActivity(new Intent(RegisterActivity.this, LoginByPinActivity.class));
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                }
            });

            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                strQuery = "Select AM_key from mAM";
                    SyncData mydata = new SyncData();
                    mydata.execute();

//                SendNotificationModel sendNotificationModel = new SendNotificationModel("Data Tidak ada silahkan cek lagi", "Notif for Admin");
//                RequestNotificaton requestNotificaton = new RequestNotificaton();
//                requestNotificaton.setSendNotificationModel(sendNotificationModel);
//                //token is id , whom you want to send notification ,
//                requestNotificaton.setToken("/topics/Approval");
//
////        requestNotificaton.setToken("dHxLVg0-VT4:APA91bEwUpFSbyodkd2zOrIfi7543g0_11WCCNPq1Ut0NiJRjOyE0ud8TxdjRVgVwhJbVIEJhW6TlwMO33RfLdbbTHEACieIdx8B4iCwLs90QG1OhFl8cdqfJ60M_aNItrbM-M_XB1GI");
////        final  Call<ResponseBody>
//                apiService = ApiClient.getClient().create(ApiInterface.class);
//                retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);
//
//                responseBodyCall.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                        Log.d("kkkk", "done");
//                        Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
////                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
////                FirebaseMessaging.getInstance().subscribeToTopic("all");
//                    }
//
//                    @Override
//                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
//                        Log.d("kkkk", "failed");
//                    }
//                });
//
////
////                sendNotificationToPatner();
////                FirebaseMessaging.getInstance().unsubscribeFromTopic("tes2");
////                FirebaseMessaging.getInstance().subscribeToTopic("tes4");
//
////                tester();


                }
            });

            btnShowPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtPassword.getTransformationMethod() == null) {
                        edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                        btnShowPassword.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_off_black_24dp, null));
                    } else {
                        edtPassword.setTransformationMethod(null);
                        btnShowPassword.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove_red_eye_black_24dp, null));
                    }
                }
            });
            btn_signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
                        Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                                .show();
                        return;
                    }

                    if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
                        Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                                .show();
                        return;
                    }

                    if ((edtPassword.getText().toString().length() < 6)) {
                        Snackbar.make(rootLayout, "Password to short !!!", Snackbar.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    final android.app.AlertDialog waitingDialog = new SpotsDialog(RegisterActivity.this);

                    waitingDialog.show();
                    //Login
                    auth.signInWithEmailAndPassword(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    waitingDialog.dismiss();

                                    // startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));


                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("Users");
                                    DatabaseReference zone1Ref = zonesRef.child(currentFirebaseUser.getUid());
                                    System.out.println("APAKAH " + zone1Ref.child("Tidak").toString());
                                    DatabaseReference zone1NameRef = zone1Ref.child("role");

                                    zone1Ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child("role").getValue(String.class).equals("Area Manager") || dataSnapshot.child("role").getValue(String.class).equals("Manager") || dataSnapshot.child("role").getValue(String.class).equals("Super User")) {
                                                if (CBKeepLogin.isChecked() == true) {
                                                    sp.edit().putBoolean("logged", true).apply();
//                                                    sp.edit().putString("lastUser", dataSnapshot.child("role").getValue().toString()).apply();
//                                                    if (dataSnapshot.child("role").getValue().toString().equals("Area Manager")) {
//                                                        sp.edit().putString("lastArea_Key", dataSnapshot.child("taa_key").getValue().toString()).apply();
////                                                    sp.edit().putString("lastArea_Key", dataSnapshot.child("tbm_key").getValue().toString()).apply();
////                                                    sp.edit().putString("lastArea_Key", dataSnapshot.child("tpi_key").getValue().toString()).apply();
//
//                                                    }
                                                } else {
                                                    sp.edit().putBoolean("logged", false).apply();
                                                }
                                                Intent intent = new Intent(RegisterActivity.this, MainMenuActivity.class);
                                                intent.putExtra("role", dataSnapshot.child("role").getValue().toString());
                                                if (dataSnapshot.child("role").getValue().toString().equals("Area Manager")) {
                                                    intent.putExtra("area_key", dataSnapshot.child("taa_key").getValue().toString());
//                                                intent.putExtra("area_key", dataSnapshot.child("tbm_key").getValue().toString());
//                                                intent.putExtra("area_key", dataSnapshot.child("tpi_key").getValue().toString());
                                                }
                                                startActivity(intent);

                                                finish();

                                            } else {
                                                Snackbar.make(rootLayout, "Login Failed..", Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            waitingDialog.dismiss();
                            Snackbar.make(rootLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                    .show();

                            //Active button
                            btn_signin.setEnabled(true);
                        }
                    });
                }
            });


        }
        else   {
            finish();
        }
    }

    public class SyncData extends AsyncTask<String, String, String> {
        String isSuccess = "false";
        String msg;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            p = new ProgressDialog(RegisterActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            aListUserNameTBM.clear();
            aListUserNameTAA.clear();
            aListUserNameTPMJ.clear();
            SoapObject request = new SoapObject("http://tempuri.org/", "Register");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.bodyOut = request;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            Object response = null;
            String responseJSON;
            try {

                httpTransport.call("http://tempuri.org/Register", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = exception.toString();
            }
            System.out.println("KRISNA77 " + response.toString());

            if (response.toString() != "[]") {
                try {

                    JSONArray json = new JSONArray(response.toString());
                    if (json != null) {

                        for (int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            System.out.println("Com_Code " + e.getString("Com_Code"));
                            if (e.getString("Com_Code").equals("TBM")) {
                                aListUserNameTBM.add(e.getString("AM_key"));
//                                    System.out.println("TBM " + e.getString("UserName"));
                            }
//                            else if(x==1){
//                                aListUserNameTAA.add(e.getString("AM_key"));
//                                aListUserNameTPMJ.add(e.getString("AM_key"));
////                                    System.out.println("TBM1 " + e.getString("UserName"));
//                            }
                            else {
                                aListUserNameTAA.add(e.getString("AM_key"));
                                aListUserNameTPMJ.add(e.getString("AM_key"));
//                                    System.out.println("TBM1 " + e.getString("UserName"));
                            }
                        }
                        if (json.length() >= 1) {
                            msg = "Found";

                            isSuccess = "true";
                        } else {
                            p.dismiss();
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
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
                showRegisterDialog();
            }
        }
    }


//    public class SyncData extends AsyncTask<String, String, String> {
//        String isSuccess = "false";
//        String msg;
//
//        @Override
//        protected void onPreExecute() //Starts the progress dailog
//        {
//            p = new ProgressDialog(RegisterActivity.this, R.style.MyTheme);
//            p.setMessage("Loading");
//            p.setIndeterminate(false);
//            p.setCancelable(false);
//            p.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
//        {
//            aListUserNameTBM.clear();
//            aListUserNameTAA.clear();
//            aListUserNameTPMJ.clear();
//            try {
//
//                rslt = "START";
//                Caller caller = new Caller();
//                caller.strParamAct  ="RegisterAct";
////                caller.join();
//                caller.start();
//
//                while (rslt == "START") {
//                    try {
//                        Thread.sleep(10);
//
//                    } catch (Exception ex) {
//                    }
//                }
//
//            } catch (Exception ex) {
//            }
//            if (rslt != "[]") {
//                try {
//
//                    JSONArray json = new JSONArray(rslt);
//                    if (json != null) {
//
//                        for (int i = 0; i < json.length(); i++) {
//                            JSONObject e = json.getJSONObject(i);
//                            System.out.println("Com_Code " + e.getString("Com_Code"));
//                            if (e.getString("Com_Code").equals("TBM")) {
//                                aListUserNameTBM.add(e.getString("AM_key"));
////                                    System.out.println("TBM " + e.getString("UserName"));
//                            }
////                            else if(x==1){
////                                aListUserNameTAA.add(e.getString("AM_key"));
////                                aListUserNameTPMJ.add(e.getString("AM_key"));
//////                                    System.out.println("TBM1 " + e.getString("UserName"));
////                            }
//                            else{
//                                aListUserNameTAA.add(e.getString("AM_key"));
//                                aListUserNameTPMJ.add(e.getString("AM_key"));
////                                    System.out.println("TBM1 " + e.getString("UserName"));
//                            }
//                        }
//                        if (json.length() >= 1) {
//                            msg = "Found";
//
//                            isSuccess = "true";
//                        } else {
//                            p.dismiss();
//                            msg = "No Data found!";
//                            isSuccess = "false";
//                        }
//                    } else {
//
//                        msg = "No Data found!";
//                        isSuccess = "false";
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//                    isSuccess = "fail";
//                }
//            } else {
//
//                isSuccess = "false";
//            }
//            return msg;
//        }
//
//        @Override
//        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
//        {
//            p.dismiss();
//            if (isSuccess == "false") {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
//                dialog.setCancelable(false);
//                dialog.setMessage("Data Tidak ada, silahkan cek lagi..");
//                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
//                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                    }
//                });
//                final AlertDialog alert = dialog.create();
//                alert.show();
//            } else if (isSuccess == "fail") {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
//                dialog.setCancelable(false);
//                dialog.setMessage("Jaringan bermasalah, silahkan cek lagi..");
//                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
//                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                    }
//                });
//                final AlertDialog alert = dialog.create();
//                alert.show();
//            } else {
//                showRegisterDialog();
//            }
//        }
//    }
//
//    private class GetData extends AsyncTask<String, String, String> {
//        Calendar calendar = Calendar.getInstance();
//        String CurrentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        Connection connect;
//        String ConnectionResult = "";
//        String isSuccess = "false";
//        String msg;
//
//
//        @Override
//        protected void onPreExecute() //Starts the progress dailog
//        {
////            p = new ProgressDialog(NotificationServiceSyncInterval.this, R.style.MyTheme);
////            p.setMessage("Loading");
////            p.setIndeterminate(false);
////            p.setCancelable(false);
////            p.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
//        {
//
//            try {
//                rslt = "START";
//                Caller d = new Caller();
//                d.strQuery = query;
//                d.join();
//                d.start();
//                while (rslt == "START") {
//                    try {
//                        Thread.sleep(10);
//
//                    } catch (Exception ex) {
//                    }
//                }
//
//            } catch (Exception ex) {
//            }
//            if (rslt != "[]") {
//                try {
//
//                    JSONArray json = new JSONArray(rslt);
//                    if (json != null) {
//                        for (int i = 0; i < json.length(); i++) {
//                            JSONObject e = json.getJSONObject(i);
//                            data2 = e.getDouble("Gross");
//                            DecimalFormat f = new DecimalFormat("#.##");
//                            strJumlahOmzet = f.format(data2);
//                            strJumlahNota = e.getString("JumlahNota");
//                            intJumlahLembar = e.getInt("JumlahLembar");
//
////                            JumlahLembarData.setText(e.getInt("JumlahLembar") + " Lembar");
////                            JumlahNotaData.setText(e.getInt("JumlahNota") + " Lembar");
////                            JumlahOmzetData.setText(formattedValue + " Juta");
////                            data2.setValueFormatter(new PercentageFormatter());
////                            dataOmzetList.add(new DataOmzetList(CurrentDate, e.getLong("Total")));
////                        }
//                        }
//                        if (json.length() >= 1) {
//                            msg = "Found";
//                            isSuccess = "true";
//                        } else {
//                            msg = "No Data found!";
//                            isSuccess = "false";
//                        }
//                        isSuccess = "true";
//                    } else {
//                        msg = "No Data found!";
//                        isSuccess = "false";
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    isSuccess = "fail";
//                }
//            } else {
//                isSuccess = "false";
//            }
//            return msg;
//        }
//
//        @Override
//        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
//        {
////            p.dismiss();
//            if (isSuccess == "false") {
//                SendNotificationModel sendNotificationModel = new SendNotificationModel("Data Tidak ada silahkan cek lagi", "Notif for Admin");
//                RequestNotificaton requestNotificaton = new RequestNotificaton();
//                requestNotificaton.setSendNotificationModel(sendNotificationModel);
//                //token is id , whom you want to send notification ,
//                requestNotificaton.setToken("/topics/Sam");
//
////        requestNotificaton.setToken("dHxLVg0-VT4:APA91bEwUpFSbyodkd2zOrIfi7543g0_11WCCNPq1Ut0NiJRjOyE0ud8TxdjRVgVwhJbVIEJhW6TlwMO33RfLdbbTHEACieIdx8B4iCwLs90QG1OhFl8cdqfJ60M_aNItrbM-M_XB1GI");
////        final  Call<ResponseBody>
//                apiService = ApiClient.getClient().create(ApiInterface.class);
//                retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);
//
//                responseBodyCall.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                        Log.d("kkkk", "done");
//                        Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
////                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
////                FirebaseMessaging.getInstance().subscribeToTopic("all");
//                    }
//
//                    @Override
//                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
//                        Log.d("kkkk", "failed");
//                    }
//                });
//            } else if (isSuccess == "fail") {
//                SendNotificationModel sendNotificationModel = new SendNotificationModel("Jaringan Bermasalah silahkan cek lagi", "Notif for Admin");
//                RequestNotificaton requestNotificaton = new RequestNotificaton();
//                requestNotificaton.setSendNotificationModel(sendNotificationModel);
//                //token is id , whom you want to send notification ,
//                requestNotificaton.setToken("/topics/Sam");
////        requestNotificaton.setToken("dHxLVg0-VT4:APA91bEwUpFSbyodkd2zOrIfi7543g0_11WCCNPq1Ut0NiJRjOyE0ud8TxdjRVgVwhJbVIEJhW6TlwMO33RfLdbbTHEACieIdx8B4iCwLs90QG1OhFl8cdqfJ60M_aNItrbM-M_XB1GI");
////        final  Call<ResponseBody>
//                apiService = ApiClient.getClient().create(ApiInterface.class);
//                retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);
//
//                responseBodyCall.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                        Log.d("kkkk", "done");
//                        Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
////                Log.d("kkkk", FirebaseInstanceId.getInstance().getToken());
////                FirebaseMessaging.getInstance().subscribeToTopic("all");
//                    }
//
//                    @Override
//                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
//                        Log.d("kkkk", "failed");
//                    }
//                });
//            } else {
//                try {
//                    sendNotificationToPatner();
////                    JumlahLembarData.setText(JumlahLembar+ " Lembar");
////                    JumlahNotaData.setText(JumlahNota + " Lembar");
////                    JumlahOmzetData.setText(JumlahOmzet + " Juta");
//////                    barEntries.clear();
//////                    theDates.clear();
//////                    for (int x = 0; x < dataOmzetList.size(); x++) {
//////                        barEntries.add(new BarEntry(x, (Long) dataOmzetList.get(x).getTotal()));
//////                        theDates.add(dataOmzetList.get(x).getSalesName());
////                    }
////                    // txtShowText.setText("Laporan Penjualan  Bulan " +spinnerblnAwal.getSelectedItem().toString()+" s/d "+spinnerblnAkhir.getSelectedItem().toString()+" Tahun " +tempTahun);
////                    barChart.setVisibility(View.VISIBLE);
////                    txtShowText.setVisibility(View.VISIBLE);
////                    Graphic();
//                } catch (Exception ex) {
//
//                }
//
//            }
////            return dataPenjualanList;
//        }
//    }

    private void sendNotification(final String regToken) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", "Hi this is sent from device to device");
                    dataJson.put("title", "dummy title");
                    json.put("notification", dataJson);
                    json.put("to", regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=AIzaSyAZiX4tECscbA5KrXgGqWLyXfQeKxGxons")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                } catch (Exception e) {
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();

    }

    private void sobad() throws IOException, JSONException {
        String authKey = "AIzaSyAZiX4tECscbA5KrXgGqWLyXfQeKxGxons";   // You FCM AUTH key
        String FMCurl = "https://fcm.googleapis.com/fcm/send";
        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("to", "fBwFVD_1GC4:APA91bEUHsfvlpRkGW2R77VJ_3XDQONnuWuH2l4G5PDwpArl0CW2aKgWCi-BNuM6dG6mfDS6dlost9_9779DBjqOEwtDcSkwf4zSYsQYpvg6ZGJPjQOHtR97-90YkhcCgiQrPuep0Vh9ZU3aUmpAf4dhnzc-E_gF_g");
        JSONObject info = new JSONObject();
        info.put("title", "Ciayo");   // Notification title
        info.put("body", "Koe Koe"); // Notification body
        json.put("notification", info);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();
        conn.getInputStream();
    }

    private void showResetPass() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(RegisterActivity.this, R.style.AlertDialogCustom));
        dialog.setTitle("RESET PASSWORD");
        dialog.setMessage("Please use email to reset");

        LayoutInflater inflater = LayoutInflater.from(this);
        View reset_layout = inflater.inflate(R.layout.layout_reset_password, null);

        final MaterialEditText edtEmail = reset_layout.findViewById(R.id.edtEmail);
        dialog.setView(reset_layout);

        dialog.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                //Check Validation
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(edtEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snackbar.make(rootLayout, "Email sent, open your email.", Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void showRegisterDialog() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(RegisterActivity.this, R.style.AlertDialogCustom));

        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(RegisterActivity.this, R.style.AlertDialogCustom));

//        dialog.setTitle("REGISTER");
//        dialog.setMessage("Please use email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtRepeatPassword = register_layout.findViewById(R.id.edtRepeatPassword);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);
        final Spinner spnApk = register_layout.findViewById(R.id.spnApk);
        final Spinner spnRole = register_layout.findViewById(R.id.spnRole);
        final Spinner spnTBM = register_layout.findViewById(R.id.spnTBM);
        final Spinner spnTAA = register_layout.findViewById(R.id.spnTAA);
        final Spinner spnTPI = register_layout.findViewById(R.id.spnTPI);
        //spnRole = (Spinner) findViewById(R.id.spnRole);
//        loadSpinner();

        spnRole.setAdapter(null);
        spnApk.setAdapter(null);

        spnTAA.setAdapter(null);
        spnTBM.setAdapter(null);
        spnTPI.setAdapter(null);

        listSpinnerRole.clear();
        listSpinnerApk.clear();

        listSpinnerApk.add("tbm");
        listSpinnerApk.add("taa");
        listSpinnerApk.add("register");
        listSpinnerApk.add("retur");
        listSpinnerApk.add("all");
        listSpinnerRole.add("Super User");
        listSpinnerRole.add("Manager");
        listSpinnerRole.add("Area Manager");

        ArrayAdapter<String> apk_adapter = new ArrayAdapter<String>(RegisterActivity.this,
                R.layout.spinner_value, listSpinnerApk);
        ArrayAdapter<String> role_adapter = new ArrayAdapter<String>(RegisterActivity.this,
                R.layout.spinner_value, listSpinnerRole);
        ArrayAdapter<String> tbm_adapter = new ArrayAdapter<String>(RegisterActivity.this,
                R.layout.spinner_value, aListUserNameTBM);
        ArrayAdapter<String> taa_adapter = new ArrayAdapter<String>(RegisterActivity.this,
                R.layout.spinner_value, aListUserNameTAA);
        ArrayAdapter<String> tpi_adapter = new ArrayAdapter<String>(RegisterActivity.this,
                R.layout.spinner_value, aListUserNameTPMJ);
        spnApk.setAdapter(apk_adapter);
        spnRole.setAdapter(role_adapter);
        spnTBM.setAdapter(tbm_adapter);
        spnTAA.setAdapter(taa_adapter);
        spnTPI.setAdapter(tpi_adapter);

        dialog.setView(register_layout);

        //set Button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                //Check Validation
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter phone number", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if ((edtPassword.getText().toString().length() < 6)) {
                    Snackbar.make(rootLayout, "Password to short !!!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(edtRepeatPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if ((edtRepeatPassword.getText().toString().length() < 6)) {
                    Snackbar.make(rootLayout, "Password to short !!!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                //dataSnapshot.getValue(String.class).equals("all") == true
                if (edtRepeatPassword.getText().toString().equals(edtPassword.getText().toString()) == false) {
                    Snackbar.make(rootLayout, "Password tidak sama, silahkan cek lagi..", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                //Register new user
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtRepeatPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //Save user to db

                                User user = new User();
                                user.setEmail(edtEmail.getText().toString());
                                user.setName(edtName.getText().toString());
                                user.setPhone(edtPhone.getText().toString());
                                user.setPassword(edtRepeatPassword.getText().toString());
                                user.setApk(spnApk.getSelectedItem().toString());
                                user.setRole(spnRole.getSelectedItem().toString());
                                user.setTbm_key(spnTBM.getSelectedItem().toString());
                                user.setTaa_key(spnTAA.getSelectedItem().toString());
                                user.setTpi_key(spnTPI.getSelectedItem().toString());
                                //Use email  to key
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout, "Register success", Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();

    }

    private void tbm() {
        if (dataSnapshot.getValue(String.class).equals("all") == true || dataSnapshot.getValue(String.class).equals("tbm") == true) {
            startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
            finish();
            //id10.setVisibility(View.GONE);
        } else {
            Snackbar.make(rootLayout, "Login Failed..", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void taa() {
        if (dataSnapshot.getValue(String.class).equals("all") == true || dataSnapshot.getValue(String.class).equals("taa") == true) {
            startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
            finish();
            //id10.setVisibility(View.GONE);
        } else {
            Snackbar.make(rootLayout, "Login Failed..", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void retur() {
        if (dataSnapshot.getValue(String.class).equals("all") == true || dataSnapshot.getValue(String.class).equals("retur") == true) {
            startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
            finish();
            //id10.setVisibility(View.GONE);
        } else {
            Snackbar.make(rootLayout, "Login Failed..", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void register() {
        if (dataSnapshot.getValue(String.class).equals("all") == true || dataSnapshot.getValue(String.class).equals("register") == true) {
            startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
            finish();
            //id10.setVisibility(View.GONE);
        } else {
            Snackbar.make(rootLayout, "Login Failed..", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void sendNotificationToPatner() {

        SendNotificationModel sendNotificationModel = new SendNotificationModel("Jumlah Lembar = " + intJumlahLembar + "\nJumlah Nota = " + strJumlahNota + "\nJumlah Gross = " + strJumlahOmzet + "juta", "Target terpenuhi!");
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

    private void sendNotificationToPatner1() {

        SendNotificationModel sendNotificationModel = new SendNotificationModel("check", "notif");
        RequestNotificaton requestNotificaton = new RequestNotificaton();
        requestNotificaton.setSendNotificationModel(sendNotificationModel);
        //token is id , whom you want to send notification ,
        requestNotificaton.setToken("/topics/tes2");
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

    @Override
    public void onBackPressed() {
        finish();
    }

}
