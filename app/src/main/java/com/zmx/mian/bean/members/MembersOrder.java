package com.zmx.mian.bean.members;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-02 23:19
 * 类功能：消费记录
 */
public class MembersOrder implements Serializable {

    private int id;
    private String buytime;
    private String total;
    private String discount;
    private String order;
    private int state;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setBuytime(String buytime) {
        this.buytime = buytime;
    }
    public String getBuytime() {
        return buytime;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    public String getTotal() {
        return total;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public String getDiscount() {
        return discount;
    }

    public void setOrder(String order) {
        this.order = order;
    }
    public String getOrder() {
        return order;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }


}
