package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.http.UrlConfig;
import com.zmx.mian.ui.util.CalendarView;
import com.zmx.mian.util.TimeUtil;
import com.zmx.mian.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

                dialog();
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
        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.GET_CONFIG, params, handler, 1, 404);

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
        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.UPDATE_CONFIG, params, handler, h, 404);

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

    public void dialog(){

        LayoutInflater inflater = LayoutInflater.from(this);//获取一个填充器
        View view = inflater.inflate(R.layout.dialog_membersday, null);//填充我们自定义的布局

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

        final CalendarView mCalendarView = view.findViewById(R.id.calendarView);

        //设置显示已经设置的会员日
        List<String> mDatas = new ArrayList<>();
        if(!TextUtils.isEmpty(userdays)){

            boolean status = userdays.contains(",");

            //先判断是否有没有选择了多天
            if(status){

                String[] result1 = userdays.split(",");
                for (int i = 0;i<result1.length;i++){

                    //再判断是否是个位数
                    if(result1[i].length()>1){

                        mDatas.add(TimeUtil.DateConversionDay(new Date()) + result1[i]);

                    }else {

                        mDatas.add(TimeUtil.DateConversionDay(new Date()) +"0"+ result1[i]);

                    }

                }

            }else{

                //再判断是否是个位数
                if(userdays.length()>1){

                    mDatas.add(TimeUtil.DateConversionDay(new Date()) + userdays);

                }else {

                    mDatas.add(TimeUtil.DateConversionDay(new Date()) +"0"+ userdays);

                }

            }

        }

        for (int i = 0;i<mDatas.size();i++){

            Log.e("数据",""+mDatas.get(i).toString());

        }

        // 设置可选日期
        mCalendarView.setSelectDate(mDatas);
        mCalendarView.setChangeDateStatus(true);
        mCalendarView.setOnDataClickListener(new CalendarView.OnDataClickListener() {
            @Override
            public void onDataClick(@NonNull CalendarView view, int year, int month, int day) {

                Log.e("test", "year: " + year);
                Log.e("test", "month,: " + (month + 1));
                Log.e("test", "day: " + day);

            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day) {
                if (select) {

                    Toast.makeText(mActivity
                            , "选中了" + day + "日为会员日", Toast.LENGTH_SHORT).show();
                }
            }
        });



        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showLoadingView("修改中...");
                List<String> s = mCalendarView.getSelectDate();
                StringBuffer buf = new StringBuffer();

                if(s.size()>0){

                    for (int j = 0;j<s.size();j++){

                        buf.append(TimeUtil.getDay(s.get(j).toString()) + ",");

                    }

                    userdays = buf.toString();
                    if(!TextUtils.isEmpty(userdays)){

                        userdays = userdays.substring(0,userdays.length() - 1);

                    }

                }else{
                    userdays = "";
                }

                DeleteCard("userdays",userdays,2);

                popWindow.dismiss();

            }
        });

        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });


    }

}
