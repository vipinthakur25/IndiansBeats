package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.model.InProfileCreatedVideo;

import java.util.List;

public class InProfileCreatedVideoAdapter extends RecyclerView.Adapter<InProfileCreatedVideoAdapter.ViewHolder> {

    Context context;
    List<InProfileCreatedVideo> inProfileCreatedVideoList;

    public InProfileCreatedVideoAdapter(Context context, List<InProfileCreatedVideo> inProfileCreatedVideoList) {
        this.context = context;
        this.inProfileCreatedVideoList = inProfileCreatedVideoList;
    }

    @NonNull
    @Override
    public InProfileCreatedVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_profile_video_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InProfileCreatedVideoAdapter.ViewHolder holder, int position) {
        InProfileCreatedVideo video = inProfileCreatedVideoList.get(position);
        holder.tvViews.setText(video.getViews());
        Glide.with(context).load(video.getThumbnail()).into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return inProfileCreatedVideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameVideo;
        ImageView ivThumbnail;
        TextView tvViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            frameVideo = itemView.findViewById(R.id.frameVideo);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvViews = itemView.findViewById(R.id.tvViews);

        }
    }
}
