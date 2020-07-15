package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tetravalstartups.dingdong.R;

public class DDLoadingProgress {

    public static DDLoadingProgress ddLoading = null;
    private Dialog mDialog;

    public static DDLoadingProgress getInstance() {
        if (ddLoading == null){
            ddLoading = new DDLoadingProgress();
        }
        return ddLoading;
    }

    public void showProgress(Context context, String message, boolean cancelable) {
        mDialog = new Dialog(context, R.style.DDLoading);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dd_loading_progress_layout);

        ImageView ivDDLogo = mDialog.findViewById(R.id.ivDDLogo);
        TextView tvDDMessage = mDialog.findViewById(R.id.tvDDMessage);
        YoYo.with(Techniques.Bounce)
                .duration(500)
                .repeat(YoYo.INFINITE)
                .playOn(ivDDLogo);
        tvDDMessage.setText(message);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void updateProgress(int progress){
        ProgressBar progressVideo = mDialog.findViewById(R.id.progressVideo);
        progressVideo.setMax(100);
        progressVideo.setProgress(progress);
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
