package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.LikedVideos;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.LikedVideoAdapter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LikedVideoFragment extends Fragment {

    private View view;
    private RecyclerView recyclerVideos;
    private List<LikedVideos> likedVideosList;
    private LikedVideoAdapter likedVideoAdapter;
    private FirebaseFirestore db;
    private Master master;
    private TextView tvNoVideos;

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
        SharedPreferences preferences = getActivity().getSharedPreferences("videoPref", 0);
        if (preferences.getString("profile_type", "none").equals("public")) {
            fetchCreatedVideos(preferences.getString("user_id", "none"));
            Toast.makeText(getContext(), "public", Toast.LENGTH_SHORT).show();

        } else if (preferences.getString("profile_type", "none").equals("private")) {
            Toast.makeText(getContext(), "private", Toast.LENGTH_SHORT).show();
            fetchCreatedVideos(master.getId());
        }
    }

    private void fetchCreatedVideos(String id) {
        likedVideosList = new ArrayList<>();

        recyclerVideos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerVideos.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.GRID));
        Query query = db.collection("users").document(id).collection("liked_videos").orderBy("id", Query.Direction.ASCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                    tvNoVideos.setVisibility(View.VISIBLE);
                    recyclerVideos.setVisibility(View.GONE);
                } else {
                    tvNoVideos.setVisibility(View.GONE);
                    recyclerVideos.setVisibility(View.VISIBLE);
                    likedVideosList.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        db.collection("videos")
                                .document(Objects.requireNonNull(snapshot.getString("id")))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.e("tagtag", task.getResult().getString("user_handle"));
                                            LikedVideos videos = new LikedVideos();
                                            videos.setId(task.getResult().getString("id"));
                                            videos.setViews(task.getResult().getString("view_count"));
                                            videos.setThumbnail(task.getResult().getString("id"));
                                            videos.setUser_id(task.getResult().getString("user_id"));
                                            likedVideosList.add(videos);
                                        }

                                        likedVideoAdapter = new LikedVideoAdapter(getContext(), likedVideosList);
                                        likedVideoAdapter.notifyDataSetChanged();
                                        recyclerVideos.setAdapter(likedVideoAdapter);

                                    }
                                });


                    }

                }

            }

        });
    }

}
