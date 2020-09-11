package com.tetravalstartups.dingdong.modules.profile.view.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.tetravalstartups.dingdong.R;

public class ContactSupportActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivGoBack;
    private LinearLayout lhMailUs;
    private LinearLayout lhCallUs;
    private LinearLayout lhChatWithUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_support);

        initViews();
    }

    private void initViews() {
        ivGoBack = findViewById(R.id.ivGoBack);
        lhMailUs = findViewById(R.id.lhMailUs);
        lhCallUs = findViewById(R.id.lhCallUs);
        lhChatWithUs = findViewById(R.id.lhChatWithUs);

        ivGoBack.setOnClickListener(this);
        lhCallUs.setOnClickListener(this);
        lhMailUs.setOnClickListener(this);
        lhChatWithUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ivGoBack) {
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (view == lhMailUs) {
            sentMail();
        }
        if (view == lhCallUs){
            callUs();
        }
        if (view == lhChatWithUs){
           startActivity(new Intent(ContactSupportActivity.this, ChatWithUsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void callUs() {
        String[] permissions = {Manifest.permission.CALL_PHONE};
        Permissions.check(this/*context*/, permissions,
                null/*rationale*/, null/*options*/,
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse("tel:" + "9981806327"));
                        startActivity(dialIntent);
                    }
                });

    }

    private void sentMail() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "support@ddcreators.com"));
        startActivity(intent);


    }
}