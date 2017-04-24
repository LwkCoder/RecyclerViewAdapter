package com.lwkandroid.rcvadapter.base;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;

/**
 * 滑到底部加载更多的基类View
 * [子类只需实现各状态的UI，具体实现请参照类RcvLoadMoreView]
 */
public abstract class RcvBaseLoadMoreView extends RelativeLayout
{
    /**
     * 初始状态：未触发加载时的状态
     */
    protected final int STATUS_INIT = 0x00000001;

    /**
     * 加载中的状态
     */
    protected final int STATUS_LOADING = 0x00000002;

    /**
     * 加载成功后的状态
     */
    protected final int STATUS_SUCCESS = 0x00000003;

    /**
     * 加载失败后的状态
     */
    protected final int STATUS_FAIL = 0x00000004;

    /**
     * 没有更多数据的状态
     */
    protected final int STATUS_NOMOREDATA = 0x00000005;

    protected View mLayoutContent;

    /**
     * 当前状态
     */
    protected int mCurStatus;

    /**
     * 加载更多监听
     */
    protected RcvLoadMoreListener mListener;

    public RcvBaseLoadMoreView(Context context)
    {
        super(context);
        setWillNotDraw(false);
        int layoutId = setContentViewId();
        if (layoutId == 0)
            throw new IllegalArgumentException("RcvLoadMoreBaseView: Must set content layout!");
        mLayoutContent = inflate(context, layoutId, this);
        initUI();
    }

    /**
     * 子类实现此方法指定所引用的布局
     */
    protected abstract int setContentViewId();

    /**
     * 子类实现此方法初始化UI
     */
    protected abstract void initUI();

    /**
     * 向外暴露的方法：回到初始状态
     */
    public void handleLoadInit()
    {
        changeStatus(STATUS_INIT);
    }

    /**
     * 向外暴露的方法：触发加载
     */
    public void handleLoadMoreRequest()
    {
        changeStatus(STATUS_LOADING);
    }

    /**
     * 向外暴露的方法：触发加载成功的状态
     */
    public void handleLoadSuccess()
    {
        changeStatus(STATUS_SUCCESS);
    }

    /**
     * 向外暴露的方法：触发加载失败的状态
     */
    public void handleLoadFail()
    {
        changeStatus(STATUS_FAIL);
    }

    /**
     * 向外暴露的方法：触发没有更多数据的状态
     */
    public void handleNoMoreData()
    {
        changeStatus(STATUS_NOMOREDATA);
    }

    /**
     * 改变状态
     */
    private void changeStatus(int status)
    {
        //待加载
        if (status == STATUS_INIT)
        {
            setBeforeLoadingUI();
        }
        //正在加载中
        else if (status == STATUS_LOADING)
        {
            if (mCurStatus == STATUS_NOMOREDATA || mCurStatus == STATUS_LOADING)
                return;
            setLoadingUI();
            if (mListener != null)
                mListener.onLoadMoreRequest();
            mLayoutContent.setOnClickListener(null);
        }
        //加载成功
        else if (status == STATUS_SUCCESS)
        {
            setLoadSuccessUI();
        }
        //加载失败
        else if (status == STATUS_FAIL)
        {
            setLoadFailUI();
            //点击后触发加载更多
            mLayoutContent.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    changeStatus(STATUS_LOADING);
                }
            });
        }
        //没有更多数据
        else if (status == STATUS_NOMOREDATA)
        {
            setNoMoreDataUI();
        }

        //同步标记
        mCurStatus = status;
    }

    /**
     * 初始状态的UI
     */
    protected abstract void setBeforeLoadingUI();

    /**
     * 正在加载中的UI
     */
    protected abstract void setLoadingUI();

    /**
     * 加载成功的UI
     */
    protected abstract void setLoadSuccessUI();

    /**
     * 加载失败的UI
     */
    protected abstract void setLoadFailUI();

    /**
     * 没有更多数据的UI
     */
    protected abstract void setNoMoreDataUI();

    /**
     * 添加监听
     */
    public void setOnLoadMoreListener(RcvLoadMoreListener loadMoreListener)
    {
        this.mListener = loadMoreListener;
    }
}
