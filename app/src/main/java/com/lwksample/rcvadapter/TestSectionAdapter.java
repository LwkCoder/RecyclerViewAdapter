package com.lwksample.rcvadapter;

import android.content.Context;

import com.lwkandroid.rcvadapter.RcvSectionAdapter;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * Created by LWK
 * TODO
 * 2017/4/27
 */

public class TestSectionAdapter extends RcvSectionAdapter<TestSection,TestData>
{
    public TestSectionAdapter(Context context, List<RcvSectionWrapper<TestSection,TestData>> datas)
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
        holder.setTvText(R.id.tv_section_label,section.getSection());
    }

    @Override
    public int getDataLayoutId()
    {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onBindDataView(RcvHolder holder, TestData data, int position)
    {
        holder.setTvText(android.R.id.text1, data.getContent());
    }
}
