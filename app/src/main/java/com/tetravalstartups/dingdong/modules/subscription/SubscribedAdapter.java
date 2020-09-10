package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptionResponse;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptions;

import java.util.List;

public class SubscribedAdapter extends RecyclerView.Adapter<SubscribedAdapter.ViewHolder> {

    Context context;
    List<MySubscriptionResponse> subscribedList;

    public SubscribedAdapter(Context context, List<MySubscriptionResponse> subscribedList) {
        this.context = context;
        this.subscribedList = subscribedList;
    }

    @NonNull
    @Override
    public SubscribedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribed_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribedAdapter.ViewHolder holder, int position) {
        MySubscriptionResponse subscribed = subscribedList.get(position);
        holder.tvName.setText("#"+subscribed.getName());
        holder.tvUploads.setText(subscribed.getAvlUploads()+"/"+subscribed.getTotalUploads());
        holder.tvValidity.setText(subscribed.getStartDate()+" ~ "+subscribed.getEndDate());
    }

    @Override
    public int getItemCount() {
        return subscribedList.size();
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
