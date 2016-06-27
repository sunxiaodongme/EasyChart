package com.example.sunxiaodong.library.chart.line.view;

import android.content.Context;
import android.util.AttributeSet;

import com.example.sunxiaodong.library.base.model.ChartData;
import com.example.sunxiaodong.library.base.view.AbstractChartView;
import com.example.sunxiaodong.library.chart.line.model.LineChartData;
import com.example.sunxiaodong.library.chart.line.model.provider.LineChartDataProvider;
import com.example.sunxiaodong.library.chart.line.renderer.LineChartRenderer;

/**
 * 线状图
 * Created by sunxiaodong on 16/6/25.
 */
public class LineChartView extends AbstractChartView implements LineChartDataProvider {

    protected LineChartData lineChartData;

    public LineChartView(Context context) {
        super(context);
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setChartRenderer(new LineChartRenderer(this, this));
    }

    @Override
    public ChartData getChartData() {
        return lineChartData;
    }

    @Override
    public LineChartData getLineChartData() {
        return lineChartData;
    }

    @Override
    public void setLineChartData(LineChartData lineChartData) {
        this.lineChartData = lineChartData;
        super.onChartDataChange();
    }

}
