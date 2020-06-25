package com.tetravalstartups.dingdong.modules.create;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cloudinary.android.MediaManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.otaliastudios.cameraview.VideoResult;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.Constants;
import com.tetravalstartups.dingdong.utils.MyAssemblyProgressListener;
import com.transloadit.android.sdk.AndroidAsyncAssembly;
import com.transloadit.android.sdk.AndroidTransloadit;
import com.transloadit.sdk.async.AssemblyProgressListener;
import com.transloadit.sdk.exceptions.LocalOperationException;
import com.transloadit.sdk.exceptions.RequestException;
import com.transloadit.sdk.response.AssemblyResponse;

import java.io.File;
import java.io.IOException;

public class VideoPreviewActivity extends AppCompatActivity implements View.OnClickListener, AssemblyProgressListener  {

    private VideoView videoView;
    private static VideoResult videoResult;
    MediaPlayer soundPlayer;
    SeekBar seekBarSound;
    SeekBar seekBarRecording;
    AudioManager audioManager;
    FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;

    private String sound_url;

    private TextView tvUpload;

    private Master master;

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

        preferences = getSharedPreferences("selected_sound", 0);

        seekBarSound = findViewById(R.id.seekBarSound);
        seekBarRecording = findViewById(R.id.seekBarRecording);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        seekBarSound.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarSound.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBarRecording.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        seekBarRecording.setProgress(0);

        firebaseAuth = FirebaseAuth.getInstance();

        master = new Master(VideoPreviewActivity.this);

        videoView = findViewById(R.id.videoView);
        tvUpload = findViewById(R.id.tvUpload);

        videoView.setOnClickListener(this);
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("videos").document();
                String public_id = documentReference.getId();

                Video video = new Video();
                video.setId(public_id);
                video.setVideo_desc("");
                video.setSound_contain(false);
                video.setSound_id("");
                video.setSound_title("");
                video.setSound_url("");
                video.setLikes_count(Constants.INITIAL_VIDEO_LIKES);
                video.setShare_count(Constants.INITIAL_VIDEO_SHARES);
                video.setComment_count(Constants.INITIAL_VIDEO_COMMENTS);
                video.setUser_id(firebaseAuth.getUid());
                video.setUser_handle(master.getHandle());
                video.setUser_photo(master.getPhoto());
                video.setVideo_thumbnail("");
                video.setVideo_status(Constants.INITIAL_VIDEO_STATUS);

                String requestId = MediaManager.get().upload(Uri.fromFile(result.getFile()))
                        .option("resource_type", "video")
                        .option("folder", "user_uploaded_videos")
                        .option("public_id", public_id)
                        .option("overwrite", true)
                        .dispatch();

                db.collection("videos").document(public_id)
                        .set(video)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(VideoPreviewActivity.this, "video upload successful...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                Log.e("requestId", requestId);
            }
        });


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
                        .load(sound_url,false)
                        .fromDirectory("dingdong/songs", FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                File loadedFile = response.getBody();

                                try {
                                    soundPlayer.setDataSource(loadedFile.getPath());
                                    soundPlayer.prepare();
                                    soundPlayer.start();
                                    playVideo();



                                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            soundPlayer.stop();
                                            soundPlayer.release();
                                        }
                                    });

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
        sound_url = preferences.getString("sound_url", null);
    }

}
