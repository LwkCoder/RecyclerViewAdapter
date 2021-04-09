package com.lwkandroid.rcvadapter.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lwkandroid.rcvadapter.R;
import com.lwkandroid.rcvadapter.utils.RcvUtils;

import androidx.annotation.NonNull;


/**
 * 用于显示 Loading 的View，支持颜色和大小的设置。
 */
public class RcvLoadingView extends View implements ValueAnimator.AnimatorUpdateListener
{
    private final int ANIM_DURATION = 800;
    private int mSize;
    private int mPaintColor;
    private int mAnimateValue = 0;
    private ValueAnimator mAnimator;
    private Paint mPaint;
    private static final int LINE_COUNT = 12;
    private static final int DEGREE_PER_LINE = 360 / LINE_COUNT;

    public RcvLoadingView(Context context)
    {
        this(context, null);
    }

    public RcvLoadingView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RcvLoadingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.RcvLoadingView, defStyleAttr, 0);
        mSize = array.getDimensionPixelSize(R.styleable.RcvLoadingView_loading_view_size,
                RcvUtils.dp2px(getContext(), 32));
        mPaintColor = array.getInt(R.styleable.RcvLoadingView_android_color, Color.WHITE);
        array.recycle();
        initPaint();
    }

    public RcvLoadingView(Context context, int size, int color)
    {
        super(context);
        mSize = size;
        mPaintColor = color;
        initPaint();
    }

    private void initPaint()
    {
        mPaint = new Paint();
        mPaint.setColor(mPaintColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setColor(int color)
    {
        mPaintColor = color;
        mPaint.setColor(color);
        invalidate();
    }

    public void setSize(int size)
    {
        mSize = size;
        requestLayout();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation)
    {
        mAnimateValue = (int) animation.getAnimatedValue();
        invalidate();
    }

    public void start()
    {
        if (mAnimator == null)
        {
            mAnimator = ValueAnimator.ofInt(0, LINE_COUNT - 1);
            mAnimator.addUpdateListener(this);
            mAnimator.setDuration(ANIM_DURATION);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();
        } else if (!mAnimator.isStarted())
        {
            mAnimator.start();
        }
    }

    public void stop()
    {
        if (mAnimator != null)
        {
            mAnimator.removeUpdateListener(this);
            mAnimator.removeAllUpdateListeners();
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    private void drawLoading(Canvas canvas, int rotateDegrees)
    {
        int width = mSize / 12, height = mSize / 6;
        mPaint.setStrokeWidth(width);

        canvas.rotate(rotateDegrees, mSize / 2, mSize / 2);
        canvas.translate(mSize / 2, mSize / 2);

        for (int i = 0; i < LINE_COUNT; i++)
        {
            canvas.rotate(DEGREE_PER_LINE);
            mPaint.setAlpha((int) (255f * (i + 1) / LINE_COUNT));
            canvas.translate(0, -mSize / 2 + width / 2);
            canvas.drawLine(0, 0, 0, height, mPaint);
            canvas.translate(0, mSize / 2 - width / 2);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int saveCount = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        } else
        {
            saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        }
        drawLoading(canvas, mAnimateValue * DEGREE_PER_LINE);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE)
        {
            start();
        } else
        {
            stop();
        }
    }
}
