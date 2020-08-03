package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.InProfileCreatedVideo;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.InProfileCreatedVideoAdapter;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CreatedVideoFragment extends Fragment {

    private View view;
    private RecyclerView recyclerVideos;
    private List<InProfileCreatedVideo> inProfileCreatedVideoList;
    private InProfileCreatedVideoAdapter inProfileCreatedVideoAdapter;
    private FirebaseFirestore db;
    private Master master;
    private TextView tvNoVideos;

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
        recyclerVideos.setHasFixedSize(true);
        tvNoVideos = view.findViewById(R.id.tvNoVideos);
        db = FirebaseFirestore.getInstance();
        master = new Master(getContext());
        SharedPreferences preferences = getActivity().getSharedPreferences("videoPref", 0);

        if (preferences.getString("profile_type", "none").equals("public")) {
            fetchCreatedVideos(preferences.getString("user_id", "none"));

        } else if (preferences.getString("profile_type", "none").equals("private")) {
            fetchCreatedVideos(master.getId());
        }


    }

    private void fetchCreatedVideos(String id) {
        inProfileCreatedVideoList = new ArrayList<>();
        recyclerVideos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerVideos.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));
        Query query = db.collection("videos");
        query.whereEqualTo("user_id", id)
                .whereEqualTo("video_status", Constant.VIDEO_STATUS_PUBLIC)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()){
                            tvNoVideos.setVisibility(View.VISIBLE);
                            recyclerVideos.setVisibility(View.GONE);
                        } else {
                            tvNoVideos.setVisibility(View.GONE);
                            recyclerVideos.setVisibility(View.VISIBLE);
                            inProfileCreatedVideoList.clear();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                InProfileCreatedVideo inProfileCreatedVideo = new InProfileCreatedVideo();
                                inProfileCreatedVideo.setId(snapshot.getString("id"));
                                inProfileCreatedVideo.setViews(snapshot.getString("view_count"));
                                inProfileCreatedVideo.setThumbnail(snapshot.getString("id"));
                                inProfileCreatedVideo.setUser_id(snapshot.getString("user_id"));
                                inProfileCreatedVideoList.add(inProfileCreatedVideo);
                            }
                            
                            inProfileCreatedVideoAdapter = new InProfileCreatedVideoAdapter(getContext(), inProfileCreatedVideoList);
                            inProfileCreatedVideoAdapter.notifyDataSetChanged();
                            recyclerVideos.setAdapter(inProfileCreatedVideoAdapter);
                            
                        }
                    }
                });
    }
}
