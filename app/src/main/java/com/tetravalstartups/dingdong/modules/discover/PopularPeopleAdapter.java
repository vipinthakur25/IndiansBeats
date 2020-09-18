package com.tetravalstartups.dingdong.modules.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopularPeopleAdapter extends RecyclerView.Adapter<PopularPeopleAdapter.PopularPeoplerViewHolder> {

    private Context context;
    private List<PopularPeopleModel> popularPeopleModelList;


    public PopularPeopleAdapter(Context context, List<PopularPeopleModel> popularPeopleModelList) {
        this.context = context;
        this.popularPeopleModelList = popularPeopleModelList;
    }

    @NonNull
    @Override
    public PopularPeopleAdapter.PopularPeoplerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_discover_popular_people, parent, false);
        return new PopularPeoplerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularPeopleAdapter.PopularPeoplerViewHolder holder, int position) {
        holder.tvUsername.setText(popularPeopleModelList.get(position).getUsername());
        holder.tvFollowersCount.setText(popularPeopleModelList.get(position).getFollowers_count());
        holder.ivPopularPeaople.setImageResource(popularPeopleModelList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return popularPeopleModelList.size();
    }

    public class PopularPeoplerViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivPopularPeaople;
        private TextView tvUsername;
        private TextView tvFollowersCount;
        public PopularPeoplerViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPopularPeaople = itemView.findViewById(R.id.ivPopularPeaople);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvFollowersCount = itemView.findViewById(R.id.tvFollowersCount);
        }
    }
}
