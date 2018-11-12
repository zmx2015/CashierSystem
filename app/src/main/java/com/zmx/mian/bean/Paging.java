package com.zmx.mian.bean;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 13:24
 * 类功能：分页类
 */


public class Paging implements Serializable {

    int pageSize;// 一页显示计几条记录
    int pageNow;// 希望显示第几页
    int rowCount;// 共有几条记录（查表）
    int pageCount;// 共有几页（计算）
    int pageitem;// （(pageNow-1)*pageSize）开始处

    // 初始化pageSize和和第几页开始
    public Paging() {

        pageSize = 30;
        pageNow = 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageCount() {

        return rowCount % pageSize == 0 ? rowCount / pageSize : (rowCount
                / pageSize + 1);

    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageitem() {
        return pageitem = (pageNow - 1) * pageSize;
    }

}

