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

public class MostViewVideoAdapter extends RecyclerView.Adapter<MostViewVideoAdapter.MostViewVideoViewHolder> {
    private Context context;
    private List<MostViewVideoResponse> mostViewVideoResponseList;


    public MostViewVideoAdapter(Context context, List<MostViewVideoResponse> mostViewVideoResponseList) {
        this.context = context;
        this.mostViewVideoResponseList = mostViewVideoResponseList;
    }

    @NonNull
    @Override
    public MostViewVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.most_view_video_item, parent, false);
        return new MostViewVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostViewVideoViewHolder holder, int position) {
        MostViewVideoResponse mostViewVideoResponse = mostViewVideoResponseList.get(position);
        Glide.with(context).load(mostViewVideoResponse.getVideoThumbnail()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivThumbnail);
        holder.tvViewsCount.setText(""+mostViewVideoResponse.getViewCount());
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MostViewVideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail;
        private TextView tvViewsCount;
        public MostViewVideoViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvViewsCount = itemView.findViewById(R.id.tvViewsCount);
        }
    }
}
