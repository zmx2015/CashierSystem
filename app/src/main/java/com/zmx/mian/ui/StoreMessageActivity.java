package com.zmx.mian.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.http.UrlConfig;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-12-06 0:33
 * 类功能：门店信息
 */
public class StoreMessageActivity extends BaseActivity {

    private TextView text_name,text_address;
    private String name,address;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_message;
    }

    @Override
    protected void initViews() {

        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        text_address = findViewById(R.id.text_address);
        text_name = findViewById(R.id.text_name);
        text_name.setText(MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_name,""));
        text_address.setText(MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_describe,""));
//        loadData();
    }

//    private Handler handler = new Handler(){

//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            switch (msg.what){
//
//                case 1:
//
//                    dismissLoadingView();
//                    try {
//
//                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                        if(jsonObject.getString("code").equals("1")){
//
//                            JSONObject object = jsonObject.getJSONObject("list");
//
//                            name = object.getString("title");
//
//                            text_name.setText(name);
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    break;
//
//            }
//
//        }
//    };

    //获取数据
//    public void loadData() {
//
//        showLoadingView("加载中...");
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("admin", MyApplication.getName());
//        params.put("mid", MyApplication.getStore_id());
//        params.put("pckey", new Tools().getKey(mActivity));
//        params.put("account", "0");
//        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.GET_CONFIG, params, handler, 1, 404);
//
//    }

}
