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
import com.tetravalstartups.dingdong.modules.profile.model.LikedVideos;
import com.tetravalstartups.dingdong.modules.profile.model.PrivateDraftVideos;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PlayVideoActivity;
import com.tetravalstartups.dingdong.utils.DDDeletePublishVideoAlert;

import java.util.List;

public class PrivateDraftVideoAdapter extends RecyclerView.Adapter<PrivateDraftVideoAdapter.ViewHolder> {

    Context context;
    List<PrivateDraftVideos> privateDraftVideosList;
    private DDDeletePublishVideoAlert ddDeletePublishVideoAlert;

    public PrivateDraftVideoAdapter(Context context, List<PrivateDraftVideos> privateDraftVideosList) {
        this.context = context;
        this.privateDraftVideosList = privateDraftVideosList;
    }

    @NonNull
    @Override
    public PrivateDraftVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_draft_video_list_item, parent, false);
        ddDeletePublishVideoAlert = DDDeletePublishVideoAlert.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrivateDraftVideoAdapter.ViewHolder holder, int position) {
        PrivateDraftVideos video = privateDraftVideosList.get(position);
        holder.tvViews.setText(video.getViews());
        String url = MediaManager.get().url().resourceType("video").generate("user_uploaded_videos/"+video.getId()+".webp");
        Glide.with(context).load(url).into(holder.ivThumbnail);
        holder.frameVideo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ddDeletePublishVideoAlert.showAlert(context, url, video.getId(), true);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return privateDraftVideosList.size();
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
