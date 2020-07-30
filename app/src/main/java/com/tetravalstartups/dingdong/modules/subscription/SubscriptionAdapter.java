package com.tetravalstartups.dingdong.modules.subscription;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.tetravalstartups.dingdong.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    Context context;
    List<Subscription> subscriptionList;

    public SubscriptionAdapter(Context context, List<Subscription> subscriptionList) {
        this.context = context;
        this.subscriptionList = subscriptionList;
    }

    @NonNull
    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.ViewHolder holder, int position) {
        Subscription subscription = subscriptionList.get(position);
        holder.tvName.setText("#" + subscription.getName());
        holder.tvAmount.setText("₹" + subscription.getAmount());
        holder.tvValidity.setText(subscription.getValidity() + " " + subscription.getValidity_unit());
        holder.tvBenefit.setText("₹" + subscription.getBenefit() + " benefit every " + subscription.getBenefit_unit());
        holder.tvTotalBenefit.setText("₹" + subscription.getTotal_benefit() + " total benefit");
        holder.tvUploads.setText(subscription.getUploads() + " payable video per " + subscription.getUpload_unit());
        holder.tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("subscription").document();
                String id = documentReference.getId();

                double amt = 1.0; //subscription.getAmount();
                DecimalFormat form = new DecimalFormat("0.00");

                final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                        .with((Activity) context)
                        .setPayeeVpa("7billionsnetwork@okhdfcbank")
                        .setPayeeName("Ding Dong")
                        .setTransactionId(id)
                        .setTransactionRefId(id)
                        .setDescription("Ding Dong Subscription Plan: " + subscription.getName())
                        .setAmount(form.format(amt))
                        .build();

                easyUpiPayment.startPayment();
                easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
                    @Override
                    public void onTransactionCompleted(TransactionDetails transactionDetails) {

                    }

                    @Override
                    public void onTransactionSuccess() {
                        Calendar current = Calendar.getInstance();
                        current.add(Calendar.MONTH, 10);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        Date resultdate = new Date(current.getTimeInMillis());
                        String dueudate = df.format(resultdate);

                        Calendar current1 = Calendar.getInstance();
                        Date start = new Date(current1.getTimeInMillis());
                        String start_date = df.format(start);

                        Subscribed subscribed = new Subscribed();
                        subscribed.setId(id);
                        subscribed.setName(subscription.getName());
                        subscribed.setStart_date(start_date);
                        subscribed.setEnd_date(dueudate);
                        subscribed.setTotal_uploads(String.valueOf(subscription.getUploads()));
                        subscribed.setAvl_uploads(String.valueOf(subscription.getUploads()));
                        subscribed.setMonthly_profit(String.valueOf(subscription.getBenefit()));
                        subscribed.setStatus("1");

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                        db.collection("users")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .collection("subscription")
                                .document(id)
                                .set(subscribed);
                    }

                    @Override
                    public void onTransactionSubmitted() {
                        Toast.makeText(context, "Transaction pending...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTransactionFailed() {
                        Toast.makeText(context, "Transaction failed...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTransactionCancelled() {
                        Toast.makeText(context, "Transaction cancelled...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAppNotFound() {
                        Toast.makeText(context, "App not found...", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAmount, tvValidity, tvBenefit, tvTotalBenefit, tvUploads, tvCheckout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvValidity = itemView.findViewById(R.id.tvValidity);
            tvBenefit = itemView.findViewById(R.id.tvBenefit);
            tvTotalBenefit = itemView.findViewById(R.id.tvTotalBenefit);
            tvUploads = itemView.findViewById(R.id.tvUploads);
            tvCheckout = itemView.findViewById(R.id.tvCheckout);

        }
    }
}
