package com.example.sunxiaodong.library.base.renderer;

import android.graphics.Canvas;

/**
 *
 * 图表渲染器
 *
 * Created by sunxiaodong on 16/6/27.
 */
public interface ChartRenderer {

    void onChartSizeChanged();
    void onChartDataChanged();
    void resetRenderer();

    void draw(Canvas canvas);
    void drawUnClipped(Canvas canvas);

}
