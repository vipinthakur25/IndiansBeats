package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tetravalstartups.dingdong.R;

public class ChatWithUsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivGoBack;
    private EditText etAlternateEmail;
    private EditText etAlternateNumber;
    private EditText etDescription;
    private TextView tvSend;
    private boolean et1State = false;
    private boolean et2State = false;
    private boolean et3State = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_us);
        initViews();
    }

    private void initViews() {
        ivGoBack = findViewById(R.id.ivGoBack);
        etAlternateEmail = findViewById(R.id.etAlternateEmail);
        etAlternateNumber = findViewById(R.id.etAlternateNumber);
        etDescription = findViewById(R.id.etDescription);
        tvSend = findViewById(R.id.tvSend);


        ivGoBack.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        etAlternateEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String alternateEmail = etAlternateEmail.getText().toString();

                if (alternateEmail.isEmpty()) {
                    et1State = false;
                    enableButton(false);
                } else {
                    et1State = true;
                    if (et1State && et2State && et3State) {
                        enableButton(true);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etAlternateNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String alternateNumber = etAlternateNumber.getText().toString();

                if (alternateNumber.isEmpty()) {
                    et2State = false;
                    enableButton(false);
                } else {
                    et2State = true;
                    if (et1State && et2State && et3State) {
                        enableButton(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = etDescription.getText().toString();

                if (description.isEmpty()) {
                    et3State = false;
                    enableButton(false);
                } else {
                    et3State = true;
                    if (et1State && et2State && et3State) {
                        enableButton(true);
                    }

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
        if (view == tvSend) {
            sendData();
        }
    }

    private void sendData() {
        String alternateEmail = etAlternateEmail.getText().toString();
        String alternateNumber = etAlternateNumber.getText().toString();
        String description = etDescription.getText().toString();

        if (TextUtils.isEmpty(alternateEmail)) {
            etAlternateEmail.requestFocus();
            etAlternateEmail.setError("Email required");
            return;
        }
        if (TextUtils.isEmpty(alternateNumber) || alternateNumber.length() < 10) {
            etAlternateNumber.requestFocus();
            etAlternateNumber.setError("Enter correct phone number");
            return;
        }
        if (TextUtils.isEmpty(description) || (description.length() < 20)) {
            etDescription.requestFocus();
            etDescription.setError("Description must be more than 20 characters");
        }
    }

    private void enableButton(boolean inputState) {
        if (inputState) {
            tvSend.setEnabled(true);
            tvSend.setBackground(getResources().getDrawable(R.drawable.bg_button_gradient));
            tvSend.setTextColor(getResources().getColor(R.color.colorTextTitle));
        } else {
            tvSend.setEnabled(false);
            tvSend.setBackground(getResources().getDrawable(R.drawable.bg_button_disabled));
            tvSend.setTextColor(getResources().getColor(R.color.colorDisableText));
        }

    }

}