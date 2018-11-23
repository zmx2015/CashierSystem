package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-23 1:12
 * 类功能：全局配置界面
 */
public class GlobalConfigurationActivity extends BaseActivity {

    private RelativeLayout relative1,relative2,relative3,relative4,relative5,relative6,relative7;

    private String sign = "";//签到送积分
    private String order = "";//消费积分
    private String mobile = "";//绑定手机送积分
    private String userdays = "";//会员日
    private String userday = "";//会员日积分倍数
    private String discounts = "";//会员平时消费折扣
    private String discountsday = "";//会员日消费折扣

    private TextView text1,text2,text3,text4,text5,text6,text7;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_global_configuration;
    }

    @Override
    protected void initViews() {
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);

        relative1 = findViewById(R.id.relative1);
        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert_label("签到随机最大积分","sign",2);

            }
        });

        relative2 = findViewById(R.id.relative2);
        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_label("绑定手机赠送积分","mobile",2);
            }
        });

        relative3 = findViewById(R.id.relative3);
        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        relative4 = findViewById(R.id.relative4);
        relative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_label("会员平时消费折扣","discounts",2);
            }
        });

        relative5 = findViewById(R.id.relative5);
        relative5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_label("会员日消费折扣","discountsday",2);
            }
        });

        relative6 = findViewById(R.id.relative6);
        relative6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_label("设置消费积分","order",2);
            }
        });
        relative7 = findViewById(R.id.relative7);
        relative7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_label("设置会员日消费积分","userday",2);
            }
        });

        loadData();
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.getString("code").equals("1")){

                            JSONObject object = jsonObject.getJSONObject("list");

                            sign = object.getString("sign");
                            order = object.getString("order");
                            mobile = object.getString("mobile");
                            userdays = object.getString("userdays");
                            userday = object.getString("userday");
                            discounts = object.getString("discounts");
                            discountsday = object.getString("discountsday");

                            text1.setText(sign+"分");
                            text2.setText(mobile+"分");
                            text3.setText(userdays+"");
                            text4.setText(discounts+"折");
                            text5.setText(discountsday+"折");
                            text6.setText(order+"分");
                            text7.setText(userday+"倍");

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
                            text1.setText(sign+"分");
                            text2.setText(mobile+"分");
                            text3.setText(userdays+"");
                            text4.setText(discounts+"折");
                            text5.setText(discountsday+"折");
                            text6.setText(order+"分");
                            text7.setText(userday+"倍");

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

    //获取数据
    public void loadData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.config/getconfig", params, handler, 1, 404);

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



    public void alert_label(String text,final String name, final int h) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的
        final EditText et = view.findViewById(R.id.editText);
        et.setHint(text);
        builder.setTitle(text);

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

                    if(name.equals("sign")){

                        sign = et.getText().toString();

                    }else if(name.equals("order")){

                        order = et.getText().toString();

                    }else if(name.equals("mobile")){

                        mobile = et.getText().toString();

                    }else if(name.equals("userday")){

                        userday = et.getText().toString();

                    }else if(name.equals("discounts")){

                        discounts = et.getText().toString();

                    }else if(name.equals("discountsday")){

                        discountsday = et.getText().toString();

                    }

                    DeleteCard(name,et.getText().toString(),h);
                    dialog.dismiss();

                } else {

                    Toast("非法输入!");
                }

            }
        });


    }

}
