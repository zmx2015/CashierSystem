package com.zmx.mian.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 *
 * 程序员：曾敏祥
 * 时间：2018-4-25下午3:58:23
 * 功能模块：商品的类别
 *
 */
public class CommodityPosition implements Serializable{

    public Long id;
    public String name;
    public String type;
    public List<Goods> list;//该类目下的商品

    public List<Goods> getList() {
        return list;
    }

    public void setList(List<Goods> list) {
        this.list = list;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }




}
