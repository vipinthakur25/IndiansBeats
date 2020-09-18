package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.AuthInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.ChatWithUs;
import com.tetravalstartups.dingdong.modules.profile.model.HelpRequest;
import com.tetravalstartups.dingdong.modules.profile.model.HelpRequestResponse;
import com.tetravalstartups.dingdong.modules.profile.presenter.HelpRequestPresenter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.HelpRequestAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatWithUsActivity extends AppCompatActivity implements View.OnClickListener, HelpRequestPresenter.IHelpRequest {
    private ImageView ivGoBack;
    private EditText etAlternateEmail;
    private EditText etAlternateNumber;
    private EditText etDescription;
    private TextView tvSend;
    private boolean et1State = false;
    private boolean et2State = false;
    private boolean et3State = false;
    private Master master;
    private AuthInterface authInterface;
    private RecyclerView helpRequestRecyclerView;
    private HelpRequestAdapter helpRequestAdapter;

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
        helpRequestRecyclerView = findViewById(R.id.helpRequestRecyclerView);
        helpRequestRecyclerView = findViewById(R.id.helpRequestRecyclerView);
        helpRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        master = new Master(ChatWithUsActivity.this);
        authInterface = APIClient.getRetrofitInstance().create(AuthInterface.class);


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

        fetchHelpRequest();
    }

    private void fetchHelpRequest() {
        HelpRequestPresenter helpRequestPresenter = new HelpRequestPresenter(ChatWithUsActivity.this, ChatWithUsActivity.this);
        helpRequestPresenter.fetchResponse();
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
        String id = master.getId();
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
            return;
        }
        messageData(id, alternateEmail, alternateNumber, description);
    }

    private void messageData(String id, String alternateEmail, String alternateNumber, String description) {
        Call<ChatWithUs> chatWithUsCall = authInterface.chatWithUs(id, description, alternateNumber, alternateEmail);
        chatWithUsCall.enqueue(new Callback<ChatWithUs>() {
            @Override
            public void onResponse(Call<ChatWithUs> call, Response<ChatWithUs> response) {
                if (response.code() == 200){
                    Toast.makeText(ChatWithUsActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                    etAlternateEmail.setText("");
                    etAlternateNumber.setText("");
                    etDescription.setText("");
                }
            }

            @Override
            public void onFailure(Call<ChatWithUs> call, Throwable t) {

            }
        });
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

    @Override
    public void fetchResponse(HelpRequest helpRequest) {
        helpRequestAdapter = new HelpRequestAdapter(ChatWithUsActivity.this, helpRequest.getData());
        helpRequestRecyclerView.setAdapter(helpRequestAdapter);

    }

    @Override
    public void fetchError(String error) {
        Toast.makeText(this, ""+ error, Toast.LENGTH_SHORT).show();
    }
}