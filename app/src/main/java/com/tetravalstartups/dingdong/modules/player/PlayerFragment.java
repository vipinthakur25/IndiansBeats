package com.tetravalstartups.dingdong.modules.player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public int lastPosition = -1;
    private View view;
    private RecyclerView recyclerVideos;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Video> videoList = new ArrayList<>();
    PlayerAdapter playerAdapter;
    private TextView tvTrending, tvFollowing;
    private ImageView ivWhatsapp;
    private DDLoading ddLoading;

    // recyclerview configuration
    private int limit = 30;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;

    private SharedPreferences preferences;

    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();


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
        mSwipeRefreshLayout.setOnRefreshListener(this);

        tvFollowing = view.findViewById(R.id.tvFollowing);
        tvTrending = view.findViewById(R.id.tvTrending);

        ivWhatsapp = view.findViewById(R.id.ivWhatsapp);
        ivWhatsapp.setOnClickListener(this);

        preferences = getContext().getSharedPreferences("video_index", 0);

        YoYo.with(Techniques.Pulse)
                .duration(2500)
                .repeat(YoYo.INFINITE)
                .playOn(ivWhatsapp);

        ddLoading = DDLoading.getInstance();
        mSwipeRefreshLayout.setRefreshing(true);
        setupAdapter();
    }

    private void setupAdapter() {

        CollectionReference videosRef = rootRef.collection("videos");
        Query query = videosRef.whereEqualTo("video_status", Constant.VIDEO_STATUS_PUBLIC)
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(limit);

        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        playerAdapter = new PlayerAdapter(getContext(), videoList);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        Toast.makeText(getContext(), "No Videos", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        videoList.clear();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Video video = snapshot.toObject(Video.class);
                            videoList.add(video);
                        }
                        recyclerVideos.setAdapter(playerAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                        playerAdapter.notifyDataSetChanged();
                        lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });

        recyclerVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();

                if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                    isScrolling = false;
                    mSwipeRefreshLayout.setRefreshing(true);
                    Query nextQuery = videosRef.whereEqualTo("video_status", Constant.VIDEO_STATUS_PUBLIC)
                            .orderBy("timestamp", Query.Direction.DESCENDING)
                            .startAfter(lastVisible).limit(limit);
                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> t) {
                            if (t.isSuccessful()) {
                                for (DocumentSnapshot d : t.getResult()) {
                                    Video video = d.toObject(Video.class);
                                    videoList.add(video);
                                }
                                playerAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                                lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);

                                if (t.getResult().size() < limit) {
                                    isLastItemReached = true;
                                }
                            }
                        }
                    });
                }

            }
        });

    }

    private void shareOnWhatsapp() {
        try {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.whatsapp");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey your friend is using *Ding Dong* which is an *Hybrid video sharing app*. Here is the *download* link:\nhttps://bit.ly/33bQke3\n\n*Create . Share . Earn*");
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.whatsapp.w4b");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey your friend is using *Ding Dong* which is an *Hybrid video sharing app*. Here is the *download* link:\nhttps://bit.ly/33bQke3\n\n*Create . Share . Earn*");
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == ivWhatsapp) {
            shareOnWhatsapp();
        }
    }


    @Override
    public void onRefresh() {
        setupAdapter();
    }
}

