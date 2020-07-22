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
import com.cloudinary.android.MediaManager;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.model.InProfileCreatedVideo;
import com.tetravalstartups.dingdong.modules.profile.model.LikedVideos;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PlayVideoActivity;

import java.util.List;

public class LikedVideoAdapter extends RecyclerView.Adapter<LikedVideoAdapter.ViewHolder> {

    Context context;
    List<LikedVideos> likedVideosList;

    public LikedVideoAdapter(Context context, List<LikedVideos> likedVideosList) {
        this.context = context;
        this.likedVideosList = likedVideosList;
    }

    @NonNull
    @Override
    public LikedVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_video_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedVideoAdapter.ViewHolder holder, int position) {
        LikedVideos video = likedVideosList.get(position);
        holder.tvViews.setText(video.getViews());
        String url = MediaManager.get().url().resourceType("video").generate("user_uploaded_videos/"+video.getId()+".webp");
        Glide.with(context).load(url).into(holder.ivThumbnail);
        holder.frameVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("video_type", "liked");
                intent.putExtra("pos", position+"");
                intent.putExtra("user_id", video.getUser_id());
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
