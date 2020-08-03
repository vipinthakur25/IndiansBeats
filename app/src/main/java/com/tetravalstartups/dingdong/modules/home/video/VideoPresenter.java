package com.tetravalstartups.dingdong.modules.home.video;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoPresenter {

    private Context context;
    private IVideo iVideo;
    private FirebaseFirestore db;

    public VideoPresenter(Context context, IVideo iVideo) {
        this.context = context;
        this.iVideo = iVideo;
    }

    public void fetchVideos() {
        db = FirebaseFirestore.getInstance();
        List<Video> videoList = new ArrayList<>();

        Query query = db.collection("videos");
        query.whereEqualTo("video_status", Constant.VIDEO_STATUS_PUBLIC)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().getDocuments().isEmpty()){
                                iVideo.fetchVideosError("No Videos");
                            } else {

                                videoList.clear();
                                for (DocumentSnapshot snapshot : task.getResult()) {
                                    Video video = snapshot.toObject(Video.class);
//                                    video.setId(snapshot.getString("id"));
//                                    video.setVideo_desc(snapshot.getString("video_desc"));
//                                    video.setSound_id(snapshot.getString("sound_id"));
//                                    video.setSound_title(snapshot.getString("sound_title"));
//                                    video.setLikes_count(snapshot.getString("likes_count"));
//                                    video.setShare_count(snapshot.getString("share_count"));
//                                    video.setComment_count(snapshot.getString("comment_count"));
//                                    video.setView_count(snapshot.getString("view_count"));
//                                    video.setUser_id(snapshot.getString("user_id"));
//                                    video.setUser_handle(snapshot.getString("user_handle"));
//                                    video.setUser_name(snapshot.getString("user_name"));
//                                    video.setUser_photo(snapshot.getString("user_photo"));
//                                    video.setVideo_thumbnail(snapshot.getString("video_thumbnail"));
//                                    video.setVideo_status(snapshot.getString("video_status"));
//                                    video.setVideo_status(snapshot.getString("video_status"));
                                    videoList.add(video);
                                }

                                Collections.shuffle(videoList);
                                iVideo.fetchVideosSuccess(videoList);

                            }
                        }
                    }
                });
    }

    public interface IVideo {
        void fetchVideosSuccess(List<Video> videoList);

        void fetchVideosError(String error);
    }

}



