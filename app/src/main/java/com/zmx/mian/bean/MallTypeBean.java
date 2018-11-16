package com.zmx.mian.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-15 22:39
 * 类功能：商城的分类
 */

@Entity
public class MallTypeBean {

    @Id(autoincrement = true)
    private Long id;
    private String tname;
    private String sort;
    private String mid;
    private String state;
    @Generated(hash = 1785837325)
    public MallTypeBean(Long id, String tname, String sort, String mid,
            String state) {
        this.id = id;
        this.tname = tname;
        this.sort = sort;
        this.mid = mid;
        this.state = state;
    }
    @Generated(hash = 1119745692)
    public MallTypeBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTname() {
        return this.tname;
    }
    public void setTname(String tname) {
        this.tname = tname;
    }
    public String getSort() {
        return this.sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getMid() {
        return this.mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }

}
