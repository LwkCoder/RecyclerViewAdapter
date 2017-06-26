package com.lwkandroid.rcvadapter.holder;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Function:RecycleView通用ViewHolder
 */
public class RcvHolder extends RecyclerView.ViewHolder
{
    private Context mContext;
    private View mConvertView;
    private SparseArray<View> mViews;

    public RcvHolder(Context context, View itemView)
    {
        super(itemView);
        this.mContext = context;
        this.mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static RcvHolder get(Context context, View itemView)
    {
        RcvHolder holder = new RcvHolder(context, itemView);
        return holder;
    }

    public static RcvHolder get(Context context, ViewGroup parent, int layoutId)
    {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RcvHolder(context, itemView);
    }

    public Context getContext()
    {
        return mContext;
    }

    public View getConvertView()
    {
        return mConvertView;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T findView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置View是否可见
     */
    public RcvHolder setVisibility(int viewId, int visibility)
    {
        View view = findView(viewId);
        if (view != null)
            view.setVisibility(visibility);
        return this;
    }

    /**
     * 为TextView设置字符串
     */
    public RcvHolder setTvText(int viewId, CharSequence text)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            tv.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     */
    public RcvHolder setTvText(int viewId, @StringRes int resId)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            tv.setText(resId);
        return this;
    }

    /**
     * 设置textview颜色
     */
    public RcvHolder setTvColor(int viewId, @ColorInt int textColor)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            tv.setTextColor(textColor);
        return this;
    }

    /**
     * 设置textview颜色【资源id】
     */
    public RcvHolder setTvColorRes(int viewId, @ColorRes int textColorRes)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            tv.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * 设置textview支持超链接
     */
    public RcvHolder setTvLinkify(int viewId)
    {
        TextView tv = findView(viewId);
        if (tv != null)
            Linkify.addLinks(tv, Linkify.ALL);
        return this;
    }

    /**
     * 设置textview的Typeface
     */
    public RcvHolder setTvTypeface(Typeface typeface, int... viewIds)
    {
        for (int viewId : viewIds)
        {
            TextView tv = findView(viewId);
            if (tv != null)
            {
                tv.setTypeface(typeface);
                tv.setPaintFlags(tv.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        }
        return this;
    }

    /**
     * 设置button的文本
     */
    public RcvHolder setBtnText(int viewId, CharSequence text)
    {
        Button button = findView(viewId);
        if (button != null)
            button.setText(text);
        return this;
    }

    /**
     * 设置button的文本【资源id】
     */
    public RcvHolder setBtnText(int viewId, @StringRes int resId)
    {
        Button button = findView(viewId);
        if (button != null)
            button.setText(resId);
        return this;
    }

    /**
     * 为ImageView设置本地资源图片
     */
    public RcvHolder setImgResource(int viewId, @DrawableRes int resId)
    {
        ImageView img = findView(viewId);
        if (img != null)
            img.setImageResource(resId);
        return this;
    }

    /**
     * 为View设置点击事件
     */
    public RcvHolder setClickListener(int viewId, View.OnClickListener l)
    {
        View view = findView(viewId);
        if (view != null)
            view.setOnClickListener(l);
        return this;
    }

    /**
     * 设置View背景色
     */
    public RcvHolder setBackgroundColor(int viewId, @ColorInt int color)
    {
        View view = findView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置View的背景【资源id】
     */
    public RcvHolder setBackgroundRes(int viewId, @DrawableRes int bgResId)
    {
        View view = findView(viewId);
        view.setBackgroundResource(bgResId);
        return this;
    }

    /**
     * 设置View是否可选
     */
    public RcvHolder setChecked(int viewId, boolean checked)
    {
        Checkable view = (Checkable) findView(viewId);
        view.setChecked(checked);
        return this;
    }
}
