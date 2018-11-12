package com.zmx.mian.model;

import android.util.Log;

import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Order;
import com.zmx.mian.http.API;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-26 22:18
 * 类功能：处理订单
 */
public class OrderServer implements IOrderServer{

    private String URL = "http://www.yiyuangy.com/admin/";

    public void OrderMessage(String account, String today, String endtime, String thisPage, String num,String admin,String mid,IDataRequestListener listener){
        getOrderMessage(account,today,endtime,thisPage,num,admin,mid,listener);
    }

    public void GoodsItemRanking(String account, String today, String endtime,String admin,String mid, IDataRequestListener listener){
        getGoodsItemRanking(account, today, endtime,admin,mid,listener);
    };

    public void MembersList(String mid, String account, String admin,String field,String sort, IDataRequestListener listener){
        getMembersList(mid, account, admin,field,sort, listener);
    }

    public void MemberMessage(String mid, String account, String admin, IDataRequestListener listener){
        getMembersMessage(mid,account,admin,listener);
    }

    public void Goods(String mid,IDataRequestListener listener){
        getGoods(mid,listener);
    };

    /**
     * 获取订单列表
     * @param account 账号
     * @param today 开始日期
     * @param endtime 结束日期
     * @param thisPage 第几页
     * @param num 显示多少条
     * @param listener
     */
    @Override
    public void getOrderMessage(String account, String today, String endtime, String thisPage, String num,String admin,String mid,final IDataRequestListener listener) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);
        String keys = "";
        try {
            keys = getKey(admin);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对 发送请求 进行封装
        Call<ResponseBody> call = request.getMainOrder(keys,account,today,endtime,thisPage,num,admin,mid);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    //请求处理,输出结果
                    listener.loadSuccess(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("放回的数据","实时"+e.toString());
                }


            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("放回的数据","shibai"+throwable.toString());
                listener.ErrorMessage("error");

            }
        });

    }

    /**
     *商品销量排行榜
     * @param account 会员账号
     * @param today   开始时间
     * @param endtime  结束时间
     * @param listener
     */
    @Override
    public void getGoodsItemRanking(String account, String today, String endtime,String admin,String mid, final IDataRequestListener listener) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);
        String keys = "";
        try {
            keys = getKey(admin);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对 发送请求 进行封装
        Call<ResponseBody> call = request.getGoodsItemRanking(keys,account,today,endtime,admin,mid);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    //请求处理,输出结果
                    listener.loadSuccess(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("放回的数据","实时"+e.toString());
                }

            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("放回的数据","shibai"+throwable.toString());
                listener.ErrorMessage("error");

            }
        });

    }

    /**
     * 获取商品
     * @param mid
     * @param listener
     */
    @Override
    public void getGoods(String mid, final IDataRequestListener listener) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);

        //对 发送请求 进行封装
        Call<ResponseBody> call = request.getGoods(mid);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    //请求处理,输出结果
                    listener.loadSuccess(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("放回的数据","shibai"+throwable.toString());
                listener.ErrorMessage("error");

            }
        });

    }

    /**
     * 修改商品
     * @param mid
     * @param admin
     * @param gid
     * @param groupID
     * @param gds_price
     * @param name
     * @param vip_price
     * @param listener
     */
    @Override
    public void UpdateGoods(String mid,String admin, String gid, String groupID, String gds_price, String name,String vip_price,final IDataRequestListener listener) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);

        String keys = "";

        try {
            keys = getKey(admin);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //对 发送请求 进行封装
        Call<ResponseBody> call = request.UpdateGoods(keys,mid,"1",admin,gid,groupID,gds_price,name,vip_price);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    //请求处理,输出结果
//                    listener.loadSuccess(response.body().string());

                    Log.e("修改的结果：",""+response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("放回的数据","shibai"+throwable.toString());
                listener.ErrorMessage("error");

            }
        });


    }

    /**
     * 会员列表
     * @param mid
     * @param account
     * @param admin
     * @param field
     * @param sort
     * @param listener
     */
    @Override
    public void getMembersList(String mid, String account, String admin,String field,String sort, final IDataRequestListener listener) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);
        String keys = "";
        try {
            keys = getKey(admin);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对 发送请求 进行封装
        Call<ResponseBody> call = request.getMembers(keys,mid,account,admin,field,sort);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    //请求处理,输出结果
                    listener.loadSuccess(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("放回的数据","实时"+e.toString());
                }

            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("放回的数据","shibai"+throwable.toString());
                listener.ErrorMessage("error");

            }
        });

    }

    /**
     * 获取会员信息
     * @param mid
     * @param account
     * @param admin
     * @param listener
     */
    @Override
    public void getMembersMessage(String mid, String account, String admin, final IDataRequestListener listener) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);
        String keys = "";
        try {
            keys = getKey(admin);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对 发送请求 进行封装
        Call<ResponseBody> call = request.getMembersMessage(keys,mid,account,admin);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    //请求处理,输出结果
                    listener.loadSuccess(response.body().string());


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("放回的数据","实时"+e.toString());
                }

            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("放回的数据","shibai"+throwable.toString());
                listener.ErrorMessage("error");

            }
        });


    }

    /**
     * 添加商品
     * @param mid
     * @param admin
     * @param groupID
     * @param gds_price
     * @param name
     * @param vip_price
     * @param listener
     */
    @Override
    public void AddGoods(String mid, String admin, String groupID, String gds_price, String name, String vip_price, final IDataRequestListener listener) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);

        String keys = "";
        try {
            keys = getKey(admin);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //对 发送请求 进行封装
        Call<ResponseBody> call = request.addGoods(keys,mid,"1",admin,name,groupID,gds_price,vip_price);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    //请求处理,输出结果
                    listener.loadSuccess(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("放回的数据","shibai"+throwable.toString());
                listener.ErrorMessage("error");

            }
        });

    }

    @Override
    public void SelectSingleGoods(String mid, String admin,String gid, String today, String endtime, final IDataRequestListener listener) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);

        String keys = "";
        try {
            keys = getKey(admin);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //对 发送请求 进行封装
        Call<ResponseBody> call = request.selectSingleGoods(keys,gid,mid,"1",admin,today,endtime);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    //请求处理,输出结果
                    listener.loadSuccess(response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("放回的数据","shibai"+throwable.toString());
                listener.ErrorMessage("error");

            }
        });

    }


    // 获取key
    public String getKey(String account) throws ParseException, IOException {

        String txt = account + "xbjrws";
        double times = (double) Long.parseLong(String
                .valueOf(System.currentTimeMillis()).toString()
                .substring(0, 10)) * 74;
        BigDecimal a = new BigDecimal(times);
        String keys = md5(txt) + a.toString();
        return keys;

    }

    /**
     * Md5加密函数
     *
     * @param txt
     * @return
     */
    public String md5(String txt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes("GBK")); // Java的字符串是unicode编码，不受源码文件的编码影响；而PHP的编码是和源码文件的编码一致，受源码编码影响。
            StringBuffer buf = new StringBuffer();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xff));
            }
            return buf.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }




}
