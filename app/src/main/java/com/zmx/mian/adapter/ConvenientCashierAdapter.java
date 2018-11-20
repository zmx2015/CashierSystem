package com.zmx.mian.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zmx.mian.R;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.ui.util.GlideCircleTransform;
import com.zmx.mian.ui.util.MyButton;
import com.zmx.mian.ui.util.MyDialog;
import com.zmx.mian.util.Tools;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-18 14:27
 * 类功能：前台收银商品适配器
 */
public class ConvenientCashierAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Goods> gs;
    private Context context;
    private Handler handler;

    private FoodActionCallback callback;

    public ConvenientCashierAdapter(Context context, List<Goods> gs, Handler handler,FoodActionCallback callback) {
        mInflater = LayoutInflater.from(context);
        this.gs = gs;
        this.handler = handler;
        this.context = context;
        this.callback = callback;
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
    public View getView(final int position,View convertView, ViewGroup parent) {

        final ConvenientCashierAdapter.MyView myView;

        if (convertView == null) {

            myView = new ConvenientCashierAdapter.MyView();
            convertView = mInflater.inflate(R.layout.convenient_cashier_adapter, null);
            myView.icon = convertView.findViewById(R.id.typeicon);
            myView.name = convertView.findViewById(R.id.typename);
            myView.price_text = convertView.findViewById(R.id.price_text);
            myView.button1 = convertView.findViewById(R.id.button_sc);
            myView.vip_price_text = convertView.findViewById(R.id.vip_price_text);

            convertView.setTag(myView);
        } else {
            myView = (ConvenientCashierAdapter.MyView) convertView.getTag();

        }


        final Goods c = gs.get(position);

        myView.name.setText(c.getG_name());
        myView.price_text.setText(Html.fromHtml("零售价：<font color='#FF0000'>" + c.getG_price() + "</font>"));
        myView.vip_price_text.setText(Html.fromHtml("会员价：<font color='#FF0000'>" + c.getVip_g_price() + "</font>"));
        Glide.with(context).load("http://www.yiyuangy.com/uploads/goods/" + c.getG_img()).transform(new GlideCircleTransform(context)).error(R.mipmap.logo).into(myView.icon);

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
        myView.button1.setText("加入购物车");

        myView.button1.setTag(position);

        myView.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupView(c, view,position);

            }
        });

        return convertView;
    }


    public class MyView {
        ImageView icon;
        TextView name, price_text, vip_price_text;
        MyButton button1;
    }

    //加入购物车后弹出的输入重量或者价格框
    private MyDialog mMyDialog;
    private TextView goods_name, price;
    private EditText goods_price, goods_weight;
    private Button cancel, submit;
    private ViceOrder vo;

    public void popupView(final Goods g,final View v,final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_shopping, null);
        mMyDialog = new MyDialog(context, 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);
        mMyDialog.show();

        goods_name = view.findViewById(R.id.goods_name);
        goods_name.setText(g.getG_name());
        goods_weight = view.findViewById(R.id.goods_weight);
        goods_weight.setText("1");
        price = view.findViewById(R.id.price);
        price.setText(g.getG_price());
        goods_price = view.findViewById(R.id.goods_price);
        goods_price.setText(g.getG_price());
        goods_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.e("输入框的价格", "" + editable);

                if (!TextUtils.isEmpty(editable.toString()) || !TextUtils.isEmpty(goods_weight.getText().toString())) {

                    if (!editable.toString().equals(".")) {

                        price.setText(Float.parseFloat(TextUtils.isEmpty(goods_price.getText().toString()) ? "0" : goods_price.getText().toString()) * Float.parseFloat(goods_weight.getText().toString()) + "");

                    }

                }
            }
        });
        goods_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!TextUtils.isEmpty(goods_price.getText().toString()) || !TextUtils.isEmpty(editable.toString())) {
                    if (!editable.toString().equals(".")) {

                        price.setText(Float.parseFloat(goods_price.getText().toString()) * Float.parseFloat(TextUtils.isEmpty(goods_weight.getText().toString()) ? "0" : goods_weight.getText().toString()) + "");
                    }
                }

            }
        });
        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyDialog.dismiss();
            }
        });
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断价格和重量是否为空
                if (TextUtils.isEmpty(goods_price.getText().toString())) {

                    Toast.makeText(context, "请输入价格", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(goods_weight.getText().toString())) {

                    Toast.makeText(context, "请输入重量", Toast.LENGTH_LONG).show();

                } else if (!Tools.isNumerics(goods_weight.getText().toString()) || !Tools.isNumerics(goods_price.getText().toString())) {

                    Toast.makeText(context, "非法输入", Toast.LENGTH_LONG).show();

                } else {
                    vo = new ViceOrder();
                    vo.setGoods_id(Integer.parseInt(g.getG_id()));
                    vo.setName(g.getG_name());
                    vo.setPrice(goods_price.getText().toString());
                    vo.setWeight(goods_weight.getText().toString());
                    vo.setSubtotal(price.getText().toString());
                    vo.setImg(g.getG_img());
                    Message msg = new Message();
                    msg.what = 4;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("vo", vo);
                    msg.setData(bundle);//mes利用Bundle传递数据
                    handler.sendMessage(msg);//用activity中的handler发送消息
                    mMyDialog.dismiss();
                    Log.e("进来了", "进来了");
                    callback.addAction(v, position);
                }
            }
        });

    }


    public interface FoodActionCallback {
        void addAction(View view, int position);
    }

}
