package com.tetravalstartups.dingdong.modules.common.hashtag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tetravalstartups.dingdong.modules.common.hashtag.model.TaggedVideos;
import com.tetravalstartups.dingdong.modules.common.hashtag.model.TaggedVideosResponse;
import com.tetravalstartups.dingdong.modules.player.PlayerActivity;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;

import java.util.List;

public class HashtagAdapter extends RecyclerView.Adapter<HashtagAdapter.ViewHolder> {

    private Context context;
    private List<VideoResponseDatum> taggedVideosResponseList;

    public HashtagAdapter(Context context, List<VideoResponseDatum> taggedVideosResponseList) {
        this.context = context;
        this.taggedVideosResponseList = taggedVideosResponseList;
    }

    @NonNull
    @Override
    public HashtagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_video_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagAdapter.ViewHolder holder, int position) {
        VideoResponseDatum taggedVideosResponse = taggedVideosResponseList.get(position);
        holder.tvViews.setText(taggedVideosResponse.getLikesCount()+"");
        Glide.with(context).load(taggedVideosResponse.getVideoUrl()).placeholder(R.drawable.dd_logo_placeholder).timeout(60000).into(holder.ivThumbnail);
        holder.frameVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Master master = new Master(context);
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
        return taggedVideosResponseList.size();
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
