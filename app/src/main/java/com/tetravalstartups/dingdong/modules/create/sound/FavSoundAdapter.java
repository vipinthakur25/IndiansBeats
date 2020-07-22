package com.tetravalstartups.dingdong.modules.create.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.like.LikeButton;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.MediaPlayerUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FavSoundAdapter extends RecyclerView.Adapter<FavSoundAdapter.ViewHolder> {

    Context context;
    List<FavSound> favSoundList;
    SharedPreferences preferences;
    int row_index = -1;
    DDLoading ddLoading;
    Master master;
    FirebaseFirestore db;

    public FavSoundAdapter(Context context, List<FavSound> favSoundList) {
        this.context = context;
        this.favSoundList = favSoundList;
    }

    @NonNull
    @Override
    public FavSoundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_sounds_list_item, parent, false);
        preferences = context.getSharedPreferences("selected_sound", 0);
        ddLoading = DDLoading.getInstance();
        db = FirebaseFirestore.getInstance();
        master = new Master(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavSoundAdapter.ViewHolder holder, int position) {
        FavSound favSound = favSoundList.get(position);

        holder.tvName.setText(favSound.getName());
        holder.tvArtist.setText(favSound.getArtist());
        holder.tvDuration.setText(favSound.getDuration());

        if (favSound.getBanner().isEmpty()) {
            holder.ivMusicBanner.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.dd_white_small_logo));
        } else {
            Glide.with(context).load(favSound.getBanner())
                    .placeholder(R.drawable.dd_white_small_logo)
                    .into(holder.ivMusicBanner);
        }

        if (favSound.getArtist().isEmpty()) {
            holder.tvArtist.setText("Unknown");
        }

        holder.lhMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddLoading.showProgress(context, "Loading...", false);
                row_index = position;
                notifyDataSetChanged();
                FileLoader.with(context)
                        .load(favSound.getMedia(), false)
                        .fromDirectory("dingdong/songs", FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                File loadedFile = response.getBody();
                                ddLoading.hideProgress();
                                MediaPlayerUtils.getInstance();
                                if (MediaPlayerUtils.isPlaying()) {
                                    holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_play_white));
                                    MediaPlayerUtils.pauseMediaPlayer();
                                    MediaPlayerUtils.releaseMediaPlayer();
                                }

                                try {
                                    holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_pause_white));
                                    MediaPlayerUtils.startAndPlayMediaPlayer(loadedFile.getPath(), new MediaPlayerUtils.Listener() {
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

                            @Override
                            public void onError(FileLoadRequest request, Throwable t) {
                                ddLoading.hideProgress();
                                Toast.makeText(context, "ERR CVEM0", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        holder.lhUseSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDLoading loading = DDLoading.getInstance();
                loading.showProgress(context, "Loading...", false);
                FileLoader.with(context)
                        .load(favSound.getMedia(), false)
                        .fromDirectory("dingdong/songs", FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                File loadedFile = response.getBody();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("sound_path", loadedFile.getPath());
                                editor.putString("sound_name", favSound.getName());
                                editor.apply();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.hideProgress();
                                        ((SoundActivity) context).finish();
                                    }
                                }, 0);
                            }

                            @Override
                            public void onError(FileLoadRequest request, Throwable t) {
                                loading.hideProgress();
                                Toast.makeText(context, "ERR CVEM0", Toast.LENGTH_SHORT).show();
                                holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_play_white));
                            }
                        });

            }
        });


        if (row_index == position) {
            holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_pause_white));
            holder.lhUseSound.setVisibility(View.VISIBLE);
        } else {
            holder.ivPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dd_music_play_white));
            holder.lhUseSound.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return favSoundList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhMusic, lhUseSound;
        ImageView ivPlayPause, ivMusicBanner;
        TextView tvName, tvArtist, tvDuration;
        CardView cardSound;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lhMusic = itemView.findViewById(R.id.lhMusic);
            ivPlayPause = itemView.findViewById(R.id.ivPlayPause);
            ivMusicBanner = itemView.findViewById(R.id.ivMusicBanner);
            tvName = itemView.findViewById(R.id.tvName);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            cardSound = itemView.findViewById(R.id.cardSound);
            lhUseSound = itemView.findViewById(R.id.lhUseSound);

        }
    }
}
