package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.HashMap;

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

    int processing = 0;
    int in_hand = 0;
    int w_draw = 0;

    private DDLoading ddLoading;

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

        String redeemable_percent = this.getArguments().getString("redeemable_percent");
        String processing_fees = this.getArguments().getString("processing_fees");
        earning_type = this.getArguments().getString("earning_type");
        String earning = this.getArguments().getString("earning");
        String conversion_rate = this.getArguments().getString("conversion_rate");

        setupRateCard(redeemable_percent, processing_fees, earning_type, earning, conversion_rate);

        etAmountToWithdraw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int fees = Integer.parseInt(processing_fees);
                int percent = Integer.parseInt(redeemable_percent);
                int amount = Integer.parseInt(earning);
                int conversion = Integer.parseInt(conversion_rate);
                int in_inr = amount * conversion;
                int redeemable = (in_inr * percent) / 100;

                String withdraw = etAmountToWithdraw.getText().toString();
                if (!withdraw.isEmpty()) {
                    w_draw = Integer.parseInt(withdraw);
                    if (w_draw >= 0) {
                        if (w_draw <= redeemable) {

                            processing = (w_draw * fees) / 100;
                            in_hand = w_draw - processing;

                            tvProcessingFee.setText(processing + "");
                            tvInHand.setText(in_hand + "");

                        } else {
                            etAmountToWithdraw.setError("Amount must be less than " + redeemable);
                        }
                    }
                } else {
                    tvProcessingFee.setText("0");
                    tvInHand.setText("0");
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

            ddLoading.showProgress(getActivity(), "Processing Request...", false);

            String redeemable_percent = this.getArguments().getString("redeemable_percent");
            String processing_fees = this.getArguments().getString("processing_fees");
            earning_type = this.getArguments().getString("earning_type");
            String earning = this.getArguments().getString("earning");
            String conversion_rate = this.getArguments().getString("conversion_rate");

            int percent = Integer.parseInt(redeemable_percent);
            int amount = Integer.parseInt(earning);
            double conversion = Double.parseDouble(conversion_rate);
            double in_inr = amount * conversion;
            double redeemable = (in_inr * percent) / 100;

            DocumentReference documentReference = db.collection("payouts").document();
            String request_id = documentReference.getId();

            PayoutRequest payoutRequest = new PayoutRequest();
            payoutRequest.setId(request_id);
            payoutRequest.setUser_id(master.getId());
            payoutRequest.setUser_name(master.getName());
            payoutRequest.setUser_handle(master.getHandle());
            payoutRequest.setUser_email(master.getEmail());
            payoutRequest.setUser_photo(master.getPhoto());

            if (earning_type.equals("organic")) {
                payoutRequest.setRequest_type(Constant.PAYOUT_REQUEST_TYPE_ORGANIC);
            } else if (earning_type.equals("subscription")) {
                payoutRequest.setRequest_type(Constant.PAYOUT_REQUEST_TYPE_SUBS);
            }

            payoutRequest.setCoin_balance_at_request(earning);
            payoutRequest.setInr_balance_at_request(in_inr + "");
            payoutRequest.setConversion_rate_at_request(conversion_rate);
            payoutRequest.setRedeemable_percent_at_request(redeemable_percent);
            payoutRequest.setRedeemable_balance_at_request(redeemable + "");
            payoutRequest.setProcessing_fee_percent_at_request(processing_fees);
            payoutRequest.setProcessing_fee_amount_at_request(processing + "");
            payoutRequest.setIn_hand_balance_at_request(in_hand + "");
            payoutRequest.setTo_withdraw_balance_at_request(w_draw + "");
            payoutRequest.setStatus(Constant.PAYOUT_STATUS_PENDING);


            db.collection("payouts")
                    .document(master.getId())
                    .set(payoutRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            int updated_balance = amount - w_draw;
                            HashMap hashMap = new HashMap();
                            if (earning_type.equals("organic")) {
                                hashMap.put("unreserved", updated_balance + "");
                            } else if (earning_type.equals("subscription")) {
                                hashMap.put("subscription", updated_balance + "");
                            }

                            db.collection("users")
                                    .document(master.getId())
                                    .collection("passbook")
                                    .document("balance")
                                    .update(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            etAmountToWithdraw.setText("");
                                            ddLoading.hideProgress();
                                            coinRedeemListener.sendData("done");
                                        }
                                    });
                        }
                    });
        }
    }

    private void setupRateCard(String redeemable_percent, String processing_fees, String earning_type, String earning, String conversion_rate) {
        int percent = Integer.parseInt(redeemable_percent);
        int amount = Integer.parseInt(earning);
        double conversion = Double.parseDouble(conversion_rate);

        double in_inr = amount * conversion;
        int redeemable = (amount * percent) / 100;

        tvBalanceInCoins.setText(amount + "");
        tvConversionRate.setText(conversion + "");
        tvBalanceInRs.setText(in_inr + "");
        tvRedeemableBalance.setText(redeemable + "");

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


}
