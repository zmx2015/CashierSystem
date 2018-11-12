package com.zmx.mian.view;

import com.zmx.mian.bean.GoodsItemRankingBean;

import java.util.List;

/**
 * Created by Administrator on 2018-06-29.
 */

public interface IGoodsItemRankingView {

    //获取订单列表
    void getOrderList(List<GoodsItemRankingBean> lists);

    void ErrerMessage();


}
