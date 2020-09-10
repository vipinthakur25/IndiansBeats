package com.tetravalstartups.dingdong.modules.record.gallery;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;

import java.util.List;

public class GalleryVideoAdapter extends RecyclerView.Adapter<GalleryVideoAdapter.VideoHolder> {

    private Context context;
    private List<String> galleryVideoList;
    private static final String TAG = "GalleryVideoAdapter";

    public GalleryVideoAdapter(Context context, List<String> galleryVideoList) {
        this.context = context;
        this.galleryVideoList = galleryVideoList;
    }

    @NonNull
    @Override
    public GalleryVideoAdapter.VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_video_list_item, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryVideoAdapter.VideoHolder holder, int position) {
        Glide.with(context).load(galleryVideoList.get(position))
                .placeholder(R.drawable.dd_logo_placeholder)
                .into(holder.ivVideos);

        holder.frameVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(galleryVideoList.get(position));
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(context, uri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMilSec = Long.parseLong(time);
                retriever.release();
                if (timeInMilSec < 5000 || timeInMilSec > 60000) {
                    Toast.makeText(context, "Video duration must be between min 5 seconds to max 1 minute.", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent("galleryVideo");
                intent.putExtra("video_uri", galleryVideoList.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryVideoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        FrameLayout frameVideo;
        ImageView ivVideos;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);

            frameVideo = itemView.findViewById(R.id.frameVideo);
            ivVideos = itemView.findViewById(R.id.ivVideos);

        }
    }
}
