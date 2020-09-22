package com.tetravalstartups.dingdong.modules.discover;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

public class MostLikedActivity extends AppCompatActivity implements MostLikedVideoPresenter.IMostLikeVideo, View.OnClickListener {
    private RecyclerView recyclerVideos;
    private ImageView ivGoBack;
    private TextView tvVideoCount;
    private LinearLayout lvLoadingData;
    private LinearLayout lvNoVideos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_liked);
        initViews();
    }

    private void initViews() {
        recyclerVideos = findViewById(R.id.recyclerVideos);
        ivGoBack = findViewById(R.id.ivGoBack);
        tvVideoCount = findViewById(R.id.tvVideoCount);
        lvLoadingData = findViewById(R.id.lvLoadingData);
        lvNoVideos = findViewById(R.id.lvNoVideos);
        ivGoBack.setOnClickListener(this);
        recyclerVideos.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerVideos.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));


        fetechData();
    }

    private void fetechData() {
        MostLikedVideoPresenter mostLikedVideoPresenter = new MostLikedVideoPresenter(MostLikedActivity.this, MostLikedActivity.this);
        mostLikedVideoPresenter.fetchLikeVideo(50);
        lvLoadingData.setVisibility(View.VISIBLE);

    }

    @Override
    public void fetchResponse(MostLikedVideo mostLikedVideo) {
        MostLikedSeeMoreAdapter mostLikedSeeMoreAdapter = new MostLikedSeeMoreAdapter(MostLikedActivity.this, mostLikedVideo.getData());
        mostLikedSeeMoreAdapter.notifyDataSetChanged();
        tvVideoCount.setText(mostLikedVideo.getData().size()+" Videos");
        recyclerVideos.setAdapter(mostLikedSeeMoreAdapter);

        lvLoadingData.setVisibility(View.GONE);
        lvNoVideos.setVisibility(View.GONE);
    }

    @Override
    public void fetchError(String error) {
        lvLoadingData.setVisibility(View.GONE);
        lvNoVideos.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack){
            onBackPressed();
        }
    }
}
