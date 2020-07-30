package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.AddBankBottomSheetFragment;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.List;

public class SubPlanChooserBottomSheet extends BottomSheetDialogFragment implements SubPlanPresenter.ISubPlan, View.OnClickListener {

    private View view;
    private RecyclerView recyclerSubs;
    private TextView tvSelectPlan;
    private SubPlanListener subPlanListener;
    private Master master;
    private TextView tvSkipPlan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choose_sub_bottom_sheet_layout, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerSubs = view.findViewById(R.id.recyclerSubs);
        tvSelectPlan = view.findViewById(R.id.tvSelectPlan);
        tvSkipPlan = view.findViewById(R.id.tvSkipPlan);

        tvSelectPlan.setOnClickListener(this);
        tvSkipPlan.setOnClickListener(this);

        master = new Master(getContext());
        setupRecyclerSubscribed(master.getId());
    }

    private void setupRecyclerSubscribed(String uid) {
        recyclerSubs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerSubs.addItemDecoration(new EqualSpacingItemDecoration(24, EqualSpacingItemDecoration.VERTICAL));

        SubPlanPresenter subPlanPresenter = new
                SubPlanPresenter(getContext(),
                SubPlanChooserBottomSheet.this);
        subPlanPresenter.fetchSubscribe(uid);
    }

    @Override
    public void subscribeFetchSuccess(List<SubPlan> subPlanList) {
        SubPlanAdapter subPlanAdapter = new SubPlanAdapter(getContext(), subPlanList);
        subPlanAdapter.notifyDataSetChanged();
        recyclerSubs.setAdapter(subPlanAdapter);
    }

    @Override
    public void subscribeFetchError(String error) {

    }

    @Override
    public void onClick(View view) {
        if (view == tvSelectPlan) {
            subPlanListener.onTaskDone("plan");
        }

        if (view == tvSkipPlan) {
            subPlanListener.onTaskDone("no_plan");
        }
    }

    public interface SubPlanListener {
        void onTaskDone(String text);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            subPlanListener = (SubPlanChooserBottomSheet.SubPlanListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }


}
