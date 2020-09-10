package com.tetravalstartups.dingdong.modules.passbook.redeem.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.passbook.CoinRedeemBottomSheet;

public class PayoutConfirmationBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private View view;
    PayoutRequestListener payoutRequestListener;
    private TextView tvOkay, tvCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.payout_confirmation, container, false);
        initView();
        return view;
    }

    private void initView() {
        tvOkay = view.findViewById(R.id.tvOkay);
        tvCancel = view.findViewById(R.id.tvCancel);

        tvOkay.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == tvOkay) {
            payoutRequestListener.payoutData("okay");
        }

        if (view == tvCancel) {
            payoutRequestListener.payoutData("cancel");
        }
    }

    public interface PayoutRequestListener {
        void payoutData(String data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            payoutRequestListener = (PayoutConfirmationBottomSheet.PayoutRequestListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }
}
