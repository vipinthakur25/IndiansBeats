package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.view.activity.EditProfileActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.FollowersActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.FollowingActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.SettingsActivity;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.VideoTabPagerAdapter;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.LightBox;

import java.util.HashMap;
import java.util.Objects;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ViewPager videoPager;
    private TabLayout videoTab;
    private LinearLayout lvFollower;
    private LinearLayout lvFollowing;
    private TextView tvName;
    private TextView tvHandle;
    private ImageView ivPhoto;
    private ImageView ivSettings;
    private TextView tvBio;
    private TextView tvLikeCount;
    private TextView tvFollowerCount;
    private TextView tvFollowingCount;
    private TextView tvEditProfile;
    private TextView tvVideosCount;

    private Master master;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();
        return view;
    }

    private void initView() {
        videoPager = view.findViewById(R.id.videoPager);
        videoTab = view.findViewById(R.id.videoTab);
        lvFollower = view.findViewById(R.id.lvFollower);
        lvFollowing = view.findViewById(R.id.lvFollowing);
        ivSettings = view.findViewById(R.id.ivSettings);
        tvEditProfile = view.findViewById(R.id.tvEditProfile);

        tvName = view.findViewById(R.id.tvName);
        tvHandle = view.findViewById(R.id.tvHandle);
        ivPhoto = view.findViewById(R.id.ivPhoto);
        tvBio = view.findViewById(R.id.tvBio);
        tvLikeCount = view.findViewById(R.id.tvLikeCount);
        tvFollowerCount = view.findViewById(R.id.tvFollowerCount);
        tvFollowingCount = view.findViewById(R.id.tvFollowingCount);
        tvVideosCount = view.findViewById(R.id.tvVideosCount);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        master = new Master(getContext());

        ivSettings.setOnClickListener(this);
        lvFollower.setOnClickListener(this);
        lvFollowing.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);

        SharedPreferences preferences = getContext().getSharedPreferences("videoPref", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profile_type", "public");
        editor.putString("user_id", master.getId());
        editor.apply();

        if (auth.getCurrentUser() != null) {
            ((MainActivity) getActivity()).getProfileData(auth.getCurrentUser().getUid());
            setupProfile();
            setupViewPager();
        }

    }

    private void setupProfile() {
        tvName.setText(master.getName());
        tvHandle.setText("@"+master.getHandle());
        if (master.getBio() != null) {
            tvBio.setText(master.getBio());
        }

        Glide.with(getContext())
                .load(master.getPhoto())
                .placeholder(R.drawable.dd_logo_placeholder)
                .into(ivPhoto);

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LightBox(getContext()).showLightBox(master.getPhoto());
            }
        });

        if (master.getLikes() != null) {
            tvLikeCount.setText(master.getLikes());
        } else {
            tvLikeCount.setText("0");
        }

        if (master.getFollowers() != null) {
            tvFollowerCount.setText(master.getFollowers());
        } else {
            tvFollowerCount.setText("0");
        }

        if (master.getFollowing() != null) {
            tvFollowingCount.setText(master.getFollowing());
        } else {
            tvFollowingCount.setText("0");
        }

        fetchFollowerFollowing();

    }

    private void setupViewPager() {
        VideoTabPagerAdapter videoTabPagerAdapter = new VideoTabPagerAdapter(getChildFragmentManager());
        videoPager.setAdapter(videoTabPagerAdapter);
        videoTab.setupWithViewPager(videoPager);

        Objects.requireNonNull(videoTab.getTabAt(0)).setIcon(R.drawable.ic_dd_public_videos_inactive);
        Objects.requireNonNull(videoTab.getTabAt(1)).setIcon(R.drawable.ic_dd_liked_videos_inactive);
        Objects.requireNonNull(videoTab.getTabAt(2)).setIcon(R.drawable.ic_dd_private_videos_inactive);

    }

    @Override
    public void onClick(View v) {
        if (v == lvFollower) {
            startActivity(new Intent(getContext(), FollowersActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lvFollowing) {
            startActivity(new Intent(getContext(), FollowingActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == ivSettings) {
            startActivity(new Intent(getContext(), SettingsActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == tvEditProfile){
            startActivity(new Intent(getContext(), EditProfileActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void fetchFollowerFollowing() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(master.getId())
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
                                        .document(master.getId())
                                        .update(hashMap);
                            }
                        }
                    }
                });

        db.collection("users")
                .document(master.getId())
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
                                        .document(master.getId())
                                        .update(hashMap);
                            }
                        }
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (auth.getCurrentUser() != null) {
            ((MainActivity) getActivity()).getProfileData(auth.getCurrentUser().getUid());
            setupProfile();
            setupViewPager();
            fetchVideosCount();
        }
    }

    private void fetchVideosCount() {
        Query query = db.collection("videos").whereEqualTo("user_id", master.getId()).whereEqualTo("video_status", Constant.VIDEO_STATUS_PUBLIC);
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



}
