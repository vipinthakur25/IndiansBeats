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
import com.tetravalstartups.dingdong.modules.profile.model.Followers;
import com.tetravalstartups.dingdong.modules.profile.presenter.FollowerPresenter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.FollowerAdapter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.List;

public class FollowersActivity extends AppCompatActivity implements FollowerPresenter.IFollower {

    private ImageView ivGoBack, ivSettings;
    private RecyclerView recyclerFollowers;
    private TextView tvNoFollowers;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivSettings = findViewById(R.id.ivSettings);
        progressBar = findViewById(R.id.progressBar);
        recyclerFollowers = findViewById(R.id.recyclerFollowers);
        recyclerFollowers.setLayoutManager(new LinearLayoutManager(FollowersActivity.this));
        recyclerFollowers.addItemDecoration(new EqualSpacingItemDecoration(0, EqualSpacingItemDecoration.VERTICAL));
        tvNoFollowers = findViewById(R.id.tvNoFollowers);
        fetchFollowers();
    }

    private void fetchFollowers() {
        FollowerPresenter followerPresenter =
                new FollowerPresenter(FollowersActivity.this,
                FollowersActivity.this);
        followerPresenter.fetchFollowers();
    }


    @Override
    public void followerFetchSuccess(List<Followers> followersList) {
        FollowerAdapter followerAdapter = new
                FollowerAdapter(FollowersActivity.this,
                followersList);
        followerAdapter.notifyDataSetChanged();
        recyclerFollowers.setAdapter(followerAdapter);
        progressBar.setVisibility(View.GONE);
        tvNoFollowers.setVisibility(View.GONE);
    }

    @Override
    public void followerFetchError(String error) {
        progressBar.setVisibility(View.GONE);
        tvNoFollowers.setVisibility(View.VISIBLE);
    }


}
