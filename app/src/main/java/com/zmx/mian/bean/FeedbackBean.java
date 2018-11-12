package com.zmx.mian.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-19 19:50
 * 类功能：意见反馈列表
 */
@Entity
public class FeedbackBean {


    @Id(autoincrement = true)
    private Long id;//表id
    private String login_name;//当前登录用户的名称
    private String user_id;//对话人的id
    private String admin_name;//对话人的名称
    private String user_head;//对话人的头像
    private String msg;//内容
    private String time;//时间
    private String type;//类型
    @Generated(hash = 577344620)
    public FeedbackBean(Long id, String login_name, String user_id,
            String admin_name, String user_head, String msg, String time,
            String type) {
        this.id = id;
        this.login_name = login_name;
        this.user_id = user_id;
        this.admin_name = admin_name;
        this.user_head = user_head;
        this.msg = msg;
        this.time = time;
        this.type = type;
    }
    @Generated(hash = 1152150518)
    public FeedbackBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUser_id() {
        return this.user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getAdmin_name() {
        return this.admin_name;
    }
    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }
    public String getUser_head() {
        return this.user_head;
    }
    public void setUser_head(String user_head) {
        this.user_head = user_head;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getLogin_name() {
        return this.login_name;
    }
    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

}
