package com.tetravalstartups.dingdong.modules.create.sound;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SoundViewPagerAdapter extends FragmentPagerAdapter {

    public SoundViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ExploreSoundFragment();
            case 1:
                return new FavoritesSoundFragment();
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
        switch (position) {
            case 0:
                return "Explore";
            case 1:
                return "Favorites";
            default:
                return null;
        }
    }
}
