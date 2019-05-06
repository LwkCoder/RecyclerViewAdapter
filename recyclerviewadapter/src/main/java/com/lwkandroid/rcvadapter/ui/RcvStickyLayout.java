package com.lwkandroid.rcvadapter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lwkandroid.rcvadapter.RcvSectionSingleLabelAdapter;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.utils.RcvUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by LWK
 * TODO RecyclerView悬浮布局
 * 2017/5/31
 */

public class RcvStickyLayout extends FrameLayout
{
    protected static final String TAG = "RcvStickyLayout";
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RcvHolder mHolder;
    protected int mStickyHeight = RecyclerView.NO_POSITION;
    protected RcvSectionSingleLabelAdapter mAdapter;
    protected int mFirstStickyPosition = RecyclerView.NO_POSITION;
    protected int mCurrentIndicatePosition = RecyclerView.NO_POSITION;
    protected List<Integer> mStickyPositionList = new LinkedList<>();
    protected int mAdapterItemCount;
    protected OnStickyLayoutClickedListener mLayoutClickedListener;

    public RcvStickyLayout(@NonNull Context context)
    {
        super(context);
        init(context, null);
    }

    public RcvStickyLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 关联RecyclerView
     */
    public void attachToRecyclerView(RecyclerView recyclerView)
    {
        if (recyclerView == null)
        {
            RcvUtils.doErrorLog(TAG, "You must attach a recyclerView");
            return;
        }

        this.mRecyclerView = recyclerView;
        this.mLayoutManager = recyclerView.getLayoutManager();

        //检查适配器
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (!(adapter instanceof RcvSectionSingleLabelAdapter))
        {
            RcvUtils.doErrorLog(TAG, "You must set the RcvSectionSingleLabelAdapter with RecyclerView");
            return;
        }
        this.mAdapter = (RcvSectionSingleLabelAdapter) recyclerView.getAdapter();
        resetParams();
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onChanged()
            {
                super.onChanged();
                resetParams();
                updateScrollState(mRecyclerView.getScrollState());
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount)
            {
                super.onItemRangeChanged(positionStart, itemCount);
                resetParams();
                updateScrollState(mRecyclerView.getScrollState());
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload)
            {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                resetParams();
                updateScrollState(mRecyclerView.getScrollState());
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
                super.onItemRangeInserted(positionStart, itemCount);
                resetParams();
                updateScrollState(mRecyclerView.getScrollState());
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount)
            {
                super.onItemRangeRemoved(positionStart, itemCount);
                resetParams();
                updateScrollState(mRecyclerView.getScrollState());
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount)
            {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                resetParams();
                updateScrollState(mRecyclerView.getScrollState());
            }
        });
        //初始化悬浮布局
        mHolder = RcvHolder.get(getContext(), this, mAdapter.getSectionLabelLayoutId());
        mHolder.getConvertView().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mLayoutClickedListener != null)
                {
                    mLayoutClickedListener.onClicked(v);
                }
            }
        });
        addView(mHolder.getConvertView(), 0);

        //添加滚动监听
        mRecyclerView.addOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                updateScrollState(newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                updateScrollState(recyclerView.getScrollState());
            }
        });
    }

    //更新滚动过程中的状态
    private void updateScrollState(int scrollState)
    {
        int firstVisibleP = RecyclerView.NO_POSITION;
        int firstCompleteVisibleP = RecyclerView.NO_POSITION;
        if (mLayoutManager instanceof LinearLayoutManager)
        {
            firstVisibleP = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            firstCompleteVisibleP = ((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager)
        {
            firstVisibleP = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            firstCompleteVisibleP = ((GridLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager)
        {
            firstVisibleP = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null)[0];
            firstCompleteVisibleP = ((StaggeredGridLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPositions(null)[0];
        }

        //需要隐藏悬浮布局的时机
        if (mFirstStickyPosition == RecyclerView.NO_POSITION || firstVisibleP == RecyclerView.NO_POSITION
                || firstVisibleP < mFirstStickyPosition)
        {
            setVisibility(GONE);
            mCurrentIndicatePosition = RecyclerView.NO_POSITION;
            return;
        }
        setVisibility(VISIBLE);

        if (firstCompleteVisibleP == RecyclerView.NO_POSITION && firstVisibleP >= mFirstStickyPosition)
        {
            firstCompleteVisibleP = firstVisibleP;
        }
        //两个Section相顶效果
        if (mAdapter.isSectionLabelViewType(mAdapter.getItemViewType(firstCompleteVisibleP)))
        {
            int top = mLayoutManager.findViewByPosition(firstCompleteVisibleP).getTop();
            if (top >= 0 && top < mStickyHeight)
            {
                setY(top - mStickyHeight);
            } else
            {
                setY(0);
            }
        } else
        {
            setY(0);
        }

        if (scrollState == RecyclerView.SCROLL_STATE_IDLE || scrollState == RecyclerView.SCROLL_STATE_DRAGGING)
        {
            //停止或者手触摸拉动的情况
            updateStickyLayout(getLastSectionPosition(firstVisibleP));
        } else
        {
            //惯性滑动的情况
            if (firstVisibleP < mAdapterItemCount && firstCompleteVisibleP < mAdapterItemCount)
            {
                if (firstVisibleP > mCurrentIndicatePosition && firstVisibleP != RecyclerView.NO_POSITION
                        && mAdapter.isSectionLabelViewType(mAdapter.getItemViewType(firstVisibleP)))
                {
                    updateStickyLayout(firstVisibleP);
                } else if (firstVisibleP < mCurrentIndicatePosition && firstCompleteVisibleP != RecyclerView.NO_POSITION
                        && mAdapter.isSectionLabelViewType(mAdapter.getItemViewType(firstCompleteVisibleP)))
                {
                    updateStickyLayout(getLastStickyPosition(mCurrentIndicatePosition));
                }
            }
        }
    }

    //更新悬浮布局
    private void updateStickyLayout(int position)
    {
        if (position == RecyclerView.NO_POSITION)
        {
            return;
        }

        RcvSectionWrapper wrapper = (RcvSectionWrapper) mAdapter.getDatas().get(position - mAdapter.getHeadCounts());
        mAdapter.onBindSectionLabelView(mHolder, wrapper.getSection(), position);
        mCurrentIndicatePosition = position;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        if (mHolder != null)
        {
            mStickyHeight = mHolder.getConvertView().getHeight();
        }
    }

    //重置各参数
    private void resetParams()
    {
        mCurrentIndicatePosition = RecyclerView.NO_POSITION;
        mAdapterItemCount = mAdapter.getItemCount();

        mStickyPositionList.clear();
        if (mAdapter != null)
        {
            for (int i = 0, count = mAdapter.getDataSize(); i < count; i++)
            {
                if (((RcvSectionWrapper) mAdapter.getDatas().get(i)).isSection())
                {
                    mStickyPositionList.add(i + mAdapter.getHeadCounts());
                }
            }
        }

        if (mStickyPositionList.size() > 0)
        {
            mFirstStickyPosition = mStickyPositionList.get(0);
        } else
        {
            mFirstStickyPosition = RecyclerView.NO_POSITION;
        }
    }

    //获取某一个Section的上一个Section的位置
    private int getLastStickyPosition(int startP)
    {
        if (mStickyPositionList == null || mStickyPositionList.size() == 0)
        {
            return RecyclerView.NO_POSITION;
        }

        int resultP = RecyclerView.NO_POSITION;
        int startIndex = mStickyPositionList.indexOf(startP);
        if (startIndex != RecyclerView.NO_POSITION && --startIndex >= 0)
        {
            resultP = mStickyPositionList.get(startIndex);
        }

        return resultP;
    }

    //获取任意位置的前一个Section的位置
    private int getLastSectionPosition(int position)
    {
        List<Integer> list = new ArrayList<>();
        list.addAll(mStickyPositionList);
        Collections.reverse(list);
        for (Integer p : list)
        {
            if (p <= position)
            {
                return p;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /**
     * 获取悬浮布局当前代表的悬浮位置
     */
    public int getCurrentIndicatePosition()
    {
        return mCurrentIndicatePosition;
    }

    /**
     * 悬浮布局点击监听
     */
    public void setOnStickyLayoutClickListener(OnStickyLayoutClickedListener listener)
    {
        this.mLayoutClickedListener = listener;
    }

    /**
     * 设置悬浮布局点击监听
     */
    public interface OnStickyLayoutClickedListener
    {
        void onClicked(View stickyLayout);
    }


}
