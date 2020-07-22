package com.tetravalstartups.dingdong.utils;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.logging.Handler;

public class EPlayerUtils {
    public static SimpleExoPlayer player;
    public static EPlayerUtils.Listener listener;
    private static Handler handler;

    public static void getInstance() {

    }

    public interface Listener {

    }
}


