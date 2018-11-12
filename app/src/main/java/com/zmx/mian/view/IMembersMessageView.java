package com.zmx.mian.view;

import com.zmx.mian.bean.GoodsItemRankingBean;
import com.zmx.mian.bean.members.Members;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-02 23:31
 * 类功能：会员信息
 */

public interface IMembersMessageView {

    //获取订单列表
    void getMembersMessage(Members m);

    void ErrerMessage(String state);


}
