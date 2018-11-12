package com.zmx.mian.bean_dao;

import android.util.Log;

import com.zmx.mian.MyApplication;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.dao.StockManagementDetailsBeanDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-24 18:37
 * 类功能：查询进货详情
 */

public class StockManagementDetailsDao {

    private static StockManagementDetailsBeanDao dao = MyApplication.getInstance().getDaoInstant().getStockManagementDetailsBeanDao();


    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param
     */
    public static long insertSmdd(StockManagementDetailsBean smdb) {
        return dao.insertOrReplace(smdb);
    }

    /**
     * 根据进货列表的编号查询所有数据
     */
    public static List<StockManagementDetailsBean> queryWhere(String number) {

        return dao.queryBuilder().where(StockManagementDetailsBeanDao.Properties.Number.eq(number)).list();

    }

    /**
     * 修改某条数据的内容
     */
    public static void UpdateStock(StockManagementDetailsBean smdb){

        //1.where是查询条件，
        //2.unique()表示查询结果为一条数据，若数据不存在，findUser为null。
        StockManagementDetailsBean g = dao.queryBuilder().where(StockManagementDetailsBeanDao.Properties.Number.eq(smdb.getNumber()),StockManagementDetailsBeanDao.Properties.Id.eq(smdb.getId())).build().unique();

        if(g != null) {

            Log.e("smdb","smdbID"+g.getId());
            g.setG_note(smdb.getG_note());
            g.setG_weight(smdb.getG_weight());
            g.setG_price(smdb.getG_price());
            g.setG_id(smdb.getG_id());
            g.setG_total(smdb.getG_total());
            g.setS_id(smdb.getS_id());
            g.setG_name(smdb.getG_name());

            Log.e("smdb","成功");
            // update为更新
            dao.update(g);
//            Toast.makeText(MyApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();

        } else {

            Log.e("smdb","添加进本地");
            insertSmdd(smdb);//添加进本地

        }

    }


    /**
     * 删除记录
     * @param smdb
     */
    public void deleteSm(StockManagementDetailsBean smdb){

        if(smdb.getId() != null){

            StockManagementDetailsBean g = dao.queryBuilder().where(StockManagementDetailsBeanDao.Properties.Number.eq(smdb.getNumber()),StockManagementDetailsBeanDao.Properties.Id.eq(smdb.getId())).build().unique();

            if(g != null){
                //通过Key来删除，这里的Key就是user字段中的ID号
                dao.deleteByKey(g.getId());
            }

        }

    }

}
