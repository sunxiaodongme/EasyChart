package com.example.sunxiaodong.library.base.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;

import com.example.sunxiaodong.library.base.listener.ChartAnimationListener;
import com.example.sunxiaodong.library.base.view.Chart;

/**
 * 支持到Android 4.0
 * Created by sunxiaodong on 16/6/28.
 */
public class SimpleChartDataAnimatorV14 implements ChartDataAnimator, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private final Chart chart;
    private ValueAnimator animator;
    private ChartAnimationListener chartAnimationListener;//图表动画回调

    public SimpleChartDataAnimatorV14(Chart chart) {
        this.chart = chart;
        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.addListener(this);
        animator.addUpdateListener(this);
    }

    @Override
    public void startAnimation(long duration) {
        if (duration >= 0) {
            animator.setDuration(duration);
        } else {
            animator.setDuration(DEFAULT_ANIMATION_DURATION);
        }
        animator.start();
    }

    @Override
    public void cancelAnimation() {
        animator.cancel();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        chart.dataAnimationUpdate(animation.getAnimatedFraction());
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        chart.dataAnimationFinished();
        if (chartAnimationListener != null) {
            chartAnimationListener.onAnimationFinished();
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (chartAnimationListener != null) {
            chartAnimationListener.onAnimationStarted();
        }
    }

    @Override
    public void setChartAnimationListener(ChartAnimationListener chartAnimationListener) {
        this.chartAnimationListener = chartAnimationListener;
    }

}
