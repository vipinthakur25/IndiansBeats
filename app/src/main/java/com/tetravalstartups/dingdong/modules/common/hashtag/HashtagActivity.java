package com.tetravalstartups.dingdong.modules.common.hashtag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.common.hashtag.model.TaggedVideos;
import com.tetravalstartups.dingdong.modules.common.hashtag.presenter.HashtagPresenter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

public class HashtagActivity extends AppCompatActivity implements View.OnClickListener, HashtagPresenter.IHashtag {

    private ImageView ivGoBack;
    private TextView tvHashtag;
    private TextView tvVideoCount;
    private LinearLayout lvLoadingData;
    private LinearLayout lvNoVideos;
    private RecyclerView recyclerVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);
        tvHashtag = findViewById(R.id.tvHashtag);
        tvVideoCount = findViewById(R.id.tvVideoCount);
        lvLoadingData = findViewById(R.id.lvLoadingData);
        lvNoVideos = findViewById(R.id.lvNoVideos);
        recyclerVideos = findViewById(R.id.recyclerVideos);
        setData();
    }

    private void setData() {
        String hashtag = getIntent().getStringExtra("data");
        SharedPreferences preferences = getSharedPreferences("tags", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tags", hashtag);
        editor.apply();
        tvHashtag.setText(hashtag);
        lvLoadingData.setVisibility(View.VISIBLE);
        fetchVideos(hashtag);
    }

    private void fetchVideos(String hashtag) {
        HashtagPresenter hashtagPresenter = new HashtagPresenter(HashtagActivity.this, HashtagActivity.this);
        hashtagPresenter.fetchVideos(hashtag);
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void taggedVideosFetch(TaggedVideos taggedVideos) {
        tvVideoCount.setText(taggedVideos.getData().size()+" Videos");
        recyclerVideos.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerVideos.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));
        HashtagAdapter hashtagAdapter = new HashtagAdapter(HashtagActivity.this, taggedVideos.getData());
        hashtagAdapter.notifyDataSetChanged();
        recyclerVideos.setAdapter(hashtagAdapter);
        lvLoadingData.setVisibility(View.GONE);
        recyclerVideos.setVisibility(View.VISIBLE);
    }

    @Override
    public void taggedVideosNotFound(String message) {
        recyclerVideos.setVisibility(View.GONE);
        lvLoadingData.setVisibility(View.GONE);
        lvNoVideos.setVisibility(View.VISIBLE);
    }

    @Override
    public void taggedVideosFetchFail(String error) {
        recyclerVideos.setVisibility(View.GONE);
        lvLoadingData.setVisibility(View.GONE);
        lvNoVideos.setVisibility(View.VISIBLE);
    }
}