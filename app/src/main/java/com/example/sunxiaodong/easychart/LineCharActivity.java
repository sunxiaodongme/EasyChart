package com.example.sunxiaodong.easychart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.sunxiaodong.library.base.model.Axis;
import com.example.sunxiaodong.library.chart.line.model.LineChartData;
import com.example.sunxiaodong.library.chart.line.view.LineChartView;

/**
 * Created by sunxiaodong on 16/6/25.
 */
public class LineCharActivity extends AppCompatActivity {

    private LineChartView lineChartView;
    private LineChartData lineChartData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        initView();
    }

    private void initView() {
        lineChartView = (LineChartView) findViewById(R.id.line_chart_view);
        initData();
//        resetViewport();
    }

    private void initData() {
        lineChartData = new LineChartData();
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasGridLines(true);
        axisX.setAxisName("Axis X");
        axisY.setAxisName("Axis Y");
        lineChartData.setAxisXBottom(axisX);
        lineChartData.setAxisYLeft(axisY);
        lineChartView.setLineChartData(lineChartData);
    }

//    private void resetViewport() {
//        //设置窗口高度范围（0 - 100），宽度（0 - 12）
//        Viewport viewport = new Viewport(lineChartView.getMaxViewport());
//        viewport.bottom = 0;
//        viewport.top = 100;
//        viewport.left = 0;
//        viewport.right = 12;
//        lineChartView.setMaxViewport(viewport);
//        lineChartView.setCurrentViewport(viewport);
//    }

}
