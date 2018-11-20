package com.zmx.mian.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.gson.Gson;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.GoodsItemRankingAdapter;
import com.zmx.mian.adapter.MembersListAdapter;
import com.zmx.mian.bean.GoodsItemRankingBean;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.bean.members.MembersList;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IMembersView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-08-26 15:24
 * 类功能：会员列表
 */
public class MembersActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;
    private int load_tag = 0;//上拉或者下拉标示

    private List<MembersList> lists = new ArrayList<>();
    private MembersListAdapter adapter;

    private Paging p;


    private ImageView search_btn;

    //分类会员控件
    private RelativeLayout members_gz, members_jf, members_je, members_time;
    private TextView m_text_gz, m_text_jf, m_text_je, m_text_time, members_size;
    private View view_text_gz, view_text_jf, view_text_je, view_text_time;
    private int CHOOLS_STATE = 1, LIFT_GZ = 0, LIFT_JF = 0, LIFT_JE = 0, LIFT_TIME = 0;//1为判断用户是否点击哪个排序，lift为1是SORT_ASC 升序，为2是SORT_DESC 降序，判断是否升降排列

    @Override
    protected int getLayoutId() {
        return R.layout.activity_members;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        p = new Paging();
        BackButton(R.id.back_button);
        mRecyclerView = findViewById(R.id.members_item_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MembersListAdapter(this, lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                Bundle bundle = new Bundle();   //得到一个 bundle对象
                bundle.putString("account", lists.get(position).getAccount()); // 将获取的edittext 的值通过bundle对象分别给 account 与 password

                startActivity(MemberMessageActivity.class, bundle);

            }
        });

        members_gz = findViewById(R.id.members_gz);
        members_gz.setOnClickListener(this);
        members_jf = findViewById(R.id.members_jf);
        members_jf.setOnClickListener(this);
        members_je = findViewById(R.id.members_je);
        members_je.setOnClickListener(this);
        members_time = findViewById(R.id.members_time);
        members_time.setOnClickListener(this);
        m_text_gz = findViewById(R.id.m_text_gz);
        m_text_jf = findViewById(R.id.m_text_jf);
        m_text_je = findViewById(R.id.m_text_je);
        m_text_time = findViewById(R.id.m_text_time);
        view_text_gz = findViewById(R.id.view_text_gz);
        view_text_jf = findViewById(R.id.view_text_jf);
        view_text_je = findViewById(R.id.view_text_je);
        view_text_time = findViewById(R.id.view_text_time);

        members_size = findViewById(R.id.members_size);


        mPtrFrame = findViewById(R.id.members_list_view_frame);
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

                p.setPageNow(1);
                lists.clear();
                load_tag = 0;
                selectMember("buytime", "SORT_DESC");
            }
        });
        //上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                load_tag = 1;
                if (p.getPageNow() == p.getPageCount()) {

                    Toast("没有更多数据!");
                    mPtrFrame.loadMoreComplete(true);
                    mPtrFrame.setLoadMoreEnable(false);

                } else {

                    p.setPageNow(p.getPageNow() + 1);
                    selectMember("buytime", "SORT_DESC");
                }
            }

        });

    }

        @Override
        public void onClick (View v){
            super.onClick(v);

            String sort = "SORT_DESC";

            switch (v.getId()) {

                case R.id.search_btn:

                    startActivity(MembersSearchActivity.class);

                    break;
                case R.id.members_gz:

                    lists.clear();
                    showLoadingView("加载中...");
                    CHOOLS_STATE = 1;
                    handler.sendEmptyMessage(2);
                    p.setPageNow(1);

                    if (LIFT_GZ == 0) {

                        sort = "SORT_DESC";
                        LIFT_GZ = 1;

                    } else {

                        sort = "SORT_ASC";
                        LIFT_GZ = 0;
                    }

                    selectMember("pubtime", sort);

                    break;
                case R.id.members_jf:

                    lists.clear();
                    showLoadingView("加载中...");
                    CHOOLS_STATE = 2;
                    p.setPageNow(1);
                    handler.sendEmptyMessage(2);
                    if (LIFT_JF == 0) {

                        sort = "SORT_DESC";
                        LIFT_JF = 1;

                    } else {

                        sort = "SORT_ASC";
                        LIFT_JF = 0;
                    }

                    selectMember("integral", sort);
                    break;
                case R.id.members_je:

                    lists.clear();
                    showLoadingView("加载中...");
                    CHOOLS_STATE = 3;
                    p.setPageNow(1);
                    handler.sendEmptyMessage(2);
                    if (LIFT_JE == 0) {

                        sort = "SORT_DESC";
                        LIFT_JE = 1;

                    } else {

                        sort = "SORT_ASC";
                        LIFT_JE = 0;
                    }
                    selectMember("total", sort);
                    break;
                case R.id.members_time:

                    lists.clear();
                    showLoadingView("加载中...");
                    CHOOLS_STATE = 4;
                    p.setPageNow(1);
                    handler.sendEmptyMessage(2);
                    if (LIFT_TIME == 0) {

                        sort = "SORT_DESC";
                        LIFT_TIME = 1;

                    } else {

                        sort = "SORT_ASC";
                        LIFT_TIME = 0;
                    }
                    selectMember("buytime", sort);
                    break;
            }

        }

        private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {

                    case 0:

                        try {

                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            if(jsonObject.getString("code").equals("1")){

                                JSONArray jsonArray = jsonObject.getJSONArray("list");

                                for (int i = 0;i<jsonArray.length();i++){

                                    JSONObject json = jsonArray.getJSONObject(i);
                                    Gson g = new Gson();
                                    MembersList ml = g.fromJson(json.toString(),MembersList.class);
                                    lists.add(ml);

                                }

                                handler.sendEmptyMessage(1);


                            }


                        } catch (JSONException e) {

                            Toast("未知错误，请联系客服！");
                            e.printStackTrace();
                        }
                        break;
                    case 1:

                        members_size.setText("会员列表（" + lists.size() + "个） ");
                        initAdapter();
                        dismissLoadingView();
                        break;

                    case 2:

                        initChoose(CHOOLS_STATE);

                        break;



                }

            }
        };

        /**
         * 更新适配器数据
         */

    public void initAdapter() {

        adapter.notifyData(CHOOLS_STATE);
        mPtrFrame.refreshComplete();
        if (load_tag == 0) {

            mPtrFrame.setLoadMoreEnable(true);

        } else {

            mPtrFrame.loadMoreComplete(true);

        }
    }


    public void initChoose(int state) {

        switch (state) {

            case 1:

                m_text_gz.setTextColor(getResources().getColor(R.color.tou));
                view_text_gz.setBackgroundColor(getResources().getColor(R.color.tou));
                m_text_jf.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_jf.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_je.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_je.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_time.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_time.setBackgroundColor(getResources().getColor(R.color.tv_color));


                break;
            case 2:

                m_text_gz.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_gz.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_jf.setTextColor(getResources().getColor(R.color.tou));
                view_text_jf.setBackgroundColor(getResources().getColor(R.color.tou));
                m_text_je.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_je.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_time.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_time.setBackgroundColor(getResources().getColor(R.color.tv_color));

                break;
            case 3:
                m_text_gz.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_gz.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_jf.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_jf.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_je.setTextColor(getResources().getColor(R.color.tou));
                view_text_je.setBackgroundColor(getResources().getColor(R.color.tou));
                m_text_time.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_time.setBackgroundColor(getResources().getColor(R.color.tv_color));
                break;
            case 4:
                m_text_gz.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_gz.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_jf.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_jf.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_je.setTextColor(getResources().getColor(R.color.tv_gray_deep));
                view_text_je.setBackgroundColor(getResources().getColor(R.color.tv_color));
                m_text_time.setTextColor(getResources().getColor(R.color.tou));
                view_text_time.setBackgroundColor(getResources().getColor(R.color.tou));
                break;

        }

    }

    //查询会员列表
    public void selectMember(String field,String sort){

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(MyApplication.getName()));
        params.put("account", "1");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("thisPage", p.getPageNow() + "");
        params.put("num",  p.getPageSize() + "");
        params.put("field",  field);
        params.put("sort",  sort);
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.user/userlist", params, handler, 0, 404);

    }


}


