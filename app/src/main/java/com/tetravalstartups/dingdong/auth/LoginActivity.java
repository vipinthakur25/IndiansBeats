package com.tetravalstartups.dingdong.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.ComingSoonActivity;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivGoBack;
    private ProgressBar progressBar;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvForgot;
    private TextView tvLogin;
    private LinearLayout lhSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        progressBar = findViewById(R.id.progressBar);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvForgot = findViewById(R.id.tvForgot);
        tvLogin = findViewById(R.id.tvLogin);
        lhSignUp = findViewById(R.id.lhSignUp);

        ivGoBack.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        lhSignUp.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack){
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        if (v == tvLogin){
            doUiValidation();
        }

        if (v == lhSignUp){
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    private void doUiValidation() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etEmail.requestFocus();
            etEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)){
            etPassword.requestFocus();
            etPassword.setError("Password is required");
            return;
        }

        if (password.length() < 6){
            etPassword.requestFocus();
            etPassword.setError("Minimum six characters are required");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        tvLogin.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
        doLogin(email, password);
    }

    private void doLogin(String email, String password) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    tvLogin.setEnabled(true);
                    etEmail.setEnabled(true);
                    etPassword.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvLogin.setEnabled(true);
                        etEmail.setEnabled(true);
                        etPassword.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
