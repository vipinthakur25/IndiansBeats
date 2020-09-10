package com.tetravalstartups.dingdong.modules.passbook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.UserInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PassbookBalance;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PassbookBalanceResponse;
import com.tetravalstartups.dingdong.modules.passbook.redeem.view.RedeemActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private TextView tvTransactions;
    private FirebaseFirestore db;

    private Master master;
    private BuyCoinBottomSheetFragment bottomSheetFragment;

    private static final String TAG = "PassbookActivity";

    private UserInterface userInterface;

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
        tvTransactions = findViewById(R.id.tvTransactions);
        tvTimeSpend = findViewById(R.id.tvTimeSpend);
        tvDailyCheckIn = findViewById(R.id.tvDailyCheckIn);
        tvFromYourFans = findViewById(R.id.tvFromYourFans);
        tvSubscriptionCoin = findViewById(R.id.tvSubscriptionCoin);
        tvRedeemCoin = findViewById(R.id.tvRedeemCoin);

        ivGoBack.setOnClickListener(this);
        tvTransactions.setOnClickListener(this);
        tvRedeemCoin.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        master = new Master(PassbookActivity.this);

        bottomSheetFragment = new BuyCoinBottomSheetFragment();

        userInterface = APIClient.getRetrofitInstance().create(UserInterface.class);

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

        if (v == tvTransactions) {
            Intent intent = new Intent(PassbookActivity.this, TransactionActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (v == tvRedeemCoin) {
            Intent intent = new Intent(PassbookActivity.this, RedeemActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }

    private void fetchBalances() {
        Call<PassbookBalance> call = userInterface.fetchPassbookDetails(master.getId());
        call.enqueue(new Callback<PassbookBalance>() {
            @Override
            public void onResponse(Call<PassbookBalance> call, Response<PassbookBalance> response) {
                if (response.code() == 200) {
                    PassbookBalanceResponse passbookBalanceResponse = response.body().getData();
                    tvSubscriptionCoin.setText(passbookBalanceResponse.getSubscription()+"");
                    tvVideoUploads.setText(passbookBalanceResponse.getVideoUploads()+"");
                    tvDailyCheckIn.setText(passbookBalanceResponse.getDailyRewards()+"");
                    tvTimeSpend.setText(passbookBalanceResponse.getTimeSpent()+"");
                    tvFromYourFans.setText(passbookBalanceResponse.getFansDonation()+"");

                    int available = passbookBalanceResponse.getDailyRewards()
                            +passbookBalanceResponse.getFansDonation()
                            +passbookBalanceResponse.getTimeSpent()
                            +passbookBalanceResponse.getVideoUploads();

                    tvAvailableCoins.setText(available+"");

                } else {
                    Log.e(TAG, "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<PassbookBalance> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
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
