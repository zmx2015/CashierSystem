package com.zmx.mian.bean;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-19 14:38
 * 类功能：插入订单用到
 */

public class ViceOrder_A implements Serializable {

    private String price;
    private String subtotal;
    private String name;
    private String goods_id;
    private String weight;

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getName() {
        return name;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public String getWeight() {
        return weight;
    }
}
