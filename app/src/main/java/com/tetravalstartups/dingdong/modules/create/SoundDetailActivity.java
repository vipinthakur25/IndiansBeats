package com.tetravalstartups.dingdong.modules.create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.transformation.Layer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.create.sound.Sound;
import com.tetravalstartups.dingdong.modules.record.RecordActivity;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SoundDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivCreate;
    private String sound_id;
    private String sound_title;
    private String user_photo;
    private String user_handle;
    private String user_id;

    private ImageView ivPhoto;
    private ImageView ivGoBack;
    private LikeButton ivFav;
    private TextView tvSoundTitle;
    private TextView tvUserHandle;
    private TextView tvVideoCount;

    private Bundle bundle;
    private DDLoading ddLoading;
    private FirebaseFirestore db;
    private Master master;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_detail);
        initView();
    }

    private void initView() {
        ivCreate = findViewById(R.id.ivCreate);
        ivPhoto = findViewById(R.id.ivPhoto);
        tvSoundTitle = findViewById(R.id.tvSoundTitle);
        tvUserHandle = findViewById(R.id.tvUserHandle);
        tvVideoCount = findViewById(R.id.tvVideoCount);
        ivGoBack = findViewById(R.id.ivGoBack);
        ivFav = findViewById(R.id.ivFav);
        ivGoBack.setOnClickListener(this);

        bundle = getIntent().getExtras();
        db = FirebaseFirestore.getInstance();
        master = new Master(SoundDetailActivity.this);

        Glide.with(this).load(R.drawable.dd_create_video).into(ivCreate);
        ivCreate.setOnClickListener(this);

        ddLoading = DDLoading.getInstance();

        setSoundData();

        ivFav.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                String url = MediaManager.get().url().resourceType("video").generate("user_uploaded_videos/"+sound_id+".mp3");
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            int duration = mediaPlayer.getDuration();
                            String time = String.format("%d:%d",
                                    TimeUnit.MILLISECONDS.toMinutes(duration),
                                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                            );
                            Sound sound = new Sound();
                            sound.setId(bundle.getString("sound_id"));
                            sound.setCat_name("Recommended");
                            sound.setName(bundle.getString("sound_title"));
                            sound.setMedia(url);
                            sound.setArtist(bundle.getString("user_handle"));
                            sound.setStatus("1");
                            sound.setBanner(bundle.getString("user_photo"));
                            sound.setDuration(time);

                            db.collection("users")
                                    .document(master.getId())
                                    .collection("sounds")
                                    .document(bundle.getString("sound_id"))
                                    .set(sound);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void unLiked(LikeButton likeButton) {
                db.collection("users")
                        .document(master.getId())
                        .collection("sounds")
                        .document(bundle.getString("sound_id"))
                        .delete();
            }
        });

    }

    private void setSoundData(){
        sound_id = bundle.getString("sound_id");
        sound_title = bundle.getString("sound_title");
        user_photo = bundle.getString("user_photo");
        user_handle = bundle.getString("user_handle");
        user_id = bundle.getString("user_id");

        Glide.with(this).load(user_photo).into(ivPhoto);
        tvSoundTitle.setText(sound_title);
        tvUserHandle.setText(user_handle);

        db.collection("users")
                .document(master.getId())
                .collection("sounds")
                .document(bundle.getString("sound_id"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                ivFav.setLiked(true);
                            } else {
                                ivFav.setLiked(false);
                            }
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v == ivCreate){
            ddLoading.showProgress(SoundDetailActivity.this, "Loading...", false);
            String url = MediaManager.get().url().resourceType("video").generate("user_uploaded_videos/"+sound_id+".mp3");
            FileLoader.with(this)
                    .load(url,false)
                    .fromDirectory("dingdong/songs", FileLoader.DIR_EXTERNAL_PUBLIC)
                    .asFile(new FileRequestListener<File>() {
                        @Override
                        public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                            File loadedFile = response.getBody();
                            ddLoading.hideProgress();
                            SharedPreferences preferences = getSharedPreferences("selected_sound", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            Intent intent = new Intent(SoundDetailActivity.this, RecordActivity.class);
                            editor.putString("sound_path", loadedFile.getPath());
                            editor.putString("sound_name", sound_title);
                            editor.apply();
                            Bundle bundle = new Bundle();
                            bundle.putString("sound", "true");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(FileLoadRequest request, Throwable t) {
                            ddLoading.hideProgress();
                        }
                    });
        }

        if (v == ivGoBack) {
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }
}
