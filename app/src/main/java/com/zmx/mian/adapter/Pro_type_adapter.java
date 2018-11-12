package com.zmx.mian.adapter;

import android.content.Context;
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
import com.zmx.mian.R;
import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.Type;
import com.zmx.mian.ui.util.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;


public class Pro_type_adapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Goods> gs;
    private Context context;
    private Goods c;

    public Pro_type_adapter(Context context, List<Goods> gs) {
        mInflater = LayoutInflater.from(context);
        this.gs = gs;

        this.context = context;
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
        final MyView view;
        if (convertView == null) {
            view = new MyView();
            convertView = mInflater.inflate(R.layout.list_pro_type_item, null);
            view.icon = convertView.findViewById(R.id.typeicon);
            view.name = convertView.findViewById(R.id.typename);
            view.price_text = convertView.findViewById(R.id.price_text);
            view.button1 = convertView.findViewById(R.id.button_md);
            view.button2 = convertView.findViewById(R.id.button_sc);
            convertView.setTag(view);
        } else {
            view = (MyView) convertView.getTag();

        }

        if (gs != null && gs.size() > 0) {

            c = gs.get(position);

            view.name.setText(c.getG_name());
            view.price_text.setText("￥" + c.getG_price());

            Glide.with(context).load(c.getG_img()).transform(new GlideCircleTransform(context)).error(R.mipmap.logo).into(view.icon);


            view.button1.setTag(c);
            final String mall_state = c.getMall_state();

            if (mall_state.equals("1")) {
                view.button1.setText("门店下架");
            } else {
                view.button1.setText("门店上架");
            }

            view.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mall_state.equals("1")) {

                        Log.e("点击了门店下架", "点击了门店下架");

                    } else {
                        Log.e("点击了门店上架", "点击了门店上架");

                    }

                }
            });


            view.button2.setTag(c);
            final String store_state = c.getStore_state();

            if (store_state.equals("1")) {
                view.button2.setText("商城下架");
            } else {
                view.button2.setText("商城上架");
            }

            view.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (store_state.equals("1")) {

                        Log.e("点击了商城上架", "点击了门店下架");

                    } else {
                        Log.e("点击了商城上架", "点击了门店上架");

                    }


                }
            });

        }

        return convertView;
    }


    private class MyView {
        private ImageView icon;
        private TextView name, price_text;
        private Button button1, button2;
    }


}
