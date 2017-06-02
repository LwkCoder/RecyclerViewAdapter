package com.lwkandroid.rcvadapter.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lwkandroid.rcvadapter.RcvSectionAdapter;
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.eunm.RcvViewType;
import com.lwkandroid.rcvadapter.holder.RcvHolder;

import java.util.LinkedList;
import java.util.List;

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
    protected int mStickyHeight = -1;
    protected RcvSectionAdapter mAdapter;
    protected int mFirstStickyPosition = -1;
    protected int mCurrentIndicatePosition = -1;
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
            Log.e(TAG, "You must attach a recyclerView");
            return;
        }

        this.mRecyclerView = recyclerView;
        this.mLayoutManager = recyclerView.getLayoutManager();

        //检查适配器
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (!(adapter instanceof RcvSectionAdapter))
        {
            Log.e(TAG, "You must set the RcvSectionAdapter with RecyclerView");
            return;
        }
        this.mAdapter = (RcvSectionAdapter) recyclerView.getAdapter();
        resetParams();
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onChanged()
            {
                super.onChanged();
                resetParams();
                updateScrollState();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount)
            {
                super.onItemRangeChanged(positionStart, itemCount);
                resetParams();
                updateScrollState();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload)
            {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                resetParams();
                updateScrollState();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
                super.onItemRangeInserted(positionStart, itemCount);
                resetParams();
                updateScrollState();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount)
            {
                super.onItemRangeRemoved(positionStart, itemCount);
                resetParams();
                updateScrollState();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount)
            {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                resetParams();
                updateScrollState();
            }
        });
        //初始化悬浮布局
        mHolder = RcvHolder.get(getContext(), this, mAdapter.getSectionLayoutId());
        mHolder.getConvertView().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mLayoutClickedListener != null)
                    mLayoutClickedListener.onClicked(v);
            }
        });
        addView(mHolder.getConvertView(), 0);

        //添加滚动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                updateScrollState();
            }
        });
    }

    //更新滚动过程中的状态
    private void updateScrollState()
    {
        int firstVisiableP = -1;
        int firstCompleteVisiableP = -1;
        if (mLayoutManager instanceof LinearLayoutManager)
        {
            firstVisiableP = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            firstCompleteVisiableP = ((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager)
        {
            firstVisiableP = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            firstCompleteVisiableP = ((GridLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager)
        {
            firstVisiableP = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null)[0];
            firstCompleteVisiableP = ((StaggeredGridLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPositions(null)[0];
        }

        //需要隐藏悬浮布局的时机
        if (firstVisiableP == -1 || firstCompleteVisiableP == -1
                || mFirstStickyPosition == -1 || firstCompleteVisiableP <= mFirstStickyPosition)
        {
            setVisibility(GONE);
            mCurrentIndicatePosition = -1;
            return;
        }

        setVisibility(VISIBLE);

        //两个Section相顶效果
        if (mAdapter.getItemViewType(firstCompleteVisiableP) == RcvViewType.SECTION_LABEL)
        {
            int top = mLayoutManager.findViewByPosition(firstCompleteVisiableP).getTop();
            if (top >= 0 && top < mStickyHeight)
                setY(top - mStickyHeight);
            else
                setY(0);
        } else
        {
            setY(0);
        }

        //更新悬浮布局
        if (firstVisiableP < mAdapterItemCount && firstCompleteVisiableP < mAdapterItemCount)
        {
            if (firstVisiableP > mCurrentIndicatePosition
                    && mAdapter.getItemViewType(firstVisiableP) == RcvViewType.SECTION_LABEL)
                updateStickyLayout(firstVisiableP);

            if (firstVisiableP < mCurrentIndicatePosition
                    && mAdapter.getItemViewType(firstCompleteVisiableP) == RcvViewType.SECTION_LABEL)
                updateStickyLayout(getLastStickyPosition(mCurrentIndicatePosition));
        }
    }

    //更新悬浮布局
    private void updateStickyLayout(int position)
    {
        if (position == -1)
            return;

        RcvSectionWrapper wrapper = (RcvSectionWrapper) mAdapter.getDatas().get(position - mAdapter.getHeadCounts());
        mAdapter.onBindSectionView(mHolder, wrapper.getSection(), position);
        mCurrentIndicatePosition = position;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        if (mHolder != null)
            mStickyHeight = mHolder.getConvertView().getHeight();
    }

    //重置各参数
    private void resetParams()
    {
        mCurrentIndicatePosition = -1;
        mAdapterItemCount = mAdapter.getItemCount();

        mStickyPositionList.clear();
        if (mAdapter != null)
        {
            for (int i = 0, count = mAdapter.getDataSize(); i < count; i++)
            {
                if (((RcvSectionWrapper) mAdapter.getDatas().get(i)).isSection())
                    mStickyPositionList.add(i + mAdapter.getHeadCounts());
            }
        }

        if (mStickyPositionList.size() > 0)
            mFirstStickyPosition = mStickyPositionList.get(0);
        else
            mFirstStickyPosition = -1;
    }

    //获取某一个位置上一个Section的位置
    private int getLastStickyPosition(int startP)
    {
        if (mStickyPositionList == null || mStickyPositionList.size() == 0)
            return -1;

        int resultP = -1;
        int startIndex = mStickyPositionList.indexOf(startP);
        if (startIndex != -1 && --startIndex >= 0)
            resultP = mStickyPositionList.get(startIndex);

        return resultP;
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
