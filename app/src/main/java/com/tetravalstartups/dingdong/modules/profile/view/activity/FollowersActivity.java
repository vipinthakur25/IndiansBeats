package com.tetravalstartups.dingdong.modules.profile.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.Followers;
import com.tetravalstartups.dingdong.modules.profile.presenter.FollowerPresenter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.FollowerAdapter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.List;

public class FollowersActivity extends AppCompatActivity implements FollowerPresenter.IFollower, View.OnClickListener {

    private ImageView ivGoBack, ivSettings;
    private RecyclerView recyclerFollowers;
    private TextView tvNoFollowers;
    private ProgressBar progressBar;
    private Master master;
    private String profile_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);
        ivSettings = findViewById(R.id.ivSettings);
        progressBar = findViewById(R.id.progressBar);
        recyclerFollowers = findViewById(R.id.recyclerFollowers);
        recyclerFollowers.setLayoutManager(new LinearLayoutManager(FollowersActivity.this));
        recyclerFollowers.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.VERTICAL));
        tvNoFollowers = findViewById(R.id.tvNoFollowers);
        master = new Master(FollowersActivity.this);
        fetchFollowers();
    }

    private void fetchFollowers() {
        FollowerPresenter followerPresenter =
                new FollowerPresenter(FollowersActivity.this,
                FollowersActivity.this);
        if (getIntent().hasExtra("user_id")) {
            profile_type = "public";
            followerPresenter.fetchFollowers(getIntent().getStringExtra("user_id"));
        } else {
            profile_type = "private";
            followerPresenter.fetchFollowers(master.getId());
        }

    }


    @Override
    public void followerFetchSuccess(List<Followers> followersList) {
        FollowerAdapter followerAdapter = new
                FollowerAdapter(FollowersActivity.this,
                followersList, profile_type);
        followerAdapter.notifyDataSetChanged();
        recyclerFollowers.setAdapter(followerAdapter);
        progressBar.setVisibility(View.INVISIBLE);
        tvNoFollowers.setVisibility(View.GONE);
        recyclerFollowers.setVisibility(View.VISIBLE);
    }

    @Override
    public void followerFetchError(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        tvNoFollowers.setVisibility(View.VISIBLE);
        recyclerFollowers.setVisibility(View.GONE);
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
