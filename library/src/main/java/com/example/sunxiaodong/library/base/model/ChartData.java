package com.example.sunxiaodong.library.base.model;

/**
 * 图标数据基础操作
 * Created by sunxiaodong on 16/6/24.
 */
public interface ChartData {

    Axis getAxisXTop();//获取顶部x轴

    Axis getAxisXBottom();//获取底部x轴

    Axis getAxisYLeft();//获取左边Y轴

    Axis getAxisYRight();//获取右边Y轴

}
