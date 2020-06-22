package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.model.Followers;

import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    Context context;
    List<Followers> followersList;

    public FollowerAdapter(Context context, List<Followers> followersList) {
        this.context = context;
        this.followersList = followersList;
    }

    @NonNull
    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.ViewHolder holder, int position) {
        Followers followers = followersList.get(position);
        if (followers.getPhoto().isEmpty()){
            holder.ivPhoto.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_profile_inactive));
        } else {
            Glide.with(context).load(followers.getPhoto()).placeholder(R.drawable.dingdong_placeholder).into(holder.ivPhoto);
        }
        holder.tvName.setText(followers.getName());
        holder.tvHandle.setText("@"+followers.getHandle());
        if (followers.isFollowing()){
            holder.tvFollow.setText(R.string.following);
            holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorDisableText));
            holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_disabled));
        } else {
            holder.tvFollow.setText(R.string.follow);
            holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_white));
        }
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhFollower;
        ImageView ivPhoto;
        TextView tvHandle, tvName, tvFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lhFollower = itemView.findViewById(R.id.lhFollower);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvName = itemView.findViewById(R.id.tvName);
            tvFollow = itemView.findViewById(R.id.tvFollow);

        }
    }
}
