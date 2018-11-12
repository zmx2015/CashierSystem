package com.zmx.mian.bean.members;

import java.io.Serializable;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-02 23:25
 * 类功能：总的会员信息
 */
public class Members implements Serializable{

    private List<MembersCoupons> coupons;
    private List<MembersExchange> exchanges;
    private List<MembersOrder> orders;
    private List<MembersPrize> prizes;
    private List<String> sign;
    private MembersList list;

    public List<MembersCoupons> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<MembersCoupons> coupons) {
        this.coupons = coupons;
    }

    public List<MembersExchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<MembersExchange> exchanges) {
        this.exchanges = exchanges;
    }

    public List<MembersOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<MembersOrder> orders) {
        this.orders = orders;
    }

    public List<MembersPrize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<MembersPrize> prizes) {
        this.prizes = prizes;
    }

    public List<String> getSign() {
        return sign;
    }

    public void setSign(List<String> sign) {
        this.sign = sign;
    }

    public MembersList getList() {
        return list;
    }

    public void setList(MembersList list) {
        this.list = list;
    }
}
