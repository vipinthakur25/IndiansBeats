package com.tetravalstartups.dingdong.modules.player;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
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
import com.cloudinary.transformation.Layer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
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
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.LoginActivity;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.comment.InVideoCommentBottomSheet;
import com.tetravalstartups.dingdong.modules.create.SoundDetailActivity;
import com.tetravalstartups.dingdong.modules.home.video.Video;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.io.File;
import java.util.HashMap;

public class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Player.EventListener {

    private Context context;
    private VideoView videoView;
    private ImageView ivSoundCD;
    private ImageView ivFollowUser;
    private ImageView ivPhoto;
    private ImageView ivPlay;
    private ImageView ivPause;
    private TextView tvSoundName;
    private TextView tvUserHandle;
    private TextView tvVideoDesc;
    private TextView tvLikeCount;
    private TextView tvCommentCount;
    private TextView tvShareCount;
    private LikeButton likeVideo;

    private LinearLayout lvLike, lvComment, lvShare;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private Master master;
    private Video videoModel;
    private InVideoCommentBottomSheet inVideoCommentBottomSheet;

    private SimpleCache simpleCache;
    private CacheDataSourceFactory cacheDataSourceFactory;

    private PlayerView playerView;
    private SimpleExoPlayer player;

    private LottieAnimationView aniLike;

    PlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        videoView = itemView.findViewById(R.id.videoView);
        ivSoundCD = itemView.findViewById(R.id.ivSoundCD);
        tvSoundName = itemView.findViewById(R.id.tvSoundName);
        ivFollowUser = itemView.findViewById(R.id.ivFollowUser);
        tvUserHandle = itemView.findViewById(R.id.tvUserHandle);
        tvVideoDesc = itemView.findViewById(R.id.tvVideoDesc);
        tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
        tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
        tvShareCount = itemView.findViewById(R.id.tvShareCount);
        likeVideo = itemView.findViewById(R.id.likeVideo);
        ivPhoto = itemView.findViewById(R.id.ivPhoto);
        lvComment = itemView.findViewById(R.id.lvComment);
        lvShare = itemView.findViewById(R.id.lvShare);
        likeVideo = itemView.findViewById(R.id.likeVideo);
        ivPlay = itemView.findViewById(R.id.ivPlay);
        ivPause = itemView.findViewById(R.id.ivPause);
        aniLike = itemView.findViewById(R.id.aniLike);
//        playerView = itemView.findViewById(R.id.player_view);

        ivFollowUser.setOnClickListener(this);
        lvComment.setOnClickListener(this);
        lvShare.setOnClickListener(this);
        tvUserHandle.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivSoundCD.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        master = new Master(context);
        inVideoCommentBottomSheet = new InVideoCommentBottomSheet();

        likeVideo.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (firebaseAuth.getCurrentUser() != null) {
                    aniLike.setVisibility(View.VISIBLE);
                    aniLike.playAnimation();
                    aniLike.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            aniLike.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    doLikeVideo();
                } else {
                    likeButton.setLiked(false);
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (firebaseAuth.getCurrentUser() != null) {
                    doUnLikeVideo();
                } else {
                    likeButton.setLiked(true);
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()){
                    videoView.pause();
                    ivPause.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivPause.setVisibility(View.INVISIBLE);
                        }
                    }, 500);
                } else {
                    videoView.start();
                    ivPlay.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivPlay.setVisibility(View.INVISIBLE);
                        }
                    }, 500);
                }
            }

        });

    }

    public void playVideo(Video model) {
        if (model.getVideo_url() != null) {
            videoView.requestFocus();
            Uri uri = Uri.parse(model.getVideo_url());
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(mp -> {
                videoView.start();

                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();
                float videoProportion = (float) videoWidth / (float) videoHeight;

                DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);

                float screenWidth = mDisplayMetrics.widthPixels;
                float screenHeight = mDisplayMetrics.heightPixels;

                float screenProportion = (float) screenWidth / (float) screenHeight;
                ViewGroup.LayoutParams lp = videoView.getLayoutParams();

                if (videoProportion > screenProportion) {
                    lp.width = (int) screenWidth;
                    lp.height = (int) ((float) screenWidth / videoProportion);
                } else {
                    lp.width = (int) (videoProportion * (float) screenHeight);
                    lp.height = (int) screenHeight;
                }

                if (mp.isPlaying()) {
                    int current_views = Integer.parseInt(model.getView_count());
                    int updated_view = current_views + 1;
                    HashMap hmView = new HashMap();
                    hmView.put("view_count", updated_view + "");
                    db.collection("videos")
                            .document(videoModel.getId())
                            .update(hmView);
                }

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();
                    }
                });

            });

            double randomDouble = Math.random();
            randomDouble = randomDouble * 1000 + 1;
            int randomInt = (int) randomDouble;

            HashMap hashMap = new HashMap();
            hashMap.put("video_index", randomInt+"");

            db.collection("videos")
                    .document(model.getId())
                    .update(hashMap);

        }

//        if (player != null) {
//            player.stop();
//            player.release();
//            player = null;
//        }
//
//            if (context != null) {
//
//                player = new SimpleExoPlayer.Builder(context).build();
//                simpleCache = App.simpleCache;
//                cacheDataSourceFactory = new CacheDataSourceFactory(simpleCache, new DefaultHttpDataSourceFactory(Util.getUserAgent(context, "dingdong"))
//                        , CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
//                ProgressiveMediaSource progressiveMediaSource = new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(Uri.parse(model.getVideo_url()));
//                playerView.setPlayer(player);
//                player.setPlayWhenReady(true);
//                player.seekTo(0, 0);
//                player.setRepeatMode(Player.REPEAT_MODE_ALL);
//                player.addListener(this);
//                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
//                player.prepare(progressiveMediaSource, true, false);
//            }

        videoModel = model;
        setVideoData(model);
    }

    public void releasePlayer() {

    }

    public void cdSpinAnimation() {
        RotateAnimation rotate = new RotateAnimation(0,
                180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        ivSoundCD.startAnimation(rotate);
    }

    private void setVideoData(Video video) {
        tvSoundName.setSelected(true);
        tvSoundName.setText(video.getSound_title());
        tvUserHandle.setText("@"+video.getUser_handle());
        tvCommentCount.setText(video.getComment_count());
        tvShareCount.setText(video.getShare_count());
        tvVideoDesc.setText(video.getVideo_desc());

        Glide.with(context).load(video.getUser_photo())
                .placeholder(R.drawable.dd_logo_placeholder).into(ivPhoto);

        if (firebaseAuth.getCurrentUser() != null) {
            checkForFollowing();
            checkForLike();
        }

        if (videoModel.getVideo_desc().isEmpty()){
            tvVideoDesc.setVisibility(View.GONE);
        }

        db.collection("videos").document(video.getId())
                .collection("liked_by")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        String likes_count = String.valueOf(queryDocumentSnapshots.getDocuments().size());
                        tvLikeCount.setText(likes_count);
                    }
                });

        fetchLatestComments();

    }

    private void checkForFollowing() {
        Query followingQuery = db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("following").whereEqualTo("id", videoModel.getUser_id());
        followingQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (videoModel.getUser_id().equals(master.getId())) {
                   ivFollowUser.setVisibility(View.GONE);
                } else if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                    ivFollowUser.setVisibility(View.VISIBLE);
                } else {
                    ivFollowUser.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkForLike() {
        Query likesQuery = db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("liked_videos");
        likesQuery.whereEqualTo("id", videoModel.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().isEmpty()) {
                                likeVideo.setLiked(false);
                            } else {
                                likeVideo.setLiked(true);
                            }
                        }
                    }
                });
    }

    private void doShowComments() {
        SharedPreferences commentsPref = context.getSharedPreferences("comments", 0);
        SharedPreferences.Editor editor = commentsPref.edit();
        editor.putString("video_id", videoModel.getId());
        editor.apply();
        inVideoCommentBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        inVideoCommentBottomSheet.show(((MainActivity)context).getSupportFragmentManager(), "comments");
    }

    private void fetchLatestComments() {
        db.collection("videos")
                .document(videoModel.getId())
                .collection("comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()){
                            tvCommentCount.setText("0");
                        } else {
                            String comments = String.valueOf(queryDocumentSnapshots.getDocuments().size());
                            tvCommentCount.setText(comments);
                        }
                    }
                });
    }


    private void doLikeVideo() {

        HashMap hmVideo = new HashMap();
        hmVideo.put("id", firebaseAuth.getCurrentUser().getUid());
        hmVideo.put("timestamp", FieldValue.serverTimestamp());
        hmVideo.put("handle", master.getHandle());

        HashMap hmUser = new HashMap();
        hmUser.put("id", videoModel.getId());
        hmUser.put("timestamp", FieldValue.serverTimestamp());

        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("liked_videos")
                .document(videoModel.getId())
                .set(hmUser);

        db.collection("videos")
                .document(videoModel.getId())
                .collection("liked_by")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(hmVideo);
    }

    private void doUnLikeVideo() {

        db.collection("videos")
                .document(videoModel.getId())
                .collection("liked_by")
                .document(firebaseAuth.getCurrentUser().getUid())
                .delete();

        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("liked_videos")
                .document(videoModel.getId())
                .delete();
    }

    private void doShareVideos() {
        DDLoading ddLoading = DDLoading.getInstance();

        String url = MediaManager.get().url().transformation(new Transformation()
                .gravity("north_east").height(50)
                .overlay(new Layer().publicId("dd_wm_v1")).width(40).x(20).y(20).crop("scale"))
                .resourceType("video").generate("user_uploaded_videos/"+videoModel.getId()+".mp4");
        Toast.makeText(context, ""+url, Toast.LENGTH_SHORT).show();

        String message = "Hey your friend is using *Ding Dong* which is an *Hybrid video sharing app*. Here is the *download* link:\nhttps://bit.ly/3jJ2kJU\n\n*Create . Share . Earn*";

        ddLoading.showProgress(context, "Loading...", false);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("*/*");
        FileLoader.with(context)
                .load(url,false)
                .fromDirectory("dingdong/shared", FileLoader.DIR_EXTERNAL_PUBLIC)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        ddLoading.hideProgress();
                        File loadedFile = response.getBody();
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(loadedFile.getPath()));
                        shareIntent.setType("*/*");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        HashMap hmFollower = new HashMap();
                        hmFollower.put("id", master.getId());
                        db.collection("videos")
                                .document(videoModel.getId())
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
                                                    .document(videoModel.getId())
                                                    .update(hashMap);
                                        }
                                    }
                                });
                        context.startActivity(Intent.createChooser(shareIntent, "Share Video"));
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        ddLoading.hideProgress();
                    }
                });


    }

    @Override
    public void onClick(View v) {
        if (v == ivFollowUser) {
            if (firebaseAuth.getCurrentUser() != null) {

                ivFollowUser.setImageDrawable(context.getResources().getDrawable(R.drawable.dd_followed_link));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivFollowUser.setVisibility(View.GONE);
                    }
                }, 1500);

                HashMap<String, String> hmFollowing = new HashMap<String, String>();
                hmFollowing.put("id", videoModel.getUser_id());
                hmFollowing.put("handle", videoModel.getUser_handle());
                hmFollowing.put("photo", videoModel.getUser_photo());
                hmFollowing.put("name", videoModel.getUser_name());

                HashMap<String, String> hmFollower = new HashMap<String, String>();
                hmFollower.put("id", master.getId());
                hmFollower.put("handle", master.getHandle());
                hmFollower.put("photo", master.getPhoto());
                hmFollower.put("name", master.getName());

                db.collection("users")
                        .document(master.getId())
                        .collection("following")
                        .document(videoModel.getUser_id())
                        .set(hmFollowing);

                db.collection("users")
                        .document(videoModel.getUser_id())
                        .collection("followers")
                        .document(videoModel.getUser_id())
                        .set(hmFollower);


            } else {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        }

        if (v == lvComment) {
            if (firebaseAuth.getCurrentUser() != null) {
                doShowComments();
            } else {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        }

        if (v == lvShare) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            Permissions.check(context/*context*/, permissions,
                    null/*rationale*/, null/*options*/,
                    new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            doShareVideos();
                        }
                    });
        }

        if (v == ivPhoto) {
            Intent intent = new Intent(context, PublicProfileActivity.class);
            intent.putExtra("user_id", videoModel.getUser_id());
            context.startActivity(intent);
        }

        if (v == tvUserHandle) {
            Intent intent = new Intent(context, PublicProfileActivity.class);
            intent.putExtra("user_id", videoModel.getUser_id());
            context.startActivity(intent);
        }

        if (v == ivSoundCD) {
            Intent intent = new Intent(context, SoundDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("sound_id", videoModel.getSound_id());
            bundle.putString("sound_title", videoModel.getSound_title());
            bundle.putString("user_photo", videoModel.getUser_photo());
            bundle.putString("user_handle", videoModel.getUser_handle());
            bundle.putString("user_id", videoModel.getUser_id());
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {

        } else if (playbackState == Player.STATE_READY) {

        }
    }

}