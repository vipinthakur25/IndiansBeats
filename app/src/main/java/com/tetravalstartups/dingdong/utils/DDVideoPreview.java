package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tetravalstartups.dingdong.R;

public class DDVideoPreview {

    public static DDVideoPreview ddVideoPreview = null;
    private Dialog mDialog;
    private VideoView videoViewPreview;
    private ImageView ivStopVideo;

    public static DDVideoPreview getInstance() {
        if (ddVideoPreview == null){
            ddVideoPreview = new DDVideoPreview();
        }
        return ddVideoPreview;
    }

    public void playVideo(Context context, String path, boolean cancelable) {
        mDialog = new Dialog(context, R.style.DDAlert);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dd_video_preview_layout);

        videoViewPreview = mDialog.findViewById(R.id.videoViewPreview);
        ivStopVideo = mDialog.findViewById(R.id.ivStopVideo);

        ivStopVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(videoViewPreview);
        videoViewPreview.setMediaController(mediaController);
        videoViewPreview.setVideoPath(path);
        videoViewPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }
        });

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void stopVideo() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
