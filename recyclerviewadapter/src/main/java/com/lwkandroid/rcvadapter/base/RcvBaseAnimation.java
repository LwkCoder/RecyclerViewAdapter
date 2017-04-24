package com.lwkandroid.rcvadapter.base;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 子布局展现时的动画基类
 * [具体实现请参照类RcvAlphaInAnim]
 */
public abstract class RcvBaseAnimation
{
    /**
     * 动画加速器：默认为LinearInterpolator
     */
    protected Interpolator mInterpolator = new LinearInterpolator();

    /**
     * 动画持续时间：默认为300ms
     */
    protected long mAnimDuration = 300;

    /**
     * 设置动画加速器
     */
    public void setInterpolator(Interpolator interpolator)
    {
        this.mInterpolator = interpolator;
    }

    /**
     * 设置动画持续时间
     */
    public void setAnimDuration(long animDuration)
    {
        this.mAnimDuration = animDuration;
    }

    /**
     * 子类实现此方法决定动画类型
     */
    public abstract Animator[] getAnimator(View v);

    /**
     * 开始动画
     */
    public void startAnim(View v)
    {
        Animator[] animators = getAnimator(v);
        for (Animator animator : animators)
        {
            animator.setDuration(mAnimDuration);
            animator.setInterpolator(mInterpolator);
            animator.start();
        }
    }
}
