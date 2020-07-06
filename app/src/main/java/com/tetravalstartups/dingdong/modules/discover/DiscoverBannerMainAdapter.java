package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;

import java.util.List;

public class DiscoverBannerMainAdapter extends RecyclerView.Adapter<DiscoverBannerMainAdapter.ViewHolder> {

    Context context;
    List<DiscoverBannerMain> discoverBannerMainList;

    public DiscoverBannerMainAdapter(Context context, List<DiscoverBannerMain> discoverBannerMainList) {
        this.context = context;
        this.discoverBannerMainList = discoverBannerMainList;
    }

    @NonNull
    @Override
    public DiscoverBannerMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_slider_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverBannerMainAdapter.ViewHolder holder, int position) {
        DiscoverBannerMain discoverBannerMain = discoverBannerMainList.get(position);
        Glide.with(context).load(discoverBannerMain.getBanner_url()).into(holder.ivBanner);
        holder.frameSlider.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return discoverBannerMainList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameSlider;
        ImageView ivBanner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            frameSlider = itemView.findViewById(R.id.frameSlider);
            ivBanner = itemView.findViewById(R.id.ivBanner);
        }
    }
}
