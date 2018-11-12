package com.zmx.mian.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-26 22:21
 * 类功能：订单数据
 */

public class MainOrder implements Serializable{


    private int id;// 订单id
    private int uid;// 会员id
    private String order;// 订单标号
    private String total;// 订单总金额
    private String backmey;// 应找金额
    private String synchro;// 订单提交到云端时间
    private String buytime;// 订单产生时间
    private int integral;// 订单积分
    private int payment;// 支付类型0订单取消1现金2微信3支付宝
    private String discount;// 订单优惠的金额
    private String receipts;// 订单实收金额
    private int state;// 订单状态：0取消1正常

    private List<ViceOrder> lists;

    private int pageNum;// 总条数
    private int allTotal;// 总金额
    private int couns;// 购买人次

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(int allTotal) {
        this.allTotal = allTotal;
    }


    public int getCouns() {
        return couns;
    }

    public void setCouns(int couns) {
        this.couns = couns;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public List<ViceOrder> getLists() {
        return lists;
    }

    public void setLists(List<ViceOrder> lists) {
        this.lists = lists;
    }

    public void setBackmey(String backmey) {
        this.backmey = backmey;
    }

    public String getBackmey() {
        return backmey;
    }

    public void setSynchro(String synchro) {
        this.synchro = synchro;
    }

    public String getSynchro() {
        return synchro;
    }

    public void setBuytime(String buytime) {
        this.buytime = buytime;
    }

    public String getBuytime() {
        return buytime;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getIntegral() {
        return integral;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getPayment() {
        return payment;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setReceipts(String receipts) {
        this.receipts = receipts;
    }

    public String getReceipts() {
        return receipts;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

}
