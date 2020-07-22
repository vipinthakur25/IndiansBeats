package com.tetravalstartups.dingdong;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tetravalstartups.dingdong.auth.LoginActivity;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.auth.Profile;
import com.tetravalstartups.dingdong.modules.home.HomeFragment;
import com.tetravalstartups.dingdong.modules.player.PlayerFragment;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.ProfileFragment;
import com.tetravalstartups.dingdong.modules.record.RecordActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

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

    Fragment fragment = null;

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

        lvHome.setOnClickListener(this);
        lvDiscover.setOnClickListener(this);
        ivCreate.setOnClickListener(this);
        lvNotification.setOnClickListener(this);
        lvProfile.setOnClickListener(this);

        Glide.with(this).load(R.drawable.dd_create_video).into(ivCreate);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fragment = new PlayerFragment();
        loadFragment(fragment);
        switchHome();

        if (auth.getCurrentUser() != null){
            String id = auth.getCurrentUser().getUid();
            getProfileData(id);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == lvHome){
            fragment = new PlayerFragment();
            loadFragment(fragment);
            switchHome();
        }
        if (v == lvDiscover){
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
//            fragment = new DiscoverFragment();
//            loadFragment(fragment);
//            switchSearch();
        }
        if (v == ivCreate){
            if (auth.getCurrentUser() != null){
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
        if (v == lvNotification){
//            if (auth.getCurrentUser() != null){
//                fragment = new NotificationFragment();
//                loadFragment(fragment);
//                switchNotification();
//            } else {
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        if (v == lvProfile){
            fragment = new ProfileFragment();

            if (auth.getCurrentUser() != null){
               switchProfile();
                loadFragment(fragment);
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

    private void switchHome(){
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_home_active));
        ivDiscover.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_discover_inactive));
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_notification_inactive));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_profile_inactive));

        tvHome.setTextColor(getResources().getColor(R.color.colorBrandYellow));
        tvDiscover.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvNotification.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvProfile.setTextColor(getResources().getColor(R.color.colorTextTitle));

    }

    private void switchSearch(){
        ivDiscover.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_discover_active));
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_home_inactive));
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_notification_inactive));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_profile_inactive));

        tvDiscover.setTextColor(getResources().getColor(R.color.colorBrandYellow));
        tvHome.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvNotification.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvProfile.setTextColor(getResources().getColor(R.color.colorTextTitle));

    }

    private void switchNotification(){
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_notification_active));
        ivDiscover.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_discover_inactive));
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_home_inactive));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_profile_inactive));

        tvNotification.setTextColor(getResources().getColor(R.color.colorBrandYellow));
        tvDiscover.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvHome.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvProfile.setTextColor(getResources().getColor(R.color.colorTextTitle));

//        fm.beginTransaction().hide(active).show(notificationFragment).commit();
//        active = notificationFragment;

    }

    private void switchProfile(){
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_profile_active));
        ivDiscover.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_discover_inactive));
        ivNotification.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_notification_inactive));
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_dd_home_inactive));

        tvProfile.setTextColor(getResources().getColor(R.color.colorBrandYellow));
        tvDiscover.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvNotification.setTextColor(getResources().getColor(R.color.colorTextTitle));
        tvHome.setTextColor(getResources().getColor(R.color.colorTextTitle));

        if (auth.getCurrentUser() != null){
            String id = auth.getCurrentUser().getUid();
            getProfileData(id);
        }

    }

    public void getProfileData(String id){
        db.collection("users")
                .document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Profile profile = documentSnapshot.toObject(Profile.class);
                        new Master(MainActivity.this).setUser(profile);
                    }
                });
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
