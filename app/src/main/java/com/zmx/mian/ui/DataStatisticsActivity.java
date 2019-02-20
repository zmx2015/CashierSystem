package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.TimeQuantum;
import com.zmx.mian.bean.TodayTotalMoney;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.http.UrlConfig;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.DoubleTimeSelectDialog;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.ui.util.MyXAxisValueFormatter;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IHomeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-12 13:30
 * 类功能：数据统计界面
 */
public class DataStatisticsActivity extends BaseActivity implements OnChartGestureListener,OnChartValueSelectedListener {

    private LineChart mChart,lineChart_1;
    private LineData data,date_1;
    private List<TimeQuantum> tq;//根据每小时统计购买人次
    private List<TodayTotalMoney> ttms;

    private TextView data_total,data_order_num,discount_money_text,members_total_text,unit_price_text,data_statistics_time;
    private String allTotal="0";//总金额
    private String nums = "0";//订单数

    private float discount_money=0;//优惠金额
    private int members_total=0;//会员次数

    //查询的开始结束时间
    public String startDate="";
    public String endDate = Tools.DateConversion(new Date());

    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_statistics;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);
        //获取前月的第一天
//        Calendar cal_1=Calendar.getInstance();//获取当前日期
//        cal_1.add(Calendar.MONTH, 0);
//        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        startDate = Tools.getPastDate(7);

        showLoadingView("数据加载中...");
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id,"");
        String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name,"");
//        op.getOrderMessage(name, startDate,Tools.DateConversion(new Date()),"1","100000",name,mid);
        selectOrder(startDate,Tools.DateConversion(new Date()));
        data_order_num = findViewById(R.id.data_order_num);
        data_total = findViewById(R.id.data_total);
        discount_money_text = findViewById(R.id.discount_money_text);
        members_total_text = findViewById(R.id.members_total_text);
        unit_price_text = findViewById(R.id.unit_price);
        data_statistics_time = findViewById(R.id.data_statistics_time);
        data_statistics_time.setOnClickListener(this);

        /**
         * 走线图
         */
        mChart = findViewById(R.id.lineChart);
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

        lineChart_1 = findViewById(R.id.lineChart_1);
        lineChart_1.setOnChartGestureListener(this);
        lineChart_1.setOnChartValueSelectedListener(this);
        lineChart_1.setDrawGridBackground(false);
        // 无描述文本
        lineChart_1.getDescription().setEnabled(false);

        // 使能点击
        lineChart_1.setTouchEnabled(true );

        // 使能拖动和缩放
        lineChart_1.setScaleEnabled(true);
        lineChart_1.setDrawGridBackground(true);
        lineChart_1.setHighlightPerDragEnabled(true);
        lineChart_1.setPinchZoom(true);

        // 如果为false，则x，y两个方向可分别缩放
        lineChart_1.setPinchZoom(true);

        Matrix m=new Matrix();
        m.postScale(2.5f, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
        lineChart_1.getViewPortHandler().refresh(m, lineChart_1, true);//将图表动画显示之前进行缩放


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==0){

                try {

                    JSONObject bodys = new JSONObject(msg.obj.toString());

                    // 处理接口返回的json数据

                    JSONArray array = bodys.getJSONArray("list");

                    List<MainOrder> lists = new ArrayList<MainOrder>();

                    JSONObject data = bodys.getJSONObject("data");
                    int nums = data.getInt("pageCount");
                    int allTotal = data.getInt("allTotal");
                    int couns = data.getInt("nums");


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
                    getOrderList(lists);

                } catch (JSONException e) {
                    e.printStackTrace();
                    dismissLoadingView();
                    Toast("获取数据失败！请联系客服");

                }


            }

            if (msg.what == 1) {

                data_order_num.setText(nums+"条");
                data_total.setText(allTotal+"元");
                discount_money_text.setText(Math.round(discount_money)+"元");
                members_total_text.setText(members_total+"单");
                unit_price_text.setText(Math.round(Float.parseFloat(allTotal)/Float.parseFloat(nums))+"元");

                init();
                draw();
                dismissLoadingView();

            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.data_statistics_time:
                showCustomTimePicker();
                break;

        }


    }


    public void getOrderList(List<MainOrder> lists) {

        tq = new ArrayList<>();
        ttms = new ArrayList<>();

        for (MainOrder m : lists){

            allTotal = m.getAllTotal()+"";
            nums = m.getCouns()+"";

            discount_money = Float.parseFloat(m.getDiscount())+discount_money;
            if(m.getUid() != 0){

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

            //每日的数据
            TodayTotalMoney tt = new TodayTotalMoney();
            tt.setToday(Tools.refFormatNowDate(m.getBuytime(),0));
            tt.setMoney(Float.parseFloat(m.getTotal()));
            ttms.add(tt);

        }

        mHandler.sendEmptyMessage(1);

    }


    //柱形图
    public void draw(){

        //拿到所有的日期
        List<String> list = new ArrayList<>();//去除相同的日期
        for (int j=0;j<ttms.size();j++){
            list.add(ttms.get(j).getToday());

        }

        List<String> ls = pastLeep1(list);
        Log.e("取值后","除重后"+ls.size());
        List<TodayTotalMoney> tts = new ArrayList<>();
        for (int i = 0;i<ls.size();i++){

            TodayTotalMoney today = new TodayTotalMoney();

            float money = 0;

            for(TodayTotalMoney t:ttms){

                if(t.getToday().equals(ls.get(i))){

                    money = t.getMoney()+money;

                }

            }

            today.setMoney(Math.round(money));
            today.setToday(ls.get(i));
            tts.add(today);

        }


        if(tts.size() >1){

            date_1 = getDatas(tts);
            date_1.notifyDataChanged();
            lineChart_1.setData(date_1);
            lineChart_1.animateX(1000);//动画时间
            //更新
            lineChart_1.invalidate();

        }


    }

    private LineData getDatas(final List<TodayTotalMoney> tts) {


        //设置x轴位置
        XAxis xAxis = lineChart_1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴上每个竖线是否显示
        xAxis.setDrawGridLines(true);
        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);
        //设置x轴间距
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(tts.size(),true);
        xAxis.setLabelCount(15);//一个界面显示10个Lable。那么这里要设置11个

        // 横坐标文字
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                if (value < tts.size()){

                    String str = tts.get((int) value).getToday();
                    Log.e("显示的天数",""+str);
                    return str.substring(5);

                }else {

                    return null;

                }
            }
        });

        //去除右边的y轴
        YAxis yAxisRight = lineChart_1.getAxisRight();
        yAxisRight.setEnabled(false);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < tts.size(); i++) {
            // 要显示的数据
            yVals1.add(new Entry(i,Float.parseFloat(tts.get(i).getMoney()+"")));
        }

        //一条曲线对应一个LineDataSet
        LineDataSet set1 = new LineDataSet(yVals1, "每天营业额");
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setValueTextSize(8f);
        set1.setDrawCircles(true);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets
//

        LineData data = new LineData(dataSets) ;

        return data;

    }



    //走势图
    private void init(){

        List<TimeQuantum> tqs = new ArrayList<>();
        int xl[] ={8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24}; //横轴数据


        for (int i = 0;i<xl.length;i++) {

            TimeQuantum tt = new TimeQuantum();
            tt.setTime(xl[i]);

            //统计次数
            int man_time = 0;
            float money = 0;
            for (TimeQuantum t:tq){

                if (t.getTime() == xl[i]) {

                    man_time++;
                    money = t.getTotal_money()+money;

                }
            }
            tt.setMan_time(man_time);
            tt.setTotal_money(money);
            tqs.add(tt);
        }

        data = getData(tqs);
        data.notifyDataChanged();

        mChart.setData(data);
        mChart.animateX(2000);//动画时间
        mChart.invalidate();

    }

    private LineData getData(List<TimeQuantum> tqs){

        //设置x轴位置
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴上每个竖线是否显示
        xAxis.setDrawGridLines(true);

        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);

        //设置x轴间距
        xAxis.setGranularity(1f);

        xAxis.setLabelCount(tqs.size(),true);

        //去除右边的y轴
        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setEnabled(false);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < tqs.size(); i++) {
            // 要显示的数据
            yVals1.add(new Entry(Float.parseFloat(tqs.get(i).getTime()+""),Float.parseFloat(tqs.get(i).getMan_time()+"")));
        }

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for (int i = 0; i < tqs.size(); i++) {
            // 要显示的数据
            yVals2.add(new Entry(Float.parseFloat(tqs.get(i).getTime()+""),Float.parseFloat(tqs.get(i).getTotal_money()+"")));
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

        LineData data = new LineData(dataSets) ;
        return data;
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

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

    //set集合去重，不改变原有的顺序
    public static List<String> pastLeep1(List<String> list){

        List<String> listNew=new ArrayList<>();
        Set set=new HashSet();
        for (String str:list) {
            if(set.add(str)){
                listNew.add(str);
            }
        }

        return listNew;
    }


    public void showCustomTimePicker() {

        String beginDeadTime = "2017-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {

                    data_statistics_time.setText("数据统计"+startTime.replace("-", ".") + "至" + endTime.replace("-", "."));

                    startDate = startTime;
                    endDate = endTime;
                    showLoadingView("数据加载中...");

                    discount_money=0;//优惠金额
                    members_total=0;//会员次数
//                  op.getOrderMessage(name, startDate,endDate,"1","10000",name,mid);
                    selectOrder(startDate,endDate);

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



    //查询订单列表
    public void selectOrder(String startD,String endD){

        Map<String, String> params = new HashMap<String, String>();
        params.put("pckey", Tools.getKey(MyApplication.getName()));
        params.put("account", "1");
        params.put("today",startD);
        params.put("endtime", endD);
        params.put("thisPage", "1");
        params.put("num", "100000");
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("type", "all");

        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.SELECT_ORDER_LIST, params, mHandler, 0, 404);


    }


}
