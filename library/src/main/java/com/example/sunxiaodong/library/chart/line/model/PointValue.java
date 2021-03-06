package com.example.sunxiaodong.library.chart.line.model;

import java.util.Arrays;

/**
 * 点数据
 * Created by sunxiaodong on 16/6/27.
 */
public class PointValue {

    /**
     * 点形状
     */
    public enum Shape {
        CIRCLE,//圆形
        SQUARE, //方形
        DIAMOND//菱形
    }

    /**
     * 当前位置点
     */
    private float x;
    private float y;
    /**
     * 原始点，用来记录动画起始点
     */
    private float originX;//原始点x坐标
    private float originY;//原始点y坐标

    /**
     * 数据变化，变化间隔
     */
    private float diffX;//变化范围x距离
    private float diffY;//变化范围y距离

    private char[] label;//点上的标签

    public PointValue() {
        set(0, 0);
    }

    public PointValue(float x, float y) {
        set(x, y);
    }

    public PointValue(PointValue pointValue) {
        set(pointValue.x, pointValue.y);
        this.label = pointValue.label;
    }

    public void update(float scale) {
        x = originX + diffX * scale;
        y = originY + diffY * scale;
    }

    public void finish() {
        set(originX + diffX, originY + diffY);
    }

    public PointValue set(float x, float y) {
        this.x = x;
        this.y = y;
        this.originX = x;
        this.originY = y;
        this.diffX = 0;
        this.diffY = 0;
        return this;
    }

    public PointValue setTarget(float targetX, float targetY) {
        set(x, y);
        this.diffX = targetX - originX;
        this.diffY = targetY - originY;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Deprecated
    public char[] getLabel() {
        return label;
    }

    public PointValue setLabel(String label) {
        this.label = label.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return label;
    }

    @Deprecated
    public PointValue setLabel(char[] label) {
        this.label = label;
        return this;
    }

    @Override
    public String toString() {
        return "PointValue [x=" + x + ", y=" + y + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointValue that = (PointValue) o;

        if (Float.compare(that.diffX, diffX) != 0) return false;
        if (Float.compare(that.diffY, diffY) != 0) return false;
        if (Float.compare(that.originX, originX) != 0) return false;
        if (Float.compare(that.originY, originY) != 0) return false;
        if (Float.compare(that.x, x) != 0) return false;
        if (Float.compare(that.y, y) != 0) return false;
        if (!Arrays.equals(label, that.label)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (originX != +0.0f ? Float.floatToIntBits(originX) : 0);
        result = 31 * result + (originY != +0.0f ? Float.floatToIntBits(originY) : 0);
        result = 31 * result + (diffX != +0.0f ? Float.floatToIntBits(diffX) : 0);
        result = 31 * result + (diffY != +0.0f ? Float.floatToIntBits(diffY) : 0);
        result = 31 * result + (label != null ? Arrays.hashCode(label) : 0);
        return result;
    }

}
