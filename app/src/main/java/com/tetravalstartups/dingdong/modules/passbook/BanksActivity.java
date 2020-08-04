package com.tetravalstartups.dingdong.modules.passbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.List;

public class BanksActivity extends AppCompatActivity implements View.OnClickListener, AddBankBottomSheetFragment.AddBankListener, BanksPresenter.IBanks {

    private ImageView ivGoBack;
    private ImageView ivAddBank;
    private AddBankBottomSheetFragment addBankBottomSheetFragment;
    private RecyclerView recyclerBanks;
    private FirebaseAuth firebaseAuth;
    private TextView tvNoData;
    private LinearLayout lvMain;
    private int bank_acc_count = 0;

    private BanksPresenter banksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banks);
        initView();
    }

    private void initView() {
        ivGoBack = findViewById(R.id.ivGoBack);
        ivAddBank = findViewById(R.id.ivAddBank);
        recyclerBanks = findViewById(R.id.recyclerBanks);
        tvNoData = findViewById(R.id.tvNoData);

        lvMain = findViewById(R.id.lvMain);

        ivGoBack.setOnClickListener(this);
        ivAddBank.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        addBankBottomSheetFragment = new AddBankBottomSheetFragment();


        setupRecycler();

    }

    private void setupRecycler() {
        recyclerBanks.setLayoutManager(new LinearLayoutManager(BanksActivity.this));
        recyclerBanks.addItemDecoration(new EqualSpacingItemDecoration(16,
                EqualSpacingItemDecoration.VERTICAL));

        banksPresenter =
                new BanksPresenter(BanksActivity.this,
                BanksActivity.this);

        banksPresenter.fetchBanks(firebaseAuth.getCurrentUser().getUid());

    }

    @Override
    public void onClick(View v) {
        if (v == ivGoBack){
            onBackPressed();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (v == ivAddBank){
            banksPresenter.fetchBanks(firebaseAuth.getCurrentUser().getUid());
            if (bank_acc_count == 0) {
                if (getSupportFragmentManager().findFragmentByTag("addBanks") == null){
                    addBankBottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
                    addBankBottomSheetFragment.show(getSupportFragmentManager(), "addBanks");
                }
            } else if (bank_acc_count == 1) {
                Snackbar.make(lvMain, "You can only one bank", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onTaskDone(String text) {
        if (text.equals("bank_added")){
            ivAddBank.setEnabled(true);
            addBankBottomSheetFragment.setCancelable(true);
            addBankBottomSheetFragment.dismiss();
        }

        if (text.equals("cancel_false")){
            addBankBottomSheetFragment.setCancelable(false);
        }
    }

    @Override
    public void fetchBanksSuccess(List<Banks> banksList) {
        tvNoData.setVisibility(View.GONE);
        recyclerBanks.setVisibility(View.VISIBLE);
        BanksAdapter banksAdapter =
                new BanksAdapter(BanksActivity.this,
                        banksList);
        banksAdapter.notifyDataSetChanged();
        recyclerBanks.setAdapter(banksAdapter);
        bank_acc_count = banksList.size();
    }

    @Override
    public void fetchBanksError(String error) {
        tvNoData.setVisibility(View.VISIBLE);
        recyclerBanks.setVisibility(View.GONE);
    }
}
