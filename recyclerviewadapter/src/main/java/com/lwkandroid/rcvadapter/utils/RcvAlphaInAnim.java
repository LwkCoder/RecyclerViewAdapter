package com.lwkandroid.rcvadapter.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.lwkandroid.rcvadapter.base.RcvBaseAnimation;

/**
 * 淡入动画效果
 */
public class RcvAlphaInAnim extends RcvBaseAnimation
{
    @Override
    public Animator[] getAnimator(View v)
    {
        return new Animator[]{ObjectAnimator.ofFloat(v, "alpha", 0f, 1f)};
    }
}
