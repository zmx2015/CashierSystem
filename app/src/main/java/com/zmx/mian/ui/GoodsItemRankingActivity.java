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
import com.zmx.mian.R;
import com.zmx.mian.adapter.GoodsItemRankingAdapter;
import com.zmx.mian.adapter.OrderDataAdapter;
import com.zmx.mian.bean.GoodsItemRankingBean;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IGoodsItemRankingView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 23:38
 * 类功能：商品单品销量排行榜
 */
public class GoodsItemRankingActivity extends BaseActivity implements IGoodsItemRankingView,View.OnClickListener{

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

    private OrderPresenter op;

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

    private LoadingDialog mLoadingDialog; //显示正在加载的对话框

    //查询的开始结束时间
    public String startDate=Tools.DateConversion(new Date());
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
        tile_time.setText("("+startDate+")");
        no_data = findViewById(R.id.no_data);
        BackButton(R.id.back_button);
        op = new OrderPresenter(this);
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
                startActivity(SingleGoodsActivity.class,bundle);

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
                String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id,"");
                String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name,"");
                op.getGoodsItemRanking(name, startDate, endDate,name,mid);

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

                    //判断有没有数据，没有就显示提示
                    if (lists.size() > 0) {

                        no_data.setVisibility(View.GONE);

                    } else {

                        no_data.setVisibility(View.VISIBLE);

                    }

                    hideLoading();
                    initAdapter();
                    break;

                case 2:

                    String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id,"");
                    String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name,"");
                    op.getGoodsItemRanking(name, startDate, endDate,name,mid);

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

    @Override
    public void getOrderList(List<GoodsItemRankingBean> lists) {

        for (GoodsItemRankingBean g:lists){
            this.lists.add(g);
        }
        handler.sendEmptyMessage(1);
    }

    @Override
    public void ErrerMessage() {

    }


    public void showCustomTimePicker() {

        String beginDeadTime = "2017-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {

                    lists.clear();
                    tile_time.setText("("+startTime.replace("-", ".") + "至" + endTime.replace("-", ".")+")");
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
            mLoadingDialog = new LoadingDialog(this,"正在加载...", false);
        }
        mLoadingDialog.show();
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


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.relayout:

                showCustomTimePicker();

                break;

        }

    }

    @Override
    protected void onDestroy() {

        if (mLoadingDialog != null) {

            mLoadingDialog.dismiss();

        }
        super.onDestroy();
    }
}
