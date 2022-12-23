package com.example.baktiar.myapplication.Control;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.baktiar.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class ChangePasswordActivity extends BaseActivity {
    Button btnShowPasswordLama,btnShowPasswordBaru1,btnShowPasswordBaru2,btn_change_password;
    EditText edtPasswordLama,edtPasswordBaru1,edtPasswordBaru2;
    private SlidrInterface slidr;

    RelativeLayout rootLayout;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        btnShowPasswordLama = (Button) findViewById(R.id.btnShowPasswordLama);
        btnShowPasswordBaru1= (Button) findViewById(R.id.btnShowPasswordBaru1);
        btnShowPasswordBaru2= (Button) findViewById(R.id.btnShowPasswordBaru2);

        slidr = Slidr.attach(this);
        btn_change_password= (Button) findViewById(R.id.btn_change_password);
        edtPasswordLama = (EditText)findViewById(R.id.edtPasswordLama);
        edtPasswordLama.setTransformationMethod(new PasswordTransformationMethod());
        edtPasswordLama.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        edtPasswordBaru1 = (EditText)findViewById(R.id.edtPasswordBaru1);
        edtPasswordBaru1.setTransformationMethod(new PasswordTransformationMethod());
        edtPasswordBaru1.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        edtPasswordBaru2 = (EditText)findViewById(R.id.edtPasswordBaru2);
        edtPasswordBaru2.setTransformationMethod(new PasswordTransformationMethod());
        edtPasswordBaru2.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);


        btnShowPasswordLama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPasswordLama.getTransformationMethod() == null) {
                    edtPasswordLama.setTransformationMethod(new PasswordTransformationMethod());
                    btnShowPasswordLama.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_off_black_24dp, null));
                } else {
                    edtPasswordLama.setTransformationMethod(null);
                    btnShowPasswordLama.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove_red_eye_black_24dp, null));
                }
            }
        });

        btnShowPasswordBaru1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPasswordBaru1.getTransformationMethod() == null) {
                    edtPasswordBaru1.setTransformationMethod(new PasswordTransformationMethod());
                    btnShowPasswordBaru1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_off_black_24dp, null));
                } else {
                    edtPasswordBaru1.setTransformationMethod(null);
                    btnShowPasswordBaru1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove_red_eye_black_24dp, null));
                }
            }
        });

        btnShowPasswordBaru2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPasswordBaru2.getTransformationMethod() == null) {
                    edtPasswordBaru2.setTransformationMethod(new PasswordTransformationMethod());
                    btnShowPasswordBaru2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_off_black_24dp, null));
                } else {
                    edtPasswordBaru2.setTransformationMethod(null);
                    btnShowPasswordBaru2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_remove_red_eye_black_24dp, null));
                }
            }
        });

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPasswordLama.getText().toString())) {
                    edtPasswordLama.setError("Password Lama Kosong");
                    return;
                }
                else if (TextUtils.isEmpty(edtPasswordBaru1.getText().toString())){
                    edtPasswordBaru1.setError("Password Baru Kosong");
                    return;
                }
                else if (TextUtils.isEmpty(edtPasswordBaru2.getText().toString())){
                    edtPasswordBaru2.setError("Password Baru Kosong");
                    return;
                }
                if ((edtPasswordBaru1.getText().toString().length() < 6)) {
                    Snackbar.make(rootLayout, "Password to short !!! Min. 6 char", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if ((edtPasswordBaru2.getText().toString().length() < 6)) {
                    Snackbar.make(rootLayout, "Password to short !!! Min. 6 char", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),edtPasswordLama.getText().toString().trim());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                                            String a =edtPasswordBaru1.getText().toString();
//                                            String b = edtPasswordBaru2.getText().toString();
                            if (edtPasswordBaru1.getText().toString().equals(edtPasswordBaru2.getText().toString())){
                                user.updatePassword(edtPasswordBaru1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Snackbar.make(rootLayout,"Berhasil Mengganti Password.",Snackbar.LENGTH_SHORT).show();
//                                                            System.out.println("CIHUYYYYY  User password updated.");
                                            Intent intent;
                                            intent = new Intent(ChangePasswordActivity.this, RegisterActivity.class);
                                            finish();
                                            startActivity(intent);

                                        }
                                        else {
                                            Snackbar.make(rootLayout,"GAGAL Mengganti Password.",Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else {
                               // System.out.println("OKEEE "+edtPasswordBaru1.getText().toString()== edtPasswordBaru2.getText().toString());
//                                                Snackbar.make(rootLayout,"GAGAL Merubah Password.",Snackbar.LENGTH_SHORT).show();
                                edtPasswordBaru2.setError("Data tidak cocok, Cek passowrd baru anda..");
                            }
                        } else {
                            edtPasswordLama.setError("Password anda salah!");
                            return;
                        }
                    }
                });
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
