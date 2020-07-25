package com.tetravalstartups.dingdong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tetravalstartups.dingdong.auth.Master;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {



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
    }

    @Override
    protected void onPause() {
        super.onPause();
        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("online")
                .document(m_androidId)
                .delete();
    }
}