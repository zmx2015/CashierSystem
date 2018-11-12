package com.zmx.mian.bean;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 23:53
 * 类功能：单品排行榜
 */

public class GoodsItemRankingBean {

    private String gid;//商品id
    private String name;// 商品名称
    private String zMoney;// 总金额
    private String zWeight;// 总重量
    private String zNum;// 购买人次
    private String num;// 数量

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getzMoney() {
        return zMoney;
    }

    public void setzMoney(String zMoney) {
        this.zMoney = zMoney;
    }

    public String getzWeight() {
        return zWeight;
    }

    public void setzWeight(String zWeight) {
        this.zWeight = zWeight;
    }

    public String getzNum() {
        return zNum;
    }

    public void setzNum(String zNum) {
        this.zNum = zNum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}

