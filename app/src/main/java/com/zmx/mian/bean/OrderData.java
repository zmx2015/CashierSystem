package com.zmx.mian.bean;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-27 1:05
 * 类功能：订单的时间类
 */

public class OrderData {


    private double allTotal;
    private int nums;
    private int pageThis;
    private double pageNum;
    private int pageCount;
    public void setAllTotal(double allTotal) {
        this.allTotal = allTotal;
    }
    public double getAllTotal() {
        return allTotal;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }
    public int getNums() {
        return nums;
    }

    public void setPageThis(int pageThis) {
        this.pageThis = pageThis;
    }
    public int getPageThis() {
        return pageThis;
    }

    public void setPageNum(double pageNum) {
        this.pageNum = pageNum;
    }
    public double getPageNum() {
        return pageNum;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public int getPageCount() {
        return pageCount;
    }

}
