package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;

import java.util.List;

public class SubscribedAdapter extends RecyclerView.Adapter<SubscribedAdapter.ViewHolder> {

    Context context;
    List<Subscribed> subscribedList;

    public SubscribedAdapter(Context context, List<Subscribed> subscribedList) {
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
        Subscribed subscribed = subscribedList.get(position);
        holder.tvName.setText("#"+subscribed.getName());
        holder.tvUploads.setText(subscribed.getAvl_uploads()+"/"+subscribed.getTotal_uploads());
        holder.tvValidity.setText(subscribed.getStart_date()+" ~ "+subscribed.getEnd_date());
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
