package com.tetravalstartups.dingdong.modules.notification.view.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.notification.model.Notification;
import com.tylersuehr.socialtextview.SocialTextView;

public class LikeViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ImageView ivSenderUserPhoto;
    private SocialTextView stvMessage;
    private ImageView ivVideoThumbnail;

    public LikeViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        ivSenderUserPhoto = itemView.findViewById(R.id.ivSenderUserPhoto);
        stvMessage = itemView.findViewById(R.id.stvMessage);
        ivVideoThumbnail = itemView.findViewById(R.id.ivVideoThumbnail);
    }

    public void setLikeNotification(Notification notification) {
        Glide.with(context).load(notification.getSender_user_photo()).placeholder(R.drawable.dd_logo_placeholder).into(ivSenderUserPhoto);
        Glide.with(context).load(notification.getVideo_thumbnail()).placeholder(R.drawable.dd_logo_placeholder).into(ivVideoThumbnail);
        stvMessage.setLinkText("@"+notification.getSender_user_handle()+" liked your video.");
    }

}