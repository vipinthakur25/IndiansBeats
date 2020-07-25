package com.tetravalstartups.dingdong.modules.profile.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.BaseActivity;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.LoginActivity;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.Profile;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.PublicProfilePagerAdapter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.VideoTabPagerAdapter;
import com.tetravalstartups.dingdong.utils.Constants;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.LightBox;

import java.util.HashMap;
import java.util.Objects;

public class PublicProfileActivity extends AppCompatActivity implements View.OnClickListener {

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

    private Profile userProfile;


    public enum FollowingStatus {
        FOLLOWING,
        NOT_FOLLOWING
    }

    private FollowingStatus followingStatus;


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

        ddLoading = DDLoading.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        master = new Master(PublicProfileActivity.this);
        ddLoading.showProgress(PublicProfileActivity.this, "Loading...", false);

        fetchUserProfile(getIntent().getStringExtra("user_id"));
        setupViewPager(getIntent().getStringExtra("user_id"));
    }

    private void fetchUserProfile(String id) {
        db.collection("users")
                .document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Profile profile = documentSnapshot.toObject(Profile.class);
                        userProfile = profile;
                        setupProfile(profile);
                        fetchVideosCount(id);
                    }
                });

    }

    private void setupProfile(Profile profile) {
        tvName.setText(profile.getName());
        tvHandle.setText(profile.getHandle());
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

        if (profile.getLikes() != null) {
            tvLikeCount.setText(profile.getLikes());
        } else {
            tvLikeCount.setText("0");
        }

        if (profile.getFollowers() != null) {
            tvFollowerCount.setText(profile.getFollowers());
        } else {
            tvFollowerCount.setText("0");
        }

        if (profile.getFollowing() != null) {
            tvFollowingCount.setText(profile.getFollowing());
        } else {
            tvFollowingCount.setText("0");
        }

        if (firebaseAuth.getCurrentUser() != null) {
            Query followingQuery = db.collection("users").document(master.getId())
                    .collection("following").whereEqualTo("id", profile.getId());
            followingQuery.
                    addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            Query followingQuery = db.collection("users").document(master.getId())
                                    .collection("following").whereEqualTo("id", profile.getId());
                            followingQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (profile.getId().equals(master.getId())) {
                                        tvFollow.setVisibility(View.GONE);
                                    } else if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                                        tvFollow.setVisibility(View.VISIBLE);
                                        tvFollow.setText("Follow");
                                        followingStatus = FollowingStatus.NOT_FOLLOWING;
                                        tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_gradient));
                                        tvFollow.setTextColor(getResources().getColor(R.color.colorTextTitle));
                                    } else {
                                        tvFollow.setVisibility(View.VISIBLE);
                                        tvFollow.setText("Unfollow");
                                        followingStatus = FollowingStatus.FOLLOWING;
                                        tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
                                        tvFollow.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    }
                                }
                            });


                            ddLoading.hideProgress();
                            lvParent.setVisibility(View.VISIBLE);
                        }
                    });


//                        if (master.getId().equals(profile.getId())) {
//                            tvFollow.setVisibility(View.GONE);
//                        } else if (queryDocumentSnapshots.getDocuments().isEmpty()){
//                            tvFollow.setText("Follow");
//                            followingStatus = FollowingStatus.NOT_FOLLOWING;
//                            tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_gradient));
//                            tvFollow.setTextColor(getResources().getColor(R.color.colorTextTitle));
//                        } else {
//                            tvFollow.setText("Unfollow");
//                            followingStatus = FollowingStatus.FOLLOWING;
//                            tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
//                            tvFollow.setTextColor(getResources().getColor(R.color.colorPrimary));
//                        }




        } else {
            tvFollow.setVisibility(View.VISIBLE);
            tvFollow.setText("Follow");
            followingStatus = FollowingStatus.NOT_FOLLOWING;
            tvFollow.setBackground(getResources().getDrawable(R.drawable.bg_button_gradient));
            tvFollow.setTextColor(getResources().getColor(R.color.colorTextTitle));

            fetchFollowerFollowing(profile);
            ddLoading.hideProgress();
            lvParent.setVisibility(View.VISIBLE);
        }

    }

    private void setupViewPager(String user_id) {
        SharedPreferences preferences = getSharedPreferences("videoPref", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profile_type", "public");
        editor.putString("user_id", user_id);
        editor.apply();

        PublicProfilePagerAdapter publicProfilePagerAdapter =
                new PublicProfilePagerAdapter(getSupportFragmentManager());
        videoPager.setAdapter(publicProfilePagerAdapter);
        videoTab.setupWithViewPager(videoPager);

        Objects.requireNonNull(videoTab.getTabAt(0)).setIcon(R.drawable.ic_dd_public_videos_inactive);

    }

    private void fetchFollowerFollowing(Profile profile) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(profile.getId())
                .collection("following")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().isEmpty()){
                                tvFollowingCount.setText("0");
                            } else {
                                int following = task.getResult().getDocuments().size();
                                if (following >= 1000) {
                                    int following_in_k = following/1000;
                                    tvFollowingCount.setText(following_in_k+"K");
                                } else {
                                    tvFollowingCount.setText(following+"");
                                }
                                tvFollowingCount.setText(following+"");
                                HashMap hashMap = new HashMap();
                                hashMap.put("following", following+"");

                                db.collection("users")
                                        .document(profile.getId())
                                        .update(hashMap);
                            }
                        }
                    }
                });

        db.collection("users")
                .document(profile.getId())
                .collection("followers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().isEmpty()){
                                tvFollowerCount.setText("0");
                            } else {
                                int followers = task.getResult().getDocuments().size();
                                if (followers >= 1000) {
                                    int followers_in_k = followers/1000;
                                    tvFollowerCount.setText(followers_in_k+"K");
                                } else {
                                    tvFollowerCount.setText(followers+"");
                                }
                                tvFollowerCount.setText(followers+"");
                                HashMap hashMap = new HashMap();
                                hashMap.put("followers", followers+"");

                                db.collection("users")
                                        .document(profile.getId())
                                        .update(hashMap);
                            }
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v == tvFollow) {
            if (firebaseAuth.getCurrentUser() != null) {

                if (followingStatus == FollowingStatus.FOLLOWING){

                    db.collection("users")
                            .document(master.getId())
                            .collection("following")
                            .document(getIntent().getStringExtra("user_id"))
                            .delete();

                    db.collection("users")
                            .document(getIntent().getStringExtra("user_id"))
                            .collection("followers")
                            .document(getIntent().getStringExtra("user_id"))
                            .delete();

                    followingStatus = FollowingStatus.NOT_FOLLOWING;

                } else {

                    HashMap hmFollowing = new HashMap();
                    hmFollowing.put("id", userProfile.getId());
                    hmFollowing.put("handle", userProfile.getHandle());
                    hmFollowing.put("photo", userProfile.getPhoto());
                    hmFollowing.put("name", userProfile.getName());


                    HashMap hmFollower = new HashMap();
                    hmFollower.put("id", master.getId());
                    hmFollower.put("handle", master.getHandle());
                    hmFollower.put("photo", master.getPhoto());
                    hmFollower.put("name", master.getName());

                    db.collection("users")
                            .document(master.getId())
                            .collection("following")
                            .document(getIntent().getStringExtra("user_id"))
                            .set(hmFollowing);

                    db.collection("users")
                            .document(getIntent().getStringExtra("user_id"))
                            .collection("followers")
                            .document(getIntent().getStringExtra("user_id"))
                            .set(hmFollower);

                    followingStatus = FollowingStatus.FOLLOWING;

                }

            } else {
                startActivity(new Intent(PublicProfileActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }


            onResume();

        }

        if (v == lvLikes) {

        }

        if (v == lvFollower) {
            Intent intent = new Intent(PublicProfileActivity.this, FollowersActivity.class);
            intent.putExtra("user_id", userProfile.getId());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lvFollowing) {
            Intent intent = new Intent(PublicProfileActivity.this, FollowingActivity.class);
            intent.putExtra("user_id", userProfile.getId());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == ivGoBack) {
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    private void fetchVideosCount(String id) {
        Query query = db.collection("videos").whereEqualTo("user_id", id)
                .whereEqualTo("video_status", Constants.VIDEO_STATUS_PUBLIC);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                    tvVideosCount.setText("0 Videos");
                } else {
                    String videos_count = String.valueOf(queryDocumentSnapshots.getDocuments().size());
                    tvVideosCount.setText(videos_count+" Videos");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserProfile(getIntent().getStringExtra("user_id"));
    }
}