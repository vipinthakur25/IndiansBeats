package com.tetravalstartups.dingdong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.tetravalstartups.dingdong.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {

        videoSplash = findViewById(R.id.videoSplash);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dd_video);
        videoSplash.setVideoURI(video);

        videoSplash.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(splashIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        videoSplash.start();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, Constants.SPLASH_DELAY);
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
