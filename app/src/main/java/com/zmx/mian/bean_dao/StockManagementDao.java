package com.zmx.mian.bean_dao;

import android.util.Log;

import com.zmx.mian.MyApplication;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.dao.StockManagementBeanDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-24 12:25
 * 类功能：操作进货类
 */
public class StockManagementDao {

    private static StockManagementBeanDao dao = MyApplication.getInstance().getDaoInstant().getStockManagementBeanDao();

    /**
     * 添加目录
     * @param smb
     */
    public static long AddStock(StockManagementBean smb){
        return dao.insertOrReplace(smb);

    }

    /**
     * 查询全部数据
     */
    public static List<StockManagementBean> queryAll() {
        return dao.loadAll();
    }


    /**
     * 修改某条数据的内容
     */
    public static void UpdateStockManagemen(StockManagementBean sb){

        //1.where是查询条件，
        //2.unique()表示查询结果为一条数据，若数据不存在，findUser为null。
        StockManagementBean g = dao.queryBuilder().where(StockManagementBeanDao.Properties.Number.eq(sb.getNumber())).build().unique();

        if(g != null) {

            g.setRh_time(sb.getRh_time());
            g.setSm_time(sb.getSm_time());
            g.setSm_state(sb.getSm_state());
            g.setTotal(sb.getTotal());
            g.setNumber(sb.getNumber());
            // update为更新
            dao.update(g);
            Log.e("更新","更新");

        } else {

            Log.e("更新aa","更新aaa");

        }

    }

    /**
     * 修改某条数据的内容
     */
    public static void UpdateSM(StockManagementBean sb){

        //1.where是查询条件，
        //2.unique()表示查询结果为一条数据，若数据不存在，findUser为null。
        StockManagementBean g = dao.queryBuilder().where(StockManagementBeanDao.Properties.Number.eq(sb.getNumber())).build().unique();

        //如果没有就添加进入本地
        if(g == null) {

            AddStock(sb);

        }

    }

    /**
     * 删除记录
     * @param sb
     */
    public void deleteSb(StockManagementBean sb){

        StockManagementBean g = dao.queryBuilder().where(StockManagementBeanDao.Properties.Number.eq(sb.getNumber())).build().unique();

        if(g != null){
            //通过Key来删除，这里的Key就是user字段中的ID号
            dao.deleteByKey(g.getId());
        }

    }

}
