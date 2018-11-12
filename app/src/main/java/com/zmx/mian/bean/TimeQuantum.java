package com.zmx.mian.bean;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-07 16:32
 * 类功能：统计每个时间段的人次和金额
 */

public class TimeQuantum {

    private int time;//小时
    private int  man_time;//人次
    private float total_money;//金额

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMan_time() {
        return man_time;
    }

    public void setMan_time(int man_time) {
        this.man_time = man_time;
    }

    public float getTotal_money() {
        return total_money;
    }

    public void setTotal_money(float total_money) {
        this.total_money = total_money;
    }
}
