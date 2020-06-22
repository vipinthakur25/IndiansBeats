package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.Avatar;
import com.tetravalstartups.dingdong.modules.profile.view.activity.EditProfileActivity;
import com.tetravalstartups.dingdong.utils.ProfilePhotoBottomSheet;

import java.util.HashMap;
import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    Context context;
    List<Avatar> avatarList;
    String selected_avatar;

    public AvatarAdapter(Context context, List<Avatar> avatarList) {
        this.context = context;
        this.avatarList = avatarList;
    }

    @NonNull
    @Override
    public AvatarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarAdapter.ViewHolder holder, int position) {
        final Avatar avatar = avatarList.get(position);
        Glide.with(context)
                .load(avatar.getPhoto())
                .placeholder(R.drawable.dingdong_placeholder)
                .into(holder.ivPhoto);
        holder.lvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("photo", avatar.getPhoto());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .document(new Master(context).getId())
                        .update(hashMap);
                ProfilePhotoBottomSheet bottomSheet = new ProfilePhotoBottomSheet();
                bottomSheet.dismiss();
                //((EditProfileActivity)context).finish();
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
