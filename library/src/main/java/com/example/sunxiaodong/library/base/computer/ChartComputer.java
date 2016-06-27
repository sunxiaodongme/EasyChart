package com.example.sunxiaodong.library.base.computer;

import android.graphics.Rect;

import com.example.sunxiaodong.library.base.listener.ViewportChangeListener;
import com.example.sunxiaodong.library.base.model.Viewport;

/**
 * 图表计算器
 * Created by sunxiaodong on 16/6/24.
 */
public class ChartComputer {

    protected static final float DEFAULT_MAX_ZOOM = 20f;//默认最大缩放比
    protected float currentZoom = DEFAULT_MAX_ZOOM;//当前缩放比

    /**
     * 屏幕坐标系数据
     */
    protected int chartWidth;//图表宽度，px，屏幕显示大小
    protected int chartHeight;//图表高度
    protected Rect maxContentRect = new Rect();//最大内容矩形
    protected Rect contentRectMinusAllMargins = new Rect();//减去所有边距的内容矩形
    protected Rect contentRectMinusAxesMargins = new Rect();//减去坐标轴边距的内容矩形

    /**
     * 图表逻辑坐标系数据
     */
    protected Viewport currentViewport = new Viewport();//当前视窗，图表当前显示逻辑坐标系部分
    protected Viewport maxViewport = new Viewport();//最大视窗，显示最大逻辑坐标系
    protected float minViewportWidth;//最小视窗宽度（图表逻辑坐标系横坐标差）
    protected float minViewportHeight;//最小的视窗高度（图表逻辑坐标系纵坐标差）

    protected ViewportChangeListener viewportChangeListener;//视窗改变回调

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

    /**
     * 根据最大，最小视窗情况，计算当前需显示视窗
     */
    private void constrainViewport(float left, float top, float right, float bottom) {
        if (right - left < minViewportWidth) {
            right = left + minViewportWidth;
            if (left < maxViewport.left) {
                left = maxViewport.left;
                right = left + minViewportWidth;
            } else if (right > maxViewport.right) {
                right = maxViewport.right;
                left = right - minViewportWidth;
            }
        }

        if (top - bottom < minViewportHeight) {
            bottom = top - minViewportHeight;
            if (top > maxViewport.top) {
                top = maxViewport.top;
                bottom = top - minViewportHeight;
            } else if (bottom < maxViewport.bottom) {
                bottom = maxViewport.bottom;
                top = bottom + minViewportHeight;
            }
        }

        currentViewport.left = Math.max(maxViewport.left, left);
        currentViewport.top = Math.min(maxViewport.top, top);
        currentViewport.right = Math.min(maxViewport.right, right);
        currentViewport.bottom = Math.max(maxViewport.bottom, bottom);

        if (viewportChangeListener != null) {
            viewportChangeListener.onViewportChanged(currentViewport);
        }

    }

    /**
     * 设置当前视窗
     * @param viewport
     */
    public void setCurrentViewport(Viewport viewport) {
        constrainViewport(viewport.left, viewport.top, viewport.right, viewport.bottom);
    }

    /**
     * 设置当前视窗
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setCurrentViewport(float left, float top, float right, float bottom) {
        constrainViewport(left, top, right, bottom);
    }

    public Viewport getCurrentViewport() {
        return currentViewport;
    }

    /**
     * 设置最大视窗
     *
     * @param maxViewport
     */
    public void setMaxViewport(Viewport maxViewport) {
        setMaxViewport(maxViewport.left, maxViewport.top, maxViewport.right, maxViewport.bottom);
    }

    /**
     * 设置最大视窗
     */
    private void setMaxViewport(float left, float top, float right, float bottom) {
        this.maxViewport.set(left, top, right, bottom);
        computeMinWidthAndHeight();
    }

    public Viewport getMaxViewport() {
        return maxViewport;
    }

    /**
     * 根据最大视窗算出最小视窗，只在确定最大视窗后，计算完成，之后缩放不再进行计算
     */
    private void computeMinWidthAndHeight() {
        minViewportWidth = this.maxViewport.width() / currentZoom;
        minViewportHeight = this.maxViewport.height() / currentZoom;
    }

    public float getCurrentZoom() {
        return currentZoom;
    }

    public void setCurrentZoom(float currentZoom) {
        this.currentZoom = currentZoom;
        computeMinWidthAndHeight();
        setCurrentViewport(currentViewport);
    }

    public ViewportChangeListener getViewportChangeListener() {
        return viewportChangeListener;
    }

    public void setViewportChangeListener(ViewportChangeListener viewportChangeListener) {
        this.viewportChangeListener = viewportChangeListener;
    }

}
