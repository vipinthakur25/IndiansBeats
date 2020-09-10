package com.tetravalstartups.dingdong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.auth.Master;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends AppCompatActivity {

    private Handler handler;
    private Timer timer;
    private TimerTask doAsynchronousTask;
    private Master master;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        HashMap hashMap = new HashMap();
        hashMap.put("device_id", m_androidId+"");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("online")
                .document(m_androidId)
                .set(hashMap);

        firebaseAuth = FirebaseAuth.getInstance();
        master = new Master(BaseActivity.this);


    }

    protected void setStatusBarTransparentFlag() {

        View decorView = getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener((v, insets) -> {
            WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
            return defaultInsets.replaceSystemWindowInsets(
                    defaultInsets.getSystemWindowInsetLeft(),
                    0,
                    defaultInsets.getSystemWindowInsetRight(),
                    defaultInsets.getSystemWindowInsetBottom());
        });
        ViewCompat.requestApplyInsets(decorView);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    protected void removeStatusBarTransparentFlag() {
        View decorView = getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener((v, insets) -> {
            WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
            return defaultInsets.replaceSystemWindowInsets(
                    defaultInsets.getSystemWindowInsetLeft(),
                    defaultInsets.getSystemWindowInsetTop(),
                    defaultInsets.getSystemWindowInsetRight(),
                    defaultInsets.getSystemWindowInsetBottom());
        });
        ViewCompat.requestApplyInsets(decorView);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }



    @Override
    protected void onResume() {
        super.onResume();
        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        HashMap hashMap = new HashMap();
        hashMap.put("device_id", m_androidId+"");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("online")
                .document(m_androidId)
                .set(hashMap);

        if (firebaseAuth.getCurrentUser() != null) {
            handler = new Handler();
            timer = new Timer();
            doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                db.collection("customers")
                                        .document(firebaseAuth.getCurrentUser().getUid())
                                        .collection("passbook")
                                        .document("balance")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                String time_spent = task.getResult().getString("time_spent");
                                                int time = Integer.parseInt(time_spent);
                                                int update_time_spent = time + 10;

                                                HashMap hashSpent = new HashMap();
                                                hashSpent.put("time_spent", update_time_spent+"");

                                                db.collection("customers")
                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                        .collection("passbook")
                                                        .document("balance")
                                                        .update(hashSpent);

                                            }
                                        });
                            } catch (Exception e) {
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 600000, 600000);
        }


    }

    private void rewardUser() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("online")
                .document(m_androidId)
                .delete();

        if (firebaseAuth.getCurrentUser() != null) {
            timer.cancel();
            doAsynchronousTask.cancel();
        }

    }

}