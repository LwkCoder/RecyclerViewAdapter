package com.lwkandroid.rcvadapter.base;


import com.lwkandroid.rcvadapter.RcvMultiAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

/**
 * RecyclerView子布局基类
 */
public abstract class RcvBaseItemView<T>
{
    private RcvMultiAdapter<T> mAdapter;

    public RcvBaseItemView()
    {
    }

    public RcvBaseItemView(RcvMultiAdapter<T> mAdapter)
    {
        this.mAdapter = mAdapter;
    }

    /**
     * 绑定适配器
     */
    public void setAdapter(RcvMultiAdapter<T> adapter)
    {
        this.mAdapter = adapter;
    }

    /**
     * 获取适配器
     */
    protected RcvMultiAdapter<T> getAdapter()
    {
        return mAdapter;
    }

    /**
     * 子类实现此方法返回对应的子布局id
     *
     * @return 子布局id
     */
    public abstract int getItemViewLayoutId();

    /**
     * 子类实现此方法决定引用该子布局的时机
     *
     * @param item     该position对应的数据
     * @param position position
     * @return 是否属于子布局
     */
    public abstract boolean isForViewType(T item, int position);

    /**
     * 绑定UI和数据的方法
     *
     * @param holder   通用ViewHolder
     * @param t        数据
     * @param position 位置
     */
    public abstract void onBindView(RcvHolder holder, T t, int position);
}
