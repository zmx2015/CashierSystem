package com.zmx.mian.view;

import com.zmx.mian.bean.StoresMessage;

import java.util.List;

/**
 * Created by Administrator on 2018-06-26.
 */

public interface ILoginView {

    /**
     * 登录成功
     * @param
     */
    void Login(String message, List<StoresMessage> lists);

    /**
     * 登录失败
     * @param message
     */
    void ErrorLogin(String message);

}
