package com.tetravalstartups.dingdong.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.ProfilePhotoPagerAdapter;

public class ProfilePhotoBottomSheet extends BottomSheetDialogFragment {

    private View view;
    private BottomSheetListener mListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_photo_bottom_sheet, container, false);
        initView();
        return view;
    }

    private void initView() {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        setUpTabPager();

    }

    private void setUpTabPager() {
        ProfilePhotoPagerAdapter profilePhotoPagerAdapter = new ProfilePhotoPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(profilePhotoPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }
}
