package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;

public class DDDeleteVideoAlert {

    public static DDDeleteVideoAlert ddAlert = null;
    private Dialog mDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static DDDeleteVideoAlert getInstance() {
        if (ddAlert == null){
            ddAlert = new DDDeleteVideoAlert();
        }
        return ddAlert;
    }

    public void showAlert(Context context, String cover_url, String video_id, boolean cancelable) {
        mDialog = new Dialog(context, R.style.DDLoading);
        mDialog.setContentView(R.layout.dd_alert_video_delete_layout);

        TextView tvDDTitle = mDialog.findViewById(R.id.tvDDTitle);
        ImageView ivVideoCover = mDialog.findViewById(R.id.ivVideoCover);
        TextView tvDelete = mDialog.findViewById(R.id.tvDelete);
        TextView tvCancel = mDialog.findViewById(R.id.tvCancel);

        tvDDTitle.setText("Delete Video?");
        Glide.with(context).load(cover_url).placeholder(R.drawable.dd_logo_placeholder).into(ivVideoCover);

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("videos")
                        .document(video_id)
                        .delete();
                mDialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
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
