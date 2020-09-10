package com.tetravalstartups.dingdong.modules.player;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;

import java.util.List;

public class ___PlayerAdapter extends RecyclerView.Adapter<___PlayerViewHolder> {

    Context context;
    List<VideoResponseDatum> videoList;

    public ___PlayerAdapter(Context context, List<VideoResponseDatum> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ___PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.___video_list_item, parent, false);
        return new ___PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ___PlayerViewHolder holder, int position) {
        VideoResponseDatum video = videoList.get(position);
        holder.playVideo(video);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ___PlayerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

}
