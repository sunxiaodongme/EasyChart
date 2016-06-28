package com.example.sunxiaodong.library.base.animator;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.example.sunxiaodong.library.base.listener.ChartAnimationListener;
import com.example.sunxiaodong.library.base.view.Chart;

/**
 * 支持到Android 2.2
 * Created by sunxiaodong on 16/6/28.
 */
public class SimpleChartDataAnimatorV8 implements ChartDataAnimator {

    private static final int FRAME_INTERVAL_TIME = 16;//真刷新间隔，ms

    private final Chart chart;
    private final Handler handler;
    private final Interpolator interpolator = new AccelerateDecelerateInterpolator();//动画执行插值器
    private long start;//动画启动时间
    private long duration;//动画执行时间

    private ChartAnimationListener chartAnimationListener;//动画执行回调

    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - start;
            if (elapsed > duration) {
                handler.removeCallbacks(runnable);
                chart.dataAnimationFinished();
                return;
            }
            float scale = Math.min(interpolator.getInterpolation((float) elapsed / duration), 1);
            chart.dataAnimationUpdate(scale);
            handler.postDelayed(this, FRAME_INTERVAL_TIME);

        }
    };

    public SimpleChartDataAnimatorV8(Chart chart) {
        this.chart = chart;
        this.handler = new Handler();
    }

    @Override
    public void startAnimation(long duration) {
        if (duration >= 0) {
            this.duration = duration;
        } else {
            this.duration = DEFAULT_ANIMATION_DURATION;
        }
        if (chartAnimationListener != null) {
            chartAnimationListener.onAnimationStarted();
        }
        start = SystemClock.uptimeMillis();
        handler.post(runnable);
    }

    @Override
    public void cancelAnimation() {
        handler.removeCallbacks(runnable);
        chart.dataAnimationFinished();
        if (chartAnimationListener != null) {
            chartAnimationListener.onAnimationFinished();
        }
    }

    @Override
    public void setChartAnimationListener(ChartAnimationListener chartAnimationListener) {
        this.chartAnimationListener = chartAnimationListener;
    }

}
