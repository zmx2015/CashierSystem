package com.zmx.mian.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-19 14:38
 * 类功能：进货记录列表
 */
@Entity
public class StockManagementBean implements Serializable {

    static final long serialVersionUID = -15515456L;//解决实现Serializable接口不能编译通过的问题
    @Id(autoincrement = true)
    public Long id;//表id
    public String number;//进货单号
    private String sm_time;//创建时间
    private String rh_time;//进货时间
    private String total;//总价
    private String sm_state;//状态（判断是否已经上传服务器了）1为上传，0为未上传,2为重新编辑待上传
    @Transient
    private List<StockManagementDetailsBean> list;

    @Generated(hash = 756909736)
    public StockManagementBean(Long id, String number, String sm_time,
            String rh_time, String total, String sm_state) {
        this.id = id;
        this.number = number;
        this.sm_time = sm_time;
        this.rh_time = rh_time;
        this.total = total;
        this.sm_state = sm_state;
    }
    @Generated(hash = 49090520)
    public StockManagementBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSm_time() {
        return this.sm_time;
    }
    public void setSm_time(String sm_time) {
        this.sm_time = sm_time;
    }
    public String getSm_state() {
        return this.sm_state;
    }
    public void setSm_state(String sm_state) {
        this.sm_state = sm_state;
    }
    public String getRh_time() {
        return this.rh_time;
    }
    public void setRh_time(String rh_time) {
        this.rh_time = rh_time;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<StockManagementDetailsBean> getList() {
        return list;
    }

    public void setList(List<StockManagementDetailsBean> list) {
        this.list = list;
    }
}
