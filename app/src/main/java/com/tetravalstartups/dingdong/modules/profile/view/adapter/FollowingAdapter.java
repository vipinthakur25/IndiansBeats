package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.LoginActivity;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.model.Followings;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;

import java.util.HashMap;
import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    Context context;
    List<Followings> followingsList;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    Master master;
    String profile_type;

    public FollowingAdapter(Context context, List<Followings> followingsList, String profile_type) {
        this.context = context;
        this.followingsList = followingsList;
        this.profile_type = profile_type;
    }

    @NonNull
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item, parent, false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        master = new Master(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ViewHolder holder, int position) {
        Followings followings = followingsList.get(position);

        if (followings.getPhoto().isEmpty()){
            holder.ivPhoto.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_profile_inactive));
        } else {
            Glide.with(context).load(followings.getPhoto()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivPhoto);
        }
        holder.tvName.setText(followings.getName());
        holder.tvHandle.setText("@"+followings.getHandle());

        holder.tvUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firebaseAuth.getCurrentUser() != null) {


                    Query followingQuery = db.collection("users").document(master.getId())
                            .collection("following").whereEqualTo("id", followings.getId());
                    followingQuery.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (followings.getId().equals(master.getId())) {
                                        holder.tvUnfollow.setVisibility(View.GONE);
                                    } else if (task.getResult().getDocuments().isEmpty()) {
                                        holder.tvUnfollow.setVisibility(View.VISIBLE);
                                        holder.tvUnfollow.setText("Follow");
                                        holder.tvUnfollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
                                        holder.tvUnfollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));

                                        HashMap hmFollowing = new HashMap();
                                        hmFollowing.put("id", followings.getId());
                                        hmFollowing.put("handle", followings.getHandle());
                                        hmFollowing.put("photo", followings.getPhoto());
                                        hmFollowing.put("name", followings.getName());


                                        HashMap hmFollower = new HashMap();
                                        hmFollower.put("id", master.getId());
                                        hmFollower.put("handle", master.getHandle());
                                        hmFollower.put("photo", master.getPhoto());
                                        hmFollower.put("name", master.getName());

                                        db.collection("users")
                                                .document(master.getId())
                                                .collection("following")
                                                .document(followings.getId())
                                                .set(hmFollowing);

                                        db.collection("users")
                                                .document(followings.getId())
                                                .collection("followers")
                                                .document(followings.getId())
                                                .set(hmFollower);

                                    } else {
                                        holder.tvUnfollow.setVisibility(View.VISIBLE);
                                        holder.tvUnfollow.setText("Unfollow");
                                        holder.tvUnfollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_white));
                                        holder.tvUnfollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                                        db.collection("users")
                                                .document(master.getId())
                                                .collection("following")
                                                .document(followings.getId())
                                                .delete();

                                        db.collection("users")
                                                .document(followings.getId())
                                                .collection("followers")
                                                .document(followings.getId())
                                                .delete();

                                    }
                                }
                            });
                } else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }

            }
        });

        if (firebaseAuth.getCurrentUser() != null) {

            Query followingQuery = db.collection("users").document(master.getId())
                    .collection("following").whereEqualTo("id", followings.getId());
            followingQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (followings.getId().equals(master.getId())) {
                        holder.tvUnfollow.setVisibility(View.GONE);
                    } else if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        holder.tvUnfollow.setVisibility(View.VISIBLE);
                        holder.tvUnfollow.setText("Follow");
                        holder.tvUnfollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
                        holder.tvUnfollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));
                    } else {
                        holder.tvUnfollow.setVisibility(View.VISIBLE);
                        holder.tvUnfollow.setText("Unfollow");
                        holder.tvUnfollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_white));
                        holder.tvUnfollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                }
            });

        } else {
            holder.tvUnfollow.setVisibility(View.VISIBLE);
            holder.tvUnfollow.setText("Follow");
            holder.tvUnfollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
            holder.tvUnfollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));
        }

        holder.lhFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", followings.getId());
                context.startActivity(intent);
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
