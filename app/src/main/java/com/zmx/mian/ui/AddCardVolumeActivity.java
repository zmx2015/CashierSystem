package com.zmx.mian.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.http.UrlConfig;
import com.zmx.mian.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-22 11:53
 * 类功能：创建卡卷
 */
public class AddCardVolumeActivity extends BaseActivity {

    private EditText card_view1, card_view2, card_view3,card_view4;
    private Spinner card_sp;
    private Button submit;
    private String type = "";//优惠卷的类别
    public ArrayAdapter<String> arr_adapter;
    private List<String> item = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_card_volume;
    }

    @Override
    protected void initViews() {

        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        card_view1 = findViewById(R.id.card_view1);
        card_view2 = findViewById(R.id.card_view2);
        card_view3 = findViewById(R.id.card_view3);
        card_view4 = findViewById(R.id.card_view4);

        item.add("未选择");
        item.add("优惠卷");
        card_sp = findViewById(R.id.card_sp);
        //适配器
        arr_adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, item);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        card_sp.setAdapter(arr_adapter);

        card_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    type = "1";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断输入是否为空
                if (TextUtils.isEmpty(card_view1.getText().toString()) || !new Tools().isNumeric(card_view1.getText().toString())) {
                    Toast("面值非法输入！");
                    return;
                } else if (TextUtils.isEmpty(card_view2.getText().toString()) || !new Tools().isNumeric(card_view2.getText().toString())) {
                    Toast("满X可用非法输入！");
                    return;
                } else if (TextUtils.isEmpty(card_view3.getText().toString()) || !new Tools().isNumeric(card_view3.getText().toString())) {
                    Toast("有效天数非法输入！");
                    return;
                } else if (type.equals("")) {
                    Toast("请选择优惠卷类别！");
                }

                showLoadingView("提交中...");
                loadData();

            }
        });

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:
                    dismissLoadingView();

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            Toast(jsonObject.getString("content"));
                            mActivity.finish();

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


    public void loadData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("quota", card_view1.getText().toString());
        params.put("term", card_view2.getText().toString());
        params.put("types", type);
        params.put("days", card_view3.getText().toString());
        params.put("content", card_view4.getText().toString());
        OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.ADD_CARD, params, handler, 1, 404);

    }


}
