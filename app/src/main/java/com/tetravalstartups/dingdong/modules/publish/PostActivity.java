package com.tetravalstartups.dingdong.modules.publish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.PlanInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.subscription.SubPlanChooserBottomSheet;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptions;
import com.tetravalstartups.dingdong.service.PublishService;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.DDVideoPreview;

import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity implements View.OnClickListener, SubPlanChooserBottomSheet.SubPlanListener {

    private static final String TAG = "PostActivity";
    private EditText etVideoDesc;
    private TextView tvPost;
    private ImageView ivVideoCover;
    private TextView tvHashTag;
    private TextView tvAtTheRate;
    private ImageView ivGoBack;
    private DDVideoPreview ddVideoPreview;
    private DDLoading ddLoading;
    private String video_path;
    private String video_index;
    private FirebaseFirestore db;
    private Master master;
    private SubPlanChooserBottomSheet subPlanChooserBottomSheet;
    private SharedPreferences preferences;
    private PublishMeta publishMeta;

    private PlanInterface planInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();
    }

    private void initView() {
        ddVideoPreview = DDVideoPreview.getInstance();
        tvHashTag = findViewById(R.id.tvHashTag);
        tvHashTag.setOnClickListener(this);

        tvAtTheRate = findViewById(R.id.tvAtTheRate);
        tvAtTheRate.setOnClickListener(this);

        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        ivVideoCover = findViewById(R.id.ivVideoCover);
        ivVideoCover.setOnClickListener(this);

        etVideoDesc = findViewById(R.id.etVideoDesc);

        tvPost = findViewById(R.id.tvPost);
        tvPost.setOnClickListener(this);

        video_path = getIntent().getStringExtra("video_path");

        ddVideoPreview = DDVideoPreview.getInstance();
        db = FirebaseFirestore.getInstance();
        master = new Master(PostActivity.this);
        ddLoading = DDLoading.getInstance();
        subPlanChooserBottomSheet = new SubPlanChooserBottomSheet();
        preferences = getSharedPreferences("sub_plan", 0);

        publishMeta = new PublishMeta();

        planInterface = APIClient.getRetrofitInstance().create(PlanInterface.class);

        setData();
    }

    private void setData() {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        DocumentReference documentReference = db.collection("thumbnails").document();
        String refID = documentReference.getId();
        String filename = refID + ".jpeg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);
        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Glide.with(this).load(dest.getPath()).into(ivVideoCover);
        publishMeta.setVideo_thumbnail(dest.getPath());
    }

    @Override
    public void onClick(View view) {
        if (view == tvHashTag) {
            doAppendHashTag();
        }
        if (view == tvAtTheRate) {
            doAppendAtTheRate();
        }
        if (view == ivGoBack) {
            onBackPressed();
        }
        if (view == ivVideoCover) {
            doPlayVideo();
        }
        if (view == tvPost) {
            String desc = etVideoDesc.getText().toString();
            if (desc.isEmpty() || desc.length() < 10) {
                Toast.makeText(this, "Please describe your video with atleast 10 characters.", Toast.LENGTH_SHORT).show();
                return;
            }
            prepareMeta();
        }
    }

    private void prepareMeta() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("videos").document();
        String public_id = documentReference.getId();
        publishMeta.setId(public_id);
        publishMeta.setSound_id(public_id);
        if (getIntent().getStringExtra("sound_title").equals("Original")) {
            publishMeta.setSound_title("Original sound by " + master.getHandle());
        } else {
            publishMeta.setSound_title(getIntent().getStringExtra("sound_title"));
        }
        publishMeta.setUser_id(master.getId());
        publishMeta.setVideo_path(video_path);
        publishMeta.setVideo_desc(etVideoDesc.getText().toString());
        publishMeta.setVideo_status(1);
        publishMeta.setVideo_index(video_index);
        doCheckForPlan();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void doPlayVideo() {
        ddVideoPreview.playVideo(PostActivity.this, video_path, true);
    }

    private void doAppendAtTheRate() {
        etVideoDesc.append("@");
    }

    private void doAppendHashTag() {
        etVideoDesc.append("#");
    }

    private void doCheckForPlan() {
        ddLoading.showProgress(PostActivity.this, "Processing...", false);
        Call<MySubscriptions> call = planInterface.fetchMySubs(master.getId());
        call.enqueue(new Callback<MySubscriptions>() {
            @Override
            public void onResponse(Call<MySubscriptions> call, Response<MySubscriptions> response) {
                if (response.code() == 200) {
                    showPlansToUpload();
                } else {
                    doUploadWithoutPlan();
                }
            }

            @Override
            public void onFailure(Call<MySubscriptions> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void showPlansToUpload() {
        ddLoading.hideProgress();
        subPlanChooserBottomSheet.show(getSupportFragmentManager(), "subsPlan");
    }

    @Override
    public void onTaskDone(int type) {
        if (type == Constant.UPLOAD_SKIP_PLAN) {
            subPlanChooserBottomSheet.dismiss();
            doUploadWithoutPlan();
        }

        if (type == Constant.UPLOAD_WITHOUT_PLAN) {
            subPlanChooserBottomSheet.dismiss();
            doUploadWithoutPlan();
        }

        if (type == Constant.UPLOAD_WITH_PLAN) {
            subPlanChooserBottomSheet.dismiss();
            doUploadWithPlan();
        }
    }

    private void doUploadWithoutPlan() {
        Intent publishIntent = new Intent(PostActivity.this, PublishService.class);
        publishMeta.setReward_type(2);
        publishMeta.setSubs_id("no_subs");
        publishIntent.putExtra("meta", publishMeta);
        publishIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startService(publishIntent);
        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("thumbnail", publishMeta.getVideo_thumbnail());
        mainIntent.putExtras(bundle);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void doUploadWithPlan() {
        String id = preferences.getString("id", "");
        int plan_id = preferences.getInt("plan_id", 0);
        int monthly_profit = preferences.getInt("monthly_profit", 0);
        int total_uploads = preferences.getInt("total_uploads", 0);
        publishMeta.setReward_type(1);
        publishMeta.setPlan_id(plan_id);
        publishMeta.setMonthly_profit(monthly_profit);
        publishMeta.setTotal_uploads(total_uploads);
        publishMeta.setSubs_id(id);
        Intent publishIntent = new Intent(PostActivity.this, PublishService.class);
        publishIntent.putExtra("meta", publishMeta);
        startService(publishIntent);
        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("thumbnail", publishMeta.getVideo_thumbnail());
        mainIntent.putExtras(bundle);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}