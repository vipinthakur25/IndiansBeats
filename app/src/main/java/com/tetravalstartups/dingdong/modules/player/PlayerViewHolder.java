package com.tetravalstartups.dingdong.modules.player;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.BaseActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.PhoneActivity;
import com.tetravalstartups.dingdong.modules.comment.InVideoCommentBottomSheet;
import com.tetravalstartups.dingdong.modules.profile.model.Follow;
import com.tetravalstartups.dingdong.modules.profile.videos.VideoResponseDatum;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

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
    private VideoResponseDatum videoModel;
    private InVideoCommentBottomSheet inVideoCommentBottomSheet;

    private SimpleCache simpleCache;
    private CacheDataSourceFactory cacheDataSourceFactory;

    private PlayerView playerView;
    private SimpleExoPlayer player;

    private LottieAnimationView aniLike;
    private DDLoading ddLoading;

    private RequestInterface requestInterface;
    private static final String TAG = "PlayerViewHolder";

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

        ivFollowUser.setOnClickListener(this);
        lvComment.setOnClickListener(this);
        lvShare.setOnClickListener(this);
        tvUserHandle.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivSoundCD.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);

        master = new Master(context);
        inVideoCommentBottomSheet = new InVideoCommentBottomSheet();
        ddLoading = DDLoading.getInstance();

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
                            aniLike.setVisibility(View.GONE);
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
                    context.startActivity(new Intent(context, PhoneActivity.class));
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (firebaseAuth.getCurrentUser() != null) {
                    doUnLikeVideo();
                } else {
                    likeButton.setLiked(true);
                    context.startActivity(new Intent(context, PhoneActivity.class));
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

    public void playVideo(VideoResponseDatum model) {
        if (model.getVideoUrl() != null) {
            videoView.requestFocus();
            Uri uri = Uri.parse(model.getVideoUrl());
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
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = db.collection("views").document();
                    String referenceId = documentReference.getId();
                    Call<Seen> call;
                    if (firebaseAuth == null) {
                        call = requestInterface.markVideoSeen(referenceId, videoModel.getId(), "");
                    } else {
                        call = requestInterface.markVideoSeen(referenceId, videoModel.getId(), master.getId());
                    }
                    call.enqueue(new Callback<Seen>() {
                        @Override
                        public void onResponse(Call<Seen> call, retrofit2.Response<Seen> response) {
                            Log.e("PlayerViewHolder", "onResponse: "+response.message() );
                        }

                        @Override
                        public void onFailure(Call<Seen> call, Throwable t) {
                            Log.e("PlayerViewHolder", t.getMessage());
                        }
                    });
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

        videoModel = model;
        setVideoData(model);
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

    private void setVideoData(VideoResponseDatum video) {
        tvSoundName.setSelected(true);
        tvSoundName.setText(video.getSoundTitle());
        tvUserHandle.setText("@"+video.getUserHandle());
        tvCommentCount.setText(video.getCommentCount()+"");
        tvShareCount.setText(video.getShareCount()+"");
        tvVideoDesc.setText(video.getVideoDesc());
        tvLikeCount.setText(video.getLikesCount()+"");

        Glide.with(context).load(video.getUserPhoto())
                .placeholder(R.drawable.dd_logo_placeholder).into(ivPhoto);

        if (firebaseAuth.getCurrentUser() != null) {
            fetchLatestComments();
            checkForFollowing();
            checkForLike();
        }

        if (videoModel.getVideoDesc().isEmpty()){
            tvVideoDesc.setVisibility(View.GONE);
        }

    }

    private void checkForLike() {
        if (videoModel.getMylike() == 0) {
            likeVideo.setLiked(false);
        } else if (videoModel.getMylike() == 1) {
            likeVideo.setLiked(true);
        }
    }

    private void doShowComments() {
        SharedPreferences commentsPref = context.getSharedPreferences("comments", 0);
        SharedPreferences.Editor editor = commentsPref.edit();
        editor.putString("video_id", videoModel.getId());
        editor.apply();
        inVideoCommentBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        inVideoCommentBottomSheet.show(((BaseActivity)context).getSupportFragmentManager(), "comments");
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
        final String url = "http://35.228.105.69/dingdong/Socialmedia/LikeUnlike/";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, videoModel.getLikesCount()+"", Toast.LENGTH_SHORT).show();
                        int current_like = Integer.parseInt(tvLikeCount.getText().toString());
                        int update = current_like + 1;
                        tvLikeCount.setText(update+"");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", videoModel.getId());
                params.put("video_id", videoModel.getId());
                params.put("user_id", master.getId());
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void doUnLikeVideo() {
        final String url = "http://35.228.105.69/dingdong/Socialmedia/LikeUnlike/";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, videoModel.getLikesCount()+"", Toast.LENGTH_SHORT).show();
                        int current_like = Integer.parseInt(tvLikeCount.getText().toString());
                        int update = current_like - 1;
                        tvLikeCount.setText(update+"");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", videoModel.getId());
                params.put("video_id", videoModel.getId());
                params.put("user_id", master.getId());
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void doDownloadVideo() {
        ddLoading.showProgress(context, "Loading...", false);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(videoModel.getVideoUrl());
        final File rootPath = new File(Environment.getExternalStorageDirectory(), "DD Shared");
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        final File localFile = new File(rootPath, videoModel.getId()+".mp4");
        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if (localFile.canRead()){
                    doShareVideos(localFile.getPath());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void doShareVideos(String path) {
        String message = "Hey your friend is using *Ding Dong* which is an *Hybrid video sharing app*. Here is the *download* link:\nhttps://bit.ly/33bQke3\n\n*Create . Share . Earn*";
        Uri imgUri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.setType("*/*");

        try {
            ddLoading.hideProgress();
            context.startActivity(Intent.createChooser(shareIntent, "Share Video"));
            Call<Share> call = requestInterface.shareVideoCount(videoModel.getId());
            call.enqueue(new Callback<Share>() {
                @Override
                public void onResponse(Call<Share> call, retrofit2.Response<Share> response) {
                }
                @Override
                public void onFailure(Call<Share> call, Throwable t) {
                    Log.e(TAG, "onFailure: "+t.getMessage() );
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }

    }

    private void checkForFollowing() {
        if (videoModel.getUserId().equals(master.getId())) {
            ivFollowUser.setVisibility(View.GONE);
        } else if (videoModel.getMylike().equals("following")) {
            ivFollowUser.setVisibility(View.GONE);
        } else {
            ivFollowUser.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivFollowUser) {
            doFollowUser();
        }

        if (v == lvComment) {
            if (firebaseAuth.getCurrentUser() != null) {
                doShowComments();
            } else {
                context.startActivity(new Intent(context, PhoneActivity.class));
            }
        }

        if (v == lvShare) {
            //Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            Permissions.check(context/*context*/, permissions,
                    null/*rationale*/, null/*options*/,
                    new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            doDownloadVideo();
                        }
                    });
        }

        if (v == ivPhoto) {
            Intent intent = new Intent(context, PublicProfileActivity.class);
            intent.putExtra("user_id", videoModel.getUserId());
            context.startActivity(intent);
        }

        if (v == tvUserHandle) {
            Intent intent = new Intent(context, PublicProfileActivity.class);
            intent.putExtra("user_id", videoModel.getUserId());
            context.startActivity(intent);
        }

        if (v == ivSoundCD) {
            Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, SoundDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("sound_id", videoModel.getSoundId());
//            bundle.putString("sound_title", videoModel.getSoundTitle());
//            bundle.putString("user_photo", videoModel.getUserPhoto());
//            bundle.putString("user_handle", videoModel.getUserHandle());
//            bundle.putString("user_id", videoModel.getUserId());
//            intent.putExtras(bundle);
//            context.startActivity(intent);
        }

    }

    private void doFollowUser() {
        if (firebaseAuth.getCurrentUser() != null) {
            ddLoading.showProgress(context, "Following...", false);
            ivFollowUser.setImageDrawable(context.getResources().getDrawable(R.drawable.dd_followed_link));
            Call<Follow> call = requestInterface.doFollowUser(master.getId(), videoModel.getUserId());
            call.enqueue(new Callback<Follow>() {
                @Override
                public void onResponse(Call<Follow> call, retrofit2.Response<Follow> response) {
                    Log.e("PlayerViewHolderFollow", "onResponse: "+response.message());
                    ddLoading.hideProgress();
                }

                @Override
                public void onFailure(Call<Follow> call, Throwable t) {
                    ddLoading.hideProgress();
                    Log.e("PlayerViewHolder", t.getMessage());
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ivFollowUser.setVisibility(View.GONE);
                }
            }, 1500);

        } else {
            context.startActivity(new Intent(context, PhoneActivity.class));
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {

        } else if (playbackState == Player.STATE_READY) {

        }
    }
}