package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.CCDetailAdapter;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.bean.ViceOrder_A;
import com.zmx.mian.bean.members.MembersList;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.util.MyButton;
import com.zmx.mian.ui.util.MyDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-18 20:06
 * 类功能：前台收银订单详情
 */
public class ConvenientCashierDetailActivity extends BaseActivity {

    private MainOrder mo;
    private List<ViceOrder> vo;
    private ListView listView;
    private CCDetailAdapter adapter;
    private TextView text,g_discounts,member_text,payment;
    private String member="";//会员号

    private Button submit;

    private RelativeLayout relative_number,relative_payment;

    private float discounts=1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_convenient_cashier_detail;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        View v = findViewById(R.id.back_button);

        mo = (MainOrder) this.getIntent().getSerializableExtra("mo");
        vo = mo.getLists();

        listView = findViewById(R.id.listView);
        adapter = new CCDetailAdapter(this, mo.getLists(),handler);
        listView.setAdapter(adapter);

        payment = findViewById(R.id.payment);
        payment.setText("现金支付");
        text = findViewById(R.id.text);
        text.setText("共" + mo.getLists().size() + "件商品   总计" + mo.getTotal() + "元");
        g_discounts = findViewById(R.id.g_discounts);
        member_text = findViewById(R.id.member_text);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 设置返回数据
                Bundle bundle = new Bundle();
                bundle.putString("state","1");
                bundle.putSerializable("mo",mo);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                // 返回intent
                setResult(RESULT_OK, intent);
                mActivity.finish();

            }
        });
        submit = findViewById(R.id.submit);
        submit.setText("收款："+mo.getTotal()+"元");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断是否有商品
                if(vo.size()>0){

                    //判断是否是会员支付，如果要有会员号
                    if(mo.getMo_classify().equals("3")){

                        if(!mo.getAccount().equals("1") || mo.getAccount().equals("")){

                            submitOrder();

                        }else{

                            Toast.makeText(mActivity,"没有选择会员，无法使用会员支付！",Toast.LENGTH_LONG).show();
                        }

                    }else{

                        submitOrder();
                    }


                }else{
                    Toast.makeText(mActivity,"没有商品，无法提交",Toast.LENGTH_LONG).show();
                }
            }
        });
        //选择会员
        relative_number = findViewById(R.id.relative_number);
        relative_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_edit();
            }
        });

        relative_payment = findViewById(R.id.relative_payment);
        relative_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_payment();
            }
        });

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 0:
                    dismissLoadingView();
                    Log.e("会员消息", "" + msg.obj.toString());
                    try {

                        JSONObject bodys = new JSONObject(msg.obj.toString());

                        if (bodys.has("msg")) {

                            Toast.makeText(mActivity,"会员不存在",Toast.LENGTH_LONG).show();

                        } else {

                            discounts = Float.parseFloat(bodys.getString("discounts"));
                            float zMoney = (float) (Math
                                    .round((float) (Float.parseFloat(mo.getTotal()) * discounts) * 100)) / 100;// 折后价
                            g_discounts.setText(Float.parseFloat(mo.getTotal()) - zMoney+"");
                            member_text.setText(member);
                            submit.setText("收款："+zMoney+"元");
                            mo.setAccount(member);//设置会员号
                        }

                        } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                    //删除商品后重新计算价格
                case 1:

                    float total = 0;

                    for(ViceOrder v:vo){

                        total = Float.parseFloat(v.getSubtotal())+total;
                    }

                    mo.setTotal(total+"");//重新设计总价格
                    text.setText("共" + mo.getLists().size() + "件商品   总计" + mo.getTotal() + "元");
                    float zMoney = (float) (Math
                        .round((float) (Float.parseFloat(mo.getTotal()) * discounts) * 100)) / 100;// 折后价

                    g_discounts.setText(Float.parseFloat(mo.getTotal()) - zMoney+"");

                    submit.setText("收款："+zMoney+"元");

                    break;

                case 2:

                    Log.e("提交成功","提交成功"+msg.obj.toString());

                    dismissLoadingView();
                    JSONObject bodys = null;

                    try {
                        bodys = new JSONObject(msg.obj.toString());

                        if (bodys != null) {

                            if (bodys.getString("status").equals("1")) {

                                Toast.makeText(mActivity,"提交成功！",Toast.LENGTH_LONG).show();

                                // 设置返回数据
                                Bundle bundle = new Bundle();
                                bundle.putString("state","0");
                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                // 返回intent
                                setResult(RESULT_OK, intent);

                                mActivity.finish();

                            } else {

                                Toast.makeText(mActivity,"提交失败！",Toast.LENGTH_LONG).show();

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    break;
                case 404:

                    dismissLoadingView();

                    Toast("连接服务器失败，请重新连接！");

                    break;
            }

        }
    };

    //查找会员
    public void searchMembers(String member) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(member));
        params.put("account", member);
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.lineapi/getUserInfo", params, handler, 0, 404);

    }


    public void alert_edit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的
        final EditText et = view.findViewById(R.id.editText);
        builder.setTitle("请输入会员号");

        final AlertDialog dialog = builder.create();
        dialog.setView(view);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "查找会员", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "清除会员", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                discounts = 1;

                g_discounts.setText("0.00元");
                member_text.setText("未选择");
                submit.setText("收款："+mo.getTotal()+"元");
                mo.setAccount("1");//设置会员号

                dialog.dismiss();
            }
        });
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et.getText().toString())) {

                    showLoadingView("加载中...");
                    member = et.getText().toString();
                    searchMembers(et.getText().toString());
                    dialog.dismiss();

                } else {

                    Toast.makeText(mActivity, "请输入会员号", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private MyDialog mMyDialog;
    private RelativeLayout relayout1,relayout2,relayout3,relayout4;
    private Button cancel;

    public void Dialog_payment(){

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_payment, null);
        mMyDialog = new MyDialog(this, 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);
        mMyDialog.show();

        relayout1 = view.findViewById(R.id.relayout1);
        relayout2 = view.findViewById(R.id.relayout2);
        relayout3 = view.findViewById(R.id.relayout3);
        relayout4 = view.findViewById(R.id.relayout4);
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyDialog.dismiss();
            }
        });

        relayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                payment.setText("现金支付");
                mo.setPayment(1);
                mo.setMo_classify("4");
                mMyDialog.dismiss();

            }
        });

        relayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                payment.setText("支付宝付");
                mo.setPayment(2);
                mo.setMo_classify("4");
                mMyDialog.dismiss();
            }
        });

        relayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                payment.setText("微信支付");
                mo.setPayment(2);
                mo.setMo_classify("4");
                mMyDialog.dismiss();
            }
        });

        relayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                payment.setText("会员支付");
                mo.setPayment(4);
                mo.setMo_classify("3");
                mMyDialog.dismiss();
            }
        });


    }

    public void submitOrder(){

        showLoadingView("加载中...");
        List<ViceOrder_A> lists = new ArrayList<>();
        for (ViceOrder v : mo.getLists()){

            ViceOrder_A va = new ViceOrder_A();

            va.setGoods_id(v.getGoods_id()+"");
            va.setName(v.getName());
            va.setPrice(v.getPrice());
            va.setSubtotal(v.getSubtotal());
            va.setWeight(v.getWeight());

            lists.add(va);
        }

        Gson g = new Gson();
        String jsonString = g.toJson(lists);

        Map<String, String> mapData = new HashMap<String, String>();
        mapData.put("admin", MyApplication.getName());
        mapData.put("mid", MyApplication.getStore_id());
        mapData.put("pckey", new Tools().getKey(this));
        mapData.put("account", mo.getAccount());
        mapData.put("orderAll", jsonString);
        mapData.put("zmey", mo.getTotal());// 总价

        float zMoney = (float) (Math
                .round((float) (Float.parseFloat(mo.getTotal()) * discounts) * 100)) / 100;// 折后价
        mapData.put("yhui", zMoney+"");
        mapData.put("shishou", "0");// 实收
        mapData.put("yingzhao", "0");// 应找
        mapData.put("payment", mo.getPayment()+"");
        mapData.put("ordernumber", mo.getOrder());
        mapData.put("discount", mo.getDiscount());
        String phpTime = new Date().getTime() + "";
        mapData.put("synchro", phpTime.substring(0, phpTime.length() - 3));
        mapData.put("buytime", phpTime.substring(0, phpTime.length() - 3));
        mapData.put("classify", mo.getMo_classify());

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.order/orderAdd", mapData, handler, 2, 404);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 设置返回数据
            Bundle bundle = new Bundle();
            bundle.putString("state","1");
            bundle.putSerializable("mo",mo);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            // 返回intent
            setResult(RESULT_OK, intent);
            mActivity.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}
