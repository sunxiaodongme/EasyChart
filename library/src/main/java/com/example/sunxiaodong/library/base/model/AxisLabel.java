package com.example.sunxiaodong.library.base.model;

import java.util.Arrays;

/**
 * 坐标轴标签数据单元
 * Created by sunxiaodong on 16/6/25.
 */
public class AxisLabel {

    private float value;//标签位置
    private char[] label;//标签值

    public AxisLabel(float value) {
        setValue(value);
    }

    @Deprecated
    public AxisLabel(float value, char[] label) {
        this.value = value;
        this.label = label;
    }

    public AxisLabel(AxisLabel axisValue) {
        this.value = axisValue.value;
        this.label = axisValue.label;
    }

    public float getValue() {
        return value;
    }

    public AxisLabel setValue(float value) {
        this.value = value;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return label;
    }

    /**
     * Set custom label for this axis value.
     *
     * @param label
     */
    public AxisLabel setLabel(String label) {
        this.label = label.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return label;
    }

    /**
     * Set custom label for this axis value.
     *
     * @param label
     */
    @Deprecated
    public AxisLabel setLabel(char[] label) {
        this.label = label;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AxisLabel axisValue = (AxisLabel) o;

        if (Float.compare(axisValue.value, value) != 0) return false;
        if (!Arrays.equals(label, axisValue.label)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (value != +0.0f ? Float.floatToIntBits(value) : 0);
        result = 31 * result + (label != null ? Arrays.hashCode(label) : 0);
        return result;
    }

}
