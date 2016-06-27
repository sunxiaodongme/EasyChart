package com.example.sunxiaodong.library.base.renderer;

import com.example.sunxiaodong.library.base.computer.ChartComputer;
import com.example.sunxiaodong.library.base.view.Chart;

/**
 * 抽象图表渲染器，基础图表操作
 * Created by sunxiaodong on 16/6/27.
 */
public abstract class AbstractChartRenderer implements ChartRenderer {

    protected Chart chart;
    protected ChartComputer chartComputer;

    public AbstractChartRenderer(Chart chart) {
        this.chart = chart;
        chartComputer = chart.getChartComputer();
    }

}
