package com.zmx.mian.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.mian.R;
import com.zmx.mian.adapter.Pro_type_adapter;
import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.Type;
import com.zmx.mian.bean_dao.goodsDao;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.GoodsDetailsActivity;
import com.zmx.mian.ui.OrderDataActivity;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IGoodsView;
import com.zmx.mian.view.IprotypeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_pro_type extends Fragment{

    private goodsDao gdao;
    private List<Goods> gs = new ArrayList<>();
    private GridView listView;
    private Pro_type_adapter adapter;
    private String typename;

    //修改商品界面属性
    private Dialog modify_dialog;//弹出框
    private View modify_goods;//选择性别的view
    private EditText g_name, g_price, g_vipprice;
    private Button button_modify, button_cancel;
    private Spinner g_fenlei;
    private List<CommodityPosition> cp;
    private List<String> spinner_item;
    public ArrayAdapter<String> arr_adapter;

    Goods goods;//要修改的商品类，修改成功后用到

    String type_id = "", type = "", ty_name = "";//类别的id,类别名称，
    private int position;


    public static final String action = "updateGoods";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro_type, null);

        gdao = new goodsDao();
        listView = view.findViewById(R.id.listView);
        typename = getArguments().getString("typename");
        ((TextView) view.findViewById(R.id.toptype)).setText(typename);
        adapter = new Pro_type_adapter(getActivity(), gs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


                // 通过Intent传递对象给Service
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods", gs.get(arg2));
                intent.setClass(Fragment_pro_type.this.getActivity(), GoodsDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        return view;
    }

    private Handler h = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    //通知界面更新
                    Intent intent = new Intent(action);
                    Fragment_pro_type.this.getActivity().sendBroadcast(intent);

                    break;

                case 2:

                    //修改后返回的状态
                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if (jsonObject.getString("status").equals("1")) {

                            Toast.makeText(Fragment_pro_type.this.getActivity(), "修改成功", Toast.LENGTH_LONG).show();

//                gdao.UpdateGoods(goods);//修改本地数据
                            gs.set(position, goods);
                            h.sendEmptyMessage(1);

                        } else {

                            Toast.makeText(Fragment_pro_type.this.getActivity(), "修改失败", Toast.LENGTH_LONG).show();

                        }
                        modify_dialog.dismiss();
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }
    };

    public void setData(List<Goods> gss, String typenames, List<CommodityPosition> cps) {

        spinner_item = new ArrayList<>();
        this.cp = cps;

        for (CommodityPosition p : cp) {

            spinner_item.add(p.getName());

        }

        for (Goods g : gss) {
            if (g.getCp_name().equals(typenames)) {
                gs.add(g);
            }
        }

        h.sendEmptyMessage(1);

    }

    public void showPhoto(final Goods g) {

        modify_dialog = new Dialog(this.getActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        modify_goods = LayoutInflater.from(this.getActivity()).inflate(R.layout.modify_goods, null);
        g_name = modify_goods.findViewById(R.id.g_name);
        g_name.setText(g.getG_name());
        g_price = modify_goods.findViewById(R.id.g_price);
        g_price.setText(g.getG_price());
        g_vipprice = modify_goods.findViewById(R.id.g_vipprice);
        g_vipprice.setText(g.getVip_g_price());
        g_fenlei = modify_goods.findViewById(R.id.g_fenlei);
        //适配器
        arr_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, spinner_item);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        g_fenlei.setAdapter(arr_adapter);

        for (int i = 0; i < spinner_item.size(); i++) {

            if (g.getCp_name().equals(spinner_item.get(i))) {

                g_fenlei.setSelection(i, true);
                type_id = cp.get(i).getType();
                type_id = type_id.substring(type_id.indexOf("-") + 1);

                type = cp.get(i).getType();
                ty_name = cp.get(i).getName();

            }

        }

        g_fenlei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                type_id = cp.get(i).getType();
                type_id = type_id.substring(type_id.indexOf("-") + 1);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        button_modify = modify_goods.findViewById(R.id.button_modify);
        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断输入是否为空或者非法输入
                if (g_name.getText().toString().equals("")) {

                    Toast.makeText(Fragment_pro_type.this.getActivity(), "名称不能为空！", Toast.LENGTH_LONG).show();

                } else if (g_price.getText().toString().equals("") || !Tools.isNumeric(g_price.getText().toString())) {
                    Toast.makeText(Fragment_pro_type.this.getActivity(), "零售价不能为空或者非法输入！", Toast.LENGTH_LONG).show();
                } else if (g_vipprice.getText().toString().equals("") || !Tools.isNumeric(g_vipprice.getText().toString())) {
                    Toast.makeText(Fragment_pro_type.this.getActivity(), "会员价价不能为空或者非法输入！", Toast.LENGTH_LONG).show();
                } else {

//                    String mid = MySharedPreferences.getInstance(Fragment_pro_type.this.getActivity()).getString(MySharedPreferences.store_id, "");
//                    String name = MySharedPreferences.getInstance(Fragment_pro_type.this.getActivity()).getString(MySharedPreferences.name, "");
//
                    goods = new Goods(g.getG_id(), g.getG_img(), g_price.getText().toString(), g_name.getText().toString(), ty_name, type, g_vipprice.getText().toString(),g.getMall_state(),g.getStore_state());
//                    op.UpdateGoods(mid, name, g.getG_id(), type_id, g_price.getText().toString(), g_name.getText().toString(), g_vipprice.getText().toString());
//
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("admin", MySharedPreferences.getInstance(Fragment_pro_type.this.getActivity()).getString(MySharedPreferences.name, ""));
                    params.put("mid", MySharedPreferences.getInstance(Fragment_pro_type.this.getActivity()).getString(MySharedPreferences.store_id, ""));
                    params.put("pckey", new Tools().getKey(Fragment_pro_type.this.getActivity()));
                    params.put("account", "0");
                    params.put("gid", g.getG_id());
                    params.put("group", type_id);
                    params.put("gds_price", g_price.getText().toString());
                    params.put("name", g_name.getText().toString());
                    params.put("vip_price", g_vipprice.getText().toString());
                    params.put("store_state", g.getStore_state());
                    params.put("mall_state", g.getMall_state());

                    OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/update", params, h, 2, 404);


                }
            }

        });

        button_cancel = modify_goods.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modify_dialog.dismiss();
            }
        });

        //将布局设置给Dialog
        modify_dialog.setContentView(modify_goods);
        //获取当前Activity所在的窗体
        Window dialogWindow = modify_dialog.getWindow();

        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;//设置Dialog距离底部的距离

//// 以下这两句是为了保证按钮可以水平满屏
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
// 设置显示位置
        modify_dialog.onWindowAttributesChanged(lp);
//       将属性设置给窗体
        modify_dialog.show();//显示对话框
    }

}
