package com.tetravalstartups.dingdong.modules.player;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerFragment extends Fragment {

    private View view;
    private RecyclerView recyclerVideos;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference videoRef = mFirestore.collection("videos");
    private Query query = videoRef.whereEqualTo("video_status", Constants.VIDEO_STATUS_PUBLIC);
    public int lastPosition = -1;
    private List<Video> videoList = new ArrayList<>();

    public PlayerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerVideos = view.findViewById(R.id.recyclerVideos);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        setupAdapter();
    }

    private void setupAdapter() {
        // Init Paging Configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
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
                                getContext(),
                                "Error Occurred!",
                                Toast.LENGTH_SHORT
                        ).show();

                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }

        };

        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        recyclerVideos.setAdapter(mAdapter);


    }

}