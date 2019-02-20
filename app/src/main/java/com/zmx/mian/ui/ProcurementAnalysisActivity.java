package com.zmx.mian.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.google.gson.Gson;
import com.zmx.mian.R;
import com.zmx.mian.adapter.ProcurementAnalysisAdapter;
import com.zmx.mian.adapter.StockManagementAdapter;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.bean.ProcurementAnalysis;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.http.UrlConfig;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-12-09 12:14
 * 类功能：采购分析·
 */

public class ProcurementAnalysisActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private List<ProcurementAnalysis> lists;
    //RecyclerView自定义Adapter
    private ProcurementAnalysisAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;
    private Paging p;
    private int load_tag = 0;//上拉或者下拉标示

    private TextView tile_time;
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd;

    //查询的开始结束时间
    public String startDate = Tools.DateConversion(new Date());
    public String endDate = Tools.DateConversion(new Date());


    @Override
    protected int getLayoutId() {
        return R.layout.activity_procurement_analysis;
    }

    @Override
    protected void initViews() {


        p = new Paging();
        tile_time = findViewById(R.id.tile_time);
        tile_time.setText("(" + startDate + ")");
        tile_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomTimePicker();
            }
        });

        lists = new ArrayList<>();
        mRecyclerView = findViewById(R.id.stock_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new ProcurementAnalysisAdapter(this, lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                // 通过Intent传递对象给Service
            }
        });
        mPtrFrame = findViewById(R.id.stock_rotate_header_list_view_frame);
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

                lists.clear();
                p.setPageNow(1);
                load_tag = 0;
                loadingData();
            }
        });
//上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

                load_tag = 1;
                if (p.getPageNow() == p.getPageCount()) {

                    Toast("没有更多数据");
                    mPtrFrame.loadMoreComplete(true);
                    mPtrFrame.setLoadMoreEnable(false);

                } else {

                    p.setPageNow(p.getPageNow() + 1);
                    loadingData();
                }


            }
        });


    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    try {

                        dismissLoadingView();

                        mRecyclerView.scrollToPosition(0);//回到顶部
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.getString("code").equals("1")){

                            JSONArray jsonArray = jsonObject.getJSONArray("list");

                            for (int i = 0;i<jsonArray.length();i++){

                                JSONObject json = jsonArray.getJSONObject(i);
                                Gson g = new Gson();
                                ProcurementAnalysis ml = g.fromJson(json.toString(),ProcurementAnalysis.class);
                                lists.add(ml);

                            }

                            initAdapter();
                        }


                    } catch (JSONException e) {

                        Toast("未知错误，请联系客服！");
                        e.printStackTrace();
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
        if (load_tag == 0) {

            if(lists.size()>0){
                mPtrFrame.setLoadMoreEnable(true);
            }

        } else {

            mPtrFrame.loadMoreComplete(true);

        }
    }

    public void showCustomTimePicker() {

        String beginDeadTime = "2017-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {

                    lists.clear();
                    tile_time.setText("(" + startTime.replace("-", ".") + "至" + endTime.replace("-", ".") + ")");
                    startDate = startTime;
                    endDate = endTime;
                    showLoadingView("加载中...");
                    loadingData();
                }
            });

            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.recoverButtonState();
            mDoubleTimeSelectDialog.show();
        }
    }


    //加载服务器的订单列表
    public void loadingData(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
        params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("today", startDate);
        params.put("endtime", endDate);

        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.PURCHASE_DATA, params, handler, 1, 404);


    }


}
