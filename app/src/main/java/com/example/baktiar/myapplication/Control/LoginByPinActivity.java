package com.example.baktiar.myapplication.Control;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
//import com.goodiebag.pinview.Pinview;
import com.example.baktiar.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginByPinActivity extends BaseActivity implements LoginByFingerPrint.BottomSheetListener {

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    ProgressDialog p;
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            p = new ProgressDialog(LoginByPinActivity.this, R.style.MyTheme);
            p.setMessage("Loading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
//            Toast.makeText(getApplicationContext(), "Pin complete: " + pin, Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Pin complete: " + pin);
            SharedPreferences mPrefs = getSharedPreferences("label", 0);
            String mString = mPrefs.getString("tag", "Tidak ada");

            if (mString.equals(pin)) {

                overridePendingTransition(R.anim.enter, R.anim.exit);
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("Users");
                DatabaseReference zone1Ref = zonesRef.child(currentFirebaseUser.getUid());

                zone1Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(LoginByPinActivity.this, MainMenuActivity.class);
                        intent.putExtra("role", dataSnapshot.child("role").getValue().toString());
                        if (dataSnapshot.child("role").getValue().toString().equals("Area Manager")) {
                            intent.putExtra("area_key", dataSnapshot.child("taa_key").getValue().toString());
//                                                intent.putExtra("area_key", dataSnapshot.child("tbm_key").getValue().toString());
//                                                intent.putExtra("area_key", dataSnapshot.child("tpi_key").getValue().toString());
                        }
                        startActivity(intent);

                        finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        System.out.println("KRISNA 13");
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginByPinActivity.this);
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

            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginByPinActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Pin anda salah, silahkan coba lagi..");
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
            p.dismiss();
        }

        @Override
        public void onEmpty() {
            System.out.println("KRISNA 11");
//            Toast.makeText(getApplicationContext(), "Pin Empty: " , Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            System.out.println("KRISNA 12");
//            Toast.makeText(getApplicationContext(), "Pin changed, new length " + pinLength + " with intermediate pin " , Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_pin);

        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(6);

        LoginByFingerPrint loginByFingerPrint = new LoginByFingerPrint();
        loginByFingerPrint.show(getSupportFragmentManager(), "exampleLoginByFingerPrint");
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//    }
}
