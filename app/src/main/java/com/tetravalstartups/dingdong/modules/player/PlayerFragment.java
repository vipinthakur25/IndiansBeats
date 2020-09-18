package com.tetravalstartups.dingdong.modules.player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.api.UserInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;
import com.tetravalstartups.dingdong.modules.profile.videos.created.CreatedVideo;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public int lastPosition = -1;
    private View view;
    private RecyclerView recyclerVideos;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<VideoResponseDatum> videoList = new ArrayList<>();
    PlayerAdapter playerAdapter;
    private TextView tvTrending, tvFollowing;
    private DDLoading ddLoading;
    private SharedPreferences preferences;
    private Master master;

    private RequestInterface requestInterface;
    private UserInterface userInterface;
    private LinearLayout lhVideoType;
    private FirebaseAuth firebaseAuth;


    public PlayerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player, container, false);
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        userInterface = APIClient.getRetrofitInstance().create(UserInterface.class);
        initView();
        return view;
    }

    private void initView() {
        recyclerVideos = view.findViewById(R.id.recyclerVideos);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        tvFollowing = view.findViewById(R.id.tvFollowing);
        tvTrending = view.findViewById(R.id.tvTrending);
        tvFollowing.setOnClickListener(this);
        tvTrending.setOnClickListener(this);

        preferences = getContext().getSharedPreferences("video_index", 0);

        master = new Master(getContext());

        ddLoading = DDLoading.getInstance();

        lhVideoType = view.findViewById(R.id.lhVideoType);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            lhVideoType.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setRefreshing(true);
        fetchTrendingVideos();
    }


    @Override
    public void onClick(View v) {
        if (v == tvFollowing) {
            tvFollowing.setTextColor(getResources().getColor(R.color.colorGradientStart));
            tvTrending.setTextColor(getResources().getColor(R.color.colorDisable));
            fetchUserFollowingVideos();
        }

        if (v == tvTrending) {
            tvFollowing.setTextColor(getResources().getColor(R.color.colorDisable));
            tvTrending.setTextColor(getResources().getColor(R.color.colorGradientStart));
            fetchTrendingVideos();
        }
    }

    private void fetchUserFollowingVideos() {
        mSwipeRefreshLayout.setRefreshing(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        SnapHelper snapHelper = new PagerSnapHelper();
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        Call<CreatedVideo> call = userInterface.getUserFollowingVideos(master.getId(), 250, 0);
        call.enqueue(new Callback<CreatedVideo>() {
            @Override
            public void onResponse(Call<CreatedVideo> call, Response<CreatedVideo> response) {
                if (response.code() == 200) {
                    CreatedVideo createdVideo = response.body();
                    List<VideoResponseDatum> createdVideosArrayList = new ArrayList<>(createdVideo.getData());
                    playerAdapter = new PlayerAdapter(getContext(), createdVideosArrayList);
                    playerAdapter.notifyDataSetChanged();
                    recyclerVideos.setAdapter(playerAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getContext(), "No Videos", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CreatedVideo> call, Throwable t) {

            }
        });
    }

    private void fetchTrendingVideos() {
        mSwipeRefreshLayout.setRefreshing(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        SnapHelper snapHelper = new PagerSnapHelper();
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        Call<CreatedVideo> call = requestInterface.getAllVideos(master.getId(), 250);
        call.enqueue(new Callback<CreatedVideo>() {
            @Override
            public void onResponse(Call<CreatedVideo> call, Response<CreatedVideo> response) {
                if (response.code() == 200) {
                    CreatedVideo createdVideo = response.body();
                    List<VideoResponseDatum> createdVideosArrayList = new ArrayList<>(createdVideo.getData());
                    playerAdapter = new PlayerAdapter(getContext(), createdVideosArrayList);
                    playerAdapter.notifyDataSetChanged();
                    recyclerVideos.setAdapter(playerAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getContext(), "No Videos", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CreatedVideo> call, Throwable t) {

            }
        });
    }




    @Override
    public void onRefresh() {
        fetchTrendingVideos();
    }
}

