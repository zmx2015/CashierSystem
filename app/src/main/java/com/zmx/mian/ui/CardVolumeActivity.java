package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.google.gson.Gson;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.CardVolumeAdapter;
import com.zmx.mian.adapter.GoodsItemRankingAdapter;
import com.zmx.mian.bean.CardVolumeBean;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.fragment.HomeFragment;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-21 17:03
 * 类功能：卡卷管理
 */
public class CardVolumeActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private ArrayList<CardVolumeBean> lists = new ArrayList<>();
    //RecyclerView自定义Adapter
    private CardVolumeAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;

    private ImageButton imageButton;

    private ImageView no_data;
    private int pos;//删除的itme；

    private RelativeLayout relative1, relative2, relative3, relative4;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_card_volume;
    }

    @Override
    protected void initViews() {

        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        imageButton = findViewById(R.id.add_button);
        imageButton.setOnClickListener(this);

        relative1 = findViewById(R.id.relative1);
        relative1.setOnClickListener(this);
        relative2 = findViewById(R.id.relative2);
        relative2.setOnClickListener(this);
        relative3 = findViewById(R.id.relative3);
        relative3.setOnClickListener(this);
        relative4 = findViewById(R.id.relative4);
        relative4.setOnClickListener(this);
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

                popWindow(position);

            }
        });

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.relative1:

                startActivity(LuckyDrawActivity.class);

                break;
            case R.id.relative2:

                startActivity(ExchangeCardActivity.class);

                break;

            case R.id.relative3:

                startActivity(RegisteredMembersActivity.class);

                break;

            case R.id.relative4:

                Toast("正在马不停蹄地开发中...");
                break;
            case R.id.add_button:

                startActivity(AddCardVolumeActivity.class);

                break;

        }

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    lists.clear();

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            Gson g = new Gson();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                CardVolumeBean cb = g.fromJson(object.toString(), CardVolumeBean.class);
                                lists.add(cb);

                            }
                            adapter.notifyDataSetChanged();

                        } else {

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

                            Toast(jsonObject.getString("content"));

                        } else {

                            Toast(jsonObject.getString("content"));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 4:

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            JSONObject json = jsonObject.getJSONObject("list");
                            JSONObject config = json.getJSONObject("config");

                            String couponid = config.getString("couponid");

                            if(!TextUtils.isEmpty(couponid)){

                                StringBuilder sb = new StringBuilder(couponid);
                                sb.append(","+lists.get(pos).getCid());
                                couponid = sb.toString();

                            }else{
                                couponid = lists.get(pos).getCid()+"";
                            }

                            DeleteCard("couponid",couponid);


                        }else{

                            DeleteCard("couponid",lists.get(pos).getCid()+"");

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
        params.put("status", "1");
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Coupon/couponsList", params, handler, 1, 404);

    }

    //作废优惠卷
    public void DeleteCard(String cid) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("cid", cid);
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Coupon/couponsDel", params, handler, 2, 404);

    }

    //添加到抽奖池里面和兑换里面
    public void addLuckDraw(String cid, String type, String integral) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("cid", cid);
        params.put("integral", integral);
        params.put("name", "");
        params.put("allow", type);
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Coupon/ajaxAddCoupons", params, handler, 3, 404);

    }

    //添加到注册赠送里面，因为后台设计问题，所以先获取后台注册赠送的优惠卷
    public void loadLuckData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Coupon/give", params, handler, 4, 404);

    }
    //修改配置
    public void DeleteCard(String name,String val) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("name", name);
        params.put("val", val);
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.config/setconfig", params, handler, 3, 404);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }


    private void popWindow(final int position) {

        LayoutInflater inflater = LayoutInflater.from(this);//获取一个填充器
        View view = inflater.inflate(R.layout.popup_card, null);//填充我们自定义的布局

        Display display = getWindowManager().getDefaultDisplay();//得到当前屏幕的显示器对象
        Point size = new Point();//创建一个Point点对象用来接收屏幕尺寸信息
        display.getSize(size);//Point点对象接收当前设备屏幕尺寸信息
        int width = size.x;//从Point点对象中获取屏幕的宽度(单位像素)
        int height = size.y;//从Point点对象中获取屏幕的高度(单位像素)
        Log.v("zxy", "width=" + width + ",height=" + height);//width=480,height=854可知手机的像素是480x854的
        //创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
        final PopupWindow popWindow = new PopupWindow(view, 2 * width / 3, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());//设置PopupWindow的背景为一个空的Drawable对象，如果不设置这个，那么PopupWindow弹出后就无法退出了
        popWindow.setOutsideTouchable(true);//设置是否点击PopupWindow外退出PopupWindow
        WindowManager.LayoutParams params = getWindow().getAttributes();//创建当前界面的一个参数对象
        params.alpha = 0.8f;//设置参数的透明度为0.8，透明度取值为0~1，1为完全不透明，0为完全透明，因为android中默认的屏幕颜色都是纯黑色的，所以如果设置为1，那么背景将都是黑色，设置为0，背景显示我们的当前界面
        getWindow().setAttributes(params);//把该参数对象设置进当前界面中
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//设置PopupWindow退出监听器
            @Override
            public void onDismiss() {//如果PopupWindow消失了，即退出了，那么触发该事件，然后把当前界面的透明度设置为不透明
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
                getWindow().setAttributes(params);
            }
        });
        //第一个参数为父View对象，即PopupWindow所在的父控件对象，第二个参数为它的重心，后面两个分别为x轴和y轴的偏移量
        popWindow.showAtLocation(inflater.inflate(R.layout.activity_stock_management_details, null), Gravity.CENTER, 0, 0);

        //添加到抽奖中
        Button button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showLoadingView("添加中...");
                addLuckDraw(lists.get(position).getCid() + "", "1", "0");
                popWindow.dismiss();

            }
        });

        //添加到兑换中
        Button button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert_label(position);
                popWindow.dismiss();
            }
        });

        //添加到注册中
        Button button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showLoadingView("添加中...");
                pos = position;
                loadLuckData();
                popWindow.dismiss();

            }
        });
        //添加到充值中
        Button button4 = view.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast("正在马不停蹄的开发中");
                popWindow.dismiss();



            }
        });

        //删除当前这条数据
        Button button5 = view.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pos = position;
                showLoadingView("删除中...");
                //作废
                DeleteCard(lists.get(position).getCid() + "");
                popWindow.dismiss();

            }
        });

    }


    public void alert_label(final int po) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的
        final EditText et = view.findViewById(R.id.editText);
        et.setHint("填写兑换积分");
        builder.setTitle("填写兑换该优惠卷的积分");

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

                    showLoadingView("加载中...");
                    addLuckDraw(lists.get(po).getCid() + "", "2", et.getText().toString());
                    dialog.dismiss();

                } else {

                    Toast("非法输入!");
                }

            }
        });


    }


}
