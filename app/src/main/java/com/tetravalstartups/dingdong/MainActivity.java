package com.tetravalstartups.dingdong;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.LoginActivity;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.PhoneActivity;
import com.tetravalstartups.dingdong.modules.discover.DiscoverFragment;
import com.tetravalstartups.dingdong.modules.notification.NotificationFragment;
import com.tetravalstartups.dingdong.modules.player.PlayerFragment;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfile;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.ProfileFragment;
import com.tetravalstartups.dingdong.modules.publish.PublishMeta;
import com.tetravalstartups.dingdong.modules.record.RecordActivity;
import com.tetravalstartups.dingdong.service.PublishService;
import com.tetravalstartups.dingdong.utils.DDAlert;
import com.tetravalstartups.dingdong.utils.DialogVideoUploadSuccess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    boolean doubleBackToExitPressedOnce = false;
    private LinearLayout lvHome;
    private LinearLayout lvDiscover;
    private LinearLayout lvNotification;
    private LinearLayout lvProfile;
    private ImageView ivHome;
    private ImageView ivDiscover;
    private ImageView ivCreate;
    private ImageView ivNotification;
    private ImageView ivProfile;
    private TextView tvHome;
    private TextView tvDiscover;
    private TextView tvNotification;
    private TextView tvProfile;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Fragment fragment = null;
    private LinearLayout lvMain;
    private FrameLayout progressUpload;
    private ImageView ivVideoCover;
    private DialogVideoUploadSuccess dialogVideoUploadSuccess;
    private com.tetravalstartups.dingdong.api.RequestInterface RequestInterface;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            String thumbnail = intent.getStringExtra("thumbnail");
            if (status.equals("published")) {
                ivCreate.setVisibility(View.VISIBLE);
                progressUpload.setVisibility(View.GONE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            dialogVideoUploadSuccess.showAlert(MainActivity.this, thumbnail, false);
                        }
                    }
                });
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparentFlag();
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        lvHome = findViewById(R.id.lvHome);
        lvDiscover = findViewById(R.id.lvDiscover);
        lvNotification = findViewById(R.id.lvNotification);
        lvProfile = findViewById(R.id.lvProfile);

        ivHome = findViewById(R.id.ivHome);
        ivDiscover = findViewById(R.id.ivDiscover);
        ivCreate = findViewById(R.id.ivCreate);
        ivNotification = findViewById(R.id.ivNotification);
        ivProfile = findViewById(R.id.ivProfile);

        tvHome = findViewById(R.id.tvHome);
        tvDiscover = findViewById(R.id.tvDiscover);
        tvNotification = findViewById(R.id.tvNotification);
        tvProfile = findViewById(R.id.tvProfile);

        lvMain = findViewById(R.id.lvMain);
        progressUpload = findViewById(R.id.progressUpload);
        ivVideoCover = findViewById(R.id.ivVideoCover);

        lvHome.setOnClickListener(this);
        lvDiscover.setOnClickListener(this);
        ivCreate.setOnClickListener(this);
        lvNotification.setOnClickListener(this);
        lvProfile.setOnClickListener(this);

        Glide.with(this).load(R.drawable.dd_create_video).into(ivCreate);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        RequestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);

        fragment = new PlayerFragment();
        loadFragment(fragment);
        switchHome();

        if (auth.getCurrentUser() != null) {
            String id = auth.getCurrentUser().getUid();
            getProfileData(id);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String thumbnail = bundle.getString("thumbnail");
                showUploadingAnimation(thumbnail);
            }
        }

        dialogVideoUploadSuccess = DialogVideoUploadSuccess.getInstance();

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("publishService"));

    }

    private void showUploadingAnimation(String thumbnail) {
        ivCreate.setVisibility(View.GONE);
        progressUpload.setVisibility(View.VISIBLE);
        Glide.with(MainActivity.this).load(thumbnail).into(ivVideoCover);
    }

    @Override
    public void onClick(View v) {
        if (v == lvHome) {
            fragment = new PlayerFragment();
            loadFragment(fragment);
            switchHome();
        }

        if (v == lvDiscover) {
            fragment = new DiscoverFragment();
            loadFragment(fragment);
            switchSearch();
        }

        if (v == ivCreate) {
            if (auth.getCurrentUser() != null) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO};
                Permissions.check(this/*context*/, permissions,
                        null/*rationale*/, null/*options*/,
                        new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                startActivity(new Intent(MainActivity.this, RecordActivity.class));
                            }
                        });
            } else {
                startActivity(new Intent(MainActivity.this, PhoneActivity.class));
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

        if (v == lvNotification) {
            if (auth.getCurrentUser() != null){
                fragment = new NotificationFragment();
                loadFragment(fragment);
                switchNotification();
            } else {
                startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }

        if (v == lvProfile) {
            fragment = new ProfileFragment();

            if (auth.getCurrentUser() != null) {
                switchProfile();
                loadFragment(fragment);
            } else {
                startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameContent, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    private void switchHome() {
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_home_active));
        ivDiscover.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_discover_inactive));
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_notification_inactive));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_profile_inactive));

        tvHome.setTextColor(getResources().getColor(R.color.colorGradientStart));
        tvDiscover.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvNotification.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvProfile.setTextColor(getResources().getColor(R.color.colorTextTitle));

    }

    private void switchSearch() {
        ivDiscover.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_discover_active));
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_home_inactive));
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_notification_inactive));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_profile_inactive));

        tvDiscover.setTextColor(getResources().getColor(R.color.colorGradientStart));
        tvHome.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvNotification.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvProfile.setTextColor(getResources().getColor(R.color.colorTextTitle));

    }

    private void switchNotification() {
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_notification_active));
        ivDiscover.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_discover_inactive));
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_home_inactive));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_profile_inactive));

        tvNotification.setTextColor(getResources().getColor(R.color.colorGradientStart));
        tvDiscover.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvHome.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvProfile.setTextColor(getResources().getColor(R.color.colorTextTitle));
    }

    private void switchProfile() {
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_profile_active));
        ivDiscover.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_discover_inactive));
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_notification_inactive));
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_home_inactive));

        tvProfile.setTextColor(getResources().getColor(R.color.colorGradientStart));
        tvDiscover.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvNotification.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvHome.setTextColor(getResources().getColor(R.color.colorTextTitle));

        if (auth.getCurrentUser() != null) {
            String id = auth.getCurrentUser().getUid();
            getProfileData(id);
        }

    }

    public void getProfileData(String id) {
        Call<PublicProfile> call = RequestInterface.getUserData(id, id);
        call.enqueue(new Callback<PublicProfile>() {
            @Override
            public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                new Master(MainActivity.this).setUser(response.body().getPublicProfileResponse());
            }

            @Override
            public void onFailure(Call<PublicProfile> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(lvMain, "Please click BACK again to exit", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


}
