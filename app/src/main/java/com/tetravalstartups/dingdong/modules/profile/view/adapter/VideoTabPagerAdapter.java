package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tetravalstartups.dingdong.modules.profile.model.PrivateDraftVideos;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.CreatedVideoFragment;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.LikedVideoFragment;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.PrivateDraftFragment;

public class VideoTabPagerAdapter extends FragmentPagerAdapter {

    private String user_id;
    Bundle bundle = new Bundle();

    public VideoTabPagerAdapter(@NonNull FragmentManager fm, String id) {
        super(fm);
        user_id = id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                bundle.putString("user_id", user_id);
                bundle.putString("type", "1");
                CreatedVideoFragment createdVideoFragment = new CreatedVideoFragment();
                createdVideoFragment.setArguments(bundle);
                return createdVideoFragment;
            case 1:
                bundle.putString("user_id", user_id);
                LikedVideoFragment likedVideoFragment = new LikedVideoFragment();
                likedVideoFragment.setArguments(bundle);
                return likedVideoFragment;
            case 2:
                bundle.putString("user_id", user_id);
                PrivateDraftFragment privateDraftFragment = new PrivateDraftFragment();
                privateDraftFragment.setArguments(bundle);
                return privateDraftFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
