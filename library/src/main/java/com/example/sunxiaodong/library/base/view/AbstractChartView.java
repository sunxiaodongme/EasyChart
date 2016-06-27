package com.example.sunxiaodong.library.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.sunxiaodong.library.base.computer.ChartComputer;
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

}
