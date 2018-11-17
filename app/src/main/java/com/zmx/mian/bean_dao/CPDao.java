package com.zmx.mian.bean_dao;

import android.util.Log;

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

        return cpDao.loadAll();
    }



    /**
     * 修改某条数据的内容
     */
    public static void UpdateCp(CommodityPositionGD cp){

        //1.where是查询条件，
        //2.unique()表示查询结果为一条数据，若数据不存在，findUser为null。
        CommodityPositionGD c = cpDao.queryBuilder().where(CommodityPositionGDDao.Properties.Id.eq(cp.getId())).build().unique();

        if(c != null) {

            c.setName(cp.getName());
            c.setType(cp.getType());
            c.setId(cp.getId());
            // update为更新
            cpDao.update(c);
            Log.e("更新","更新");

        } else {

            Log.e("更新aa","更新aaa");

        }

    }

    public void deleteData(){

        cpDao.deleteAll();

    }
}
