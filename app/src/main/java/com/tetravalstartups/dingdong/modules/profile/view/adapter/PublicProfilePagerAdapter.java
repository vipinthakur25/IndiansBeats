package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tetravalstartups.dingdong.modules.profile.view.fragment.CreatedVideoFragment;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.LikedVideoFragment;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.PrivateDraftFragment;

public class PublicProfilePagerAdapter extends FragmentPagerAdapter {

    private String id;

    public PublicProfilePagerAdapter(@NonNull FragmentManager fm, String user_id) {
        super(fm);
        id = user_id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("user_id", id);
                CreatedVideoFragment createdVideoFragment = new CreatedVideoFragment();
                createdVideoFragment.setArguments(bundle);
                return createdVideoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
