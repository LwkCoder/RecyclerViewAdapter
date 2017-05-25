package com.lwkandroid.rcvadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.eunm.RcvViewType;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * Created by LWK
 * TODO 带Section的适配器基类
 * 2017/4/26
 */

public abstract class RcvSectionAdapter<S, D> extends RcvMultiAdapter<RcvSectionWrapper<S, D>>
{
    protected RcvSectionLabelItemView mSectionItemView;
    protected RcvSectionDataItemView mDataItemView;

    public RcvSectionAdapter(Context context, List<RcvSectionWrapper<S, D>> datas)
    {
        super(context, datas);
        addItemView(RcvViewType.SECTION_LABEL, mSectionItemView = new RcvSectionLabelItemView());
        addItemView(mDataItemView = new RcvSectionDataItemView());
    }

    protected class RcvSectionLabelItemView extends RcvBaseItemView<RcvSectionWrapper<S, D>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return getSectionLayoutId();
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<S, D> item, int position)
        {
            return item.isSection();
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<S, D> wrapper, int position)
        {
            onBindSectionView(holder, wrapper.getSection(), position);
        }
    }

    protected class RcvSectionDataItemView extends RcvBaseItemView<RcvSectionWrapper<S, D>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return getDataLayoutId();
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<S, D> item, int position)
        {
            return !item.isSection();
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<S, D> wrapper, int position)
        {
            onBindDataView(holder, wrapper.getData(), position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager)
        {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    if (isInHeadViewPos(position)
                            || isInSectionLabelPos(position)
                            || isInFootViewPos(position)
                            || isInLoadMorePos(position)
                            || isInEmptyStatus())
                        return gridManager.getSpanCount();
                    else
                        return 1;
                }
            });
            gridManager.setSpanCount(gridManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(RcvHolder holder)
    {
        if (holder.getItemViewType() == RcvViewType.SECTION_LABEL)
        {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams)
            {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
        super.onViewAttachedToWindow(holder);
    }

    //判断某位置是否为SectionLabel
    private boolean isInSectionLabelPos(int position)
    {
        int dataP = position - getHeadCounts();
        return mDataList.size() > 0 && dataP < mDataList.size() && mDataList.get(dataP).isSection();
    }

    /************************************ 子类需要实现的抽象方法 ****************************************************************/

    /**
     * 设置Section的布局id
     */
    public abstract int getSectionLayoutId();

    /**
     * 绑定Section 数据和UI
     */
    public abstract void onBindSectionView(RcvHolder holder, S section, int position);

    /**
     * 设置Item的布局id
     */
    public abstract int getDataLayoutId();

    /**
     * 绑定Item的数据和UI
     */
    public abstract void onBindDataView(RcvHolder holder, D data, int position);
}
