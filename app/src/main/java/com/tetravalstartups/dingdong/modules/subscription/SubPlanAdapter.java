package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptionResponse;

import java.util.List;

public class SubPlanAdapter extends RecyclerView.Adapter<SubPlanAdapter.ViewHolder> {

    Context context;
    List<MySubscriptionResponse> subPlanList;
    int row_index = -1;

    public SubPlanAdapter(Context context, List<MySubscriptionResponse> subPlanList) {
        this.context = context;
        this.subPlanList = subPlanList;
    }

    @NonNull
    @Override
    public SubPlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribed_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubPlanAdapter.ViewHolder holder, int position) {
        MySubscriptionResponse subPlan = subPlanList.get(position);
        holder.tvName.setText("#" + subPlan.getName());
        holder.tvUploads.setText(subPlan.getAvlUploads() + "/" + subPlan.getTotalUploads());
        holder.tvValidity.setText(subPlan.getStartDate() + " ~ " + subPlan.getEndDate());

        holder.lhSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if (row_index == position) {
            holder.lhSubs.setBackground(context.getResources().getDrawable(R.drawable.bg_button_gradient));
            SharedPreferences preferences = context.getSharedPreferences("sub_plan", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id", subPlan.getId());
            editor.putInt("plan_id", subPlan.getPlanId());
            editor.putInt("monthly_profit", subPlan.getMonthlyProfit());
            editor.putInt("total_uploads", subPlan.getTotalUploads());
            editor.apply();
        } else {
            holder.lhSubs.setBackground(context.getResources().getDrawable(R.drawable.bg_button_v3));
        }

    }

    @Override
    public int getItemCount() {
        return subPlanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardSubs;
        LinearLayout lhSubs;
        TextView tvName, tvUploads, tvValidity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardSubs = itemView.findViewById(R.id.cardSubs);
            lhSubs = itemView.findViewById(R.id.lhSubs);
            tvName = itemView.findViewById(R.id.tvName);
            tvUploads = itemView.findViewById(R.id.tvUploads);
            tvValidity = itemView.findViewById(R.id.tvValidity);

        }
    }
}
