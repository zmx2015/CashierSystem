package com.zmx.mian.bean_dao;

import com.zmx.mian.MyApplication;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.dao.CommodityPositionGDDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-18 16:53
 * 类功能：处理GreenDao的类别
 */
public class CPDao {

    private static CommodityPositionGDDao cpDao = MyApplication.getInstance().getDaoInstant().getCommodityPositionGDDao();


    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param
     */
    public static long insertCp(CommodityPositionGD cp) {
        return cpDao.insertOrReplace(cp);
    }

    /**
     * 查询全部数据
     */
    public static List<CommodityPositionGD> queryAll() {
        return cpDao .loadAll();
    }


}
