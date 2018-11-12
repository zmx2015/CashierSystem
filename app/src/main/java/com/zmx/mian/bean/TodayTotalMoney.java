package com.zmx.mian.bean;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-12 21:15
 * 类功能：根据每日来统计每天的营业额
 */

public class TodayTotalMoney {

    private String today;//日期
    private float money;//金额

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
