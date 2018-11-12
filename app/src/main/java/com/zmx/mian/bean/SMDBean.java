package com.zmx.mian.bean;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-24 21:47
 * 类功能：适配后台数据，进货上传用到
 */
public class SMDBean{

    private String gid;
    private String memo;
    private String price;
    private String nums;
    private String weight;
    private String color;
    private String subtotal;
    private String total;
    private String payment;//支付方式 1为现金，2为银行卡，3为微信，4为支付宝，5为欠账，6其他
    private String supplier;//供应商
    private String deposit;//押金
    private String freight;//运费
    public String unita;//计价单位 1为按件，2为重量

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getUnita() {
        return unita;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public void setUnita(String unita) {
        this.unita = unita;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
