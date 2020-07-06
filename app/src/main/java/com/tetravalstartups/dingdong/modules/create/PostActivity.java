package com.tetravalstartups.dingdong.modules.create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.Constants;
import com.tetravalstartups.dingdong.utils.DDVideoPreview;

import java.io.File;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvPost;
    private ImageView ivVideoCover;
    private SharedPreferences preferences;
    private DDVideoPreview ddVideoPreview;

    private Master master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();
    }

    private void initView() {

        master = new Master(this);

        preferences = getSharedPreferences("trans_path", 0);
        ddVideoPreview = DDVideoPreview.getInstance();

        tvPost = findViewById(R.id.tvPost);
        tvPost.setOnClickListener(this);

        ivVideoCover = findViewById(R.id.ivVideoCover);
        ivVideoCover.setOnClickListener(this);
        Glide.with(this).load(preferences.getString("video_path", "")).into(ivVideoCover);
    }

    @Override
    public void onClick(View v) {
        if (v == tvPost){
            postVideo(preferences.getString("video_path", ""));
        }

        if (v == ivVideoCover){
            ddVideoPreview.playVideo(PostActivity.this,
                    preferences.getString("video_path", ""),
                    false);
        }

    }

    private void postVideo(String video_path) {
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
                video.setUser_id(master.getId());
                video.setUser_handle(master.getHandle());
                video.setUser_photo(master.getPhoto());
                video.setVideo_thumbnail("");
                video.setVideo_status(Constants.INITIAL_VIDEO_STATUS);

                String requestId = MediaManager.get().upload(Uri.fromFile(new File(video_path)))
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
                                    Toast.makeText(PostActivity.this, "video upload successful...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                Log.e("requestId", requestId);
        }

        }

