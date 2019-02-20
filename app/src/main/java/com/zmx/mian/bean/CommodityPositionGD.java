package com.zmx.mian.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-18 16:16
 * 类功能：保存到greendao
 */
@Entity
public class CommodityPositionGD implements Serializable{

    static final long serialVersionUID = -15515456L;//解决实现Serializable接口不能编译通过的问题

    @Id(autoincrement = true)
    public Long id;
    public String name;//名称
    public String type;//没有用到了
    public String state;//是否显示
    public String mid;
    public String admin;
    @Generated(hash = 1124937417)
    public CommodityPositionGD(Long id, String name, String type, String state,
            String mid, String admin) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
        this.mid = mid;
        this.admin = admin;
    }
    @Generated(hash = 1464399358)
    public CommodityPositionGD() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getMid() {
        return this.mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getAdmin() {
        return this.admin;
    }
    public void setAdmin(String admin) {
        this.admin = admin;
    }

}
