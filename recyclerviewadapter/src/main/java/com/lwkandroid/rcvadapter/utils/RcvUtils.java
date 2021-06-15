package com.lwkandroid.rcvadapter.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.lwkandroid.rcvadapter.BuildConfig;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

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

    public static Drawable getDrawableResources(Context context, @DrawableRes int id)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            return context.getResources().getDrawable(id, context.getTheme());
        } else
        {
            return context.getResources().getDrawable(id);
        }
    }

    public static int getColorResources(Context context, @ColorRes int id)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            return context.getColor(id);
        } else
        {
            return context.getResources().getColor(id);
        }
    }
}
