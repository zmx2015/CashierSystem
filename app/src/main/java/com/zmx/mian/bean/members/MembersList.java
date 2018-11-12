package com.zmx.mian.bean.members;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-08-26 18:19
 * 类功能：会员列表
 */

public class MembersList implements Serializable{


    private int uid;
    private String account;//会员号
    private String password;
    private String wechatImg;//微信头像
    private String wechat;
    private String wechatName;
    private String mob;
    private String birthday;
    private String pubtime;
    private String money;
    private long sign;
    private int integral;//积分
    private int mid;
    private long lasttime;
    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getUid() {
        return uid;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccount() {
        return account;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setWechatImg(String wechatImg) {
        this.wechatImg = wechatImg;
    }
    public String getWechatImg() {
        return wechatImg;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
    public String getWechat() {
        return wechat;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }
    public String getMob() {
        return mob;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getBirthday() {
        return birthday;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }
    public String getPubtime() {
        return pubtime;
    }

    public void setMoney(String money) {
        this.money = money;
    }
    public String getMoney() {
        return money;
    }

    public void setSign(long sign) {
        this.sign = sign;
    }
    public long getSign() {
        return sign;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }
    public int getIntegral() {
        return integral;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
    public int getMid() {
        return mid;
    }

    public String getWechatName() {
        return wechatName;
    }

    public long getLasttime() {
        return lasttime;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }
}
