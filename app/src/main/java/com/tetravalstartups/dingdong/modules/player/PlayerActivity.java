package com.tetravalstartups.dingdong.modules.player;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.BaseActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.CommonInterface;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.common.hashtag.model.TaggedVideos;
import com.tetravalstartups.dingdong.modules.common.hashtag.model.TaggedVideosResponse;
import com.tetravalstartups.dingdong.modules.discover.MostLikedVideo;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;
import com.tetravalstartups.dingdong.modules.profile.videos.created.CreatedVideo;
import com.tetravalstartups.dingdong.modules.profile.videos.liked.LikedVideos;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PlayerActivity";
    private View view;
    private RecyclerView recyclerVideos;
    private PlayerAdapter playerAdapter;
    private DDLoading ddLoading;
    private FirebaseFirestore db;
    private String user_id, video_type, pos;
    private RequestInterface requestInterface;
    private CommonInterface commonInterface;
    private TextView tvTitle;
    private ImageView ivGoBack;
    private Master master;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
    }

    private void initView() {
        recyclerVideos = findViewById(R.id.recyclerVideos);
        tvTitle = findViewById(R.id.tvTitle);
        ivGoBack = findViewById(R.id.ivGoBack);

        ivGoBack.setOnClickListener(this);
        ddLoading = DDLoading.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        commonInterface = APIClient.getRetrofitInstance().create(CommonInterface.class);

        user_id = getIntent().getStringExtra("user_id");
        video_type = getIntent().getStringExtra("video_type");
        pos = getIntent().getStringExtra("pos");
        master = new Master(PlayerActivity.this);

        if (video_type.equals("created")) {
            tvTitle.setText("Created");
            setupCreatedVideoAdapter();
        } else if (video_type.equals("liked")) {
            tvTitle.setText("Liked");
            setupLikedVideoAdapter();
        } else if (video_type.equals("hashtag")) {
            tvTitle.setText("Popular");
            fetchTaggedVideos();
        } else if (video_type.equals("most_liked")) {
            tvTitle.setText("Most Liked");
            fetchMostLikedVideos();
        } else if (video_type.equals("most_viewed")) {
            tvTitle.setText("Most Viewed");
            fetchMostViewedVideos();
        }

    }

    private void setupCreatedVideoAdapter() {
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        Call<CreatedVideo> call;
        if (firebaseAuth.getCurrentUser() != null) {
            call = requestInterface.getVideoResponse(user_id, master.getId());
        } else {
            call = requestInterface.getVideoResponse(user_id, "0");
        }
        call.enqueue(new Callback<CreatedVideo>() {
            @Override
            public void onResponse(Call<CreatedVideo> call, Response<CreatedVideo> response) {
                if (response.code() == 200) {
                    playerAdapter = new PlayerAdapter(PlayerActivity.this, response.body().getData());
                    playerAdapter.notifyDataSetChanged();
                    recyclerVideos.setAdapter(playerAdapter);
                    recyclerVideos.scrollToPosition(Integer.parseInt(pos));
                }
            }

            @Override
            public void onFailure(Call<CreatedVideo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setupLikedVideoAdapter() {
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);

        Call<LikedVideos> call = requestInterface.getLikedVideoResponse(user_id);
        call.enqueue(new Callback<LikedVideos>() {
            @Override
            public void onResponse(Call<LikedVideos> call, Response<LikedVideos> response) {
                if (response.code() == 200) {
                    LikedVideos likedVideos = response.body();
                    List<VideoResponseDatum> likedVideosList = new ArrayList<>(likedVideos.getData());
                    playerAdapter = new PlayerAdapter(PlayerActivity.this, likedVideosList);
                    playerAdapter.notifyDataSetChanged();
                    recyclerVideos.setAdapter(playerAdapter);
                    recyclerVideos.scrollToPosition(Integer.parseInt(pos));
                }
            }

            @Override
            public void onFailure(Call<LikedVideos> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void fetchTaggedVideos() {
        SharedPreferences preferences = getSharedPreferences("tags", 0);
        String tags = preferences.getString("tags", "#dingdong");
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        Call<TaggedVideos> call;
        if (firebaseAuth.getCurrentUser() != null) {
            call = commonInterface.fetchTaggedVideos(master.getId(), tags);
        } else {
            call = commonInterface.fetchTaggedVideos("0", tags);
        }

        call.enqueue(new Callback<TaggedVideos>() {
            @Override
            public void onResponse(Call<TaggedVideos> call, Response<TaggedVideos> response) {
                if (response.code() == 200) {
                    playerAdapter = new PlayerAdapter(PlayerActivity.this, response.body().getData());
                    playerAdapter.notifyDataSetChanged();
                    recyclerVideos.setAdapter(playerAdapter);
                    recyclerVideos.scrollToPosition(Integer.parseInt(pos));
                }
            }

            @Override
            public void onFailure(Call<TaggedVideos> call, Throwable t) {

            }
        });

    }

    private void fetchMostViewedVideos() {
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);

    }

    private void fetchMostLikedVideos() {
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        CommonInterface commonInterface = APIClient.getRetrofitInstance().create(CommonInterface.class);
        Call<MostLikedVideo> mostLikedVideoCall = commonInterface.mostLiked("", 20);
        mostLikedVideoCall.enqueue(new Callback<MostLikedVideo>() {
            @Override
            public void onResponse(Call<MostLikedVideo> call, Response<MostLikedVideo> response) {
                if (response.code() == 200) {
                    
                } else if (response.code() == 400) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<MostLikedVideo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
        }
    }
}