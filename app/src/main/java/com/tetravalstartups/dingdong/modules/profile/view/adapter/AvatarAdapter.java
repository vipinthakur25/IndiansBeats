package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfile;
import com.tetravalstartups.dingdong.modules.profile.model.Avatar;
import com.tetravalstartups.dingdong.modules.profile.view.activity.EditProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    Context context;
    List<Avatar> avatarList;
    String selected_avatar;
    private RequestInterface requestInterface;
    private Master master;
    private static final String TAG = "AvatarAdapter";

    public AvatarAdapter(Context context, List<Avatar> avatarList) {
        this.context = context;
        this.avatarList = avatarList;
    }

    @NonNull
    @Override
    public AvatarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        master = new Master(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarAdapter.ViewHolder holder, int position) {
        final Avatar avatar = avatarList.get(position);
        Glide.with(context)
                .load(avatar.getPhoto())
                .placeholder(R.drawable.dd_logo_placeholder)
                .into(holder.ivPhoto);
        holder.lvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<PublicProfile> call = requestInterface.editUserPhoto(master.getId(), avatar.getPhoto());
                call.enqueue(new Callback<PublicProfile>() {
                    @Override
                    public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                        new Master(context).setUser(response.body().getPublicProfileResponse());
                        ((EditProfileActivity)context).closeSheet();
                    }

                    @Override
                    public void onFailure(Call<PublicProfile> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return avatarList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lvPhoto;
        ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lvPhoto = itemView.findViewById(R.id.lvPhoto);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }
    }

}
