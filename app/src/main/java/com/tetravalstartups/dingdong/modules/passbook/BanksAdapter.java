package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;

import java.util.List;

public class BanksAdapter extends RecyclerView.Adapter<BanksAdapter.ViewHolder> {

    Context context;
    List<Banks> banksList;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    public BanksAdapter(Context context, List<Banks> banksList) {
        this.context = context;
        this.banksList = banksList;
    }

    @NonNull
    @Override
    public BanksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banks_list_item, parent, false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BanksAdapter.ViewHolder holder, int position) {
        Banks banks = banksList.get(position);
        holder.tvAccount.setText(banks.getAccount());
        holder.tvIFSC.setText(banks.getIfsc());
        holder.tvBranch.setText(banks.getBranch());
        holder.tvAddress.setText(banks.getAddress());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                                    .setTitle("Delete Bank")
                        .setMessage("Are you sure, want to delete this bank?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.collection("users")
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .collection("banks")
                                            .document(banks.getId())
                                            .delete();
                                }
                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                        })
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return banksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardBanks;
        TextView tvAccount, tvIFSC, tvBranch, tvAddress;
        ImageView ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardBanks = itemView.findViewById(R.id.cardBanks);
            tvAccount = itemView.findViewById(R.id.tvAccount);
            tvIFSC = itemView.findViewById(R.id.tvIFSC);
            tvBranch = itemView.findViewById(R.id.tvBranch);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ivDelete = itemView.findViewById(R.id.ivDelete);

        }
    }
}
