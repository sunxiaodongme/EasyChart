package com.example.sunxiaodong.library.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.sunxiaodong.library.base.animator.ChartDataAnimator;
import com.example.sunxiaodong.library.base.animator.SimpleChartDataAnimatorV14;
import com.example.sunxiaodong.library.base.animator.SimpleChartDataAnimatorV8;
import com.example.sunxiaodong.library.base.computer.ChartComputer;
import com.example.sunxiaodong.library.base.listener.ChartAnimationListener;
import com.example.sunxiaodong.library.base.model.Viewport;
import com.example.sunxiaodong.library.base.renderer.AxesRenderer;
import com.example.sunxiaodong.library.base.renderer.ChartRenderer;

/**
 * 抽象的图表视图，用于所有图形的基础绘制
 * Created by sunxiaodong on 16/6/25.
 */
public abstract class AbstractChartView extends View implements Chart {

    protected ChartComputer chartComputer;
    protected AxesRenderer axesRenderer;
    protected ChartRenderer chartRenderer;
    protected ChartDataAnimator chartDataAnimator;

    public AbstractChartView(Context context) {
        super(context);
        init(context);
    }

    public AbstractChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbstractChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        chartComputer = new ChartComputer();
        axesRenderer = new AxesRenderer(context, this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            chartDataAnimator = new SimpleChartDataAnimatorV8(this);
        } else {
            chartDataAnimator = new SimpleChartDataAnimatorV14(this);
        }
    }

    @Override
    public ChartComputer getChartComputer() {
        return chartComputer;
    }

    @Override
    public ChartRenderer getChartRenderer() {
        return chartRenderer;
    }

    @Override
    public void setChartRenderer(ChartRenderer renderer) {
        chartRenderer = renderer;
        chartRenderer.resetRenderer();
        axesRenderer.resetRenderer();
//        touchHandler.resetTouchHandler();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chartComputer.setContentRect(getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        chartRenderer.onChartSizeChanged();
        axesRenderer.onChartSizeChanged();
    }

    @Override
    public void onChartDataChange() {
        chartRenderer.resetRenderer();
        chartRenderer.onChartDataChanged();
        axesRenderer.resetRenderer();
//        this.touchHandler.resetTouchHandler();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnabled()) {
            axesRenderer.drawBackground(canvas);

            //TODO 图表绘制

            int clipRestoreCount = canvas.save();
            canvas.clipRect(chartComputer.getContentRectMinusAllMargins());
            chartRenderer.draw(canvas);
            canvas.restoreToCount(clipRestoreCount);
            chartRenderer.drawUnClipped(canvas);

            axesRenderer.drawForeground(canvas);
        } else {
            canvas.drawColor(Color.parseColor("#DFDFDF"));
        }
    }

    @Override
    public Viewport getMaxViewport() {
        return chartComputer.getMaxViewport();
    }

    @Override
    public void setMaxViewport(Viewport maxViewport) {
        chartComputer.setMaxViewport(maxViewport);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public Viewport getCurrentViewport() {
        return chartComputer.getCurrentViewport();
    }

    @Override
    public void setCurrentViewport(Viewport targetViewport) {
        if (null != targetViewport) {
            chartComputer.setCurrentViewport(targetViewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void startAnimation() {
        chartDataAnimator.startAnimation(Long.MIN_VALUE);
    }

    @Override
    public void startAnimation(long duration) {
        chartDataAnimator.startAnimation(duration);
    }

    @Override
    public void cancelAnimation() {
        chartDataAnimator.cancelAnimation();
    }

    @Override
    public void setChartAnimationListener(ChartAnimationListener chartAnimationListener) {
        chartDataAnimator.setChartAnimationListener(chartAnimationListener);
    }

    @Override
    public void dataAnimationUpdate(float scale) {
        getChartData().update(scale);
        chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void dataAnimationFinished() {
        getChartData().finish();
        chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

}
