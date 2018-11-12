package com.zmx.mian.view;

import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Order;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-26 22:19
 * 类功能：订单列表
 */

public interface IHomeView {

    //获取订单列表
    void getOrderList(List<MainOrder> lists);

    void ErrerMessage();

}
