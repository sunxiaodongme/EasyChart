package com.example.sunxiaodong.library.base.computer;

import android.graphics.Rect;

import com.example.sunxiaodong.library.base.model.Viewport;

/**
 * 图表计算器
 * Created by sunxiaodong on 16/6/24.
 */
public class ChartComputer {

    protected int chartWidth;//图表宽度，px，屏幕显示大小
    protected int chartHeight;//图表高度
    protected Rect maxContentRect = new Rect();//最大内容矩形
    protected Rect contentRectMinusAllMargins = new Rect();//减去所有边距的内容矩形
    protected Rect contentRectMinusAxesMargins = new Rect();//减去坐标轴边距的内容矩形

    protected Viewport currentViewport = new Viewport();//当前视窗

    /**
     * 设置内容矩形大小
     * @param width
     * @param height
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    public void setContentRect(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        chartWidth = width;
        chartHeight = height;
        maxContentRect.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        contentRectMinusAxesMargins.set(maxContentRect);
        contentRectMinusAllMargins.set(maxContentRect);
    }

    /**
     * 生成去除“轴边距”的内容矩形
     * @param deltaLeft
     * @param deltaTop
     * @param deltaRight
     * @param deltaBottom
     */
    public void insetContentRect(int deltaLeft, int deltaTop, int deltaRight, int deltaBottom) {
        contentRectMinusAxesMargins.left = contentRectMinusAxesMargins.left + deltaLeft;
        contentRectMinusAxesMargins.top = contentRectMinusAxesMargins.top + deltaTop;
        contentRectMinusAxesMargins.right = contentRectMinusAxesMargins.right - deltaRight;
        contentRectMinusAxesMargins.bottom = contentRectMinusAxesMargins.bottom - deltaBottom;

        insetContentRectByInternalMargins(deltaLeft, deltaTop, deltaRight, deltaBottom);
    }

    /**
     * 生成去除“轴边距”和“轴内边距”的内容矩形
     * @param deltaLeft
     * @param deltaTop
     * @param deltaRight
     * @param deltaBottom
     */
    public void insetContentRectByInternalMargins(int deltaLeft, int deltaTop, int deltaRight, int deltaBottom) {
        contentRectMinusAllMargins.left = contentRectMinusAllMargins.left + deltaLeft;
        contentRectMinusAllMargins.top = contentRectMinusAllMargins.top + deltaTop;
        contentRectMinusAllMargins.right = contentRectMinusAllMargins.right - deltaRight;
        contentRectMinusAllMargins.bottom = contentRectMinusAllMargins.bottom - deltaBottom;
    }

    public Rect getContentRectMinusAllMargins() {
        return contentRectMinusAllMargins;
    }

    public Rect getContentRectMinusAxesMargins() {
        return contentRectMinusAxesMargins;
    }

    public Viewport getVisibleViewport() {
        return currentViewport;
    }

    /**
     * 将点的横坐标值，转换为屏幕上X的位置（从图表逻辑坐标系转换到屏幕绘图坐标系）
     */
    public float computeRawX(float valueX) {
        final float pixelOffset = (valueX - currentViewport.left) * (contentRectMinusAllMargins.width() / currentViewport.width());
        return contentRectMinusAllMargins.left + pixelOffset;
    }

    /**
     *将点的纵坐标值，转换为屏幕上Y的位置（从图表逻辑坐标系转换到屏幕绘图坐标系）
     */
    public float computeRawY(float valueY) {
        final float pixelOffset = (valueY - currentViewport.bottom) * (contentRectMinusAllMargins.height() / currentViewport.height());
        return contentRectMinusAllMargins.bottom - pixelOffset;
    }

}
