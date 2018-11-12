package com.zmx.mian.view;

import com.zmx.mian.bean.members.MembersList;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-08-30 14:04
 * 类功能：获得会员列表界面
 */

public interface IMembersView {

    void getMembersList(List<MembersList> lists);
    void ErrerMessage();

}
