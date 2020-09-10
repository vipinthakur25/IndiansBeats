package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.passbook.transactions.model.TxnResponse;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>
        implements TxnDetailsBottomSheetFragment.TxnDetailsListener{

    private Context context;
    private List<TxnResponse> transactionsList;
    private TxnDetailsBottomSheetFragment txnDetailsBottomSheetFragment;

    public TransactionAdapter(Context context, List<TxnResponse> transactionsList) {
        this.context = context;
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_list_item, parent, false);
        txnDetailsBottomSheetFragment = new TxnDetailsBottomSheetFragment();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        TxnResponse transactions = transactionsList.get(position);

        holder.tvTimeDate.setText(transactions.getTime()+" ~ "+transactions.getDate());

        holder.tvRemark.setText(transactions.getRemark());

        holder.tvAmount.setText(transactions.getAmount()+"");

        if (transactions.getStatus() == 0){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(null);
            holder.ivDone.setImageDrawable(null);

        }

        if (transactions.getStatus() == 1){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivDone.setImageDrawable(null);

        }

        if (transactions.getStatus() == 2){
            holder.ivPending.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivProcessing.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
            holder.ivDone.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_check_mark_white));
        }

        holder.cardTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = context.getSharedPreferences("unreserved", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id", transactions.getId());
                editor.putInt("amount", transactions.getAmount());
                editor.putString("time", transactions.getTime());
                editor.putString("date", transactions.getDate());
                editor.putString("remark", transactions.getRemark());
                editor.putInt("status", transactions.getStatus());
                editor.apply();
                txnDetailsBottomSheetFragment.show(((TransactionActivity)context).getSupportFragmentManager(),
                        "profilePhotoBottomSheet");
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
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
