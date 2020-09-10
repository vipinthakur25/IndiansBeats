package com.tetravalstartups.dingdong.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.tetravalstartups.dingdong.modules.profile.external.PublicProfileResponse;

public class Master {
    Context context;

    public Master(Context context) {
        this.context = context;
    }

    public void setUser(PublicProfileResponse data) {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", data.getId());
        editor.putString("name", data.getName());
        editor.putString("handle", data.getHandle());
        editor.putString("photo", data.getPhoto());
        editor.putString("bio", data.getBio());
        editor.putString("email", data.getEmail());
        editor.putInt("likes", data.getLikes());
        editor.putInt("videos", data.getVideos());
        editor.putInt("followers", data.getFollowers());
        editor.putInt("following", data.getFollowing());
        editor.apply();
    }

    public String getId() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("id", "");
    }

    public String getName() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("name", "");
    }

    public String getHandle() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("handle", "");
    }

    public String getPhoto() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("photo", "");
    }

    public String getBio() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("bio", "");
    }

    public String getEmail() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("email", "");
    }

    public Integer getLikes() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getInt("likes", 0);
    }

    public Integer getVideos() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getInt("videos", 0);
    }

    public Integer getFollowers() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getInt("followers", 0);
    }

    public Integer getFollowing() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getInt("following", 0);
    }

    public void userLogout() {
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
