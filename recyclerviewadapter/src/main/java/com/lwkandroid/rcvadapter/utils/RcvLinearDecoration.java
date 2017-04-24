package com.lwkandroid.rcvadapter.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Function:RecyclerView的分割线【适用于LinearLayoutMananger】
 */
public class RcvLinearDecoration extends RecyclerView.ItemDecoration
{
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    //垂直分割线样式标识
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    //水平分割线样式标识
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    //分割线drawable
    private Drawable mDivider;

    //分割线高度
    private int mDividerHeight;

    //分割线样式
    private int mOrientation;

    /**
     * 默认分割线drawable，自定义风格
     *
     * @param context     上下文
     * @param orientation HORIZONTAL_LIST或VERTICAL_LIST
     */
    public RcvLinearDecoration(Context context, int orientation)
    {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        mDividerHeight = mDivider.getIntrinsicHeight();
        setOrientation(orientation);
    }

    /**
     * 自定义分割线drawable，自定义风格
     *
     * @param context     上下文
     * @param drawable    自定义分割线drawable
     * @param orientation 自定义风格
     */
    public RcvLinearDecoration(Context context, Drawable drawable, int orientation)
    {
        mDivider = drawable;
        mDividerHeight = mDivider.getIntrinsicHeight();
        setOrientation(orientation);
    }

    //设置风格
    public void setOrientation(int orientation)
    {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)
            throw new IllegalArgumentException("RcvLinearDecoration orientation must be HORIZONTAL_LIST or VERTICAL_LIST !!!");
        mOrientation = orientation;
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent)
    {
        if (mOrientation == VERTICAL_LIST)
            drawVertical(c, parent);
        else
            drawHorizontal(c, parent);
    }

    //绘制水平风格分割线
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
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //绘制垂直风格分割线
    private void drawHorizontal(Canvas c, RecyclerView parent)
    {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST)
            outRect.set(0, 0, 0, mDividerHeight);
        else
            outRect.set(0, 0, mDividerHeight, 0);
    }
}
