package com.lwkandroid.rcvadapter.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lwkandroid.rcvadapter.R;
import com.lwkandroid.rcvadapter.base.RcvBaseLoadMoreView;
import com.lwkandroid.rcvadapter.utils.RcvUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
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
        if (mOptions == null)
        {
            return;
        }

        setBackgroundColor(mOptions.getBgColor());

        int size = mOptions.getIconAndLoadingSize();
        ViewGroup.LayoutParams layoutParams = mImgStatus.getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        mImgStatus.setLayoutParams(layoutParams);

        mLoadingView.setSize(size);
        mLoadingView.setColor(mOptions.getTextColor());

        mTvStatus.setTextColor(mOptions.getTextColor());
        mTvStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOptions.getTextSize());

        setBeforeLoadingUI();
    }

    @Override
    public void setBeforeLoadingUI()
    {
        mFlStatus.setVisibility(GONE);
        if (mOptions != null)
        {
            mTvStatus.setText(mOptions.getInitMessage());
        }
    }

    @Override
    public void setLoadingUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mLoadingView.setVisibility(VISIBLE);
        mImgStatus.setVisibility(GONE);
        if (mOptions != null)
        {
            mTvStatus.setText(mOptions.getLoadingMessage());
        }
    }

    @Override
    public void setLoadSuccessUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mImgStatus.setVisibility(VISIBLE);
        if (mOptions != null)
        {
            Drawable drawable = mOptions.getSuccessDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable != null)
            {
                drawable.setTint(mOptions.getTextColor());
            }
            mImgStatus.setImageDrawable(drawable);
            mTvStatus.setText(mOptions.getSuccessMessage());
        }
    }

    @Override
    public void setLoadFailUI()
    {
        mFlStatus.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mImgStatus.setVisibility(VISIBLE);
        if (mOptions != null)
        {
            Drawable drawable = mOptions.getFailDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable != null)
            {
                drawable.setTint(mOptions.getTextColor());
            }
            mImgStatus.setImageDrawable(drawable);
            mTvStatus.setText(mOptions.getFailMessage());
        }
    }

    @Override
    public void setNoMoreDataUI()
    {
        mFlStatus.setVisibility(GONE);
        if (mOptions != null)
        {
            mTvStatus.setText(mOptions.getNoMoreDataMessage());
        }
    }

    public static final class Options
    {
        private int bgColor = -1;
        private int textColor = -1;
        private Drawable successDrawable;
        private Drawable failDrawable;
        private String initMessage;
        private String loadingMessage;
        private String failMessage;
        private String successMessage;
        private String noMoreDataMessage;
        private int iconAndLoadingSize;
        private int textSize;

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

        public Drawable getSuccessDrawable()
        {
            return successDrawable;
        }

        public void setSuccessDrawable(Drawable successDrawable)
        {
            this.successDrawable = successDrawable;
        }

        public Drawable getFailDrawable()
        {
            return failDrawable;
        }

        public void setFailDrawable(Drawable failDrawable)
        {
            this.failDrawable = failDrawable;
        }

        public String getInitMessage()
        {
            return initMessage;
        }

        public void setInitMessage(String initMessage)
        {
            this.initMessage = initMessage;
        }

        public String getLoadingMessage()
        {
            return loadingMessage;
        }

        public void setLoadingMessage(String loadingMessage)
        {
            this.loadingMessage = loadingMessage;
        }

        public String getFailMessage()
        {
            return failMessage;
        }

        public void setFailMessage(String failMessage)
        {
            this.failMessage = failMessage;
        }

        public String getSuccessMessage()
        {
            return successMessage;
        }

        public void setSuccessMessage(String successMessage)
        {
            this.successMessage = successMessage;
        }

        public String getNoMoreDataMessage()
        {
            return noMoreDataMessage;
        }

        public void setNoMoreDataMessage(String noMoreDataMessage)
        {
            this.noMoreDataMessage = noMoreDataMessage;
        }

        public int getIconAndLoadingSize()
        {
            return iconAndLoadingSize;
        }

        public void setIconAndLoadingSize(int iconAndLoadingSize)
        {
            this.iconAndLoadingSize = iconAndLoadingSize;
        }

        public int getTextSize()
        {
            return textSize;
        }

        public void setTextSize(int textSize)
        {
            this.textSize = textSize;
        }
    }

    public static final class Builder
    {
        private Context context;
        private Options options;

        public Builder(Context context)
        {
            this.options = new Options();
            this.context = context;
            options.setBgColor(Color.TRANSPARENT);
            options.setFailDrawable(RcvUtils.getDrawableResources(context, R.drawable.loadmore_fail));
            options.setSuccessDrawable(RcvUtils.getDrawableResources(context, R.drawable.loadmore_success));
            options.setInitMessage(context.getResources().getString(R.string.rcv_loadmore_init));
            options.setLoadingMessage(context.getResources().getString(R.string.rcv_loadmore_loading));
            options.setFailMessage(context.getResources().getString(R.string.rcv_loadmore_fail));
            options.setSuccessMessage(context.getResources().getString(R.string.rcv_loadmore_success));
            options.setNoMoreDataMessage(context.getResources().getString(R.string.rcv_loadmore_nomoredata));
            options.setIconAndLoadingSize(context.getResources().getDimensionPixelOffset(R.dimen.rcvadapter_icon_size_loadmore));
            options.setTextSize(context.getResources().getDimensionPixelOffset(R.dimen.rcvadapter_text_size_loadmore));
        }

        public Builder setBgColorResId(@ColorRes int resId)
        {
            return setBgColor(RcvUtils.getColorResources(context, resId));
        }

        public Builder setBgColor(@ColorInt int color)
        {
            options.setBgColor(color);
            return this;
        }

        public Builder setTextColorResId(@ColorRes int resId)
        {
            return setTextColorResId(RcvUtils.getColorResources(context, resId));
        }

        public Builder setTextColor(@ColorInt int textColor)
        {
            options.setTextColor(textColor);
            return this;
        }

        public Builder setSuccessDrawableResId(@DrawableRes int resId)
        {
            return setSuccessDrawable(RcvUtils.getDrawableResources(context, resId));
        }

        public Builder setSuccessDrawable(Drawable drawable)
        {
            options.setSuccessDrawable(drawable);
            return this;
        }

        public Builder setFailDrawableResId(@DrawableRes int resId)
        {
            return setFailDrawable(RcvUtils.getDrawableResources(context, resId));
        }

        public Builder setFailDrawable(Drawable drawable)
        {
            options.setFailDrawable(drawable);
            return this;
        }

        public Builder setInitMessageResId(@StringRes int resId)
        {
            return setInitMessage(context.getString(resId));
        }

        public Builder setInitMessage(String message)
        {
            options.setInitMessage(message);
            return this;
        }

        public Builder setLoadingMessageResId(@StringRes int resId)
        {
            return setLoadingMessage(context.getString(resId));
        }

        public Builder setLoadingMessage(String message)
        {
            options.setLoadingMessage(message);
            return this;
        }

        public Builder setFailMessageResId(@StringRes int resId)
        {
            return setFailMessage(context.getString(resId));
        }

        public Builder setFailMessage(String message)
        {
            options.setFailMessage(message);
            return this;
        }

        public Builder setSuccessMessageResId(@StringRes int resId)
        {
            return setSuccessMessage(context.getString(resId));
        }

        public Builder setSuccessMessage(String message)
        {
            options.setSuccessMessage(message);
            return this;
        }

        public Builder setNoMoreDataMessageResId(@StringRes int resId)
        {
            return setNoMoreDataMessage(context.getString(resId));
        }

        public Builder setNoMoreDataMessage(String message)
        {
            options.setNoMoreDataMessage(message);
            return this;
        }

        public Builder setIconAndLoadingSizeResId(@DimenRes int resId)
        {
            return setIconAndLoadingSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelOffset(resId));
        }

        public Builder setIconAndLoadingSize(int unit, int size)
        {
            int value = (int) TypedValue.applyDimension(unit, size, context.getResources().getDisplayMetrics());
            options.setIconAndLoadingSize(value);
            return this;
        }

        public Builder setTextSizeResId(@DimenRes int resId)
        {
            return setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelOffset(resId));
        }

        public Builder setTextSize(int unit, int size)
        {
            int value = (int) TypedValue.applyDimension(unit, size, context.getResources().getDisplayMetrics());
            options.setTextSize(value);
            return this;
        }

        public RcvDefLoadMoreView build()
        {
            return new RcvDefLoadMoreView(context, options);
        }
    }
}
