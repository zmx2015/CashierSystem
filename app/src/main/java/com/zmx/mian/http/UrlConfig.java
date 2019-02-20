package com.zmx.mian.http;

/**
 * Created by Administrator on 2018-12-08.
 */

public class UrlConfig {

    private static String URL = "http://api.yiyuangy.com/admin/";
    public static String IMG_URL ="http://api.yiyuangy.com/uploads/goods/";//图片路径

    public static String LOGIN = URL+"api.line/login";//登录

    public static String TYPE_LIST =  URL+"api.class/typeList";//获取门店分类列表和商城分类列表
    public static String UPDATE_CP =  URL+"api.class/classPc";//修改门店分类信息
    public static String UPDATE_MALL =  URL+"api.class/classMall";//修改商城分类信息


    public static String INSERT_GOODS =  URL+"api.goods/insert";//新增商品
    public static String GET_ALL_GOODS =  URL+"api.goods/goodsList";//根据分类查询商品
    public static String UPDATE_GOODS=  URL+"api.goods/update";//修改商品
    public static String SELECT_GOODS=  URL+"api.goods/goods";//根据id查询商品
    public static String SELECT_GOODS_COUNT=  URL+"api.goods/goodsCount";//查询单品排行榜
    public static String SEARCH_GOODS=  URL+"api.goods/search";//搜索商品

    public static String ADD_ORDER =  URL+"api.order/orderAdd";//新增订单
    public static String SELECT_ORDER_LIST =  URL+"api.order/orderList";//查询订单列表
    public static String SELECT_ORDER_TWO_LIST =  URL+"api.ordertwo/mallorder";//查询商城订单列表
    public static String DELETE_ORDER=  URL+"api.ordertwo/cancelOrder";//删除订单
    public static String SELECT_ONE_ORDER=  URL+"api.order/odsList";//查询某个商品的销售订单


    public static String SELECT_MEMBER =  URL+"api.lineapi/getUserInfo";//查找会员
    public static String SELECT_ADD_MEMBER =  URL+"api.user/UserCount";//获取新增会员个数
    public static String UPDATE_MEMBER_PHONE =  URL+"api.user/bindPhone";//修改会员手机号码
    public static String UPDATE_MEMBER_BEI =  URL+"api.user/userUp";//修改会员备注
    public static String SELECT_MEMBER_LIST =  URL+"api.user/userlist";//查找会员列表
    public static String SELECT_MEMBER_FACE_LIST =  URL+"api.user/faceList";//会员充值金额列表
    public static String ADD_MONEY =  URL+"api.user/addMoney";//会员充值

    public static String COUPONS_LIST =  URL+"api.Coupon/couponsList";//获取优惠卷列表
    public static String ADD_CARD =  URL+"api.Coupon/couponsAdd";//新增优惠卷
    public static String DELETE_CARD =  URL+"api.Coupon/couponsDel";//作废优惠卷
    public static String ADD_C_D_CARD =  URL+"api.Coupon/ajaxAddCoupons"; //添加到抽奖池里面和兑换里面
    public static String GET_GIVE =  URL+"api.Coupon/give"; //获取后台注册赠送的优惠卷
    public static String SELECT_EXCHANGE =  URL+"api.Coupon/exchange"; //查询兑换中的优惠卷
    public static String DELETE_EXCHANGE =  URL+"api.Coupon/ajaxDelCoupons"; //移走兑换中的优惠卷
    public static String SELECT_DRAW =  URL+"api.Coupon/draw"; //查询抽奖里面的优惠卷

    public static String UPDATE_CONFIG =  URL+"api.config/setconfig"; //修改配置
    public static String GET_CONFIG =  URL+"api.config/getconfig"; //获得配置信息

    public static String INSERT_FEEDBACK =  URL+"api.notice/insert"; //添加意见反馈


    public static String INSERT_PURCHASE =  URL+"api.Purchase/insert"; //添加或者修改采购单
    public static String SELECT_PURCHASE_DETAIL =  URL+"api.Purchase/detail"; //查询采购单详情
    public static String SELECT_PURCHASE_LOCKS =  URL+"api.purchase/locks"; //采购单加锁
    public static String SELECT_PURCHASE =  URL+"api.Purchase/index"; //采购单加锁
    public static String PURCHASE_DATA =  URL+"api.purchase/goodsData"; //采购分析数据


}
