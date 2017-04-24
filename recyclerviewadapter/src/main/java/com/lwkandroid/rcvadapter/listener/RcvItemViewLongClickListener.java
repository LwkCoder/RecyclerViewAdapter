package com.lwkandroid.rcvadapter.listener;

import android.view.View;

import com.lwkandroid.rcvadapter.holder.RcvHolder;

/**
 * Created by LWK
 * TODO 子布局长点击事件监听
 * 2017/4/24
 */
public interface RcvItemViewLongClickListener<T>
{
    void onItemViewLongClicked(int viewType, View view, RcvHolder holder, T t, int position);
}
