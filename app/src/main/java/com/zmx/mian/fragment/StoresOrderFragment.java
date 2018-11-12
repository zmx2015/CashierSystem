package com.zmx.mian.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zmx.mian.adapter.OrderDataAdapter;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.OrderDataActivity;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IOrderDataView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-23 0:15
 * 类功能：门店订单
 */

public class StoresOrderFragment extends  BaseFragment implements View.OnClickListener,IOrderDataView {


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

    //查询的开始结束时间
    public String startDate = Tools.DateConversion(new Date());
    public String endDate = Tools.DateConversion(new Date());

    public String mid,name;
    private ImageView no_data;


    //分类会员控件
    private RelativeLayout s_o_all_layout1,s_o_all_layout2,s_o_all_layout3,s_o_all_layout4;
    private TextView s_o_all_text1,s_o_all_text2,s_o_all_text3,s_o_all_text4;
    private View s_o_all_view_1,s_o_all_view_2,s_o_all_view_3,s_o_all_view_4;
    private int CHOOLS_STATE = 1,LIFT_GZ = 0,LIFT_JF = 0,LIFT_JE = 0,LIFT_TIME = 0;//1为判断用户是否点击哪个排序，lift为1是SORT_ASC 升序，为2是SORT_DESC 降序，判断是否升降排列



    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";

    public static StoresOrderFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        StoresOrderFragment pageFragment = new StoresOrderFragment();
        pageFragment.setArguments(args);

        return pageFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stores_order_fragment,container,false);

        mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
        name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, "");

        p = new Paging();
        op = new OrderPresenter(this);
        no_data = view.findViewById(R.id.no_data);
        mRecyclerView = view.findViewById(R.id.moder_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new OrderDataAdapter(mActivity, mo);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        mPtrFrame = view.findViewById(R.id.order_rotate_header_list_view_frame);
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
                op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);

            }
        });
//上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

                load_tag = 1;
                if (p.getPageNow() == p.getPageCount()) {

                    Toast.makeText(mActivity, "没有更多数据", Toast.LENGTH_SHORT)
                            .show();
                    mPtrFrame.loadMoreComplete(true);
                    mPtrFrame.setLoadMoreEnable(false);

                } else {

                    p.setPageNow(p.getPageNow() + 1);
                    op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);

                }


            }
        });


        s_o_all_layout1 = view.findViewById(R.id.s_o_all_layout1);
        s_o_all_layout1.setOnClickListener(this);
        s_o_all_layout2 = view.findViewById(R.id.s_o_all_layout2);
        s_o_all_layout2.setOnClickListener(this);
        s_o_all_layout3 = view.findViewById(R.id.s_o_all_layout3);
        s_o_all_layout3.setOnClickListener(this);
        s_o_all_layout4 = view.findViewById(R.id.s_o_all_layout4);
        s_o_all_layout4.setOnClickListener(this);
        s_o_all_text1 = view.findViewById(R.id.s_o_all_text1);
        s_o_all_text2 = view.findViewById(R.id.s_o_all_text2);
        s_o_all_text3 = view.findViewById(R.id.s_o_all_text3);
        s_o_all_text4 = view.findViewById(R.id.s_o_all_text4);
        s_o_all_view_1 = view.findViewById(R.id.s_o_all_view_1);
        s_o_all_view_2 = view.findViewById(R.id.s_o_all_view_2);
        s_o_all_view_3 = view.findViewById(R.id.s_o_all_view_3);
        s_o_all_view_4 = view.findViewById(R.id.s_o_all_view_4);

        return view;
    }


    @Override
    protected void initView() {

    }


    @Override
    public void onClick(View v) {


        String sort = "SORT_DESC";

        switch (v.getId()){

            case R.id.s_o_all_layout1:

                mo.clear();
                mRecyclerView.scrollToPosition(0);//回到顶部
                showLoadingView("加载中...");
                CHOOLS_STATE = 1;
                handler.sendEmptyMessage(2);

                if(LIFT_GZ == 0){

                    sort = "SORT_DESC";
                    LIFT_GZ = 1;

                }else{

                    sort =  "SORT_ASC";
                    LIFT_GZ = 0;
                }

                p.setPageNow(1);
                load_tag = 0;
                op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);

                break;
            case R.id.s_o_all_layout2:

                mo.clear();
                showLoadingView("加载中...");
                CHOOLS_STATE = 2;
                handler.sendEmptyMessage(2);
                if(LIFT_JF == 0){

                    sort = "SORT_DESC";
                    LIFT_JF = 1;

                }else{

                    sort =  "SORT_ASC";
                    LIFT_JF = 0;
                }

                p.setPageNow(1);
                load_tag = 0;
                op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);


                break;
            case R.id.s_o_all_layout3:
                mo.clear();
                showLoadingView("加载中...");
                CHOOLS_STATE = 3;
                handler.sendEmptyMessage(2);
                if(LIFT_JE == 0){

                    sort = "SORT_DESC";
                    LIFT_JE = 1;

                }else{

                    sort =  "SORT_ASC";
                    LIFT_JE = 0;
                }

                p.setPageNow(1);
                load_tag = 0;
                op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);


                break;
            case R.id.s_o_all_layout4:
                mo.clear();
                showLoadingView("加载中...");
                CHOOLS_STATE = 4;
                handler.sendEmptyMessage(2);
                if(LIFT_TIME == 0){

                    sort = "SORT_DESC";
                    LIFT_TIME = 1;

                }else{

                    sort =  "SORT_ASC";
                    LIFT_TIME = 0;
                }

                p.setPageNow(1);
                load_tag = 0;
                op.getOrderMessageD(name, startDate, endDate, p.getPageNow() + "", p.getPageSize() + "", name, mid);


                break;
        }

    }


    public void initChoose(int state){

        switch (state){

            case 1:

                s_o_all_text1.setTextColor(getResources().getColor(R.color.tou));
                s_o_all_view_1.setBackgroundColor(getResources().getColor(R.color.tou));
                s_o_all_text2.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_2.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text3.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_3.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text4.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_4.setBackgroundColor(getResources().getColor(R.color.tv_color));


                break;
            case 2:

                s_o_all_text1.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_1.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text2.setTextColor(getResources().getColor(R.color.tou));
                s_o_all_view_2.setBackgroundColor(getResources().getColor(R.color.tou));
                s_o_all_text3.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_3.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text4.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_4.setBackgroundColor(getResources().getColor(R.color.tv_color));

                break;
            case 3:
                s_o_all_text1.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_1.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text2.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_2.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text3.setTextColor(getResources().getColor(R.color.tou));
                s_o_all_view_3.setBackgroundColor(getResources().getColor(R.color.tou));
                s_o_all_text4.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_4.setBackgroundColor(getResources().getColor(R.color.tv_color));
                break;
            case 4:
                s_o_all_text1.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_1.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text2.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_2.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text3.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                s_o_all_view_3.setBackgroundColor(getResources().getColor(R.color.tv_color));
                s_o_all_text4.setTextColor(getResources().getColor(R.color.tou));
                s_o_all_view_4.setBackgroundColor(getResources().getColor(R.color.tou));
                break;

        }

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    if(mo.size()>0){
                        no_data.setVisibility(View.GONE);
                    }else {
                        no_data.setVisibility(View.VISIBLE);
                    }

                    dismissLoadingView();
                    initAdapter();

                    break;

                case 2:

                    initChoose(CHOOLS_STATE);

                    break;

            }

        }
    };


    @Override
    public void getOrderList(List<MainOrder> lists) {


        int pagenow = 1;

        for (MainOrder m : lists) {
            mo.add(m);
            pagenow = m.getPageNum();

        }

        p.setRowCount(pagenow);

        handler.sendEmptyMessage(1);


    }

    @Override
    public void ErrerMessage() {

    }

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


}
