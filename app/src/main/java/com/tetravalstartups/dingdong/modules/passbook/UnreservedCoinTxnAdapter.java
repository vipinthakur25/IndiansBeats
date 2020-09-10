package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PayoutHistoryResponse;
import com.tetravalstartups.dingdong.modules.passbook.redeem.view.RedeemActivity;

import java.util.List;

public class UnreservedCoinTxnAdapter extends RecyclerView.Adapter<UnreservedCoinTxnAdapter.ViewHolder>
        implements TxnDetailsBottomSheetFragment.TxnDetailsListener {

    Context context;
    List<PayoutHistoryResponse> unreservedCoinTxnList;

    private TxnDetailsBottomSheetFragment txnDetailsBottomSheetFragment;


    public UnreservedCoinTxnAdapter(Context context, List<PayoutHistoryResponse> unreservedCoinTxnList) {
        this.context = context;
        this.unreservedCoinTxnList = unreservedCoinTxnList;
    }

    @NonNull
    @Override
    public UnreservedCoinTxnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unreserved_list_item, parent, false);

        txnDetailsBottomSheetFragment = new TxnDetailsBottomSheetFragment();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnreservedCoinTxnAdapter.ViewHolder holder, int position) {
        PayoutHistoryResponse unreservedCoinTxn = unreservedCoinTxnList.get(position);

        String[] dateTime = unreservedCoinTxn.getCreatedDate().split("\\s");

        holder.tvTimeDate.setText(dateTime[1]+" ~ "+dateTime[0]);
        holder.tvRemark.setText("Payout Request");
        holder.tvAmount.setText(unreservedCoinTxn.getAmount()+"");

        if (unreservedCoinTxn.getStatus() == 0){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(null);
            holder.ivDone.setImageDrawable(null);

        }

        if (unreservedCoinTxn.getStatus() == 1){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivDone.setImageDrawable(null);

        }

        if (unreservedCoinTxn.getStatus() == 2){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivDone.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
        }

        if (unreservedCoinTxn.getStatus() == 3){
            holder.ivPending.setImageDrawable(null);
            holder.ivProcessing.setImageDrawable(null);
            holder.ivDone.setImageDrawable(null);
        }

        if (unreservedCoinTxn.getStatus() == 4){
            holder.ivPending.setImageDrawable(null);
            holder.ivProcessing.setImageDrawable(null);
            holder.ivDone.setImageDrawable(null);
        }

        holder.cardTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = context.getSharedPreferences("unreserved", 0);
                Editor editor = preferences.edit();
                editor.putString("id", unreservedCoinTxn.getId());
                editor.putInt("amount", unreservedCoinTxn.getAmount());
                String[] dateTime = unreservedCoinTxn.getCreatedDate().split("\\s");
                editor.putString("time",dateTime[1]);
                editor.putString("date", dateTime[0]);
                if (unreservedCoinTxn.getDescription().isEmpty()) {
                    editor.putString("remark", "Payout Request");
                } else {
                    editor.putString("remark", unreservedCoinTxn.getDescription());
                }
                editor.putInt("status", unreservedCoinTxn.getStatus());
                editor.apply();
                txnDetailsBottomSheetFragment.show(((RedeemActivity)context).getSupportFragmentManager(),
                        "profilePhotoBottomSheet");
            }
        });

    }

    @Override
    public int getItemCount() {
        return unreservedCoinTxnList.size();
    }

    @Override
    public void onButtonClicked(String text) {

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
