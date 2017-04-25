package com.lwksample.rcvadapter;

import android.content.Context;

import com.lwkandroid.rcvadapter.RcvMultiAdapter;

import java.util.List;

/**
 * Created by LWK
 * TODO 多种ViewTupe的适配器
 * 2017/4/25
 */

public class TestMultiAdapter extends RcvMultiAdapter<TestData>
{
    public TestMultiAdapter(Context context, List<TestData> datas)
    {
        super(context, datas);
        addItemView(new LeftItemView());
        addItemView(new RightItemView());
    }
}
