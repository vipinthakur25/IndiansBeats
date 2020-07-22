package com.tetravalstartups.dingdong.modules.profile.view.fragment;

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
import com.tetravalstartups.dingdong.modules.profile.model.LikedVideos;
import com.tetravalstartups.dingdong.modules.profile.model.PrivateDraftVideos;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.LikedVideoAdapter;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.PrivateDraftVideoAdapter;
import com.tetravalstartups.dingdong.utils.Constants;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PrivateDraftFragment extends Fragment {

    private View view;
    private RecyclerView recyclerVideos;
    private List<PrivateDraftVideos> privateDraftVideosList;
    private PrivateDraftVideoAdapter privateDraftVideoAdapter;
    private FirebaseFirestore db;
    private Master master;
    private TextView tvNoVideos;

    public PrivateDraftFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_private_draft, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerVideos = view.findViewById(R.id.recyclerVideos);
        tvNoVideos = view.findViewById(R.id.tvNoVideos);
        db = FirebaseFirestore.getInstance();
        master = new Master(getContext());
        fetchCreatedVideos();
    }

    private void fetchCreatedVideos() {
        privateDraftVideosList = new ArrayList<>();
        recyclerVideos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerVideos.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));
        Query query = db.collection("videos");
        query.whereEqualTo("user_id", master.getId())
                .whereEqualTo("video_status", Constants.VIDEO_STATUS_PRIVATE)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()){
                            tvNoVideos.setVisibility(View.VISIBLE);
                            recyclerVideos.setVisibility(View.GONE);
                        } else {
                            tvNoVideos.setVisibility(View.GONE);
                            recyclerVideos.setVisibility(View.VISIBLE);
                            privateDraftVideosList.clear();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                PrivateDraftVideos videos = new PrivateDraftVideos();
                                videos.setId(snapshot.getString("id"));
                                videos.setViews(snapshot.getString("view_count"));
                                videos.setThumbnail(snapshot.getString("id"));
                                privateDraftVideosList.add(videos);
                            }

                            privateDraftVideoAdapter = new PrivateDraftVideoAdapter(getContext(), privateDraftVideosList);
                            privateDraftVideoAdapter.notifyDataSetChanged();
                            recyclerVideos.setAdapter(privateDraftVideoAdapter);
                        }
                    }
                });
    }

}