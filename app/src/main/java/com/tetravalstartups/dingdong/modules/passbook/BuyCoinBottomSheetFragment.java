package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.subscription.SubscriptionActivity;
import com.tetravalstartups.dingdong.utils.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BuyCoinBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView tvSubscription;
    private View view;
    private BuyCoinListener buyCoinListener;
    private EditText etCoins;
    private TextView tvBuyNow;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private LinearLayout lvBuyCoin;
    private LinearLayout lvTxn;
    private TextView tvTxnStatus;
    private TextView tvTxnTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.buy_coin_bottom_sheet_layout, container, false);
        initView();
        return view;
    }

    private void initView() {
        tvSubscription = view.findViewById(R.id.tvSubscription);

        etCoins = view.findViewById(R.id.etCoins);
        tvBuyNow = view.findViewById(R.id.tvBuyNow);

        lvBuyCoin = view.findViewById(R.id.lvBuyCoin);
        lvTxn = view.findViewById(R.id.lvTxn);

        tvTxnStatus = view.findViewById(R.id.tvTxnStatus);
        tvTxnTime = view.findViewById(R.id.tvTxnTime);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        tvSubscription.setOnClickListener(this);
        tvBuyNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvSubscription){
            startActivity(new Intent(getContext(), SubscriptionActivity.class));
        }
        if (v == tvBuyNow){
            doUiValidation();
        }
    }

    private void doUiValidation() {
        String coins = etCoins.getText().toString();
        if (TextUtils.isEmpty(coins)){
            etCoins.requestFocus();
            etCoins.setError("Coins quantity required");
            return;
        }
        if (!TextUtils.isDigitsOnly(coins)){
            etCoins.requestFocus();
            etCoins.setError("Invalid coins quantity");
            return;
        }

        doAddCoins(coins);

    }

    private void doAddCoins(String coins) {
        DateFormat df = new SimpleDateFormat("h:mm a ~ d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        DateFormat dfTime = new SimpleDateFormat("h:mm a");
        String txnTime = dfTime.format(Calendar.getInstance().getTime());

        DateFormat dfDate = new SimpleDateFormat("d MMM yyyy");
        String txnDate = dfDate.format(Calendar.getInstance().getTime());

        buyCoinListener.onButtonClicked("cancel_false");
        etCoins.setEnabled(false);
        tvBuyNow.setEnabled(false);
        lvBuyCoin.setVisibility(View.GONE);
        lvTxn.setVisibility(View.VISIBLE);
        tvSubscription.setEnabled(false);
        db.collection("customers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("passbook")
                .document("balance")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().getString("unreserved") != null){
                            String unreserved_coin = task.getResult().getString("unreserved");
                            int ur_coin = Integer.parseInt(unreserved_coin);
                            int up_ur_coin = Integer.parseInt(coins);
                            int updatable_coins = ur_coin+up_ur_coin;
                            String unreserved = String.valueOf(updatable_coins);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("unreserved", unreserved);
                            db.collection("customers")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .collection("passbook")
                                    .document("balance")
                                    .update(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                DocumentReference documentReference = db.collection("customers").document();
                                                String id = documentReference.getId();
                                                UnreservedCoinTxn unreservedCoinTxn = new UnreservedCoinTxn();
                                                unreservedCoinTxn.setId(id);
                                                unreservedCoinTxn.setType(String.valueOf(Constant.TXN_CREDIT));
                                                unreservedCoinTxn.setAmount(String.valueOf(up_ur_coin));
                                                unreservedCoinTxn.setTime(txnTime);
                                                unreservedCoinTxn.setDate(txnDate);
                                                unreservedCoinTxn.setRemark(Constant.TXN_REMARK_COIN_PURCHASE);
                                                unreservedCoinTxn.setStatus(String.valueOf(2));
                                                db.collection("customers")
                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                        .collection("unreserved_coins_txn")
                                                        .document(id)
                                                        .set(unreservedCoinTxn)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    tvTxnTime.setText(date);
                                                                    etCoins.setText("");
                                                                    buyCoinListener.onButtonClicked("cancel_true");
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }

                    }
                });

    }

    public interface BuyCoinListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            buyCoinListener = (BuyCoinBottomSheetFragment.BuyCoinListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }

}
