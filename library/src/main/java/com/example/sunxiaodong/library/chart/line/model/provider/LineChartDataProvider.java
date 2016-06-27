package com.example.sunxiaodong.library.chart.line.model.provider;

import com.example.sunxiaodong.library.chart.line.model.LineChartData;

/**
 * 线型图表数据提供器
 * Created by sunxiaodong on 16/6/27.
 */
public interface LineChartDataProvider {

    LineChartData getLineChartData();

    void setLineChartData(LineChartData lineChartData);

}
