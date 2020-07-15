package com.tetravalstartups.dingdong.modules.profile.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.like.LikeButton;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;

public class PlayVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivGoBack;
    private ImageView ivPhoto;
    private ImageView ivFollowUser;
    private ImageView ivComment;
    private ImageView ivShare;
    private ImageView ivSoundCD;

    private TextView tvLikeCount;
    private TextView tvCommentCount;
    private TextView tvShareCount;
    private TextView tvUserHandle;
    private TextView tvVideoDesc;
    private TextView tvSoundName;

    private LikeButton likeVideo;

    private SimpleExoPlayer player;
    private SimpleExoPlayerView exoPlay;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_video);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        db = FirebaseFirestore.getInstance();

        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        ivPhoto = findViewById(R.id.ivPhoto);
        ivFollowUser = findViewById(R.id.ivFollowUser);
        ivComment = findViewById(R.id.ivComment);
        ivShare = findViewById(R.id.ivShare);
        ivSoundCD = findViewById(R.id.ivSoundCD);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        tvCommentCount = findViewById(R.id.tvCommentCount);
        tvShareCount = findViewById(R.id.tvShareCount);
        tvUserHandle = findViewById(R.id.tvUserHandle);
        tvVideoDesc = findViewById(R.id.tvVideoDesc);
        tvSoundName = findViewById(R.id.tvSoundName);
        likeVideo = findViewById(R.id.likeVideo);

        exoPlay = findViewById(R.id.exoPlay);

        String video_id = getIntent().getStringExtra("video_id");
        String url = MediaManager.get().url().transformation(new Transformation().quality(5)).resourceType("video").generate("user_uploaded_videos/"+video_id+".webm");

        setVideoData(video_id);
        playVideo(url);
    }

    private void setVideoData(String video_id) {
        db.collection("videos")
                .document(video_id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        assert documentSnapshot != null;
                        if (documentSnapshot.exists()){
                            String user_photo = documentSnapshot.getString("user_photo");
                            String likes_count = documentSnapshot.getString("likes_count");
                            String comment_count = documentSnapshot.getString("comment_count");
                            String share_count = documentSnapshot.getString("share_count");
                            String user_handle = documentSnapshot.getString("user_handle");
                            String video_desc = documentSnapshot.getString("video_desc");
                            String sound_title = documentSnapshot.getString("sound_title");
                            
                            setDataToUI(user_photo, likes_count,
                                    comment_count, share_count,
                                    user_handle, video_desc,
                                    sound_title);
                            
                        } else {
                            Toast.makeText(PlayVideoActivity.this, ""+e.getMessage()
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDataToUI(String user_photo, String likes_count, String comment_count, String share_count, String user_handle, String video_desc, String sound_title) {
        Glide.with(PlayVideoActivity.this)
                .load(user_photo)
                .into(ivPhoto);

        tvLikeCount.setText(likes_count);
        tvCommentCount.setText(comment_count);
        tvShareCount.setText(share_count);
        tvUserHandle.setText(user_handle);
        tvVideoDesc.setText(video_desc);
        tvSoundName.setText(sound_title);
    }

    private void playVideo(String url) {
        player = new SimpleExoPlayer.Builder(this).build();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "BubbleTok"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(url));

        player.prepare(videoSource);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        player.setPlaybackParameters(PlaybackParameters.DEFAULT);

        exoPlay.setPlayer(player);
        player.setPlayWhenReady(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack) {
            onBackPressed();
            finish();
        }
    }
}