package com.zmx.mian.bean_dao;

import android.util.Log;

import com.zmx.mian.MyApplication;
import com.zmx.mian.bean.MallTypeBean;
import com.zmx.mian.dao.MallTypeBeanDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-15 22:41
 * 类功能：
 */

public class MallTypeDao {

    private static MallTypeBeanDao dao = MyApplication.getInstance().getDaoInstant().getMallTypeBeanDao();


    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param
     */
    public static long insertMtb(MallTypeBean mtb) {

        return dao.insertOrReplace(mtb);

    }

    /**
     * 查询全部数据
     */
    public static List<MallTypeBean> queryAll() {

        return dao.loadAll();
    }



    /**
     * 修改某条数据的内容
     */
    public static void UpdateMtp(MallTypeBean mtp){

        //1.where是查询条件，
        //2.unique()表示查询结果为一条数据，若数据不存在，findUser为null。
        MallTypeBean c = dao.queryBuilder().where(MallTypeBeanDao.Properties.Id.eq(mtp.getId())).build().unique();

        if(c != null) {

           c.setMid(mtp.getMid());
           c.setSort(mtp.getSort());
           c.setState(mtp.getState());
           c.setTname(mtp.getTname());
            // update为更新
            dao.update(c);
            Log.e("更新","更新");

        } else {

            Log.e("更新aa","更新aaa");

        }

    }

    public void deleteData(){

        dao.deleteAll();

    }

}
