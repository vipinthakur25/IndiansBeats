package com.tetravalstartups.dingdong.modules.create;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.Preview;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.gesture.Gesture;
import com.otaliastudios.cameraview.gesture.GestureAction;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.create.sound.SoundActivity;
import com.tetravalstartups.dingdong.utils.Constants;

import java.io.File;
import java.io.IOException;


public class ScreenCamActivity extends AppCompatActivity implements View.OnClickListener  {

    private CameraView camera;
    private LinearLayout lvFlip;
    private LinearLayout lvFilters;
    private ImageView ivRecord;
    private TextView tvAddSound;

    private int mCurrentFilter = 0;
    private final Filters[] mAllFilters = Filters.values();

    public enum RECORDING_STATUS {
        RECORDING,
        STOPPED
    }

    RECORDING_STATUS status = RECORDING_STATUS.STOPPED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_cam);
        initView();
    }

    private void initView() {
        // camera init
        camera = findViewById(R.id.cameraView);
        initCameraView();

        // flip camera
        lvFlip = findViewById(R.id.lvFlip);
        lvFlip.setOnClickListener(this);

        // filter camera
        lvFilters = findViewById(R.id.lvFilters);
        lvFilters.setOnClickListener(this);

        // record video
        ivRecord = findViewById(R.id.ivRecord);
        ivRecord.setOnClickListener(this);

        // add sound
        tvAddSound = findViewById(R.id.tvAddSound);
        tvAddSound.setOnClickListener(this);

    }

    private void initCameraView(){
        camera.setLifecycleOwner(this);
        camera.setFacing(Facing.FRONT);
        camera.setMode(Mode.VIDEO);
        camera.setAudio(Audio.STEREO);
        camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        camera.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS);
        camera.setFilter(Filters.NONE.newInstance());
        camera.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                super.onVideoTaken(result);
                VideoPreviewActivity.setVideoResult(result);
                Intent intent = new Intent(ScreenCamActivity.this, VideoPreviewActivity.class);
                startActivity(intent);
            }

            @Override
            public void onVideoRecordingStart() {
                super.onVideoRecordingStart();
                message("Video recording started", false);
            }

            @Override
            public void onVideoRecordingEnd() {
                super.onVideoRecordingEnd();
                message("Video recording end", false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == lvFlip){
            flipCamera();
        }

        if (v == ivRecord){
            if (status == RECORDING_STATUS.STOPPED){
                ivRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_video_record_red));
                FileLoader.with(this)
                        .load(Constants.SOUND_0,false)
                        .fromDirectory("test4", FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                File loadedFile = response.getBody();
                                MediaPlayer mp = new MediaPlayer();
                                try {
                                    mp.setDataSource(loadedFile.getPath());
                                    mp.prepare();
                                    mp.start();
                                    captureVideoSnapshot(mp.getDuration());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //captureVideoSnapshot();
                            }

                            @Override
                            public void onError(FileLoadRequest request, Throwable t) {
                            }
                        });
                status = RECORDING_STATUS.RECORDING;
            } else
                if (status == RECORDING_STATUS.RECORDING){
                    ivRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_video_record_white));
                    camera.stopVideo();
                    status = RECORDING_STATUS.STOPPED;
                }
        }

        if (v == lvFilters){
            changeCurrentFilter();
        }

        if (v == tvAddSound){
            startActivity(new Intent(ScreenCamActivity.this, SoundActivity.class));
        }

    }


    private void captureVideoSnapshot(int duration) {
        if (camera.isTakingVideo()) {
            message("Already taking video.", false);
            return;
        }
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Video snapshots are only allowed with the GL_SURFACE preview.", true);
            return;
        }
        camera.takeVideoSnapshot(new File(getFilesDir(), "video.mp4"), duration);
    }

    private void message(@NonNull String content, boolean important) {
        if (important) {
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        }
    }

    private void changeCurrentFilter() {
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Filters are supported only when preview is Preview.GL_SURFACE.", true);
            return;
        }
        if (mCurrentFilter < mAllFilters.length - 1) {
            mCurrentFilter++;
        } else {
            mCurrentFilter = 0;
        }
        Filters filter = mAllFilters[mCurrentFilter];
        message(filter.toString(), false);
        camera.setFilter(filter.newInstance());
    }


    private void flipCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                message("Switched to back camera!", false);
                break;

            case FRONT:
                message("Switched to front camera!", false);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isOpened()) {
            camera.open();
        }
    }

}
