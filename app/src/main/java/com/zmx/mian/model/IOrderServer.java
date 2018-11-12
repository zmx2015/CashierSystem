package com.zmx.mian.model;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-26 22:09
 * 类功能：处理订单的
 */

public interface IOrderServer {

    /**
     * 获取订单列表
     * @param account 账号
     * @param today 开始日期
     * @param endtime 结束日期
     * @param thisPage 第几页
     * @param num 显示多少条
     */
    void getOrderMessage(String account, String today,
                         String endtime, String thisPage, String num,String admin,String mid,IDataRequestListener listener);

    /**
     * 获取单品排行榜
     * @param account 会员账号
     * @param today   开始时间
     * @param endtime  结束时间
     * @param listener
     */
    void getGoodsItemRanking(String account, String today,
                             String endtime,String admin,String mid,IDataRequestListener listener);

    /**
     * 获得商品列表
     * @param mid
     * @param listener
     */
    void getGoods(String mid,IDataRequestListener listener);

    /**
     * 修改商品
     * @param mid
     * @param gid
     * @param groupID
     * @param gds_price
     * @param name
     * @param listener
     */
    void UpdateGoods(String mid,String admin,String gid,String groupID,String gds_price,String name,String vip_price,IDataRequestListener listener);

    /**
     * 获得会员列表
     * @param
     * @param mid
     * @param account
     * @param admin
     * @param listener
     */
    void getMembersList(String mid,String account,String admin,String field,String sort,IDataRequestListener listener);

    /**
     * 获得会员详情
     */
    void getMembersMessage(String mid,String account,String admin,IDataRequestListener listener);

    /**
     * 添加商品
     */
    void AddGoods(String mid,String admin,String groupID,String gds_price,String name,String vip_price,IDataRequestListener listener);

    /**
     * 根据商品id查询商品当天的订单
     */
    void SelectSingleGoods(String mid,String admin,String gid,String today,String endtime,IDataRequestListener listener);

}
