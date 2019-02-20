package com.zmx.mian.ui;

import android.app.DatePickerDialog;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.google.gson.Gson;
import com.zmx.mian.R;
import com.zmx.mian.adapter.StockManagementDetailsAdapter;
import com.zmx.mian.bean.SMDBean;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.bean_dao.StockManagementDao;
import com.zmx.mian.bean_dao.StockManagementDetailsDao;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.util.SoftHideKeyBoardUtil;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-20 12:17
 * 类功能：进货详情【该类已经废弃没用了】
 */
public class StockManagementDetailsActivity extends BaseActivity implements StockManagementDetailsAdapter.OnClickUpload {

    private ListView listview;
    private MyHandler myHandler;
    //List数据
    private List<StockManagementDetailsBean> lists;
    //RecyclerView自定义Adapter
    private StockManagementDetailsAdapter adapter;

    private Button speed_model, up_model, add_model,submit;
    private TextView title_time;

    private StockManagementDetailsDao smDao;
    private StockManagementDao smd;
    private StockManagementBean smb;

    private String number,ru_time;

    private int S_UPLOAD = 0;

    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);


    @Override
    protected int getLayoutId() {
        return R.layout.activity_stock_management_details;
    }

    @Override
    protected void initViews() {

        myHandler = new MyHandler(this);

        smb = (StockManagementBean) getIntent().getSerializableExtra("sb");
        number = smb.getNumber();
        ru_time = smb.getRh_time();

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        smd = new StockManagementDao();
        smDao = new StockManagementDetailsDao();
        lists = new ArrayList<>();
//        getListData(number);

        title_time = findViewById(R.id.title);
        title_time.setOnClickListener(this);
        title_time.setText(ru_time);
        listview = findViewById(R.id.listview);
        speed_model = findViewById(R.id.speed_model);
        speed_model.setOnClickListener(this);
        up_model = findViewById(R.id.up_model);
        up_model.setOnClickListener(this);
        add_model = findViewById(R.id.add_model);
        add_model.setOnClickListener(this);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        if(smb.getSm_state().equals("1")){

            S_UPLOAD = 1;
            submit.setText("已上传");
            submit.setEnabled(false);

        }else if(smb.getSm_state().equals("2")){

            S_UPLOAD = 2;
            submit.setEnabled(true);
            submit.setText("编辑");

        }else{

            S_UPLOAD = 0;
            submit.setText("上传");

        }


        adapter = new StockManagementDetailsAdapter(this, lists);
        adapter.setOnClickUpload(this);
        listview.setAdapter(adapter);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                popWindow(i);

                return true;
            }
        });

        //listview设置滑动监听
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:    //当停止滚动时

                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:    //滚动时

                        //没错，下面这一坨就是隐藏软键盘的代码
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:   //手指抬起，但是屏幕还在滚动状态

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        loadingData();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.title:
                new DatePickerDialog(mActivity, onDateSetListener, mYear, mMonth, mDay).show();
                break;

            case R.id.speed_model:
                adapter.setState(1);
                adapter.notifyDataSetChanged();
                break;

            case R.id.up_model:
                adapter.setState(0);
                adapter.notifyDataSetChanged();
                break;

            case R.id.add_model:


                if(!smb.getSm_state().equals("0")){

                    smb.setSm_state("2");
                    smd.UpdateStockManagemen(smb);
                    submit.setText("编辑");
                    submit.setEnabled(true);

                }

                //新添加商品
                StockManagementDetailsBean sb = new StockManagementDetailsBean();
//                sb.setS_id(s_id);
                sb.setG_price("0");
                sb.setG_weight("0");
                sb.setG_color("0");
                sb.setG_note("");
                sb.setG_name("未选择");
                Long lg = smDao.insertSmdd(sb);

                if (lg > 0) {

                    lists.add(sb);

                }

                myHandler.sendEmptyMessage(22);
                break;

            case R.id.submit:

                boolean goods_name = true;
                //先判断有没有未选择的
                for (int i=0;i<lists.size();i++){

                    if(lists.get(i).getG_name().equals("未选择")){
                        goods_name = false;
                        break;

                    }

                }

                if(goods_name){

                    List<SMDBean> smds = new ArrayList<>();

                    float total = 0;

                    for (int i=0;i<lists.size();i++){

                        total = Float.parseFloat(lists.get(i).getG_price())*Float.parseFloat(lists.get(i).getG_weight())+total;

                        SMDBean smd = new SMDBean();

                        smd.setColor(lists.get(i).getG_color());
                        smd.setGid(lists.get(i).getG_id());
                        smd.setMemo(lists.get(i).getG_note());
                        smd.setNums(lists.get(i).getG_weight());
                        smd.setPrice(lists.get(i).getG_total());

                        smds.add(smd);

                    }

                    Gson g = new Gson();
                    String jsonString = g.toJson(smds);


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
                    params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
                    params.put("pckey", new Tools().getKey(mActivity));
                    params.put("account", "0");
                    params.put("purtime", smb.getRh_time());
                    params.put("content",  "");
                    params.put("p_order",  smb.getNumber());
                    params.put("list",  jsonString);

                    if(S_UPLOAD == 0){

                        Log.e("上传状态","");

                        params.put("type",  "insert");

                    }else{

                        params.put("type",  "update");

                    }

                    params.put("total",  total+"");
                    params.put("pid",  "1");
//                    OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Purchase/insert", params, handler, 1, 404);


                }else{

                    Toast.makeText(this,"还有商品没有选择",Toast.LENGTH_LONG).show();

                }

                break;

        }


    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                try {

                    JSONObject jsonObject = new JSONObject(msg.obj.toString());

                    String status = jsonObject.getString("status");

                    if(status.equals("1")){

                        //提交成功
                        smb.setSm_state("1");
                        smd.UpdateStockManagemen(smb);
                        S_UPLOAD = 1;
                        submit.setText("已上传");
                        submit.setEnabled(false);
                        Toast.makeText(mActivity,jsonObject.getString("content"),Toast.LENGTH_LONG).show();

                    }else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

                case 2:

                    Log.e("返回数据",""+msg.obj.toString());

                    try {

                        JSONArray jsonArray = new JSONArray(msg.obj.toString());
                        for (int i = 0;i<jsonArray.length();i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            StockManagementDetailsBean sdb = new StockManagementDetailsBean();
                            sdb.setS_id(object.getString("pid"));
                            sdb.setG_id(object.getString("gid"));
                            sdb.setG_total(object.getString("price"));
                            sdb.setG_weight(object.getString("nums"));
                            sdb.setG_color(object.getString("color"));
                            sdb.setG_note(object.getString("memo"));
                            sdb.setG_name(object.getString("name"));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

            }

        }
    };

    /**
     * 初始化详情
     *
     */
//    public void getListData(String sid) {
//
//        List<StockManagementDetailsBean> l = smDao.queryWhere(sid);
//
//        for (StockManagementDetailsBean sb : l) {
//
//            lists.add(sb);
//
//        }
//
//    }

    @Override
    public void setUploadState() {

        if(!smb.getSm_state().equals("0")){

            smb.setSm_state("2");
            smd.UpdateStockManagemen(smb);
            submit.setText("编辑");
            submit.setEnabled(true);

        }



    }

    private class MyHandler extends Handler {

        WeakReference<AppCompatActivity> mAct;

        public MyHandler(AppCompatActivity act) {
            mAct = new WeakReference<AppCompatActivity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            AppCompatActivity atc = mAct.get();
            if (atc == null)
                return;

            adapter.notifyDataSetChanged();

        }
    }

    private void popWindow(final int pos) {

        LayoutInflater inflater = LayoutInflater.from(this);//获取一个填充器
        View view = inflater.inflate(R.layout.popup_content_layout, null);//填充我们自定义的布局

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
        Button button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("点击了", "点击了");
                adapter.update(pos, listview, R.color.button1);

                StockManagementDetailsBean sdb = lists.get(pos);
                sdb.setG_color("1");
                smDao.UpdateStock(sdb);
                popWindow.dismiss();

            }
        });

        Button button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("点击了", "点击了");
                adapter.update(pos, listview, R.color.button2);

                StockManagementDetailsBean sdb = lists.get(pos);
                sdb.setG_color("2");
                smDao.UpdateStock(sdb);
                popWindow.dismiss();

            }
        });

        Button button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("点击了", "点击了");
                adapter.update(pos, listview, R.color.button3);

                StockManagementDetailsBean sdb = lists.get(pos);
                sdb.setG_color("3");
                smDao.UpdateStock(sdb);
                popWindow.dismiss();

            }
        });

        Button button4 = view.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("点击了", "点击了");
                adapter.update(pos, listview, R.color.button4);

                StockManagementDetailsBean sdb = lists.get(pos);
                sdb.setG_color("4");
                smDao.UpdateStock(sdb);
                popWindow.dismiss();

            }
        });

        Button button5 = view.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("点击了", "点击了");
                adapter.update(pos, listview, R.color.button5);

                StockManagementDetailsBean sdb = lists.get(pos);
                sdb.setG_color("5");
                smDao.UpdateStock(sdb);
                popWindow.dismiss();

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

            title_time.setText(days);
            smb.setRh_time(days);
            //修改数据
            smd.UpdateStockManagemen(smb);
        }
    };


    //加载服务器的订单列表
    public void loadingData(){

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
        params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("pid", smb.getId()+"");

//        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.Purchase/detail", params, handler, 2, 404);


    }


}
