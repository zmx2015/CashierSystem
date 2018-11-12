package com.zmx.mian.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-21 15:54
 * 类功能：进货详情列表
 */
@Entity
public class StockManagementDetailsBean {

    @Id(autoincrement = true)
    public Long id;//表id
    public String g_id;//商品id
    public String g_name;//商品名称
    public String g_price;//商品单价
    public String g_total;//商品总价
    public String g_weight;//商品重量
    public String g_nb;//商品件数
    public String g_note;//商品备注
    public String g_color;//对商品标志的颜色值
    public String s_id;//关联的进货记录id
    public String supplier;//供应商
    public String g_payment_mode;//支付模式
    public String g_the_fare;//车费
    public String g_the_deposit;//押金
    public String number;//关联的编号
    public String unita;//计价单位 1为按件，2为重量
    @Generated(hash = 483229659)
    public StockManagementDetailsBean(Long id, String g_id, String g_name,
            String g_price, String g_total, String g_weight, String g_nb,
            String g_note, String g_color, String s_id, String supplier,
            String g_payment_mode, String g_the_fare, String g_the_deposit,
            String number, String unita) {
        this.id = id;
        this.g_id = g_id;
        this.g_name = g_name;
        this.g_price = g_price;
        this.g_total = g_total;
        this.g_weight = g_weight;
        this.g_nb = g_nb;
        this.g_note = g_note;
        this.g_color = g_color;
        this.s_id = s_id;
        this.supplier = supplier;
        this.g_payment_mode = g_payment_mode;
        this.g_the_fare = g_the_fare;
        this.g_the_deposit = g_the_deposit;
        this.number = number;
        this.unita = unita;
    }

    @Generated(hash = 823454073)
    public StockManagementDetailsBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getG_id() {
        return this.g_id;
    }
    public void setG_id(String g_id) {
        this.g_id = g_id;
    }
    public String getG_name() {
        return this.g_name;
    }
    public void setG_name(String g_name) {
        this.g_name = g_name;
    }
    public String getG_price() {
        return this.g_price;
    }
    public void setG_price(String g_price) {
        this.g_price = g_price;
    }
    public String getG_total() {
        return this.g_total;
    }
    public void setG_total(String g_total) {
        this.g_total = g_total;
    }
    public String getG_weight() {
        return this.g_weight;
    }
    public void setG_weight(String g_weight) {
        this.g_weight = g_weight;
    }
    public String getG_note() {
        return this.g_note;
    }
    public void setG_note(String g_note) {
        this.g_note = g_note;
    }
    public String getS_id() {
        return this.s_id;
    }
    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getG_color() {
        return this.g_color;
    }

    public void setG_color(String g_color) {
        this.g_color = g_color;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getG_nb() {
        return this.g_nb;
    }

    public void setG_nb(String g_nb) {
        this.g_nb = g_nb;
    }

    public String getG_payment_mode() {
        return this.g_payment_mode;
    }

    public void setG_payment_mode(String g_payment_mode) {
        this.g_payment_mode = g_payment_mode;
    }

    public String getG_the_fare() {
        return this.g_the_fare;
    }

    public void setG_the_fare(String g_the_fare) {
        this.g_the_fare = g_the_fare;
    }

    public String getG_the_deposit() {
        return this.g_the_deposit;
    }

    public void setG_the_deposit(String g_the_deposit) {
        this.g_the_deposit = g_the_deposit;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getUnita() {
        return this.unita;
    }

    public void setUnita(String unita) {
        this.unita = unita;
    }



}
