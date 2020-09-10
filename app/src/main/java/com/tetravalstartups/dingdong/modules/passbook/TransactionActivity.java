package com.tetravalstartups.dingdong.modules.passbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.passbook.transactions.model.Txn;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.List;

public class TransactionActivity extends AppCompatActivity implements TransactionPresenter.ITransaction, View.OnClickListener {

    private TextView tvNoData;
    private RecyclerView recyclerTxn;
    private FirebaseAuth firebaseAuth;
    private ImageView ivGoBack;
    private DDLoading ddLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        
        initView();
        
    }

    private void initView() {
        recyclerTxn = findViewById(R.id.recyclerTxn);
        recyclerTxn.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
        tvNoData = findViewById(R.id.tvNoData);
        ivGoBack = findViewById(R.id.ivGoBack);
        firebaseAuth = FirebaseAuth.getInstance();

        ivGoBack.setOnClickListener(this);

        String id = firebaseAuth.getCurrentUser().getUid();

        ddLoading = DDLoading.getInstance();

        fetchTxn(id);
    }

    private void fetchTxn(String id) {
        TransactionPresenter transactionPresenter =
                new TransactionPresenter(TransactionActivity.this,
                TransactionActivity.this);
        ddLoading.showProgress(TransactionActivity.this, "Fetching Data...", false);
        transactionPresenter.fetchTxn(id);
    }

    @Override
    public void fetchSuccess(Txn txn) {
        TransactionAdapter transactionAdapter =
                new TransactionAdapter(TransactionActivity.this,
                txn.getData());
        recyclerTxn.setAdapter(transactionAdapter);
        tvNoData.setVisibility(View.GONE);
        recyclerTxn.setVisibility(View.VISIBLE);
        ddLoading.hideProgress();
    }

    @Override
    public void fetchError(String error) {
        tvNoData.setVisibility(View.VISIBLE);
        recyclerTxn.setVisibility(View.GONE);
        ddLoading.hideProgress();
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
        }
    }
}