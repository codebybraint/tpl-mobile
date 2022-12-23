package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baktiar.myapplication.Model.Caller;
import com.example.baktiar.myapplication.Model.SalesListName;
import com.example.baktiar.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LogoutListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    SharedPreferences sp, spNotif;
    SharedPreferences.Editor mEditor;
    public static String rslt = "";
    ProgressDialog p;
    String strQuery;
    //    RelativeLayout rootLayout; e
    String newPassword = "cccccc";
    //    TextView tx;
    String[] s = {"India ", "Arica", "India ", "Arica", "India ", "Arica",
            "India ", "Arica", "India ", "Arica"};
//    SharedPreferences.Editor mEditor = spNotif.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).registerSessionListener(this);
        ((MyApp) getApplication()).startUserSession();

        setContentView(R.layout.activity_base);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spNotif = getSharedPreferences("Notif", 0);
        //   SharedPreferences mPrefs = getSharedPreferences("label", 0);
        mEditor = spNotif.edit();
        // SharedPreferences mPrefs = getSharedPreferences("label", 0);

        //  mEditor.putString("Notif On", edtPinBaru2.getText().toString()).commit();


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String appPackageName = getPackageName();

                switch (item.getItemId()) {

                    case R.id.nav_change_pass:
                        Intent changePassword = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        changePassword.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        startActivity(changePassword);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_log_out:
                        Intent loginscreen = new Intent(getApplicationContext(), RegisterActivity.class);
                        loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        sp.edit().putBoolean("logged", false).apply();
                        startActivity(loginscreen);
                        finish();
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_create_pin:
                        Intent createPin = new Intent(getApplicationContext(), CreatePinActivity.class);
                        createPin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        startActivity(createPin);
                        drawer.closeDrawers();
                        break;

                    case R.id.nav_notifikasi:

                        if (spNotif.getBoolean("Notif On", true)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
                            dialog.setCancelable(false);
                            dialog.setMessage("Ingin Mematikan Notifikasi? ");
                            dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    /////////////JIKA tBM Test1, dan TAA Test2
//                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TAA1");
//                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TPT1");
//                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TBM1");
                                    System.out.println("KRISNA NOTIF OFF");
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TES");
//                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getResources().getString(R.string.notif_app_name));
//                                    spNotif.edit().putString("Notif On", "true").apply();
//                                    mEditor.putBoolean("Notif On", true);
                                    mEditor.putBoolean("Notif On", false).commit();
                                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(BaseActivity.this);
                                    dialog1.setCancelable(false);
                                    dialog1.setMessage("Berhasil Mematikan Notifikasi ");
                                    dialog1.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                                    final AlertDialog alert = dialog1.create();
                                    alert.show();
                                }
                            });
                            dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    spNotif.edit().putString("Notif On", "false").apply();
                                    mEditor.putBoolean("Notif On", true).commit();
//                                    mEditor.putBoolean("Notif On", false);
                                    /////////////JIKA tBM Test1, dan TAA Test2
//                                    FirebaseMessaging.getInstance().subscribeToTopic("TAA1");
//                                    FirebaseMessaging.getInstance().subscribeToTopic("TPT1");
//                                    FirebaseMessaging.getInstance().subscribeToTopic("TBM1");
                                    System.out.println("KRISNA NOTIF ON");
                                    FirebaseMessaging.getInstance().subscribeToTopic("TES");
//                                    FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.notif_app_name));
                                }
                            });
                            final AlertDialog alert = dialog.create();
                            alert.show();
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
                            dialog.setCancelable(false);
                            dialog.setMessage("Ingin Mengaktifkan Notifikasi? ");
                            dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
//                                    spNotif.edit().putString("Notif On", "false").apply();
                                    mEditor.putBoolean("Notif On", true).commit();
                                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(BaseActivity.this);
                                    dialog1.setCancelable(false);
                                    dialog1.setMessage("Berhasil Menyalakan Notifikasi ");
                                    dialog1.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            /////////////JIKA tBM Test1, dan TAA Test2
//                                            FirebaseMessaging.getInstance().subscribeToTopic("TAA1");
//                                            FirebaseMessaging.getInstance().subscribeToTopic("TPT1");
//                                            FirebaseMessaging.getInstance().subscribeToTopic("TBM1");
                                            System.out.println("KRISNA NOTIF ON");
                                            FirebaseMessaging.getInstance().subscribeToTopic("TES");
//                                            FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.notif_app_name));
                                        }
                                    });
                                    final AlertDialog alert = dialog1.create();
                                    alert.show();
                                }
                            });
                            dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    spNotif.edit().putString("Notif On", "true").apply();
                                    //mEditor.putBoolean("Notif On", true);
                                    mEditor.putBoolean("Notif On", false).commit();
                                    /////////////JIKA tBM Test1, dan TAA Test2
//                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TAA1");
//                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TPT1");
//                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TBM1");
                                    System.out.println("KRISNA NOTIF OFF");
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("TES");
//                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getResources().getString(R.string.notif_app_name));
                                }
                            });
                            final AlertDialog alert = dialog.create();
                            alert.show();

                        }

                        break;

                }
                return false;
            }
        });
    }

//    public class SyncData extends AsyncTask<String, String, String> {
//        String isSuccess = "false";
//        String msg;
//
//        @Override
//        protected void onPreExecute() //Starts the progress dailog
//        {
//            p = new ProgressDialog(BaseActivity.this, R.style.MyTheme);
//            p.setMessage("Loading");
//            p.setIndeterminate(false);
//            p.setCancelable(false);
//            p.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
//        {
//            try {
//                rslt = "START";
//                Caller d = new Caller();
//                d.strQuery = strQuery;
//                d.join();
//                d.start();
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
////                            salesListName.add(new SalesListName(e.getString("SalesName")));
//                        }
//                        System.out.println("Jumlah datanya "+ json.length());
//
//                        if (json.length() >= 1) {
//                            msg = "Found";
//                            p.dismiss();
//                            isSuccess = "true";
//                        } else {
//                            p.dismiss();
//                            msg = "No Data found!";
//                            isSuccess = "false";
//                        }
//                        //  isSuccess = true;
//                    } else {
//                        p.dismiss();
//                        msg = "No Data found!";
//                        isSuccess = "false";
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    p.dismiss();
//                    isSuccess = "fail";
//                }
//            } else {
//                p.dismiss();
//                isSuccess = "false";
//            }
//
//            return msg;
//        }
//
//        @Override
//        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
//        {
//            p.dismiss();
//            if (isSuccess == "false") {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
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
//                AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
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
//                System.out.println("Berhasil lho");
////                try {
////                    for (int x = 0; x < salesListName.size(); x++) {
////                        listSalesName.add(salesListName.get(x).getSalesName());
////                    }
////                    ArrayAdapter<String> dist_adapter = new ArrayAdapter<String>(LaporanPenjualanActivity.this, R.layout.spinner_value, listSalesName);
////                    spinnerDist.setAdapter(dist_adapter);
////                } catch (Exception ex) {
////
////                }
//
//            }
//        }
//    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        ((MyApp) getApplication()).onUserInteracted();
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
            super.onBackPressed();
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_change_pass) {
//           showResetPass();
//        }
//        else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSessionLogout() {
        finish();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("iFlagTimer", 1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        startActivity(new Intent(this, RegisterActivity.class));
    }
}
