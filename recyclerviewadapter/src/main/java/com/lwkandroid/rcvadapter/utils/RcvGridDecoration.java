package com.lwkandroid.rcvadapter.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Function:RecyclerView的分割线【适用于GridLayoutMananger和StaggeredGridLayoutManager】
 */
public class RcvGridDecoration extends RecyclerView.ItemDecoration
{
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    //分割线drawable
    private Drawable mDivider;

    //分割线高度
    private int mDividerHeight;

    //分割线宽度
    private int mDividerWidth;

    public static RcvGridDecoration createDefault(Context context)
    {
        return new RcvGridDecoration(context);
    }

    public RcvGridDecoration(Context context)
    {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        mDividerHeight = mDivider.getIntrinsicHeight();
        mDividerWidth = mDivider.getIntrinsicWidth();
    }

    public RcvGridDecoration(Context context, Drawable drawable)
    {
        mDivider = drawable;
        mDividerHeight = mDivider.getIntrinsicHeight();
        mDividerWidth = mDivider.getIntrinsicWidth();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent)
    {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        else if (layoutManager instanceof StaggeredGridLayoutManager)
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        return spanCount;
    }

    //绘制垂直分割线
    public void drawHorizontal(Canvas c, RecyclerView recyclerView)
    {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = recyclerView.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin + mDividerWidth;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //绘制水平分割线
    public void drawVertical(Canvas c, RecyclerView recyclerView)
    {
        final int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            final View child = recyclerView.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerWidth;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //是否为最后一列
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount)
    {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL)
            {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                    return true;
            } else
            {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    //是否为最后一行
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount)
    {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL)
            {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0)
                    return true;
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent)
    {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
            outRect.set(0, 0, mDividerWidth, 0);
        else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
            outRect.set(0, 0, 0, mDividerHeight);
        else
            outRect.set(0, 0, mDividerWidth, mDividerHeight);
    }
}