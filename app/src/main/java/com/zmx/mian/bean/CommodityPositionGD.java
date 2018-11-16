package com.zmx.mian.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-18 16:16
 * 类功能：保存到greendao
 */
@Entity
public class CommodityPositionGD {

    @Id(autoincrement = true)
    public Long id;
    public String name;//名称
    public String type;//没有用到了
    public String state;//是否显示
    public String mid;
    @Generated(hash = 910371906)
    public CommodityPositionGD(Long id, String name, String type, String state,
            String mid) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
        this.mid = mid;
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

}
