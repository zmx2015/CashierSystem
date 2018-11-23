package com.zmx.mian.bean;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-21 17:11
 * 类功能：卡卷管理类
 */

public class CardVolumeBean {

    private int pid;
    private int cid;  //卡卷id
    private int quota;  // 面值
    private int term;   //使用条件
    private int type;   //卡卷类型
    private int status;   //状态0为不可用1位可用
    private long addtime;   //创建时间
    private int days;   //多少天
    private int uid;   //管理员id
    private String content;  //描述


    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCid() {
        return cid;
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

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
