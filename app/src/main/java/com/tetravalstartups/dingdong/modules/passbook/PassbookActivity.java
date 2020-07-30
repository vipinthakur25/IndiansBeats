package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;

public class PassbookActivity extends AppCompatActivity implements View.OnClickListener, BuyCoinBottomSheetFragment.BuyCoinListener {

    private ImageView ivGoBack;
    private TextView tvVideoUploads;
    private TextView tvTotalEarning;
    private TextView tvAvailableCoins;
    private TextView tvBuyCoin;
    private TextView tvTotalSpending;
    private TextView tvTimeSpend;
    private TextView tvDailyCheckIn;
    private TextView tvFromYourFans;
    private TextView tvRedeemCoin;
    private TextView tvSubscriptionCoin;
    private FirebaseFirestore db;

    private Master master;
    private BuyCoinBottomSheetFragment bottomSheetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        tvVideoUploads = findViewById(R.id.tvVideoUploads);
        tvTotalEarning = findViewById(R.id.tvTotalEarning);
        tvAvailableCoins = findViewById(R.id.tvAvailableCoins);
        tvTotalSpending = findViewById(R.id.tvTotalSpending);
        tvBuyCoin = findViewById(R.id.tvBuyCoin);
        tvTimeSpend = findViewById(R.id.tvTimeSpend);
        tvDailyCheckIn = findViewById(R.id.tvDailyCheckIn);
        tvFromYourFans = findViewById(R.id.tvFromYourFans);
        tvSubscriptionCoin = findViewById(R.id.tvSubscriptionCoin);
        tvRedeemCoin = findViewById(R.id.tvRedeemCoin);

        ivGoBack.setOnClickListener(this);
        tvBuyCoin.setOnClickListener(this);
        tvRedeemCoin.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        master = new Master(PassbookActivity.this);

        bottomSheetFragment = new BuyCoinBottomSheetFragment();

        fetchBalances();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack) {
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == tvBuyCoin) {
            bottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
            bottomSheetFragment.show(getSupportFragmentManager(), "addCoin");
        }

        if (v == tvRedeemCoin) {
            Intent intent = new Intent(PassbookActivity.this, RedeemActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    private void fetchBalances() {
        db.collection("users")
                .document(master.getId())
                .collection("passbook")
                .document("balance")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        assert documentSnapshot != null;
                        String subscription_balance = documentSnapshot.getString("subscription");
                        tvSubscriptionCoin.setText(subscription_balance);
                    }
                });
    }

    private void dailyCheckInStatus() {
        String current_date = "01/01/2020";
        String date2 = "02/01/2020";
        SharedPreferences preferences = getSharedPreferences("check_in", 0);
        if (preferences.getString("checked_in", "0").equals(current_date)) {

        }
        SharedPreferences.Editor editor = preferences.edit();

    }

    @Override
    public void onButtonClicked(String text) {
        if (text.equals("cancel_false")) {
            bottomSheetFragment.setCancelable(false);
        } else if (text.equals("cancel_true")) {
            bottomSheetFragment.setCancelable(true);
        }
    }
}
