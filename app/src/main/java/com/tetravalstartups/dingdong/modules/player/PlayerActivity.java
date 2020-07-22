package com.tetravalstartups.dingdong.modules.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.Constants;

import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {

    private RecyclerView recyclerVideos;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference videoRef = mFirestore.collection("videos");
    private Query query = videoRef.whereEqualTo("video_status", Constants.VIDEO_STATUS_PUBLIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
    }

    private void initView() {
        recyclerVideos = findViewById(R.id.recyclerVideos);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        setupAdapter();
    }

    private void setupAdapter() {
        // Init Paging Configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(3)
                .build();

        // Init Adapter Configuration
        FirestorePagingOptions<Video> options = new FirestorePagingOptions.Builder<Video>()
                .setLifecycleOwner(this)
                .setQuery(query, config, Video.class)
                .build();

        FirestorePagingAdapter<Video, PlayerViewHolder> mAdapter = new FirestorePagingAdapter<Video, PlayerViewHolder>(options) {

            @NonNull
            @Override
            public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.video_list_item, parent, false);
                return new PlayerViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PlayerViewHolder holder, int position, @NonNull Video model) {
                holder.playVideo(model);
            }

            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                Log.e("MainActivity", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:

                    case FINISHED:
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        Toast.makeText(
                               PlayerActivity.this,
                                "Error Occurred!",
                                Toast.LENGTH_SHORT
                        ).show();

                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }

        };

        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(PlayerActivity.this));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        recyclerVideos.setAdapter(mAdapter);

    }
}