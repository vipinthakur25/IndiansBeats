package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.view.activity.EditProfileActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.FollowersActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.SettingsActivity;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.VideoTabPagerAdapter;
import com.tetravalstartups.dingdong.utils.LightBox;

import java.util.Objects;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ViewPager videoPager;
    private TabLayout videoTab;
    private LinearLayout lvFollower;
    private TextView tvName;
    private TextView tvHandle;
    private ImageView ivPhoto;
    private ImageView ivSettings;
    private TextView tvBio;
    private TextView tvLikeCount;
    private TextView tvFollowerCount;
    private TextView tvFollowingCount;
    private TextView tvEditProfile;


    private Master master;
    private FirebaseAuth auth;

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
        ivSettings = view.findViewById(R.id.ivSettings);
        tvEditProfile = view.findViewById(R.id.tvEditProfile);

        tvName = view.findViewById(R.id.tvName);
        tvHandle = view.findViewById(R.id.tvHandle);
        ivPhoto = view.findViewById(R.id.ivPhoto);
        tvBio = view.findViewById(R.id.tvBio);
        tvLikeCount = view.findViewById(R.id.tvLikeCount);
        tvFollowerCount = view.findViewById(R.id.tvFollowerCount);
        tvFollowingCount = view.findViewById(R.id.tvFollowingCount);

        auth = FirebaseAuth.getInstance();
        master = new Master(getContext());

        ivSettings.setOnClickListener(this);
        lvFollower.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);

        if (auth.getCurrentUser() != null) {
            ((MainActivity) getActivity()).getProfileData(auth.getCurrentUser().getUid());
            setupProfile();
            setupViewPager();
        }

    }

    private void setupProfile() {
        tvName.setText(master.getName());
        tvHandle.setText(master.getHandle());
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

    }

    private void setupViewPager() {
        VideoTabPagerAdapter videoTabPagerAdapter = new VideoTabPagerAdapter(getChildFragmentManager());
        videoPager.setAdapter(videoTabPagerAdapter);
        videoTab.setupWithViewPager(videoPager);
        Objects.requireNonNull(videoTab.getTabAt(0)).setIcon(R.drawable.ic_dd_created_video_inactive);
        Objects.requireNonNull(videoTab.getTabAt(1)).setIcon(R.drawable.ic_dd_liked_video_inactive);
        Objects.requireNonNull(videoTab.getTabAt(2)).setIcon(R.drawable.ic_dd_private_draft_video);
    }

    @Override
    public void onClick(View v) {
        if (v == lvFollower) {
            startActivity(new Intent(getContext(), FollowersActivity.class));
        }

        if (v == ivSettings) {
            startActivity(new Intent(getContext(), SettingsActivity.class));
        }

        if (v == tvEditProfile){
            startActivity(new Intent(getContext(), EditProfileActivity.class));
        }
    }
}
