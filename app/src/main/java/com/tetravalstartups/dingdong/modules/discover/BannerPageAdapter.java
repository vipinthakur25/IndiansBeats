package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;

import java.util.List;

public class BannerPageAdapter extends RecyclerView.Adapter<BannerPageAdapter.BannerViewHolder> {
    private List<DiscoverBannerResponse> discoverBannerModelList;
    private ViewPager2 viewPager2;
    private Context context;

    public BannerPageAdapter(List<DiscoverBannerResponse> discoverBannerModelList, ViewPager2 viewPager2, Context context) {
        this.discoverBannerModelList = discoverBannerModelList;
        this.viewPager2 = viewPager2;
        this.context = context;
    }


    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_discover_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        DiscoverBannerResponse bannerResponse = discoverBannerModelList.get(position);
        Glide.with(context).load(bannerResponse.getBannerUrl()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivBannerPhoto);
        if (position == discoverBannerModelList.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return discoverBannerModelList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBannerPhoto;


        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);

            ivBannerPhoto = itemView.findViewById(R.id.ivBannerPhoto);

        }

    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
        discoverBannerModelList.addAll(discoverBannerModelList);
        notifyDataSetChanged();
        }
    };

}
