package com.lwkandroid.rcvadapter.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Function:RecyclerView的分割线【适用于LinearLayoutMananger】
 */
public class RcvLinearDecoration extends RecyclerView.ItemDecoration
{
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    //分割线drawable
    private Drawable mDividerDrawable;

    //绘制分割线的Paint
    private Paint mDividerPaint;

    //分割线大小
    private int mDividerSize;

    //分割线样式
    private int mOrientation;

    /**
     * 创建默认竖直排列的分割线
     */
    public static RcvLinearDecoration createDefaultVertical(Context context)
    {
        return new RcvLinearDecoration(context, LinearLayoutManager.VERTICAL);
    }

    /**
     * 创建自定义色值默认竖直排列的分割线
     *
     * @param color 自定义色值
     */
    public static RcvLinearDecoration createDefaultVertical(int color)
    {
        return new RcvLinearDecoration(color, LinearLayoutManager.VERTICAL);
    }

    /**
     * 创建默认水平排列的分割线
     */
    public static RcvLinearDecoration createDefaultHorizontal(Context context)
    {
        return new RcvLinearDecoration(context, LinearLayoutManager.HORIZONTAL);
    }

    /**
     * 创建自定义色值默认水平排列的分割线
     *
     * @param color 自定义色值
     */
    public static RcvLinearDecoration createDefaultHorizontal(int color)
    {
        return new RcvLinearDecoration(color, LinearLayoutManager.HORIZONTAL);
    }

    /**
     * 默认Drawable分割线
     *
     * @param context     上下文
     * @param orientation 排列方向
     */
    public RcvLinearDecoration(Context context, int orientation)
    {
        setOrientation(orientation);
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDividerDrawable = a.getDrawable(0);
        a.recycle();
        if (orientation == LinearLayoutManager.VERTICAL)
        {
            mDividerSize = mDividerDrawable.getIntrinsicHeight();
        } else
        {
            mDividerSize = mDividerDrawable.getIntrinsicWidth();
        }
    }

    /**
     * 自定义Drawable分割线
     *
     * @param context     上下文
     * @param drawable    自定义分割线drawable
     * @param orientation 排列方向
     */
    public RcvLinearDecoration(Context context, Drawable drawable, int orientation)
    {
        setOrientation(orientation);
        mDividerDrawable = drawable;
        if (orientation == LinearLayoutManager.VERTICAL)
        {
            mDividerSize = mDividerDrawable.getIntrinsicHeight();
        } else
        {
            mDividerSize = mDividerDrawable.getIntrinsicWidth();
        }
    }

    /**
     * 自定义Drawable分割线
     *
     * @param context       上下文
     * @param drawableResId 自定义分割线drawable资源id
     * @param orientation   排列方向
     */
    public RcvLinearDecoration(Context context, @DrawableRes int drawableResId, int orientation)
    {
        setOrientation(orientation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mDividerDrawable = context.getResources().getDrawable(drawableResId, null);
        } else
        {
            mDividerDrawable = context.getResources().getDrawable(drawableResId);
        }

        if (orientation == LinearLayoutManager.VERTICAL)
        {
            mDividerSize = mDividerDrawable.getIntrinsicHeight();
        } else
        {
            mDividerSize = mDividerDrawable.getIntrinsicWidth();
        }
    }

    /**
     * 自定义Color分割线（宽度或者高度默认1px）
     *
     * @param color       色值
     * @param orientation 排列方向
     */
    public RcvLinearDecoration(@ColorInt int color, int orientation)
    {
        this(color, 1, orientation);
    }

    /**
     * 自定义Color分割线
     *
     * @param color       色值
     * @param size        分割线大小【单位px】当排列方向为竖直，size为高度，排列方向为横向，size为宽度
     * @param orientation 排列方向
     */
    public RcvLinearDecoration(@ColorInt int color, int size, int orientation)
    {
        this.mDividerSize = size;
        setOrientation(orientation);
        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setColor(color);
        mDividerPaint.setStyle(Paint.Style.FILL);
    }

    //设置排列方向
    private void setOrientation(int orientation)
    {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL)
        {
            throw new IllegalArgumentException("RcvLinearDecoration orientation must be LinearLayoutManager.VERTICAL or LinearLayoutManager.HORIZONTAL !!!");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent)
    {
        if (mOrientation == LinearLayoutManager.VERTICAL)
        {
            drawVertical(c, parent);
        } else
        {
            drawHorizontal(c, parent);
        }
    }

    //绘制垂直排列分割线
    private void drawVertical(Canvas c, RecyclerView parent)
    {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerSize;
            if (mDividerDrawable != null)
            {
                mDividerDrawable.setBounds(left, top, right, bottom);
                mDividerDrawable.draw(c);
            } else if (mDividerPaint != null)
            {
                c.drawRect(left, top, right, bottom, mDividerPaint);
            }
        }
    }

    //绘制水平排列分割线
    private void drawHorizontal(Canvas c, RecyclerView parent)
    {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() + params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerSize;
            if (mDividerDrawable != null)
            {
                mDividerDrawable.setBounds(left, top, right, bottom);
                mDividerDrawable.draw(c);
            } else
            {
                c.drawRect(left, top, right, bottom, mDividerPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL)
        {
            outRect.set(0, 0, 0, mDividerSize);
        } else
        {
            outRect.set(0, 0, mDividerSize, 0);
        }
    }
}
