package com.tetravalstartups.dingdong.modules.subscription;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.passbook.AddBankBottomSheetFragment;
import com.tetravalstartups.dingdong.modules.subscription.model.MySubscriptions;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.DDLoading;
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

        SharedPreferences preferences = getContext().getSharedPreferences("sub_plan", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        master = new Master(getContext());
        setupRecyclerSubscribed();
    }

    private void setupRecyclerSubscribed() {
        recyclerSubs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerSubs.addItemDecoration(new EqualSpacingItemDecoration(24, EqualSpacingItemDecoration.VERTICAL));

        SubPlanPresenter subPlanPresenter = new
                SubPlanPresenter(getContext(),
                SubPlanChooserBottomSheet.this);
        subPlanPresenter.fetchSubscribe();
    }

    @Override
    public void subscribeFetchSuccess(MySubscriptions mySubscriptions) {
        SubPlanAdapter subPlanAdapter = new SubPlanAdapter(getContext(), mySubscriptions.getData());
        subPlanAdapter.notifyDataSetChanged();
        recyclerSubs.setAdapter(subPlanAdapter);
    }

    @Override
    public void subscribeFetchError(String error) {

    }

    @Override
    public void onClick(View view) {
        if (view == tvSelectPlan) {
            SharedPreferences preferences = getContext().getSharedPreferences("sub_plan", 0);
            String id = preferences.getString("id", "none");
            if (id.equals("none")) {
                Toast.makeText(getContext(), "Please select plan from above.", Toast.LENGTH_SHORT).show();
                return;
            }
            subPlanListener.onTaskDone(Constant.UPLOAD_WITH_PLAN);
        }

        if (view == tvSkipPlan) {
            subPlanListener.onTaskDone(Constant.UPLOAD_SKIP_PLAN);
        }
    }

    public interface SubPlanListener {
        void onTaskDone(int type);
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
