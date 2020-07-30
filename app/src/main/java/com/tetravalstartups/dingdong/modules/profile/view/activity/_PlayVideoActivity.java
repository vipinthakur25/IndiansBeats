package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.BaseActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.modules.home.video.VideoAdapter;
import com.tetravalstartups.dingdong.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class _PlayVideoActivity extends BaseActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerVideos;
    private VideoAdapter videoAdapter;
    private FirebaseAuth auth;
    private Master master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparentFlag();
        setContentView(R.layout.activity_play_video);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        recyclerVideos = findViewById(R.id.recyclerVideos);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        master = new Master(_PlayVideoActivity.this);
        int pos = Integer.parseInt(getIntent().getStringExtra("pos"));
        String video_type = getIntent().getStringExtra("video_type");
        if (video_type.equals("created")){

            fetchCreatedVideos(pos);

        } else if (video_type.equals("liked")) {

            SharedPreferences preferences = getSharedPreferences("videoPref", 0);
            if (preferences.getString("profile_type", "none").equals("public")) {

                fetchLikedVideos(pos,preferences.getString("user_id", "none"));

            } else if (preferences.getString("profile_type", "none").equals("private")) {

                fetchLikedVideos(pos, master.getId());

            }

        } else if (video_type.equals("draft")) {
            fetchDraftVideos(pos);
        }

    }

    private void fetchDraftVideos(int pos) {
        Query query = db.collection("videos");
        List<Video> privateDraftVideosList = new ArrayList<>();
        query.whereEqualTo("user_id", master.getId())
                .whereEqualTo("video_status", Constants.VIDEO_STATUS_PRIVATE)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()){
                        } else {
                            privateDraftVideosList.clear();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                Video video = new Video();
                                video.setId(snapshot.getString("id"));
                                video.setVideo_desc(snapshot.getString("video_desc"));
                                video.setSound_id(snapshot.getString("sound_id"));
                                video.setSound_title(snapshot.getString("sound_title"));
                                video.setLikes_count(snapshot.getString("likes_count"));
                                video.setShare_count(snapshot.getString("share_count"));
                                video.setComment_count(snapshot.getString("comment_count"));
                                video.setView_count(snapshot.getString("view_count"));
                                video.setUser_id(snapshot.getString("user_id"));
                                video.setUser_handle(snapshot.getString("user_handle"));
                                video.setUser_photo(snapshot.getString("user_photo"));
                                video.setVideo_thumbnail(snapshot.getString("video_thumbnail"));
                                video.setVideo_status(snapshot.getString("video_status"));
                                video.setVideo_status(snapshot.getString("video_status"));
                                privateDraftVideosList.add(video);
                            }

                            videoAdapter = new VideoAdapter(_PlayVideoActivity.this, privateDraftVideosList);
                            videoAdapter.notifyDataSetChanged();
                            SnapHelper snapHelper = new PagerSnapHelper();
                            recyclerVideos.setLayoutManager(new LinearLayoutManager(_PlayVideoActivity.this));
                            recyclerVideos.scrollToPosition(pos);
                            if (recyclerVideos.getOnFlingListener() == null)
                                snapHelper.attachToRecyclerView(recyclerVideos);
                            recyclerVideos.setAdapter(videoAdapter);
                        }
                    }
                });


    }

    private void fetchLikedVideos(int pos, String id) {
        FirebaseFirestore likedDB = FirebaseFirestore.getInstance();
        Query query = likedDB.collection("users").document(id).collection("liked_videos").orderBy("id", Query.Direction.ASCENDING);
        List<Video> likedVideosList = new ArrayList<>();
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        likedVideosList.clear();
                        for (DocumentSnapshot snapshot : task.getResult()) {
                           //og.e("taskresult", snapshot.toString());
                                                    db.collection("videos")
                                .document(Objects.requireNonNull(snapshot.getString("id")))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(_PlayVideoActivity.this, "task success", Toast.LENGTH_SHORT).show();
                                            Video video = task.getResult().toObject(Video.class);
                                            Log.e("taskresult", task.getResult().getString("id"));
                                            likedVideosList.add(video);
                                        }

                                        videoAdapter = new VideoAdapter(_PlayVideoActivity.this, likedVideosList);
                                        videoAdapter.notifyDataSetChanged();
                                        SnapHelper snapHelper = new PagerSnapHelper();
                                        recyclerVideos.setLayoutManager(new LinearLayoutManager(_PlayVideoActivity.this));
                                        recyclerVideos.scrollToPosition(pos);
                                        if (recyclerVideos.getOnFlingListener() == null)
                                            snapHelper.attachToRecyclerView(recyclerVideos);
                                        recyclerVideos.setAdapter(videoAdapter);
                                    }
                                });
                        }
                    }
                });
    }

//    {
//        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (queryDocumentSnapshots.getDocuments().isEmpty()) {
//                } else {
//
//                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
//                        db.collection("videos")
//                                .document(Objects.requireNonNull(snapshot.getString("id")))
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        likedVideosList.clear();
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(PlayVideoActivity.this, "task success", Toast.LENGTH_SHORT).show();
//                                            Video video = task.getResult().toObject(Video.class);
//                                            Log.e("taskresult", task.getResult().getString("id"));
//                                            likedVideosList.add(video);
//                                        }
//
//                                        videoAdapter = new VideoAdapter(PlayVideoActivity.this, likedVideosList);
//                                        videoAdapter.notifyDataSetChanged();
//                                        SnapHelper snapHelper = new PagerSnapHelper();
//                                        recyclerVideos.setLayoutManager(new LinearLayoutManager(PlayVideoActivity.this));
//                                        recyclerVideos.scrollToPosition(pos);
//                                        if (recyclerVideos.getOnFlingListener() == null)
//                                            snapHelper.attachToRecyclerView(recyclerVideos);
//                                        recyclerVideos.setAdapter(videoAdapter);
//
//                                    }
//                                });
//
//
//                    }
//
//                }
//
//            }
//
//        });
//
//    }


    private void fetchCreatedVideos(int pos) {

        db = FirebaseFirestore.getInstance();
        List<Video> videoList = new ArrayList<>();

        Query query = db.collection("videos");
        query.whereEqualTo("user_id", getIntent().getStringExtra("user_id"))
                .whereEqualTo("video_status", Constants.VIDEO_STATUS_PUBLIC)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().getDocuments().isEmpty()){
                                Toast.makeText(_PlayVideoActivity.this, "No Videos", Toast.LENGTH_SHORT).show();
                            } else {

                                videoList.clear();
                                for (DocumentSnapshot snapshot : task.getResult()) {
                                    Video video = new Video();
                                    video.setId(snapshot.getString("id"));
                                    video.setVideo_desc(snapshot.getString("video_desc"));
                                    video.setSound_id(snapshot.getString("sound_id"));
                                    video.setSound_title(snapshot.getString("sound_title"));
                                    video.setLikes_count(snapshot.getString("likes_count"));
                                    video.setShare_count(snapshot.getString("share_count"));
                                    video.setComment_count(snapshot.getString("comment_count"));
                                    video.setView_count(snapshot.getString("view_count"));
                                    video.setUser_id(snapshot.getString("user_id"));
                                    video.setUser_handle(snapshot.getString("user_handle"));
                                    video.setUser_photo(snapshot.getString("user_photo"));
                                    video.setVideo_thumbnail(snapshot.getString("video_thumbnail"));
                                    video.setVideo_status(snapshot.getString("video_status"));
                                    video.setVideo_status(snapshot.getString("video_status"));
                                    videoList.add(video);
                                }

                                videoAdapter = new VideoAdapter(_PlayVideoActivity.this, videoList);
                                videoAdapter.notifyDataSetChanged();
                                SnapHelper snapHelper = new PagerSnapHelper();
                                recyclerVideos.setLayoutManager(new LinearLayoutManager(_PlayVideoActivity.this));
                                recyclerVideos.scrollToPosition(pos);
                                if (recyclerVideos.getOnFlingListener() == null)
                                    snapHelper.attachToRecyclerView(recyclerVideos);
                                recyclerVideos.setAdapter(videoAdapter);
                            }
                        }
                    }
                });


    }

}