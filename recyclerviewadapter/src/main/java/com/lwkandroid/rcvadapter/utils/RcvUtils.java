package com.lwkandroid.rcvadapter.utils;

import android.content.Context;
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


    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
