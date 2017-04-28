package com.lwkandroid.rcvadapter.listener;

import com.lwkandroid.rcvadapter.holder.RcvHolder;

/**
 * Created by LWK
 * TODO
 * 2017/4/27
 */

public interface RcvSectionClickListener<S>
{
    void onSectionClicked(RcvHolder holder, S section, int position);
}
