package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-22 21:46
 * 类功能：会员注册赠送配置
 */
public class RegisteredMembersActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private ArrayList<CardVolumeBean> lists = new ArrayList<>();
    //RecyclerView自定义Adapter
    private CardVolumeAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;

    private RelativeLayout relative1;
    private ImageView no_data;
    private int pos;
    private TextView jifen;

    private String red = "";//注册赠送的积分
    private String red_id = "";//移除优惠卷

    @Override
    protected int getLayoutId() {
        return R.layout.activity_registered_members;
    }

    @Override
    protected void initViews() {

        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        mRecyclerView = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        no_data = findViewById(R.id.no_data);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new CardVolumeAdapter(this, lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        jifen = findViewById(R.id.jifen);

        relative1 = findViewById(R.id.relative1);
        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert_label();
            }
        });

        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, final int position) {

                new AlertDialog.Builder(mActivity)
                        .setTitle("移除优惠卷？")
                        .setMessage("确定移除该优惠卷？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                showLoadingView("移除中...");
                                pos = position;
                                StringBuffer buf = new StringBuffer();
                                buf.append("");
                                for (int j = 0; j < lists.size(); j++) {

                                    if (j != pos) {

                                        buf.append(lists.get(j).getCid() + ",");

                                    }

                                }

                                red_id = buf.toString();
                                if(!TextUtils.isEmpty(red_id)){

                                    red_id = red_id.substring(0,red_id.length() - 1);

                                }

                                DeleteCard("couponid",red_id,2);
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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    lists.clear();

                    Log.e("返回的数据",""+msg.obj.toString());

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            JSONObject json = jsonObject.getJSONObject("list");
                            JSONObject config = json.getJSONObject("config");
                            red = config.getString("red");
                            JSONArray jsonArray = json.getJSONArray("coupons");
                            Gson g = new Gson();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                CardVolumeBean cb = g.fromJson(object.toString(), CardVolumeBean.class);
                                lists.add(cb);

                            }

                            if (lists.size() <= 0) {

                                no_data.setVisibility(View.VISIBLE);

                            }
                            jifen.setText(red + "分");
                            adapter.notifyDataSetChanged();

                        }else{

                            JSONObject jsons = jsonObject.getJSONObject("list");
                            JSONObject configs = jsons.getJSONObject("config");
                            red = configs.getString("red");
                            no_data.setVisibility(View.VISIBLE);
                            jifen.setText(red + "分");
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

                case 3:

                    dismissLoadingView();
                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            jifen.setText(red + "分");
                            Toast(jsonObject.getString("content"));

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
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Coupon/give", params, handler, 1, 404);

    }

    //修改配置
    public void DeleteCard(String name,String val,int h) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("name", name);
        params.put("val", val);
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.config/setconfig", params, handler, h, 404);

    }


    public void alert_label() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的
        final EditText et = view.findViewById(R.id.editText);
        et.setHint("填写注册赠送积分");
        builder.setTitle("填写注册赠送积分");

        final AlertDialog dialog = builder.create();
        dialog.setView(view);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et.getText().toString()) || new Tools().isNumeric(et.getText().toString())) {

                    showLoadingView("修改中...");
                    red = et.getText().toString();
                    DeleteCard("red",et.getText().toString(),3);
                    dialog.dismiss();

                } else {

                    Toast("非法输入!");
                }

            }
        });


    }



}

