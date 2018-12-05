package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.OrderDataAdapter;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IOrderDataView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-12 14:22
 * 类功能：查询某个商品的订单历史
 */
public class SingleGoodsActivity extends BaseActivity{


    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private ArrayList<MainOrder> mo = new ArrayList<>();
    //RecyclerView自定义Adapter
    private OrderDataAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;
    private TextView choose_time;


    //查询的开始结束时间
    public String startDate;
    public String endDate;

    private String gid;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_goods;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        BackButton(R.id.back_button);
        gid = getIntent().getStringExtra("gid");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        choose_time = findViewById(R.id.choose_time);
        choose_time.setText(getIntent().getStringExtra("g_name")+"("+startDate+"至"+endDate+")");

        mRecyclerView = findViewById(R.id.moder_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new OrderDataAdapter(this, mo);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.order_rotate_header_list_view_frame);
//下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
//下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
// default is false
        mPtrFrame.setPullToRefresh(false);
// default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
//进入Activity就进行自动下拉刷新
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
//下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mo.clear();

                selectData();

            }
        });
//上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {


            }
        });

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    try {

                        JSONObject bodys = new JSONObject(msg.obj.toString());

                        // 处理接口返回的json数据
                        JSONArray array = bodys.getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {

                            MainOrder mw = new MainOrder();
                            JSONObject json = array.getJSONObject(i);

                            mw.setPageNum(1);
                            mw.setAllTotal(1);
                            mw.setCouns(1);
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
                            mw.setMo_classify(json.getString("classify"));
                            List<ViceOrder> vws = new ArrayList<>();

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

                            mo.add(mw);

                        }

                        initAdapter();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("未知错误",""+e.toString());
                        Toast("未知错误，请联系客服！");
                    }


                    break;


            }

        }
    };

    /**
     * 更新适配器数据
     */
    public void initAdapter() {

        mAdapter.notifyDataSetChanged();
        mPtrFrame.refreshComplete();
        mPtrFrame.setLoadMoreEnable(false);//设置不上拉刷新

    }

    public void selectData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(MyApplication.getName()));
        params.put("account", "1");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("today", startDate);
        params.put("endtime", endDate);
        params.put("gid", gid);

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.order/odsList", params, handler, 1, 404);


    }


}


