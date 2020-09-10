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
import com.tetravalstartups.dingdong.modules.profile.model.followers.FollowersResponse;
import com.tetravalstartups.dingdong.modules.profile.view.activity.FollowersActivity;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    private static final String TAG = "FollowerAdapter";
    Context context;
    List<FollowersResponse> followersList;
    Master master;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private RequestInterface requestInterface;

    public FollowerAdapter(Context context, List<FollowersResponse> followersList) {
        this.context = context;
        this.followersList = followersList;
    }

    @NonNull
    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_list_item, parent, false);
        master = new Master(context);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.ViewHolder holder, int position) {
        FollowersResponse followers = followersList.get(position);

        Glide.with(context).load(followers.getPhoto()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivPhoto);
        holder.tvName.setText(followers.getName());
        holder.tvHandle.setText(String.format("@%s", followers.getHandle()));

        if (firebaseAuth.getCurrentUser() == null) {
            holder.tvFollow.setVisibility(View.VISIBLE);
            holder.tvFollow.setText(R.string.follow);
            holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
            holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));

        } else {

            if (followers.getId().equals(master.getId())) {
                holder.tvFollow.setVisibility(View.GONE);

            } else if (followers.getMyfollow().equals("followBack")) {
                holder.tvFollow.setVisibility(View.VISIBLE);
                holder.tvFollow.setText(R.string.follow_back);
                holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
                holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));

            } else if (followers.getMyfollow().equals("following")) {
                holder.tvFollow.setVisibility(View.VISIBLE);
                holder.tvFollow.setText(R.string.unfollow);
                holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_disabled));
                holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        holder.tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() == null) {
                    context.startActivity(new Intent(context, PhoneActivity.class));
                } else {
                    doFollow(followers);
                }
            }
        });


        holder.lhFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", followers.getId());
                context.startActivity(intent);
            }
        });

    }

    private void doFollow(FollowersResponse followers) {
        Call<Follow> call = requestInterface.doFollowUser(master.getId(), followers.getId());
        call.enqueue(new Callback<Follow>() {
            @Override
            public void onResponse(Call<Follow> call, Response<Follow> response) {
                if (response.code() == 200) {
                    notifyDataSetChanged();
                    ((FollowersActivity) context).fetchFollowers();
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
        return followersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhFollower;
        ImageView ivPhoto;
        TextView tvHandle, tvName, tvFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lhFollower = itemView.findViewById(R.id.lhFollower);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvName = itemView.findViewById(R.id.tvName);
            tvFollow = itemView.findViewById(R.id.tvFollow);
        }
    }
}
