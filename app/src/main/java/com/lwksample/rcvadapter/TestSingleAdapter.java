package com.lwksample.rcvadapter;

import android.content.Context;

import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * Created by LWK
 * TODO 单一ViewType的适配器
 * 2017/4/25
 */
public class TestSingleAdapter extends RcvSingleAdapter<TestData>
{
    public TestSingleAdapter(Context context, List<TestData> datas)
    {
        super(context, android.R.layout.simple_list_item_1, datas);
    }

    @Override
    public void setData(RcvHolder holder, TestData itemData, int position)
    {
        holder.setTvText(android.R.id.text1, itemData.getContent());
    }
}
