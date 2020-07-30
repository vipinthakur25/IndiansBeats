package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.create.sound.SoundActivity;
import com.tetravalstartups.dingdong.modules.record.PreviewActivity;

public class DDRemoveSoundAlert {

    public static DDRemoveSoundAlert ddAlert = null;
    private Dialog mDialog;

    public static DDRemoveSoundAlert getInstance() {
        if (ddAlert == null){
            ddAlert = new DDRemoveSoundAlert();
        }
        return ddAlert;
    }

    public void showAlert(Context context, String title, String message, boolean cancelable) {
        mDialog = new Dialog(context, R.style.DDAlert);
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
