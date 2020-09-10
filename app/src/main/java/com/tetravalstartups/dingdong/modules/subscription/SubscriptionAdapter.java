package com.tetravalstartups.dingdong.modules.subscription;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.PlanInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.transactions.model.TransactionResponse;
import com.tetravalstartups.dingdong.modules.subscription.model.PaymentKey;
import com.tetravalstartups.dingdong.modules.subscription.model.PlanResponse;
import com.tetravalstartups.dingdong.modules.subscription.model.SubscriptionPurchaseResponse;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    private static final String TAG = "SubscriptionAdapter";
    Context context;
    List<PlanResponse> subscriptionList;
    private EasyUpiPayment easyUpiPayment;

    private PlanInterface planInterface;
    private Master master;

    public SubscriptionAdapter(Context context, List<PlanResponse> subscriptionList) {
        this.context = context;
        this.subscriptionList = subscriptionList;
    }

    @NonNull
    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_list_item, parent, false);

        planInterface = APIClient.getRetrofitInstance().create(PlanInterface.class);
        master = new Master(context);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.ViewHolder holder, int position) {
        PlanResponse subscription = subscriptionList.get(position);
        holder.tvName.setText("#" + subscription.getName());
        holder.tvAmount.setText("₹" + subscription.getAmount());
        holder.tvValidity.setText(subscription.getValidity() + " " + subscription.getValidityUnit());
        holder.tvBenefit.setText("₹" + subscription.getBenefit() + " benefit every " + subscription.getBenefitUnit());
        holder.tvTotalBenefit.setText("₹" + subscription.getTotalBenefit() + " total benefit");
        holder.tvUploads.setText(subscription.getUploads() + " payable video per " + subscription.getUploadUnit());

        holder.tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDLoading ddLoading = DDLoading.getInstance();
                ddLoading.showProgress(context, "Initiating Payment...", false);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("transaction").document();
                String txn_id = documentReference.getId();

                double amt = subscription.getAmount();
                DecimalFormat form = new DecimalFormat("0.00");

                Call<PaymentKey> call = planInterface.fetchPaymentKey();
                call.enqueue(new Callback<PaymentKey>() {
                    @Override
                    public void onResponse(Call<PaymentKey> call, Response<PaymentKey> response) {
                        ddLoading.hideProgress();
                        if (response.code() == 200) {
                            easyUpiPayment = new EasyUpiPayment.Builder()
                                    .with((Activity) context)
                                    .setPayeeVpa(response.body().getUpi())
                                    .setPayeeName(context.getResources().getString(R.string.app_name))
                                    .setTransactionId(txn_id)
                                    .setTransactionRefId(txn_id)
                                    .setDescription(context.getResources().getString(R.string.app_name) + " "
                                            + subscription.getName()
                                            + " Subscription")
                                    .setAmount(form.format(amt))
                                    .build();
                            easyUpiPayment.startPayment();

                            easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
                                @Override
                                public void onTransactionCompleted(TransactionDetails transactionDetails) {
                                    Log.e(TAG, "onTransactionCompleted: " + transactionDetails.getStatus());
                                }

                                @Override
                                public void onTransactionSuccess() {
                                    Log.e(TAG, "onTransactionSuccess: ");
                                    doCreateTransaction(txn_id,
                                            master.getId(),
                                            subscription.getAmount(),
                                            Constant.SUBS_PURCHASE_REMARK,
                                            getCurrentDate(),
                                            getCurrentTime(),
                                            1,
                                            subscription.getId());
                                }

                                @Override
                                public void onTransactionSubmitted() {
                                    Log.e(TAG, "onTransactionSubmitted: ");
                                }

                                @Override
                                public void onTransactionFailed() {
                                    Log.e(TAG, "onTransactionFailed: ");
                                }

                                @Override
                                public void onTransactionCancelled() {
                                    Log.e(TAG, "onTransactionCancelled: ");
                                }

                                @Override
                                public void onAppNotFound() {
                                    Log.e(TAG, "onAppNotFound: ");
                                }
                            });

                        } else {
                            ddLoading.hideProgress();
                            Log.e(TAG, "onResponse: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentKey> call, Throwable t) {
                        ddLoading.hideProgress();
                        Log.e(TAG, "onFailure: "+t.getMessage());
                    }
                });
            }
        });
    }

    private void doCreateTransaction(String txn_id, String masterId, int amount, String subsPurchaseRemark, String currentDate, String currentTime, int status, Integer plan_id) {
        Call<TransactionResponse> call = planInterface.performTransaction(
                txn_id,
                masterId,
                String.valueOf(amount),
                subsPurchaseRemark,
                currentDate,
                currentTime,
                status
        );

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if (response.code() == 200) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = db.collection("subscriptions").document();
                    String subs_id = documentReference.getId();
                    doPurchaseSubscription(subs_id, masterId, plan_id, txn_id);
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void doPurchaseSubscription(String subs_id, String masterId, int plan_id, String txn_id) {
        Call<SubscriptionPurchaseResponse> call = planInterface.purchaseSubscription(
                subs_id,
                masterId,
                plan_id,
                txn_id
        );
        call.enqueue(new Callback<SubscriptionPurchaseResponse>() {
            @Override
            public void onResponse(Call<SubscriptionPurchaseResponse> call, Response<SubscriptionPurchaseResponse> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Purchase Successful", Toast.LENGTH_SHORT).show();
                    ((SubscriptionActivity)context).setupRecyclerSubscribed();
                }
            }

            @Override
            public void onFailure(Call<SubscriptionPurchaseResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: subs"+t.getMessage() );
            }
        });
    }


    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    private String getCurrentDate() {
        DateFormat dfDate = new SimpleDateFormat("d MMM yyyy");
        String txnDate = dfDate.format(Calendar.getInstance().getTime());
        return txnDate;
    }

    private String getCurrentTime() {
        DateFormat dfTime = new SimpleDateFormat("h:mm a");
        String txnTime = dfTime.format(Calendar.getInstance().getTime());
        return txnTime;
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
