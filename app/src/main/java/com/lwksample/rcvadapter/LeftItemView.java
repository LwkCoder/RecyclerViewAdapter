package com.lwksample.rcvadapter;

import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

public class LeftItemView extends RcvBaseItemView<TestData>
{
    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.layout_item_left;
    }

    @Override
    public boolean isForViewType(TestData item, int position)
    {
        return position % 2 == 0;
    }

    @Override
    public void onBindView(RcvHolder holder, TestData testData, int position)
    {
        holder.setTvText(R.id.tv_left, testData.getContent());
    }
}
