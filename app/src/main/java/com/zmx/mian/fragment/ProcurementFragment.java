package com.zmx.mian.fragment;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import com.zmx.mian.adapter.StockManagementAdapter;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.bean_dao.StockManagementDao;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.AddGoodsActivity;
import com.zmx.mian.ui.FeedbackActivity;
import com.zmx.mian.ui.ProcurementDetailsActivity;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.MyDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.ToastUtil;
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
 * 开发时间：2018-11-07 0:13
 * 类功能：采购列表
 */
public class ProcurementFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RelativeLayout title_relative;
    private TextView tile_time;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private List<StockManagementBean> lists;
    //RecyclerView自定义Adapter
    private StockManagementAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;

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
    public String endDate = "";

    private int load_tag = 0;//上拉或者下拉标示

    private ImageView no_data;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_procurement, container, false);

        //获取前月的第一天
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, 0);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        int y = cal_1.get(Calendar.YEAR);
        int m = cal_1.get(Calendar.MONTH);

        startDate = Tools.DateConversion(cal_1.getTime());

        endDate = new Tools().getDateLastDay(y + "", m + "");

        lists = new ArrayList<>();
        ImageButton ib = view.findViewById(R.id.add_button);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();

            }
        });

        title_relative = view.findViewById(R.id.title_relative);
        title_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomTimePicker();
            }
        });
        tile_time = view.findViewById(R.id.tile_time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String dateString = formatter.format(new Date());
        tile_time.setText("("+dateString+")");

        no_data = view.findViewById(R.id.no_data);
        mRecyclerView = view.findViewById(R.id.stock_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new StockManagementAdapter(mActivity, lists);
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
                startActivity(ProcurementDetailsActivity.class, bundle);

            }
        });
        mPtrFrame = view.findViewById(R.id.stock_rotate_header_list_view_frame);
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
        }, 10);
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
                Toast("没有更多数据");
                mPtrFrame.loadMoreComplete(true);
                mPtrFrame.setLoadMoreEnable(false);

            }
        });

        return view;
    }

    @Override
    protected void initView() {

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    try {

                        lists.clear();
                        JSONArray jsonArray = new JSONArray(msg.obj.toString());

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            StockManagementBean sb = new StockManagementBean();
                            sb.setNumber(object.getString("p_order"));
                            sb.setRh_time(Tools.refFormatNowDate(object.getString("purtime"), 0));
                            sb.setSm_time(Tools.refFormatNowDate(object.getString("addtime"), 0));
                            sb.setSm_state("1");
                            sb.setId(Long.parseLong(object.getString("id")));
                            sb.setTotal(object.getString("total"));

                            lists.add(sb);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //判断有没有数据，没有就显示提示
                    if (lists.size() > 0) {

                        no_data.setVisibility(View.GONE);

                    } else {

                        no_data.setVisibility(View.VISIBLE);

                    }

                    initAdapter();

                    break;

                case 2:
                    loadingData();//加载网络订单
                    break;

                case 404:

                    initAdapter();
                    Toast("连接网络失败，请检查网络！");

                    break;

            }

        }
    };

    public void Toast(String msg){

        ToastUtil toastUtil = new ToastUtil(this.getActivity(), R.layout.toast_center_horizontal, msg);
        toastUtil.show(1500);

    }

    /**
     * 更新适配器数据
     */
    public void initAdapter() {

        dismissLoadingView();
        mAdapter.notifyDataSetChanged();
        mPtrFrame.refreshComplete();
        if (load_tag == 0) {

            if (lists.size() > 0 ) {
                mPtrFrame.setLoadMoreEnable(true);
            }

        } else {

            mPtrFrame.loadMoreComplete(true);


        }
    }


    public void showCustomTimePicker() {

        String beginDeadTime = "2017-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(mActivity, beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {

                    tile_time.setText("("+startTime.replace("-", ".") + "至" + endTime.replace("-", ".")+")");
                    startDate = startTime;
                    endDate = endTime;
                    showLoadingView("加载中...");
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




    //    //弹出框
    public void showDialog() {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.stock_list_dialog, null);
        mMyDialog = new MyDialog(mActivity, 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);
        mMyDialog.show();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String toDay = dateFormat.format(c.getTime());
        button1 = view.findViewById(R.id.button1);
        button1.setText("今天（" + toDay + "）");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addProcure(toDay.trim());
                mMyDialog.dismiss();

            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, +1);//明天
        final String mt = dateFormat.format(calendar.getTime());
        button2 = view.findViewById(R.id.button2);
        button2.setText("明天（" + mt + "）");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addProcure(mt.trim());
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
            addProcure(days.trim());
            mMyDialog.dismiss();
        }
    };


    //加载服务器的订单列表
    public void loadingData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
        params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("today", startDate);
        params.put("endtime", endDate);

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Purchase/index", params, handler, 1, 404);


    }


    public void addProcure(String date) {


        String OrderNumber = new Date().getTime() + "";
        //先创建一条列表数据，拿到id
        String s_time = Tools.DateConversions(new Date());
        StockManagementBean smb = new StockManagementBean();
        smb.setSm_state("0");
        smb.setTotal("0.00");
        smb.setSm_time(s_time);
        smb.setRh_time(date);
        smb.setNumber(OrderNumber);

        // 通过Intent传递对象给Service
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("sb", smb);
        intent.putExtras(bundle);
        startActivity(ProcurementDetailsActivity.class, bundle);

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();

        loadingData();//加载网络订单

    }
}
