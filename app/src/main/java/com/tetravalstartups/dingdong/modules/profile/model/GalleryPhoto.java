package com.tetravalstartups.dingdong.modules.profile.model;

import android.net.Uri;

public class GalleryPhoto {
    private Uri uri;

    public GalleryPhoto() {
    }

    public GalleryPhoto(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
