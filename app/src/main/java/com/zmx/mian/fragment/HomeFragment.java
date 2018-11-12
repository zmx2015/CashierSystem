package com.zmx.mian.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.zmx.mian.R;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.Order;
import com.zmx.mian.bean.TimeQuantum;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.http.API;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.DataStatisticsActivity;
import com.zmx.mian.ui.GoodsItemRankingActivity;
import com.zmx.mian.ui.MainActivity;
import com.zmx.mian.ui.MembersActivity;
import com.zmx.mian.ui.OrderDataActivity;
import com.zmx.mian.ui.ProcurementActivity;
import com.zmx.mian.ui.SingleGoodsActivity;
import com.zmx.mian.ui.StockManagementListActivity;
import com.zmx.mian.ui.StoreListActivity;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IHomeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-25 1:54
 * 类功能：首页
 */

public class HomeFragment extends Fragment implements IHomeView, OnChartGestureListener, OnChartValueSelectedListener {

    private OrderPresenter op;

    private TextView order_num,
            switch_store, store_name,
            unit_price, yesterday_num, yesterday_total,
            yesterday_discount_money_text, yesterday_members_total_text, yesterday_unit_price_text;
    private TextView order_total;

    private String allTotal = "0";//总金额
    private String nums = "0";//订单数
    private String yesterday_nums = "0";//昨天金额
    private String yesterday_allTotal = "0";//昨天订单数

    private LoadingDialog mLoadingDialog; //显示正在加载的对话框

    //    BarChart mBarChart;//图表
    private LineChart mChart;
    private LineData data;

    private List<MainOrder> lists;

    private float discount_money = 0;//会员优惠金额
    private int members_total = 0;//会员消费次数
    private float yesterday_discount_money = 0;//昨日会员优惠金额
    private int yesterday_members_total = 0;//会员消费次数

    private TextView discount_money_text, members_total_text;
    private RelativeLayout relative1,relative2,relative3,relative4;
    private List<TimeQuantum> tq;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        discount_money_text = view.findViewById(R.id.discount_money);
        members_total_text = view.findViewById(R.id.members_total);

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        String s_name = MySharedPreferences.getInstance(this.getActivity()).getString(MySharedPreferences.store_name, "");
        ;
        order_num = view.findViewById(R.id.order_num);
        order_total = view.findViewById(R.id.all_total);
        yesterday_num = view.findViewById(R.id.yesterday_num);
        yesterday_total = view.findViewById(R.id.yesterday_total);
        unit_price = view.findViewById(R.id.unit_price);
        yesterday_discount_money_text = view.findViewById(R.id.yesterday_discount_money_text);
        yesterday_members_total_text = view.findViewById(R.id.yesterday_members_total_text);
        yesterday_unit_price_text = view.findViewById(R.id.yesterday_unit_price_text);
        store_name = view.findViewById(R.id.store_name);
        store_name.setText(s_name);

        relative1 = view.findViewById(R.id.relative1);
        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeFragment.this.getActivity(), DataStatisticsActivity.class);
                startActivity(intent);

            }

        });

        relative2 = view.findViewById(R.id.relative2);
        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeFragment.this.getActivity(), GoodsItemRankingActivity.class);
                startActivity(intent);

            }

        });

        relative3 = view.findViewById(R.id.relative3);
        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeFragment.this.getActivity(), OrderDataActivity.class);
                startActivity(intent);

            }

        });

        relative4 = view.findViewById(R.id.relative4);
        relative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeFragment.this.getActivity(), MembersActivity.class);
                startActivity(intent);

            }

        });

//        single_data = view.findViewById(R.id.single_data);
//        single_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeFragment.this.getActivity(), DataStatisticsActivity.class);
//                startActivity(intent);
//            }
//        });

//        my_dance.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(HomeFragment.this.getActivity(), OrderDataActivity.class);
//                startActivity(intent);
//
//            }
//        });

        switch_store = view.findViewById(R.id.switch_store);
        switch_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeFragment.this.getActivity(), StoreListActivity.class);
                startActivity(intent);


            }
        });

//        goods_ph = view.findViewById(R.id.goods_ph);
//        goods_ph.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(HomeFragment.this.getActivity(), GoodsItemRankingActivity.class);
//                startActivity(intent);
//
//            }
//        });

//        members_text = view.findViewById(R.id.members_text);
//        members_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeFragment.this.getActivity(), MembersActivity.class);
//                startActivity(intent);
//            }
//        });


        showLoading();
        op = new OrderPresenter(this);

//        mBarChart = (BarChart) view.findViewById(R.id.mBarChart);
//
//        mBarChart.setDrawBarShadow(false);     //表不要阴影
//        mBarChart.setDrawValueAboveBar(true);
//        Description description=new Description();
//        description.setText("日期");
//        mBarChart.setDescription(description);  //表的描述信息
//
//        mBarChart.setPinchZoom(false);
//        mBarChart.setMaxVisibleValueCount(60); //最大显示的个数。超过60个将不再显示
//        mBarChart.setScaleEnabled(false);     //禁止缩放
//        mBarChart.setDragEnabled(true);// 是否可以拖拽
//        mBarChart.setHighlightPerDragEnabled(true);// 拖拽超过图标绘制画布时高亮显示
//        mBarChart.setDrawGridBackground(false);//
//        mBarChart.getAxisRight().setEnabled(false);//右侧不显示Y轴
//      /*  mBarChart.setAutoScaleMinMaxEnabled(true);*/
//       /* mBarChart.animateX(500);//数据显示动画，从左往右依次显示*/
//       /* mBarChart.getAxisRight().setEnabled(false);*/
//        /*mBarChart.setDragDecelerationEnabled(true);*///拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
//        mBarChart.zoom(2.5f,1f,0,0);//显示的时候 是 按照多大的比率缩放显示   1f表示不放大缩小
//        //我默认手机屏幕上显示10  剩下的滑动直方图 然后显示。。假如要显示25个 那么除以10 就是放大2.5f。。同理
//        // 56个民族   那么放大5.6f
//        draw();
        mChart = view.findViewById(R.id.lineChart);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        // 无描述文本
        mChart.getDescription().setEnabled(false);

        // 使能点击
        mChart.setTouchEnabled(false);

        // 使能拖动和缩放
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);

        // 如果为false，则x，y两个方向可分别缩放
        mChart.setPinchZoom(true);

        /**
         1.添加assets文件   project->src->main位置，右键new->Directory->assets
         2.在assets文件中   new->Directory->Fonts文件
         3.在Fonts文件中复制下载好的ttf文件，例如OpenSans-Regular.ttf
         */
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        String mid = MySharedPreferences.getInstance(this.getActivity()).getString(MySharedPreferences.store_id, "");
        String name = MySharedPreferences.getInstance(this.getActivity()).getString(MySharedPreferences.name, "");

        getYesterday(name, Tools.getYesterday(), Tools.getYesterday(), "1", "10000", name, mid);

        return view;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    order_num.setText(nums + "条");
                    order_total.setText(allTotal + "元");

                    discount_money_text.setText(discount_money + "元");
                    members_total_text.setText(members_total + "次");
                    unit_price.setText(Math.round(Float.parseFloat(allTotal) / Float.parseFloat(nums)) + "元");
                    init();
                    hideLoading();

                    break;

                case 2:

                    for (MainOrder m : lists) {

                        yesterday_allTotal = m.getAllTotal() + "";
                        yesterday_nums = m.getCouns() + "";

                        yesterday_discount_money = Float.parseFloat(m.getDiscount()) + yesterday_discount_money;

                        if (m.getUid() != 0) {

                            yesterday_members_total++;

                        }

                    }

                    yesterday_total.setText("昨日金额：" + yesterday_allTotal + "元");
                    yesterday_num.setText("昨日订单：" + yesterday_nums + "单");
                    yesterday_discount_money_text.setText("昨日优惠：" + yesterday_discount_money + "元");
                    yesterday_members_total_text.setText("昨日会员：" + yesterday_members_total + "次");
                    yesterday_unit_price_text.setText("昨日单价" + Math.round(Float.parseFloat(yesterday_allTotal) / Float.parseFloat(yesterday_nums)) + "元");
                    break;

            }

        }
    };

    /**
     * 显示加载的进度款
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this.getActivity(), "正在加载数据...", false);
        }
        mLoadingDialog.show();
    }


    /**
     * 隐藏加载的进度框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });

        }
    }


    @Override
    public void getOrderList(List<MainOrder> lists) {

        tq = new ArrayList<>();


        for (MainOrder m : lists) {

            allTotal = m.getAllTotal() + "";
            nums = m.getCouns() + "";

            discount_money = Float.parseFloat(m.getDiscount()) + discount_money;

            if (m.getUid() != 0) {

                members_total++;

            }

            //统计每个小时的人次和金额
            String gm_time = m.getBuytime() + "000";
            Date nowTime = new Date(Long.parseLong(gm_time));
            int h = nowTime.getHours();

            TimeQuantum t = new TimeQuantum();
            t.setTime(h);
            t.setTotal_money(Float.parseFloat(m.getTotal()));
            tq.add(t);

        }

        mHandler.sendEmptyMessage(1);

    }

    @Override
    public void ErrerMessage() {

        hideLoading();
        Toast.makeText(this.getActivity(), "加载错误", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();

        discount_money = 0;
        members_total = 0;

        String mid = MySharedPreferences.getInstance(this.getActivity()).getString(MySharedPreferences.store_id, "");
        String name = MySharedPreferences.getInstance(this.getActivity()).getString(MySharedPreferences.name, "");

        op.getOrderMessage(name, Tools.DateConversion(new Date()), Tools.DateConversion(new Date()), "1", "10000", name, mid);

    }


    //走势图
    private void init() {

        List<TimeQuantum> tqs = new ArrayList<>();
        int xl[] = {8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24}; //横轴数据

        for (int i = 0; i < xl.length; i++) {

            TimeQuantum tt = new TimeQuantum();
            tt.setTime(xl[i]);

            //统计次数
            int man_time = 0;
            float money = 0;
            for (TimeQuantum t : tq) {

                if (t.getTime() == xl[i]) {

                    man_time++;
                    money = t.getTotal_money() + money;

                }
            }
            tt.setMan_time(man_time);
            tt.setTotal_money(money);
            tqs.add(tt);
        }
        data = getData(tqs);
        mChart.setData(data);
        mChart.animateX(2000);//动画时间
    }

    private LineData getData(List<TimeQuantum> tqs) {


        //设置x轴位置
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴上每个竖线是否显示
        xAxis.setDrawGridLines(true);

        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);

        //设置x轴间距
        xAxis.setGranularity(1f);

        xAxis.setLabelCount(tqs.size(), true);

        //去除右边的y轴
        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setEnabled(false);

        ArrayList<Entry> yVals1 = new ArrayList<>();
        for (int i = 0; i < tqs.size(); i++) {

            // 要显示的数据
            yVals1.add(new Entry(Float.parseFloat(tqs.get(i).getTime() + ""), Float.parseFloat(tqs.get(i).getMan_time() + "")));
        }

        ArrayList<Entry> yVals2 = new ArrayList<>();
        for (int i = 0; i < tqs.size(); i++) {
            // 要显示的数据
            yVals2.add(new Entry(Float.parseFloat(tqs.get(i).getTime() + ""), Float.parseFloat(tqs.get(i).getTotal_money() + "")));
        }

        //一条曲线对应一个LineDataSet
        LineDataSet set1 = new LineDataSet(yVals1, "每小时人次");
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setValueTextSize(8f);
        set1.setDrawCircles(true);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小


        //一条曲线对应一个LineDataSet
        LineDataSet set2 = new LineDataSet(yVals2, "每小时金额");
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置曲线为圆滑的线
        set2.setCubicIntensity(0.2f);
        set2.setValueTextSize(8f);
        //折线二 折线的颜色
        set2.setColor(getResources().getColor(R.color.red));
        set2.setDrawCircles(true);  //设置有圆点
        set2.setLineWidth(2f);    //设置线的宽度
        set2.setCircleSize(5f);   //设置小圆的大小


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2); // add the datasets

        LineData data = new LineData(dataSets);

        return data;
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    //柱形图
    public void draw() {

//        //X轴 样式
//        final XAxis xAxis = mBarChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setLabelRotationAngle(0);//柱的下面描述文字  旋转90度
//        xAxis.setDrawLabels(true);
//        xAxis.setDrawGridLines(false);
//         xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
//        xAxis.setCenterAxisLabels(true);//字体下面的标签 显示在每个直方图的中间
//        xAxis.setLabelCount(11,true);//一个界面显示10个Lable。那么这里要设置11个
//        xAxis.setTextSize(10f);
//
//
//        //Y轴样式
//        YAxis leftAxis = mBarChart.getAxisLeft();
//        leftAxis.setLabelCount(10);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setSpaceTop(15f);
//        leftAxis.setStartAtZero(false);
//        leftAxis.setYOffset(10f);
//
//
//        //这个替换setStartAtZero(true)
//        leftAxis.setAxisMaxValue(5000f);
//        leftAxis.setAxisMinValue(10f);
//        leftAxis.setDrawGridLines(true);//背景线
//        leftAxis.setAxisLineColor(getResources().getColor(R.color.colorPrimaryDark));
//
//
//
//
//        //.设置比例图标的显示隐藏
//        Legend l = mBarChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        //样式
//        l.setForm(Legend.LegendForm.CIRCLE);
//        //字体
//        l.setFormSize(10f);
//        //大小
//        l.setTextSize(13f);
//        l.setFormToTextSpace(10f);
//        l.setXEntrySpace(10f);
//
//
//        //模拟数据
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        yVals1.add(new BarEntry(1,2300));
//        yVals1.add(new BarEntry(2, 2000));
//        yVals1.add(new BarEntry(3, 3000));
//        yVals1.add(new BarEntry(4, 1500));
//        yVals1.add(new BarEntry(5, 4500));
//        yVals1.add(new BarEntry(6, 2000));
//        yVals1.add(new BarEntry(7, 3500));
//        yVals1.add(new BarEntry(8, 2600));
//        yVals1.add(new BarEntry(9, 1400));
//        yVals1.add(new BarEntry(10, 2000));
//        yVals1.add(new BarEntry(11, 3300));
//        yVals1.add(new BarEntry(12, 4400));
//        yVals1.add(new BarEntry(13, 4002));
//        yVals1.add(new BarEntry(14, 4100));
//        yVals1.add(new BarEntry(15, 1200));
//        yVals1.add(new BarEntry(16, 3100));
//        yVals1.add(new BarEntry(17, 2100));
//        yVals1.add(new BarEntry(18, 2000));
//        yVals1.add(new BarEntry(19, 4400));
//        yVals1.add(new BarEntry(20, 4200));
//        yVals1.add(new BarEntry(21, 4100));
//        yVals1.add(new BarEntry(22, 1200));
//        yVals1.add(new BarEntry(23, 3100));
//        yVals1.add(new BarEntry(24, 2100));
//        yVals1.add(new BarEntry(25, 2000));
//        setData(yVals1);
    }

    private void setData(ArrayList yVals1) {

//        for(int i=1;i<=yVals1.size();i++) {
//            mlist.add(""+i);
//        }
//        IAxisValueFormatter ix=new MyXAxisValueFormatter(mlist);
//        mBarChart.getXAxis().setValueFormatter(ix);
//        BarDataSet set1;
//        if (mBarChart.getData() != null &&
//                mBarChart.getData().getDataSetCount() > 0) {
//
//            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            mBarChart.getData().notifyDataChanged();
//            mBarChart.notifyDataSetChanged();
//
//        } else {
//
//            set1 = new BarDataSet(yVals1, "当月每天销售额");
//            set1.setColors(ColorTemplate.MATERIAL_COLORS);
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
//            data.setBarWidth(0.9f);
//            mBarChart.setData(data);
//
//        }
    }

    //请求昨天的数据，懒得写在模型里了
    public void getYesterday(String account, String today, String endtime, String thisPage, String num, String admin, String mid) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.yiyuangy.com/admin/") // 设置 网络请求 Url
                .build();

        // 步骤5:创建 网络请求接口 的实例
        API request = retrofit.create(API.class);

        //对 发送请求 进行封装
        Call<ResponseBody> call = request.getMainOrder(Tools.getKey(admin), account, today, endtime, thisPage, num, admin, mid);

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时候的回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    JSONObject bodys = new JSONObject(response.body().string());

                    // 处理接口返回的json数据

                    JSONArray array = bodys.getJSONArray("list");

                    lists = new ArrayList<MainOrder>();

                    JSONObject data = bodys.getJSONObject("data");
                    int nums = data.getInt("pageCount");
                    int allTotal = data.getInt("allTotal");
                    int couns = data.getInt("nums");

                    yesterday_nums = data.getInt("nums") + "";
                    yesterday_allTotal = data.getInt("allTotal") + "";

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

                        lists.add(mw);

                    }

                    mHandler.sendEmptyMessage(2);


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("戳了", "处理" + e.toString());
                }

            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

    }

}