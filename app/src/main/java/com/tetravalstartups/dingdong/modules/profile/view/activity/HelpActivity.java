package com.tetravalstartups.dingdong.modules.profile.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tetravalstartups.dingdong.R;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {
private ImageView ivGoBack;
private LinearLayout lhContactSupport;
private LinearLayout lhReportAProblem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initViews();
    }

    private void initViews() {
        ivGoBack = findViewById(R.id.ivGoBack);
        lhContactSupport = findViewById(R.id.lhContactSupport);
        lhReportAProblem = findViewById(R.id.lhReportAProblem);

        ivGoBack.setOnClickListener(this);
        lhReportAProblem.setOnClickListener(this);
        lhContactSupport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack){
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (view == lhReportAProblem){
            startActivity(new Intent(HelpActivity.this, ReportAProblemActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (view == lhContactSupport){
            startActivity(new Intent(HelpActivity.this, ContactSupportActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}