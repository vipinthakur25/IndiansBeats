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

public class MostViewedSeeMoreAdapter extends RecyclerView.Adapter<MostViewedSeeMoreAdapter.MostLikedSeeMoreViewHolder> {

    private Context context;
    private List<MostViewVideoResponse> mostViewVideoResponseList;

    public MostViewedSeeMoreAdapter(Context context, List<MostViewVideoResponse> mostViewVideoResponseList) {
        this.context = context;
        this.mostViewVideoResponseList = mostViewVideoResponseList;
    }


    @NonNull
    @Override
    public MostViewedSeeMoreAdapter.MostLikedSeeMoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.liked_video_list_item, parent, false);
        return new MostLikedSeeMoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostViewedSeeMoreAdapter.MostLikedSeeMoreViewHolder holder, int position) {
        MostViewVideoResponse mostViewVideoResponse = mostViewVideoResponseList.get(position);
        Glide.with(context).load(mostViewVideoResponse.getVideoThumbnail()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivThumbnail);
        holder.tvViews.setText(""+mostViewVideoResponse.getViewCount());
    }

    @Override
    public int getItemCount() {
        return mostViewVideoResponseList.size();
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
