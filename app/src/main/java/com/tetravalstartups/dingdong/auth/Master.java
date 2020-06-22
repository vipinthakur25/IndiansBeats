package com.tetravalstartups.dingdong.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Master {
    Context context;

    public Master(Context context) {
        this.context = context;
    }

    public void setUser(Profile profile){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", profile.getId());
        editor.putString("name", profile.getName());
        editor.putString("handle", profile.getHandle());
        editor.putString("photo", profile.getPhoto());
        editor.putString("bio", profile.getBio());
        editor.putString("phone", profile.getPhone());
        editor.putString("email", profile.getEmail());
        editor.putString("likes", profile.getLikes());
        editor.putString("followers", profile.getFollowers());
        editor.putString("following", profile.getFollowing());
        editor.apply();
    }

    public String getId(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("id", "");
    }

    public String getName(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("name", "");
    }

    public String getHandle(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("handle", "");
    }

    public String getPhoto(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("photo", "");
    }

    public String getBio(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("bio", "");
    }

    public String getPhone(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("phone", "");
    }

    public String getEmail(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("email", "");
    }

    public String getLikes(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("likes", "");
    }

    public String getFollowers(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("followers", "");
    }

    public String getFollowing(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return preferences.getString("following", "");
    }

    public void userLogout(){
        SharedPreferences preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
