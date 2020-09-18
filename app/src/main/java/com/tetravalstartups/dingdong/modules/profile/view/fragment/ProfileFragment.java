package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.tabs.TabLayout;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfile;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfileResponse;
import com.tetravalstartups.dingdong.modules.profile.view.activity.EditProfileActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.FollowersActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.FollowingActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.SettingsActivity;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.VideoTabPagerAdapter;
import com.tetravalstartups.dingdong.utils.LightBox;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ProfileFragment";
    private View view;
    private ViewPager videoPager;
    private TabLayout videoTab;
    private LinearLayout lvVideos;
    private LinearLayout lvLikes;
    private LinearLayout lvFollower;
    private LinearLayout lvFollowing;
    private TextView tvName;
    private TextView tvHandle;
    private ImageView ivPhoto;
    private ImageView ivSettings;
    private SocialTextView tvBio;
    private TextView tvLikeCount;
    private TextView tvFollowerCount;
    private TextView tvFollowingCount;
    private TextView tvEditProfile;
    private TextView tvVideosCount;
    private Master master;
    private RequestInterface requestInterface;
    private SkeletonScreen skVideo, skLikes, skFollower, skFollowing;

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
        lvVideos = view.findViewById(R.id.lvVideos);
        lvLikes = view.findViewById(R.id.lvLikes);
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

        master = new Master(getContext());

        ivSettings.setOnClickListener(this);
        lvFollower.setOnClickListener(this);
        lvFollowing.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);

        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);

        skVideo = Skeleton.bind(tvVideosCount).load(R.layout.shimmer_count_tv).shimmer(true).show();
        skLikes = Skeleton.bind(tvLikeCount).load(R.layout.shimmer_count_tv).shimmer(true).show();
        skFollower = Skeleton.bind(tvFollowerCount).load(R.layout.shimmer_count_tv).shimmer(true).show();
        skFollowing = Skeleton.bind(tvFollowingCount).load(R.layout.shimmer_count_tv).shimmer(true).show();

        setupUserDetails();
        fetchCounts();

    }

    @Override
    public void onStart() {
        super.onStart();
        setupViewPager(master.getId());
    }

    private void setupUserDetails() {
        tvName.setText(master.getName());
        tvHandle.setText(String.format("@%s", master.getHandle()));
        tvBio.setLinkText(master.getBio());

        if (getActivity() != null) {
            Glide.with(getContext())
                    .load(master.getPhoto())
                    .placeholder(R.drawable.dd_logo_placeholder)
                    .timeout(60000)
                    .into(ivPhoto);
        }

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LightBox(getContext()).showLightBox(master.getPhoto());
            }
        });
    }

    private void fetchCounts() {
        Call<PublicProfile> call = requestInterface.getUserData(master.getId(), master.getId());
        call.enqueue(new Callback<PublicProfile>() {
            @Override
            public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                if (response.code() == 200) {
                    PublicProfileResponse publicProfileResponse = response.body().getPublicProfileResponse();
                    setupCountsAndVideos(publicProfileResponse);
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PublicProfile> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setupCountsAndVideos(PublicProfileResponse publicProfileResponse) {
        tvVideosCount.setText(publicProfileResponse.getVideos() + "");
        tvLikeCount.setText(publicProfileResponse.getLikes() + "");
        tvFollowerCount.setText(publicProfileResponse.getFollowers() + "");
        tvFollowingCount.setText(publicProfileResponse.getFollowing() + "");

        skVideo.hide();
        skLikes.hide();
        skFollower.hide();
        skFollowing.hide();
    }

    private void setupViewPager(String id) {
        VideoTabPagerAdapter videoTabPagerAdapter = new VideoTabPagerAdapter(getChildFragmentManager(), id);
        videoPager.setAdapter(videoTabPagerAdapter);
        videoTab.setupWithViewPager(videoPager);
        Objects.requireNonNull(videoTab.getTabAt(0)).setIcon(R.drawable.ic_dd_public_videos_inactive);
        Objects.requireNonNull(videoTab.getTabAt(1)).setIcon(R.drawable.ic_dd_liked_videos_inactive);
        Objects.requireNonNull(videoTab.getTabAt(2)).setIcon(R.drawable.ic_dd_private_videos_inactive);
    }

    @Override
    public void onClick(View v) {
        if (v == lvFollower) {
            Intent intent = new Intent(getContext(), FollowersActivity.class);
            intent.putExtra("user_id", master.getId());
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == lvFollowing) {
            Intent intent = new Intent(getContext(), FollowingActivity.class);
            intent.putExtra("user_id", master.getId());
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == ivSettings) {
            startActivity(new Intent(getContext(), SettingsActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == tvEditProfile) {
            startActivity(new Intent(getContext(), EditProfileActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getProfileData(master.getId());
        fetchCounts();
    }


}
