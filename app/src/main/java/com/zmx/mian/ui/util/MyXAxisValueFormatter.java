package com.zmx.mian.ui.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-03 20:20
 * 类功能：图表用到的
 */
public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private List<String> mValues;

    public MyXAxisValueFormatter(List<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int x=(int)(value);
        if (x<0)
            x=0;
        if (x>=mValues.size())
            x=mValues.size()-1;
        return mValues.get(x);
    }
}
