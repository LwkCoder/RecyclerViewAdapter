package com.lwkandroid.rcvadapter.ui;

import android.content.Context;
import android.graphics.Color;
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
    private Options mOptions;

    public RcvDefLoadMoreView(Context context)
    {
        this(context, new Options());
    }

    public RcvDefLoadMoreView(Context context, Options options)
    {
        super(context);
        this.mOptions = options;
        refreshUI();
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

    protected void refreshUI()
    {
        setBackgroundColor(mOptions != null ? mOptions.getBgColor() : Color.WHITE);
        mPgbLoading.setIndeterminateDrawable(getContext().getResources()
                .getDrawable(mOptions != null ?
                        mOptions.getProgressDrawableResId() : R.drawable.rcvadapter_progressbar_circle));
        mTvStatus.setTextColor(mOptions != null ? mOptions.getTextColor() : Color.BLACK);
        setBeforeLoadingUI();
    }

    @Override
    public void setBeforeLoadingUI()
    {
        mFlStatus.setVisibility(GONE);
        mTvStatus.setText(mOptions != null ?
                mOptions.getInitMessageResId() : R.string.rcv_loadmore_init);
    }

    @Override
    public void setLoadingUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mPgbLoading.setVisibility(VISIBLE);
        mImgStatus.setVisibility(GONE);
        mTvStatus.setText(mOptions != null ?
                mOptions.getLoadingMessageResId() : R.string.rcv_loadmore_loading);
    }

    @Override
    public void setLoadSuccessUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mPgbLoading.setVisibility(GONE);
        mImgStatus.setVisibility(VISIBLE);
        mImgStatus.setImageResource(mOptions != null ?
                mOptions.getSuccessDrawableResId() : R.drawable.loadmore_success);
        mTvStatus.setText(mOptions != null ?
                mOptions.getSuccessMessageResId() : R.string.rcv_loadmore_success);
    }

    @Override
    public void setLoadFailUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mPgbLoading.setVisibility(GONE);
        mImgStatus.setVisibility(VISIBLE);
        mImgStatus.setImageResource(mOptions != null ?
                mOptions.getFailDrawableResId() : R.drawable.loadmore_fail);
        mTvStatus.setText(mOptions != null ?
                mOptions.getFailMessageResId() : R.string.rcv_loadmore_fail);
    }

    @Override
    public void setNoMoreDataUI()
    {
        mFlStatus.setVisibility(GONE);
        mTvStatus.setText(mOptions != null ?
                mOptions.getNoMoreDataMessageResId() : R.string.rcv_loadmore_nomoredata);
    }

    public static final class Options
    {
        //背景色
        private int bgColor = Color.WHITE;
        //文字颜色
        private int textColor = Color.BLACK;
        //ProgressBar的圆圈
        private int progressDrawableResId = R.drawable.rcvadapter_progressbar_circle;
        //成功加载的图片
        private int successDrawableResId = R.drawable.loadmore_success;
        //失败加载的图片
        private int failDrawableResId = R.drawable.loadmore_fail;
        //未触发状态的文字
        private int initMessageResId = R.string.rcv_loadmore_init;
        //加载中的文字
        private int loadingMessageResId = R.string.rcv_loadmore_loading;
        //加载失败的文字
        private int failMessageResId = R.string.rcv_loadmore_fail;
        //加载成功的文字
        private int successMessageResId = R.string.rcv_loadmore_success;
        //没有更多数据的文字
        private int noMoreDataMessageResId = R.string.rcv_loadmore_nomoredata;

        public int getBgColor()
        {
            return bgColor;
        }

        public void setBgColor(int bgColor)
        {
            this.bgColor = bgColor;
        }

        public int getTextColor()
        {
            return textColor;
        }

        public void setTextColor(int textColor)
        {
            this.textColor = textColor;
        }

        public int getProgressDrawableResId()
        {
            return progressDrawableResId;
        }

        public void setProgressDrawableResId(int progressDrawableResId)
        {
            this.progressDrawableResId = progressDrawableResId;
        }

        public int getSuccessDrawableResId()
        {
            return successDrawableResId;
        }

        public void setSuccessDrawableResId(int successDrawableResId)
        {
            this.successDrawableResId = successDrawableResId;
        }

        public int getFailDrawableResId()
        {
            return failDrawableResId;
        }

        public void setFailDrawableResId(int failDrawableResId)
        {
            this.failDrawableResId = failDrawableResId;
        }

        public int getInitMessageResId()
        {
            return initMessageResId;
        }

        public void setInitMessageResId(int initMessageResId)
        {
            this.initMessageResId = initMessageResId;
        }

        public int getLoadingMessageResId()
        {
            return loadingMessageResId;
        }

        public void setLoadingMessageResId(int loadingMessageResId)
        {
            this.loadingMessageResId = loadingMessageResId;
        }

        public int getFailMessageResId()
        {
            return failMessageResId;
        }

        public void setFailMessageResId(int failMessageResId)
        {
            this.failMessageResId = failMessageResId;
        }

        public int getSuccessMessageResId()
        {
            return successMessageResId;
        }

        public void setSuccessMessageResId(int successMessageResId)
        {
            this.successMessageResId = successMessageResId;
        }

        public int getNoMoreDataMessageResId()
        {
            return noMoreDataMessageResId;
        }

        public void setNoMoreDataMessageResId(int noMoreDataMessageResId)
        {
            this.noMoreDataMessageResId = noMoreDataMessageResId;
        }
    }

    public static final class Builder
    {
        private RcvDefLoadMoreView loadMoreView;
        private Options options;

        public Builder()
        {
            options = new Options();
        }

        public Builder setBgColor(int bgColor)
        {
            options.setBgColor(bgColor);
            return this;
        }

        public Builder setTextColor(int textColor)
        {
            options.setTextColor(textColor);
            return this;
        }

        public Builder setProgressDrawableResId(int resId)
        {
            options.setProgressDrawableResId(resId);
            return this;
        }

        public Builder setSuccessDrawableResId(int resId)
        {
            options.setSuccessDrawableResId(resId);
            return this;
        }

        public Builder setFailDrawableResId(int resId)
        {
            options.setFailDrawableResId(resId);
            return this;
        }

        public Builder setInitMessageResId(int resId)
        {
            options.setInitMessageResId(resId);
            return this;
        }

        public Builder setLoadingMessageResId(int resId)
        {
            options.setLoadingMessageResId(resId);
            return this;
        }

        public Builder setFailMessageResId(int resId)
        {
            options.setFailMessageResId(resId);
            return this;
        }

        public Builder setSuccessMessageResId(int resId)
        {
            options.setSuccessMessageResId(resId);
            return this;
        }

        public Builder setNoMoreDataMessageResId(int resId)
        {
            options.setNoMoreDataMessageResId(resId);
            return this;
        }

        public RcvDefLoadMoreView build(Context context)
        {
            return new RcvDefLoadMoreView(context, options);
        }
    }
}
