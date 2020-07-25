package com.tetravalstartups.dingdong.modules.player;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.modules.home.video.Video;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerViewHolder> {

    Context context;
    List<Video> videoList;



    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
