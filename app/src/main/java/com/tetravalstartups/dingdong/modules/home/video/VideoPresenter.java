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
//                                Video video = snapshot.toObject(Video.class);
                                Video video = new Video();
                                video.setId(snapshot.getString("id"));
                                video.setVideo_desc(snapshot.getString("video_desc"));
                                video.setSound_contain(snapshot.getBoolean("sound_contain"));
                                video.setSound_id(snapshot.getString("sound_id"));
                                video.setSound_title(snapshot.getString("sound_title"));
                                video.setSound_url(snapshot.getString("sound_url"));
                              //  video.setLikes_count(snapshot.getString("likes_count"));
                                video.setShare_count(snapshot.getString("share_count"));
                                video.setComment_count(snapshot.getString("comment_count"));
                                video.setUser_id(snapshot.getString("user_id"));
                                video.setUser_handle(snapshot.getString("user_handle"));
                                video.setUser_photo(snapshot.getString("user_photo"));
                                video.setVideo_thumbnail(snapshot.getString("video_thumbnail"));
                                video.setVideo_status(snapshot.getString("video_status"));
                                videoList.add(video);
                            }

                            iVideo.fetchVideosSuccess(videoList);
                        }
                    }
                });
    }

}
