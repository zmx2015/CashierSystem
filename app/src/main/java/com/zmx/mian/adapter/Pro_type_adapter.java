package com.zmx.mian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.Type;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.util.GlideCircleTransform;
import com.zmx.mian.ui.util.MyButton;
import com.zmx.mian.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;


public class Pro_type_adapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Goods> gs;
    private Context context;

    public Pro_type_adapter(Context context, List<Goods> gs) {
        mInflater = LayoutInflater.from(context);
        this.gs = gs;

        this.context = context;
    }


    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (gs != null && gs.size() > 0)
            return gs.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return gs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MyView myView;
        if (convertView == null) {

            myView = new MyView();
            convertView = mInflater.inflate(R.layout.list_pro_type_item, null);
            myView.icon = convertView.findViewById(R.id.typeicon);
            myView.name = convertView.findViewById(R.id.typename);
            myView.price_text = convertView.findViewById(R.id.price_text);
            myView.button1 = convertView.findViewById(R.id.button_md);
            myView.button2 = convertView.findViewById(R.id.button_sc);
            myView.vip_price_text = convertView.findViewById(R.id.vip_price_text);

            convertView.setTag(myView);
        } else {
            myView = (MyView) convertView.getTag();

        }


        final Goods c = gs.get(position);

        myView.name.setText(c.getG_name());
        myView.price_text.setText(Html.fromHtml("零售价：<font color='#FF0000'>"+c.getG_price()+"</font>"));
        myView.vip_price_text.setText(Html.fromHtml("会员价：<font color='#FF0000'>"+c.getVip_g_price()+"</font>"));
        Glide.with(context).load("http://www.yiyuangy.com/uploads/goods/" + c.getG_img()).transform(new GlideCircleTransform(context)).error(R.mipmap.logo).into(myView.icon);

        myView.button1.setTag(c);

        final String store_state = c.getStore_state();
        if (store_state.equals("1")) {

            //字体颜色
            myView.button1.setTextColori(android.graphics.Color.WHITE);
            //字体大小
            myView.button1.setTextSize(14);
            //设置圆角
            myView.button1.setFillet(true);
            //背景色
            myView.button1.setBackColor(Color.parseColor("#31C17B"));
            //选中的背景色
            myView.button1.setBackColorSelected(Color.parseColor("#31C17B"));
            myView.button1.setText("门店下架");

        } else {

            //字体颜色
            myView.button1.setTextColori(android.graphics.Color.WHITE);
            //字体大小
            myView.button1.setTextSize(14);
            //设置圆角
            myView.button1.setFillet(true);
            //背景色
            myView.button1.setBackColor(Color.parseColor("#9E9E9E"));
            //选中的背景色
            myView.button1.setBackColorSelected(Color.parseColor("#9E9E9E"));
            myView.button1.setText("门店上架");

        }

        myView.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String store;

                if (store_state.equals("1")) {

                    store = "0";

                } else {

                    store = "1";

                }

                c.setStore_state(store);
                submit(c, store, c.getMall_state());

            }
        });


        myView.button2.setTag(c);
        final String mall_state = c.getMall_state();
        if (mall_state.equals("1")) {

            //字体颜色
            myView.button2.setTextColori(android.graphics.Color.WHITE);
            //字体大小
            myView.button2.setTextSize(14);
            //设置圆角
            myView.button2.setFillet(true);
            //背景色
            myView.button2.setBackColor(Color.parseColor("#31C17B"));
            //选中的背景色
            myView.button2.setBackColorSelected(Color.parseColor("#37a670"));
            myView.button2.setText("商城下架");

        } else {

            //字体颜色
            myView.button2.setTextColori(android.graphics.Color.WHITE);
            //字体大小
            myView.button2.setTextSize(14);
            //设置圆角
            myView.button2.setFillet(true);
            //背景色
            myView.button2.setBackColor(Color.parseColor("#9E9E9E"));
            //选中的背景色
            myView.button2.setBackColorSelected(Color.parseColor("#BDBDBD"));
            myView.button2.setText("商城上架");
        }

        myView.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mall;
                if (mall_state.equals("1")) {

                    mall = "0";


                } else {

                    mall = "1";

                }
                c.setMall_state(mall);
                submit(c, c.getStore_state(), mall);

            }
        });


        return convertView;
    }


    public class MyView {
        ImageView icon;
        TextView name, price_text,vip_price_text;
        MyButton button1, button2;
    }

    public void submit(Goods g, String store_state, String mall_state) {

        Log.e("状态：", "" + store_state);

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(context));
        params.put("account", "0");
        params.put("gid", g.getG_id());

        String type_id = g.getCp_group();
        type_id = type_id.substring(type_id.indexOf("-") + 1);
        params.put("group", type_id);
        params.put("gds_price", g.getG_price());
        params.put("name", g.getG_name());
        params.put("vip_price", g.getVip_g_price());
        params.put("store_state", store_state);
        params.put("mall_state", mall_state);

        OkHttp3ClientManager.getInstance().getStringExecute("http://www.yiyuangy.com/admin/api.goods/update", params, h, 1, 404);

    }

    private android.os.Handler h = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    refresh();

                    Log.e("分类类别", "失败");
                    break;

                case 404:
                    Log.e("分类类别", "成功");
                    break;

            }

        }
    };

}
