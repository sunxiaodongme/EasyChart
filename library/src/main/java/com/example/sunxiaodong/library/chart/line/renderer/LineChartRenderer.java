package com.example.sunxiaodong.library.chart.line.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.example.sunxiaodong.library.base.model.Viewport;
import com.example.sunxiaodong.library.base.renderer.AbstractChartRenderer;
import com.example.sunxiaodong.library.base.view.Chart;
import com.example.sunxiaodong.library.chart.line.model.Line;
import com.example.sunxiaodong.library.chart.line.model.LineChartData;
import com.example.sunxiaodong.library.chart.line.model.PointValue;
import com.example.sunxiaodong.library.chart.line.model.provider.LineChartDataProvider;

import java.util.List;

/**
 * 线型图表绘制渲染器
 * Created by sunxiaodong on 16/6/27.
 */
public class LineChartRenderer extends AbstractChartRenderer {

    private static final int DEFAULT_TOUCH_TOLERANCE_MARGIN_PX = 12;

    private LineChartDataProvider lineChartDataProvider;

    private Paint linePaint = new Paint();//线条绘制画笔
    private Paint pointPaint = new Paint();//点绘制画笔
    protected Paint pointLabelPaint = new Paint();//点标签画笔
    private Path path = new Path();//线条路径

    protected char[] pointLabelBuffer = new char[64];//点标签缓存
    protected Paint.FontMetricsInt labelFontMetrics = new Paint.FontMetricsInt();

    protected boolean isViewportCalculationEnabled = true;//当图表数据改变时，是否重新计算视窗

    public LineChartRenderer(Chart chart, LineChartDataProvider lineChartDataProvider) {
        super(chart);
        this.lineChartDataProvider = lineChartDataProvider;
        initLinePaint();
        initPointPaint();
        initPointLabelPaint();
    }

    /**
     * 初始化线条画笔
     */
    private void initLinePaint() {
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 初始化点画笔
     */
    private void initPointPaint() {
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 初始化点标签画笔
     */
    private void initPointLabelPaint() {
        pointLabelPaint.setAntiAlias(true);
        pointLabelPaint.setStyle(Paint.Style.FILL);
        pointLabelPaint.setTextAlign(Paint.Align.LEFT);
        pointLabelPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pointLabelPaint.setColor(Color.WHITE);
    }

    private int calculateContentRectInternalMargin() {
        int contentAreaMargin = 0;
        final LineChartData data = lineChartDataProvider.getLineChartData();
        for (Line line : data.getLines()) {
            if (line.isHasPoints() && line.getPoints().size() > 0) {
                int margin = line.getPointRadius() + DEFAULT_TOUCH_TOLERANCE_MARGIN_PX;
                if (margin > contentAreaMargin) {
                    contentAreaMargin = margin;
                }
            }
        }
        return contentAreaMargin;
    }

    @Override
    public void onChartSizeChanged() {
        final int internalMargin = calculateContentRectInternalMargin();
        chartComputer.insetContentRectByInternalMargins(internalMargin, internalMargin, internalMargin, internalMargin);
    }

    @Override
    public void onChartDataChanged() {
        final int internalMargin = calculateContentRectInternalMargin();
        chartComputer.insetContentRectByInternalMargins(internalMargin, internalMargin, internalMargin, internalMargin);

        onChartViewportChanged();
    }

    @Override
    public void onChartViewportChanged() {
        if (isViewportCalculationEnabled) {
            chartComputer.setMaxViewport(calculateMaxViewport());
            chartComputer.setCurrentViewport(chartComputer.getMaxViewport());
        }
    }

    /**
     * 计算最大视窗
     * @return
     */
    private Viewport calculateMaxViewport() {
        Viewport tempMaxViewport = new Viewport();
        tempMaxViewport.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE);
        LineChartData data = lineChartDataProvider.getLineChartData();

        for (Line line : data.getLines()) {
            for (PointValue pointValue : line.getPoints()) {
                if (pointValue.getX() < tempMaxViewport.left) {
                    tempMaxViewport.left = pointValue.getX();
                }
                if (pointValue.getX() > tempMaxViewport.right) {
                    tempMaxViewport.right = pointValue.getX();
                }
                if (pointValue.getY() < tempMaxViewport.bottom) {
                    tempMaxViewport.bottom = pointValue.getY();
                }
                if (pointValue.getY() > tempMaxViewport.top) {
                    tempMaxViewport.top = pointValue.getY();
                }

            }
        }
        return tempMaxViewport;
    }

    @Override
    public void resetRenderer() {
        chartComputer = chart.getChartComputer();
    }

    /**
     * 绘制线
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        final LineChartData data = lineChartDataProvider.getLineChartData();
        for (Line line : data.getLines()) {
            if (line.isHasLines()) {
//                if (line.isCubic()) {
//                    drawSmoothPath(canvas, line);
//                } else if (line.isSquare()) {
//                    drawSquarePath(canvas, line);
//                } else {
//                    drawSimplePath(canvas, line);
//                }
                drawSimplePath(canvas, line);
            }
        }
    }

    /**
     * 绘制简单线
     * @param canvas
     * @param line
     */
    private void drawSimplePath(Canvas canvas, final Line line) {
        prepareLinePaint(line);

        int valueIndex = 0;
        for (PointValue pointValue : line.getPoints()) {
            final float rawX = chartComputer.computeRawX(pointValue.getX());
            final float rawY = chartComputer.computeRawY(pointValue.getY());
            if (valueIndex == 0) {
                path.moveTo(rawX, rawY);
            } else {
                path.lineTo(rawX, rawY);
            }
            ++valueIndex;
        }

        canvas.drawPath(path, linePaint);

        if (line.isFilled()) {
            drawArea(canvas, line);
        }

        path.reset();
    }

    /**
     * 设置线条绘制画笔
     * @param line
     */
    private void prepareLinePaint(final Line line) {
        linePaint.setStrokeWidth(line.getLineWidth());
        linePaint.setColor(line.getLineColor());
        linePaint.setPathEffect(line.getPathEffect());
    }

    /**
     * 绘制区域填充
     * @param canvas
     * @param line
     */
    private void drawArea(Canvas canvas, Line line) {
        List<PointValue> linePoints = line.getPoints();
        final int linePointsNum = linePoints.size();
        if (linePointsNum < 2) {
            return;
        }

        final Rect contentRect = chartComputer.getContentRectMinusAllMargins();
        final float baseRawValue = Math.max(contentRect.bottom, contentRect.top);
        final float left = Math.max(chartComputer.computeRawX(linePoints.get(0).getX()), contentRect.left);
        final float right = Math.min(chartComputer.computeRawX(linePoints.get(linePointsNum - 1).getX()), contentRect.right);

        path.lineTo(right, baseRawValue);
        path.lineTo(left, baseRawValue);
        path.close();

        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAlpha(line.getFillAlpha());
        canvas.drawPath(path, linePaint);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 绘制点和标签
     * @param canvas
     */
    @Override
    public void drawUnClipped(Canvas canvas) {
        final LineChartData data = lineChartDataProvider.getLineChartData();
        for (Line line : data.getLines()) {
            if (line.isHasPoints() && line.getPoints().size() > 0) {
                drawPoints(canvas, line);
            }
        }
    }

    /**
     * 绘制线条上的点
     * @param canvas
     * @param line
     */
    private void drawPoints(Canvas canvas, Line line) {
        pointPaint.setColor(line.getPointColor());
        for (PointValue pointValue : line.getPoints()) {
            final float rawX = chartComputer.computeRawX(pointValue.getX());
            final float rawY = chartComputer.computeRawY(pointValue.getY());
            if (chartComputer.isInContentRect(rawX, rawY)) {
                drawPoint(canvas, line, pointValue, rawX, rawY, line.getPointRadius());
            }
        }
    }

    /**
     * 绘制具体形状的单个点
     * @param canvas
     * @param line
     * @param pointValue
     * @param rawX
     * @param rawY
     * @param pointRadius
     */
    private void drawPoint(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, float pointRadius) {
        if (PointValue.Shape.SQUARE.equals(line.getPointShape())) {
            canvas.drawRect(rawX - pointRadius, rawY - pointRadius, rawX + pointRadius, rawY + pointRadius, pointPaint);
        } else if (PointValue.Shape.CIRCLE.equals(line.getPointShape())) {
            canvas.drawCircle(rawX, rawY, pointRadius, pointPaint);
        } else if (PointValue.Shape.DIAMOND.equals(line.getPointShape())) {
            canvas.save();
            canvas.rotate(45, rawX, rawY);
            canvas.drawRect(rawX - pointRadius, rawY - pointRadius, rawX + pointRadius, rawY + pointRadius, pointPaint);
            canvas.restore();
        } else {
            throw new IllegalArgumentException("Invalid point shape: " + line.getPointShape());
        }
        if (line.isHasPointLabel()) {
            drawLabel(canvas, line, pointValue, rawX, rawY, pointRadius + line.getPointLabelOffset());
        }
    }

    /**
     * 绘制点上的标签
     * @param canvas
     * @param line
     * @param pointValue
     * @param rawX
     * @param rawY
     * @param offset
     */
    private void drawLabel(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, float offset) {
        prepareLabelPaint(line);
        final Rect contentRect = chartComputer.getContentRectMinusAllMargins();
        final int numChars = line.getLabelFormatter().formatChartValue(pointLabelBuffer, pointValue);
        if (numChars == 0) {
            return;
        }

        final float labelWidth = pointLabelPaint.measureText(pointLabelBuffer, pointLabelBuffer.length - numChars, numChars);
        final int labelHeight = Math.abs(labelFontMetrics.ascent);
        float left = rawX - labelWidth / 2 - line.getPointLabelOffset();
        float right = rawX + labelWidth / 2 + line.getPointLabelOffset();

        float top;
        float bottom;

//        if (pointValue.getY() >= baseValue) {
//            top = rawY - offset - labelHeight - line.getPointLabelOffset() * 2;
//            bottom = rawY - offset;
//        } else {
//            top = rawY + offset;
//            bottom = rawY + offset + labelHeight + line.getPointLabelOffset() * 2;
//        }

        top = rawY - offset - labelHeight - line.getPointLabelOffset() * 2;
        bottom = rawY - offset;

        if (top < contentRect.top) {
            top = rawY + offset;
            bottom = rawY + offset + labelHeight + line.getPointLabelOffset() * 2;
        }
        if (bottom > contentRect.bottom) {
            top = rawY - offset - labelHeight - line.getPointLabelOffset() * 2;
            bottom = rawY - offset;
        }
        if (left < contentRect.left) {
            left = rawX;
            right = rawX + labelWidth + line.getPointLabelOffset() * 2;
        }
        if (right > contentRect.right) {
            left = rawX - labelWidth - line.getPointLabelOffset() * 2;
            right = rawX;
        }
        canvas.drawText(pointLabelBuffer, pointLabelBuffer.length - numChars, numChars, left, bottom, pointLabelPaint);
    }

    private void prepareLabelPaint(Line line) {
        pointLabelPaint.getFontMetricsInt(labelFontMetrics);
        pointLabelPaint.setColor(line.getPointLabelColor());
        pointLabelPaint.setTextSize(line.getPointLabelTextSize());
    }

}
