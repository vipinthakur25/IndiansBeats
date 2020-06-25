package com.tetravalstartups.dingdong.modules.create.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.utils.MediaPlayerUtils;

import java.io.IOException;
import java.util.List;

public class ExploreMusicAdapter extends RecyclerView.Adapter<ExploreMusicAdapter.ViewHolder>{

    Context context;
    List<Music> musicList;
    SharedPreferences preferences;
    int row_index = -1;

    public ExploreMusicAdapter(Context context, List<Music> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public ExploreMusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sounds_list_item, parent, false);
        preferences = context.getSharedPreferences("selected_sound", 0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreMusicAdapter.ViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.tvName.setText(music.getName());
        holder.tvArtist.setText(music.getArtist());
        holder.tvDuration.setText(music.getDuration());

        if (music.getBanner().isEmpty()) {
            holder.ivMusicBanner.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.dd_logo_placeholder));
        } else {
            Glide.with(context).load(music.getBanner())
                    .placeholder(R.drawable.dd_logo_placeholder)
                    .into(holder.ivMusicBanner);
        }

        if (music.getArtist().isEmpty()) {
            holder.tvArtist.setText("Unknown");
        }

        holder.lhMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();

                MediaPlayerUtils.getInstance();
                if (MediaPlayerUtils.isPlaying()){
                    holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_play_white));
                    MediaPlayerUtils.pauseMediaPlayer();
                    MediaPlayerUtils.releaseMediaPlayer();
                }

                try {

                    holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_pause_white));

                    MediaPlayerUtils.startAndPlayMediaPlayer(music.getMedia(), new MediaPlayerUtils.Listener() {
                        @Override
                        public void onAudioComplete() {
                            holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_play_white));
                        }

                        @Override
                        public void onAudioUpdate(int currentPosition) {
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.lhUseSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editor editor = preferences.edit();
                editor.putString("sound_url", music.getMedia());
                editor.putString("sound_name", music.getName());
                editor.apply();
                ((SoundActivity)context).finish();
            }
        });


        if(row_index==position){
            holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_pause_white));
            holder.lhUseSound.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_play_white));
            holder.lhUseSound.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhMusic, lhUseSound;
        ImageView ivPlayPause, ivMusicBanner;
        TextView tvName, tvArtist, tvDuration;
        ImageView ivFav;
        CardView cardSound;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lhMusic = itemView.findViewById(R.id.lhMusic);
            ivPlayPause = itemView.findViewById(R.id.ivPlayPause);
            ivMusicBanner = itemView.findViewById(R.id.ivMusicBanner);
            tvName = itemView.findViewById(R.id.tvName);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            ivFav = itemView.findViewById(R.id.ivFav);
            cardSound = itemView.findViewById(R.id.cardSound);
            lhUseSound = itemView.findViewById(R.id.lhUseSound);

        }
    }
}