package com.lwkandroid.rcvadapter.manager;

import android.util.SparseArray;

import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.holder.RcvHolder;


/**
 * Function:RecyclerView子布局管理器
 */
public class RcvItemViewManager<T>
{
    private SparseArray<RcvBaseItemView<T>> mAllItemViews = new SparseArray();

    /**
     * 获取子布局数量
     *
     * @return 子布局数量
     */
    public int getItemViewCount()
    {
        return mAllItemViews.size();
    }

    /**
     * 添加子布局（自动指定布局对应的ViewType）
     *
     * @param itemView 子布局
     */
    public RcvItemViewManager<T> addItemView(RcvBaseItemView<T> itemView)
    {
        int viewType = mAllItemViews.size();
        if (itemView != null)
        {
            mAllItemViews.put(viewType, itemView);
            viewType++;
        }
        return this;
    }

    /**
     * 添加子布局（手动指定布局对应的ViewType）
     *
     * @param viewType 布局ViewType
     * @param itemView 子布局
     */
    public RcvItemViewManager<T> addItemView(int viewType, RcvBaseItemView<T> itemView)
    {
        if (mAllItemViews.get(viewType) != null)
        {
            throw new IllegalArgumentException(
                    "An ItemView is already registered for the viewType = " + viewType);
        }
        mAllItemViews.put(viewType, itemView);
        return this;
    }

    /**
     * 移除一种子布局
     *
     * @param itemView 子布局
     */
    public RcvItemViewManager<T> removeItemView(RcvBaseItemView<T> itemView)
    {
        if (itemView == null)
        {
            throw new NullPointerException("ItemView object is null");
        }

        int indexToRemove = mAllItemViews.indexOfValue(itemView);
        if (indexToRemove >= 0)
        {
            mAllItemViews.removeAt(indexToRemove);
        }
        return this;
    }

    /**
     * 移除一种子布局
     *
     * @param itemType 子布局对应的ViewType
     */
    public RcvItemViewManager<T> removeItemView(int itemType)
    {
        int indexToRemove = mAllItemViews.indexOfKey(itemType);

        if (indexToRemove >= 0)
        {
            mAllItemViews.removeAt(indexToRemove);
        }
        return this;
    }

    /**
     * 遍历所有子布局并关联数据
     *
     * @param holder   ViewHolder
     * @param item     子布局
     * @param position 子布局位置
     */
    public void bindView(RcvHolder holder, T item, int position)
    {
        int itemViewCounts = mAllItemViews.size();
        for (int i = 0; i < itemViewCounts; i++)
        {
            RcvBaseItemView<T> itemView = mAllItemViews.valueAt(i);

            if (itemView.isForViewType(item, position))
            {
                itemView.onBindView(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No RcvItemViewManager added that matches position=" + position + " in data source");
    }

    /**
     * 获取某一位置下子布局对应的ViewType
     *
     * @param item     子布局
     * @param position 位置
     * @return 子布局ViewType
     */
    public int getItemViewType(T item, int position)
    {
        int itemViewCounts = mAllItemViews.size();
        for (int i = 0; i < itemViewCounts; i++)
        {
            RcvBaseItemView<T> itemView = mAllItemViews.valueAt(i);
            if (itemView.isForViewType(item, position))
            {
                return mAllItemViews.keyAt(i);
            }
        }
        throw new IllegalArgumentException("No ItemView added that matches position=" + position + " in data source");
    }

    /**
     * 获取某子布局的ViewType
     *
     * @param itemView
     * @return
     */
    public int getItemViewType(RcvBaseItemView itemView)
    {
        return mAllItemViews.indexOfValue(itemView);
    }

    /**
     * 获取子布局的LayoutId
     *
     * @param viewType ViewType
     * @return 子布局LayoutId
     */
    public int getItemViewLayoutId(int viewType)
    {
        RcvBaseItemView<T> itemView = mAllItemViews.get(viewType);
        return itemView != null ? itemView.getItemViewLayoutId() : -1;
    }

    /**
     * 获取子布局的LayoutId
     *
     * @param item     子布局
     * @param position 位置
     * @return 子布局LayoutId
     */
    public int getItemViewLayoutId(T item, int position)
    {
        int itemViewCounts = mAllItemViews.size();
        for (int i = 0; i < itemViewCounts; i++)
        {
            RcvBaseItemView<T> itemView = mAllItemViews.valueAt(i);
            if (itemView.isForViewType(item, position))
            {
                return itemView.getItemViewLayoutId();
            }
        }
        throw new IllegalArgumentException("No ItemView added that matches position=" + position + " in data source");
    }
}
