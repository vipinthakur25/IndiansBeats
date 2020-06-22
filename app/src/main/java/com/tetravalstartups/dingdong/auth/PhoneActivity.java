package com.tetravalstartups.dingdong.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.tetravalstartups.dingdong.R;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private EditText etPhoneNumber;
    private TextView tvSendOtp;
    private LinearLayout lvPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        initView();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        lvPhone = findViewById(R.id.lvPhone);
        
        tvSendOtp = findViewById(R.id.tvSendOtp);
        tvSendOtp.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        if (v == tvSendOtp){
            doPhoneNumberValidation();
        }
    }

    private void doPhoneNumberValidation() {
        String phone = etPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phone)){
            Snackbar.make(lvPhone, "Phone number required", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isDigitsOnly(phone)){
            Snackbar.make(lvPhone, "Valid phone number required", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() <10){
                Snackbar.make(lvPhone, "Valid phone number required", Snackbar.LENGTH_SHORT).show();
                return;
        }
        
        sendCode(phone);
        
    }

    private void sendCode(String phone) {
        Intent intent = new Intent(PhoneActivity.this, CodeActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
