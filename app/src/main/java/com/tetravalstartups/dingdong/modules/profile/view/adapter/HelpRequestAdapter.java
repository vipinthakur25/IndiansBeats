package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.model.HelpRequest;
import com.tetravalstartups.dingdong.modules.profile.model.HelpRequestResponse;
import com.tetravalstartups.dingdong.utils.Constant;

import java.util.List;

public class HelpRequestAdapter extends RecyclerView.Adapter<HelpRequestAdapter.HelpRequestViewHolder> {

    private Context context;
    private List<HelpRequestResponse> helpRequestList;


    public HelpRequestAdapter(Context context, List<HelpRequestResponse> helpRequestList) {
        this.context = context;
        this.helpRequestList = helpRequestList;
    }

    @NonNull
    @Override
    public HelpRequestAdapter.HelpRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.help_request_item_layout, parent, false);
        return new HelpRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpRequestAdapter.HelpRequestViewHolder holder, int position) {
        holder.tvMessageId.setText(helpRequestList.get(position).getId());
        holder.tvTimeDate.setText(helpRequestList.get(position).getRequestDate());
        holder.tvAlternateNumber.setText(helpRequestList.get(position).getAlternatePhone());
        holder.tvAlternateEmail.setText(helpRequestList.get(position).getAlternateEmail());
        if (helpRequestList.get(position).getStatus() == Constant.STATUS_QUERY_PENDING) {
            holder.ivPending.setImageResource(R.drawable.bg_red_circle);
        } else if (helpRequestList.get(position).getStatus() == Constant.STATUS_QUERY_ENQUEUE) {
            holder.ivPending.setImageResource(R.drawable.bg_yellow_circle);
        } else if (helpRequestList.get(position).getStatus() == Constant.STATUS_QUERY_RESOLVED) {
            holder.ivPending.setImageResource(R.drawable.bg_green_circle);
        }

    }

    @Override
    public int getItemCount() {
        return helpRequestList.size();
    }

    public class HelpRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessageId;
        private TextView tvTimeDate;
        private ImageView ivPending;
        private TextView tvAlternateNumber;
        private TextView tvAlternateEmail;

        public HelpRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageId = itemView.findViewById(R.id.tvMessageId);
            tvTimeDate = itemView.findViewById(R.id.tvTimeDate);
            ivPending = itemView.findViewById(R.id.ivPending);
            tvAlternateNumber = itemView.findViewById(R.id.tvAlternateNumber);
            tvAlternateEmail = itemView.findViewById(R.id.tvAlternateEmail);

        }
    }
}
