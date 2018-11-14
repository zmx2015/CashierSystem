package com.zmx.mian.bean;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class Goods implements Serializable {

    static final long serialVersionUID = -15515456L;//解决实现Serializable接口不能编译通过的问题
    @Unique
    private String g_id;
    private String g_img;
    private String g_price;
    private String g_name;
    private String cp_name;
    private String cp_group;
    private String rowCount;
    private String vip_g_price;
    private String mall_state;//门店上架下架1上架，0下架
    private String store_state;//商城上架和下架

    public Goods(String g_id, String g_img, String g_price, String g_name,
                 String cp_name, String cp_group,String vip_g_price,String mall_state,String store_state) {
        super();
        this.g_id = g_id;
        this.g_img = g_img;
        this.g_price = g_price;
        this.g_name = g_name;
        this.cp_name = cp_name;
        this.cp_group = cp_group;
        this.vip_g_price = vip_g_price;
        this.mall_state = mall_state;
        this.store_state = store_state;
    }

    @Generated(hash = 933231250)
    public Goods(String g_id, String g_img, String g_price, String g_name, String cp_name,
            String cp_group, String rowCount, String vip_g_price, String mall_state,
            String store_state) {
        this.g_id = g_id;
        this.g_img = g_img;
        this.g_price = g_price;
        this.g_name = g_name;
        this.cp_name = cp_name;
        this.cp_group = cp_group;
        this.rowCount = rowCount;
        this.vip_g_price = vip_g_price;
        this.mall_state = mall_state;
        this.store_state = store_state;
    }

    @Generated(hash = 1770709345)
    public Goods() {
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getG_img() {
        return g_img;
    }

    public void setG_img(String g_img) {
        this.g_img = g_img;
    }

    public String getG_price() {
        return g_price;
    }

    public void setG_price(String g_price) {
        this.g_price = g_price;
    }

    public String getG_name() {
        return g_name;
    }

    public void setG_name(String g_name) {
        this.g_name = g_name;
    }

    public String getCp_name() {
        return cp_name;
    }

    public void setCp_name(String cp_name) {
        this.cp_name = cp_name;
    }

    public String getCp_group() {
        return cp_group;
    }

    public void setCp_group(String cp_group) {
        this.cp_group = cp_group;
    }

    public String getRowCount() {
        return rowCount;
    }

    public void setRowCount(String rowCount) {
        this.rowCount = rowCount;
    }

    public String getVip_g_price() {
        return vip_g_price;
    }

    public void setVip_g_price(String vip_g_price) {
        this.vip_g_price = vip_g_price;
    }

    public String getMall_state() {
        return this.mall_state;
    }

    public void setMall_state(String mall_state) {
        this.mall_state = mall_state;
    }

    public String getStore_state() {
        return this.store_state;
    }

    public void setStore_state(String store_state) {
        this.store_state = store_state;
    }
}