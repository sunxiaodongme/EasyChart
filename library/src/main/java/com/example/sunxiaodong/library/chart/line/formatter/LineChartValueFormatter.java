package com.example.sunxiaodong.library.chart.line.formatter;

import com.example.sunxiaodong.library.chart.line.model.PointValue;

/**
 * Created by sunxiaodong on 16/6/27.
 */
public interface LineChartValueFormatter {

    int formatChartValue(char[] formattedValue, PointValue value);

}
