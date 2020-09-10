package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.create.sound.SoundActivity;

public class DDAlert {

    public static DDAlert ddAlert = null;
    private Dialog mDialog;

    public static DDAlert getInstance() {
        if (ddAlert == null){
            ddAlert = new DDAlert();
        }
        return ddAlert;
    }

    public void showAlert(Context context, String title, String message, boolean cancelable) {
        mDialog = new Dialog(context, R.style.DDAlert);
        mDialog.setContentView(R.layout.dialog_video_upload_success);

        TextView tvOk = mDialog.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
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
