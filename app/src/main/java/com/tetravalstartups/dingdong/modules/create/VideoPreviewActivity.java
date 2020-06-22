package com.tetravalstartups.dingdong.modules.create;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.otaliastudios.cameraview.VideoResult;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.Constants;

import java.io.File;
import java.io.IOException;

public class VideoPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;
    private static VideoResult videoResult;
    MediaPlayer soundPlayer;
    SeekBar seekBarSound;
    SeekBar seekBarRecording;
    AudioManager audioManager;
    public static void setVideoResult(@Nullable VideoResult result) {
        videoResult = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        initView();

    }

    private void initView() {
        final VideoResult result = videoResult;
        if (result == null) {
            finish();
            return;
        }

        seekBarSound = findViewById(R.id.seekBarSound);
        seekBarRecording = findViewById(R.id.seekBarRecording);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        seekBarSound.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarSound.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBarRecording.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        seekBarRecording.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        videoView = findViewById(R.id.videoView);
        videoView.setOnClickListener(this);
        initVideo(result);
    }

    private void initVideo(VideoResult result) {
        soundPlayer = new MediaPlayer();
        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.fromFile(result.getFile()));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ViewGroup.LayoutParams lp = videoView.getLayoutParams();
                float videoWidth = mp.getVideoWidth();
                float videoHeight = mp.getVideoHeight();
                float viewWidth = videoView.getWidth();
                lp.height = (int) (viewWidth * (videoHeight / videoWidth));
                videoView.setLayoutParams(lp);
                FileLoader.with(VideoPreviewActivity.this)
                        .load(Constants.SOUND_0,false)
                        .fromDirectory("test4", FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                File loadedFile = response.getBody();

                                try {
                                    soundPlayer.setDataSource(loadedFile.getPath());
                                    soundPlayer.prepare();
                                    soundPlayer.start();
                                    playVideo();


                                    seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                            soundPlayer.setVolume((float) progress, (float) progress);
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });

                                    seekBarRecording.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                            mp.setVolume((float) progress, (float) progress);
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });



                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //captureVideoSnapshot();
                            }

                            @Override
                            public void onError(FileLoadRequest request, Throwable t) {
                            }
                        });
                playVideo();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == videoView){
            playVideo();
        }
    }

    void playVideo() {
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            setVideoResult(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        soundPlayer.stop();
        soundPlayer.release();
    }
}
