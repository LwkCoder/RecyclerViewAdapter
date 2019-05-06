package com.lwkandroid.rcvadapter;

import android.content.Context;
import android.view.ViewGroup;

import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.eunm.RcvViewType;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by LWK
 * TODO 支持多种Section、多种Data类型的适配器
 * 【不可与RcvStickyLayout联用】
 */
public abstract class RcvSectionMultiLabelAdapter<S, D> extends RcvMultiAdapter<RcvSectionWrapper<S, D>>
{
    private final Set<Integer> mLabelViewTypesSet = new HashSet<>();

    public RcvSectionMultiLabelAdapter(Context context, List<RcvSectionWrapper<S, D>> datas)
    {
        super(context, datas);
        RcvBaseItemView<RcvSectionWrapper<S, D>>[] labelItemViews = createLabelItemViews();
        RcvBaseItemView<RcvSectionWrapper<S, D>>[] dataItemViews = createDataItemViews();

        //添加所有标签类型
        if (labelItemViews != null && labelItemViews.length > 0)
        {
            for (int i = 0, length = labelItemViews.length; i < length; i++)
            {
                int viewType = RcvViewType.SECTION_LABEL - i;
                addItemView(viewType, labelItemViews[i]);
                mLabelViewTypesSet.add(viewType);
            }
        }

        //添加所有数据类型
        if (dataItemViews != null && dataItemViews.length > 0)
        {
            for (RcvBaseItemView<RcvSectionWrapper<S, D>> dataItemView : dataItemViews)
            {
                addItemView(dataItemView);
            }
        }
    }

    /**
     * 判断某个ViewType是否为标签类型
     *
     * @param viewType
     * @return
     */
    public boolean isSectionLabelViewType(int viewType)
    {
        return mLabelViewTypesSet.contains(viewType);
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
        if (isSectionLabelViewType(holder.getItemViewType()))
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

    protected abstract RcvBaseItemView<RcvSectionWrapper<S, D>>[] createLabelItemViews();

    protected abstract RcvBaseItemView<RcvSectionWrapper<S, D>>[] createDataItemViews();
}
