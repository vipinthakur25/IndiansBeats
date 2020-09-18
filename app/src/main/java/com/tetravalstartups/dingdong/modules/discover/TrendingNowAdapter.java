package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;

import java.util.List;

public class TrendingNowAdapter extends RecyclerView.Adapter<TrendingNowAdapter.TrendingNowViewHolder> {

    private Context context;
    private List<TrendingNowModel> trendingNowModelList;

    public TrendingNowAdapter(Context context, List<TrendingNowModel> trendingNowModelList) {
        this.context = context;
        this.trendingNowModelList = trendingNowModelList;
    }

    @NonNull
    @Override
    public TrendingNowAdapter.TrendingNowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_discover_trending_now, parent, false);
        return new TrendingNowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingNowAdapter.TrendingNowViewHolder holder, int position) {
        holder.ivTrendingNow.setImageResource(trendingNowModelList.get(position).getImage());
        holder.tvHashTag.setText(trendingNowModelList.get(position).getHashtags());
        holder.tvVideoCount.setText(trendingNowModelList.get(position).getVideoCount());
    }

    @Override
    public int getItemCount() {
        return trendingNowModelList.size();
    }

    public class TrendingNowViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivTrendingNow;
    private TextView tvHashTag;
    private TextView tvVideoCount;
        public TrendingNowViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTrendingNow = itemView.findViewById(R.id.ivTrendingNow);
            tvHashTag = itemView.findViewById(R.id.tvHashTag);
            tvVideoCount = itemView.findViewById(R.id.tvVideoCount);
        }
    }
}
