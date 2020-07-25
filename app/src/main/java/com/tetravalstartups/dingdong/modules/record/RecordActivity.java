package com.tetravalstartups.dingdong.modules.record;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.Preview;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.gesture.Gesture;
import com.otaliastudios.cameraview.gesture.GestureAction;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.engine.TrackType;
import com.otaliastudios.transcoder.sink.DataSink;
import com.otaliastudios.transcoder.sink.DefaultDataSink;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.create.FlashState;
import com.tetravalstartups.dingdong.modules.create.sound.SoundActivity;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.DDLoadingProgress;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Future;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, TranscoderListener {

    private static final int FILE_REQUEST_CODE = 1;
    private static final int CROP_REQUEST = 2;
    private static final int PROGRESS_BAR_MAX = 100;
    private int mCurrentFilter = 0;
    private final Filters[] mAllFilters = Filters.values();
    private VideoResult videoResult;
    private Future<Void> mTranscodeFuture;
    private SharedPreferences preferences;
    private AlphaAnimation alphaAnimation;
    private DDLoading ddLoading;
    private DDLoadingProgress ddLoadingProgress;
    File mTranscodeOutputFile = null;

    private FrameLayout frameSidePanel;

    private MediaPlayer soundPlayer;
    private CameraView camera;

    private ImageView ivRecord;
    private ImageView ivSoundSelected;
    private ImageView ivRemoveSound;
    private ImageView ivFlip;
    private ImageView ivFlash;

    private LinearLayout lvGallery;
    private LinearLayout lvFilters;
    private LinearLayout lhSound;
    private LinearLayout lvSpeed;
    private LinearLayout lvBeauty;
    private LinearLayout lvTimer;
    private LinearLayout lhRecordControl;
    private LinearLayout lhDiscardNext;
    private LinearLayout lvFlip;
    private LinearLayout lvFlash;

    private TextView tvAddSound;
    private TextView tvFilterName;
    private TextView tvDiscard;
    private TextView tvNext;

    private enum RECORDER_STATUS {
        STOPPED,
        RECORDING
    }

    private enum SOUND_STATUS {
        WITHOUT_SOUND,
        WITH_SOUND
    }

    private String sound_path;

    private RECORDER_STATUS recorderStatus;
    private SOUND_STATUS soundStatus;
    private FlashState flashState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO};
        Permissions.check(this/*context*/, permissions,
                null/*rationale*/, null/*options*/,
                new PermissionHandler() {
            @Override
            public void onGranted() {

            }
        });

        initView();
    }

    private void initView() {
        camera = findViewById(R.id.cameraRecord);

        ivRecord = findViewById(R.id.ivRecord);
        ivRecord.setOnClickListener(this);

        lvFilters = findViewById(R.id.lvFilters);
        lvFilters.setOnClickListener(this);

        lvGallery = findViewById(R.id.lvGallery);
        lvGallery.setOnClickListener(this);

        lvSpeed = findViewById(R.id.lvSpeed);
        lvSpeed.setOnClickListener(this);

        lvBeauty = findViewById(R.id.lvBeauty);
        lvBeauty.setOnClickListener(this);

        lvTimer = findViewById(R.id.lvTimer);
        lvTimer.setOnClickListener(this);

        lhSound = findViewById(R.id.lhSound);
        lhSound.setOnClickListener(this);

        lvFlip = findViewById(R.id.lvFlip);
        ivFlip = findViewById(R.id.ivFlip);
        lvFlip.setOnClickListener(this);

        lvFlash = findViewById(R.id.lvFlash);
        ivFlash = findViewById(R.id.ivFlash);
        lvFlash.setOnClickListener(this);

        frameSidePanel = findViewById(R.id.frameSidePanel);

        lhRecordControl = findViewById(R.id.lhRecordControl);

        lhDiscardNext = findViewById(R.id.lhDiscardNext);

        tvAddSound = findViewById(R.id.tvAddSound);

        tvFilterName = findViewById(R.id.tvFilterName);

        ivSoundSelected = findViewById(R.id.ivSoundSelected);

        tvDiscard = findViewById(R.id.tvDiscard);
        tvDiscard.setOnClickListener(this);

        tvNext = findViewById(R.id.tvNext);
        tvNext.setOnClickListener(this);

        ivRemoveSound = findViewById(R.id.ivRemoveSound);
        ivRemoveSound.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        preferences = getSharedPreferences("selected_sound", 0);

        if (bundle != null){
            soundStatus = SOUND_STATUS.WITH_SOUND;
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            soundStatus = SOUND_STATUS.WITHOUT_SOUND;
        }

        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setRepeatCount(300);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        ddLoading = DDLoading.getInstance();
        ddLoadingProgress = DDLoadingProgress.getInstance();

        initCamera();

    }

    @Override
    public void onClick(View v) {
        if (v == ivRecord){
            if (recorderStatus == RECORDER_STATUS.STOPPED){
                startRecording();
                recorderStatus = RECORDER_STATUS.RECORDING;
            } else {
                stopRecording();
                recorderStatus = RECORDER_STATUS.STOPPED;
            }
        }

        if (v == lvFilters) {
            flipFilters();
        }

        if (v == lvGallery) {
            Intent intent = new Intent(this, FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission(true)
                    .setShowImages(false)
                    .setShowAudios(false)
                    .setShowFiles(false)
                    .setShowVideos(true)
                    .setSkipZeroSizeFiles(true)
                    .setMaxSelection(1)
                    .build());
            startActivityForResult(intent, FILE_REQUEST_CODE);
        }

        if (v == lhSound) {
            startActivity(new Intent(RecordActivity.this, SoundActivity.class));
        }

        if (v == ivRemoveSound) {
            removeSound();
        }

        if (v == tvDiscard) {
            discardAndResetCamera();
        }

        if (v == tvNext){
            goNextWithResult();
        }

        if (v == lvFlip) {
            YoYo.with(Techniques.FlipInY)
                    .duration(500)
                    .repeat(0)
                    .playOn(ivFlip);
            flipCamera();
        }

        if (v == lvFlash) {
            if (flashState == FlashState.FLASH_OFF) {
                ivFlash.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_flash_on));
                camera.setFlash(Flash.TORCH);
                flashState = FlashState.FLASH_ON;
            } else {
                ivFlash.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_flash_off));
                camera.setFlash(Flash.OFF);
                flashState = FlashState.FLASH_OFF;
            }
        }

        if (v == lvSpeed) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if (v == lvBeauty) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if (v == lvTimer) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE){
            if (data != null){
                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                Intent intent = new Intent(RecordActivity.this, PreviewActivity.class);
                intent.putExtra("video_path", files.get(0).getPath());
                intent.putExtra("sound_title", "Original");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }

    }

    private void initCamera() {
        camera.setLifecycleOwner(this);
        camera.setFacing(Facing.FRONT);
        camera.setMode(Mode.VIDEO);
        camera.setAudio(Audio.STEREO);
        camera.setFlash(Flash.OFF);
        flashState = FlashState.FLASH_OFF;
        camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        camera.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS);
        camera.mapGesture(Gesture.SCROLL_VERTICAL, GestureAction.EXPOSURE_CORRECTION);
        soundPlayer = new MediaPlayer();

        recorderStatus = RECORDER_STATUS.STOPPED;

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                super.onVideoTaken(result);
                prepareRecordingTaken(result);
            }

            @Override
            public void onZoomChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
                super.onZoomChanged(newValue, bounds, fingers);
            }

            @Override
            public void onExposureCorrectionChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
                super.onExposureCorrectionChanged(newValue, bounds, fingers);
            }

            @Override
            public void onVideoRecordingStart() {
                super.onVideoRecordingStart();
                prepareRecordingStart();
            }

            @Override
            public void onVideoRecordingEnd() {
                super.onVideoRecordingEnd();
                prepareRecordingStop();
            }
        });
    }

    private void discardAndResetCamera() {
        startActivity(new Intent(RecordActivity.this, RecordActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void goNextWithResult() {
        if (soundStatus == SOUND_STATUS.WITHOUT_SOUND){
            Intent intent = new Intent(RecordActivity.this, PreviewActivity.class);
            intent.putExtra("video_path", videoResult.getFile().getPath());
            if (soundStatus == SOUND_STATUS.WITH_SOUND){
                intent.putExtra("sound_title", preferences.getString("sound_name", "Add Sound"));
            } else {
                intent.putExtra("sound_title", "Original");
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            ddLoadingProgress.showProgress(RecordActivity.this, "Loading...", false);
            transcodeVideoWithSound();
        }
    }

    private void removeSound() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        onResume();
    }

    private void startRecording() {
        ivRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_video_record_red));
        ivRecord.startAnimation(alphaAnimation);
        if (soundStatus == SOUND_STATUS.WITHOUT_SOUND){
            recordWithoutSound();
        } else {
            recordWithSound();
        }
    }

    private void recordWithSound() {
        if (camera.isTakingVideo()) {
            Toast.makeText(this, "Already taking video.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (camera.getPreview() != Preview.GL_SURFACE) {
            Toast.makeText(this, "Video snapshots are only allowed with the GL_SURFACE preview.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        File folder = new File(
                Environment.getExternalStorageDirectory()
                        + File.separator + "dingdong/videos");

        boolean success = true;

        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        if (!sound_path.equals("no_sound")){
            try {
                soundPlayer.setDataSource(sound_path);
                soundPlayer.prepare();
                if (success) {
                    camera.setAudio(Audio.OFF);
                    camera.takeVideoSnapshot(new File(Environment.getExternalStorageDirectory(),
                                    "dingdong/videos/video.mp4"),
                            soundPlayer.getDuration());
                } else {
                    Toast.makeText(this, ""+getResources().getString(R.string.folder_creation_problem),
                            Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void recordWithoutSound() {
        if (camera.isTakingVideo()) {
            Toast.makeText(this, "Already taking video.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (camera.getPreview() != Preview.GL_SURFACE) {
            Toast.makeText(this, "Video snapshots are only allowed with the GL_SURFACE preview.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        File folder = new File(
                Environment.getExternalStorageDirectory()
                        + File.separator + "dingdong/videos");

        boolean success = true;

        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            camera.takeVideoSnapshot(new File(Environment.getExternalStorageDirectory(),
                            "dingdong/videos/video.mp4"),
                    Video.MAX_VIDEO_DURATION);
        } else {
            Toast.makeText(this, ""+getResources().getString(R.string.folder_creation_problem),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        camera.stopVideo();
    }

    private void prepareRecordingTaken(VideoResult result) {
        lhRecordControl.setVisibility(View.GONE);
        lhDiscardNext.setVisibility(View.VISIBLE);
        videoResult = result;
    }

    private void prepareRecordingStart() {
        if (soundStatus == SOUND_STATUS.WITH_SOUND){
            soundPlayer.start();
        }
        hideViews();
    }

    private void prepareRecordingStop() {
        ivRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_video_record_white));
        ivRecord.clearAnimation();
        soundPlayer.pause();
        soundPlayer.reset();
        showViews();
    }

    private void showViews() {
        frameSidePanel.animate()
                .translationX(0)
                .alpha(1.0f);

        lvGallery.animate()
                .translationY(0)
                .alpha(1.0f);

        lvFilters.animate()
                .translationY(0)
                .alpha(1.0f);

        lhSound.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        frameSidePanel.animate()
                .translationX(50)
                .alpha(0.0f);

        lvGallery.animate()
                .translationY(50)
                .alpha(0.0f);

        lvFilters.animate()
                .translationY(50)
                .alpha(0.0f);

        lhSound.setVisibility(View.GONE);
    }

    private void flipCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                lvFlash.setVisibility(View.VISIBLE);
                flashState = FlashState.FLASH_OFF;
                ivFlash.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_flash_off));
                break;

            case FRONT:
                lvFlash.setVisibility(View.GONE);
                flashState = FlashState.FLASH_ON;
                ivFlash.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_flash_on));
                break;
        }
    }

    private void flipFilters() {
        if (camera.getPreview() != Preview.GL_SURFACE) {
            return;
        }

        if (mCurrentFilter < mAllFilters.length - 1) {
            mCurrentFilter++;
        } else {
            mCurrentFilter = 0;
        }
        Filters filter = mAllFilters[mCurrentFilter];

        tvFilterName.setText(filter.toString());
        tvFilterName.setVisibility(View.VISIBLE);
        tvFilterName.setAlpha(1.0f);
        camera.setFilter(filter.newInstance());
        tvFilterName.animate()
                .alpha(0.0f);
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



    @Override
    protected void onResume() {
        super.onResume();
        preferences = getSharedPreferences("selected_sound", 0);
        tvAddSound.setText(preferences.getString("sound_name", "Add Sound"));
        if (preferences.getString("sound_name", "Add Sound").equals("Add Sound")){
            soundStatus = SOUND_STATUS.WITHOUT_SOUND;
            tvAddSound.setSelected(false);
            ivSoundSelected.setImageDrawable(getResources().getDrawable(R.drawable.dd_sound));
            ivRemoveSound.setVisibility(View.GONE);
            sound_path = "no_sound";
        } else {
            soundStatus = SOUND_STATUS.WITH_SOUND;
            tvAddSound.setSelected(true);
            ivSoundSelected.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_sound_selected));
            ivRemoveSound.setVisibility(View.VISIBLE);
            sound_path = preferences.getString("sound_path", "no_sound");
        }
    }

    private void transcodeVideoWithSound() {
        DataSink sink;
        File transFile = new File(Environment.getExternalStorageDirectory() + "/dingdong/transcode/");
        transFile.mkdirs();

        try {
            mTranscodeOutputFile = File.createTempFile("video", ".mp4", transFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sink = new DefaultDataSink(mTranscodeOutputFile.getAbsolutePath());
        TranscoderOptions.Builder builder = Transcoder.into(sink);
        builder.addDataSource(TrackType.AUDIO, sound_path);
        builder.addDataSource(TrackType.VIDEO, videoResult.getFile().getPath());
        mTranscodeFuture = builder.setListener(this).transcode();
    }

    @Override
    public void onTranscodeProgress(double progress) {
        int percent = (int) ((progress / 1) * 100);
        ddLoadingProgress.updateProgress(percent);
    }

    @Override
    public void onTranscodeCompleted(int successCode) {
        ddLoading.hideProgress();
        Intent intent = new Intent(RecordActivity.this, PreviewActivity.class);
        intent.putExtra("video_path", mTranscodeOutputFile.getPath());
        if (soundStatus == SOUND_STATUS.WITH_SOUND){
            intent.putExtra("sound_title", preferences.getString("sound_name", "Add Sound"));
        } else {
            intent.putExtra("sound_title", "Original");
        }

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onTranscodeCanceled() {
        ddLoading.hideProgress();
    }

    @Override
    public void onTranscodeFailed(@NonNull Throwable exception) {
        ddLoading.hideProgress();
    }

}