package com.tetravalstartups.dingdong.modules.home.video;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VideoPresenter {

    private Context context;
    private IVideo iVideo;
    private FirebaseFirestore db;

    public VideoPresenter(Context context, IVideo iVideo) {
        this.context = context;
        this.iVideo = iVideo;
    }

    public interface IVideo {
        void fetchVideosSuccess(List<Video> videoList);

        void fetchVideosError(String error);
    }

    public void fetchVideos(){
        db = FirebaseFirestore.getInstance();
        List<Video> videoList = new ArrayList<>();

        db.collection("videos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshots.getDocuments().isEmpty()){
                            iVideo.fetchVideosError("No Videos");
                        } else {
                            for (DocumentSnapshot snapshot : documentSnapshots.getDocuments()){
                                Video video = snapshot.toObject(Video.class);
                                videoList.add(video);
                            }

                            iVideo.fetchVideosSuccess(videoList);
                        }
                    }
                });
    }

}
