package com.zmx.mian.http;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018-06-24.
 */

public interface  API {

    @GET("api.order/orderList")
    Call<ResponseBody> getMainOrder(@Query("pckey") String pckey, @Query("account")String account, @Query("today")String today, @Query("endtime")String endtime, @Query("thisPage")String thisPage, @Query("num")String num, @Query("admin")String admin, @Query("mid")String nid);

    @GET("api.goods/goodsCount")
    Call<ResponseBody> getGoodsItemRanking(@Query("pckey") String pckey, @Query("account")String account, @Query("today")String today, @Query("endtime")String endtime, @Query("admin")String admin, @Query("mid")String mid);

    @GET("api.line/goods")
    Call<ResponseBody> getGoods(@Query("mid") String mid);

    @GET("api.goods/update")
    Call<ResponseBody> UpdateGoods(@Query("pckey") String pckey,@Query("mid") String mid,@Query("account")String account,@Query("admin") String admin,@Query("gid") String gid,@Query("group") String group,@Query("gds_price") String gds_price,@Query("name") String name,@Query("vip_price") String vip_gds_price);


    @GET("api.user/userList")
    Call<ResponseBody> getMembers(@Query("pckey") String pckey,@Query("mid") String mid,@Query("account")String account,@Query("admin") String admin,@Query("field") String field,@Query("sort") String sort);

    @GET("api.user/index")
    Call<ResponseBody> getMembersMessage(@Query("pckey") String pckey,@Query("mid") String mid,@Query("account")String account,@Query("admin") String admin);

    @GET("api.goods/insert")
    Call<ResponseBody> addGoods(@Query("pckey") String pckey,@Query("mid") String mid,@Query("account")String account,@Query("admin") String admin,@Query("name") String name,@Query("group") String group,@Query("gds_price") String gds_price,@Query("vip_price") String vip_gds_price);

    @GET("api.order/odsList")
    Call<ResponseBody> selectSingleGoods(@Query("pckey") String pckey,@Query("gid") String gid,@Query("mid") String mid,@Query("account")String account,@Query("admin") String admin,@Query("today") String today,@Query("endtime") String endtime);


}
