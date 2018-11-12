package com.zmx.mian.bean.members;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-02 22:18
 * 类功能：签到记录
 */
public class MembersPrize implements Serializable {

    private long prizetime;
    private int outcome;
    private int integral;

    public void setPrizetime(long prizetime) {
        this.prizetime = prizetime;
    }
    public long getPrizetime() {
        return prizetime;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }
    public int getOutcome() {
        return outcome;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }
    public int getIntegral() {
        return integral;
    }

}
