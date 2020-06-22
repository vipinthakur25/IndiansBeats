package com.tetravalstartups.dingdong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.tetravalstartups.dingdong.utils.Constants;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(splashIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, Constants.SPLASH_DELAY);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        boolean animationStarted = false;
        if (!hasFocus || animationStarted) {
            return;
        }
        super.onWindowFocusChanged(hasFocus);
    }

}
