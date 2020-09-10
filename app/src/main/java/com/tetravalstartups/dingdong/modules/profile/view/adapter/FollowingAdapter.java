package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.PhoneActivity;
import com.tetravalstartups.dingdong.modules.profile.model.Follow;
import com.tetravalstartups.dingdong.modules.profile.model.following.FollowingResponse;
import com.tetravalstartups.dingdong.modules.profile.view.activity.FollowingActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private static final String TAG = "FollowingAdapter";
    Context context;
    List<FollowingResponse> followingsList;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    Master master;
    private RequestInterface requestInterface;

    public FollowingAdapter(Context context, List<FollowingResponse> followingsList) {
        this.context = context;
        this.followingsList = followingsList;
    }

    @NonNull
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item, parent, false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        master = new Master(context);
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ViewHolder holder, int position) {
        FollowingResponse followings = followingsList.get(position);

        Glide.with(context).load(followings.getPhoto()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivPhoto);
        holder.tvName.setText(followings.getName());
        holder.tvHandle.setText(String.format("@%s", followings.getHandle()));

        if (firebaseAuth.getCurrentUser() == null) {
            holder.tvUnfollow.setVisibility(View.VISIBLE);
            holder.tvUnfollow.setText(R.string.follow);
            holder.tvUnfollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
            holder.tvUnfollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));
        } else {
            if (followings.getId().equals(master.getId())) {
                holder.tvUnfollow.setVisibility(View.GONE);
            } else {
                holder.tvUnfollow.setVisibility(View.VISIBLE);
                holder.tvUnfollow.setText(R.string.unfollow);
                holder.tvUnfollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_disabled));
                holder.tvUnfollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }

        }

        holder.tvUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() == null) {
                    context.startActivity(new Intent(context, PhoneActivity.class));
                } else {
                    doUnfollow(followings);
                }
            }
        });

        holder.lhFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", followings.getId());
                context.startActivity(intent);
            }
        });

    }

    private void doUnfollow(FollowingResponse followings) {
        Call<Follow> call = requestInterface.doFollowUser(master.getId(), followings.getId());
        call.enqueue(new Callback<Follow>() {
            @Override
            public void onResponse(Call<Follow> call, Response<Follow> response) {
                if (response.code() == 200) {
                    notifyDataSetChanged();
                    ((FollowingActivity) context).fetchFollowing();
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Follow> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return followingsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhFollowing;
        ImageView ivPhoto;
        TextView tvHandle, tvName, tvUnfollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lhFollowing = itemView.findViewById(R.id.lhFollowing);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvName = itemView.findViewById(R.id.tvName);
            tvUnfollow = itemView.findViewById(R.id.tvUnfollow);

        }
    }
}
