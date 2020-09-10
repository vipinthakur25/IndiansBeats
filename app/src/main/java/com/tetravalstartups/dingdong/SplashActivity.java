package com.tetravalstartups.dingdong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.CheckUserResponse;
import com.tetravalstartups.dingdong.auth.SetupProfileActivity;

import retrofit2.Call;
import retrofit2.Callback;

public class SplashActivity extends BaseActivity {

    private ImageView ivSplash;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private RequestInterface requestInterface;
    private static final String TAG = "SplashActivity";

    private FrameLayout frameSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setStatusBarTransparentFlag();
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {

        frameSplash = findViewById(R.id.frameSplash);
        ivSplash = findViewById(R.id.ivSplash);

        Glide.with(this).load(R.drawable.dd_logo_amin).into(ivSplash);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) {
                    String authPhone = firebaseAuth.getCurrentUser().getPhoneNumber();
                    authPhone = authPhone.replace("+91", "");
                    checkUser(authPhone);
                } else {
                    Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(splashIntent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, 2000);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        boolean animationStarted = false;
        if (!hasFocus || animationStarted) {
            return;
        }
        super.onWindowFocusChanged(hasFocus);
    }

    private void checkUser(String phone) {
        Call<CheckUserResponse> call = requestInterface.checkUser(phone);
        call.enqueue(new Callback<CheckUserResponse>() {
            @Override
            public void onResponse(Call<CheckUserResponse> call, retrofit2.Response<CheckUserResponse> response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (response.code() == 400){
                    Intent intent = new Intent(SplashActivity.this, SetupProfileActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (response.code() == 500){
                    Snackbar.make(frameSplash, "Something went wrong...", Snackbar.LENGTH_LONG).show();
                } else if (response.code() == 403) {
                    Snackbar.make(frameSplash, "Scheduled maintenance going on...", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
