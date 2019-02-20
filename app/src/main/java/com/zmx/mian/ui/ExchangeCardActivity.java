package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.google.gson.Gson;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.CardVolumeAdapter;
import com.zmx.mian.bean.CardVolumeBean;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.http.UrlConfig;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-22 17:51
 * 类功能：兑换优惠卷
 */
public class ExchangeCardActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private ArrayList<CardVolumeBean> lists = new ArrayList<>();
    //RecyclerView自定义Adapter
    private CardVolumeAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;
    private ImageView no_data;
    private int pos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_card;
    }

    @Override
    protected void initViews() {


        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        no_data = findViewById(R.id.no_data);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new CardVolumeAdapter(this, lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, final int position) {

                new AlertDialog.Builder(mActivity)
                        .setTitle("移除优惠卷？")
                        .setMessage("确定移除该优惠卷？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                pos = position;
                                showLoadingView("移除中...");
                                DeleteCard(lists.get(position).getPid()+"");
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            }
        });

        mPtrFrame = findViewById(R.id.rotate_header_list_view_frame);
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

                loadData();
                mPtrFrame.refreshComplete();
            }
        });
        //上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:
                    lists.clear();

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            JSONObject json = jsonObject.getJSONObject("list");

                            JSONArray jsonArray = json.getJSONArray("checkList");
                            Gson g = new Gson();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                CardVolumeBean cb = g.fromJson(object.toString(), CardVolumeBean.class);
                                lists.add(cb);

                            }
                            if(lists.size()<=0){

                                no_data.setVisibility(View.VISIBLE);

                            }
                            adapter.notifyDataSetChanged();

                        }else{
                            no_data.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 2:

                dismissLoadingView();
                try {

                    JSONObject jsonObject = new JSONObject(msg.obj.toString());

                    if (jsonObject.getString("code").equals("1")) {

                        Toast(jsonObject.getString("content"));
                        lists.remove(pos);
                        //判断是否为null了，没有数据就显示图片出来
                        if(lists.size() <= 0){
                            no_data.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();

                    } else {

                        Toast(jsonObject.getString("content"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    break;

            }

        }
    };

    public void loadData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.SELECT_EXCHANGE, params, handler, 1, 404);

    }

    //从奖池中移走该优惠卷
    public void DeleteCard(String pid) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("pid", pid);
        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.DELETE_EXCHANGE, params, handler, 2, 404);

    }


}
