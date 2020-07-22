package com.tetravalstartups.dingdong.modules.home.video;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.LoginActivity;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.comment.InVideoCommentBottomSheet;
import com.tetravalstartups.dingdong.modules.create.SoundDetailActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.vincan.medialoader.MediaLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    Context context;
    List<Video> videoList;
    int row_index = -1;
    Master master;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private InVideoCommentBottomSheet inVideoCommentBottomSheet;

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
        inVideoCommentBottomSheet = new InVideoCommentBottomSheet();
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoAdapter.VideoViewHolder holder, int position) {
        final Video video = videoList.get(position);
        row_index = position;

        if (row_index == position) {
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
        holder.tvUserHandle.setText("@" + video.getUser_handle());
        holder.tvVideoDesc.setText(video.getVideo_desc());
        holder.tvSoundName.setText(video.getSound_title());

        // Glide.with(context).load(R.drawable.dd_w)

        String url = MediaManager.get().url().transformation(new Transformation().quality(5)).resourceType("video").generate("user_uploaded_videos/" + video.getId() + ".webm");
        String proxyUrl = MediaLoader.getInstance(context).getProxyUrl(url);

        holder.lvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences commentsPref = context.getSharedPreferences("comments", 0);
                SharedPreferences.Editor editor = commentsPref.edit();
                editor.putString("video_id", video.getId());
                editor.apply();
                inVideoCommentBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
                inVideoCommentBottomSheet.show(((MainActivity)context).getSupportFragmentManager(), "addBanks");
            }
        });

        holder.likeVideo.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (firebaseAuth.getCurrentUser() != null) {
                    doLikeVideo(video, holder);
                } else {
                    likeButton.setLiked(false);
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (firebaseAuth.getCurrentUser() != null) {
                    doUnLikeVideo(video, holder);
                } else {
                    likeButton.setLiked(true);
                    context.startActivity(new Intent(context, LoginActivity.class));
                }

            }
        });

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", video.getUser_id());
                context.startActivity(intent);
            }
        });

        holder.tvUserHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", video.getUser_id());
                context.startActivity(intent);
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {

            Query likesQuery = db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                    .collection("liked_videos");
            likesQuery.whereEqualTo("id", video.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().isEmpty()) {
                                    holder.likeVideo.setLiked(false);
                                } else {
                                    holder.likeVideo.setLiked(true);
                                }
                            }
                        }
                    });

            Query followingQuery = db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                    .collection("following").whereEqualTo("id", video.getUser_id());
            followingQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (video.getUser_id().equals(master.getId())) {
                        holder.ivFollowUser.setVisibility(View.GONE);
                    } else if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        holder.ivFollowUser.setVisibility(View.VISIBLE);
                    } else {
                        holder.ivFollowUser.setVisibility(View.GONE);
                    }
                }
            });

        }

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.videoView.isPlaying()){
                    holder.videoView.pause();
                } else {
                    holder.videoView.start();
                }
            }
        });

        Glide.with(context).load(video.getUser_photo()).into(holder.ivPhoto);

        holder.videoView.setVideoURI(Uri.parse(proxyUrl));
        holder.videoView.requestFocus();

        String thumb_url = MediaManager.get().url()
                .resourceType("video")
                .generate("user_uploaded_videos/" + video.getId() + ".webp");

        String thumb_blur_url = MediaManager.get().url()
                .transformation(new Transformation().effect("blur:966"))
                .resourceType("video")
                .generate("user_uploaded_videos/" + video.getId() + ".webp");

      //  Glide.with(context).load(thumb_url).into(holder.ivThumbnail);

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

                if (mp.isPlaying()) {
                    holder.ivThumbnail.setVisibility(View.INVISIBLE);
                    int current_views = Integer.parseInt(video.getView_count());
                    int updated_view = current_views + 1;
                    HashMap hmView = new HashMap();
                    hmView.put("view_count", updated_view + "");
                    db.collection("videos")
                            .document(video.getId())
                            .update(hmView);
                } else {
                    String thumb_url = MediaManager.get().url()
                            .resourceType("video")
                            .generate("user_uploaded_videos/" + video.getId() + ".webp");
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
                holder.ivFollowUser.setImageDrawable(context.getResources().getDrawable(R.drawable.dd_followed_link));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.ivFollowUser.setVisibility(View.GONE);

                    }
                }, 1500);

                HashMap hmFollowing = new HashMap();
                hmFollowing.put("id", video.getUser_id());
                hmFollowing.put("handle", video.getUser_handle());
                hmFollowing.put("photo", video.getUser_photo());
                hmFollowing.put("name", video.getUser_name());

                HashMap hmFollower = new HashMap();
                hmFollower.put("id", master.getId());
                hmFollower.put("handle", master.getHandle());
                hmFollower.put("photo", master.getPhoto());
                hmFollower.put("name", master.getName());

                db.collection("users")
                        .document(master.getId())
                        .collection("following")
                        .document(video.getUser_id())
                        .set(hmFollowing);

                db.collection("users")
                        .document(video.getUser_id())
                        .collection("followers")
                        .document(video.getUser_id())
                        .set(hmFollower);

            }
        });

        holder.ivSoundCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SoundDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sound_id", video.getSound_id());
                bundle.putString("sound_title", video.getSound_title());
                bundle.putString("user_photo", video.getUser_photo());
                bundle.putString("user_handle", video.getUser_handle());
                bundle.putString("user_id", video.getUser_id());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.lvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDLoading ddLoading = DDLoading.getInstance();
                ddLoading.showProgress(context, "Loading...", false);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("*/*");
                FileLoader.with(context)
                        .load(url,false)
                        .fromDirectory("dingdong/shared", FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {

                                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                        .setLink(Uri.parse(url))
                                        .setDomainUriPrefix("https://dingdong7b.page.link")
                                        .buildShortDynamicLink()
                                        .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                                            @Override
                                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                                if (task.isSuccessful()) {
                                                    String link = task.getResult().getShortLink().toString();
                                                    ddLoading.hideProgress();
                                                    File loadedFile = response.getBody();
                                                    Intent shareIntent = new Intent();
                                                    shareIntent.setAction(Intent.ACTION_SEND);
                                                    shareIntent.putExtra(Intent.EXTRA_TEXT,link);
                                                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(loadedFile.getPath()));
                                                    shareIntent.setType("video/*");
                                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    HashMap hmFollower = new HashMap();
                                                    hmFollower.put("id", master.getId());
                                                    db.collection("videos")
                                                            .document(video.getId())
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()){
                                                                        String current_share_count = task.getResult().getString("share_count");
                                                                        int current_sc = Integer.parseInt(current_share_count);
                                                                        int updated_sc = current_sc + 1;
                                                                        HashMap hashMap = new HashMap();
                                                                        hashMap.put("share_count", updated_sc+"");
                                                                        db.collection("videos")
                                                                                .document(video.getId())
                                                                                .update(hashMap);
                                                                    }
                                                                }
                                                            });
                                                    context.startActivity(Intent.createChooser(shareIntent, "Share Video"));

                                                }
                                            }
                                        });


                            }

                            @Override
                            public void onError(FileLoadRequest request, Throwable t) {
                                ddLoading.hideProgress();
                            }
                        });

            }
        });

    }

    private void doLikeVideo(Video video, VideoViewHolder holder) {

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
                        if (task.isSuccessful()) {
                            String like_count = task.getResult().getString("likes_count");
                            int likes = Integer.parseInt(like_count);
                            int update_like = likes + 1;
                            holder.tvLikes.setText(update_like + "");

                            HashMap hashMap = new HashMap();
                            hashMap.put("likes_count", update_like + "");

                            db.collection("videos")
                                    .document(video.getId())
                                    .update(hashMap);
                        }
                    }
                });
    }

    private void doUnLikeVideo(Video video, VideoViewHolder holder) {

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
                        if (task.isSuccessful()) {
                            String like_count = task.getResult().getString("likes_count");
                            int likes = Integer.parseInt(like_count);
                            int update_like = likes - 1;
                            holder.tvLikes.setText(update_like + "");

                            HashMap hashMap = new HashMap();
                            hashMap.put("likes_count", update_like + "");

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
        ImageView ivSoundCD, ivPhoto, ivHeadphone, ivPlayPause, ivFollowUser, ivCover;
        LinearLayout lvLike, lvComment, lvShare;
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
            ivCover = itemView.findViewById(R.id.ivCover);
            lvComment = itemView.findViewById(R.id.lvComment);
            lvShare = itemView.findViewById(R.id.lvShare);
            tvSoundName.setSelected(true);

        }
    }
}
