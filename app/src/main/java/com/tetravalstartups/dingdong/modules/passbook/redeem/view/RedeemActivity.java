package com.tetravalstartups.dingdong.modules.passbook.redeem.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.UserInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.BanksActivity;
import com.tetravalstartups.dingdong.modules.passbook.CoinRedeemBottomSheet;
import com.tetravalstartups.dingdong.modules.passbook.UnreservedCoinTxn;
import com.tetravalstartups.dingdong.modules.passbook.UnreservedCoinTxnAdapter;
import com.tetravalstartups.dingdong.modules.passbook.UnreservedCoinTxnPresenter;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PassbookBalance;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PassbookBalanceResponse;
import com.tetravalstartups.dingdong.modules.passbook.redeem.model.PayoutHistory;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemActivity extends AppCompatActivity implements View.OnClickListener, CoinRedeemBottomSheet.CoinRedeemListener, UnreservedCoinTxnPresenter.IUnreservedTxn {

    private ImageView ivGoBack;
    private ImageView ivBanks;

    private LinearLayout lvOrganicEarning;
    private LinearLayout lvSubscriptionEarning;
    private LinearLayout lhHeader;
    private LinearLayout lhEarning;

    private RecyclerView recyclerRequest;
    private TextView tvNoData;

    private TextView tvOEarning;
    private TextView tvSEarning;

    private LinearLayout lvMain;

    private LottieAnimationView aniView;

    private CoinRedeemBottomSheet coinRedeemBottomSheet;
    private DDLoading ddLoading;

    private Master master;
    private UserInterface userInterface;

    private FirebaseFirestore db;

    private static final String TAG = "RedeemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        ivBanks = findViewById(R.id.ivBanks);
        ivBanks.setOnClickListener(this);

        lvOrganicEarning = findViewById(R.id.lvOrganicEarning);
        lvSubscriptionEarning = findViewById(R.id.lvSubscriptionEarning);

        lhHeader = findViewById(R.id.lhHeader);
        lhEarning = findViewById(R.id.lhEarning);
        recyclerRequest = findViewById(R.id.recyclerRequest);

        tvOEarning = findViewById(R.id.tvOEarning);
        tvSEarning = findViewById(R.id.tvSEarning);

        lvMain = findViewById(R.id.lvMain);

        aniView = findViewById(R.id.aniView);

        lvOrganicEarning.setOnClickListener(this);
        lvSubscriptionEarning.setOnClickListener(this);

        coinRedeemBottomSheet = new CoinRedeemBottomSheet();
        ddLoading = DDLoading.getInstance();
        master = new Master(RedeemActivity.this);

        userInterface = APIClient.getRetrofitInstance().create(UserInterface.class);
        db = FirebaseFirestore.getInstance();

        ddLoading.showProgress(RedeemActivity.this, "Loading...", false);

        hideAni();
        setEarnings();
        fetchPayoutRequest();
    }

    public void fetchPayoutRequest() {
        recyclerRequest.setLayoutManager(new LinearLayoutManager(RedeemActivity.this));
        UnreservedCoinTxnPresenter unreservedCoinTxnPresenter =
                new UnreservedCoinTxnPresenter(RedeemActivity.this,
                        RedeemActivity.this);

        unreservedCoinTxnPresenter.fetchTxn();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
        }

        if (view == ivBanks) {
            startActivity(new Intent(RedeemActivity.this, BanksActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (view == lvOrganicEarning) {
            doOrgEarn();
        }

        if (view == lvSubscriptionEarning) {
            doSubEarn();
        }

    }

    private void doOrgEarn() {
        ddLoading.showProgress(RedeemActivity.this, "Hold On...", false);
        db.collection("customers")
                .document(master.getId())
                .collection("banks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().getDocuments().size() == 1) {
                            showOrgBottomSheet();
                        } else {
                            ddLoading.hideProgress();
                            Snackbar.make(lvMain, "Please add bank account first", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showOrgBottomSheet() {
        Call<PassbookBalance> call = userInterface.fetchPassbookDetails(master.getId());
        call.enqueue(new Callback<PassbookBalance>() {
            @Override
            public void onResponse(Call<PassbookBalance> call, Response<PassbookBalance> response) {
                if (response.code() == 200) {
                    ddLoading.hideProgress();
                    int video_org = response.body().getData().getVideoUploads();
                    int time_org = response.body().getData().getTimeSpent();
                    int daily_org = response.body().getData().getDailyRewards();
                    int donation_org = response.body().getData().getFansDonation();
                    int total_org = video_org + time_org + daily_org + donation_org;
                    Bundle bundle = new Bundle();
                    bundle.putInt("redeemable_percent", 85);
                    bundle.putDouble("conversion_rate", 0.01);
                    bundle.putInt("processing_fees", 10);
                    bundle.putInt("earning", total_org);
                    bundle.putString("earning_type", "organic");
                    ddLoading.hideProgress();
                    coinRedeemBottomSheet.setArguments(bundle);
                    coinRedeemBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
                    coinRedeemBottomSheet.show(getSupportFragmentManager(), "coinRedeem");
                } else {
                    ddLoading.hideProgress();
                    Log.e(TAG, "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<PassbookBalance> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void doSubEarn() {
        ddLoading.showProgress(RedeemActivity.this, "Hold On...", false);
        db.collection("customers")
                .document(master.getId())
                .collection("banks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().getDocuments().size() == 1) {
                            showSubsBottomSheet();
                        } else {
                            ddLoading.hideProgress();
                            Snackbar.make(lvMain, "Please add bank account first", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showSubsBottomSheet() {
        Call<PassbookBalance> call = userInterface.fetchPassbookDetails(master.getId());
        call.enqueue(new Callback<PassbookBalance>() {
            @Override
            public void onResponse(Call<PassbookBalance> call, Response<PassbookBalance> response) {
                if (response.code() == 200) {
                    ddLoading.hideProgress();
                    Bundle bundle = new Bundle();
                    bundle.putInt("redeemable_percent", 85);
                    bundle.putDouble("conversion_rate", 1);
                    bundle.putInt("processing_fees", 10);
                    bundle.putInt("earning", response.body().getData().getSubscription());
                    bundle.putString("earning_type", "subscription");
                    ddLoading.hideProgress();
                    coinRedeemBottomSheet.setArguments(bundle);
                    coinRedeemBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
                    coinRedeemBottomSheet.show(getSupportFragmentManager(), "coinRedeem");
                } else {
                    ddLoading.hideProgress();
                    Log.e(TAG, "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<PassbookBalance> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void showAni() {
        lhHeader.setVisibility(View.GONE);
        lhEarning.setVisibility(View.GONE);
        recyclerRequest.setVisibility(View.GONE);
        aniView.setVisibility(View.VISIBLE);
        aniView.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideAni();
            }
        }, 2000);

    }

    private void hideAni() {
        lhHeader.setVisibility(View.VISIBLE);
        lhEarning.setVisibility(View.VISIBLE);
        recyclerRequest.setVisibility(View.VISIBLE);
        aniView.setVisibility(View.GONE);
    }

    private void setEarnings() {
        Call<PassbookBalance> call = userInterface.fetchPassbookDetails(master.getId());
        call.enqueue(new Callback<PassbookBalance>() {
            @Override
            public void onResponse(Call<PassbookBalance> call, Response<PassbookBalance> response) {
                if (response.code() == 200) {
                    PassbookBalanceResponse passbookBalanceResponse = response.body().getData();
                    int video_org = response.body().getData().getVideoUploads();
                    int time_org = response.body().getData().getTimeSpent();
                    int daily_org = response.body().getData().getDailyRewards();
                    int donation_org = response.body().getData().getFansDonation();
                    int total_org = video_org + time_org + daily_org + donation_org;
                    tvOEarning.setText(total_org+"");
                    tvSEarning.setText(passbookBalanceResponse.getSubscription()+"");
                } else {
                    Log.e(TAG, "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<PassbookBalance> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
        db.collection("customers")
                .document(master.getId())
                .collection("passbook")
                .document("balance")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()) {
                            String subscription_earning = documentSnapshot.getString("subscription");
                            String organic_earning = documentSnapshot.getString("unreserved");
                            ddLoading.hideProgress();
                        }
                    }
                });
    }

    @Override
    public void sendData(String data) {
        if (data.equals("success")) {
            coinRedeemBottomSheet.dismiss();
            showAni();
        }
        if (data.equals("failed")) {
            coinRedeemBottomSheet.dismiss();
        }
    }

    @Override
    public void fetchTxnSuccess(PayoutHistory payoutHistory) {
        UnreservedCoinTxnAdapter unreservedCoinTxnAdapter =
                new UnreservedCoinTxnAdapter(RedeemActivity.this,
                        payoutHistory.getData());
        unreservedCoinTxnAdapter.notifyDataSetChanged();
        recyclerRequest.setAdapter(unreservedCoinTxnAdapter);
        recyclerRequest.setVisibility(View.VISIBLE);
    }

    @Override
    public void fetchTxnError(String error) {
        recyclerRequest.setVisibility(View.GONE);
    }
}