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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Function:RecyclerView的分割线【适用于GridLayoutMananger和StaggeredGridLayoutManager】
 */
public class RcvGridDecoration extends RecyclerView.ItemDecoration
{
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    //分割线drawable
    private Drawable mDividerDrawable;

    //绘制分割线的Paint
    private Paint mDividerPaint;

    //分割线高度
    private int mDividerHeight;

    //分割线宽度
    private int mDividerWidth;

    /**
     * 创建默认分割线
     */
    public static RcvGridDecoration createDefault(Context context)
    {
        return new RcvGridDecoration(context);
    }

    /**
     * 创建自定义色值默认分割线
     *
     * @param color 色值
     */
    public static RcvGridDecoration createDefault(int color)
    {
        return new RcvGridDecoration(color);
    }

    /**
     * 默认Drawable的分割线
     *
     * @param context 上下文
     */
    public RcvGridDecoration(Context context)
    {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDividerDrawable = a.getDrawable(0);
        a.recycle();
        mDividerHeight = mDividerDrawable.getIntrinsicHeight();
        mDividerWidth = mDividerDrawable.getIntrinsicWidth();
    }

    /**
     * 自定义Drawable的分割线
     *
     * @param context  上下文
     * @param drawable 自定义Drawable
     */
    public RcvGridDecoration(Context context, Drawable drawable)
    {
        mDividerDrawable = drawable;
        mDividerHeight = mDividerDrawable.getIntrinsicHeight();
        mDividerWidth = mDividerDrawable.getIntrinsicWidth();
    }

    /**
     * 自定义Drawable的分割线
     *
     * @param context       上下文
     * @param drawableResId 自定义Drawable的资源id
     */
    public RcvGridDecoration(Context context, @DrawableRes int drawableResId)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mDividerDrawable = context.getResources().getDrawable(drawableResId, null);
        } else
        {
            mDividerDrawable = context.getResources().getDrawable(drawableResId);
        }
        mDividerHeight = mDividerDrawable.getIntrinsicHeight();
        mDividerWidth = mDividerDrawable.getIntrinsicWidth();
    }

    /**
     * 自定义Color的分割线（默认分割线宽高均为1px）
     *
     * @param color 色值
     */
    public RcvGridDecoration(@ColorInt int color)
    {
        this(color, 1, 1);
    }

    /**
     * 自定义Color的分割线
     *
     * @param color  色值
     * @param width  宽度【单位px】
     * @param height 高度【单位px】
     */
    public RcvGridDecoration(@ColorInt int color, int width, int height)
    {
        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setColor(color);
        mDividerPaint.setStyle(Paint.Style.FILL);
        mDividerWidth = width;
        mDividerHeight = height;
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
        {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
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
        if (isLastRaw(parent, itemPosition, spanCount, childCount))
        {
            // 如果是最后一行，则不需要绘制底部
            outRect.set(0, 0, mDividerWidth, 0);
        } else if (isLastColum(parent, itemPosition, spanCount, childCount))
        {
            // 如果是最后一列，则不需要绘制右边
            outRect.set(0, 0, 0, mDividerHeight);
        } else
        {
            outRect.set(0, 0, mDividerWidth, mDividerHeight);
        }
    }
}