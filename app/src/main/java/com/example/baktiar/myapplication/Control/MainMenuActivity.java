package com.example.baktiar.myapplication.Control;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.baktiar.myapplication.R;
import com.example.baktiar.myapplication.View.ViewPagerAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class MainMenuActivity extends BaseActivity {
    Intent intent;
    LinearLayout id1, id2, id3, id4, id5, id6, id7, id11;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String dbSelect, strRole, strArea_key ="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your
        getLayoutInflater().inflate(R.layout.activity_main_menu, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        strRole = getIntent().getStringExtra("role");
        if(strRole.equals("Area Manager"))
        {
            strArea_key = getIntent().getStringExtra("area_key");
        }

//        System.out.println("STRROLE " +strRole);
//        System.out.println("STRKEY " +strArea_key);

        id1 = (LinearLayout) findViewById(R.id.id1);
        id2 = (LinearLayout) findViewById(R.id.id2);
        id3 = (LinearLayout) findViewById(R.id.id3);
        id4 = (LinearLayout) findViewById(R.id.id4);
        id5 = (LinearLayout) findViewById(R.id.id5);
        id6 = (LinearLayout) findViewById(R.id.id6);
        id7 = (LinearLayout) findViewById(R.id.id7);
        id11 = (LinearLayout) findViewById(R.id.id11);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // id10.setVisibility(View.GONE);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        dbSelect = "true";
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        //JIKA BUILD selain TAA Command perintah dibawah, karena TBM atau TPI tidak ada perbandingan bahan dengan PLC
          id11.setVisibility(View.GONE);
//        try {
            if (strRole.equals("Manager")) {
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id11.setVisibility(View.GONE);
            }
            else if(strRole.equals("Area Manager")){
                id5.setVisibility(View.GONE);
                id6.setVisibility(View.GONE);
                id11.setVisibility(View.GONE);
                id4.setVisibility(View.GONE);
            }

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            MainMenuActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });

        }
    }


    public void clickHandler(View v) {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        switch (v.getId()) {
            case R.id.id1:
                intent = new Intent(MainMenuActivity.this, LaporanPenjualanActivity.class);
                intent.putExtra("role", strRole);
                if(strRole.equals("Area Manager")){
                    intent.putExtra("area_key", strArea_key);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.id2:
                intent = new Intent(MainMenuActivity.this, LaporanOmzetActivity.class);
                intent.putExtra("role", strRole);
                if(strRole.equals("Area Manager")){
                    intent.putExtra("area_key", strArea_key);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.id3:
                intent = new Intent(MainMenuActivity.this, LaporanOmzetPieActivity.class);
                intent.putExtra("role", strRole);
                if(strRole.equals("Area Manager")){
                    intent.putExtra("area_key", strArea_key);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.id4:
                intent = new Intent(MainMenuActivity.this, ReturActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.id5:
                intent = new Intent(MainMenuActivity.this, ProduksiActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.id6:
                intent = new Intent(MainMenuActivity.this, Produksi_Mesin_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.id7:
                intent = new Intent(MainMenuActivity.this, LaporanHarianActivity.class);
                intent.putExtra("role", strRole);
                if(strRole.equals("Area Manager")){
                    intent.putExtra("area_key", strArea_key);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.id11:
                intent = new Intent(MainMenuActivity.this, PemakaianBarangActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.setTheme(R.style.AlertDialogCustom);
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        MainMenuActivity.this.finish();
                        finish();
                        Intent intent = new Intent(MainMenuActivity.this, RegisterActivity.class);
                        intent.putExtra("iFlagTimer", 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}

