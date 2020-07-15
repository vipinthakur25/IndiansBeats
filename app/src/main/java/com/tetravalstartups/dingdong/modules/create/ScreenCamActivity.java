package com.tetravalstartups.dingdong.modules.create;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.create.filters.CameraFilter;
import com.tetravalstartups.dingdong.modules.create.filters.CameraFilterAdapter;
import com.tetravalstartups.dingdong.modules.create.filters.CameraFilterBottomSheet;
import com.tetravalstartups.dingdong.modules.create.sound.SoundActivity;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.DDAlert;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ScreenCamActivity extends AppCompatActivity implements View.OnClickListener {

    private final Filters[] mAllFilters = Filters.values();
    FlashState flashState;
    MediaPlayer soundPlayer;
    RECORDING_STATUS recordingStatus = RECORDING_STATUS.STOPPED;
    VideoType videoType = VideoType.WITHOUT_SOUND;
    FilterStatus filterStatus = FilterStatus.HIDE;
    private CameraView camera;
    private LinearLayout lvFlip;
    private LinearLayout lvFilters;
    private ImageView ivRecord;
    private TextView tvAddSound;
    private LinearLayout lvGallery;
    private LinearLayout lvFlash;
    private FrameLayout frameSidePanel;
    private LinearLayout lhSound;
    private MediaPlayer mp;
    private SharedPreferences preferences;
    private ImageView ivFlip;
    private ImageView ivSoundSelected;
    private ImageView ivRemoveSound;
    private ImageView ivFlash;
    private Vibrator vibRecorder;
    private int mCurrentFilter = 0;
    private Editor editor;
    private CameraFilterBottomSheet cameraFilterBottomSheet;
    private String sound_path;
    private TextView tvVideoTimer;
    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;
    private int total_time = 5 * 60;
    private ProgressBar progressVideo;
    private AlphaAnimation alphaAnimation;
    private LinearLayout lvSpeed;
    private LinearLayout lvBeauty;
    private LinearLayout lvStickers;
    private LinearLayout lvTimer;
    private TextView tvSpeed05x;
    private TextView tvSpeed1x;
    private TextView tvSpeed2x;
    private TextView tvFilterName;
    private ImageView ivSpeed;
    private ImageView ivTimer;
    private FrameLayout frameSpeed;
    private FrameLayout frameTimer;
    private TextView tvTimer3;
    private TextView tvTimer10;
    private LinearLayout lhControl;
    private ImageView ivSendResult;
    private ImageView ivDeleteResult;
    private RecyclerView recyclerFilters;
    private List<CameraFilter> cameraFilterList;
    private CameraFilterAdapter cameraFilterAdapter;
    private LinearLayout lvFilterSheet;
    private TextView tvDiscard;
    private TextView tvNext;
    private DDAlert ddAlert;
    private VideoResult videoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_cam);

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {

            }
        });

        preferences = getSharedPreferences("selected_sound", 0);

        editor = preferences.edit();
        editor.clear();
        editor.apply();
        initView();
    }

    private void initView() {
        camera = findViewById(R.id.cameraView);
        initCameraView();
        soundPlayer = new MediaPlayer();

        lvFlip = findViewById(R.id.lvFlip);
        ivFlip = findViewById(R.id.ivFlip);
        lvFlip.setOnClickListener(this);

        vibRecorder = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        lvFilters = findViewById(R.id.lvFilters);
        lvSpeed = findViewById(R.id.lvSpeed);
        lvBeauty = findViewById(R.id.lvBeauty);
        lvStickers = findViewById(R.id.lvStickers);
        lvGallery = findViewById(R.id.lvGallery);
        lvTimer = findViewById(R.id.lvTimer);
        tvFilterName = findViewById(R.id.tvFilterName);

        tvDiscard = findViewById(R.id.tvDiscard);
        tvNext = findViewById(R.id.tvNext);

        tvDiscard.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        tvSpeed05x = findViewById(R.id.tvSpeed05x);
        tvSpeed1x = findViewById(R.id.tvSpeed1x);
        tvSpeed2x = findViewById(R.id.tvSpeed2x);

        tvSpeed05x.setOnClickListener(this);
        tvSpeed1x.setOnClickListener(this);
        tvSpeed2x.setOnClickListener(this);

        ivSpeed = findViewById(R.id.ivSpeed);
        ivTimer = findViewById(R.id.ivTimer);
        frameSpeed = findViewById(R.id.frameSpeed);
        frameTimer = findViewById(R.id.frameTimer);

        tvTimer3 = findViewById(R.id.tvTimer3);
        tvTimer10 = findViewById(R.id.tvTimer10);

        tvTimer3.setOnClickListener(this);
        tvTimer10.setOnClickListener(this);

        lvSpeed.setOnClickListener(this);
        lvBeauty.setOnClickListener(this);
        lvStickers.setOnClickListener(this);
        lvGallery.setOnClickListener(this);
        lvTimer.setOnClickListener(this);

        ivSoundSelected = findViewById(R.id.ivSoundSelected);
        ivRemoveSound = findViewById(R.id.ivRemoveSound);
        lvFlash = findViewById(R.id.lvFlash);
        ivFlash = findViewById(R.id.ivFlash);
        frameSidePanel = findViewById(R.id.frameSidePanel);
        lhSound = findViewById(R.id.lhSound);
        tvVideoTimer = findViewById(R.id.tvVideoTimer);
        progressVideo = findViewById(R.id.progressVideo);
        progressVideo.setMax(300);

        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setRepeatCount(300);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        lvFlash.setOnClickListener(this);
        ivRemoveSound.setOnClickListener(this);
        lvFilters.setOnClickListener(this);

        ivRecord = findViewById(R.id.ivRecord);
        ivRecord.setOnClickListener(this);

        tvAddSound = findViewById(R.id.tvAddSound);
        tvAddSound.setOnClickListener(this);

        lhControl = findViewById(R.id.lhControl);
        lvFilterSheet = findViewById(R.id.lvFilterSheet);

        cameraFilterBottomSheet = new CameraFilterBottomSheet();
        setSpeed1x();
        setTimer3();

        recyclerFilters = findViewById(R.id.recyclerFilters);
        setFilterData();

    }

    private void initCameraView() {
        camera.setLifecycleOwner(this);
        camera.setFacing(Facing.FRONT);
        camera.setMode(Mode.VIDEO);
        camera.setAudio(Audio.STEREO);
        camera.setFlash(Flash.OFF);
        flashState = FlashState.FLASH_OFF;
        camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        camera.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS);
        camera.mapGesture(Gesture.SCROLL_VERTICAL, GestureAction.EXPOSURE_CORRECTION);
        mp = new MediaPlayer();

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                super.onVideoTaken(result);
                lvGallery.setVisibility(View.GONE);
                lvStickers.setVisibility(View.GONE);
                ivRecord.setVisibility(View.GONE);
                lhControl.setVisibility(View.VISIBLE);
                videoResult = result;
            }

            @Override
            public void onVideoRecordingStart() {
                super.onVideoRecordingStart();
            }

            @Override
            public void onVideoRecordingEnd() {
                super.onVideoRecordingEnd();

            }
        });

    }

    private void setFilterData() {
        cameraFilterList = new ArrayList<>();
        recyclerFilters.setLayoutManager(new LinearLayoutManager(ScreenCamActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false));
        recyclerFilters.addItemDecoration(new EqualSpacingItemDecoration(16,
                EqualSpacingItemDecoration.HORIZONTAL));

        // add filter data
        cameraFilterList.add(new CameraFilter(0, "None", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(1, "Auto Fix", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(2, "B&W", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(3, "Bright", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(4, "Contrast", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(5, "Cross", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(6, "Document", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(7, "Duotone", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(8, "Fill", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(9, "Gamma", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(10, "Grain", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(11, "Greyscale", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(12, "Hue", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(13, "Invert", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(14, "Lomoish", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(15, "Posterize", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(16, "Saturation", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(17, "Sepia", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(18, "Sharp", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(19, "Temp", R.drawable.emma));
        cameraFilterList.add(new CameraFilter(20, "Vintage", R.drawable.emma));

        cameraFilterAdapter =
                new CameraFilterAdapter(ScreenCamActivity.this, cameraFilterList);

        cameraFilterAdapter.notifyDataSetChanged();

        recyclerFilters.setAdapter(cameraFilterAdapter);
    }

    private void runTimer() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time =
                        String.format(Locale.getDefault(),
                                "%02d:%02d", minutes, secs);
                tvVideoTimer.setText(time);

                if (running) {
                    seconds++;
                }

                progressVideo.setProgress(seconds);

                handler.postDelayed(this, 1000);

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == lvFlip) {
            YoYo.with(Techniques.FlipInY)
                    .duration(500)
                    .repeat(0)
                    .playOn(ivFlip);
            flipCamera();
        }

        if (v == ivRecord) {
            if (recordingStatus == RECORDING_STATUS.STOPPED) {
                try {
                    vibRecorder.vibrate(50);
                    hideViews();
                    running = true;
                    runTimer();
                    ivRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_video_record_red));
                    ivRecord.startAnimation(alphaAnimation);
                    startVideoRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                vibRecorder.vibrate(50);
                showViews();
                running = false;
                ivRecord.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_video_record_white));
                ivRecord.clearAnimation();
                stopVideoRecording();
            }

        }

        if (v == lvFilters) {
            flipFilters();
        }

        if (v == tvAddSound) {
            startActivity(new Intent(ScreenCamActivity.this, SoundActivity.class));
        }

        if (v == ivRemoveSound) {
            editor = preferences.edit();
            editor.clear();
            editor.apply();
            Bundle bundle = getIntent().getExtras();
            sound_path = "Add Sound";
            onResume();
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
//
//            if (speedStatus == SpeedStatus.OFF){
//                frameTimer.setVisibility(View.GONE);
//                frameSpeed.setVisibility(View.VISIBLE);
//                speedStatus = SpeedStatus.ON;
//            } else {
//                frameSpeed.setVisibility(View.GONE);
//                speedStatus = SpeedStatus.OFF;
//            }

            message("Coming Soon", false);

        }

        if (v == lvBeauty) {
            message("Coming Soon", false);
        }

        if (v == lvStickers) {
            message("Coming Soon", false);
        }

        if (v == lvGallery) {
            message("Coming Soon", false);
        }

        if (v == lvTimer) {

        }

        if (v == tvSpeed05x) {
            //setSpeed05x();
        }

        if (v == tvSpeed1x) {
            //setSpeed1x();
        }

        if (v == tvSpeed2x) {
            //setSpeed2x();
        }

        if (v == tvTimer3) {
            setTimer3();
        }

        if (v == tvTimer10) {
            setTimer10();
        }

        if (v == tvDiscard) {
            discardAndResetCamera();
        }

        if (v == tvNext) {
            goNextWithResult();
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
    //    message(filter.toString(), false);

        tvFilterName.setText(filter.toString());
        tvFilterName.setVisibility(View.VISIBLE);
        tvFilterName.setAlpha(1.0f);
        tvFilterName.animate()
                .alpha(0.0f).translationX(50);
        //tvFilterName.setVisibility(View.INVISIBLE);


        // Normal behavior:
        camera.setFilter(filter.newInstance());

    }

    private void goNextWithResult() {
        VideoPreviewActivity.setVideoResult(videoResult);
        Intent intent = new Intent(ScreenCamActivity.this, VideoPreviewActivity.class);
        intent.putExtra("sound_path", sound_path);
        intent.putExtra("filepath", Environment.getExternalStorageDirectory() + "dingdong/videos/video_trans.mp4");
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void discardAndResetCamera() {
        startActivity(new Intent(ScreenCamActivity.this, ScreenCamActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        lhControl.setVisibility(View.GONE);
//        lvStickers.setVisibility(View.VISIBLE);
//        lvGallery.setVisibility(View.VISIBLE);
//        ivRecord.setVisibility(View.VISIBLE);
//        seconds = 0;
//        running = false;
    }

    private void setTimer10() {
        tvTimer10.setBackgroundColor(getResources().getColor(R.color.text_shadow_white));
        tvTimer10.setTextColor(getResources().getColor(R.color.colorPrimary));

        ivTimer.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_timer_10));

        tvTimer3.setBackgroundColor(0);
        tvTimer3.setTextColor(getResources().getColor(R.color.colorDisable));
    }

    private void setTimer3() {
        tvTimer3.setBackgroundColor(getResources().getColor(R.color.text_shadow_white));
        tvTimer3.setTextColor(getResources().getColor(R.color.colorPrimary));

        ivTimer.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_timer));

        tvTimer10.setBackgroundColor(0);
        tvTimer10.setTextColor(getResources().getColor(R.color.colorDisable));
    }

    private void setSpeed2x() {
        tvSpeed2x.setBackgroundColor(getResources().getColor(R.color.text_shadow_white));
        tvSpeed2x.setTextColor(getResources().getColor(R.color.colorPrimary));

        ivSpeed.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_speed_on));

        tvSpeed1x.setBackgroundColor(0);
        tvSpeed1x.setTextColor(getResources().getColor(R.color.colorDisable));
        tvSpeed05x.setBackgroundColor(0);
        tvSpeed05x.setTextColor(getResources().getColor(R.color.colorDisable));
    }

    private void setSpeed1x() {
        tvSpeed1x.setBackgroundColor(getResources().getColor(R.color.text_shadow_white));
        tvSpeed1x.setTextColor(getResources().getColor(R.color.colorPrimary));

        ivSpeed.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_speed_off));

        tvSpeed2x.setBackgroundColor(0);
        tvSpeed2x.setTextColor(getResources().getColor(R.color.colorDisable));
        tvSpeed05x.setBackgroundColor(0);
        tvSpeed05x.setTextColor(getResources().getColor(R.color.colorDisable));
    }

    private void setSpeed05x() {
        tvSpeed05x.setBackgroundColor(getResources().getColor(R.color.text_shadow_white));
        tvSpeed05x.setTextColor(getResources().getColor(R.color.colorPrimary));

        ivSpeed.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_speed_on));

        tvSpeed2x.setBackgroundColor(0);
        tvSpeed2x.setTextColor(getResources().getColor(R.color.colorDisable));
        tvSpeed1x.setBackgroundColor(0);
        tvSpeed1x.setTextColor(getResources().getColor(R.color.colorDisable));
    }

    private void showViews() {
        frameSidePanel.animate()
                .translationX(0)
                .alpha(1.0f);

        lvGallery.animate()
                .translationY(0)
                .alpha(1.0f);

        lvStickers.animate()
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

        lvStickers.animate()
                .translationY(50)
                .alpha(0.0f);

        lhSound.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void checkForVideoType() {
        if (sound_path.equals("NO_SOUND")) {
            videoType = VideoType.WITHOUT_SOUND;
            Toast.makeText(this, "no sound", Toast.LENGTH_SHORT).show();
        } else {
            videoType = VideoType.WITH_SOUND;
            Toast.makeText(this, "sound", Toast.LENGTH_SHORT).show();
        }
    }

    private void startVideoRecording() throws IOException {
        if (videoType == VideoType.WITHOUT_SOUND) {
            if (recordingStatus == RECORDING_STATUS.STOPPED) {
                recordVideoWithoutSound();
            }
        } else if (videoType == VideoType.WITH_SOUND) {
            soundPlayer.setDataSource(sound_path);
            soundPlayer.prepare();
            recordVideoWithSound(soundPlayer.getDuration());
        }
    }

    private void recordVideoWithoutSound() {
        if (camera.isTakingVideo()) {
            message("Already taking video.", false);
            return;
        }

        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Video snapshots are only allowed with the GL_SURFACE preview.", true);
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
            recordingStatus = RECORDING_STATUS.RECORDING;
            camera.takeVideoSnapshot(new File(Environment.getExternalStorageDirectory(),
                            "dingdong/videos/video.mp4"),
                    Video.MAX_VIDEO_DURATION);
        } else {
            message(getResources().getString(R.string.folder_creation_problem), false);
        }

    }

    private void recordVideoWithSound(int sound_duration) {
        if (camera.isTakingVideo()) {
            message("Already taking video.", false);
            return;
        }

        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Video snapshots are only allowed with the GL_SURFACE preview.", true);
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
            recordingStatus = RECORDING_STATUS.RECORDING;
            soundPlayer.start();
            camera.setAudio(Audio.OFF);
            camera.takeVideoSnapshot(new File(Environment.getExternalStorageDirectory(),
                            "dingdong/videos/video.mp4"),
                    sound_duration);
        } else {
            message(getResources().getString(R.string.folder_creation_problem), false);
        }
    }

    private void stopVideoRecording() {
        camera.stopVideo();
        soundPlayer.stop();
        soundPlayer.reset();
        recordingStatus = RECORDING_STATUS.STOPPED;
    }

    private void captureVideoSnapshot() {
        if (camera.isTakingVideo()) {
            message("Already taking video.", false);
            return;
        }

        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Video snapshots are only allowed with the GL_SURFACE preview.", true);
            return;
        }

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "dingdong/videos");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            camera.takeVideoSnapshot(new File(Environment.getExternalStorageDirectory(), "dingdong/videos/video.mp4"), 44542);
        } else {
            message("Something went wrong...", false);
        }
    }

    private void message(@NonNull String content, boolean important) {
        if (important) {
            Toast.makeText(this, content, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        }
    }

    private void changeCurrentFilter(int id) {
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Filters are supported only when preview is Preview.GL_SURFACE.", true);
            return;
        }

        Filters filter = mAllFilters[id];
        message(filter.toString(), false);
        camera.setFilter(filter.newInstance());
    }

    private void flipCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                lvFlash.setVisibility(View.VISIBLE);
                break;

            case FRONT:
                lvFlash.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();
//        preferences = getSharedPreferences("selected_sound", 0);
//        tvAddSound.setText(preferences.getString("sound_name", "Add Sound"));
        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            tvAddSound.setSelected(false);
            ivSoundSelected.setImageDrawable(getResources().getDrawable(R.drawable.dd_sound));
            ivRemoveSound.setVisibility(View.GONE);
            sound_path = "NO_SOUND";
        } else {
            tvAddSound.setSelected(true);
            ivSoundSelected.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_sound_selected));
            tvAddSound.setText(bundle.getString("sound_title"));
            sound_path = bundle.getString("sound_path");
            ivRemoveSound.setVisibility(View.VISIBLE);
        }
//

//
//        if (!preferences.getString("sound_name", "Add Sound").equals("Add Sound")) {
//            tvAddSound.setSelected(true);
//            ivSoundSelected.setImageDrawable(getResources().getDrawable(R.drawable.dd_record_sound_selected));
//            ivRemoveSound.setVisibility(View.VISIBLE);
//            sound_path = preferences.getString("sound_path", "NO_SOUND");
//        } else {
//            tvAddSound.setSelected(false);
//            ivSoundSelected.setImageDrawable(getResources().getDrawable(R.drawable.dd_sound));
//            ivRemoveSound.setVisibility(View.GONE);
//        }
        checkForVideoType();
    }

    public enum SpeedStatus {
        ON,
        OFF
    }

    public enum TimerStatus {
        ON,
        OFF
    }

    public enum FilterStatus {
        HIDE,
        SHOW
    }

    public enum RECORDING_STATUS {
        RECORDING,
        STOPPED
    }
}
