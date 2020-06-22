package com.tetravalstartups.dingdong.modules.passbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PassbookPagerAdapter extends FragmentPagerAdapter {
    public PassbookPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ReservedFragment();

            case 1:
                return new UnreservedFragment();

            case 2:
                return new CashbackFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Reserved Coins";

            case 1:
                return "Unreserved Coins";

            case 2:
                return "Cashback Coins";

            default:
                return null;
        }
    }
}
