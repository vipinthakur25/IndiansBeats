package com.tetravalstartups.dingdong.modules.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private View view;
    private RecyclerView recyclerVideos;
    private ArrayList<Video> videoList = new ArrayList<>();
    PlayerAdapter playerAdapter;
    private DDLoading ddLoading;

    private FirebaseFirestore db;
    private String user_id, video_type, pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
    }

    private void initView() {
        recyclerVideos = findViewById(R.id.recyclerVideos);
        ddLoading = DDLoading.getInstance();
        db = FirebaseFirestore.getInstance();

        user_id = getIntent().getStringExtra("user_id");
        video_type = getIntent().getStringExtra("video_type");
        pos = getIntent().getStringExtra("pos");

        Toast.makeText(this, "user: "+user_id+" video: "+video_type+"  pos: "+pos, Toast.LENGTH_SHORT).show();

        if (video_type.equals("created")) {
            setupCreatedVideoAdapter();
        } else
            if (video_type.equals("liked")) {
                setupLikedVideoAdapter();
        }

    }

    private void setupCreatedVideoAdapter() {
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        playerAdapter = new PlayerAdapter(this, videoList);

        Query query = db.collection("videos")
                .whereEqualTo("user_id", user_id)
                .whereEqualTo("video_status", Constant.VIDEO_STATUS_PUBLIC)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            videoList.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                Video video = snapshot.toObject(Video.class);
                                videoList.add(video);
                            }

                            playerAdapter.notifyDataSetChanged();
                            recyclerVideos.setAdapter(playerAdapter);
                            recyclerVideos.scrollToPosition(Integer.parseInt(pos));

                        }
                    }
                });

    }

    private void setupLikedVideoAdapter() {
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        if (recyclerVideos.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerVideos);
        playerAdapter = new PlayerAdapter(this, videoList);

        Query query = db.collection("users")
                .document(user_id).
                        collection("liked_videos")
                .orderBy("timestamp", Query.Direction.DESCENDING);

       query.get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           videoList.clear();
                           for (DocumentSnapshot snapshot : task.getResult()) {
                               String id = snapshot.getString("id");
                               Query query1 = db.collection("videos")
                                       .whereEqualTo("id", id)
                                       .orderBy("timestamp", Query.Direction.DESCENDING);
                               query1.get()
                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                               if (task.isSuccessful()) {

                                                   for (DocumentSnapshot snapshot1 : task1.getResult()) {
                                                       Video video = snapshot1.toObject(Video.class);
                                                       videoList.add(video);
                                                       Log.e("snap_log", snapshot.toString());
                                                   }

                                                   playerAdapter.notifyDataSetChanged();
                                                   recyclerVideos.setAdapter(playerAdapter);
                                                //   recyclerVideos.scrollToPosition(Integer.parseInt(pos));

                                               }
                                           }
                                       });
                           }
                       }
                   }
               });

    }
}