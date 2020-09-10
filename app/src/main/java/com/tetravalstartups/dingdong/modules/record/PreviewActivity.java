package com.tetravalstartups.dingdong.modules.record;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.publish.PostActivity;

import java.io.File;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private String video_path;
    private String sound_title;
    private String video_index;

    private VideoView videoView;
    private TextView tvGoBack;
    private TextView tvNext;

    private LinearLayout lhSound;
    private ImageView ivSoundSelected;
    private ImageView ivRemoveSound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        videoView = findViewById(R.id.videoView);

        tvGoBack = findViewById(R.id.tvGoBack);
        tvGoBack.setOnClickListener(this);

        tvNext = findViewById(R.id.tvNext);
        tvNext.setOnClickListener(this);

        lhSound = findViewById(R.id.lhSound);
        lhSound.setOnClickListener(this);

        ivSoundSelected = findViewById(R.id.ivSoundSelected);

        ivRemoveSound = findViewById(R.id.ivRemoveSound);
        ivRemoveSound.setOnClickListener(this);

        video_path = getIntent().getStringExtra("video_path");
        sound_title = getIntent().getStringExtra("sound_title");
        video_index = getIntent().getStringExtra("video_index");

        playVideo();
    }

    private void playVideo() {
        videoView.setVideoURI(Uri.fromFile(new File(video_path)));
        videoView.setOnPreparedListener(mp -> {
            ViewGroup.LayoutParams lp = videoView.getLayoutParams();
            float videoWidth = mp.getVideoWidth();
            float videoHeight = mp.getVideoHeight();
            float viewWidth = videoView.getWidth();
            lp.height = (int) (viewWidth * (videoHeight / videoWidth));
            videoView.setLayoutParams(lp);
            if (!videoView.isPlaying()) {
                videoView.start();
                videoView.setOnCompletionListener(mp1 -> mp1.start());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == tvGoBack) {
            onBackPressed();
            finish();
        }

        if (v == tvNext){
            Intent intent = new Intent(PreviewActivity.this, PostActivity.class);
            intent.putExtra("video_path", video_path);
            intent.putExtra("sound_title", sound_title);
            intent.putExtra("video_index", video_index);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }
}
