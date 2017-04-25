package com.lwkandroid.rcvadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.lwkandroid.rcvadapter.base.RcvBaseAnimation;
import com.lwkandroid.rcvadapter.base.RcvBaseItemView;
import com.lwkandroid.rcvadapter.base.RcvBaseLoadMoreView;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvItemViewLongClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;
import com.lwkandroid.rcvadapter.manager.RcvItemViewManager;
import com.lwkandroid.rcvadapter.ui.RcvDefLoadMoreView;
import com.lwkandroid.rcvadapter.utils.RcvAlphaInAnim;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:RecycleView通用多布局适配器
 */
public abstract class RcvMultiAdapter<T> extends RecyclerView.Adapter<RcvHolder>
{
    //HeadView的ViewType基础标识
    protected static final int VIEW_TYPE_HEAD = 1000000;
    //FootView的ViewType基础标识
    protected static final int VIEW_TYPE_FOOT = 2000000;
    //LoadMoreLayout的ViewType标识
    protected static final int VIEW_TYPE_LOADMORE = Integer.MAX_VALUE - 2;
    //EmptyView的ViewType标识
    protected static final int VIEW_TYPE_EMPTY = Integer.MAX_VALUE - 1;
    //上下文
    protected Context mContext;
    //数据源
    protected List<T> mDataList = new ArrayList<>();
    //子布局管理器
    protected RcvItemViewManager mItemViewManager;
    //存放头部布局的容器
    protected SparseArray<View> mHeaderViews;
    //存放底部布局的容器
    protected SparseArray<View> mFooterViews;
    //底部加载更多的布局
    protected RcvBaseLoadMoreView mLoadMoreLayout;
    //行布局点击监听
    protected RcvItemViewClickListener<T> mOnItemClickListener;
    //行布局长按监听
    protected RcvItemViewLongClickListener<T> mOnItemLongClickListener;
    //空数据占位View
    protected View mEmptyView;
    //空数据占位View的布局id
    protected int mEmptyViewId;
    //子item展示动画
    protected RcvBaseAnimation mAnimation;
    //上次子item展示动画最后的位置
    protected int mAnimLastPosition = -1;
    //EmptyView的位置
    protected int mEmptyViewPosition = -1;

    public RcvMultiAdapter(Context context, List<T> datas)
    {
        this.mContext = context;
        if (datas != null && datas.size() > 0)
            this.mDataList.addAll(datas);
        mItemViewManager = new RcvItemViewManager();
    }

    /**
     * 获取当前所有数据
     */
    public List<T> getDatas()
    {
        return mDataList;
    }

    /**
     * 添加子布局类型
     */
    public void addItemView(RcvBaseItemView<T> itemView)
    {
        mItemViewManager.addItemView(itemView);
    }

    /**
     * 添加子布局类型
     */
    public void addItemView(int viewType, RcvBaseItemView<T> itemView)
    {
        mItemViewManager.addItemView(viewType, itemView);
    }

    /**
     * 添加HeadView
     * [注意！调用该方法前请确保RecyclerView已经调用了setLayoutManager()]
     */
    public void addHeaderView(View... headerViews)
    {
        if (mHeaderViews == null)
            mHeaderViews = new SparseArray<>();
        for (View headerView : headerViews)
        {
            mHeaderViews.put(VIEW_TYPE_HEAD + getHeadCounts(), headerView);
        }

        notifyDataSetChanged();
    }

    /**
     * 删除指定位置的HeaderView
     *
     * @param index 索引位置
     */
    public void removeHeaderViewAt(int index)
    {
        if (mHeaderViews != null)
        {
            mHeaderViews.removeAt(index);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空HeaderView
     */
    public void clearHeaderViews()
    {
        if (mHeaderViews != null && mHeaderViews.size() > 0)
        {
            mHeaderViews.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 添加FooterView
     * [注意！调用该方法前请确保RecyclerView已经调用了setLayoutManager()]
     */
    public void addFooterView(View... footViews)
    {
        if (mFooterViews == null)
            mFooterViews = new SparseArray<>();
        for (View footView : footViews)
        {
            mFooterViews.put(VIEW_TYPE_FOOT + getFootCounts(), footView);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除指定位置的FooterView
     *
     * @param index 索引位置
     */
    public void removeFooterViewAt(int index)
    {
        if (mFooterViews != null)
        {
            mFooterViews.removeAt(index);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空FooterView
     */
    public void clearFooterViews()
    {
        if (mFooterViews != null && mFooterViews.size() > 0)
        {
            mFooterViews.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 是否开启加载更多功能【使用默认布局】
     *
     * @param enable   是否开启该功能
     * @param listener 加载更多监听
     */
    public void enableLoadMore(boolean enable, RcvLoadMoreListener listener)
    {
        enableLoadMore(enable, new RcvDefLoadMoreView(mContext), listener);
    }

    /**
     * 是否开启加载更多功能
     *
     * @param enable   是否开启该功能
     * @param layout   自定义加载更多布局
     * @param listener 加载更多监听
     */
    public void enableLoadMore(boolean enable, RcvBaseLoadMoreView layout, RcvLoadMoreListener listener)
    {
        if (enable)
        {
            this.mLoadMoreLayout = layout;
            mLoadMoreLayout.setOnLoadMoreListener(listener);
        } else
        {
            this.mLoadMoreLayout = null;
        }
        notifyDataSetChanged();
    }

    /**
     * 是否开启加载更多功能
     */
    public boolean isLoadMoreEnable()
    {
        return mLoadMoreLayout != null ? true : false;
    }

    /**
     * 设置空数据占位VIew
     */
    public void setEmptyView(View emptyView)
    {
        this.mEmptyView = emptyView;
    }

    /**
     * 设置空数据占位View的id
     */
    public void setEmptyView(int layoutId)
    {
        this.mEmptyViewId = layoutId;
    }

    /**
     * 开启子item展示动画
     * [默认为AlphaIn动画]
     *
     * @param enable 是否开启该功能
     */
    public void enableItemShowingAnim(boolean enable)
    {
        enableItemShowingAnim(enable, new RcvAlphaInAnim());
    }

    /**
     * 开启子item展示动画
     * [注意！当有HeadView或LoadMore的情况时，自定义动画可能会有问题!!!]
     *
     * @param enable    是否开启该功能
     * @param animation 自定义动画
     */
    public void enableItemShowingAnim(boolean enable, RcvBaseAnimation animation)
    {
        if (enable)
            this.mAnimation = animation;
        else
            this.mAnimation = null;
    }

    //子item展示动画是否开启
    protected boolean isItemShowingAnimEnable()
    {
        return mAnimation != null ? true : false;
    }

    @Override
    public RcvHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //ViewType大小数序：VIEW_TYPE_EMPTY > VIEW_TYPE_LOADMORE > VIEW_TYPE_FOOT > VIEW_TYPE_HEAD
        if (viewType == VIEW_TYPE_EMPTY)
        {
            if (mEmptyView != null)
                return RcvHolder.get(mContext, mEmptyView);
            else
                return RcvHolder.get(mContext, parent, mEmptyViewId);
        } else if (viewType == VIEW_TYPE_LOADMORE && isLoadMoreEnable())
        {
            return RcvHolder.get(mContext, mLoadMoreLayout);
        } else if (viewType >= VIEW_TYPE_FOOT && mFooterViews.get(viewType) != null)
        {
            return RcvHolder.get(mContext, mFooterViews.get(viewType));
        } else if (viewType >= VIEW_TYPE_HEAD && mHeaderViews.get(viewType) != null)
        {
            return RcvHolder.get(mContext, mHeaderViews.get(viewType));
        } else
        {
            int layoutId = mItemViewManager.getItemViewLayoutId(viewType);
            RcvHolder holder = RcvHolder.get(mContext, parent, layoutId);
            setListener(parent, holder, viewType);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RcvHolder holder, int position)
    {
        if (isInHeadViewPos(position) || isInFootViewPos(position) || isInEmptyStatus())
        {
            return;
        } else if (isInLoadMorePos(position))
        {
            mLoadMoreLayout.handleLoadMoreRequest();
            return;
        } else
        {
            //设置数据
            mItemViewManager.setData(holder, mDataList.get(position - getHeadCounts()), holder.getLayoutPosition());
            //设置动画
            startItemAnim(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isInHeadViewPos(position))
        {
            return mHeaderViews.keyAt(position);
        } else if (isInEmptyStatus() && (mEmptyViewPosition == -1 || position == mEmptyViewPosition))
        {
            mEmptyViewPosition = position;
            return VIEW_TYPE_EMPTY;
        } else if (isInFootViewPos(position))
        {
            return mFooterViews.keyAt(position - getDataSize() - getHeadCounts() - getEmptyViewCounts());
        } else if (isInLoadMorePos(position))
        {
            return VIEW_TYPE_LOADMORE;
        } else if (useItemViewManager())
        {
            return mItemViewManager.getItemViewType(mDataList.get(position - getHeadCounts()), position);
        } else
            return super.getItemViewType(position);
    }

    //启动子item展示动画
    protected void startItemAnim(RcvHolder holder, int position)
    {
        if (isItemShowingAnimEnable() && position > mAnimLastPosition)
        {
            mAnimLastPosition = position;
            mAnimation.startAnim(holder.itemView);
        }
    }

    /**
     * 是否启用子布局管理器
     * 【会根据其内子布局个数自动判断】
     */
    protected boolean useItemViewManager()
    {
        return mItemViewManager.getItemViewCount() > 0 && getDataSize() > 0;
    }

    /**
     * 获取数据数量
     */
    protected int getDataSize()
    {
        return mDataList.size();
    }

    /**
     * 获取HeadView的数量
     */
    protected int getHeadCounts()
    {
        return mHeaderViews != null ? mHeaderViews.size() : 0;
    }

    /**
     * 获取FootView的数量
     */
    protected int getFootCounts()
    {
        return mFooterViews != null ? mFooterViews.size() : 0;
    }

    /**
     * 获取加载更多的数量
     */
    protected int getLoadMoreCounts()
    {
        return isLoadMoreEnable() ? 1 : 0;
    }

    /**
     * 获取空占位View数量
     */
    protected int getEmptyViewCounts()
    {
        return isInEmptyStatus() ? 1 : 0;
    }

    /**
     * 某个位置是否处于HeadView的位置内
     */
    protected boolean isInHeadViewPos(int p)
    {
        return p < getHeadCounts();
    }

    /**
     * 某个位置是否处于FootView的位置内
     */
    protected boolean isInFootViewPos(int p)
    {
        return p >= getDataSize() + getHeadCounts() + getEmptyViewCounts() &&
                p < getDataSize() + getHeadCounts() + getEmptyViewCounts() + getFootCounts();
    }

    /**
     * 某个位置是否处于LoadMore的位置内
     */
    protected boolean isInLoadMorePos(int p)
    {
        return isLoadMoreEnable() &&
                p == getDataSize() + getHeadCounts() + getEmptyViewCounts() + getFootCounts();
    }

    /**
     * 判断当前是否符合空数据状态
     */
    protected boolean isInEmptyStatus()
    {
        return (mEmptyView != null || mEmptyViewId != 0) && getDataSize() == 0;
    }

    /**
     * 设置行布局点击监听【单击和长按】
     */
    protected void setListener(final ViewGroup parent, final RcvHolder viewHolder, final int viewType)
    {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mOnItemClickListener != null)
                {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemViewClicked(viewType, v, viewHolder,
                            mDataList.get(position - getHeadCounts()), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (mOnItemLongClickListener != null)
                {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemViewLongClicked(viewType, v, viewHolder,
                            mDataList.get(position - getHeadCounts()), position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        //这里获取的是适配器中子元素的数量，不是数据的数量
        return getDataSize()
                + getEmptyViewCounts()
                + getHeadCounts()
                + getFootCounts()
                + getLoadMoreCounts();
    }

    /**
     * 添加HeadView或FootView或LoadMore或EmptyView
     * 兼容GridLayoutMananger的方法
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
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

    /**
     * 添加HeadView或FootView或LoadMore或EmptyView后
     * 兼容StaggeredGridLayoutManager的方法
     */
    @Override
    public void onViewAttachedToWindow(RcvHolder holder)
    {
        super.onViewAttachedToWindow(holder);
        if (isInHeadViewPos(holder.getLayoutPosition())
                || isInFootViewPos(holder.getLayoutPosition())
                || isInLoadMorePos(holder.getLayoutPosition())
                || isInEmptyStatus())
        {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams)
            {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * 刷新数据的方法
     */
    public void refreshDatas(List<T> data)
    {
        mDataList.clear();
        mAnimLastPosition = -1;

        if (data != null && data.size() > 0)
            mDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 增加一条数据
     *
     * @return 成功则返回添加的位置，失败返回-1
     */
    public int addData(T t)
    {
        return addData(getHeadCounts() + getDataSize(), t);
    }

    /**
     * 添加一条数据
     *
     * @param position 添加的位置，需要考虑HeadView的数量
     * @param t        数据
     * @return 成功则返回添加的位置，失败返回-1
     */
    public int addData(int position, T t)
    {
        if (t != null)
        {
            mDataList.add(position - getHeadCounts(), t);
            notifyItemInserted(position);
            return position;
        }
        return -1;
    }

    /**
     * 添加若干条数据
     *
     * @param data 数据list
     */
    public void addDatas(List<T> data)
    {
        if (data != null && data.size() > 0)
        {
            int posStart = getHeadCounts() + getDataSize();
            mDataList.addAll(data);
            notifyItemRangeInserted(posStart, data.size());
        }
    }

    /**
     * 删除一条数据
     *
     * @param position 位置，需要考虑HeadView的数量
     * @return 成功返回位置，失败返回-1
     */
    public int deleteData(int position)
    {
        if (mDataList != null)
        {
            mDataList.remove(position - getHeadCounts());
            notifyItemRemoved(position);
            return position;
        }
        return -1;
    }

    /**
     * 删除一条数据，但不主动调用notifyDataSetChanged()
     *
     * @param t 数据
     * @return 成功返回位置，失败返回-1
     */
    public int deleteData(T t)
    {
        if (mDataList != null)
        {
            int p = mDataList.indexOf(t);
            if (mDataList.remove(t))
            {
                int position = p + getHeadCounts();
                notifyItemRemoved(position);
                return position;
            }
        }
        return -1;
    }

    /**
     * 删除若干条数据
     *
     * @param data 数据
     */
    public void deleteDatas(List<T> data)
    {
        if (data != null && data.size() > 0 && mDataList != null)
        {
            mDataList.removeAll(data);
            notifyDataSetChanged();
        }
    }

    /**********************
     * 点击事件
     ****************************************************/
    public void setOnItemClickListener(RcvItemViewClickListener<T> l)
    {
        this.mOnItemClickListener = l;
    }

    public void setOnItemLongClickListener(RcvItemViewLongClickListener<T> l)
    {
        this.mOnItemLongClickListener = l;
    }

    /**
     * 通知加载更多成功
     *
     * @param newDataList 新增加的数据
     * @param hasMoreData 是否还有更多数据
     */
    public void notifyLoadMoreSuccess(final List<T> newDataList, final boolean hasMoreData)
    {
        if (isLoadMoreEnable())
        {
            mLoadMoreLayout.handleLoadSuccess();
            //延迟刷新UI,让用户看见加载结果
            mLoadMoreLayout.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    //添加数据
                    if (newDataList != null && newDataList.size() > 0)
                    {
                        int posStart = getHeadCounts() + getEmptyViewCounts() + getDataSize();
                        mDataList.addAll(newDataList);
                        notifyItemRangeInserted(posStart, newDataList.size());
                    }
                    //刷新UI
                    if (hasMoreData)
                        mLoadMoreLayout.handleLoadInit();
                    else
                        mLoadMoreLayout.handleNoMoreData();
                }
            }, 500);
        } else
            throw new IllegalArgumentException("RcvMultiAdapter: Must enableLoadMore()");
    }

    /**
     * 通知加载更多失败
     */
    public void notifyLoadMoreFail()
    {
        if (isLoadMoreEnable())
            mLoadMoreLayout.handleLoadFail();
        else
            throw new IllegalArgumentException("RcvMultiAdapter: Must enableLoadMore()");
    }

    /**
     * 通知加载更多没有更多数据
     */
    public void notifyLoadMoreHasNoMoreData()
    {
        if (isLoadMoreEnable())
            mLoadMoreLayout.handleNoMoreData();
        else
            throw new IllegalArgumentException("RcvMultiAdapter: Must enableLoadMore()");
    }
}
