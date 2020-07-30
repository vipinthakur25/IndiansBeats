package com.tetravalstartups.dingdong.modules.comment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.view.activity.PublicProfileActivity;

import java.util.List;


public class InVideoCommentAdapter extends RecyclerView.Adapter<InVideoCommentAdapter.ViewHolder> {

    Context context;
    List<InVideoComment> inVideoCommentList;
    private Master master;
    private FirebaseFirestore db;

    public InVideoCommentAdapter(Context context, List<InVideoComment> inVideoCommentList) {
        this.context = context;
        this.inVideoCommentList = inVideoCommentList;
    }

    @NonNull
    @Override
    public InVideoCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_video_comment_list_item, parent, false);
        master = new Master(context);
        db = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InVideoCommentAdapter.ViewHolder holder, int position) {
        InVideoComment inVideoComment = inVideoCommentList.get(position);
        Glide.with(context).load(inVideoComment.getUser_photo()).placeholder(R.drawable.dd_logo_placeholder)
                .into(holder.ivPhoto);

        holder.tvHandle.setText(inVideoComment.getUser_handle());
        holder.tvMessage.setText(inVideoComment.getMessage());

        if (inVideoComment.getUser_id().equals(master.getId())) {
            holder.ivDeleteComment.setVisibility(View.VISIBLE);
        } else {
            holder.ivDeleteComment.setVisibility(View.GONE);
        }

        holder.ivDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("videos")
                        .document(inVideoComment.getVideo_id())
                        .collection("comments")
                        .document(inVideoComment.getId())
                        .delete();
            }
        });

        holder.tvHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", inVideoComment.getUser_id());
                context.startActivity(intent);
            }
        });

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("user_id", inVideoComment.getUser_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inVideoCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhComments;
        ImageView ivPhoto, ivDeleteComment;
        TextView tvHandle, tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lhComments = itemView.findViewById(R.id.lhComments);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivDeleteComment = itemView.findViewById(R.id.ivDeleteComment);

        }
    }
}
