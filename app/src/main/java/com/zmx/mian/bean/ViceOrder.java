package com.zmx.mian.bean;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-07-29 15:03
 * 类功能：订单详情
 */
public class ViceOrder implements Serializable {

    private int order_id;// 关联订单的id
    private int goods_id;// 订单商品关联的id
    private String img;
    private String name;
    private String weight;// 单品的重量
    private String price;// 单品的单价
    private String subtotal;// 单品的小计
    private int type;// 单品称重的类型0重量1个数

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
