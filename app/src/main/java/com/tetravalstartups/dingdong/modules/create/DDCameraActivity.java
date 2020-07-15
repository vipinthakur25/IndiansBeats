package com.tetravalstartups.dingdong.modules.create;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Mode;
import com.tetravalstartups.dingdong.R;

public class DDCameraActivity extends AppCompatActivity {

    private CameraView camera;
    private FlashState flashState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dd_camera);
        initView();
    }

    private void initView() {
        camera = findViewById(R.id.cameraView);
        initCamera();
    }

    private void initCamera() {
        camera.setLifecycleOwner(this);

        // front camera
        camera.setFacing(Facing.FRONT);

        // video mode
        camera.setMode(Mode.VIDEO);

        // audio mode
        camera.setAudio(Audio.STEREO);

        // flash mode
        camera.setFlash(Flash.OFF);
        flashState = FlashState.FLASH_OFF;

    }
}
