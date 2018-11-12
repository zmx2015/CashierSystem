package com.zmx.mian.view;

import com.zmx.mian.bean.MainOrder;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 0:46
 * 类功能：订单列表的
 */

public interface IOrderDataView {

    //获取订单列表
    void getOrderList(List<MainOrder> lists);

    void ErrerMessage();

}
