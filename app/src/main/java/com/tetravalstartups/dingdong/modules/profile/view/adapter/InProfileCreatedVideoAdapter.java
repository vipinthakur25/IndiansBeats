package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.player.PlayerActivity;
import com.tetravalstartups.dingdong.modules.profile.model.InProfileCreatedVideo;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PlayVideoActivity;
import com.tetravalstartups.dingdong.utils.DDDeleteVideoAlert;

import java.io.Serializable;
import java.util.List;

public class
InProfileCreatedVideoAdapter extends RecyclerView.Adapter<InProfileCreatedVideoAdapter.ViewHolder> {

    Context context;
    List<InProfileCreatedVideo> inProfileCreatedVideoList;
    private DDDeleteVideoAlert ddDeleteVideoAlert;

    public InProfileCreatedVideoAdapter(Context context, List<InProfileCreatedVideo> inProfileCreatedVideoList) {
        this.context = context;
        this.inProfileCreatedVideoList = inProfileCreatedVideoList;
    }

    @NonNull
    @Override
    public InProfileCreatedVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_profile_video_list_item, parent, false);
        ddDeleteVideoAlert = DDDeleteVideoAlert.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InProfileCreatedVideoAdapter.ViewHolder holder, int position) {
        InProfileCreatedVideo video = inProfileCreatedVideoList.get(position);
        holder.tvViews.setText(video.getViews());
        String url = MediaManager.get().url().resourceType("video").generate("user_uploaded_videos/"+video.getId()+".webp");
        Glide.with(context).load(url).into(holder.ivThumbnail);
        holder.frameVideo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ddDeleteVideoAlert.showAlert(context, url, video.getId(), true);
                return true;
            }
        });

        holder.frameVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("video_type", "created");
                intent.putExtra("pos", position+"");
                intent.putExtra("user_id", video.getUser_id());
                context.startActivity(intent);
            }
        });
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
