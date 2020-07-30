package com.tetravalstartups.dingdong.modules.create;

import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.modules.subscription.SubPlanChooserBottomSheet;
import com.tetravalstartups.dingdong.modules.subscription.Subscribed;
import com.tetravalstartups.dingdong.utils.Constants;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.DDLoadingProgress;
import com.tetravalstartups.dingdong.utils.DDVideoPreview;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener, SubPlanChooserBottomSheet.SubPlanListener {

    private TextView tvPost;
    private ImageView ivVideoCover;
    private DDVideoPreview ddVideoPreview;
    private DDLoadingProgress ddLoadingProgress;
    private String video_path;

    private DDLoading ddLoading;

    private EditText etVideoDesc;
    private TextView tvHashTag;
    private TextView tvAtTheRate;
    private ImageView ivGoBack;

    private Switch switchVideoStatus;
    private boolean video_status = false;

    private SubPlanChooserBottomSheet subPlanChooserBottomSheet;

    private Master master;
    private FirebaseFirestore db;

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
        ddLoading = DDLoading.getInstance();

        db = FirebaseFirestore.getInstance();

        tvPost = findViewById(R.id.tvPost);
        tvPost.setOnClickListener(this);

        ddLoadingProgress = DDLoadingProgress.getInstance();

        subPlanChooserBottomSheet = new SubPlanChooserBottomSheet();

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
        List<Subscribed> subscribedList = new ArrayList<>();
        if (v == tvPost) {
            ddLoading.showProgress(PostActivity.this, "Loading...", false);
            db.collection("users")
                    .document(master.getId())
                    .collection("subscription")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult().getDocuments().isEmpty()) {
                                ddLoading.hideProgress();
                                postWithoutSubs(video_path);
                            } else {
                                subscribedList.clear();
                                for (DocumentSnapshot snapshot : task.getResult()) {
                                    if (!snapshot.getString("avl_uploads").equals("0")) {
                                        Subscribed subscribed = snapshot.toObject(Subscribed.class);
                                        subscribedList.add(subscribed);
                                    }
                                }

                                if (subscribedList.isEmpty()) {
                                    ddLoading.hideProgress();
                                    postWithoutSubs(video_path);
                                } else {
                                    ddLoading.hideProgress();
                                    postWithSubs();
                                }

                            }
                        }
                    });
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

    private void postVideo(int profit_per_day) {
        ddLoadingProgress.showProgress(PostActivity.this, "Uploading...", false);
        doUpload(video_path, profit_per_day);
    }

    private void postWithSubs() {
        subPlanChooserBottomSheet.show(getSupportFragmentManager(), "subsPlan");
    }

    private void postWithoutSubs(String path) {
        ddLoadingProgress.showProgress(PostActivity.this, "Uploading...", false);
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
                                .resourceType("video").generate("user_uploaded_videos/" + public_id + ".mp4");

                        String thumbnail = MediaManager.get().url().transformation(new Transformation().quality(50))
                                .resourceType("video").generate("user_uploaded_videos/" + public_id + ".webp");

                        Video video = new Video();
                        video.setId(public_id);
                        video.setVideo_desc(etVideoDesc.getText().toString());
                        video.setSound_id(public_id);

                        if (getIntent().getStringExtra("sound_title").equals("Original")) {
                            video.setSound_title("Original sound by " + master.getHandle());
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
                        video.setVideo_index(randomInt + "");

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

    }

    private void doUpload(String path, int profit_per_day) {
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
                                .resourceType("video").generate("user_uploaded_videos/" + public_id + ".mp4");

                        String thumbnail = MediaManager.get().url().transformation(new Transformation().quality(50))
                                .resourceType("video").generate("user_uploaded_videos/" + public_id + ".webp");

                        Video video = new Video();
                        video.setId(public_id);
                        video.setVideo_desc(etVideoDesc.getText().toString());
                        video.setSound_id(public_id);

                        if (getIntent().getStringExtra("sound_title").equals("Original")) {
                            video.setSound_title("Original sound by " + master.getHandle());
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
                        video.setVideo_index(randomInt + "");

                        if (video_status) {
                            video.setVideo_status(Constants.VIDEO_STATUS_PRIVATE);
                        } else {
                            video.setVideo_status(Constants.VIDEO_STATUS_PUBLIC);
                        }

                        db.collection("videos").document(public_id)
                                .set(video)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        db.collection("users")
                                                .document(master.getId())
                                                .collection("passbook")
                                                .document("balance")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        String video_uploads = task.getResult().getString("subscription");
                                                        int video_uploads_int = Integer.parseInt(video_uploads);
                                                        int updated_coins = video_uploads_int + profit_per_day;
                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("subscription", updated_coins + "");

                                                        db.collection("users")
                                                                .document(master.getId())
                                                                .collection("passbook")
                                                                .document("balance")
                                                                .update(hashMap)
                                                                .addOnCompleteListener(new OnCompleteListener() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task task) {
                                                                        Intent intent = new Intent(PostActivity.this, MainActivity.class);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivity(intent);
                                                                        finish();
                                                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                                        ddLoadingProgress.hideProgress();
                                                                    }
                                                                });
                                                    }
                                                });
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

    @Override
    public void onTaskDone(String text) {
        subPlanChooserBottomSheet.dismiss();

        if (text.equals("plan")) {
            SharedPreferences preferences = getSharedPreferences("sub_plan", 0);
            String id = preferences.getString("id", "");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            assert id != null;
            db.collection("users")
                    .document(master.getId())
                    .collection("subscription")
                    .document(id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String avl_uploads = task.getResult().getString("avl_uploads");
                            String end_date = task.getResult().getString("end_date");
                            String id = task.getResult().getString("id");
                            String name = task.getResult().getString("name");
                            String start_date = task.getResult().getString("start_date");
                            String total_uploads = task.getResult().getString("total_uploads");
                            String monthly_profit = task.getResult().getString("monthly_profit");

                            int profit = Integer.parseInt(monthly_profit);
                            Calendar c = Calendar.getInstance();
                            int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                            int profit_per_day = profit / monthMaxDays;

                            int remaining = Integer.parseInt(avl_uploads);
                            int total = Integer.parseInt(total_uploads);

                            int current = remaining - 1;

                            HashMap hashMap = new HashMap();
                            hashMap.put("avl_uploads", current+"");

                            db.collection("users")
                                    .document(master.getId())
                                    .collection("subscription")
                                    .document(id)
                                    .update(hashMap);

                            postVideo(profit_per_day);

                        }
                    });

        } else if (text.equals("no_plan")) {
            postWithoutSubs(video_path);
        }
    }
}

