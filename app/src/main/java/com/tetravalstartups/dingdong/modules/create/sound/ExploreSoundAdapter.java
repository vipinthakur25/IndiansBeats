package com.tetravalstartups.dingdong.modules.create.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.utils.DDLoading;
import com.tetravalstartups.dingdong.utils.MediaPlayerUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExploreSoundAdapter extends RecyclerView.Adapter<ExploreSoundAdapter.ViewHolder> {

    Context context;
    List<Sound> soundList;
    SharedPreferences preferences;
    int row_index = -1;
    DDLoading ddLoading;
    Master master;
    FirebaseFirestore db;

    public ExploreSoundAdapter(Context context, List<Sound> soundList) {
        this.context = context;
        this.soundList = soundList;
    }

    @NonNull
    @Override
    public ExploreSoundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sounds_list_item, parent, false);
        preferences = context.getSharedPreferences("selected_sound", 0);
        ddLoading = DDLoading.getInstance();
        db = FirebaseFirestore.getInstance();
        master = new Master(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreSoundAdapter.ViewHolder holder, int position) {
        Sound sound = soundList.get(position);
        holder.tvName.setText(sound.getName());
        holder.tvArtist.setText(sound.getArtist());
        holder.tvDuration.setText(sound.getDuration());

        if (sound.getBanner().isEmpty()) {
            holder.ivMusicBanner.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.dd_white_small_logo));
        } else {
            Glide.with(context).load(sound.getBanner())
                    .placeholder(R.drawable.dd_white_small_logo)
                    .into(holder.ivMusicBanner);
        }

        if (sound.getArtist().isEmpty()) {
            holder.tvArtist.setText("Unknown");
        }

        holder.lhMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddLoading.showProgress(context, "Loading...", false);
                row_index = position;
                notifyDataSetChanged();
                FileLoader.with(context)
                        .load(sound.getMedia(), false)
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
                        .load(sound.getMedia(), false)
                        .fromDirectory("dingdong/songs", FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(new FileRequestListener<File>() {
                            @Override
                            public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                                File loadedFile = response.getBody();
                                Editor editor = preferences.edit();
                                editor.putString("sound_path", loadedFile.getPath());
                                editor.putString("sound_name", sound.getName());
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

        holder.ivFav.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                db.collection("users")
                        .document(master.getId())
                        .collection("sounds")
                        .document(sound.getId())
                        .set(sound);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                db.collection("users")
                        .document(master.getId())
                        .collection("sounds")
                        .document(sound.getId())
                        .delete();
            }
        });

        db.collection("users")
                .document(master.getId())
                .collection("sounds")
                .document(sound.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                holder.ivFav.setLiked(true);
                            } else {
                                holder.ivFav.setLiked(false);
                            }
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lhMusic, lhUseSound;
        ImageView ivPlayPause, ivMusicBanner;
        TextView tvName, tvArtist, tvDuration;
        LikeButton ivFav;
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