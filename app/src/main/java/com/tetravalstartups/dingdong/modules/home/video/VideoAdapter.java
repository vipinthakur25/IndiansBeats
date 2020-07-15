package com.tetravalstartups.dingdong.modules.home.video;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.transformation.Layer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.LoginActivity;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.create.SoundDetailActivity;
import com.vincan.medialoader.MediaLoader;

import java.util.HashMap;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    Context context;
    List<Video> videoList;
    int row_index = -1;
    Master master;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

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
        firebaseAuth = FirebaseAuth.getInstance();
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

        holder.tvLikes.setText(video.getLikes_count());
        holder.tvComments.setText(video.getComment_count());
        holder.tvShares.setText(video.getShare_count());
        holder.tvUserHandle.setText(video.getUser_handle());
        holder.tvVideoDesc.setText(video.getVideo_desc());
        holder.tvSoundName.setText(video.getSound_title());

       // Glide.with(context).load(R.drawable.dd_w)

        String url = MediaManager.get().url().transformation(new Transformation().quality(5)).resourceType("video").generate("user_uploaded_videos/"+video.getId()+".webm");

        String proxyUrl = MediaLoader.getInstance(context).getProxyUrl(url);

        holder.likeVideo.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (firebaseAuth.getCurrentUser() != null){
                    doLikeVideo(video, holder);
                } else {
                    likeButton.setLiked(false);
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (firebaseAuth.getCurrentUser() != null){
                    doUnLikeVideo(video, holder);
                } else {
                    likeButton.setLiked(true);
                    context.startActivity(new Intent(context, LoginActivity.class));
                }

            }
        });

        if (firebaseAuth.getCurrentUser() != null){


            Query query = db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                    .collection("liked_videos");
            query.whereEqualTo("id", video.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().getDocuments().isEmpty()){
                                    holder.likeVideo.setLiked(false);
                                } else {
                                    holder.likeVideo.setLiked(true);
                                }
                            }
                        }
                    });

        }




//        holder.likeVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (firebaseAuth.getCurrentUser() != null){
//
//
//
//                } else {
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                    //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                }
//            }
//        });



        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivPlayPause.setVisibility(View.VISIBLE);
                holder.ivPlayPause.animate().alpha(0.0f);
            }
        });

        Glide.with(context).load(video.getUser_photo()).into(holder.ivPhoto);

        holder.videoView.setVideoURI(Uri.parse(proxyUrl));
        holder.videoView.requestFocus();

        String thumb_url = MediaManager.get().url()
                .resourceType("video")
                .generate("user_uploaded_videos/"+video.getId()+".webp");
        Glide.with(context).load(thumb_url).into(holder.ivThumbnail);

        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();
                float videoProportion = (float) videoWidth / (float) videoHeight;

                DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);

                float screenWidth = mDisplayMetrics.widthPixels;
                float screenHeight = mDisplayMetrics.heightPixels;

                float screenProportion = (float) screenWidth / (float) screenHeight;
                android.view.ViewGroup.LayoutParams lp = holder.videoView.getLayoutParams();

                if (videoProportion > screenProportion) {
                    lp.width = (int) screenWidth;
                    lp.height = (int) ((float) screenWidth / videoProportion);
                } else {
                    lp.width = (int) (videoProportion * (float) screenHeight);
                    lp.height = (int) screenHeight;
                }
                holder.videoView.start();

                if (mp.isPlaying()){
                    holder.ivThumbnail.setVisibility(View.INVISIBLE);
                } else {
                    String thumb_url = MediaManager.get().url()
                            .resourceType("video")
                            .generate("user_uploaded_videos/"+video.getId()+".webp");
                    Glide.with(context).load(thumb_url).into(holder.ivThumbnail);
                }

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }
        });

        holder.ivFollowUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.ivSoundCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, SoundDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("sound_id", video.getId());
//                bundle.putString("sound_title", video.getSound_title());
//                bundle.putString("user_photo", video.getUser_photo());
//                bundle.putString("user_handle", video.getUser_handle());
//                bundle.putString("user_id", video.getUser_id());
//                intent.putExtras(bundle);
//                context.startActivity(intent);
            }
        });

    }

    private void doLikeVideo(Video video, VideoViewHolder holder) {
//        db.collection("users")
//                .document(master.getId())
//                .collection("liked_videos")
//                .document(video.getId())
//                .set(video);

        HashMap hmVideo = new HashMap();
        hmVideo.put("id", firebaseAuth.getCurrentUser().getUid());
        hmVideo.put("handle", master.getHandle());

        HashMap hmUser = new HashMap();
        hmUser.put("id", video.getId());

        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("liked_videos")
                .document(video.getId())
                .set(hmUser);

        db.collection("videos")
                .document(video.getId())
                .collection("liked_by")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(hmVideo);

        db.collection("videos")
                .document(video.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String like_count =  task.getResult().getString("likes_count");
                            int likes = Integer.parseInt(like_count);
                            int update_like = likes+1;
                            holder.tvLikes.setText(update_like+"");

                            HashMap hashMap = new HashMap();
                            hashMap.put("likes_count", update_like+"");

                            db.collection("videos")
                                    .document(video.getId())
                                    .update(hashMap);
                        }
                    }
                });
    }

    private void doUnLikeVideo(Video video, VideoViewHolder holder) {

//        db.collection("users")
//                .document(master.getId())
//                .collection("liked_videos")
//                .document(video.getId())
//                .set(video);


        db.collection("videos")
                .document(video.getId())
                .collection("liked_by")
                .document(firebaseAuth.getCurrentUser().getUid())
                .delete();

        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("liked_videos")
                .document(video.getId())
                .delete();


        db.collection("videos")
                .document(video.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String like_count =  task.getResult().getString("likes_count");
                            int likes = Integer.parseInt(like_count);
                            int update_like = likes-1;
                            holder.tvLikes.setText(update_like+"");

                            HashMap hashMap = new HashMap();
                            hashMap.put("likes_count", update_like+"");

                            db.collection("videos")
                                    .document(video.getId())
                                    .update(hashMap);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView tvLikes, tvComments, tvShares, tvUserHandle, tvSoundName, tvVideoDesc;
        VideoView videoView;
        ImageView ivSoundCD, ivPhoto, ivHeadphone, ivPlayPause, ivFollowUser, ivDDWaterMark;
        LinearLayout lvLike;
        LottieAnimationView aniLike;
        LikeButton likeVideo;
        ImageView ivThumbnail;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLikes = itemView.findViewById(R.id.tvLikeCount);
            tvComments = itemView.findViewById(R.id.tvCommentCount);
            tvShares = itemView.findViewById(R.id.tvShareCount);
            tvUserHandle = itemView.findViewById(R.id.tvUserHandle);
            videoView = itemView.findViewById(R.id.videoView);
            ivFollowUser = itemView.findViewById(R.id.ivFollowUser);
            ivSoundCD = itemView.findViewById(R.id.ivSoundCD);
            tvSoundName = itemView.findViewById(R.id.tvSoundName);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            likeVideo = itemView.findViewById(R.id.likeVideo);
            ivPlayPause = itemView.findViewById(R.id.ivPlayPause);
            tvVideoDesc = itemView.findViewById(R.id.tvVideoDesc);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
          //  ivDDWaterMark = itemView.findViewById(R.id.ivDDWaterMark);
            tvSoundName.setSelected(true);

        }
    }
}
