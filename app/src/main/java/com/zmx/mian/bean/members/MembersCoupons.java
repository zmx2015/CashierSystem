package com.zmx.mian.bean.members;

import java.io.Serializable;
import java.util.Date;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-02 22:19
 * 类功能：优惠卷
 */

public class MembersCoupons  implements Serializable {

    private String name;
    private int id;
    private int quota;
    private int term;
    private int state;
    private int type;
    private int days;
    private Date starttime;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }
    public int getQuota() {
        return quota;
    }

    public void setTerm(int term) {
        this.term = term;
    }
    public int getTerm() {
        return term;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setDays(int days) {
        this.days = days;
    }
    public int getDays() {
        return days;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }
    public Date getStarttime() {
        return starttime;
    }


}
