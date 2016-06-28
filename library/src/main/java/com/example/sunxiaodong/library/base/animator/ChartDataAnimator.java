package com.example.sunxiaodong.library.base.animator;

import com.example.sunxiaodong.library.base.listener.ChartAnimationListener;

/**
 * 图表数据改变，视图变化动画接口
 * Created by sunxiaodong on 16/6/28.
 */
public interface ChartDataAnimator {

    long DEFAULT_ANIMATION_DURATION = 500;

    void startAnimation(long duration);

    void cancelAnimation();

    void setChartAnimationListener(ChartAnimationListener chartAnimationListener);

}
