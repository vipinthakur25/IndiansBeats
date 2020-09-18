package com.tetravalstartups.dingdong.modules.notification.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.notification.model.Notification;
import com.tetravalstartups.dingdong.modules.notification.view.viewholder.CommentViewHolder;
import com.tetravalstartups.dingdong.modules.notification.view.viewholder.FollowViewHolder;
import com.tetravalstartups.dingdong.modules.notification.view.viewholder.LikeViewHolder;
import com.tetravalstartups.dingdong.modules.notification.view.viewholder.MentionViewHolder;
import com.tetravalstartups.dingdong.modules.notification.view.viewholder.NoticeViewHolder;
import com.tetravalstartups.dingdong.modules.notification.view.viewholder.RewardViewHolder;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Notification> notificationArrayList;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Notification.NOTIFICATION_TYPE_FOLLOW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notify_follow, parent, false);
                return new FollowViewHolder(view);

            case Notification.NOTIFICATION_TYPE_LIKE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notify_liked, parent, false);
                return new LikeViewHolder(view);

            case Notification.NOTIFICATION_TYPE_MENTION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notify_mention, parent, false);
                return new MentionViewHolder(view);

            case Notification.NOTIFICATION_TYPE_REWARD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notify_reward, parent, false);
                return new RewardViewHolder(view);

            case Notification.NOTIFICATION_TYPE_NOTICE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notify_notice, parent, false);
                return new NoticeViewHolder(view);

            case Notification.NOTIFICATION_TYPE_COMMENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notify_comment, parent, false);
                return new CommentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Notification notification = notificationArrayList.get(position);
        if (notification != null) {
            switch (notification.getType()) {

                case Notification.NOTIFICATION_TYPE_FOLLOW:
                    ((FollowViewHolder) holder).setFollowNotification(notification);
                    break;

                case Notification.NOTIFICATION_TYPE_LIKE:
                    ((LikeViewHolder) holder).setLikeNotification(notification);
                    break;

                case Notification.NOTIFICATION_TYPE_MENTION:
                    ((MentionViewHolder) holder).setMentionNotification(notification);
                    break;

                case Notification.NOTIFICATION_TYPE_REWARD:
                    ((RewardViewHolder) holder).setRewardNotification(notification);
                    break;

                case Notification.NOTIFICATION_TYPE_NOTICE:
                    ((NoticeViewHolder) holder).setNoticeNotification(notification);
                    break;

                case Notification.NOTIFICATION_TYPE_COMMENT:
                    ((CommentViewHolder) holder).setCommentNotification(notification);
                    break;

            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (notificationArrayList.get(position).getType()) {
            case Notification.NOTIFICATION_TYPE_FOLLOW:
                return Notification.NOTIFICATION_TYPE_FOLLOW;

            case Notification.NOTIFICATION_TYPE_LIKE:
                return Notification.NOTIFICATION_TYPE_LIKE;

            case Notification.NOTIFICATION_TYPE_MENTION:
                return Notification.NOTIFICATION_TYPE_MENTION;

            case Notification.NOTIFICATION_TYPE_REWARD:
                return Notification.NOTIFICATION_TYPE_REWARD;

            case Notification.NOTIFICATION_TYPE_NOTICE:
                return Notification.NOTIFICATION_TYPE_NOTICE;

            case Notification.NOTIFICATION_TYPE_COMMENT:
                return Notification.NOTIFICATION_TYPE_COMMENT;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }
}
