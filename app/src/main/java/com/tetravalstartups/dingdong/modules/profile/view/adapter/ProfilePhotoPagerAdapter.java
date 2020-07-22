package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tetravalstartups.dingdong.modules.profile.view.fragment.AvatarPhotoFragment;
import com.tetravalstartups.dingdong.modules.profile.view.fragment.UploadPhotoFragment;

public class ProfilePhotoPagerAdapter extends FragmentPagerAdapter {

    public ProfilePhotoPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UploadPhotoFragment();
            case 1:
                return new AvatarPhotoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Gallery";
            case 1:
                return "Avatar";
            default:
                return null;
        }
    }
}
