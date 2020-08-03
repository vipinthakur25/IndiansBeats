package com.tetravalstartups.dingdong.modules.passbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.exoplayer2.C;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.utils.DDLoading;

public class RedeemActivity extends AppCompatActivity implements View.OnClickListener, CoinRedeemBottomSheet.CoinRedeemListener {

    private ImageView ivGoBack;
    private ImageView ivBanks;

    private LinearLayout lvOrganicEarning;
    private LinearLayout lvSubscriptionEarning;
    private LinearLayout lhHeader;
    private LinearLayout lhEarning;

    private RecyclerView recyclerRequest;

    private TextView tvOEarning;
    private TextView tvSEarning;

    private LottieAnimationView aniView;

    private CoinRedeemBottomSheet coinRedeemBottomSheet;
    private DDLoading ddLoading;

    private Master master;
    private FirebaseFirestore db;

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

        aniView = findViewById(R.id.aniView);

        lvOrganicEarning.setOnClickListener(this);
        lvSubscriptionEarning.setOnClickListener(this);

        coinRedeemBottomSheet = new CoinRedeemBottomSheet();
        ddLoading = DDLoading.getInstance();
        master = new Master(RedeemActivity.this);
        db = FirebaseFirestore.getInstance();

        hideAni();
        setEarnings();
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
            ddLoading.showProgress(RedeemActivity.this, "Fetching Data...", false);
            Bundle bundle = new Bundle();
            db.collection("users")
                    .document(master.getId())
                    .collection("attributes")
                    .document("redemption")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String redeemable_percent = task.getResult().getString("redeemable_percent");
                            String processing_fees = task.getResult().getString("processing_fees");
                            String conversion_rate = task.getResult().getString("conversion_rate");
                            String conversion_org = task.getResult().getString("conversion_org");

                            bundle.putString("redeemable_percent", redeemable_percent);
                            bundle.putString("conversion_rate", conversion_org);
                            bundle.putString("processing_fees", processing_fees);

                            db.collection("users")
                                    .document(master.getId())
                                    .collection("passbook")
                                    .document("balance")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                String organic_earning = task.getResult().getString("unreserved");
                                                bundle.putString("earning", organic_earning);
                                                bundle.putString("earning_type", "organic");
                                                ddLoading.hideProgress();
                                                coinRedeemBottomSheet.setArguments(bundle);
                                                coinRedeemBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
                                                coinRedeemBottomSheet.show(getSupportFragmentManager(), "coinRedeem");
                                            }
                                        }
                                    });
                        }
                    });
        }

        if (view == lvSubscriptionEarning) {
            ddLoading.showProgress(RedeemActivity.this, "Fetching Data...", false);
            Bundle bundle = new Bundle();
            db.collection("users")
                    .document(master.getId())
                    .collection("attributes")
                    .document("redemption")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String redeemable_percent = task.getResult().getString("redeemable_percent");
                            String processing_fees = task.getResult().getString("processing_fees");
                            String conversion_sub = task.getResult().getString("conversion_sub");

                            bundle.putString("redeemable_percent", redeemable_percent);
                            bundle.putString("conversion_rate", conversion_sub);
                            bundle.putString("processing_fees", processing_fees);

                            db.collection("users")
                                    .document(master.getId())
                                    .collection("passbook")
                                    .document("balance")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                String subscription_earning = task.getResult().getString("subscription");
                                                bundle.putString("earning", subscription_earning);
                                                bundle.putString("earning_type", "subscription");
                                                ddLoading.hideProgress();
                                                coinRedeemBottomSheet.setArguments(bundle);
                                                coinRedeemBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
                                                coinRedeemBottomSheet.show(getSupportFragmentManager(), "coinRedeem");
                                            }
                                        }
                                    });
                        }
                    });
        }

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
        db.collection("users")
                .document(master.getId())
                .collection("passbook")
                .document("balance")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                       if (documentSnapshot.exists()) {
                            String subscription_earning = documentSnapshot.getString("subscription");
                            String organic_earning = documentSnapshot.getString("unreserved");
                            tvOEarning.setText(organic_earning);
                            tvSEarning.setText(subscription_earning);
                        }
                    }
                });
    }

    @Override
    public void sendData(String data) {
        if (data.equals("done")) {
            coinRedeemBottomSheet.dismiss();
            showAni();
        }
    }
}