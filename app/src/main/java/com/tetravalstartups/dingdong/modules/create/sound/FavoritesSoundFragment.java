package com.tetravalstartups.dingdong.modules.create.sound;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;
import com.tetravalstartups.dingdong.utils.MediaPlayerUtils;

import java.util.List;

public class FavoritesSoundFragment extends Fragment implements FavoriteSoundPresenter.IFav {

    private View view;
    private RecyclerView recyclerFavMusic;

    public FavoritesSoundFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites_music, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerFavMusic = view.findViewById(R.id.recyclerFavMusic);
        fetchMusic();
    }

    private void fetchMusic() {
        recyclerFavMusic.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerFavMusic.addItemDecoration(new EqualSpacingItemDecoration(16,
                EqualSpacingItemDecoration.VERTICAL));

        FavoriteSoundPresenter favoriteSoundPresenter =
                new FavoriteSoundPresenter(getContext(),
                        FavoritesSoundFragment.this);

        favoriteSoundPresenter.fetchFavMusic();
    }

    @Override
    public void musicFetchSuccess(List<FavSound> favSoundList) {
        FavSoundAdapter favoriteSoundAdapter =
                new FavSoundAdapter(getContext(), favSoundList);
        favoriteSoundAdapter.notifyDataSetChanged();
        recyclerFavMusic.setAdapter(favoriteSoundAdapter);
    }

    @Override
    public void musicFetchError(String error) {
        Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayerUtils.getInstance();
        if (MediaPlayerUtils.isPlaying()){
            MediaPlayerUtils.pauseMediaPlayer();
            MediaPlayerUtils.releaseMediaPlayer();
        }
    }
}
