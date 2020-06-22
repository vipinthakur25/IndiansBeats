package com.tetravalstartups.dingdong.modules.passbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.ProfilePhotoBottomSheet;

public class PassbookActivity extends AppCompatActivity implements View.OnClickListener, BuyCoinBottomSheetFragment.BuyCoinListener {

    private TabLayout passbookTab;
    private ViewPager passbookPager;
    private LinearLayout lvBuyCoins;
    private LinearLayout lvBanks;
    private TextView tvTotalCoins;
    private TextView tvReservedCoins;
    private TextView tvUnReservedCoins;
    private TextView tvCashbackCoin;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    private String reserved_coin;
    private String unreserved_coin;
    private String cashback_coin;

    private ImageView ivGoBack;

    private BuyCoinBottomSheetFragment bottomSheetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);
        initView();
    }

    private void initView() {
        passbookTab = findViewById(R.id.passbookTab);
        passbookPager = findViewById(R.id.passbookPager);
        lvBuyCoins = findViewById(R.id.lvBuyCoins);
        lvBanks = findViewById(R.id.lvBanks);

        tvTotalCoins = findViewById(R.id.tvTotalCoins);
        tvReservedCoins = findViewById(R.id.tvReservedCoins);
        tvUnReservedCoins = findViewById(R.id.tvUnReservedCoins);
        tvCashbackCoin = findViewById(R.id.tvCashbackCoin);

        ivGoBack = findViewById(R.id.ivGoBack);
        ivGoBack.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        bottomSheetFragment = new BuyCoinBottomSheetFragment();

        lvBuyCoins.setOnClickListener(this);
        lvBanks.setOnClickListener(this);
        setupViewPager();
        setCoinBalance();
    }

    private void setupViewPager() {
        PassbookPagerAdapter passbookPagerAdapter = new PassbookPagerAdapter(getSupportFragmentManager());
        passbookPager.setAdapter(passbookPagerAdapter);
        passbookTab.setupWithViewPager(passbookPager);
    }

    private void setCoinBalance(){

        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("passbook")
                .document("balance")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.getString("reserved") != null){
                            reserved_coin = documentSnapshot.getString("reserved");
                            tvReservedCoins.setText(reserved_coin);
                        }
                        if (documentSnapshot.getString("unreserved") != null){
                            unreserved_coin = documentSnapshot.getString("unreserved");
                            tvUnReservedCoins.setText(unreserved_coin);
                        }
                        if (documentSnapshot.getString("cashback") != null){
                            cashback_coin = documentSnapshot.getString("cashback");
                            tvCashbackCoin.setText(cashback_coin);
                        }

                        if (documentSnapshot.getString("reserved") != null && documentSnapshot.getString("unreserved") != null){
                            int rs_coin = Integer.parseInt(reserved_coin);
                            int us_coin = Integer.parseInt(unreserved_coin);
                            int cb_coin = Integer.parseInt(cashback_coin);
                            int ttl_coin = rs_coin+us_coin+cb_coin;
                            String total_coin = String.valueOf(ttl_coin);
                            tvTotalCoins.setText(total_coin);
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack){
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (v == lvBuyCoins){
            bottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
            bottomSheetFragment.show(getSupportFragmentManager(), "profilePhotoBottomSheet");
        }
        if (v == lvBanks){
            startActivity(new Intent(PassbookActivity.this, BanksActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public void onButtonClicked(String text) {
            if (text.equals("cancel_false")){
                bottomSheetFragment.setCancelable(false);
            }
            else
            if (text.equals("cancel_true")){
                bottomSheetFragment.setCancelable(true);
            }
    }
}
