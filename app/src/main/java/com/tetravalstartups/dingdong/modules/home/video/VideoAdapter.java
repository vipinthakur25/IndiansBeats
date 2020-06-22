package com.tetravalstartups.dingdong.modules.home.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    Context context;
    List<Video> videoList;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_video_list_item_layout, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoAdapter.VideoViewHolder holder, int position) {
        final Video video = videoList.get(position);
        holder.tvLikes.setText(video.getVideo_likes());
        holder.tvComments.setText(video.getVideo_comments());
        holder.tvShares.setText(video.getVideo_shares());
        holder.tvUserName.setText(video.getVideo_user_name());
        holder.tvFollowers.setText(video.getVideo_user_followers());
        holder.videoView.setVideoURI(Uri.parse(video.getVideo_url()));
        holder.videoView.requestFocus();
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.videoView.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView tvLikes, tvComments, tvShares, tvUserName, tvFollowers;
        VideoView videoView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvComments = itemView.findViewById(R.id.tvComments);
            tvShares = itemView.findViewById(R.id.tvShares);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvFollowers = itemView.findViewById(R.id.tvFollowers);
            videoView = itemView.findViewById(R.id.videoView);

        }
    }
}
