package com.tetravalstartups.dingdong.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.MainActivity;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.view.activity.EditProfileActivity;
import com.tetravalstartups.dingdong.utils.DDLoading;

import java.util.concurrent.TimeUnit;

public class CodeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCodeDesc;

    private String mobile_number;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private DDLoading ddLoading;
    private TextView tvVerifyCode;
    private EditText etCode;

    private LinearLayout lhAutoVerify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        initView();
    }

    private void initView() {

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvCodeDesc = findViewById(R.id.tvCodeDesc);
        lhAutoVerify = findViewById(R.id.lhAutoVerify);
        tvVerifyCode = findViewById(R.id.tvVerifyCode);
        etCode = findViewById(R.id.etCode);
        tvVerifyCode.setOnClickListener(this);

        tvCodeDesc.setText(String.format("Please enter code the received on phone number %s",
                getIntent().getStringExtra("phone")));

        mobile_number = getIntent().getStringExtra("phone");

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
               // progressDialog.show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(CodeActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(CodeActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(CodeActivity.this, "Quota Exceeded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        startPhoneNumberVerification(mobile_number);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CodeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference profileRef = db.collection("users").document(uid);
                            profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()){
                                           // progressDialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }else {
                                            startActivity(new Intent(getApplicationContext(), SetupProfileActivity.class));
                                        }
                                        finish();
                                    } else {
                                     //   progressDialog.dismiss();
                                     //   Toast.makeText(OTPActivity.this, "database error (otp->profile)", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                         //   progressDialog.dismiss();
                            Toast.makeText(CodeActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(CodeActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        Toast.makeText(this, "Code Resent", Toast.LENGTH_SHORT).show();
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
            String otpcode = etCode.getText().toString();
            if (TextUtils.isEmpty(otpcode) || !TextUtils.isDigitsOnly(otpcode) || otpcode.length() != 6){
                Toast.makeText(CodeActivity.this, "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
            }
        //    progressDialog.show();
            verifyPhoneNumberWithCode(mVerificationId, otpcode);
        }
    }
}
