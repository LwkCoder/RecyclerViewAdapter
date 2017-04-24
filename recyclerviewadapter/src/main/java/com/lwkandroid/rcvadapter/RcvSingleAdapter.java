package com.lwkandroid.rcvadapter;

import android.content.Context;

import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.List;

/**
 * Function:RecycleView通用布局适配器【所有子布局样式统一】
 */
public abstract class RcvSingleAdapter<T> extends RcvMultiAdapter<T>
{
    protected int mLayoutId;

    public RcvSingleAdapter(Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        this.mLayoutId = layoutId;
        addItemView(new RcvBaseItemView<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return mLayoutId;
            }

            @Override
            public boolean isForViewType(T item, int position)
            {
                return true;
            }

            @Override
            public void setData(RcvHolder holder, T t, int position)
            {
                RcvSingleAdapter.this.setData(holder, t, position);
            }
        });
    }

    public abstract void setData(RcvHolder holder, T itemData, int position);
}
