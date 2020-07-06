package com.tetravalstartups.dingdong.modules.create.sound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.MediaPlayerUtils;

public class SoundActivity extends AppCompatActivity {

    private TabLayout tabMusic;
    private ViewPager pagerMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        initView();
    }

    private void initView() {
        tabMusic = findViewById(R.id.tabMusic);
        pagerMusic = findViewById(R.id.pagerMusic);
        setupViewPager();
    }

    private void setupViewPager() {
        SoundViewPagerAdapter soundViewPagerAdapter = new SoundViewPagerAdapter(getSupportFragmentManager());
        pagerMusic.setAdapter(soundViewPagerAdapter);
        tabMusic.setupWithViewPager(pagerMusic);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerUtils.getInstance();
        MediaPlayerUtils.pauseMediaPlayer();
        MediaPlayerUtils.releaseMediaPlayer();
        setupViewPager();
    }
}
