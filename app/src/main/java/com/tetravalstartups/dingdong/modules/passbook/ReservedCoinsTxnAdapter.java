package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.Constant;

import java.util.List;

public class ReservedCoinsTxnAdapter extends RecyclerView.Adapter<ReservedCoinsTxnAdapter.ViewHolder> {

    Context context;
    List<ReservedCoinsTxn> reservedCoinsTxnList;

    public ReservedCoinsTxnAdapter(Context context, List<ReservedCoinsTxn> reservedCoinsTxnList) {
        this.context = context;
        this.reservedCoinsTxnList = reservedCoinsTxnList;
    }

    @NonNull
    @Override
    public ReservedCoinsTxnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserved_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservedCoinsTxnAdapter.ViewHolder holder, int position) {
        ReservedCoinsTxn reservedCoinsTxn = reservedCoinsTxnList.get(position);
        if (reservedCoinsTxn.getType() == Constant.TXN_CREDIT){
            holder.tvAmount.setText("+ "+reservedCoinsTxn.getAmount());
        } else if (reservedCoinsTxn.getType() == Constant.TXN_DEBIT){
            holder.tvAmount.setText("- "+reservedCoinsTxn.getAmount());
        }

        holder.tvTimeDate.setText(reservedCoinsTxn.getTime()+" ~ "+reservedCoinsTxn.getDate());

        holder.tvRemark.setText(reservedCoinsTxn.getRemark());


        if (reservedCoinsTxn.getStatus() == 0){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(null);
            holder.ivDone.setImageDrawable(null);

        } else if (reservedCoinsTxn.getStatus() == 1){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivDone.setImageDrawable(null);

        } else if (reservedCoinsTxn.getStatus() == 2){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivDone.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
        }



    }

    @Override
    public int getItemCount() {
        return reservedCoinsTxnList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardTransaction;
        TextView tvAmount, tvTimeDate, tvRemark;
        ImageView ivPending, ivProcessing, ivDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardTransaction = itemView.findViewById(R.id.cardTransaction);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvTimeDate = itemView.findViewById(R.id.tvTimeDate);
            tvRemark = itemView.findViewById(R.id.tvRemark);
            ivPending = itemView.findViewById(R.id.ivPending);
            ivProcessing = itemView.findViewById(R.id.ivProcessing);
            ivDone = itemView.findViewById(R.id.ivDone);

        }
    }
}
