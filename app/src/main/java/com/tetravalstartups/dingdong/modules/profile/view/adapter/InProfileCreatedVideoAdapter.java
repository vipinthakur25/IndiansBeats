package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.player.PlayerActivity;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.CreatedVideoFragment;
import com.tetravalstartups.dingdong.utils.DDDeleteVideoAlert;

import java.util.HashMap;
import java.util.List;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

public class
InProfileCreatedVideoAdapter extends RecyclerView.Adapter<InProfileCreatedVideoAdapter.ViewHolder> {

    Context context;
    List<VideoResponseDatum> datumList;
    private DDDeleteVideoAlert ddDeleteVideoAlert;

    public InProfileCreatedVideoAdapter(Context context, List<VideoResponseDatum> datumList) {
        this.context = context;
        this.datumList = datumList;
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
        VideoResponseDatum video = datumList.get(position);
        holder.tvViews.setText(video.getViewCount()+"");
        Glide.with(context).load(video.getVideoThumbnail()).placeholder(R.drawable.dd_logo_placeholder).timeout(60000).into(holder.ivThumbnail);

        holder.frameVideo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ddDeleteVideoAlert.showAlert(context, video.getVideoThumbnail(), video.getId(), true);
                notifyDataSetChanged();
                return true;
            }
        });

        holder.frameVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("video_type", "created");
                intent.putExtra("pos", position+"");
                intent.putExtra("user_id", video.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();
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
