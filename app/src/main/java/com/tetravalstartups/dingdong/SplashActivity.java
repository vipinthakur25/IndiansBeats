package com.tetravalstartups.dingdong;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.auth.SetupProfileActivity;

public class SplashActivity extends BaseActivity {

    private ImageView ivSplash;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setStatusBarTransparentFlag();
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {

        ivSplash = findViewById(R.id.ivSplash);
        Glide.with(this).load(R.drawable.dd_gif_splash).into(ivSplash);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) {
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
                    Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(splashIntent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, 2000);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        boolean animationStarted = false;
        if (!hasFocus || animationStarted) {
            return;
        }
        super.onWindowFocusChanged(hasFocus);
    }

}
