package com.tetravalstartups.dingdong.modules.passbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.List;

public class ReservedFragment extends Fragment implements ReservedCoinTxnPresenter.ITxn {
    
    private View view;
    private RecyclerView recyclerReserved;
    private FirebaseAuth auth;
    private TextView tvNoData;

    public ReservedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reserved, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerReserved = view.findViewById(R.id.recyclerReserved);
        tvNoData = view.findViewById(R.id.tvNoData);

        auth = FirebaseAuth.getInstance();

        setupReservedCoinTxn();
    }

    private void setupReservedCoinTxn() {
        recyclerReserved.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReserved.addItemDecoration(new EqualSpacingItemDecoration(16,
                EqualSpacingItemDecoration.VERTICAL));

        ReservedCoinTxnPresenter reservedCoinTxnPresenter =
                new ReservedCoinTxnPresenter(getContext(),
                ReservedFragment.this);

        reservedCoinTxnPresenter.fetchTxn(auth.getCurrentUser().getUid());

    }

    @Override
    public void fetchTxnSuccess(List<ReservedCoinsTxn> reservedCoinsTxnList) {
        tvNoData.setVisibility(View.GONE);
        recyclerReserved.setVisibility(View.VISIBLE);
        ReservedCoinsTxnAdapter reservedCoinsTxnAdapter =
                new ReservedCoinsTxnAdapter(getContext(),
                reservedCoinsTxnList);
        reservedCoinsTxnAdapter.notifyDataSetChanged();
        recyclerReserved.setAdapter(reservedCoinsTxnAdapter);
    }

    @Override
    public void fetchTxnError(String error) {
        tvNoData.setVisibility(View.VISIBLE);
        recyclerReserved.setVisibility(View.GONE);
    }
}
