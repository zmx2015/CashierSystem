package com.zmx.mian.model;

/**
 *作者：胖胖祥
 *时间：2016/8/24 0024 下午 5:21
 *功能模块：请求后台数据服务器响应后的回调
 */
public interface IDataRequestListener {

    void loadSuccess(Object object);

    void ErrorMessage(String message);

}
