package com.tetravalstartups.dingdong.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;

public class LightBox {
    Context context;
    Dialog dialog;

    public LightBox(Context context) {
        this.context = context;
    }

    public void showLightBox(String photo) {
        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.light_box_layout);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ImageView ivLightBox = dialog.findViewById(R.id.ivLightBox);
            Glide.with(context)
                    .load(photo)
                    .placeholder(R.drawable.dd_logo_placeholder)
                    .into(ivLightBox);

        dialog.show();
    }

    public void hideLightBox() {
        dialog.dismiss();
    }

}
