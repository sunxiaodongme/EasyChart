package com.example.sunxiaodong.library.chart.line.model;

import android.graphics.Color;
import android.graphics.PathEffect;

import com.example.sunxiaodong.library.chart.line.formatter.LineChartValueFormatter;
import com.example.sunxiaodong.library.chart.line.formatter.SimpleLineChartValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 线型图表数据单元
 * Created by sunxiaodong on 16/6/27.
 */
public class Line {
    private static final int DEFAULT_LINE_WIDTH_PX = 9;
    private static final int DEFAULT_FILL_ALPHA = 64;
    private static final int DEFAULT_POINT_RADIUS_PX = 18;
    private int DEFAULT_POINT_LABEL_OFFSET_PX = 12;
    private int DEFAULT_POINT_LABEL_TEXT_SIZE_PX = 48;

    private List<PointValue> points = new ArrayList<PointValue>();//线条上的点

    private boolean isFilled = false;//是否填充线下区域
    private int fillAlpha = DEFAULT_FILL_ALPHA;//填充区域alpha

    private boolean hasLines = true;//是否显示线
    private int lineColor = Color.parseColor("#DFDFDF");//线条颜色
    private int lineWidth = DEFAULT_LINE_WIDTH_PX;//线条宽度(单位：px)

    private boolean hasPoints = true;//是否显示点
    private int pointColor = 0;//点颜色
    private PointValue.Shape pointShape = PointValue.Shape.CIRCLE;//点形状
    private int pointRadius = DEFAULT_POINT_RADIUS_PX;//点半径（单位：px)
    private boolean hasPointLabel = false;//是否显示点标签
    private int pointLabelOffset = DEFAULT_POINT_LABEL_OFFSET_PX;//点上的标签距点的位置偏移
    private LineChartValueFormatter labelFormatter = new SimpleLineChartValueFormatter();//电上标签格式化工具
    private int pointLabelColor = Color.parseColor("#99CC00");//点上标签文本颜色
    private int pointLabelTextSize = DEFAULT_POINT_LABEL_TEXT_SIZE_PX;//点上标签文本大小
    private PathEffect pathEffect;//线条特性设置

    public List<PointValue> getPoints() {
        return points;
    }

    public Line setPoints(List<PointValue> points) {
        this.points = points;
        return this;
    }

    public boolean isHasLines() {
        return hasLines;
    }

    public Line setHasLines(boolean hasLines) {
        this.hasLines = hasLines;
        return this;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public Line setFilled(boolean filled) {
        isFilled = filled;
        return this;
    }

    public int getFillAlpha() {
        return fillAlpha;
    }

    public Line setFillAlpha(int fillAlpha) {
        this.fillAlpha = fillAlpha;
        return this;
    }

    public int getLineColor() {
        return lineColor;
    }

    public Line setLineColor(int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getPointColor() {
        return pointColor;
    }

    public Line setPointColor(int pointColor) {
        this.pointColor = pointColor;
        return this;
    }

    public PointValue.Shape getPointShape() {
        return pointShape;
    }

    public Line setPointShape(PointValue.Shape pointShape) {
        this.pointShape = pointShape;
        return this;
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public Line setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
        return this;
    }

    public boolean isHasPointLabel() {
        return hasPointLabel;
    }

    public void setHasPointLabel(boolean hasPointLabel) {
        this.hasPointLabel = hasPointLabel;
    }

    public int getPointLabelOffset() {
        return pointLabelOffset;
    }

    public Line setPointLabelOffset(int pointLabelOffset) {
        this.pointLabelOffset = pointLabelOffset;
        return this;
    }

    public LineChartValueFormatter getLabelFormatter() {
        return labelFormatter;
    }

    public Line setLabelFormatter(LineChartValueFormatter labelFormatter) {
        this.labelFormatter = labelFormatter;
        return this;
    }

    public int getPointLabelColor() {
        return pointLabelColor;
    }

    public Line setPointLabelColor(int pointLabelColor) {
        this.pointLabelColor = pointLabelColor;
        return this;
    }

    public int getPointLabelTextSize() {
        return pointLabelTextSize;
    }

    public Line setPointLabelTextSize(int pointLabelTextSize) {
        this.pointLabelTextSize = pointLabelTextSize;
        return this;
    }

    public PathEffect getPathEffect() {
        return pathEffect;
    }

    public Line setPathEffect(PathEffect pathEffect) {
        this.pathEffect = pathEffect;
        return this;
    }

    public boolean isHasPoints() {
        return hasPoints;
    }

    public Line setHasPoints(boolean hasPoints) {
        this.hasPoints = hasPoints;
        return this;
    }

}
