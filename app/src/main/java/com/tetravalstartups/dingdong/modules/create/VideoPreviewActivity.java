package com.tetravalstartups.dingdong.modules.create;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.engine.TrackType;
import com.otaliastudios.transcoder.sink.DataSink;
import com.otaliastudios.transcoder.sink.DefaultDataSink;
import com.otaliastudios.transcoder.source.DataSource;
import com.otaliastudios.transcoder.source.UriDataSource;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.DDProgress;
import com.transloadit.sdk.async.AssemblyProgressListener;
import com.transloadit.sdk.response.AssemblyResponse;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.Future;

public class VideoPreviewActivity extends AppCompatActivity implements View.OnClickListener, AssemblyProgressListener, TranscoderListener {

    private VideoView videoView;
    private static VideoResult videoResult;
    MediaPlayer soundPlayer;
    FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;
    private String sound_path;
    private TextView tvUpload;
    private Master master;

    private VideoType videoType;

    private DDLoading ddLoading;

    private RecordingSpeed recordingSpeed = RecordingSpeed.ONE;

    private float speed_value;

    private Future<Void> mTranscodeFuture;

    public static void setVideoResult(@Nullable VideoResult result) {
        videoResult = result;
    }

    File mTranscodeOutputFile;

    private DDProgress ddProgress;

    private TextView tvNext;
    VideoResult result = videoResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_preview);
        initView();

    }

    private void initView() {
        if (result == null) {
            finish();
            return;
        }

        preferences = getSharedPreferences("selected_sound", 0);

        firebaseAuth = FirebaseAuth.getInstance();

        master = new Master(VideoPreviewActivity.this);

        videoView = findViewById(R.id.videoView);

        videoView.setOnClickListener(this);

        soundPlayer = new MediaPlayer();

        ddLoading = DDLoading.getInstance();

        ddProgress = DDProgress.getInstance();

        tvNext = findViewById(R.id.tvNext);
        tvNext.setOnClickListener(this);

//        tvUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {


//        initVideo(result);

        try {
            //ddLoading.showProgress(VideoPreviewActivity.this, "Processing Video...", false);
            initVideoPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVideoPlayer() throws IOException {
        DataSink sink;
        File f = new File(Environment.getExternalStorageDirectory() + "/dingdong/transcode/");

            f.mkdirs();
            mTranscodeOutputFile = File.createTempFile("video", ".mp4", f);
            sink = new DefaultDataSink(mTranscodeOutputFile.getAbsolutePath());

        TranscoderOptions.Builder builder = Transcoder.into(sink);

        if (!getIntent().getStringExtra("sound_path").equals("NO _SOUND")){
            builder.addDataSource(TrackType.AUDIO, getIntent().getStringExtra("sound_path"));
            builder.addDataSource(TrackType.VIDEO, result.getFile().getPath());
            mTranscodeFuture = builder.setListener(this).transcode();
            SharedPreferences preferences = getSharedPreferences("trans_path", 0);
            Editor editor = preferences.edit();
            editor.putString("video_path", mTranscodeOutputFile.getPath());
            editor.apply();
        } else {
            playTranscodeResult(result.getFile());
            SharedPreferences preferences = getSharedPreferences("trans_path", 0);
            Editor editor = preferences.edit();
            editor.putString("video_path", result.getFile().getPath());
            editor.apply();
        }

    }

    private void playTranscodeResult(File mTranscodeOutputFile){

        videoView.setVideoURI(Uri.parse(mTranscodeOutputFile.getAbsolutePath()));
        videoView.setOnPreparedListener(mp -> {
            ViewGroup.LayoutParams lp = videoView.getLayoutParams();
            float videoWidth = mp.getVideoWidth();
            float videoHeight = mp.getVideoHeight();
            float viewWidth = videoView.getWidth();
            lp.height = (int) (viewWidth * (videoHeight / videoWidth));
            videoView.setLayoutParams(lp);

//            if (sound_path == null){
//                playVideoWithoutSound();
//
//            } else {
//                playVideoWithSound();
//            }

            playVideoWithoutSound();

            if (result.isSnapshot()) {
                Log.e("VideoPreview", "The video full size is " + videoWidth + "x" + videoHeight);
            }
        });

    }

//    private void playVideoWithSound() {
//        try {
//            soundPlayer.setDataSource(sound_path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            soundPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (!videoView.isPlaying()) {
//            soundPlayer.start();
//            videoView.start();
//            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    soundPlayer.start();
//                    mp.start();
//                }
//            });
//        }
//    }

    private void playVideoWithoutSound() {
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

    @Override
    public void onClick(View v) {
        if (v == tvNext){
            //Intent intent = new Intent();
            startActivity(new Intent(VideoPreviewActivity.this, PostActivity.class));
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
        soundPlayer.reset();
    }

    @Override
    public void onUploadFinished() {

    }

    @Override
    public void onUploadPogress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onAssemblyFinished(AssemblyResponse response) {

    }

    @Override
    public void onUploadFailed(Exception exception) {

    }

    @Override
    public void onAssemblyStatusUpdateFailed(Exception exception) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = getSharedPreferences("selected_sound", 0);
        sound_path = preferences.getString("sound_path", null);
        try {
            initVideoPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTranscodeProgress(double progress) {
        double percent = (progress / 1) * 100;
        Log.e("trans_percent", percent+"");
        //ddProgress.updateProgress("Processing Video", (int) percent);
    }

    @Override
    public void onTranscodeCompleted(int successCode) {
        Toast.makeText(this, "Task Done", Toast.LENGTH_SHORT).show();
        if (successCode == Transcoder.SUCCESS_TRANSCODED){
            playTranscodeResult(mTranscodeOutputFile);
        }
    }

    @Override
    public void onTranscodeCanceled() {

    }

    @Override
    public void onTranscodeFailed(@NonNull Throwable exception) {

    }
}
