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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.transformation.Layer;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;

import java.util.HashMap;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    Context context;
    List<Video> videoList;
    int row_index = -1;
    Master master;
    FirebaseFirestore db;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_video_list_item_layout, parent, false);
        master = new Master(context);
        db = FirebaseFirestore.getInstance();
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

        String url = MediaManager.get().url().transformation(new Transformation().gravity("north_west").quality(40).overlay(new Layer().publicId("dd_final_wm")).width(200).x(20).y(20).crop("scale")).resourceType("video").generate("user_uploaded_videos/"+video.getId()+".mp4");

//        holder.likeVideo.setOnLikeListener(new OnLikeListener() {
//            @Override
//            public void liked(LikeButton likeButton) {
//                doLikeVideo(video);
//            }
//
//            @Override
//            public void unLiked(LikeButton likeButton) {
//                doUnLikeVideo();
//            }
//        });

        Glide.with(context).load(video.getUser_photo()).into(holder.ivPhoto);

        holder.videoView.setVideoURI(Uri.parse(url));
        holder.videoView.requestFocus();
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.videoView.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }
        });
    }

//    private void doLikeVideo(Video video) {
//
//        db.collection("users")
//                .document(master.getId())
//                .collection("liked_videos")
//                .document(video.getId())
//                .set(video);
//
//        db.collection("videos")
//                .document(video.getId())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()){
//                            String like_count =  task.getResult().getString("likes_count");
//                            int likes = Integer.parseInt(like_count);
//                            int update_like = likes+1;
//
//                            HashMap hashMap = new HashMap();
//                            hashMap.put("likes_count", update_like);
//
//                            db.collection("videos")
//                                    .document(video.getId())
//                                    .update(hashMap);
//                        }
//                    }
//                });
//
//
//
//    }

    private void doUnLikeVideo() {
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView tvLikes, tvComments, tvShares, tvUserName, tvFollowers, tvSoundName;
        VideoView videoView;
        ImageView ivSoundCD, ivPhoto, ivHeadphone;
        LinearLayout lvLike;
        LottieAnimationView aniLike;
        LikeButton likeVideo;

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
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            likeVideo = itemView.findViewById(R.id.likeVideo);
            tvSoundName.setSelected(true);

        }
    }
}
