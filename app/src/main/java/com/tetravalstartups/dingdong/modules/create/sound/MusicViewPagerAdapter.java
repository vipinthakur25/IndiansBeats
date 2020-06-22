package com.tetravalstartups.dingdong.modules.create.sound;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MusicViewPagerAdapter extends FragmentPagerAdapter {

    public MusicViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ExploreMusicFragment();
            case 1:
                return new FavoritesMusicFragment();
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
