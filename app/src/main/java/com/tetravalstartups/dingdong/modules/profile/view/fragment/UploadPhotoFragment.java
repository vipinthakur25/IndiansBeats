package com.tetravalstartups.dingdong.modules.profile.view.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tetravalstartups.dingdong.R;
import com.tetravalstartups.dingdong.modules.profile.view.adapter.GalleryAdapter;
import com.tetravalstartups.dingdong.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;

public class UploadPhotoFragment extends Fragment {

    private View view;
    private RecyclerView recyclerGallery;

    public UploadPhotoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_photo, container, false);
        initView();
        return view;
    }

    private void initView() {
        recyclerGallery = view.findViewById(R.id.recyclerGallery);
        fetchGallery();
    }

    private void fetchGallery() {
        recyclerGallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerGallery.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.GRID));
        GalleryAdapter galleryAdapter = new GalleryAdapter(getContext(), getAllShownImagesPath(getActivity()));
        galleryAdapter.notifyDataSetChanged();
        recyclerGallery.setAdapter(galleryAdapter);
    }


    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, "date_modified DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

}
