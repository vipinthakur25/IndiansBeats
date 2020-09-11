package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;

public class ReportAProblemActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivGoBack;
    private EditText etIssue;

    private ImageView ivAddPhoto;
    private ImageView ivScreenShot;
    private LinearLayout lvAddIssue;
    private TextView tvSubmitProblem;

    private TextView tvAddScreenshot;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_a_problem);

        initViews();
    }

    private void initViews() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivScreenShot = findViewById(R.id.ivScreenShot);
        etIssue = findViewById(R.id.etIssue);
        ivAddPhoto = findViewById(R.id.ivAddPhoto);
        lvAddIssue = findViewById(R.id.lvAddIssue);
        tvSubmitProblem = findViewById(R.id.tvSubmitProblem);
        tvAddScreenshot = findViewById(R.id.tvAddScreenshot);


        ivGoBack.setOnClickListener(this);
        lvAddIssue.setOnClickListener(this);
        tvSubmitProblem.setOnClickListener(this);

        etIssue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String issue = etIssue.getText().toString();
                if (issue.isEmpty() || issue.length() < 10) {
                    tvSubmitProblem.setBackground(getDrawable(R.drawable.bg_button_disabled));
                    tvSubmitProblem.setTextColor(getResources().getColor(R.color.colorDisableText));
                    tvSubmitProblem.setEnabled(false);
                } else {
                    tvSubmitProblem.setBackground(getDrawable(R.drawable.bg_button_gradient));
                    tvSubmitProblem.setTextColor(getResources().getColor(R.color.colorTextTitle));
                    tvSubmitProblem.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (view == lvAddIssue) {
            chooseImage();
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageuri = data.getData();

            Glide.with(ReportAProblemActivity.this).load(imageuri).into(ivAddPhoto);
            ivAddPhoto.setVisibility(View.VISIBLE);
            lvAddIssue.setVisibility(View.GONE);
            ivScreenShot.setVisibility(View.GONE);
            tvAddScreenshot.setVisibility(View.GONE);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


}
