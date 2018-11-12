package com.zmx.mian.bean;

import java.io.Serializable;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-07-29 15:04
 * 类功能：门店信息
 */

public class StoresMessage implements Serializable {

    private int admin_id;
    private int auth_group_id;
    private String aname;
    private int id;
    private int type;

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public int getAuth_group_id() {
        return auth_group_id;
    }

    public void setAuth_group_id(int auth_group_id) {
        this.auth_group_id = auth_group_id;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
