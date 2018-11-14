package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.mian.R;
import com.zmx.mian.adapter.OrderDataAdapter;
import com.zmx.mian.adapter.StoreListAdapter;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IOrderDataView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 0:03
 * 类功能：订单管理界面
 */
public class  OrderDataActivity extends BaseActivity implements IOrderDataView, View.OnClickListener {

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

    private Paging p;

    private int load_tag = 0;//上拉或者下拉标示

    private TextView choose_time, total_of, total_price;

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
    public String startDate = Tools.DateConversion(new Date());
    public String endDate = Tools.DateConversion(new Date());

    private String allTotal = "0";//总金额
    private String nums = "0";//订单数
    private ImageView no_data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_data;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);
        no_data = findViewById(R.id.no_data);
        total_of = findViewById(R.id.total_of);
        total_price = findViewById(R.id.total_price);
        choose_time = findViewById(R.id.choose_time);
        choose_time.setText(startDate + "（可选）");
        choose_time.setOnClickListener(this);
        p = new Paging();
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

                p.setPageNow(1);
                load_tag = 0;

                String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
                String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, "");

                op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);

            }
        });
//上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

                load_tag = 1;
                if (p.getPageNow() == p.getPageCount()) {

                    Toast.makeText(OrderDataActivity.this, "没有更多数据", Toast.LENGTH_SHORT)
                            .show();
                    mPtrFrame.loadMoreComplete(true);
                    mPtrFrame.setLoadMoreEnable(false);

                } else {

                    String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
                    String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, "");

                    p.setPageNow(p.getPageNow() + 1);
                    op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);

                }


            }
        });

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:


                    //判断有没有数据，没有就显示提示
                    if (mo.size() > 0) {

                        no_data.setVisibility(View.GONE);

                    } else {

                        no_data.setVisibility(View.VISIBLE);

                    }
                    hideLoading();


                    String tn="人次：<font color='#FF0000'>"+nums+"次</font>";
                    total_of.setText(Html.fromHtml(tn));
                    String ta = "销量：<font color='#FF0000'>"+allTotal+"元</font>";
                    total_price.setText(Html.fromHtml(ta));
                    mRecyclerView.scrollToPosition(0);//回到顶部
                    initAdapter();

                    break;

                case 2:

                    String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
                    String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, "");

                    p.setPageNow(p.getPageNow() + 1);
                    op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);
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

            if(mo.size()>0){
                mPtrFrame.setLoadMoreEnable(true);
            }

        } else {

            mPtrFrame.loadMoreComplete(true);

        }
    }

    @Override
    public void getOrderList(List<MainOrder> lists) {

        Log.e("拿到数据", "数据大小" + lists.size());

        int pagenow = 1;

        for (MainOrder m : lists) {

            allTotal = m.getAllTotal() + "";
            nums = m.getCouns() + "";
            mo.add(m);
            pagenow = m.getPageNum();

        }

        p.setRowCount(pagenow);

        handler.sendEmptyMessage(1);

    }

    @Override
    public void ErrerMessage() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.choose_time:
                showCustomTimePicker();

                break;

        }

    }

    public void showCustomTimePicker() {

        String beginDeadTime = "2017-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {

                    mo.clear();

                    choose_time.setText(startTime.replace("-", ".") + "至\n" + endTime.replace("-", "."));

                    startDate = startTime;
                    endDate = endTime;
                    showLoading();
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

    /**
     * 显示加载的进度款
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, "正在加载...", false);
        }
        mLoadingDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadingDialog != null){

            mLoadingDialog.dismiss();
        }
    }

    /**
     * 隐藏加载的进度框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });

        }
    }


}



