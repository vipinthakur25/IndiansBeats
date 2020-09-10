package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.player.PlayerActivity;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;

import java.util.List;

public class LikedVideoAdapter extends RecyclerView.Adapter<LikedVideoAdapter.ViewHolder> {

    Context context;
    List<VideoResponseDatum> likedVideosList;
    private Master master;

    public LikedVideoAdapter(Context context, List<VideoResponseDatum> likedVideosList) {
        this.context = context;
        this.likedVideosList = likedVideosList;
    }

    @NonNull
    @Override
    public LikedVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_video_list_item, parent, false);
        master = new Master(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedVideoAdapter.ViewHolder holder, int position) {
        VideoResponseDatum video = likedVideosList.get(position);
        holder.tvViews.setText(video.getLikesCount()+"");
        Glide.with(context).load(video.getVideoUrl()).placeholder(R.drawable.dd_logo_placeholder).timeout(60000).into(holder.ivThumbnail);

        holder.frameVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("video_type", "liked");
                intent.putExtra("pos", position+"");
                intent.putExtra("user_id", master.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return likedVideosList.size();
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
