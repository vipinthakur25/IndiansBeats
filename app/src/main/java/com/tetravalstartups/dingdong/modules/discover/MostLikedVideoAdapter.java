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

public class MostLikedVideoAdapter extends RecyclerView.Adapter<MostLikedVideoAdapter.MostLikedideoViewHolder> {

    private Context context;
    private List<MostLikedVideoResponse> mostLikedVideoResponseList;


    public MostLikedVideoAdapter(Context context, List<MostLikedVideoResponse> mostLikedVideoResponseList) {
        this.context = context;
        this.mostLikedVideoResponseList = mostLikedVideoResponseList;
    }

    @NonNull
    @Override
    public MostLikedVideoAdapter.MostLikedideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.most_liked_video_item, parent, false);
        return new MostLikedideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostLikedVideoAdapter.MostLikedideoViewHolder holder, int position) {
        MostLikedVideoResponse mostLikedVideoResponse = mostLikedVideoResponseList.get(position);
        Glide.with(context).load(mostLikedVideoResponse.getVideoThumbnail()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivThumbnail);
        holder.tvLikeCount.setText(""+mostLikedVideoResponse.getLikesCount());
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class MostLikedideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail;
        private TextView tvLikeCount;
        public MostLikedideoViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
        }
    }
}
