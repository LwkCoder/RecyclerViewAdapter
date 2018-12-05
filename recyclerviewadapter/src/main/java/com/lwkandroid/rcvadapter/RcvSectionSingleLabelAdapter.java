package com.lwkandroid.rcvadapter;

import android.content.Context;

import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * Created by LWK
 * TODO 支持单种Section、多种Data类型的适配器
 * 【可以与RcvStickyLayout联用】
 */
public abstract class RcvSectionSingleLabelAdapter<S, D> extends RcvSectionMultiLabelAdapter<S, D>
{
    public RcvSectionSingleLabelAdapter(Context context, List<RcvSectionWrapper<S, D>> datas)
    {
        super(context, datas);
    }

    @Override
    protected RcvBaseItemView<RcvSectionWrapper<S, D>>[] createLabelItemViews()
    {
        return new RcvBaseItemView[]{new RcvSectionLabelItemView()};
    }

    private class RcvSectionLabelItemView extends RcvBaseItemView<RcvSectionWrapper<S, D>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return getSectionLabelLayoutId();
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<S, D> item, int position)
        {
            return item.isSection();
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<S, D> wrapper, int position)
        {
            onBindSectionLabelView(holder, wrapper.getSection(), position);
        }
    }


    /************************************ 子类需要实现的抽象方法 ****************************************************************/

    /**
     * 设置Section的布局id
     */
    public abstract int getSectionLabelLayoutId();

    /**
     * 绑定Section 数据和UI
     */
    public abstract void onBindSectionLabelView(RcvHolder holder, S section, int position);
}
