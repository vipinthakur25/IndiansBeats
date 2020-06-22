package com.tetravalstartups.dingdong.modules.create.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetravalstartups.dingdong.R;

import java.util.List;

public class ExploreMusicAdapter extends RecyclerView.Adapter<ExploreMusicAdapter.ViewHolder> {

    Context context;
    List<Music> musicList;
    int state = 0;
    MediaPlayer mediaPlayer;


    public ExploreMusicAdapter(Context context, List<Music> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public ExploreMusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item, parent, false);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreMusicAdapter.ViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.tvName.setText(music.getName());
        holder.tvArtist.setText(music.getArtist());
        holder.tvDuration.setText(music.getDuration());

        Glide.with(context).load(music.getBanner()).placeholder(R.drawable.dingdong_placeholder).into(holder.ivMusicBanner);

        if (music.isFavorite()){
            holder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_liked_video_active));
        } else {
            holder.ivFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_liked_video_inactive));
        }

        if (music.getBanner().isEmpty()){
            holder.ivMusicBanner.setImageDrawable(context.getResources().getDrawable(R.drawable.dingdong_placeholder));
        }

        if (music.getArtist().isEmpty()){
            holder.tvArtist.setText("Unknown");
        }

        holder.ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhMusic;
        ImageView ivPlayPause, ivMusicBanner;
        TextView tvName, tvArtist, tvDuration;
        ImageView ivFav, ivAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lhMusic = itemView.findViewById(R.id.lhMusic);
            ivPlayPause = itemView.findViewById(R.id.ivPlayPause);
            ivMusicBanner = itemView.findViewById(R.id.ivMusicBanner);
            tvName = itemView.findViewById(R.id.tvName);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            ivFav = itemView.findViewById(R.id.ivFav);
            ivAdd = itemView.findViewById(R.id.ivAdd);

        }
    }
}
