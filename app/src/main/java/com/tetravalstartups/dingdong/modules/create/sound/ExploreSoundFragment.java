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

public class ExploreSoundFragment extends Fragment implements ExploreSoundPresenter.IExploreMusic{

    private View view;
    private RecyclerView recyclerExploreMusic;

    public ExploreSoundFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore_music, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerExploreMusic = view.findViewById(R.id.recyclerExploreMusic);
        fetchMusic();
    }

    private void fetchMusic() {
        recyclerExploreMusic.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerExploreMusic.addItemDecoration(new EqualSpacingItemDecoration(16,
                EqualSpacingItemDecoration.VERTICAL));

        ExploreSoundPresenter exploreSoundPresenter =
                new ExploreSoundPresenter(getContext(),
                        ExploreSoundFragment.this);

        exploreSoundPresenter.fetchExploreMusic();
    }

    @Override
    public void musicFetchSuccess(List<Sound> soundList) {
        ExploreSoundAdapter exploreSoundAdapter =
                new ExploreSoundAdapter(getContext(), soundList);
        exploreSoundAdapter.notifyDataSetChanged();
        recyclerExploreMusic.setAdapter(exploreSoundAdapter);
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
