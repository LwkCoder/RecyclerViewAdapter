package com.lwkandroid.rcvadapter.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lwkandroid.rcvadapter.R;
import com.lwkandroid.rcvadapter.base.RcvBaseLoadMoreView;

/**
 * 滑倒底部自动加载更多默认实现View
 */
public class RcvDefLoadMoreView extends RcvBaseLoadMoreView
{
    private View mFlStatus;
    private ProgressBar mPgbLoading;
    private ImageView mImgStatus;
    private TextView mTvStatus;

    public RcvDefLoadMoreView(Context context)
    {
        super(context);
    }

    @Override
    protected int setContentViewId()
    {
        return R.layout.layout_rcvadapter_loadmore;
    }

    @Override
    protected void initUI()
    {
        mFlStatus = findViewById(R.id.fl_rcv_loadmore_status);
        mPgbLoading = (ProgressBar) findViewById(R.id.pgb_rcv_loadmore_loading);
        mImgStatus = (ImageView) findViewById(R.id.img_rcv_loadmore_status);
        mTvStatus = (TextView) findViewById(R.id.tv_rcv_loadmore_status);
    }

    @Override
    public void setBeforeLoadingUI()
    {
        mFlStatus.setVisibility(GONE);
        mTvStatus.setText(R.string.rcv_loadmore_init);
    }

    @Override
    public void setLoadingUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mPgbLoading.setVisibility(VISIBLE);
        mImgStatus.setVisibility(GONE);
        mTvStatus.setText(R.string.rcv_loadmore_loading);
    }

    @Override
    public void setLoadSuccessUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mPgbLoading.setVisibility(GONE);
        mImgStatus.setVisibility(VISIBLE);
        mImgStatus.setImageResource(R.drawable.loadmore_success);
        mTvStatus.setText(R.string.rcv_loadmore_success);
    }

    @Override
    public void setLoadFailUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mPgbLoading.setVisibility(GONE);
        mImgStatus.setVisibility(VISIBLE);
        mImgStatus.setImageResource(R.drawable.loadmore_fail);
        mTvStatus.setText(R.string.rcv_loadmore_fail);
    }

    @Override
    public void setNoMoreDataUI()
    {
        mFlStatus.setVisibility(GONE);
        mTvStatus.setText(R.string.rcv_loadmore_nomoredata);
    }
}
