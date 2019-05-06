package com.lwkandroid.rcvadapter.utils;

import android.util.Log;

import com.lwkandroid.rcvadapter.BuildConfig;

/**
 * Created by LWK
 * TODO 公共工具
 */
public class RcvUtils
{
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void doErrorLog(String tag, String log)
    {
        if (DEBUG)
        {
            Log.e(tag, log);
        }
    }
}
