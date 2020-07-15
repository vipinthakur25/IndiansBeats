package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tetravalstartups.dingdong.modules.profile.view.fragment.CreatedVideoFragment;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.LikedVideoFragment;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.PrivateDraftFragment;

public class VideoTabPagerAdapter extends FragmentPagerAdapter {

    public VideoTabPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CreatedVideoFragment();
            case 1:
                return new LikedVideoFragment();
            case 2:
                return new PrivateDraftFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
