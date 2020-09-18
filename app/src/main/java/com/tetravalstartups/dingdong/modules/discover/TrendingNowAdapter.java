package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    }

    @Override
    public int getItemCount() {
        return trendingNowModelList.size();
    }

    public class TrendingNowViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivTrendingNow;
        public TrendingNowViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTrendingNow = itemView.findViewById(R.id.ivTrendingNow);
        }
    }
}
