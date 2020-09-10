package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.model.followers.FollowersResponse;
import com.tetravalstartups.dingdong.modules.profile.presenter.FollowerPresenter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.FollowerAdapter;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.List;

public class FollowersActivity extends AppCompatActivity implements FollowerPresenter.IFollower, View.OnClickListener {

    private ImageView ivGoBack;
    private RecyclerView recyclerFollowers;
    private TextView tvNoFollowers;
    private DDLoading ddLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        recyclerFollowers = findViewById(R.id.recyclerFollowers);
        recyclerFollowers.setLayoutManager(new LinearLayoutManager(FollowersActivity.this));
        recyclerFollowers.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.VERTICAL));

        tvNoFollowers = findViewById(R.id.tvNoFollowers);

        ddLoading = DDLoading.getInstance();
        fetchFollowers();
    }

    public void fetchFollowers() {
        ddLoading.showProgress(FollowersActivity.this, "Hold On...", false);
        FollowerPresenter followerPresenter = new FollowerPresenter(FollowersActivity.this, FollowersActivity.this);
        followerPresenter.fetchFollowers(getIntent().getStringExtra("user_id"));
    }


    @Override
    public void followerFetchSuccess(List<FollowersResponse> followersList) {
        FollowerAdapter followerAdapter = new FollowerAdapter(FollowersActivity.this, followersList);
        followerAdapter.notifyDataSetChanged();
        recyclerFollowers.setAdapter(followerAdapter);
        tvNoFollowers.setVisibility(View.GONE);
        recyclerFollowers.setVisibility(View.VISIBLE);
        ddLoading.hideProgress();
    }

    @Override
    public void followerFetchError(String error) {
        tvNoFollowers.setVisibility(View.VISIBLE);
        recyclerFollowers.setVisibility(View.GONE);
        ddLoading.hideProgress();
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
