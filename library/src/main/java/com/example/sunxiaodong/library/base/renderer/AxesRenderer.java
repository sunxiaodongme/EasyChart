package com.example.sunxiaodong.library.base.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.example.sunxiaodong.library.base.computer.ChartComputer;
import com.example.sunxiaodong.library.base.model.Axis;
import com.example.sunxiaodong.library.base.model.AxisAutoValues;
import com.example.sunxiaodong.library.base.model.Viewport;
import com.example.sunxiaodong.library.base.util.ScreenUtil;
import com.example.sunxiaodong.library.base.view.Chart;

/**
 * Created by sunxiaodong on 16/6/24.
 */
public class AxesRenderer {

    private static final int DEFAULT_AXIS_SPACE_DP = 2;//轴间距

    /*坐标轴位置*/
    private static final int TOP = 0;//在上面
    private static final int BOTTOM = 2;//在下面
    private static final int LEFT = 3;//在左边
    private static final int RIGHT = 4;//在右边

    /**
     * 临时用来计算标签宽度的字符集
     */
    private static final char[] labelWidthChars = new char[]{
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};

    private Context context;
    private Chart chart;//图表数据
    private ChartComputer chartComputer;//图表计算器

    private int axisCommonSpace;//轴通用间距，轴名与标签之间间距，标签与轴线之间间距

    private Paint[] labelPaintTab = new Paint[]{new Paint(), new Paint(), new Paint(), new Paint()};//标签画笔
    private Paint[] namePaintTab = new Paint[]{new Paint(), new Paint(), new Paint(), new Paint()};//坐标轴名画笔
    private Paint[] linePaintTab = new Paint[]{new Paint(), new Paint(), new Paint(), new Paint()};//轴线画笔

    private int[] labelTextAscentTab = new int[4];//标签文本 ascent（基线上高度）
    private int[] labelTextDescentTab = new int[4];//标签文本 descent（基线下高度）

    private int[] labelWidthTab = new int[4];//最长标签的宽度

    private int[] labelDimensionForMarginsTab = new int[4];//标签文本dimens尺寸，左右轴算标签文本长度，上下轴算标签文本高度
    private int[] labelDimensionForStepsTab = new int[4];//坐标轴最小显示分隔的一半(两条分隔线之间像素数的一半），单位px。纵坐标上以标签文本的ascent作为一半。

    private float[] nameBaselineTab = new float[4];//坐标轴名文本绘制基准线（基准线只是一个坐标上的，另一个坐标需要动态计算）
    private float[] labelBaselineTab = new float[4];//坐标轴标签文本绘制基准线
    private float[] axisLineTab = new float[4];//坐标轴线位置（关键单坐标位置）

    private Paint.FontMetricsInt[] fontMetricsTab = new Paint.FontMetricsInt[]{new Paint.FontMetricsInt(), new Paint.FontMetricsInt(),
            new Paint.FontMetricsInt(), new Paint.FontMetricsInt()};//坐标轴名和标签文字统一FontMetrics

    private AxisAutoValues[] axisAutoLabelPosValuesTab = new AxisAutoValues[]{new AxisAutoValues(),
            new AxisAutoValues(), new AxisAutoValues(), new AxisAutoValues()};//坐标轴标签位置数据，图表逻辑坐标系

    private float[][] gridLinesCoordinateTab = new float[4][0];//网格线坐标，其中绘制一条线需要相邻的四个单元存储，上，下，左，右
    private float[][] labelValuesTab = new float[4][0];//坐标轴标签值
    private char[] labelFormatValue = new char[64];//单个标签值，格式化后数值
    private float[][] labelScreenPosValuesTab = new float[4][0];//标签屏幕坐标系位置数据（单位:px）
    private int[] labelDrawIndexTab = new int[4];//标签绘制索引，存储标签绘制顺序

    public AxesRenderer(Context context, Chart chart) {
        this.context = context;
        this.chart = chart;
        chartComputer = chart.getChartComputer();
        axisCommonSpace = ScreenUtil.dp2px(context, DEFAULT_AXIS_SPACE_DP);
        for (int position = 0; position < 4; ++position) {
            labelPaintTab[position].setStyle(Paint.Style.FILL);
            labelPaintTab[position].setAntiAlias(true);
            namePaintTab[position].setStyle(Paint.Style.FILL);
            namePaintTab[position].setAntiAlias(true);
            linePaintTab[position].setStyle(Paint.Style.STROKE);
            linePaintTab[position].setAntiAlias(true);
        }
    }

    /**
     * 图表尺寸改变
     */
    public void onChartSizeChanged() {
        onChartDataOrSizeChanged();
    }

    /**
     * 图表数据改变
     */
    public void onChartDataChanged() {
        onChartDataOrSizeChanged();
    }

    private void onChartDataOrSizeChanged() {
        initAxis(chart.getChartData().getAxisXTop(), TOP);
        initAxis(chart.getChartData().getAxisXBottom(), BOTTOM);
        initAxis(chart.getChartData().getAxisYLeft(), LEFT);
        initAxis(chart.getChartData().getAxisYRight(), RIGHT);
    }

    /**
     * 初始化坐标轴
     *
     * @param axis
     * @param position
     */
    private void initAxis(Axis axis, int position) {
        if (null == axis) {
            return;
        }
        initAxisAttributes(axis, position);
        initAxisMargin(axis, position);
        initAxisMeasurements(axis, position);
    }

    /**
     * 初始化坐标轴属性
     *
     * @param axis
     * @param position
     */
    private void initAxisAttributes(Axis axis, int position) {
        initAxisPaints(axis, position);
        initAxisTextAlignment(axis, position);
        initAxisDimension(position);
    }

    /**
     * 初始化坐标轴画笔
     *
     * @param axis
     * @param position
     */
    private void initAxisPaints(Axis axis, int position) {
        Typeface typeface = axis.getTypeface();
        if (null != typeface) {
            labelPaintTab[position].setTypeface(typeface);
            namePaintTab[position].setTypeface(typeface);
        }
        //标签画笔
        labelPaintTab[position].setColor(axis.getTextColor());
        labelPaintTab[position].setTextSize(ScreenUtil.sp2px(context, axis.getTextSize()));
        labelPaintTab[position].getFontMetricsInt(fontMetricsTab[position]);
        //标签名画笔
        namePaintTab[position].setColor(axis.getTextColor());
        namePaintTab[position].setTextSize(ScreenUtil.sp2px(context, axis.getTextSize()));
        //坐标轴线画笔
        linePaintTab[position].setColor(axis.getLineColor());

        labelTextAscentTab[position] = Math.abs(fontMetricsTab[position].ascent);
        labelTextDescentTab[position] = Math.abs(fontMetricsTab[position].descent);

        labelWidthTab[position] = (int) labelPaintTab[position].measureText(labelWidthChars, 0, axis.getMaxLabelChars());
    }

    /**
     * 初始化坐标轴文本对齐方式
     *
     * @param axis
     * @param position
     */
    private void initAxisTextAlignment(Axis axis, int position) {
        namePaintTab[position].setTextAlign(Paint.Align.CENTER);
        if (TOP == position || BOTTOM == position) {
            labelPaintTab[position].setTextAlign(Paint.Align.CENTER);
        } else if (LEFT == position) {
            if (axis.isInside()) {
                labelPaintTab[position].setTextAlign(Paint.Align.LEFT);
            } else {
                labelPaintTab[position].setTextAlign(Paint.Align.RIGHT);
            }
        } else if (RIGHT == position) {
            if (axis.isInside()) {
                labelPaintTab[position].setTextAlign(Paint.Align.RIGHT);
            } else {
                labelPaintTab[position].setTextAlign(Paint.Align.LEFT);
            }
        }
    }

    /**
     * 初始化尺寸
     *
     * @param position
     */
    private void initAxisDimension(int position) {
        if (LEFT == position || RIGHT == position) {
            labelDimensionForMarginsTab[position] = labelWidthTab[position];
            labelDimensionForStepsTab[position] = labelTextAscentTab[position];
        } else if (TOP == position || BOTTOM == position) {
            labelDimensionForMarginsTab[position] = labelTextAscentTab[position] + labelTextDescentTab[position];
            labelDimensionForStepsTab[position] = labelWidthTab[position];
        }
    }

    private void initAxisMargin(Axis axis, int position) {
        int margin = 0;
        if (!axis.isInside()) {
            margin += axisCommonSpace + labelDimensionForMarginsTab[position];
        }
        margin += getAxisNameMargin(axis, position);
        insetContentRectWithAxesMargins(margin, position);
    }

    /**
     * 坐标轴名字边距
     *
     * @param axis
     * @param position
     * @return
     */
    private int getAxisNameMargin(Axis axis, int position) {
        int margin = 0;
        if (!TextUtils.isEmpty(axis.getAxisName())) {
            margin += labelTextAscentTab[position];
            margin += labelTextDescentTab[position];
            margin += axisCommonSpace;
        }
        return margin;
    }

    /**
     * 用总的坐标轴边距（轴名尺寸，轴名与标签间距，便签尺寸，标签与轴线间距），初始化内容矩形大小
     *
     * @param axisMargin
     * @param position
     */
    private void insetContentRectWithAxesMargins(int axisMargin, int position) {
        if (LEFT == position) {
            chartComputer.insetContentRect(axisMargin, 0, 0, 0);
        } else if (RIGHT == position) {
            chartComputer.insetContentRect(0, 0, axisMargin, 0);
        } else if (TOP == position) {
            chartComputer.insetContentRect(0, axisMargin, 0, 0);
        } else if (BOTTOM == position) {
            chartComputer.insetContentRect(0, 0, 0, axisMargin);
        }
    }

    /**
     * 初始化轴名，标签和轴线绘制基准线
     *
     * @param axis
     * @param position
     */
    private void initAxisMeasurements(Axis axis, int position) {
        if (LEFT == position) {
            if (axis.isInside()) {
                labelBaselineTab[position] = chartComputer.getContentRectMinusAllMargins().left + axisCommonSpace;
                nameBaselineTab[position] = chartComputer.getContentRectMinusAxesMargins().left - axisCommonSpace - labelTextDescentTab[position];
            } else {
                labelBaselineTab[position] = chartComputer.getContentRectMinusAxesMargins().left - axisCommonSpace;
                nameBaselineTab[position] = labelBaselineTab[position] - axisCommonSpace - labelTextDescentTab[position] - labelDimensionForMarginsTab[position];
            }
            axisLineTab[position] = chartComputer.getContentRectMinusAllMargins().left;
        } else if (RIGHT == position) {
            if (axis.isInside()) {
                labelBaselineTab[position] = chartComputer.getContentRectMinusAllMargins().right - axisCommonSpace;
                nameBaselineTab[position] = chartComputer.getContentRectMinusAxesMargins().right + axisCommonSpace + labelTextAscentTab[position];
            } else {
                labelBaselineTab[position] = chartComputer.getContentRectMinusAxesMargins().right + axisCommonSpace;
                nameBaselineTab[position] = labelBaselineTab[position] + axisCommonSpace + labelTextAscentTab[position] + labelDimensionForMarginsTab[position];
            }
            axisLineTab[position] = chartComputer.getContentRectMinusAllMargins().right;
        } else if (BOTTOM == position) {
            if (axis.isInside()) {
                labelBaselineTab[position] = chartComputer.getContentRectMinusAllMargins().bottom - axisCommonSpace - labelTextDescentTab[position];
                nameBaselineTab[position] = chartComputer.getContentRectMinusAxesMargins().bottom + axisCommonSpace + labelTextAscentTab[position];
            } else {
                labelBaselineTab[position] = chartComputer.getContentRectMinusAxesMargins().bottom + axisCommonSpace + labelTextAscentTab[position];
                nameBaselineTab[position] = labelBaselineTab[position] + axisCommonSpace + labelDimensionForMarginsTab[position];
            }
            axisLineTab[position] = chartComputer.getContentRectMinusAllMargins().bottom;
        } else if (TOP == position) {
            if (axis.isInside()) {
                labelBaselineTab[position] = chartComputer.getContentRectMinusAllMargins().top + axisCommonSpace + labelTextAscentTab[position];
                nameBaselineTab[position] = chartComputer.getContentRectMinusAxesMargins().top - axisCommonSpace - labelTextDescentTab[position];
            } else {
                labelBaselineTab[position] = chartComputer.getContentRectMinusAxesMargins().top - axisCommonSpace - labelTextDescentTab[position];
                nameBaselineTab[position] = labelBaselineTab[position] - axisCommonSpace - labelDimensionForMarginsTab[position];
            }
            axisLineTab[position] = chartComputer.getContentRectMinusAllMargins().top;
        } else {
            throw new IllegalArgumentException("Invalid axis position: " + position);
        }
    }

    /**
     * 绘制坐标轴背景部分（只绘制坐标线）
     *
     * @param canvas
     */
    public void drawBackground(Canvas canvas) {
        //绘制上面坐标轴
        Axis axis = chart.getChartData().getAxisXTop();
        if (null != axis) {
            prepareAxisDrawData(axis, TOP);
            drawLines(canvas, axis, TOP);
        }

        //绘制下面坐标轴
        axis = chart.getChartData().getAxisXBottom();
        if (null != axis) {
            prepareAxisDrawData(axis, BOTTOM);
            drawLines(canvas, axis, BOTTOM);
        }

        //绘制左边坐标轴
        axis = chart.getChartData().getAxisYLeft();
        if (null != axis) {
            prepareAxisDrawData(axis, LEFT);
            drawLines(canvas, axis, LEFT);
        }

        //绘制右边坐标轴
        axis = chart.getChartData().getAxisYRight();
        if (null != axis) {
            prepareAxisDrawData(axis, RIGHT);
            drawLines(canvas, axis, RIGHT);
        }
    }

    /**
     * 准备坐标轴绘制数据，当前主要是网格线绘制数据
     *
     * @param axis
     * @param position
     */
    private void prepareAxisDrawData(Axis axis, int position) {
        final Viewport visibleViewport = chartComputer.getVisibleViewport();
        final Rect contentRect = chartComputer.getContentRectMinusAllMargins();
        boolean isAxisVertical = isAxisVertical(position);
        float start, stop;
        int contentRectDimension;
        if (isAxisVertical) {
            start = visibleViewport.bottom;
            stop = visibleViewport.top;
            contentRectDimension = contentRect.height();
        } else {
            start = visibleViewport.left;
            stop = visibleViewport.right;
            contentRectDimension = contentRect.width();
        }
        computeAutoGeneratedAxisLabelValues(start, stop, Math.abs(contentRectDimension) / labelDimensionForStepsTab[position] / 2, axisAutoLabelPosValuesTab[position]);

        //初始化网格线坐标
        if (axis.isHasGridLines() && (gridLinesCoordinateTab[position].length < axisAutoLabelPosValuesTab[position].valuesNumber * 4)) {
            gridLinesCoordinateTab[position] = new float[axisAutoLabelPosValuesTab[position].valuesNumber * 4];
        }

        //
        if (labelScreenPosValuesTab[position].length < axisAutoLabelPosValuesTab[position].valuesNumber) {
            labelScreenPosValuesTab[position] = new float[axisAutoLabelPosValuesTab[position].valuesNumber];
        }
        if (labelValuesTab[position].length < axisAutoLabelPosValuesTab[position].valuesNumber) {
            labelValuesTab[position] = new float[axisAutoLabelPosValuesTab[position].valuesNumber];
        }

        float rawValue;
        int labelToDrawIndex = 0;
        for (int i = 0; i < axisAutoLabelPosValuesTab[position].valuesNumber; ++i) {
            if (isAxisVertical) {
                rawValue = chartComputer.computeRawY(axisAutoLabelPosValuesTab[position].values[i]);
            } else {
                rawValue = chartComputer.computeRawX(axisAutoLabelPosValuesTab[position].values[i]);
            }
            if (checkRawValue(contentRect, rawValue, axis.isInside(), position, isAxisVertical)) {
                labelScreenPosValuesTab[position][labelToDrawIndex] = rawValue;
                labelValuesTab[position][labelToDrawIndex] = axisAutoLabelPosValuesTab[position].values[i];
                ++labelToDrawIndex;
            }
        }
        labelDrawIndexTab[position] = labelToDrawIndex;
    }

    /**
     * 计算自动生成的坐标轴上的标签值
     *
     * @param start     图表显示起始坐标
     * @param stop      图表显示终止坐标
     * @param steps     坐标轴最小显示分隔单位，即两条网格线之间的像素数（单位：px)
     * @param outValues 坐标轴待显示标签数组
     */
    private void computeAutoGeneratedAxisLabelValues(float start, float stop, int steps, AxisAutoValues outValues) {
        double range = stop - start;
        if (steps == 0 || range <= 0) {
            outValues.values = new float[]{};
            outValues.valuesNumber = 0;
            return;
        }

        double rawInterval = range / steps;
        double interval = roundToOneSignificantFigure(rawInterval);
        double intervalMagnitude = Math.pow(10, (int) Math.log10(interval));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or 90
            interval = Math.floor(10 * intervalMagnitude);
        }

        double first = Math.ceil(start / interval) * interval;
        double last = nextUp(Math.floor(stop / interval) * interval);

        double intervalValue;
        int valueIndex;
        int valuesNum = 0;
        for (intervalValue = first; intervalValue <= last; intervalValue += interval) {
            ++valuesNum;
        }

        outValues.valuesNumber = valuesNum;

        if (outValues.values.length < valuesNum) {
            // Ensure stops contains at least numStops elements.
            outValues.values = new float[valuesNum];
        }

        for (intervalValue = first, valueIndex = 0; valueIndex < valuesNum; intervalValue += interval, ++valueIndex) {
            outValues.values[valueIndex] = (float) intervalValue;
        }

        if (interval < 1) {
            outValues.decimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            outValues.decimals = 0;
        }
    }

    private float roundToOneSignificantFigure(double num) {
        final float d = (float) Math.ceil((float) Math.log10(num < 0 ? -num : num));
        final int power = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, power);
        final long shifted = Math.round(num * magnitude);
        return shifted / magnitude;
    }

    public static double nextUp(double d) {
        if (Double.isNaN(d) || d == Double.POSITIVE_INFINITY) {
            return d;
        } else {
            d += 0.0;
            return Double.longBitsToDouble(Double.doubleToRawLongBits(d) + ((d >= 0.0) ? +1 : -1));
        }
    }

    private boolean checkRawValue(Rect rect, float rawValue, boolean axisInside, int position, boolean isVertical) {
        if (axisInside) {
            if (isVertical) {
                float marginBottom = labelTextAscentTab[BOTTOM] + axisCommonSpace;
                float marginTop = labelTextAscentTab[TOP] + axisCommonSpace;
                if (rawValue <= rect.bottom - marginBottom && rawValue >= rect.top + marginTop) {
                    return true;
                } else {
                    return false;
                }
            } else {
                float margin = labelWidthTab[position] / 2;
                if (rawValue >= rect.left + margin && rawValue <= rect.right - margin) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 绘制线（包括轴线和网格线）
     *
     * @param canvas
     * @param axis
     * @param position
     */
    private void drawLines(Canvas canvas, Axis axis, int position) {
        if (axis.isHasAxisLine()) {
            drawAxisLine(canvas, position);
        }
        if (axis.isHasGridLines()) {
            drawGridLines(canvas, position);
        }
    }

    /**
     * 绘制坐标轴线，使用与标签相同的画笔绘制
     *
     * @param canvas
     * @param position
     */
    private void drawAxisLine(Canvas canvas, int position) {
        final Rect contentRectMargins = chartComputer.getContentRectMinusAxesMargins();
        float separationX1, separationY1, separationX2, separationY2;
        separationX1 = separationY1 = separationX2 = separationY2 = 0;

        if (isAxisVertical(position)) {
            separationX1 = separationX2 = axisLineTab[position];
            separationY1 = contentRectMargins.bottom;
            separationY2 = contentRectMargins.top;
        } else if (TOP == position || BOTTOM == position) {
            separationX1 = contentRectMargins.left;
            separationX2 = contentRectMargins.right;
            separationY1 = separationY2 = axisLineTab[position];
        }
        canvas.drawLine(separationX1, separationY1, separationX2, separationY2, labelPaintTab[position]);
    }

    /**
     * 绘制网格线，使用与标签相同的画笔绘制
     *
     * @param canvas
     * @param position
     */
    private void drawGridLines(Canvas canvas, int position) {
        final Rect contentRectMargins = chartComputer.getContentRectMinusAxesMargins();
        float lineX1, lineY1, lineX2, lineY2;
        lineX1 = lineY1 = lineX2 = lineY2 = 0;
        boolean isAxisVertical = isAxisVertical(position);
        if (isAxisVertical) {
            lineX1 = contentRectMargins.left;
            lineX2 = contentRectMargins.right;
        } else if (TOP == position || BOTTOM == position) {
            lineY1 = contentRectMargins.top;
            lineY2 = contentRectMargins.bottom;
        }
        int labelToDrawIndex = 0;
        for (; labelToDrawIndex < labelDrawIndexTab[position]; ++labelToDrawIndex) {
            if (isAxisVertical) {
                lineY1 = lineY2 = labelScreenPosValuesTab[position][labelToDrawIndex];
            } else {
                lineX1 = lineX2 = labelScreenPosValuesTab[position][labelToDrawIndex];
            }
            gridLinesCoordinateTab[position][labelToDrawIndex * 4 + 0] = lineX1;
            gridLinesCoordinateTab[position][labelToDrawIndex * 4 + 1] = lineY1;
            gridLinesCoordinateTab[position][labelToDrawIndex * 4 + 2] = lineX2;
            gridLinesCoordinateTab[position][labelToDrawIndex * 4 + 3] = lineY2;
        }
        canvas.drawLines(gridLinesCoordinateTab[position], 0, labelToDrawIndex * 4, linePaintTab[position]);
    }

    /**
     * 绘制坐标轴前景部分（坐标轴名字和标签）
     *
     * @param canvas
     */
    public void drawForeground(Canvas canvas) {
        //绘制上边坐标轴
        Axis axis = chart.getChartData().getAxisXTop();
        if (null != axis) {
            drawAxisLabelsAndName(canvas, axis, TOP);
        }

        //绘制下边坐标轴
        axis = chart.getChartData().getAxisXBottom();
        if (null != axis) {
            drawAxisLabelsAndName(canvas, axis, BOTTOM);
        }

        //绘制左边坐标轴
        axis = chart.getChartData().getAxisYLeft();
        if (null != axis) {
            drawAxisLabelsAndName(canvas, axis, LEFT);
        }

        //绘制右边坐标轴
        axis = chart.getChartData().getAxisYRight();
        if (null != axis) {
            drawAxisLabelsAndName(canvas, axis, RIGHT);
        }
    }

    /**
     * 绘制坐标轴标签和名字
     *
     * @param canvas
     * @param axis
     * @param position
     */
    private void drawAxisLabelsAndName(Canvas canvas, Axis axis, int position) {
        drawAxisLabels(canvas, axis, position);
        drawAxisName(canvas, axis, position);
    }

    /**
     * 绘制坐标轴标签名
     *
     * @param canvas
     * @param axis
     * @param position
     */
    private void drawAxisLabels(Canvas canvas, Axis axis, int position) {
        float labelX, labelY;
        labelX = labelY = 0;
        boolean isAxisVertical = isAxisVertical(position);
        if (LEFT == position || RIGHT == position) {
            labelX = labelBaselineTab[position];
        } else if (TOP == position || BOTTOM == position) {
            labelY = labelBaselineTab[position];
        }
        for (int labelToDrawIndex = 0; labelToDrawIndex < labelDrawIndexTab[position]; ++labelToDrawIndex) {
            int charsNumber = 0;
            final float value = labelValuesTab[position][labelToDrawIndex];
            charsNumber = axis.getLabelFormatter().formatValueForAutoGeneratedAxis(labelFormatValue, value, axisAutoLabelPosValuesTab[position].decimals);
            if (isAxisVertical) {
                labelY = labelScreenPosValuesTab[position][labelToDrawIndex];
            } else {
                labelX = labelScreenPosValuesTab[position][labelToDrawIndex];
            }
            canvas.drawText(labelFormatValue, labelFormatValue.length - charsNumber, charsNumber, labelX, labelY, labelPaintTab[position]);
        }
    }

    /**
     * 绘制坐标轴名
     *
     * @param canvas
     * @param axis
     * @param position
     */
    private void drawAxisName(Canvas canvas, Axis axis, int position) {
        final Rect contentRectMargins = chartComputer.getContentRectMinusAxesMargins();
        if (!TextUtils.isEmpty(axis.getAxisName())) {
            if (isAxisVertical(position)) {
                canvas.save();
                canvas.rotate(-90, contentRectMargins.centerY(), contentRectMargins.centerY());
                canvas.drawText(axis.getAxisName(), contentRectMargins.centerY(), nameBaselineTab[position], namePaintTab[position]);
                canvas.restore();
            } else {
                canvas.drawText(axis.getAxisName(), contentRectMargins.centerX(), nameBaselineTab[position], namePaintTab[position]);
            }
        }
    }

    /**
     * 根据位置判断，轴线是否垂直
     *
     * @param position
     * @return
     */
    private boolean isAxisVertical(int position) {
        if (LEFT == position || RIGHT == position) {
            return true;
        } else if (TOP == position || BOTTOM == position) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid axis position " + position);
        }
    }

}
