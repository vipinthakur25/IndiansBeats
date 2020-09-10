package com.tetravalstartups.dingdong.modules.passbook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class AddBankBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private View view;
    private AddBankListener addBankListener;

    private EditText etName;
    private EditText etAccount;
    private EditText etReAccount;
    private EditText etIFSC;
    private TextView tvAddBank;
    private TextView tvBankName;
    private TextView tvBankAddress;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressIFSC;
    private ProgressBar progressAddBank;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_bank_bottom_sheet_layout, container, false);
        initView();
        return view;
    }

    private void initView() {
        etName = view.findViewById(R.id.etName);
        etAccount = view.findViewById(R.id.etAccount);
        etReAccount = view.findViewById(R.id.etReAccount);
        etIFSC = view.findViewById(R.id.etIFSC);
        tvAddBank = view.findViewById(R.id.tvAddBank);
        tvBankName = view.findViewById(R.id.tvBankName);
        tvBankAddress = view.findViewById(R.id.tvBankAddress);
        progressIFSC = view.findViewById(R.id.progressIFSC);
        progressAddBank = view.findViewById(R.id.progressAddBank);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        etIFSC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ifsc = etIFSC.getText().toString();
                if (ifsc.length() == 11){
                    tvAddBank.setEnabled(true);
                    hideKeyboardFrom(getContext(), view);
                    validateIFSC(ifsc);
                } else {
                    etIFSC.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tvBankName.setText("");
                    tvBankAddress.setText("");
                    tvBankName.setVisibility(View.GONE);
                    tvBankAddress.setVisibility(View.GONE);
                    progressIFSC.setVisibility(View.INVISIBLE);
                    tvAddBank.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvAddBank.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == tvAddBank){
            doUiValidation();
        }
    }

    private void validateIFSC(String ifsc) {
        progressIFSC.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://ifsc.razorpay.com/" + ifsc;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String BANK = jsonObject.getString("BANK");
                            String ADDRESS = jsonObject.getString("ADDRESS");
                            etIFSC.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dd_check_mark_green, 0);

                            tvBankName.setVisibility(View.VISIBLE);
                            tvBankAddress.setVisibility(View.VISIBLE);

                            tvBankName.setText(BANK);
                            tvBankAddress.setText(ADDRESS);
                            progressIFSC.setVisibility(View.INVISIBLE);
                            tvAddBank.setEnabled(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                etIFSC.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dd_cancel_red, 0);
                tvBankName.setText("");
                tvBankAddress.setText("");
                tvBankName.setVisibility(View.GONE);
                tvBankAddress.setVisibility(View.GONE);
                progressIFSC.setVisibility(View.INVISIBLE);
                tvAddBank.setEnabled(false);
            }
        });
        queue.add(stringRequest);
    }

    private void doUiValidation() {
        String name = etName.getText().toString();
        String account = etAccount.getText().toString();
        String re_account = etReAccount.getText().toString();
        String ifsc = etIFSC.getText().toString();

        if (TextUtils.isEmpty(name)){
            etName.requestFocus();
            etName.setError("Name required");
            return;
        }

        if (TextUtils.isEmpty(account)){
            etAccount.requestFocus();
            etAccount.setError("Account number required");
            return;
        }

        if (TextUtils.isEmpty(re_account)){
            etReAccount.requestFocus();
            etReAccount.setError("Repeat account number required");
            return;
        }

        if (TextUtils.isEmpty(ifsc)){
            etIFSC.requestFocus();
            etIFSC.setError("IFSC code required");
            return;
        }

        if (ifsc.length() < 11){
            etIFSC.requestFocus();
            etIFSC.setError("IFSC code must be of 11 characters");
            return;
        }

        if (!account.equals(re_account)){
            etAccount.requestFocus();
            etAccount.setError("Account number did not match");
            etReAccount.setError("Account number did not match");
            return;
        }

        String branch = tvBankName.getText().toString();
        String address = tvBankAddress.getText().toString();
        addBankListener.onTaskDone("cancel_false");
        tvAddBank.setEnabled(false);
        progressAddBank.setVisibility(View.VISIBLE);
        doAddBank(name, account, ifsc, branch, address);

    }

    private void doAddBank(String name, String account, String ifsc, String branch, String address) {

        DocumentReference documentReference = db.collection("customers").document();
        String id = documentReference.getId();

        Banks banks = new Banks();
        banks.setId(id);
        banks.setName(name);
        banks.setAccount(account);
        banks.setIfsc(ifsc);
        banks.setBranch(branch);
        banks.setAddress(address);
        banks.setStatus(Constant.BANK_UNVERIFIED);

        db.collection("customers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("banks")
                .document(id)
                .set(banks)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        etName.setText("");
                        etAccount.setText("");
                        etReAccount.setText("");
                        etIFSC.setText("");
                        tvAddBank.setEnabled(true);
                        progressAddBank.setVisibility(View.GONE);
                        addBankListener.onTaskDone("bank_added");
                    }
                });
    }

    public interface AddBankListener {
        void onTaskDone(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            addBankListener = (AddBankBottomSheetFragment.AddBankListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
