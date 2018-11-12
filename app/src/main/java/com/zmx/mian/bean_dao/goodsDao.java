package com.zmx.mian.bean_dao;

import com.zmx.mian.MyApplication;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.dao.GoodsDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-18 18:11
 * 类功能：
 */
public class goodsDao {

   private static GoodsDao dao = MyApplication.getInstance().getDaoInstant().getGoodsDao();


    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param
     */
    public static long insertCp(Goods g) {
        return dao.insertOrReplace(g);
    }

    /**
     * 查询全部数据
     */
    public static List<Goods> queryAll() {
        return dao.loadAll();
    }

    /**
     * 根据商品分类查询
     */
    public static List<Goods> queryWhere(String name) {
        return dao.queryBuilder().where(GoodsDao.Properties.Cp_name.eq(name)).list();
    }

    /**
     * 根据商品id修改商品
     */
    public static void UpdateGoods(Goods gg){

        //1.where是查询条件，
        //2.unique()表示查询结果为一条数据，若数据不存在，findUser为null。
        Goods g = dao.queryBuilder().where(GoodsDao.Properties.G_id.eq(gg.getG_id())).build().unique();
        g.setCp_group(gg.getCp_group());
        g.setCp_name(gg.getCp_name());
        g.setG_img(gg.getG_img());
        g.setG_name(gg.getG_name());
        g.setG_price(gg.getG_price());
        g.setVip_g_price(gg.getVip_g_price());

        if(g != null) {

            // update为更新
            dao.update(g);
//            Toast.makeText(MyApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();

        } else {

//            Toast.makeText(MyApplication.getContext(), "用户不存在", Toast.LENGTH_SHORT).show();

        }

    }


    public List<Goods> SelectDimGoods(String name){
        return dao.queryBuilder().where(GoodsDao.Properties.G_name.like("%" + name + "%")).list();
    }

}
