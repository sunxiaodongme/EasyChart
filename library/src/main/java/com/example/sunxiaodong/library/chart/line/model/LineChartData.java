package com.example.sunxiaodong.library.chart.line.model;

import com.example.sunxiaodong.library.base.model.AbstractChartData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunxiaodong on 16/6/25.
 */
public class LineChartData extends AbstractChartData {

    private List<Line> lines = new ArrayList<Line>();

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

}
