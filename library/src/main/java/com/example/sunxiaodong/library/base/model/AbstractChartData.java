package com.example.sunxiaodong.library.base.model;

/**
 * 抽象的图表数据单元，提供基础的图表数据
 * Created by sunxiaodong on 16/6/25.
 */
public abstract class AbstractChartData implements ChartData {

    protected Axis axisXBottom;
    protected Axis axisYLeft;
    protected Axis axisXTop;
    protected Axis axisYRight;

    @Override
    public Axis getAxisXBottom() {
        return axisXBottom;
    }

    public void setAxisXBottom(Axis axisXBottom) {
        this.axisXBottom = axisXBottom;
    }

    @Override
    public Axis getAxisYLeft() {
        return axisYLeft;
    }

    public void setAxisYLeft(Axis axisYLeft) {
        this.axisYLeft = axisYLeft;
    }

    @Override
    public Axis getAxisXTop() {
        return axisXTop;
    }

    public void setAxisXTop(Axis axisXTop) {
        this.axisXTop = axisXTop;
    }

    @Override
    public Axis getAxisYRight() {
        return axisYRight;
    }

    public void setAxisYRight(Axis axisYRight) {
        this.axisYRight = axisYRight;
    }

}
