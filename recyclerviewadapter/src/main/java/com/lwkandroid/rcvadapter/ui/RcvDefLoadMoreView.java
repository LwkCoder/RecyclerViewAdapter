package com.lwkandroid.rcvadapter.ui;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwkandroid.rcvadapter.R;
import com.lwkandroid.rcvadapter.base.RcvBaseLoadMoreView;

import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * 滑倒底部自动加载更多默认实现View
 *
 * @author LWK
 */
public class RcvDefLoadMoreView extends RcvBaseLoadMoreView
{
    private View mFlStatus;
    private ImageView mImgStatus;
    private TextView mTvStatus;
    private RcvLoadingView mLoadingView;
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
        mLoadingView = findViewById(R.id.loadingView);
        mImgStatus = findViewById(R.id.img_rcv_loadmore_status);
        mTvStatus = findViewById(R.id.tv_rcv_loadmore_status);
    }

    protected void refreshUI()
    {
        if (mOptions != null && mOptions.getBgColor() != -1)
        {
            setBackgroundColor(mOptions.getBgColor());
        }
        if (mOptions != null && mOptions.getIconAndLoadingSizeResId() != -1)
        {
            int size = getContext().getResources().getDimensionPixelOffset(mOptions.getIconAndLoadingSizeResId());
            ViewGroup.LayoutParams layoutParams = mImgStatus.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
            mImgStatus.setLayoutParams(layoutParams);
            mLoadingView.setSize(size);
        }
        if (mOptions != null && mOptions.getTextColor() != -1)
        {
            mTvStatus.setTextColor(mOptions.getTextColor());
            mLoadingView.setColor(mOptions.getTextColor());
        }
        if (mOptions != null && mOptions.getTextSizeResId() != -1)
        {
            mTvStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getContext().getResources().getDimensionPixelSize(mOptions.getTextSizeResId()));
        }

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
        mLoadingView.setVisibility(VISIBLE);
        mImgStatus.setVisibility(GONE);
        mTvStatus.setText(mOptions != null ?
                mOptions.getLoadingMessageResId() : R.string.rcv_loadmore_loading);
    }

    @Override
    public void setLoadSuccessUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
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
        mLoadingView.setVisibility(GONE);
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
        private int bgColor = -1;
        //文字颜色
        private int textColor = -1;
        //ProgressBar的圆圈
        //1.4.5已废弃
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
        //icon和Loading大小
        private int iconAndLoadingSizeResId = -1;
        //文字大小
        private int textSizeResId = -1;

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

        @Deprecated
        public int getProgressDrawableResId()
        {
            return progressDrawableResId;
        }

        @Deprecated
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

        public int getIconAndLoadingSizeResId()
        {
            return iconAndLoadingSizeResId;
        }

        public void setIconAndLoadingSizeResId(int iconAndLoadingSizeResId)
        {
            this.iconAndLoadingSizeResId = iconAndLoadingSizeResId;
        }

        public int getTextSizeResId()
        {
            return textSizeResId;
        }

        public void setTextSizeResId(int textSizeResId)
        {
            this.textSizeResId = textSizeResId;
        }
    }

    public static final class Builder
    {
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

        @Deprecated
        public Builder setProgressDrawableResId(@DrawableRes int resId)
        {
            options.setProgressDrawableResId(resId);
            return this;
        }

        public Builder setSuccessDrawableResId(@DrawableRes int resId)
        {
            options.setSuccessDrawableResId(resId);
            return this;
        }

        public Builder setFailDrawableResId(@DrawableRes int resId)
        {
            options.setFailDrawableResId(resId);
            return this;
        }

        public Builder setInitMessageResId(@StringRes int resId)
        {
            options.setInitMessageResId(resId);
            return this;
        }

        public Builder setLoadingMessageResId(@StringRes int resId)
        {
            options.setLoadingMessageResId(resId);
            return this;
        }

        public Builder setFailMessageResId(@StringRes int resId)
        {
            options.setFailMessageResId(resId);
            return this;
        }

        public Builder setSuccessMessageResId(@StringRes int resId)
        {
            options.setSuccessMessageResId(resId);
            return this;
        }

        public Builder setNoMoreDataMessageResId(@StringRes int resId)
        {
            options.setNoMoreDataMessageResId(resId);
            return this;
        }

        public Builder setIconAndLoadingSizeResId(@DimenRes int resId)
        {
            options.setIconAndLoadingSizeResId(resId);
            return this;
        }

        public Builder setTextSizeResId(@DimenRes int resId)
        {
            options.setTextSizeResId(resId);
            return this;
        }

        public RcvDefLoadMoreView build(Context context)
        {
            return new RcvDefLoadMoreView(context, options);
        }
    }
}
