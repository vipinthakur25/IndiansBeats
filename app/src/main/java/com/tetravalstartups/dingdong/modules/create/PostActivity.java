package com.tetravalstartups.dingdong.modules.create;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.Constants;
import com.tetravalstartups.dingdong.utils.DDLoadingProgress;
import com.tetravalstartups.dingdong.utils.DDVideoPreview;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvPost;
    private ImageView ivVideoCover;
    private DDVideoPreview ddVideoPreview;
    private DDLoadingProgress ddLoadingProgress;
    private String video_path;

    private EditText etVideoDesc;
    private TextView tvHashTag;
    private TextView tvAtTheRate;
    private ImageView ivGoBack;

    private Switch switchVideoStatus;
    private boolean video_status = false;



    private Master master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();
    }

    private void initView() {
        video_path = getIntent().getStringExtra("video_path");
        master = new Master(this);

        ddVideoPreview = DDVideoPreview.getInstance();

        tvPost = findViewById(R.id.tvPost);
        tvPost.setOnClickListener(this);

        ddLoadingProgress = DDLoadingProgress.getInstance();

        etVideoDesc = findViewById(R.id.etVideoDesc);

        tvHashTag = findViewById(R.id.tvHashTag);
        tvHashTag.setOnClickListener(this);

        tvAtTheRate = findViewById(R.id.tvAtTheRate);
        tvAtTheRate.setOnClickListener(this);

        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        switchVideoStatus = findViewById(R.id.switchVideoStatus);
        switchVideoStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                video_status = isChecked;
            }
        });

        ivVideoCover = findViewById(R.id.ivVideoCover);
        ivVideoCover.setOnClickListener(this);
        Glide.with(this).load(video_path).into(ivVideoCover);
    }

    @Override
    public void onClick(View v) {
        if (v == tvPost) {
            postVideo();
        }

        if (v == ivVideoCover) {
            ddVideoPreview.playVideo(PostActivity.this,
                    video_path,
                    false);
        }

        if (v == tvHashTag) {
            etVideoDesc.append("#");
        }

        if (v == tvAtTheRate) {
            etVideoDesc.append("@");
        }

        if (v == ivGoBack) {
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    private void postVideo() {
        ddLoadingProgress.showProgress(PostActivity.this, "Uploading...", false);
        doUpload(video_path);
    }

    private void doUpload(String path) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("videos").document();
        String public_id = documentReference.getId();
        String requestId = MediaManager.get().upload(Uri.fromFile(new File(path)))
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        int progress = (int) ((bytes / new File(path).length()) * 100);
                        ddLoadingProgress.updateProgress(progress);
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        String media_url = MediaManager.get().url().transformation(new Transformation().quality(50))
                                .resourceType("video").generate("user_uploaded_videos/" +public_id+".mp4");

                        String thumbnail = MediaManager.get().url().transformation(new Transformation().quality(50))
                                .resourceType("video").generate("user_uploaded_videos/" +public_id+".webp");

                        Video video = new Video();
                        video.setId(public_id);
                        video.setVideo_desc(etVideoDesc.getText().toString());
                        video.setSound_id(public_id);

                        if (getIntent().getStringExtra("sound_title").equals("Original")){
                            video.setSound_title("Original sound by "+master.getHandle());
                        } else {
                            video.setSound_title(getIntent().getStringExtra("sound_title"));
                        }

                        double randomDouble = Math.random();
                        randomDouble = randomDouble * 1000 + 1;
                        int randomInt = (int) randomDouble;

                        video.setLikes_count(Constants.INITIAL_VIDEO_LIKES);
                        video.setShare_count(Constants.INITIAL_VIDEO_SHARES);
                        video.setComment_count(Constants.INITIAL_VIDEO_COMMENTS);
                        video.setView_count(Constants.INITIAL_VIDEO_VIEWS);
                        video.setUser_id(master.getId());
                        video.setUser_handle(master.getHandle());
                        video.setUser_name(master.getName());
                        video.setUser_photo(master.getPhoto());
                        video.setVideo_thumbnail(thumbnail);
                        video.setVideo_url(media_url);
                        video.setVideo_index(randomInt+"");

                        if (video_status) {
                            video.setVideo_status(Constants.VIDEO_STATUS_PRIVATE);
                        } else {
                            video.setVideo_status(Constants.VIDEO_STATUS_PUBLIC);
                        }

                        db.collection("videos").document(public_id)
                                .set(video)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        ddLoadingProgress.hideProgress();
                                    }
                                });
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        ddLoadingProgress.hideProgress();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .option("resource_type", "video")
                .option("folder", "user_uploaded_videos")
                .option("public_id", public_id)
                .option("overwrite", true)
                .dispatch();

        Log.e("requestId", requestId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

