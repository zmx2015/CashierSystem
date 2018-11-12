package com.zmx.mian.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.zmx.mian.adapter.ProcurementDetailsAdapter;
import com.zmx.mian.adapter.SearchGoodsAdapter;
import com.zmx.mian.adapter.StockManagementDetailsAdapter;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.SMDBean;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.bean_dao.StockManagementDao;
import com.zmx.mian.bean_dao.StockManagementDetailsDao;
import com.zmx.mian.bean_dao.goodsDao;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-25 18:18
 * 类功能：
 */
public class  ProcurementDetailsActivity extends BaseActivity implements ProcurementDetailsAdapter.OnClickUpload {

    private ListView listview;
    //List数据
    private List<StockManagementDetailsBean> lists;
    //RecyclerView自定义Adapter
    private ProcurementDetailsAdapter adapter;

    private Button speed_model, up_model, add_model,submit;
    private TextView title_time;

    private String number,ru_time;

    private int S_UPLOAD = 0;

    private goodsDao gdao;
    private StockManagementDetailsDao smDao;
    private StockManagementBean smb;
    private StockManagementDao smd;

    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_procurement_details;
    }

    @Override
    protected void initViews() {

        setTitleColor(R.id.position_view);
        gdao = new goodsDao();
        smd = new StockManagementDao();
        smDao = new StockManagementDetailsDao();

        smb = (StockManagementBean) getIntent().getSerializableExtra("sb");
        Log.e("pid","pid"+smb.getId());
        number = smb.getNumber();
        ru_time = smb.getRh_time();

        lists = new ArrayList<>();
//        getListData(number);

        BackButton(R.id.back_button);
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
            submit.setEnabled(true);
            submit.setText("上传");

        }

        adapter = new ProcurementDetailsAdapter(this, lists,smb);
        adapter.setOnClickUpload(this);
        listview.setAdapter(adapter);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                popWindow(i);

                return false;
            }
        });

        //先判断点击进来的是否是有没有同步了
        if(smb.getSm_state().equals("1")){

            loadingData();

        }else{

            //查询本地的
            getListData(number);

        }

    }

    /**
     * 初始化详情
     *
     */
    public void getListData(String number) {

        lists.clear();
        List<StockManagementDetailsBean> l = smDao.queryWhere(number);

        for (StockManagementDetailsBean sb : l) {

            lists.add(sb);

        }
        handler.sendEmptyMessage(2);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.title:
                new DatePickerDialog(mActivity, onDateSetListener, mYear, mMonth, mDay).show();
                break;

            case R.id.add_model:
                chooseGoods();
                break;

            case R.id.speed_model:
                adapter.setState(1);
                adapter.notifyDataSetChanged();
                break;


            case R.id.up_model:
                adapter.setState(0);
                adapter.notifyDataSetChanged();
                break;

            case R.id.submit:

                if(lists.size()<=0){
                    Toast.makeText(mActivity,"没有商品，无需上传",Toast.LENGTH_LONG).show();
                    return;
                }

                List<SMDBean> smds = new ArrayList<>();

                float total = 0;

                for (int i=0;i<lists.size();i++){

                    if(lists.get(i).getUnita().equals("1")){

                        total = Float.parseFloat(lists.get(i).getG_price())*Float.parseFloat(lists.get(i).getG_nb())+total;

                    }else{

                        total = Float.parseFloat(lists.get(i).getG_price())*Float.parseFloat(lists.get(i).getG_weight())+total;

                    }
                    SMDBean smd = new SMDBean();

                    smd.setColor(lists.get(i).getG_color());
                    smd.setGid(lists.get(i).getG_id());
                    smd.setMemo(lists.get(i).getG_note());
                    smd.setNums(lists.get(i).getG_nb());
                    smd.setWeight(lists.get(i).getG_weight());
                    smd.setPrice(lists.get(i).getG_price());
                    smd.setFreight(lists.get(i).getG_the_fare());
                    smd.setDeposit(lists.get(i).getG_the_deposit());
                    smd.setSupplier(lists.get(i).getSupplier());
                    smd.setUnita(lists.get(i).getUnita());
                    smd.setSubtotal(lists.get(i).getG_total());
                    smd.setPayment(lists.get(i).getG_payment_mode());
//                    smd.setTotal(lists.get(i).getG_total());

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

                Log.e("现在的状态",""+S_UPLOAD);

                if(S_UPLOAD == 0){

                    params.put("type",  "insert");
                    params.put("pid",  "");

                }else{

                    params.put("type",  "update");
                    params.put("pid",  smb.getId()+"");

                }

                params.put("total",  total+"");
                OkHttp3ClientManager.getInstance().postStringExecute("http://www.yiyuangy.com/admin/api.Purchase/insert", params, handler, 3,404);

                break;

        }

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    Log.e("返回数据",""+msg.obj.toString());

                    try {

                        JSONArray jsonArray = new JSONArray(msg.obj.toString());
                        for (int i = 0;i<jsonArray.length();i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            StockManagementDetailsBean sdb = new StockManagementDetailsBean();
                            sdb.setS_id(object.getString("pid"));
                            sdb.setG_id(object.getString("gid"));
                            sdb.setG_total(object.getString("subtotal"));
                            sdb.setG_price(object.getString("price"));
                            sdb.setG_weight(object.getString("weight"));
                            sdb.setG_nb(object.getString("nums"));
                            sdb.setG_color(object.getString("color"));
                            sdb.setG_note(object.getString("memo"));
                            sdb.setG_name(object.getString("name"));
                            sdb.setG_payment_mode(object.getString("payment"));
                            sdb.setSupplier(object.getString("supplier"));
                            sdb.setG_the_fare(object.getString("freight"));
                            sdb.setG_the_deposit(object.getString("deposit"));
                            sdb.setNumber(object.getString("p_order"));
                            sdb.setUnita(object.getString("unita"));
                            lists.add(sdb);

                        }

                        handler.sendEmptyMessage(2);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

                case 2:

                    adapter.notifyDataSetChanged();

                    break;

                case 3:

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
            }

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

        OkHttp3ClientManager.getInstance().getStringExecute("http://www.yiyuangy.com/admin/api.Purchase/detail", params, handler, 1, 404);

    }

    @Override
    public void setUploadState(int upLoad) {

        this.S_UPLOAD = upLoad;

        float f_total=0;

        for (StockManagementDetailsBean sb:lists){

            f_total = Float.parseFloat(sb.getG_total())+f_total;

        }

        smb.setTotal(f_total+"");//更改列表的总价
        smd.UpdateStockManagemen(smb);

        if(S_UPLOAD == 0){

            submit.setText("上传");

        }else{

            submit.setText("编辑");
            S_UPLOAD = 2;
        }

        submit.setEnabled(true);
        handler.sendEmptyMessage(2);

    }


    public void chooseGoods() {

        final List<Goods> g = gdao.SelectDimGoods("");
        final SearchGoodsAdapter adapter = new SearchGoodsAdapter(mActivity, g);

        final PtrClassicFrameLayout mPtrFrame;
        RecyclerAdapterWithHF mAdapter;

        //修改商品界面属性
        final Dialog search_dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        View search_goods = LayoutInflater.from(mActivity).inflate(R.layout.search_goods, null);//选择性别的view
        //将布局设置给Dialog
        search_dialog.setContentView(search_goods);
        //获取当前Activity所在的窗体
        Window dialogWindow = search_dialog.getWindow();

        RecyclerView rv = search_goods.findViewById(R.id.search_view);
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rv.setAdapter(mAdapter);
        rv.setNestedScrollingEnabled(false);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                //先判断当前的上传状态，如果是已经上传了就先保存
                //判断是否是已经上传修改的订单还是没有上传修改
                if(smb.getSm_state().equals("1")){

                    S_UPLOAD = 2;
                    smb.setSm_state("2");
                    smd.UpdateSM(smb);
                    //循环存入单条记录
                    for(StockManagementDetailsBean s : lists){

                        smDao.insertSmdd(s);

                    }

                    submit.setText("编辑");
                    submit.setEnabled(true);

                }

                //新添加商品
                StockManagementDetailsBean sb = new StockManagementDetailsBean();
                sb.setNumber(number);
                sb.setG_price("0");
                sb.setG_weight("0");
                sb.setG_color("0");
                sb.setG_nb("0");
                sb.setG_note("");
                sb.setG_payment_mode("1");
                sb.setG_the_fare("0");
                sb.setG_the_deposit("0");
                sb.setSupplier("");
                sb.setG_name(g.get(position).getG_name());
                sb.setG_id(g.get(position).getG_id());
                sb.setG_total("0");
                sb.setUnita("1");
                smDao.insertSmdd(sb);
                lists.add(sb);
                handler.sendEmptyMessage(2);
                search_dialog.dismiss();

            }
        });
        mPtrFrame = search_goods.findViewById(R.id.rotate_header_list_view_frame);
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

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return false;
            }
        });

        final EditText et = search_dialog.findViewById(R.id.goods_search_edit);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                Log.e("sss", "输入文本之前的状态");


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("输入文字中的状态", "输入文字中的状态，count是输入字符数");
                Log.e("输入文字中的状态", "sss");

            }

            @Override
            public void afterTextChanged(Editable editable) {

                g.clear();
                //查询更新
                if (et.getText().toString() != null || !et.getText().toString().equals("")) {

                    List<Goods> gs = gdao.SelectDimGoods(et.getText().toString());

                    for (Goods gg : gs) {

                        g.add(gg);

                    }
                    adapter.notifyDataSetChanged();
                    Log.e("查询到的商品数量=", "" + g.size());

                }


            }
        });

        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;//设置Dialog距离底部的距离

//// 以下这两句是为了保证按钮可以水平满屏
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
// 设置显示位置
        search_dialog.onWindowAttributesChanged(lp);
//       将属性设置给窗体
        search_dialog.show();//显示对话框

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
                lists.set(pos,sdb);
                addSystem(sdb);
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
                lists.set(pos,sdb);
                addSystem(sdb);
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
                lists.set(pos,sdb);
                addSystem(sdb);
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
                lists.set(pos,sdb);
                addSystem(sdb);
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
                lists.set(pos,sdb);
                addSystem(sdb);
                popWindow.dismiss();

            }
        });

        //删除当前这条数据
        Button button6 = view.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StockManagementDetailsBean sdb = lists.get(pos);//拿到当前的数据
                //判断是否是已经上传修改的订单还是没有上传修改
                if (smb.getSm_state().equals("1")) {

                    smb.setSm_state("2");
                    S_UPLOAD = 2;
                    smd.UpdateSM(smb);
                    lists.remove(sdb);
                    //循环存入单条记录
                    for (StockManagementDetailsBean s : lists) {

                        smDao.insertSmdd(s);

                    }

                    submit.setText("编辑");

                } else if (smb.getSm_state().equals("2")){

                    smDao.deleteSm(sdb);//删除这条数据
                    submit.setText("编辑");
                    S_UPLOAD = 2;

                }else{

                    smDao.deleteSm(sdb);//删除这条数据
                    submit.setText("上传");
                    S_UPLOAD = 0;

                }
                getListData(number);//刷新数据
                popWindow.dismiss();

            }
        });

    }

    //存入本地
    public void addSystem(StockManagementDetailsBean smdbs){

            //判断是否是已经上传修改的订单还是没有上传修改
            if (smb.getSm_state().equals("1")) {

                smb.setSm_state("2");
                S_UPLOAD = 2;
                smd.UpdateSM(smb);
                //循环存入单条记录
                for (StockManagementDetailsBean s : lists) {

                    smDao.insertSmdd(s);

                }

                submit.setText("编辑");

            } else if (smb.getSm_state().equals("2")){

                S_UPLOAD = 2;
                //如果是本地数据就直接存入
                smDao.UpdateStock(smdbs);
                submit.setText("编辑");

            }else{

                S_UPLOAD = 0;
                smDao.UpdateStock(smdbs);
                submit.setText("上传");

            }

        submit.setEnabled(true);
        handler.sendEmptyMessage(2);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //判断是否是上传了，上传就删除本地数据
        if(S_UPLOAD ==1){

            smd.deleteSb(smb);
            for (StockManagementDetailsBean s:lists){
                smDao.deleteSm(s);
            }


        }


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

            //判断是否是已经上传修改的订单还是没有上传修改
            if (smb.getSm_state().equals("1")) {

                smb.setSm_state("2");
                S_UPLOAD = 2;
                smd.UpdateSM(smb);
                //循环存入单条记录
                for (StockManagementDetailsBean s : lists) {

                    smDao.insertSmdd(s);

                }

                submit.setText("编辑");

            } else if (smb.getSm_state().equals("2")){

                S_UPLOAD = 2;
                submit.setText("编辑");

            }else{

                S_UPLOAD = 0;
                submit.setText("上传");

            }

            //修改数据
            smd.UpdateStockManagemen(smb);
            submit.setEnabled(true);


        }
    };


}
