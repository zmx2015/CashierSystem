package com.zmx.mian.bean_dao;

import android.util.Log;

import com.zmx.mian.MyApplication;
import com.zmx.mian.bean.FeedbackBean;
import com.zmx.mian.dao.FeedbackBeanDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-20 13:39
 * 类功能：
 */

public class FeedbackDao {

    private static FeedbackBeanDao dao = MyApplication.getInstance().getDaoInstant().getFeedbackBeanDao();

    /**
     * 分页查询聊天记录
     * @param
     * @return
     */
    public List<FeedbackBean> SelectAllFeedbackBean(String admin){

        return dao.queryBuilder().where(FeedbackBeanDao.Properties.Login_name.eq(admin)).build().list();

    }

    /**
     * 插入聊天记录
     * @param fb
     */
    public long addFeedbackBean(FeedbackBean fb){

       return dao.insert(fb);

    }


}
