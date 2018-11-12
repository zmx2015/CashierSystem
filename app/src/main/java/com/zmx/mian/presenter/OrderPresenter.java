package com.zmx.mian.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.zmx.mian.bean.GoodsItemRankingBean;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Order;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.bean.members.Members;
import com.zmx.mian.bean.members.MembersCoupons;
import com.zmx.mian.bean.members.MembersExchange;
import com.zmx.mian.bean.members.MembersList;
import com.zmx.mian.bean.members.MembersOrder;
import com.zmx.mian.bean.members.MembersPrize;
import com.zmx.mian.model.IDataRequestListener;
import com.zmx.mian.model.OrderServer;
import com.zmx.mian.view.IAddGoodsView;
import com.zmx.mian.view.IGoodsItemRankingView;
import com.zmx.mian.view.IGoodsView;
import com.zmx.mian.view.IHomeView;
import com.zmx.mian.view.IMembersMessageView;
import com.zmx.mian.view.IMembersView;
import com.zmx.mian.view.IOrderDataView;
import com.zmx.mian.view.IprotypeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-26 22:23
 * 类功能：处理订单列表
 */

public class OrderPresenter {

    private OrderServer os;
    private IHomeView home;
    private IOrderDataView iOrderDataView;
    private IGoodsItemRankingView igir;
    private IGoodsView igv;
    private IprotypeView ipv;
    private IMembersView imv;
    private IMembersMessageView immv;
    private IAddGoodsView iagv;

    public OrderPresenter(IprotypeView ipv){

        this.ipv = ipv;
        os = new OrderServer();

    }

    public OrderPresenter(IHomeView home){
        this.home = home;
        os = new OrderServer();
    }

    public OrderPresenter(IGoodsView igv){
        this.igv = igv;
        os = new OrderServer();
    }


    public OrderPresenter(IOrderDataView iOrderDataView){
        this.iOrderDataView = iOrderDataView;
        os = new OrderServer();
    }
    public OrderPresenter(IGoodsItemRankingView igir){
        this.igir = igir;
        os = new OrderServer();
    }

    public OrderPresenter(IMembersView imv){
        this.imv = imv;
        os = new OrderServer();
    }
    public OrderPresenter(IMembersMessageView immv){
        this.immv = immv;
        os = new OrderServer();
    }

    public OrderPresenter(IAddGoodsView iagv){
        this.iagv = iagv;
        os = new OrderServer();
    }


    public void getOrderMessage(String account, String today, String endtime, String thisPage, String num, String admin, String mid){

        os.OrderMessage(account, today, endtime, thisPage, num,admin,mid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    JSONObject bodys = new JSONObject(object.toString());

                    // 处理接口返回的json数据

                    JSONArray array = bodys.getJSONArray("list");

                    List<MainOrder> lists = new ArrayList<MainOrder>();

                    JSONObject data = bodys.getJSONObject("data");
                    int nums = data.getInt("pageCount");
                    int allTotal = data.getInt("allTotal");
                    int couns = data.getInt("nums");


                    for (int i = 0; i < array.length(); i++) {

                        MainOrder mw = new MainOrder();
                        JSONObject json = array.getJSONObject(i);

                        mw.setPageNum(nums);
                        mw.setAllTotal(allTotal);
                        mw.setCouns(couns);
                        mw.setId(json.getInt("id"));
                        mw.setUid(json.getInt("uid"));
                        mw.setOrder(json.getString("order"));
                        mw.setTotal(json.getString("total"));
                        mw.setBackmey(json.getString("backmey"));
                        mw.setSynchro(json.getString("synchro"));
                        mw.setBuytime(json.getString("buytime"));
                        mw.setIntegral(json.getInt("integral"));
                        mw.setPayment(json.getInt("payment"));
                        mw.setDiscount(json.getString("discount"));
                        mw.setReceipts(json.getString("receipts"));
                        mw.setState(json.getInt("state"));

                        List<ViceOrder> vws = new ArrayList<ViceOrder>();

                        JSONArray ja = json.getJSONArray("detailed");
                        for (int j = 0; j < ja.length(); j++) {

                            ViceOrder vw = new ViceOrder();
                            JSONObject jj = ja.getJSONObject(j);

                            vw.setOrder_id(jj.getInt("order_id"));
                            vw.setGoods_id(jj.getInt("goods_id"));
                            vw.setWeight(jj.getString("weight"));
                            vw.setPrice(jj.getString("price"));
                            vw.setSubtotal(jj.getString("subtotal"));
                            vw.setType(jj.getInt("type"));
                            vw.setName(jj.getString("name"));

                            vws.add(vw);
                        }

                        mw.setLists(vws);

                        lists.add(mw);

                    }

                    home.getOrderList(lists);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("戳了","处理"+e.toString());
                }


            }

            @Override
            public void ErrorMessage(String message) {

                home.ErrerMessage();

            }
        });

    }

    /**
     * 获取订单列表
     * @param account
     * @param today
     * @param endtime
     * @param thisPage
     * @param num
     */
    public void getOrderMessageD(String account, String today, String endtime, String thisPage, String num, String admin, String mid){

        os.OrderMessage(account, today, endtime, thisPage, num,admin,mid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                Log.e("进来了s","进来了s"+object.toString());

                try {

                    JSONObject bodys = new JSONObject(object.toString());

                    // 处理接口返回的json数据

                    JSONArray array = bodys.getJSONArray("list");

                    List<MainOrder> lists = new ArrayList<MainOrder>();

                    JSONObject data = bodys.getJSONObject("data");
                    int nums = data.getInt("pageCount");
                    int allTotal = data.getInt("allTotal");
                    int couns = data.getInt("nums");

                    Log.e("进来了s","进来了s");

                    for (int i = 0; i < array.length(); i++) {

                        Log.e("进来了","进来了");
                        MainOrder mw = new MainOrder();
                        JSONObject json = array.getJSONObject(i);

                        mw.setPageNum(nums);
                        mw.setAllTotal(allTotal);
                        mw.setCouns(couns);
                        mw.setId(json.getInt("id"));
                        mw.setUid(json.getInt("uid"));
                        mw.setOrder(json.getString("order"));
                        mw.setTotal(json.getString("total"));
                        mw.setBackmey(json.getString("backmey"));
                        mw.setSynchro(json.getString("synchro"));
                        mw.setBuytime(json.getString("buytime"));
                        mw.setIntegral(json.getInt("integral"));
                        mw.setPayment(json.getInt("payment"));
                        mw.setDiscount(json.getString("discount"));
                        mw.setReceipts(json.getString("receipts"));
                        mw.setState(json.getInt("state"));

                        List<ViceOrder> vws = new ArrayList<ViceOrder>();

                        JSONArray ja = json.getJSONArray("detailed");
                        for (int j = 0; j < ja.length(); j++) {

                            ViceOrder vw = new ViceOrder();
                            JSONObject jj = ja.getJSONObject(j);

                            vw.setOrder_id(jj.getInt("order_id"));
                            vw.setGoods_id(jj.getInt("goods_id"));
                            vw.setWeight(jj.getString("weight"));
                            vw.setPrice(jj.getString("price"));
                            vw.setSubtotal(jj.getString("subtotal"));
                            vw.setType(jj.getInt("type"));
                            vw.setName(jj.getString("name"));

                            vws.add(vw);
                        }

                        mw.setLists(vws);

                        lists.add(mw);

                    }

                    iOrderDataView.getOrderList(lists);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("戳了","处理"+e.toString());
                }


            }

            @Override
            public void ErrorMessage(String message) {

                Log.e("进来了","进来了");
                iOrderDataView.ErrerMessage();

            }
        });

    }

    /**
     * 商品排行榜
     * @param account
     * @param today
     * @param endtime
     * @param admin
     * @param mid
     */
    public void getGoodsItemRanking(String account, String today, String endtime,String admin,String mid){

        os.GoodsItemRanking(account, today, endtime,admin,mid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                try {

                    String msg = object.toString();

                    System.out.println("单品：" + msg);
                    Log.e("fdff","单品：" + msg);

                    if (!msg.equals("")) {

                        List<GoodsItemRankingBean> lists = new ArrayList<>();
                        JSONArray array = new JSONArray(msg);

                        for (int i = 0; i < array.length(); i++) {

                            GoodsItemRankingBean r = new GoodsItemRankingBean();
                            JSONObject o = array.getJSONObject(i);

                            r.setGid(o.getString("gid"));
                            r.setName(o.getString("name"));
                            r.setzMoney(o.getString("total"));
                            r.setzNum(o.getString("number"));
                            r.setzWeight(o.getString("weight"));
                            r.setNum(o.getString("num"));

                            lists.add(r);
                        }

                        igir.getOrderList(lists);

                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void ErrorMessage(String message) {

            }
        });

    }

    /**
     * 获得商品
     * @param mid
     */
    public void getGoods(String mid){

        os.getGoods(mid, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                String msg = object.toString();

                igv.getGoodsList(msg);

            }

            @Override
            public void ErrorMessage(String message) {

                igv.ErrerMessage();

            }
        });

    };

    /**
     * 更新商品
     * @param mid
     * @param gid
     * @param groupID
     * @param gds_price
     * @param name
     */
    public void UpdateGoods(String mid,String admin, String gid, String groupID, String gds_price, String name,String price){

        Log.e("价格："+gds_price,"名称"+name+"  gid"+gid);

        os.UpdateGoods(mid,admin, gid, groupID, gds_price, name,price, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                ipv.UpdateGoods(object.toString());

            }

            @Override
            public void ErrorMessage(String message) {

            }
        });

    }


    public void getMembersList(String mid,String account,String admin,String field,String sort){

        os.MembersList(mid, account, admin,field,sort, new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {

                try {

                    Log.e("会员细信息","实收"+object.toString());

                    List<MembersList> lists = new ArrayList<>();

                    JSONArray array = new JSONArray(object.toString());

                    for (int i = 0;i<array.length();i++){

                        JSONObject json = array.getJSONObject(i);
                        Gson g = new Gson();
                        MembersList ml = g.fromJson(json.toString(),MembersList.class);
                        lists.add(ml);

                    }

                imv.getMembersList(lists);


                } catch (JSONException e) {

                    Log.e("解析异常","异常"+e.toString());

                    e.printStackTrace();
                }


            }

            @Override
            public void ErrorMessage(String message) {



            }
        });

    }


    public void getMembersMessage(String mid,String account,String admin){
        os.getMembersMessage(mid, account, admin, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                Log.e("shuju","sss"+object.toString());
                Members m = new Members();
                try {

                    JSONObject json = new JSONObject(object.toString());


                    String status = json.getString("status");

                    if(status.equals("0")){

                        immv.ErrerMessage(json.getString("msg"));

                    }else{

                    String info = json.getString("info");
                    JSONObject jinfo = new JSONObject(info);
                    Gson g = new Gson();
                    MembersList ml = g.fromJson(jinfo.toString(),MembersList.class);
                    m.setList(ml);//会员基本信息

                    //签到记录
                    List<String> signs = new ArrayList<>();
                    JSONArray signArray = json.getJSONArray("sign");
                    for (int a=0;a<signArray.length();a++){

                        JSONObject signJson = signArray.getJSONObject(a);
                        String s = signJson.getString("addtime");
                        signs.add(s);

                    }
                    m.setSign(signs);

                    //抽奖记录
                    List<MembersPrize> lmps = new ArrayList<>();
                    JSONArray prizeArray = json.getJSONArray("prize");
                    for (int b=0;b<prizeArray.length();b++){

                        JSONObject prizeJson = prizeArray.getJSONObject(b);
                        MembersPrize mp = g.fromJson(prizeJson.toString(),MembersPrize.class);
                        lmps.add(mp);

                    }

                    m.setPrizes(lmps);

                    //订单记录
                    List<MembersOrder> mos = new ArrayList<>();
                    JSONArray orderArray = json.getJSONArray("order");
                    for (int c = 0;c<orderArray.length();c++){

                        JSONObject orderJson = orderArray.getJSONObject(c);
                        MembersOrder order = g.fromJson(orderJson.toString(),MembersOrder.class);
                        mos.add(order);

                    }
                    m.setOrders(mos);

                    //优惠卷记录
                    List<MembersCoupons> mcs = new ArrayList<>();
                    JSONArray couponsArray = json.getJSONArray("coupons");
                    for (int d=0;d<couponsArray.length();d++){

                        JSONObject couponsJSon = couponsArray.getJSONObject(d);
                        MembersCoupons mc = g.fromJson(couponsJSon.toString(),MembersCoupons.class);
                        mcs.add(mc);
                    }
                    m.setCoupons(mcs);
                    //兑换记录
                    List<MembersExchange> mes = new ArrayList<>();
                    JSONArray exchangeArray = json.getJSONArray("exchange");
                    for (int e = 0;e<exchangeArray.length();e++){

                        JSONObject exchangeJson = exchangeArray.getJSONObject(e);
                        MembersExchange me = g.fromJson(exchangeJson.toString(),MembersExchange.class);
                        mes.add(me);

                    }
                    m.setExchanges(mes);

                    immv.getMembersMessage(m);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
               }


            }

            @Override
            public void ErrorMessage(String message) {

            }
        });
    }

    public void addGoods(String mid,String admin, String groupID,String name,String price,String vip_price){

        os.AddGoods(mid, admin, groupID, name, price, vip_price, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {

                iagv.getAddMessage(object.toString());

            }

            @Override
            public void ErrorMessage(String message) {

            }
        });


    }

    public void SelectSingleGoods(String gid,String mid,String admin,String today,String endtime){

        os.SelectSingleGoods(mid, admin,gid, today, endtime, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {



                Log.e("进来了s","进来了s"+object.toString());

                try {

                    // 处理接口返回的json数据

                    JSONArray array = new JSONArray(object.toString());

                    List<MainOrder> lists = new ArrayList<MainOrder>();

                    for (int i = 0; i < array.length(); i++) {

                        Log.e("进来了","进来了");
                        MainOrder mw = new MainOrder();
                        JSONObject json = array.getJSONObject(i);

                        mw.setId(json.getInt("id"));
                        mw.setUid(json.getInt("uid"));
                        mw.setOrder(json.getString("order"));
                        mw.setTotal(json.getString("total"));
                        mw.setBuytime(json.getString("buytime"));
                        mw.setIntegral(json.getInt("integral"));
                        mw.setPayment(json.getInt("payment"));
                        mw.setDiscount(json.getString("discount"));
                        mw.setState(json.getInt("state"));

                        List<ViceOrder> vws = new ArrayList<ViceOrder>();

                        JSONArray ja = json.getJSONArray("detailed");
                        for (int j = 0; j < ja.length(); j++) {

                            ViceOrder vw = new ViceOrder();
                            JSONObject jj = ja.getJSONObject(j);

                            vw.setOrder_id(jj.getInt("order_id"));
                            vw.setGoods_id(jj.getInt("goods_id"));
                            vw.setWeight(jj.getString("weight"));
                            vw.setPrice(jj.getString("price"));
                            vw.setSubtotal(jj.getString("subtotal"));
                            vw.setType(jj.getInt("type"));
                            vw.setName(jj.getString("name"));

                            vws.add(vw);
                        }

                        mw.setLists(vws);

                        lists.add(mw);

                    }

                    iOrderDataView.getOrderList(lists);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("戳了","处理"+e.toString());
                }


            }

            @Override
            public void ErrorMessage(String message) {

            }
        });

    }

}
