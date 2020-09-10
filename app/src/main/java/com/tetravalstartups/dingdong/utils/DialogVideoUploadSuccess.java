package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;

import java.util.HashMap;

public class DialogVideoUploadSuccess {

    public static DialogVideoUploadSuccess ddAlert = null;
    private Dialog mDialog;

    public static DialogVideoUploadSuccess getInstance() {
        if (ddAlert == null){
            ddAlert = new DialogVideoUploadSuccess();
        }
        return ddAlert;
    }

    public void showAlert(Context context, String cover_url, boolean cancelable) {
        mDialog = new Dialog(context, R.style.DDLoading);
        mDialog.setContentView(R.layout.dialog_video_upload_success);

        TextView tvDDTitle = mDialog.findViewById(R.id.tvDDTitle);
        ImageView ivVideoCover = mDialog.findViewById(R.id.ivVideoCover);
        TextView tvOkay = mDialog.findViewById(R.id.tvOkay);
        Glide.with(context).load(cover_url).placeholder(R.drawable.dd_logo_placeholder).into(ivVideoCover);

        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void hideAlert() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
