package com.lwksample.rcvadapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.lwkandroid.rcvadapter.RcvSectionMultiLabelAdapter;
import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * Created by LWK
 * TODO
 * 2017/4/27
 */

public class TestSectionMultiLabelAdapter extends RcvSectionMultiLabelAdapter<TestSection, TestData>
{
    public TestSectionMultiLabelAdapter(Context context, List<RcvSectionWrapper<TestSection, TestData>> datas)
    {
        super(context, datas);
    }

    @Override
    protected RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>[] createLabelItemViews()
    {
        return new RcvBaseItemView[]{new LabelItemView01(), new LabelItemView02()};
    }

    @Override
    protected RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>[] createDataItemViews()
    {
        return new RcvBaseItemView[]{new DataItemView01(), new DataItemView02()};
    }


    //第一种Label ItemView
    private class LabelItemView01 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.layout_section_label;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return item.isSection() && item.getSection().getType() == 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            holder.setTvText(R.id.tv_section_label, wrapper.getSection().getSection());
        }
    }

    //第二种Label ItemView
    private class LabelItemView02 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.layout_section_label02;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return item.isSection() && item.getSection().getType() != 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            holder.setTvText(R.id.tv_section_label, wrapper.getSection().getSection());
        }
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
