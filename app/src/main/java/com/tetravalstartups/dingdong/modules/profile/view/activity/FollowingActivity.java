package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.model.following.FollowingResponse;
import com.tetravalstartups.dingdong.modules.profile.presenter.FollowingPresenter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.FollowingAdapter;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.List;

public class FollowingActivity extends AppCompatActivity implements View.OnClickListener, FollowingPresenter.IFollowing {

    private RecyclerView recyclerFollowing;
    private ImageView ivGoBack;
    private TextView tvNoFollowers;
    private DDLoading ddLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        recyclerFollowing = findViewById(R.id.recyclerFollowing);
        recyclerFollowing.setLayoutManager(new LinearLayoutManager(FollowingActivity.this));
        recyclerFollowing.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.VERTICAL));

        tvNoFollowers = findViewById(R.id.tvNoFollowers);

        ddLoading = DDLoading.getInstance();
        fetchFollowing();
    }

    public void fetchFollowing() {
        ddLoading.showProgress(FollowingActivity.this, "Hold On...", false);
        FollowingPresenter followingPresenter = new FollowingPresenter(FollowingActivity.this, FollowingActivity.this);
        followingPresenter.fetchFollowing(getIntent().getStringExtra("user_id"));
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
    public void followingFetchSuccess(List<FollowingResponse> followingsList) {
        FollowingAdapter followingAdapter = new FollowingAdapter(FollowingActivity.this, followingsList);
        followingAdapter.notifyDataSetChanged();
        recyclerFollowing.setAdapter(followingAdapter);
        recyclerFollowing.setVisibility(View.VISIBLE);
        tvNoFollowers.setVisibility(View.GONE);
        ddLoading.hideProgress();
    }

    @Override
    public void followingFetchError(String error) {
        recyclerFollowing.setVisibility(View.GONE);
        tvNoFollowers.setVisibility(View.VISIBLE);
        ddLoading.hideProgress();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}