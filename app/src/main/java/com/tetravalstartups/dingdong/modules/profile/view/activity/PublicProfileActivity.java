package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.PhoneActivity;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfile;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfileResponse;
import com.tetravalstartups.dingdong.modules.profile.model.Follow;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.PublicProfilePagerAdapter;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.LightBox;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PublicProfileActivity";
    private ViewPager videoPager;
    private TabLayout videoTab;
    private LinearLayout lvLikes;
    private LinearLayout lvFollower;
    private LinearLayout lvFollowing;
    private TextView tvName;
    private TextView tvHandle;
    private ImageView ivPhoto;
    private ImageView ivSettings;
    private ImageView ivGoBack;
    private TextView tvBio;
    private TextView tvLikeCount;
    private TextView tvFollowerCount;
    private TextView tvFollowingCount;
    private TextView tvFollow;
    private TextView tvVideosCount;
    private LinearLayout lvParent;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private DDLoading ddLoading;
    private Master master;
    private PublicProfileResponse profile;
    private RequestInterface requestInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        initView();
    }

    private void initView() {
        videoPager = findViewById(R.id.videoPager);
        videoTab = findViewById(R.id.videoTab);
        lvLikes = findViewById(R.id.lvLikes);
        lvFollower = findViewById(R.id.lvFollower);
        lvFollowing = findViewById(R.id.lvFollowing);
        ivSettings = findViewById(R.id.ivSettings);
        tvFollow = findViewById(R.id.tvFollow);
        tvFollow.setOnClickListener(this);

        lvLikes.setOnClickListener(this);
        lvFollower.setOnClickListener(this);
        lvFollowing.setOnClickListener(this);

        tvName = findViewById(R.id.tvName);
        tvHandle = findViewById(R.id.tvHandle);
        ivPhoto = findViewById(R.id.ivPhoto);
        tvBio = findViewById(R.id.tvBio);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        tvFollowerCount = findViewById(R.id.tvFollowerCount);
        tvFollowingCount = findViewById(R.id.tvFollowingCount);
        lvParent = findViewById(R.id.lvParent);
        ivGoBack = findViewById(R.id.ivGoBack);
        tvVideosCount = findViewById(R.id.tvVideosCount);
        ivGoBack.setOnClickListener(this);
        profile = new PublicProfileResponse();

        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        ddLoading = DDLoading.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        master = new Master(PublicProfileActivity.this);
        ddLoading.showProgress(PublicProfileActivity.this, "Loading...", false);

        if (firebaseAuth.getCurrentUser() == null) {
            fetchUserProfile(getIntent().getStringExtra("user_id"), getIntent().getStringExtra("user_id"));
        } else {
            fetchUserProfile(getIntent().getStringExtra("user_id"), master.getId());
        }
        setupViewPager(getIntent().getStringExtra("user_id"));
    }

    private void fetchUserProfile(String id, String user_id) {
        Call<PublicProfile> call = requestInterface.getUserData(id, user_id);
        call.enqueue(new Callback<PublicProfile>() {
            @Override
            public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                PublicProfileResponse publicProfileResponse = response.body().getPublicProfileResponse();
                profile = publicProfileResponse;
                setupProfile();
                ddLoading.hideProgress();
            }

            @Override
            public void onFailure(Call<PublicProfile> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                ddLoading.hideProgress();
            }
        });
    }

    private void setupProfile() {
        tvName.setText(profile.getName());
        tvHandle.setText(profile.getHandle());
        tvFollowerCount.setText(profile.getFollowers() + "");
        tvFollowingCount.setText(profile.getFollowing() + "");
        tvLikeCount.setText(profile.getLikes() + "");
        tvVideosCount.setText(profile.getVideos() + " videos");

        if (firebaseAuth.getCurrentUser() == null) {
            tvFollow.setVisibility(View.VISIBLE);
            tvFollow.setText(getResources().getString(R.string.follow));
            tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_gradient));
            tvFollow.setTextColor(getResources().getColor(R.color.colorTextTitle));
            tvFollow.setVisibility(View.VISIBLE);

        } else {
            if (profile.getId().equals(master.getId())) {
                tvFollow.setVisibility(View.GONE);

            } else if (profile.getMyfollow().equals("notfollowed")) {
                tvFollow.setVisibility(View.VISIBLE);
                tvFollow.setText("Follow");
                tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_gradient));
                tvFollow.setTextColor(getResources().getColor(R.color.colorTextTitle));
                tvFollow.setVisibility(View.VISIBLE);

            } else if (profile.getMyfollow().equals("following")) {
                tvFollow.setVisibility(View.VISIBLE);
                tvFollow.setText("Unfollow");
                tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_disabled));
                tvFollow.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvFollow.setVisibility(View.VISIBLE);

            } else if (profile.getMyfollow().equals("followBack")) {
                tvFollow.setVisibility(View.VISIBLE);
                tvFollow.setText("Follow Back");
                tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_gradient));
                tvFollow.setTextColor(getResources().getColor(R.color.colorTextTitle));
                tvFollow.setVisibility(View.VISIBLE);

            }
        }

        if (profile.getBio() != null) {
            tvBio.setText(profile.getBio());
        } else {
            tvBio.setVisibility(View.GONE);
        }

        Glide.with(getApplicationContext())
                .load(profile.getPhoto())
                .placeholder(R.drawable.dd_logo_placeholder)
                .into(ivPhoto);

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LightBox(PublicProfileActivity.this).showLightBox(profile.getPhoto());
            }
        });
    }

    private void setupViewPager(String user_id) {
        PublicProfilePagerAdapter publicProfilePagerAdapter = new PublicProfilePagerAdapter(getSupportFragmentManager(), user_id);
        videoPager.setAdapter(publicProfilePagerAdapter);
        videoTab.setupWithViewPager(videoPager);
        Objects.requireNonNull(videoTab.getTabAt(0)).setIcon(R.drawable.ic_dd_public_videos_inactive);
    }

    @Override
    public void onClick(View v) {
        if (v == tvFollow) {
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(PublicProfileActivity.this, PhoneActivity.class));
            } else {
                doFollow();
            }
        }

        if (v == lvFollower) {
            Intent intent = new Intent(PublicProfileActivity.this, FollowersActivity.class);
            intent.putExtra("user_id", profile.getId());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lvFollowing) {
            Intent intent = new Intent(PublicProfileActivity.this, FollowingActivity.class);
            intent.putExtra("user_id", profile.getId());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == ivGoBack) {
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseAuth.getCurrentUser() == null) {
            fetchUserProfile(getIntent().getStringExtra("user_id"), getIntent().getStringExtra("user_id"));
        } else {
            fetchUserProfile(getIntent().getStringExtra("user_id"), master.getId());
        }
    }

    private void doFollow() {
        Call<Follow> call = requestInterface.doFollowUser(master.getId(), profile.getId());
        call.enqueue(new Callback<Follow>() {
            @Override
            public void onResponse(Call<Follow> call, Response<Follow> response) {
                if (response.code() == 200) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        fetchUserProfile(getIntent().getStringExtra("user_id"), getIntent().getStringExtra("user_id"));
                    } else {
                        fetchUserProfile(getIntent().getStringExtra("user_id"), master.getId());
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Follow> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}