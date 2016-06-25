package com.example.sunxiaodong.library.base.view;

import com.example.sunxiaodong.library.base.computer.ChartComputer;
import com.example.sunxiaodong.library.base.model.ChartData;

/**
 * 图表基础操作接口
 * Created by sunxiaodong on 16/6/24.
 */
public interface Chart {

    ChartData getChartData();//获取图表数据
    ChartComputer getChartComputer();//获取图表计算器

}
