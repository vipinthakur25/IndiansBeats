package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.InProfileCreatedVideo;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.InProfileCreatedVideoAdapter;
import com.tetravalstartups.dingdong.utils.Constants;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CreatedVideoFragment extends Fragment {

    private View view;
    private RecyclerView recyclerCreatedVideo;
    private List<InProfileCreatedVideo> inProfileCreatedVideoList;
    private InProfileCreatedVideoAdapter inProfileCreatedVideoAdapter;
    private FirebaseFirestore db;
    private Master master;

    public CreatedVideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_created_video, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerCreatedVideo = view.findViewById(R.id.recyclerCreatedVideo);
        db = FirebaseFirestore.getInstance();
        master = new Master(getContext());
        fetchCreatedVideos();
    }

    private void fetchCreatedVideos() {
        inProfileCreatedVideoList = new ArrayList<>();
        recyclerCreatedVideo.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerCreatedVideo.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));
        Query query = db.collection("videos");
        query.whereEqualTo("user_id", master.getId())
                .whereEqualTo("video_status", Constants.VIDEO_STATUS_PUBLIC)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()){
                            Toast.makeText(getContext(), "No Videos", Toast.LENGTH_SHORT).show();
                        } else {
                            inProfileCreatedVideoList.clear();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                InProfileCreatedVideo inProfileCreatedVideo = new InProfileCreatedVideo();
                                inProfileCreatedVideo.setId(snapshot.getString("id"));
                                inProfileCreatedVideo.setViews("10K");
                                inProfileCreatedVideo.setThumbnail(snapshot.getString("id"));
                                inProfileCreatedVideoList.add(inProfileCreatedVideo);
                            }
                            
                            inProfileCreatedVideoAdapter = new InProfileCreatedVideoAdapter(getContext(), inProfileCreatedVideoList);
                            inProfileCreatedVideoAdapter.notifyDataSetChanged();
                            recyclerCreatedVideo.setAdapter(inProfileCreatedVideoAdapter);
                        }
                    }
                });
    }
}
