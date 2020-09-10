package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.PlanInterface;
import com.tetravalstartups.dingdong.api.UserInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PayoutRequestResponse;
import com.tetravalstartups.dingdong.modules.passbook.redeem.view.PayoutConfirmationBottomSheet;
import com.tetravalstartups.dingdong.modules.passbook.redeem.view.RedeemActivity;
import com.tetravalstartups.dingdong.modules.passbook.transactions.model.TransactionResponse;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinRedeemBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private View view;
    private CoinRedeemListener coinRedeemListener;

    private TextView tvBalanceInCoins;
    private TextView tvConversionRate;
    private TextView tvBalanceInRs;
    private TextView tvRedeemableBalance;
    private TextView tvProcessingFee;
    private TextView tvInHand;

    private EditText etAmountToWithdraw;
    private TextView tvRequest;

    private FirebaseFirestore db;
    private Master master;

    private String earning_type;

    int in_hand = 0;
    double redeemable_inr = 0;
    String earning_status;
    private DDLoading ddLoading;
    private UserInterface userInterface;
    private PlanInterface planInterface;


    private static final String TAG = "CoinRedeemBottomSheet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.coin_redeem_bottom_sheet_layout, container, false);
        initView();

        etAmountToWithdraw = view.findViewById(R.id.etAmountToWithdraw);
        tvRequest = view.findViewById(R.id.tvRequest);
        tvRequest.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        master = new Master(getActivity());

        return view;
    }

    private void initView() {
        tvBalanceInCoins = view.findViewById(R.id.tvBalanceInCoins);
        tvConversionRate = view.findViewById(R.id.tvConversionRate);
        tvBalanceInRs = view.findViewById(R.id.tvBalanceInRs);
        tvRedeemableBalance = view.findViewById(R.id.tvRedeemableBalance);
        etAmountToWithdraw = view.findViewById(R.id.etAmountToWithdraw);
        tvRequest = view.findViewById(R.id.tvRequest);
        tvProcessingFee = view.findViewById(R.id.tvProcessingFee);
        tvInHand = view.findViewById(R.id.tvInHand);

        ddLoading = DDLoading.getInstance();
        userInterface = APIClient.getRetrofitInstance().create(UserInterface.class);
        planInterface = APIClient.getRetrofitInstance().create(PlanInterface.class);


        int redeemable_percent = this.getArguments().getInt("redeemable_percent");
        int processing_fees = this.getArguments().getInt("processing_fees");
        earning_type = this.getArguments().getString("earning_type");
        int earning = this.getArguments().getInt("earning");
        double conversion_rate = this.getArguments().getDouble("conversion_rate");

        setupRateCard(redeemable_percent, processing_fees, earning, conversion_rate);

        etAmountToWithdraw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String withdraw = etAmountToWithdraw.getText().toString();
                if (withdraw.isEmpty()) {
                    tvProcessingFee.setText("...");
                    tvInHand.setText("...");
                } else {
                    int withdraw_int = Integer.parseInt(withdraw);
                    int redeemable = (earning * redeemable_percent) / 100;
                    redeemable_inr = redeemable * conversion_rate;
                    if (withdraw_int <= redeemable_inr) {
                        int fees = (withdraw_int * 10) / 100;
                        in_hand = withdraw_int - fees;
                        tvInHand.setText(in_hand + "");
                        tvProcessingFee.setText(fees + "");
                    } else {
                        etAmountToWithdraw.setError("Amount must me less than ₹" + redeemable_inr);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == tvRequest) {
            String withdraw = etAmountToWithdraw.getText().toString();
            if (withdraw.isEmpty()) {
                etAmountToWithdraw.setError("Withdrawal amount required");
                return;
            }

            if (in_hand < 500) {
                etAmountToWithdraw.setError("In hand amount must be greater than ₹500");
                return;
            }

            if (in_hand > redeemable_inr) {
                etAmountToWithdraw.setError("In hand amount must be less than ₹" + redeemable_inr);
                return;
            }

            showConfirmationDialog();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.payout_confirmation, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView);
        TextView tvOkay = dialogView.findViewById(R.id.tvOkay);
        TextView tvCancel = dialogView.findViewById(R.id.tvCancel);
        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                ddLoading.showProgress(getContext(), "Hold On...", false);
                doRequestPayout();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void setupRateCard(int redeemable_percent, int processing_fees, int earning, double conversion_rate) {
        double in_inr = earning * conversion_rate;
        int redeemable = (earning * redeemable_percent) / 100;
        double redeemable_inr = redeemable * conversion_rate;
        tvBalanceInCoins.setText(earning + "");
        tvConversionRate.setText(conversion_rate + "");
        tvBalanceInRs.setText(in_inr + "");
        tvRedeemableBalance.setText(redeemable_inr + "");
    }


    public interface CoinRedeemListener {
        void sendData(String data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            coinRedeemListener = (CoinRedeemBottomSheet.CoinRedeemListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

    private void doRequestPayout() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Payouts").document();
        String id = documentReference.getId();
        if (earning_type.equals("organic")) {
            earning_status = "0";
        } else if (earning_type.equals("subscription")) {
            earning_status = "1";
        }
        Call<PayoutRequestResponse> call = userInterface.payoutRequest(
                id,
                master.getId(),
                String.valueOf(in_hand),
                earning_status
        );
        call.enqueue(new Callback<PayoutRequestResponse>() {
            @Override
            public void onResponse(Call<PayoutRequestResponse> call, Response<PayoutRequestResponse> response) {
                ddLoading.hideProgress();
                if (response.code() == 200) {
                    etAmountToWithdraw.setText("");
                    coinRedeemListener.sendData("success");
                    Toast.makeText(getContext(), "Payout Request Successful", Toast.LENGTH_SHORT).show();
                    ((RedeemActivity) getContext()).fetchPayoutRequest();
                } else if (response.code() == 400) {
                    coinRedeemListener.sendData("failed");
                    Toast.makeText(getContext(), "You have already request for payout in this month.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PayoutRequestResponse> call, Throwable t) {
                ddLoading.hideProgress();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
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


}
