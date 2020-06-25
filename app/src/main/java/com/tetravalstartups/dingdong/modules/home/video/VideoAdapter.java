package com.tetravalstartups.dingdong.modules.home.video;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.transformation.Layer;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    Context context;
    List<Video> videoList;
    int row_index = -1;

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

        row_index = position;

        if (row_index == position){
            RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(5000);
            rotate.setRepeatMode(Animation.RESTART);
            rotate.setRepeatCount(Animation.INFINITE);
            rotate.setInterpolator(new LinearInterpolator());
            holder.ivSoundCD.startAnimation(rotate);
        }

//        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate);
//        rotation.setFillAfter(true);
//        holder.ivSoundCD.startAnimation(rotation);

//        holder.tvLikes.setText(video.getLikes_count()+"");
//        holder.tvComments.setText(video.getComment_count()+"");
//        holder.tvShares.setText(video.getShare_count()+"");
//        holder.tvUserName.setText(video.getUser_handle());

        String url = MediaManager.get().url().transformation(new
                Transformation().quality(30)).resourceType("video").generate("user_uploaded_videos/"+video.getId()+".mp4");



        holder.videoView.setVideoURI(Uri.parse(url));
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

        TextView tvLikes, tvComments, tvShares, tvUserName, tvFollowers, tvSoundName;
        VideoView videoView;
        ImageView ivSoundCD;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

//            tvLikes = itemView.findViewById(R.id.tvLikes);
//            tvComments = itemView.findViewById(R.id.tvComments);
//            tvShares = itemView.findViewById(R.id.tvShares);
//            tvUserName = itemView.findViewById(R.id.tvUserName);
//            tvFollowers = itemView.findViewById(R.id.tvFollowers);
            videoView = itemView.findViewById(R.id.videoView);
            ivSoundCD = itemView.findViewById(R.id.ivSoundCD);
            tvSoundName = itemView.findViewById(R.id.tvSoundName);
            tvSoundName.setSelected(true);

        }
    }
}
