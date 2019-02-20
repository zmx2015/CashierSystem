package com.zmx.mian.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.OrderDataAdapter;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.http.UrlConfig;
import com.zmx.mian.util.ToastUtil;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-23 18:29
 * 类功能：商城订单列表
 */
public class OnlineOrderFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private ArrayList<MainOrder> mo = new ArrayList<>();
    //RecyclerView自定义Adapter
    private OrderDataAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;

    private Paging p;

    private int load_tag = 0;//上拉或者下拉标示

    //查询的开始结束时间
    public String startDate = Tools.DateConversion(new Date());
    public String endDate = Tools.DateConversion(new Date());

    public String mid,name;
    private ImageView no_data;
    private int state=1;

    //分类会员控件
    private RelativeLayout s_o_all_layout1,s_o_all_layout2,s_o_all_layout3,s_o_all_layout4;
    private TextView s_o_all_text1,s_o_all_text2,s_o_all_text3,s_o_all_text4;
    private View s_o_all_view_1,s_o_all_view_2,s_o_all_view_3,s_o_all_view_4;
    private int CHOOLS_STATE = 1;//1为判断用户是否点击哪个排序，lift为1是SORT_ASC 升序，为2是SORT_DESC 降序，判断是否升降排列


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.online_order_fragment,container,false);
        p = new Paging();
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
                loadData();

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
                    loadData();
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

        loadData();
        return view;
    }


    @Override
    protected void initView() {
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 0:
                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        JSONObject page = jsonObject.getJSONObject("page");
                        JSONArray array = jsonObject.getJSONArray("list");
                        int nums = page.getInt("pageCount");
                        int allTotal = page.getInt("allTotal");
                        int couns = page.getInt("nums");

                        p.setRowCount(nums);

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
                            mw.setMo_classify(json.getString("detailed"));

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

                            mo.add(mw);

                        }

                        if (state == 1){

                            uou.setNumber(mo.size());

                        }

                        handler.sendEmptyMessage(1);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

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


    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";

    public static OnlineOrderFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        OnlineOrderFragment pageFragment = new OnlineOrderFragment();
        pageFragment.setArguments(args);

        return pageFragment;
    }


    public void loadData(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(MyApplication.getName()));
        params.put("account", "1");
        params.put("today", startDate);
        params.put("endtime", endDate);
        params.put("thisPage", p.getPageNow() + "");
        params.put("num",  p.getPageSize() + "");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("state", state+"");
        params.put("payment", "1,2,3,4");
        params.put("classify", "1,2");
        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.SELECT_ORDER_TWO_LIST, params, handler, 0, 404);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.s_o_all_layout1:

                mo.clear();
                mRecyclerView.scrollToPosition(0);//回到顶部
                showLoadingView("加载中...");
                CHOOLS_STATE = 1;
                state=1;
                handler.sendEmptyMessage(2);
                p.setPageNow(1);
                load_tag = 0;
                loadData();//加载数据
                break;
            case R.id.s_o_all_layout2:

                mo.clear();
                showLoadingView("加载中...");
                mRecyclerView.scrollToPosition(0);//回到顶部
                CHOOLS_STATE = 2;
                state=2;
                handler.sendEmptyMessage(2);
                p.setPageNow(1);
                load_tag = 0;
                loadData();//加载数据

                break;
            case R.id.s_o_all_layout3:
                mo.clear();
                showLoadingView("加载中...");
                mRecyclerView.scrollToPosition(0);//回到顶部
                CHOOLS_STATE = 3;
                state=0;
                handler.sendEmptyMessage(2);
                p.setPageNow(1);
                load_tag = 0;
                loadData();//加载数据

                break;
            case R.id.s_o_all_layout4:
                mo.clear();
                showLoadingView("加载中...");
                mRecyclerView.scrollToPosition(0);//回到顶部
                CHOOLS_STATE = 4;
                state=1;
                handler.sendEmptyMessage(2);
                p.setPageNow(1);
                load_tag = 0;
                loadData();//加载数据

                break;
        }

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

            }else{

                mPtrFrame.setLoadMoreEnable(false);
            }


        } else {

            mPtrFrame.loadMoreComplete(true);

        }
    }


    public void initChoose(int state) {

        switch (state) {

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

    //未读消息
    UnreadOrderNun uou;

    public interface UnreadOrderNun{

        void setNumber(int num);

    }

    public void setUnreadOrderNun(UnreadOrderNun uou){

        this.uou = uou;

    };

}
