package com.zmx.mian.bean;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-12-09 13:10
 * 类功能：采购分析用到的
 */
public class ProcurementAnalysis {

    private int gid;
    private String name;
    private double total;//累计售出金额
    private double all_total;//累计采购金额
    private double weight;//售出重量
    private int num;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAll_total() {
        return all_total;
    }

    public void setAll_total(double all_total) {
        this.all_total = all_total;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
