package com.tetravalstartups.dingdong.modules.record;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.engine.TrackType;
import com.otaliastudios.transcoder.sink.DataSink;
import com.otaliastudios.transcoder.sink.DefaultDataSink;
import com.otaliastudios.transcoder.strategy.DefaultAudioStrategy;
import com.otaliastudios.transcoder.strategy.RemoveTrackStrategy;
import com.otaliastudios.transcoder.strategy.TrackStrategy;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.create.PostActivity;
import com.tetravalstartups.dingdong.modules.create.sound.SoundActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private String video_path;
    private String sound_title;

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

        playVideo();
    }

    private void playVideo() {
        videoView.setVideoPath(video_path);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ViewGroup.LayoutParams lp = videoView.getLayoutParams();
                float videoWidth = mp.getVideoWidth();
                float videoHeight = mp.getVideoHeight();
                float viewWidth = videoView.getWidth();
                lp.height = (int) (viewWidth * (videoHeight / videoWidth));
                videoView.setLayoutParams(lp);
                if (!videoView.isPlaying()) {
                    videoView.start();
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                }
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
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lhSound) {
            startActivity(new Intent(PreviewActivity.this, SoundActivity.class));
        }

    }
}
