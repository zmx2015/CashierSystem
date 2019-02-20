package com.zmx.mian.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.mian.R;
import com.zmx.mian.adapter.SearchGoodsAdapter;
import com.zmx.mian.adapter.StockManagementAdapter;
import com.zmx.mian.adapter.StoreListAdapter;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.bean.StoresMessage;
import com.zmx.mian.bean_dao.StockManagementDao;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.MyDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-19 14:19
 * 类功能：进货管理【该类已经废弃没用了】
 */
public class StockManagementListActivity extends BaseActivity{

    private TextView title;
    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private List<StockManagementBean> lists;
    //RecyclerView自定义Adapter
    private StockManagementAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;

    private StockManagementDao smDao;

    private Button button1, button2, button3;
    private MyDialog mMyDialog;
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd;

    //查询的开始结束时间
    public String startDate = "";
    public String endDate = Tools.DateConversion(new Date());

    private int load_tag = 0;//上拉或者下拉标示

    @Override
    protected int getLayoutId() {
        return R.layout.activity_stock_management_list;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        smDao = new StockManagementDao();

        lists = new ArrayList<>();

        List<StockManagementBean> ss = smDao.queryAll();
        for (StockManagementBean s : ss) {

            lists.add(s);

        }

        startDate = getPastDate(7);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();

            }
        });

        title = findViewById(R.id.title);
        title.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.stock_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new StockManagementAdapter(this, lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                // 通过Intent传递对象给Service
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("sb", (Serializable) lists.get(position));
                intent.putExtras(bundle);
                startActivity(StockManagementDetailsActivity.class, bundle);

            }
        });
        mPtrFrame = findViewById(R.id.stock_rotate_header_list_view_frame);
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
                load_tag = 0;
                loadingData();//加载网络订单

            }
        });
//上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {

                load_tag = 1;
                Toast.makeText(mActivity, "没有更多数据", Toast.LENGTH_SHORT)
                        .show();
                mPtrFrame.loadMoreComplete(true);
                mPtrFrame.setLoadMoreEnable(false);

            }
        });

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.title:
                showCustomTimePicker();
                break;

        }


    }

    /**
     * Activity从后台重新回到前台时被调用
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        handler.sendEmptyMessage(1);

    }

    /**
     * 更新适配器数据
     */
    public void initAdapter() {

        mAdapter.notifyDataSetChanged();
        mPtrFrame.refreshComplete();
        if (load_tag == 0) {

            mPtrFrame.setLoadMoreEnable(true);

        } else {

            mPtrFrame.loadMoreComplete(true);

        }
    }



    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:
                    lists.clear();
                    List<StockManagementBean> ss = smDao.queryAll();

                    for (StockManagementBean s : ss) {

                        lists.add(s);

                    }

                    adapter.notifyDataSetChanged();
                    break;

                case 2:

                    Log.e("返回的数据",""+msg.obj.toString());

                    try {

                        JSONArray jsonArray = new JSONArray(msg.obj.toString());

                        for (int i=0;i<jsonArray.length();i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            StockManagementBean sb = new StockManagementBean();
                            sb.setNumber(object.getString("p_order"));
                            sb.setRh_time(Tools.refFormatNowDate(object.getString("purtime"),0));
                            sb.setSm_time(Tools.refFormatNowDate(object.getString("addtime"),0));
                            sb.setSm_state("1");
                            sb.setId(Long.parseLong(object.getString("id")));
                            sb.setTotal(object.getString("total"));
                            lists.add(sb);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    initAdapter();
                    break;

            }

        }
    };


    //    //弹出框
    public void showDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.stock_list_dialog, null);
        mMyDialog = new MyDialog(this, 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);
        mMyDialog.show();

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        final String toDay = dateFormat.format(c.getTime());
        button1 = view.findViewById(R.id.button1);
        button1.setText("今天（"+toDay+"）");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addDate(toDay);
                mMyDialog.dismiss();

            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, +1);//明天
        final String mt = dateFormat.format(calendar.getTime());
        button2 = view.findViewById(R.id.button2);
        button2.setText("明天（"+mt+"）");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addDate(mt);
                mMyDialog.dismiss();
            }
        });

        button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(mActivity, onDateSetListener, mYear, mMonth, mDay).show();

            }
        });

    }

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {

                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();

                } else {

                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {

                if (mDay < 10) {

                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();

                } else {

                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();

                }

            }

            button3.setText(days);
            Log.e("录入时间", "" + days);
            addDate(days);
            mMyDialog.dismiss();
        }
    };


    public void addDate(String date) {


        String OrderNumber = new Date().getTime() + "";
        //先创建一条列表数据，拿到id
        String s_time = Tools.DateConversions(new Date());
        StockManagementBean smb = new StockManagementBean();
        smb.setSm_state("0");
        smb.setSm_time(s_time);
        smb.setRh_time(date);
        smb.setNumber(OrderNumber);

        Long lid = smDao.AddStock(smb);

        // 通过Intent传递对象给Service
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("sb", (Serializable)smb);
        intent.setClass(mActivity, StoreListActivity.class);
        intent.putExtras(bundle);
        startActivity(StockManagementDetailsActivity.class, bundle);

    }


    //加载服务器的订单列表
    public void loadingData(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
        params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("today", startDate);
        params.put("endtime", endDate);

//        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Purchase/index", params, handler, 2, 404);


    }


    public void showCustomTimePicker() {

        String beginDeadTime = "2017-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {

                    title.setText(startTime.replace("-", ".") + "至\n" + endTime.replace("-", "."));
                    startDate = startTime;
                    endDate = endTime;
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
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

}
