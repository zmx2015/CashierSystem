package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.zmx.mian.adapter.StoreListAdapter;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Paging;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IOrderDataView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 0:03
 * 类功能：订单管理界面
 */
public class  OrderDataActivity extends BaseActivity implements View.OnClickListener {

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

    private int pos;//记录要删除的订单item；

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
        mRecyclerView = findViewById(R.id.moder_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new OrderDataAdapter(this, mo);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                pos = position;
                dialog(pos);
            }
        });
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
                selectOrder();
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
                    selectOrder();
                }


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

                        JSONObject bodys = new JSONObject(msg.obj.toString());

                        // 处理接口返回的json数据

                        JSONArray array = bodys.getJSONArray("list");
                        int pagenow = 1;

                        JSONObject data = bodys.getJSONObject("data");
                        pagenow = data.getInt("pageCount");
                        allTotal = data.getInt("allTotal")+"";
                        nums = data.getInt("nums")+"";

                        for (int i = 0; i < array.length(); i++) {

                            MainOrder mw = new MainOrder();
                            JSONObject json = array.getJSONObject(i);

                            mw.setPageNum(pagenow);
                            mw.setAllTotal(data.getInt("allTotal"));
                            mw.setCouns(data.getInt("nums"));
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
                            mw.setMo_classify(json.getString("classify"));

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

                        p.setRowCount(pagenow);

                        handler.sendEmptyMessage(1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast("未知错误，请联系客服！");
                    }


                    break;

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
                    p.setPageNow(p.getPageNow() + 1);
                    selectOrder();
                    break;

                case 3:


//                {"code":1,"content":"订单取消成功"}

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if(jsonObject.getString("code").equals("1")){

                            Toast(jsonObject.getString("content"));


                        }else{

                            Toast(jsonObject.getString("content"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    //查询订单列表
    public void selectOrder(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(MyApplication.getName()));
        params.put("account", "1");
        params.put("today",startDate);
        params.put("endtime", endDate);
        params.put("thisPage",p.getPageNow()+"");
        params.put("num", p.getPageSize() + "");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("type", "all");

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.order/orderList", params, handler, 0, 404);


    }

    private RelativeLayout rl_layout1,rl_layout2,rl_layout3,rl_layout4;
    private TextView textView1,textView2,textView3;
    private Button submit;
    private EditText edit_why;

    private int STATE = 0;//选择原因的状态，默认0为没选中

    public void dialog(int pos) {

        final MainOrder m = mo.get(pos);
        LayoutInflater inflater = LayoutInflater.from(this);//获取一个填充器
        View view = inflater.inflate(R.layout.dialog_delete_order, null);//填充我们自定义的布局

        Display display = getWindowManager().getDefaultDisplay();//得到当前屏幕的显示器对象
        Point size = new Point();//创建一个Point点对象用来接收屏幕尺寸信息
        display.getSize(size);//Point点对象接收当前设备屏幕尺寸信息
        int width = size.x;//从Point点对象中获取屏幕的宽度(单位像素)
        int height = size.y;//从Point点对象中获取屏幕的高度(单位像素)
        //创建一个PopupWindow对象，第二个参数是设置宽度的，用刚刚获取到的屏幕宽度乘以2/3，取该屏幕的2/3宽度，从而在任何设备中都可以适配，高度则包裹内容即可，最后一个参数是设置得到焦点
        final PopupWindow popWindow = new PopupWindow(view, 4 * width / 5, ViewGroup.LayoutParams.WRAP_CONTENT, true);
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

        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);
        edit_why = view.findViewById(R.id.edit_why);

        rl_layout4 = view.findViewById(R.id.rl_layout4);
        rl_layout1 = view.findViewById(R.id.rl_layout1);
        rl_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_layout1.setBackgroundColor(mActivity.getResources().getColor(R.color.tou));
                rl_layout2.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                textView1.setTextColor(mActivity.getResources().getColor(R.color.tou));
                textView2.setTextColor(mActivity.getResources().getColor(R.color.grey_500));
                textView3.setTextColor(mActivity.getResources().getColor(R.color.grey_500));
                rl_layout4.setVisibility(View.GONE);
                STATE = 1;

            }
        });

        rl_layout2 = view.findViewById(R.id.rl_layout2);
        rl_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_layout1.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(mActivity.getResources().getColor(R.color.tou));
                rl_layout3.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                textView1.setTextColor(mActivity.getResources().getColor(R.color.grey_500));
                textView2.setTextColor(mActivity.getResources().getColor(R.color.tou));
                textView3.setTextColor(mActivity.getResources().getColor(R.color.grey_500));
                rl_layout4.setVisibility(View.GONE);
                STATE = 2;
            }
        });

        rl_layout3 = view.findViewById(R.id.rl_layout3);
        rl_layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_layout1.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(mActivity.getResources().getColor(R.color.tou));
                textView1.setTextColor(mActivity.getResources().getColor(R.color.grey_500));
                textView2.setTextColor(mActivity.getResources().getColor(R.color.grey_500));
                textView3.setTextColor(mActivity.getResources().getColor(R.color.tou));
                rl_layout4.setVisibility(View.VISIBLE);
                STATE = 3;
            }
        });

        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_why = "";
                if(STATE == 0){

                    Toast("请选择取消原因！");

                }else {

                    if (STATE == 1) {
                        s_why = textView1.getText().toString();
                    } else if (STATE == 2) {
                        s_why = textView2.getText().toString();
                    } else if (STATE == 3) {
                        s_why = edit_why.getText().toString();
                    }

                    deleteOrder(m.getOrder(),m.getId()+"",s_why);
                    popWindow.dismiss();

                }
            }
        });

    }

    //删除订单
    public void deleteOrder(String order,String order_id,String s_why) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("order", order);
        params.put("id", order_id);
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.ordertwo/cancelOrder", params, handler, 3, 404);

    }


    }



