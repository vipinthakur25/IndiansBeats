package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tetravalstartups.dingdong.R;

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
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.getWindow().
//                setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dd_alert_layout);

        TextView tvDDTitle = mDialog.findViewById(R.id.tvDDTitle);
        TextView tvDDMessage = mDialog.findViewById(R.id.tvDDMessage);
        TextView tvDDAction = mDialog.findViewById(R.id.tvDDAction);

        tvDDTitle.setText(title);
        tvDDMessage.setText(message);

        tvDDAction.setOnClickListener(new View.OnClickListener() {
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
