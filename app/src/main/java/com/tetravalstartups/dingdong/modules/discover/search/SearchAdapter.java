package com.tetravalstartups.dingdong.modules.discover.search;

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
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.PhoneActivity;
import com.tetravalstartups.dingdong.modules.profile.model.Follow;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static final String TAG = "SearchAdapter";
    private Context context;
    private List<SearchResponse> searchResponseList;
    private FirebaseAuth firebaseAuth;
    private RequestInterface requestInterface;
    private Master master;

    public SearchAdapter(Context context, List<SearchResponse> searchResponseList) {
        this.context = context;
        this.searchResponseList = searchResponseList;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        firebaseAuth = FirebaseAuth.getInstance();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        master = new Master(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        SearchResponse searchResponse = searchResponseList.get(position);

        Glide.with(context)
                .load(searchResponse.getPhoto())
                .placeholder(R.drawable.dd_logo_placeholder)
                .into(holder.ivPhoto);

        holder.tvName.setText(searchResponse.getName());
        holder.tvHandle.setText(searchResponse.getHandle());

        if (firebaseAuth.getCurrentUser() == null) {
            holder.tvFollow.setVisibility(View.VISIBLE);
            holder.tvFollow.setText(context.getResources().getString(R.string.follow));
            holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
            holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));
            holder.tvFollow.setVisibility(View.VISIBLE);

        } else {
            if (searchResponse.getId().equals(master.getId())) {
                holder.tvFollow.setVisibility(View.GONE);

            } else if (searchResponse.getMyfollow().equals("notfollowed")) {
                holder.tvFollow.setVisibility(View.VISIBLE);
                holder.tvFollow.setText("Follow");
                holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
                holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));
                holder.tvFollow.setVisibility(View.VISIBLE);

            } else if (searchResponse.getMyfollow().equals("following")) {
                holder.tvFollow.setVisibility(View.VISIBLE);
                holder.tvFollow.setText("Unfollow");
                holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_disabled));
                holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.tvFollow.setVisibility(View.VISIBLE);

            } else if (searchResponse.getMyfollow().equals("followBack")) {
                holder.tvFollow.setVisibility(View.VISIBLE);
                holder.tvFollow.setText("Follow Back");
                holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
                holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));
                holder.tvFollow.setVisibility(View.VISIBLE);

            }
        }

        holder.tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (searchResponse.getMyfollow().equals("following")) {
                        Call<Follow> call = requestInterface.doFollowUser(master.getId(), searchResponse.getId());
                        call.enqueue(new Callback<Follow>() {
                            @Override
                            public void onResponse(Call<Follow> call, Response<Follow> response) {
                                Log.e(TAG, "onResponse: " + response.message());
                                if (response.code() == 200) {
                                    holder.tvFollow.setVisibility(View.VISIBLE);
                                    holder.tvFollow.setText("Follow");
                                    holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_disabled));
                                    holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                }
                            }

                            @Override
                            public void onFailure(Call<Follow> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t.getMessage());
                            }
                        });
                    } else if (searchResponse.getMyfollow().equals("followBack")) {
                        Call<Follow> call = requestInterface.doFollowUser(master.getId(), searchResponse.getId());
                        call.enqueue(new Callback<Follow>() {
                            @Override
                            public void onResponse(Call<Follow> call, Response<Follow> response) {
                                Log.e(TAG, "onResponse: " + response.message());
                                if (response.code() == 200) {
                                    holder.tvFollow.setVisibility(View.VISIBLE);
                                    holder.tvFollow.setText("Unfollow");
                                    holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_disabled));
                                    holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                }
                            }

                            @Override
                            public void onFailure(Call<Follow> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t.getMessage());
                            }
                        });
                    }


                } else {
                    context.startActivity(new Intent(context, PhoneActivity.class));
                }
            }
        });

        holder.lhSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", searchResponse.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhSearch;
        ImageView ivPhoto;
        TextView tvHandle, tvName, tvFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lhSearch = itemView.findViewById(R.id.lhSearch);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvName = itemView.findViewById(R.id.tvName);
            tvFollow = itemView.findViewById(R.id.tvFollow);

        }
    }
}
