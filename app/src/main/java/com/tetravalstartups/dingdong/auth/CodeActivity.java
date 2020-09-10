package com.tetravalstartups.dingdong.auth;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.utils.Constant;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCodeDesc;
    private FirebaseAuth firebaseAuth;
    private DDLoading ddLoading;
    private TextView tvVerifyCode;
    private TextView tvStatus;
    private TextView tvResendCode;
    private EditText etCode;

    private ImageView ivGoBack;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private LinearLayout lhAutoVerify;
    private LinearLayout lvCode;

    private final static String country = "+91";
    private String phone;

    private RequestInterface requestInterface;

    private static final String TAG = "CodeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        initView();
    }

    private void initView() {
        firebaseAuth = FirebaseAuth.getInstance();
        tvCodeDesc = findViewById(R.id.tvCodeDesc);
        lhAutoVerify = findViewById(R.id.lhAutoVerify);
        tvVerifyCode = findViewById(R.id.tvVerifyCode);
        etCode = findViewById(R.id.etCode);
        lvCode = findViewById(R.id.lvCode);
        tvStatus = findViewById(R.id.tvStatus);
        tvResendCode = findViewById(R.id.tvResendCode);
        ivGoBack = findViewById(R.id.ivGoBack);
        tvVerifyCode.setOnClickListener(this);
        tvResendCode.setOnClickListener(this);
        ivGoBack.setOnClickListener(this);

        tvCodeDesc.setText(String.format("Please enter code the received on phone number %s",
                getIntent().getStringExtra("phone")));

        phone = getIntent().getStringExtra("phone");

        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);

        sendCode();
    }

    private void sendCode() {
        lhAutoVerify.setVisibility(View.VISIBLE);
        setUpVerificationCallbacks();
        tvStatus.setText(Constant.AUTH_SENDING_CODE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                country+phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }

    private void setUpVerificationCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(lvCode, "Invalid Credentials", Snackbar.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    lhAutoVerify.setVisibility(View.GONE);
                    Snackbar.make(lvCode, "Limit Reached Try Again In a few Hours", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                tvResendCode.setVisibility(View.VISIBLE);
                tvStatus.setText(Constant.AUTH_AUTO_VERIFY);
                phoneVerificationId = s;
                resendToken = forceResendingToken;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                lhAutoVerify.setVisibility(View.GONE);
            }
        };
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        if (view == tvVerifyCode) {
            if (!isConnected()) {
                Snackbar.make(lvCode, "Please make sure that you are connect to internet.", Snackbar.LENGTH_LONG).show();
            } else {
                doVerifyCode();
            }
        }

        if (view == tvResendCode) {
            resendCode();
        }

        if (view == ivGoBack) {
            onBackPressed();
        }
    }

    private void resendCode() {
        tvResendCode.setVisibility(View.GONE);
        lhAutoVerify.setVisibility(View.VISIBLE);
        setUpVerificationCallbacks();
        tvStatus.setText(Constant.AUTH_RESENDING_CODE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                country+phone,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }

    private void doVerifyCode() {
        String otp = etCode.getText().toString();
        if (TextUtils.isEmpty(otp) || !TextUtils.isDigitsOnly(otp) || otp.length() != 6) {
            Toast.makeText(CodeActivity.this, "Please enter valid OTP.", Toast.LENGTH_LONG).show();
            return;
        }

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, otp);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(lvCode, "Successful", Snackbar.LENGTH_LONG).show();
                            String cred = task.getResult().getUser().getPhoneNumber();
                            cred.replace("+91", "");
                            checkForUser();
                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(lvCode, "Invalid Credentials", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void checkForUser() {
        Call<CheckUserResponse> call = requestInterface.checkUser(phone);
        call.enqueue(new Callback<CheckUserResponse>() {
            @Override
            public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                if (response.code() == 200) {
                    goToHome();
                } else {
                    goToSignUp();
                }
            }

            @Override
            public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(CodeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToSignUp() {
        Intent intent = new Intent(CodeActivity.this, SetupProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
