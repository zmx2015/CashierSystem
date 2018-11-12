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
    public String name;
    public String type;
    @Generated(hash = 960347377)
    public CommodityPositionGD(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

}
