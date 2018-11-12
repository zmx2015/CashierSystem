package com.zmx.mian.bean.members;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-02 23:18
 * 类功能：兑换记录
 */
public class MembersExchange implements Serializable {

    private long prizetime;
    private int integral;
    public void setPrizetime(long prizetime) {
        this.prizetime = prizetime;
    }
    public long getPrizetime() {
        return prizetime;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }
    public int getIntegral() {
        return integral;
    }

}
