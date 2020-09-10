package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;
import com.tetravalstartups.dingdong.modules.profile.videos.liked.LikedVideos;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.LikedVideoAdapter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikedVideoFragment extends Fragment {

    private View view;
    private RecyclerView recyclerVideos;
    private List<VideoResponseDatum> likedVideosList;
    private LikedVideoAdapter likedVideoAdapter;
    private FirebaseFirestore db;
    private Master master;
    private TextView tvNoVideos;

    private RequestInterface requestInterface;

    public LikedVideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_liked_video, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerVideos = view.findViewById(R.id.recyclerVideos);
        tvNoVideos = view.findViewById(R.id.tvNoVideos);

        db = FirebaseFirestore.getInstance();
        master = new Master(getContext());

        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        fetchLikedVideos(getArguments().getString("user_id"));
    }


    private void fetchLikedVideos(String id) {
        recyclerVideos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerVideos.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));
        Call<LikedVideos> call = requestInterface.getLikedVideoResponse(id);
        call.enqueue(new Callback<LikedVideos>() {
            @Override
            public void onResponse(Call<LikedVideos> call, Response<LikedVideos> response) {
                if (response.code() == 200) {
                    likedVideoAdapter = new LikedVideoAdapter(getContext(), response.body().getData());
                    likedVideoAdapter.notifyDataSetChanged();
                    recyclerVideos.setAdapter(likedVideoAdapter);
                    tvNoVideos.setVisibility(View.GONE);
                    recyclerVideos.setVisibility(View.VISIBLE);
                } else {
                    tvNoVideos.setVisibility(View.VISIBLE);
                    recyclerVideos.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<LikedVideos> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}


