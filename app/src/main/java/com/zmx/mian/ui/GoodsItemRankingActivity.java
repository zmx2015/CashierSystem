package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.GoodsItemRankingAdapter;
import com.zmx.mian.adapter.OrderDataAdapter;
import com.zmx.mian.bean.GoodsItemRankingBean;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IGoodsItemRankingView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 23:38
 * 类功能：商品单品销量排行榜
 */
public class GoodsItemRankingActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RelativeLayout relayout;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private ArrayList<GoodsItemRankingBean> lists = new ArrayList<>();
    //RecyclerView自定义Adapter
    private GoodsItemRankingAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;

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

    private ImageView no_data;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_item_ranking;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        relayout = findViewById(R.id.relayout);
        relayout.setOnClickListener(this);
        tile_time = findViewById(R.id.tile_time);
        tile_time.setText("(" + startDate + ")");
        no_data = findViewById(R.id.no_data);
        BackButton(R.id.back_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.goods_item_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new GoodsItemRankingAdapter(this, lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                Bundle bundle = new Bundle();   //得到一个 bundle对象
                bundle.putString("gid", lists.get(position).getGid()); // 将获取的edittext 的值通过bundle对象分别给 account 与 password
                bundle.putString("startDate", startDate);
                bundle.putString("endDate", endDate);
                bundle.putString("g_name", lists.get(position).getName());
                startActivity(SingleGoodsActivity.class, bundle);

            }
        });

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.goods_rotate_header_list_view_frame);
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

                case 0:

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            JSONArray array = jsonObject.getJSONArray("list");
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


                        }
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case 1:

                    //判断有没有数据，没有就显示提示
                    if (lists.size() > 0) {

                        no_data.setVisibility(View.GONE);

                    } else {

                        no_data.setVisibility(View.VISIBLE);

                    }

                    dismissLoadingView();
                    initAdapter();
                    break;

                case 2:

                    selectData();

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
                    handler.sendEmptyMessage(2);

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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.relayout:

                showCustomTimePicker();

                break;

        }

    }

    public void selectData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(MyApplication.getName()));
        params.put("account", "1");
        params.put("today", startDate);
        params.put("endtime", endDate);
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("type", "all");

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/goodsCount", params, handler, 0, 404);


    }

}
