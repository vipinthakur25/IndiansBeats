package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;
import com.tetravalstartups.dingdong.modules.profile.videos.created.CreatedVideo;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.InProfileCreatedVideoAdapter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatedVideoFragment extends Fragment {

    private View view;
    private RecyclerView recyclerVideos;
    private List<VideoResponseDatum> createdVideosArrayList;
    private InProfileCreatedVideoAdapter inProfileCreatedVideoAdapter;
    private FirebaseFirestore db;
    private Master master;
    private TextView tvNoVideos;

    private RequestInterface requestInterface;
    private static final String TAG = "CreatedVideoFragment";

    public CreatedVideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_created_video, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerVideos = view.findViewById(R.id.recyclerVideos);
        tvNoVideos = view.findViewById(R.id.tvNoVideos);

        db = FirebaseFirestore.getInstance();
        master = new Master(getContext());

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(receiver, new IntentFilter("deleteVideo"));

        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        fetchCreatedVideos(getArguments().getString("user_id"));
    }

    public void fetchCreatedVideos(String id) {
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerVideos.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));

        Call<CreatedVideo> call = requestInterface.getVideoResponse(id, master.getId());
        call.enqueue(new Callback<CreatedVideo>() {
            @Override
            public void onResponse(Call<CreatedVideo> call, Response<CreatedVideo> response) {
                if (response.code() == 200) {
                    CreatedVideo createdVideo = response.body();
                    createdVideosArrayList = new ArrayList<>(createdVideo.getData());
                    inProfileCreatedVideoAdapter = new InProfileCreatedVideoAdapter(getContext(), createdVideosArrayList);
                    inProfileCreatedVideoAdapter.notifyDataSetChanged();
                    recyclerVideos.setAdapter(inProfileCreatedVideoAdapter);
                    tvNoVideos.setVisibility(View.GONE);
                    recyclerVideos.setVisibility(View.VISIBLE);
                } else {
                    tvNoVideos.setVisibility(View.VISIBLE);
                    recyclerVideos.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CreatedVideo> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String code = intent.getStringExtra("code");
                if (code.equals("200")) {
                    fetchCreatedVideos(getArguments().getString("user_id"));
                }
            }
        }
    };

}
