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
import com.tetravalstartups.dingdong.modules.profile.model.Followers;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;

import java.util.HashMap;
import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    Context context;
    List<Followers> followersList;
    Master master;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    private String profile_type;

    public FollowerAdapter(Context context, List<Followers> followersList, String profile_type) {
        this.context = context;
        this.followersList = followersList;
        this.profile_type = profile_type;
    }

    @NonNull
    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_list_item, parent, false);
        master = new Master(context);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.ViewHolder holder, int position) {
        Followers followers = followersList.get(position);

        if (followers.getPhoto().isEmpty()) {
            holder.ivPhoto.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_profile_inactive));
        } else {
            Glide.with(context).load(followers.getPhoto()).placeholder(R.drawable.dd_logo_placeholder).into(holder.ivPhoto);
        }
        holder.tvName.setText(followers.getName());
        holder.tvHandle.setText("@" + followers.getHandle());

        holder.tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firebaseAuth.getCurrentUser() != null) {

                    Query followingQuery = db.collection("users").document(master.getId())
                            .collection("following").whereEqualTo("id", followers.getId());
                    followingQuery.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (followers.getId().equals(master.getId())) {
                                        holder.tvFollow.setVisibility(View.GONE);
                                    } else if (task.getResult().getDocuments().isEmpty()) {
                                        holder.tvFollow.setVisibility(View.VISIBLE);
                                        holder.tvFollow.setText("Follow");
                                        holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
                                        holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));

                                        HashMap hmFollowing = new HashMap();
                                        hmFollowing.put("id", followers.getId());
                                        hmFollowing.put("handle", followers.getHandle());
                                        hmFollowing.put("photo", followers.getPhoto());
                                        hmFollowing.put("name", followers.getName());


                                        HashMap hmFollower = new HashMap();
                                        hmFollower.put("id", master.getId());
                                        hmFollower.put("handle", master.getHandle());
                                        hmFollower.put("photo", master.getPhoto());
                                        hmFollower.put("name", master.getName());

                                        db.collection("users")
                                                .document(master.getId())
                                                .collection("following")
                                                .document(followers.getId())
                                                .set(hmFollowing);

                                        db.collection("users")
                                                .document(followers.getId())
                                                .collection("followers")
                                                .document(followers.getId())
                                                .set(hmFollower);

                                    } else {
                                        holder.tvFollow.setVisibility(View.VISIBLE);
                                        holder.tvFollow.setText("Unfollow");
                                        holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_white));
                                        holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                                        db.collection("users")
                                                .document(master.getId())
                                                .collection("following")
                                                .document(followers.getId())
                                                .delete();

                                        db.collection("users")
                                                .document(followers.getId())
                                                .collection("followers")
                                                .document(followers.getId())
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
                    .collection("following").whereEqualTo("id", followers.getId());
            followingQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (followers.getId().equals(master.getId())) {
                        holder.tvFollow.setVisibility(View.GONE);
                    } else if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                        holder.tvFollow.setVisibility(View.VISIBLE);
                        holder.tvFollow.setText("Follow");
                        holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
                        holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));
                    } else {
                        holder.tvFollow.setVisibility(View.VISIBLE);
                        holder.tvFollow.setText("Unfollow");
                        holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_white));
                        holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                }
            });

        } else {
            holder.tvFollow.setVisibility(View.VISIBLE);
            holder.tvFollow.setText("Follow");
            holder.tvFollow.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
            holder.tvFollow.setTextColor(context.getResources().getColor(R.color.colorTextTitle));
        }

        holder.lhFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", followers.getId());
                context.startActivity(intent);
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
