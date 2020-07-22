package com.tetravalstartups.dingdong.modules.profile.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.Followings;
import com.tetravalstartups.dingdong.modules.profile.presenter.FollowerPresenter;
import com.tetravalstartups.dingdong.modules.profile.presenter.FollowingPresenter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.FollowerAdapter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.FollowingAdapter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.List;

public class FollowingActivity extends AppCompatActivity implements View.OnClickListener, FollowingPresenter.IFollowing {

    private RecyclerView recyclerFollowing;
    private ImageView ivGoBack, ivSettings;
    private TextView tvNoFollowers;
    private ProgressBar progressBar;
    private Master master;
    private String profile_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);
        ivSettings = findViewById(R.id.ivSettings);
        progressBar = findViewById(R.id.progressBar);
        recyclerFollowing = findViewById(R.id.recyclerFollowing);
        recyclerFollowing.setLayoutManager(new LinearLayoutManager(FollowingActivity.this));
        recyclerFollowing.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.VERTICAL));
        tvNoFollowers = findViewById(R.id.tvNoFollowers);
        master = new Master(FollowingActivity.this);
        fetchFollowing();
    }

    private void fetchFollowing() {
        FollowingPresenter followingPresenter = new FollowingPresenter(FollowingActivity.this, FollowingActivity.this);
        if (getIntent().hasExtra("user_id")) {
            profile_type = "public";
            followingPresenter.fetchFollowing(getIntent().getStringExtra("user_id"));
        } else {
            profile_type = "private";
            followingPresenter.fetchFollowing(master.getId());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack) {
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public void followingFetchSuccess(List<Followings> followingsList) {
        FollowingAdapter followingAdapter = new
                FollowingAdapter(FollowingActivity.this,
                followingsList, profile_type);
        followingAdapter.notifyDataSetChanged();
        recyclerFollowing.setAdapter(followingAdapter);
        recyclerFollowing.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        tvNoFollowers.setVisibility(View.GONE);
    }

    @Override
    public void followingFetchError(String error) {
        recyclerFollowing.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);
        tvNoFollowers.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}