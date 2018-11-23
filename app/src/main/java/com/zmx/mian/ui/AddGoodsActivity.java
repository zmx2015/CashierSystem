package com.zmx.mian.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean.MallTypeBean;
import com.zmx.mian.bean_dao.CPDao;
import com.zmx.mian.fragment.Fragment_pro_type;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.model.OrderServer;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IAddGoodsView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-07 18:23
 * 类功能：添加商品
 */
public class AddGoodsActivity extends BaseActivity implements IAddGoodsView {

    private Spinner g_fenlei,sc_fenlei;
    public ArrayAdapter<String> arr_adapter,sc_arr_adapter;
    private List<String> spinner_item,sc_itme;
    private String type_id = "",sc_id="";//类别的id,类别名称，
    private List<CommodityPositionGD> cp = new ArrayList<>(); //List数据
    private List<MallTypeBean> lists = new ArrayList<>();

    private EditText g_name, g_price, g_vipprice;
    private Button submit;

    public static final String action = "addGoods";

    private CheckBox store_sj, online_sj;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_goods;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        g_name = findViewById(R.id.add_name);
        g_price = findViewById(R.id.add_price);
        g_vipprice = findViewById(R.id.add_vip_price);

        store_sj = findViewById(R.id.store_sj);
        store_sj.setChecked(true);

        online_sj = findViewById(R.id.online_sj);
        online_sj.setChecked(true);


        g_fenlei = findViewById(R.id.g_fenlei);
        sc_fenlei = findViewById(R.id.sc_fenlei);
        sc_fenlei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sc_id = lists.get(i).getId()+"";
                if(sc_id.equals("0")){
                    sc_id="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        g_fenlei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                type_id = cp.get(i).getId()+"";

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        submit = findViewById(R.id.button_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //判断输入是否为空或者非法输入
                if (g_name.getText().toString().equals("")) {

                    Toast("名称不能为空！");

                } else if (g_price.getText().toString().equals("") || !Tools.isNumeric(g_price.getText().toString())) {

                    Toast("零售价不能为空或者非法输入！");

                } else if (g_vipprice.getText().toString().equals("") || !Tools.isNumeric(g_vipprice.getText().toString())) {

                    Toast("会员价价不能为空或者非法输入！");

                } else if (type_id.equals("0")) {

                    Toast("请选择门店分类！");

                } else {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("admin", MyApplication.getName());
                    params.put("mid", MyApplication.getStore_id());
                    params.put("pckey", new Tools().getKey(mActivity));
                    params.put("account", "0");
                    params.put("name", g_name.getText().toString());
                    params.put("group", type_id);
                    params.put("mall_group", sc_id);
                    params.put("gds_price", g_price.getText().toString());
                    params.put("vip_price", g_vipprice.getText().toString());

                    if (store_sj.isChecked()) {

                        params.put("store_state", "1");

                    } else {

                        params.put("store_state", "0");

                    }

                    if (online_sj.isChecked()) {

                        params.put("mall_state", "1");

                    } else {

                        params.put("mall_state", "0");

                    }

                    OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/insert", params, handler, 2, 404);

                }

            }
        });

        selectCP();


    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 0:

                    cp.clear();
                    JSONObject json = null;
                    try {
                        json = new JSONObject(msg.obj.toString());
                        //先判断有没有分类先
                        if (json.getString("code").equals("1")) {

                            //先添加一个未选择
                            CommodityPositionGD c = new CommodityPositionGD();
                            c.setType("0");
                            c.setName("未选择");
                            c.setId(Long.parseLong("0"));
                            cp.add(c);
                            //有分类先保存分类数据
                            JSONArray j_store = json.getJSONArray("list");
                            for (int i = 0; i < j_store.length(); i++) {

                                CommodityPositionGD cpGD = new CommodityPositionGD();

                                JSONObject js = j_store.getJSONObject(i);
                                cpGD.setType(js.getString("id"));
                                cpGD.setName(js.getString("gname"));
                                cpGD.setId(Long.parseLong(js.getString("id")));
                                cp.add(cpGD);

                            }

                            if (cp.size() > 0) {

                                selectMall();

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 1:

                    JSONObject mall = null;

                    try {
                        mall = new JSONObject(msg.obj.toString());
                        //判断是否有商城分类，有就保存本地
                        if (mall.getString("code").equals("1")) {

                            //有分类先保存分类数据
                            JSONArray j_mall = mall.getJSONArray("list");

                            MallTypeBean m = new MallTypeBean();//模拟一个未分类
                            m.setState("0");
                            m.setTname("未选择");
                            m.setMid("0");
                            lists.add(m);

                            for (int i = 0; i < j_mall.length(); i++) {

                                JSONObject j = j_mall.getJSONObject(i);
                                MallTypeBean mtb = new MallTypeBean();
                                mtb.setId(Long.parseLong(j.getString("id")));
                                mtb.setTname(j.getString("tname"));
                                mtb.setState(j.getString("state"));
                                mtb.setSort(j.getString("sort"));
                                mtb.setMid(j.getString("mid"));
                                lists.add(mtb);

                            }

                        }

                        spinner_item = new ArrayList<>();
                        for (int i = 0; i < cp.size(); i++) {

                            spinner_item.add(cp.get(i).getName());

                        }
                        sc_itme = new ArrayList<>();
                        for (int i = 0; i < lists.size(); i++) {

                            sc_itme.add(lists.get(i).getTname());

                        }


                        //适配器
                        arr_adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, spinner_item);
                        //设置样式
                        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        g_fenlei.setAdapter(arr_adapter);


                        //适配器
                        sc_arr_adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, sc_itme);
                        //设置样式
                        sc_arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //加载适配器
                        sc_fenlei.setAdapter(sc_arr_adapter);

                        dismissLoadingView();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

                case 2:

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if(jsonObject.getString("code").equals("1")){

                            Toast(jsonObject.getString("content"));
                            mActivity.finish();

                        }else{

                            Toast(jsonObject.getString("content"));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 404:
                    break;

            }

        }
    };


    @Override
    public void getAddMessage(String msg) {

        try {

            JSONObject jsonObject = new JSONObject(msg);
            String status = jsonObject.getString("status");
            if (status.equals("1")) {

                Intent intent = new Intent(action);
                sendBroadcast(intent);
                Toast(jsonObject.getString("content"));
            } else {
                Toast(jsonObject.getString("content"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //查询类别
    public void selectCP() {

        showLoadingView("加载中");
        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("type", "store");
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.class/typeList", params, handler, 0, 404);

    }

    //查询商城分类
    public void selectMall() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("type", "mall");
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.class/typeList", params, handler, 1, 404);

    }

}
