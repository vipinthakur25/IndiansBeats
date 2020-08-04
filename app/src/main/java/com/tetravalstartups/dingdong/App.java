package com.tetravalstartups.dingdong;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cloudinary.android.MediaManager;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class App extends Application {

    public static SimpleCache simpleCache = null;
    public static LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = null;
    public static ExoDatabaseProvider exoDatabaseProvider = null;
    public static Long exoPlayerCacheSize = (long) (90 * 1024 * 1024);

    private SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
        MediaManager.init(this);

        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = new ExoDatabaseProvider(this);
        }

        if (simpleCache == null) {
            simpleCache = new SimpleCache(getCacheDir(), leastRecentlyUsedCacheEvictor, exoDatabaseProvider);
            if (simpleCache.getCacheSpace() >= 400207768) {
                freeMemory();
            }
            Log.i(TAG, "onCreate: " + simpleCache.getCacheSpace());
        }


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("plan_renew", 0);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        if (firebaseAuth.getCurrentUser() != null) {

            if (!preferences.getString("date", "").equals(formattedDate)) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .collection("subscription")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (!task.getResult().getDocuments().isEmpty()) {
                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                        if (snapshot.getString("status").equals("1")) {
                                            String total = snapshot.getString("total_uploads");
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("avl_uploads", total);



                                            db.collection("users")
                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                    .collection("subscription")
                                                    .document(snapshot.getString("id"))
                                                    .update(hashMap);

                                        } else if (formattedDate.equals(snapshot.getString("end_date"))) {
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("status", "-1");

                                            db.collection("users")
                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                    .collection("subscription")
                                                    .document(snapshot.getString("id"))
                                                    .update(hashMap);
                                        }
                                    }
                                }
                            }
                        });
            }

        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("date", formattedDate);
        editor.apply();

    }

    public void freeMemory() {

        try {
            File dir = getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}
