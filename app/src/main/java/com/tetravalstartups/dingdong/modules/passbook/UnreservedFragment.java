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

public class UnreservedFragment extends Fragment implements UnreservedCoinTxnPresenter.IUnreservedTxn{

    private View view;
    private RecyclerView recyclerUnreserved;
    private FirebaseAuth auth;
    private TextView tvNoData;

    public UnreservedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_unreserved, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerUnreserved = view.findViewById(R.id.recyclerUnreserved);
        tvNoData = view.findViewById(R.id.tvNoData);

        auth = FirebaseAuth.getInstance();

        setupReservedCoinTxn();
    }

    private void setupReservedCoinTxn() {
        recyclerUnreserved.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerUnreserved.addItemDecoration(new EqualSpacingItemDecoration(16,
                EqualSpacingItemDecoration.VERTICAL));

        UnreservedCoinTxnPresenter unreservedCoinTxnPresenter =
                new UnreservedCoinTxnPresenter(getContext(),
                        UnreservedFragment.this);

        unreservedCoinTxnPresenter.fetchTxn(auth.getCurrentUser().getUid());
    }

    @Override
    public void fetchTxnSuccess(List<UnreservedCoinTxn> unreservedCoinTxnList) {
        tvNoData.setVisibility(View.GONE);
        recyclerUnreserved.setVisibility(View.VISIBLE);
        UnreservedCoinTxnAdapter unreservedCoinTxnAdapter =
                new UnreservedCoinTxnAdapter(getContext(),
                        unreservedCoinTxnList);
        unreservedCoinTxnAdapter.notifyDataSetChanged();
        recyclerUnreserved.setAdapter(unreservedCoinTxnAdapter);
    }

    @Override
    public void fetchTxnError(String error) {
        tvNoData.setVisibility(View.VISIBLE);
        recyclerUnreserved.setVisibility(View.GONE);
    }
}
