package com.zmx.mian.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.MallTypeBean;
import com.zmx.mian.bean_dao.CPDao;
import com.zmx.mian.fragment.Fragment_pro_type;
import com.zmx.mian.fragment.HomeFragment;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.util.GlideCircleTransform;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-13 14:03
 * 类功能：商品详情
 */

public class GoodsDetailsActivity extends BaseActivity {

    private Goods g;

    private ImageView goods_img;
    private EditText g_name, g_price, g_vipprice;
    private Button submit;
    private Spinner g_fenlei,sc_fenlei;
    public ArrayAdapter<String> arr_adapter,sc_arr_adapter;
    private List<String> spinner_item,sc_itme;
    private String type_id = "",sc_id="";//类别的id,类别名称，
    private List<CommodityPositionGD> cp = new ArrayList<>();
    //List数据
    private List<MallTypeBean> lists = new ArrayList<>();

    private CheckBox store_sj, online_sj;

    private String gid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        BackButton(R.id.back_button);

        gid = getIntent().getStringExtra("gid");

        g_name = findViewById(R.id.g_name);
        g_price = findViewById(R.id.g_price);
        g_vipprice = findViewById(R.id.g_vipprice);
        goods_img = findViewById(R.id.goods_img);
        store_sj = findViewById(R.id.store_sj);
        online_sj = findViewById(R.id.online_sj);
        g_fenlei = findViewById(R.id.g_fenlei);
        sc_fenlei = findViewById(R.id.sc_fenlei);

        sc_fenlei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sc_id = lists.get(i).getId()+"";
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

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断输入是否为空或者非法输入
                if (g_name.getText().toString().equals("")) {

                    Toast.makeText(mActivity, "名称不能为空！", Toast.LENGTH_LONG).show();

                } else if (g_price.getText().toString().equals("") || !Tools.isNumeric(g_price.getText().toString())) {
                    Toast.makeText(mActivity, "零售价不能为空或者非法输入！", Toast.LENGTH_LONG).show();
                } else if (g_vipprice.getText().toString().equals("") || !Tools.isNumeric(g_vipprice.getText().toString())) {
                    Toast.makeText(mActivity, "会员价价不能为空或者非法输入！", Toast.LENGTH_LONG).show();
                } else {

                    showLoadingView("加载中...");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("admin", MyApplication.getName());
                    params.put("mid", MyApplication.getStore_id());
                    params.put("pckey", new Tools().getKey(mActivity));
                    params.put("account", "0");
                    params.put("gid", g.getG_id());
                    params.put("group", type_id);
                    params.put("gds_price", g_price.getText().toString());
                    params.put("name", g_name.getText().toString());
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

                    OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/update", params, h, 1, 404);

                }

            }
        });

        selectCP();

    }

    private Handler h = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 0:

                    dismissLoadingView();
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if (jsonObject.getString("code").equals("1")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("list");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                g = new Goods(object.getString("gid"), object.getString("img"), object.getString("gds_price"),
                                        object.getString("name"), "", object.getString("group"), "",
                                        object.getString("vip_price"), object.getString("mall_state"), object.getString("store_state"));

                            }

                            spinner_item = new ArrayList<>();
                            for (int i = 0; i < cp.size(); i++) {

                                spinner_item.add(cp.get(i).getName());

                            }
                            sc_itme = new ArrayList<>();
                            for (int i = 0; i < lists.size(); i++) {

                                sc_itme.add(lists.get(i).getTname());

                            }

                            g_name.setText(g.getG_name());
                            g_price.setText(g.getG_price());
                            g_vipprice.setText(g.getVip_g_price());
                            Glide.with(mActivity).load("http://www.yiyuangy.com/uploads/goods/" + g.getG_img()).transform(new GlideCircleTransform(mActivity)).error(R.mipmap.logo).into(goods_img);
                            if (g.getStore_state().equals("1")) {

                                store_sj.setChecked(true);

                            } else {

                                store_sj.setChecked(false);

                            }
                            if (g.getMall_state().equals("1")) {
                                online_sj.setChecked(true);
                            } else {
                                online_sj.setChecked(false);
                            }


                            //适配器
                            arr_adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, spinner_item);
                            //设置样式
                            arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //加载适配器
                            g_fenlei.setAdapter(arr_adapter);

                            //循环选中默认的分类
                            for (int i = 0; i < spinner_item.size(); i++) {

                                if (g.getCp_group().equals(cp.get(i).getId()+"")) {

                                    g_fenlei.setSelection(i, true);
                                    type_id = cp.get(i).getId()+"";

                                }

                            }

                            //适配器
                            sc_arr_adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, sc_itme);
                            //设置样式
                            sc_arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //加载适配器
                            sc_fenlei.setAdapter(sc_arr_adapter);

                            //循环选中默认的分类
                            for (int i = 0; i < sc_itme.size(); i++) {

                                if (g.getCp_group().equals(lists.get(i).getId()+"")) {

                                    sc_fenlei.setSelection(i, true);
                                    sc_id = lists.get(i).getId()+"";

                                }

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

                case 1:

                    Log.e("修改成功","修改成功");

                    //修改后返回的状态
                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if (jsonObject.getString("code").equals("1")) {

                            Toast.makeText(mActivity, jsonObject.getString("content"), Toast.LENGTH_LONG).show();
                            mActivity.finish();

                        } else {

                            Toast.makeText(mActivity, jsonObject.getString("content"), Toast.LENGTH_LONG).show();

                        }

                        dismissLoadingView();//隐藏加载框

                    } catch (JSONException e) {

                        e.printStackTrace();
                        dismissLoadingView();//隐藏加载框
                        Toast.makeText(mActivity, "获取数据失败！请联系客服", Toast.LENGTH_LONG).show();

                    }

                    break;

                case 3:

                    cp.clear();
                    JSONObject json = null;
                    try {
                        json = new JSONObject(msg.obj.toString());
                        //先判断有没有分类先
                        if (json.getString("code").equals("1")) {

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

                case 4:

                    JSONObject mall = null;

                    try {
                        mall = new JSONObject(msg.obj.toString());
                        //判断是否有商城分类，有就保存本地
                        if (mall.getString("code").equals("1")) {

                            //有分类先保存分类数据
                            JSONArray j_mall = mall.getJSONArray("list");

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

                            if (cp.size() > 0) {

                                selectGoods(gid);

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 404:

                    Toast.makeText(mActivity, "网络连接失败！请检查网络", Toast.LENGTH_LONG).show();
                    dismissLoadingView();//隐藏加载框

                    break;

            }

        }
    };

    @Override
    protected void onDestroy() {

        if (mLoadingDialog != null) {

            mLoadingDialog.dismiss();
        }

        super.onDestroy();
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
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.class/typeList", params, h, 3, 404);

    }


    //查询商城分类
    public void selectMall() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("type", "mall");
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.class/typeList", params, h, 4, 404);

    }

    //根据商品id查询某个商品详情
    public void selectGoods(String gid) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("gid", gid);
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/goods", params, h, 0, 404);

    }

}
