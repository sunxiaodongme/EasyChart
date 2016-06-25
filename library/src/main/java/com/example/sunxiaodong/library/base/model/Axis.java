package com.example.sunxiaodong.library.base.model;

import android.graphics.Color;
import android.graphics.Typeface;

import com.example.sunxiaodong.library.base.formatter.AxisValueFormatter;
import com.example.sunxiaodong.library.base.formatter.SimpleAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 坐标轴数据单元
 * Created by sunxiaodong on 16/6/24.
 */
public class Axis {

    public static final int DEFAULT_TEXT_SIZE_SP = 12;//默认字体大小，单位sp
    public static final int DEFAULT_MAX_AXIS_LABEL_CHARS = 3;//默认坐标轴标签最大显示字符数

    private String axisName;//轴名

    private Typeface typeface;//轴名和标签的字体
    private int textColor = Color.LTGRAY;//轴名和标签的字体颜色
    private int textSize = DEFAULT_TEXT_SIZE_SP;//轴名和标签的字体大小，单位sp

    private int lineColor = Color.parseColor("#DDDDDD");//轴线颜色

    private int maxLabelChars = DEFAULT_MAX_AXIS_LABEL_CHARS;

    private boolean isInside = false;//标签是否显示在坐标轴内？
    private boolean hasGridLines = false;//是否有网格线
    private boolean hasAxisLine = true;//是否有坐标轴线

    private List<AxisLabel> axisLabels = new ArrayList<AxisLabel>();//坐标轴标签

    private AxisValueFormatter labelFormatter = new SimpleAxisValueFormatter();//标签格式化器

    public String getAxisName() {
        return axisName;
    }

    public void setAxisName(String axisName) {
        this.axisName = axisName;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getMaxLabelChars() {
        return maxLabelChars;
    }

    public void setMaxLabelChars(int maxLabelChars) {
        this.maxLabelChars = maxLabelChars;
    }

    public boolean isInside() {
        return isInside;
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }

    public boolean isHasGridLines() {
        return hasGridLines;
    }

    public void setHasGridLines(boolean hasGridLines) {
        this.hasGridLines = hasGridLines;
    }

    public boolean isHasAxisLine() {
        return hasAxisLine;
    }

    public void setHasAxisLine(boolean hasAxisLine) {
        this.hasAxisLine = hasAxisLine;
    }

    public List<AxisLabel> getAxisLabels() {
        return axisLabels;
    }

    public void setAxisLabels(List<AxisLabel> axisLabels) {
        this.axisLabels = axisLabels;
    }

    public AxisValueFormatter getLabelFormatter() {
        return labelFormatter;
    }

    public void setLabelFormatter(AxisValueFormatter labelFormatter) {
        this.labelFormatter = labelFormatter;
    }

}
