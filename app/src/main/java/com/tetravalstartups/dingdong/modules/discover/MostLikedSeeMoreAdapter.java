package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;

import java.util.List;

public class MostLikedSeeMoreAdapter extends RecyclerView.Adapter<MostLikedSeeMoreAdapter.MostLikedSeeMoreViewHolder> {
    private Context context;
    private List<MostLikedVideoResponse> mostLikedVideoResponseList;


    public MostLikedSeeMoreAdapter(Context context, List<MostLikedVideoResponse> mostLikedVideoResponseList) {
        this.context = context;
        this.mostLikedVideoResponseList = mostLikedVideoResponseList;
    }

    @NonNull
    @Override
    public MostLikedSeeMoreAdapter.MostLikedSeeMoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.liked_video_list_item, parent, false);
        return new MostLikedSeeMoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostLikedSeeMoreAdapter.MostLikedSeeMoreViewHolder holder, int position) {
        MostLikedVideoResponse mostLikedVideoResponse = mostLikedVideoResponseList.get(position);
        Glide.with(context).load(mostLikedVideoResponse.getVideoThumbnail()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivThumbnail);
        holder.tvViews.setText(""+mostLikedVideoResponse.getViewCount());
    }

    @Override
    public int getItemCount() {
        return mostLikedVideoResponseList.size();
    }

    public class MostLikedSeeMoreViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail;
        private TextView tvViews;
        public MostLikedSeeMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvViews = itemView.findViewById(R.id.tvViews);
        }
    }
}
