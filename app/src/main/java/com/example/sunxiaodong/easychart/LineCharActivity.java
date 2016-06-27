package com.example.sunxiaodong.easychart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.sunxiaodong.library.base.model.Axis;
import com.example.sunxiaodong.library.base.model.Viewport;
import com.example.sunxiaodong.library.chart.line.model.Line;
import com.example.sunxiaodong.library.chart.line.model.LineChartData;
import com.example.sunxiaodong.library.chart.line.model.PointValue;
import com.example.sunxiaodong.library.chart.line.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

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
        resetViewport();
    }

    private void initData() {
        generateValues();
        generateData();
//        lineChartData = new LineChartData();
//        Axis axisX = new Axis();
//        Axis axisY = new Axis().setHasGridLines(true);
//        axisX.setAxisName("Axis X");
//        axisY.setAxisName("Axis Y");
//        lineChartData.setAxisXBottom(axisX);
//        lineChartData.setAxisYLeft(axisY);
//        lineChartView.setLineChartData(lineChartData);
    }

    private int numberOfLines = 1;//线数量
    private int maxNumberOfLines = 4;//最大的线数量
    private int numberOfPoints = 12;//点数

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];//点数据集合

    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }

    private void generateData() {
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line();
            line.setPoints(values);

            line.setLineColor(Color.parseColor("#33B5E5"));
            line.setPointShape(PointValue.Shape.CIRCLE);
//            line.setCubic(isCubic);
            line.setFilled(true);
            line.setHasPointLabel(true);
//            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(true);
            line.setHasPoints(true);
            line.setPointColor(Color.parseColor("#33B5E5"));
            lines.add(line);
        }

        lineChartData = new LineChartData();
        lineChartData.setLines(lines);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasGridLines(true);
        axisX.setAxisName("Axis X");
        axisY.setAxisName("Axis Y");
        lineChartData.setAxisXBottom(axisX);
        lineChartData.setAxisYLeft(axisY);

        lineChartView.setLineChartData(lineChartData);

    }

    private void resetViewport() {
        //设置窗口高度范围（0 - 100），宽度（0 - 12）
        Viewport viewport = new Viewport(lineChartView.getMaxViewport());
        viewport.bottom = 0;
        viewport.top = 100;
        viewport.left = 0;
        viewport.right = 12;
        lineChartView.setMaxViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }

}
