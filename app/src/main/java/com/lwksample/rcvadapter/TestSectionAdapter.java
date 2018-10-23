package com.lwksample.rcvadapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.lwkandroid.rcvadapter.RcvSectionAdapter;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * Created by LWK
 * TODO
 * 2017/4/27
 */

public class TestSectionAdapter extends RcvSectionAdapter<TestSection, TestData>
{
    public TestSectionAdapter(Context context, List<RcvSectionWrapper<TestSection, TestData>> datas)
    {
        super(context, datas);
    }

    @Override
    public int getSectionLayoutId()
    {
        return R.layout.layout_section_label;
    }

    @Override
    public void onBindSectionView(RcvHolder holder, TestSection section, int position)
    {
        holder.setTvText(R.id.tv_section_label, section.getSection());
    }

    @Override
    public int getDataLayoutId()
    {
        return R.layout.adapter_item_long;
    }

    @Override
    public void onBindDataView(RcvHolder holder, TestData data, int position)
    {
        TextView textView = holder.findView(R.id.tv_item_long);
        if (position % 2 == 0)
            textView.setBackgroundColor(Color.GREEN);
        else
            textView.setBackgroundColor(Color.RED);
        textView.setText(data.getContent());
    }
}
