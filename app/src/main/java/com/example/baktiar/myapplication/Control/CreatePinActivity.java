package com.example.baktiar.myapplication.Control;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.baktiar.myapplication.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class CreatePinActivity extends BaseActivity {
    Button btnShowPinBaru1, btnShowPinBaru2, btn_create_pin;
    EditText edtPinBaru1, edtPinBaru2;
    private SlidrInterface slidr;

    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        btnShowPinBaru1 = (Button) findViewById(R.id.btnShowPinBaru1);
        btnShowPinBaru2 = (Button) findViewById(R.id.btnShowPinBaru2);
        btn_create_pin = (Button) findViewById(R.id.btn_create_pin);
        slidr = Slidr.attach(this);
        edtPinBaru1 = (EditText) findViewById(R.id.edtPinBaru1);
        edtPinBaru1.setTransformationMethod(new PasswordTransformationMethod());
        edtPinBaru2 = (EditText) findViewById(R.id.edtPinBaru2);
        edtPinBaru2.setTransformationMethod(new PasswordTransformationMethod());

        btnShowPinBaru1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPinBaru1.getTransformationMethod() == null) {
                    edtPinBaru1.setTransformationMethod(new PasswordTransformationMethod());
                    btnShowPinBaru1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_off_black_24dp, null));
                } else {
                    edtPinBaru1.setTransformationMethod(null);
                    btnShowPinBaru1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove_red_eye_black_24dp, null));
                }
            }
        });

        btnShowPinBaru2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPinBaru2.getTransformationMethod() == null) {
                    edtPinBaru2.setTransformationMethod(new PasswordTransformationMethod());
                    btnShowPinBaru2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_off_black_24dp, null));
                } else {
                    edtPinBaru2.setTransformationMethod(null);
                    btnShowPinBaru2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove_red_eye_black_24dp, null));
                }
            }
        });
        btn_create_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPinBaru1.getText().toString())) {
                    edtPinBaru1.setError("Pin Baru Kosong");
                    return;
                } else if (TextUtils.isEmpty(edtPinBaru2.getText().toString())) {
                    edtPinBaru2.setError("Pin Baru Kosong");
                    return;

                }
                if ((edtPinBaru2.getText().toString().length() < 6)) {
                    edtPinBaru2.setError("Pin < 6");
                    return;
                }
                if ((edtPinBaru1.getText().toString().length() < 6)) {
                    edtPinBaru1.setError("Pin < 6");
                    return;
                }
                if (edtPinBaru1.getText().toString().equals(edtPinBaru2.getText().toString()) == false) {
                    edtPinBaru2.setError("Pastikan Password sama");
                    return;
                } else {
                    SharedPreferences mPrefs = getSharedPreferences("label", 0);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("tag", edtPinBaru2.getText().toString()).commit();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(CreatePinActivity.this);
                    dialog.setCancelable(false);
                    dialog.setMessage("Pembuatan pin berhasil..");
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
