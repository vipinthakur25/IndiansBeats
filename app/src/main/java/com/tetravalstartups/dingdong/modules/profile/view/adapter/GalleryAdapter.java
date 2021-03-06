package com.tetravalstartups.dingdong.modules.profile.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tetravalstartups.dingdong.api.APIClient;
import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.api.RequestInterface;
import com.tetravalstartups.dingdong.auth.Master;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfile;
import com.tetravalstartups.dingdong.modules.profile.view.activity.EditProfileActivity;
import com.tetravalstartups.dingdong.utils.DDLoadingProgress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    Context context;
    List<String> galleryPhotoList;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;
    Master master;
    DDLoadingProgress ddLoadingProgress;
    private RequestInterface requestInterface;
    private static final String TAG = "GalleryAdapter";

    public GalleryAdapter(Context context, ArrayList<String> galleryPhotoList) {
        this.context = context;
        this.galleryPhotoList = galleryPhotoList;
    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);
        db = FirebaseFirestore.getInstance();
        master = new Master(context);
        ddLoadingProgress = DDLoadingProgress.getInstance();
        requestInterface = APIClient.getRetrofitInstance().create(RequestInterface.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(galleryPhotoList.get(position))
                .placeholder(R.drawable.dd_logo_placeholder)
                .into(holder.ivPhoto);

        holder.lvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(galleryPhotoList.get(position));
                uploadPhoto(Uri.fromFile(file));
            }
        });

    }

    @Override
    public int getItemCount() {
        return galleryPhotoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lvPhoto;
        ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lvPhoto = itemView.findViewById(R.id.lvPhoto);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);

        }
    }

    private void uploadPhoto(Uri filePath) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (filePath != null) {
            ddLoadingProgress.showProgress(context, "Uploading...", false);
            final StorageReference ref = storageReference.child("profile_photo/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String img_url = uri.toString();
                                    Call<PublicProfile> call = requestInterface.editUserPhoto(master.getId(), img_url);
                                    call.enqueue(new Callback<PublicProfile>() {
                                        @Override
                                        public void onResponse(Call<PublicProfile> call, Response<PublicProfile> response) {
                                            ddLoadingProgress.hideProgress();
                                            new Master(context).setUser(response.body().getPublicProfileResponse());
                                            ((EditProfileActivity)context).closeSheet();
                                        }

                                        @Override
                                        public void onFailure(Call<PublicProfile> call, Throwable t) {
                                            Log.e(TAG, "onFailure: "+t.getMessage() );
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) ((taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()) * 100);
                            ddLoadingProgress.updateProgress(progress);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            ddLoadingProgress.hideProgress();
                        }
                    });


        }

    }

}
