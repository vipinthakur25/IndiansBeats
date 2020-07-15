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
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tetravalstartups.dingdong.utils.Constants;

public class SplashActivity extends BaseActivity {

    private VideoView videoSplash;
    private ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setStatusBarTransparentFlag();
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {

//        videoSplash = findViewById(R.id.videoSplash);
        ivSplash = findViewById(R.id.ivSplash);
        Glide.with(this).load(R.drawable.dd_gif_splash).into(ivSplash);
        //Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dd_video);
        //videoSplash.setVideoURI(video);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(splashIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

//                YoYo.with(Techniques.Bounce)
//                        .duration(800)
//                .repeat(4)
//                .playOn(videoSplash);
            }
        }, 2000);

//        videoSplash.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            public void onCompletion(MediaPlayer mp) {
//                Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(splashIntent);
//                finish();
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
//        });
//
//        videoSplash.start();
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
