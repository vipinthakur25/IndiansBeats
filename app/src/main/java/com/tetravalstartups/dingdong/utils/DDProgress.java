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

public class DDProgress {

    public static DDProgress ddProgress = null;
    private Dialog mDialog;
    private TextView tvDDMessage;
    private ProgressBar progressBar;

    public static DDProgress getInstance() {
        if (ddProgress == null){
            ddProgress = new DDProgress();
        }
        return ddProgress;
    }

    public void showProgress(Context context, String message, boolean cancelable) {
        mDialog = new Dialog(context, R.style.DDLoading);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dd_loading_layout);

        ImageView ivDDLogo = mDialog.findViewById(R.id.ivDDLogo);

        tvDDMessage = mDialog.findViewById(R.id.tvDDMessage);
        progressBar = mDialog.findViewById(R.id.progressBar);

        YoYo.with(Techniques.Bounce)
                .duration(500)
                .repeat(YoYo.INFINITE)
                .playOn(ivDDLogo);
       // tvDDMessage.setText(message);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void updateProgress(String message, int progress){
        tvDDMessage.setText(progress+"%"+"\n\n"+message);
        ///progressBar.setProgress(progress);
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
