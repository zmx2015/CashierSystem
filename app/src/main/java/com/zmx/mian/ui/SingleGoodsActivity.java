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
import com.zmx.mian.R;
import com.zmx.mian.adapter.OrderDataAdapter;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IOrderDataView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-12 14:22
 * 类功能：查询某个商品的订单历史
 */
public class SingleGoodsActivity extends BaseActivity implements IOrderDataView {


    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private ArrayList<MainOrder> mo = new ArrayList<>();
    //RecyclerView自定义Adapter
    private OrderDataAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;

    private OrderPresenter op;
    private TextView choose_time;

    private int load_tag = 0;//上拉或者下拉标示

    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd;


    private LoadingDialog mLoadingDialog; //显示正在加载的对话框

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

        op = new OrderPresenter(this);
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

                load_tag = 0;

                String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
                String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, "");

                op.SelectSingleGoods(gid, mid, name, startDate, endDate);

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

                    initAdapter();

                    break;

                case 2:

                    String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
                    String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, "");

                    op.SelectSingleGoods(gid, mid, name, startDate, endDate);

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

    @Override
    public void getOrderList(List<MainOrder> lists) {


        for (MainOrder m : lists) {

            mo.add(m);

        }

        handler.sendEmptyMessage(1);

    }

    @Override
    public void ErrerMessage() {

    }


}


