package com.tetravalstartups.dingdong.modules.create.sound;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tetravalstartups.dingdong.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesMusicFragment extends Fragment {

    public FavoritesMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_music, container, false);
    }
}
