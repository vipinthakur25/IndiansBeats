package com.tetravalstartups.dingdong.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.tetravalstartups.dingdong.R;

public class CodeActivity extends AppCompatActivity {

    private TextView tvCodeDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        initView();
    }

    private void initView() {
        tvCodeDesc = findViewById(R.id.tvCodeDesc);
        tvCodeDesc.setText(String.format("Please enter code the received on phone number %s",
                getIntent().getStringExtra("phone")));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
