package com.example.sunxiaodong.library.base.view;

import com.example.sunxiaodong.library.base.computer.ChartComputer;
import com.example.sunxiaodong.library.base.listener.ChartAnimationListener;
import com.example.sunxiaodong.library.base.model.ChartData;
import com.example.sunxiaodong.library.base.model.Viewport;
import com.example.sunxiaodong.library.base.renderer.ChartRenderer;

/**
 * 图表基础操作接口
 * Created by sunxiaodong on 16/6/24.
 */
public interface Chart {

    /*------------------------------数据----------------------------------start*/

    ChartData getChartData();//获取图表数据
    ChartComputer getChartComputer();//获取图表计算器

    /*------------------------------数据----------------------------------end*/

    void onChartDataChange();

    /*------------------------------视窗----------------------------------start*/

    Viewport getMaxViewport();//获取最大视窗
    void setMaxViewport(Viewport maxViewport);//设置最大视窗

    Viewport getCurrentViewport();//获取当前视窗
    void setCurrentViewport(Viewport viewport);//设置当前视窗

    /*------------------------------视窗----------------------------------end*/

    /*------------------------------图表渲染器-----------------------------start*/

    ChartRenderer getChartRenderer();//获取图表渲染器
    void setChartRenderer(ChartRenderer renderer);//设置图表渲染器

    /*------------------------------图表渲染器-----------------------------end*/

    /*------------------------------图表动画-------------------------------start*/

    void startAnimation();
    void startAnimation(long duration);
    void cancelAnimation();
    void setChartAnimationListener(ChartAnimationListener chartAnimationListener);

    void dataAnimationUpdate(float scale);//变换比例（范围：0 - 1）,持续更新图表
    void dataAnimationFinished();//动画结束

    /*------------------------------图表动画-------------------------------end*/

}
