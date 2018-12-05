package com.lwksample.rcvadapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.lwkandroid.rcvadapter.RcvSectionSingleLabelAdapter;
import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * Created by LWK
 * TODO
 * 2017/4/27
 */

public class TestSectionSingleLabelAdapter extends RcvSectionSingleLabelAdapter<TestSection, TestData>
{
    public TestSectionSingleLabelAdapter(Context context, List<RcvSectionWrapper<TestSection, TestData>> datas)
    {
        super(context, datas);
    }

    @Override
    protected RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>[] createDataItemViews()
    {
        return new RcvBaseItemView[]{new DataItemView01(), new DataItemView02()};
    }

    @Override
    public int getSectionLabelLayoutId()
    {
        return R.layout.layout_section_label;
    }

    @Override
    public void onBindSectionLabelView(RcvHolder holder, TestSection section, int position)
    {
        holder.setTvText(R.id.tv_section_label, section.getSection());
    }

    //第一种Data ItemView
    private class DataItemView01 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.adapter_item_long;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return !item.isSection() && item.getData().getType() == 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            TextView textView = holder.findView(R.id.tv_item_long);
            textView.setBackgroundColor(Color.GREEN);
            textView.setText("第一种数据类型：" + wrapper.getData().getContent());
        }
    }

    //第二种Data ItemView
    private class DataItemView02 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.adapter_item_short;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return !item.isSection() && item.getData().getType() != 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            TextView textView = holder.findView(R.id.tv_item_short);
            textView.setBackgroundColor(Color.RED);
            textView.setText("第二种数据类型：" + wrapper.getData().getContent());
        }
    }

}
