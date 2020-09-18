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
import com.tetravalstartups.dingdong.modules.profile.model.MyComplain;
import com.tetravalstartups.dingdong.modules.profile.model.MyComplainResponse;

import java.util.List;

public class MyComplainAdapter extends RecyclerView.Adapter<MyComplainAdapter.MyComplainViewHolder> {
    private Context context;
    private List<MyComplainResponse> myComplainList;


    public MyComplainAdapter(Context context, List<MyComplainResponse> myComplainList) {
        this.context = context;
        this.myComplainList = myComplainList;
    }

    @NonNull
    @Override
    public MyComplainAdapter.MyComplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_complain_item, parent, false);
        return new MyComplainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyComplainAdapter.MyComplainViewHolder holder, int position) {
        holder.tvComplainDescription.setText(myComplainList.get(position).getDescription());
        holder.tvTimeDate.setText(myComplainList.get(position).getReportDate());
        if (myComplainList.get(position).getStatus() == 0){
            holder.ivPending.setImageResource(R.drawable.bg_red_circle);
            //holder.tvStatus.setText("Pending");
        } else if (myComplainList.get(position).getStatus() == 1){
            holder.ivPending.setImageResource(R.drawable.bg_yellow_circle);
            //holder.tvStatus.setText("Processing");
        } else if (myComplainList.get(position).getStatus() == 2){
            holder.ivPending.setImageResource(R.drawable.bg_green_circle);
           // holder.tvStatus.setText("Successful");
        }
    }

    @Override
    public int getItemCount() {
        return myComplainList.size();
    }

    public class MyComplainViewHolder extends RecyclerView.ViewHolder {

        private TextView tvComplainDescription;
        private TextView tvTimeDate;
        private TextView tvStatus;
        private ImageView ivPending;
        public MyComplainViewHolder(@NonNull View itemView) {
            super(itemView);


            tvComplainDescription = itemView.findViewById(R.id.tvComplainDescription);
            tvTimeDate = itemView.findViewById(R.id.tvTimeDate);
            ivPending = itemView.findViewById(R.id.ivPending);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
