package com.zmx.mian.adapter;

import android.app.Dialog;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

                showPhoto(c, view,position);

            }
        });

        return convertView;
    }


    public class MyView {
        ImageView icon;
        TextView name, price_text, vip_price_text;
        MyButton button1;
    }




    private TextView textView1, textView2, textView3, textView, text_yuan, goods_name;
    private RelativeLayout rl_layout1, rl_layout2, rl_layout3;
    private String s_variable = "";
    private int L_STATE = 0;//点击哪个布局的状态，0为没有选择，1为点击件数，2为点击单价，3位点击重量，4为点击行费，5位点击押金
    private Dialog modify_dialogs;//弹出框
    private int I_STATE = 1;//斤和件，2为斤，1位件，默认为件
    public void showPhoto(final Goods g,final View v,final int position) {

        View view;//选择性别的view

        modify_dialogs = new Dialog(context, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        view = LayoutInflater.from(context).inflate(R.layout.dialog_shopping, null);
        //将布局设置给Dialog
        modify_dialogs.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = modify_dialogs.getWindow();

        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //        lp.y = 20;//设置Dialog距离底部的距离

        //// 以下这两句是为了保证按钮可以水平满屏
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        modify_dialogs.onWindowAttributesChanged(lp);
        //       将属性设置给窗体
//        modify_dialogs.setCanceledOnTouchOutside(false);
        modify_dialogs.show();//显示对话框

        goods_name = view.findViewById(R.id.goods_name);
        goods_name.setText("采购：" + g.getG_name());
        textView1 = view.findViewById(R.id.textView1);
        textView1.setText("1");
        textView2 = view.findViewById(R.id.textView2);
        textView2.setText(g.getG_price());
        textView3 = view.findViewById(R.id.textView3);
        textView = view.findViewById(R.id.textView);
        textView.setText(g.getG_price());
        text_yuan = view.findViewById(R.id.text_yuan);
        rl_layout1 = view.findViewById(R.id.rl_layout1);
        rl_layout2 = view.findViewById(R.id.rl_layout2);
        rl_layout3 = view.findViewById(R.id.rl_layout3);

        rl_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 1;
                s_variable = "";
                rl_layout1.setBackgroundColor(context.getResources().getColor(R.color.tou));
                rl_layout2.setBackgroundColor(context.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        });

        rl_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 2;
                s_variable = "";
                rl_layout1.setBackgroundColor(context.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(context.getResources().getColor(R.color.tou));
                rl_layout3.setBackgroundColor(context.getResources().getColor(R.color.white));

            }
        });

        rl_layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 3;

                s_variable = "";
                rl_layout1.setBackgroundColor(context.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(context.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(context.getResources().getColor(R.color.tou));

            }
        });


        TextView btn_price_1 = view.findViewById(R.id.btn_price_1);
        btn_price_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("点击了", "点击了1");


                s_variable = s_variable + 1;

                ToCalculate();


            }
        });
        TextView btn_price_2 = view.findViewById(R.id.btn_price_2);
        btn_price_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("点击了", "点击了2");

                s_variable = s_variable + 2;

                ToCalculate();

            }
        });

        TextView btn_price_3 = view.findViewById(R.id.btn_price_3);
        btn_price_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_variable = s_variable + 3;

                ToCalculate();


            }
        });

        TextView btn_price_4 = view.findViewById(R.id.btn_price_4);
        btn_price_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_variable = s_variable + 4;
                ToCalculate();


            }
        });

        TextView btn_price_5 = view.findViewById(R.id.btn_price_5);
        btn_price_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_variable = s_variable + 5;

                ToCalculate();


            }
        });

        TextView btn_price_6 = view.findViewById(R.id.btn_price_6);
        btn_price_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_variable = s_variable + 6;

                ToCalculate();


            }
        });

        TextView btn_price_7 = view.findViewById(R.id.btn_price_7);
        btn_price_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_variable = s_variable + 7;

                ToCalculate();


            }
        });

        TextView btn_price_8 = view.findViewById(R.id.btn_price_8);
        btn_price_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_variable = s_variable + 8;
                ToCalculate();


            }
        });

        TextView btn_price_9 = view.findViewById(R.id.btn_price_9);
        btn_price_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_variable = s_variable + 9;
                ToCalculate();


            }
        });

        TextView btn_price_0 = view.findViewById(R.id.btn_price_0);
        btn_price_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_variable = s_variable + 0;

                ToCalculate();


            }
        });

        TextView btn_count_00 = view.findViewById(R.id.btn_count_00);
        btn_count_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        //点
        TextView btn_price_point = view.findViewById(R.id.btn_price_point);
        btn_price_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!s_variable.equals("")) {

                    boolean status = s_variable.contains(".");

                    //包含了
                    if (status) {

                        System.out.println("包含");

                    } else {

                        s_variable = s_variable + ".";
                        ToCalculate();


                    }
                }


            }
        });

        //删除
        TextView btn_price_clear = view.findViewById(R.id.btn_price_clear);
        btn_price_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("现在值s_variable", "：" + s_variable);

                if (!s_variable.equals("") && !s_variable.equals("0")) {

                    Log.e("现在值s_variable", "：事实上");
                    s_variable = s_variable.substring(0, s_variable.length() - 1);
                    if (!s_variable.equals("")) {

                        ToCalculate();

                    } else {

                        s_variable = "";
                        if (L_STATE == 1) {

                            textView1.setText("0");

                        } else if (L_STATE == 2) {

                            textView2.setText("0");

                        } else if (L_STATE == 3) {

                            textView3.setText("0");

                        }

                        ToCalculate();
                    }

                } else {

                    s_variable = "";
                    Log.e("现在值s_variable", "：反反复复" + s_variable);
                    if (L_STATE == 1) {

                        textView1.setText("0");

                    } else if (L_STATE == 2) {

                        textView2.setText("0");

                    } else if (L_STATE == 3) {

                        textView3.setText("0");

                    }

                    ToCalculate();

                }

            }
        });

        //收款
        TextView btn_price_shoukuan = view.findViewById(R.id.btn_price_shoukuan);
        btn_price_shoukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //先判断总价是否为空
                float f = Float.parseFloat(textView.getText().toString());

                if(f > 0){


                    ViceOrder vo = new ViceOrder();
                    vo.setGoods_id(Integer.parseInt(g.getG_id()));
                    vo.setName(g.getG_name());
                    vo.setPrice(textView2.getText().toString());

                    if (I_STATE == 1) {

                        vo.setWeight(textView1.getText().toString());

                    } else {

                        vo.setWeight(textView3.getText().toString());

                    }

                    vo.setSubtotal(textView.getText().toString());
                    vo.setImg(g.getG_img());

                    Message msg = new Message();
                    msg.what = 4;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("vo", vo);
                    msg.setData(bundle);//mes利用Bundle传递数据
                    handler.sendMessage(msg);//用activity中的handler发送消息
                    callback.addAction(v, position);
                    //初始化值
                    s_variable = "";
                    I_STATE = 1;
                    L_STATE = 0;
                    modify_dialogs.dismiss();

                }else{

                    Toast.makeText(context, "商品总价不能小于0元 ", Toast.LENGTH_LONG).show();
                }


            }

        });


        final Button button_jin = view.findViewById(R.id.button_jin);
        final Button button_jian = view.findViewById(R.id.button_jian);
        button_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_jin.setBackgroundColor(context.getResources().getColor(R.color.main_bg));
                button_jian.setBackgroundColor(context.getResources().getColor(R.color.tou));
                I_STATE = 1;
                float t1 = Float.parseFloat(TextUtils.isEmpty(textView1.getText().toString()) ? "0" : textView1.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float total = t1 * t2;
                textView.setText(total + "");
                text_yuan.setText("元/件");
            }
        });
        button_jin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_jin.setBackgroundColor(context.getResources().getColor(R.color.tou));
                button_jian.setBackgroundColor(context.getResources().getColor(R.color.main_bg));
                I_STATE = 2;
                float t3 = Float.parseFloat(TextUtils.isEmpty(textView3.getText().toString()) ? "0" : textView3.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float total = t3 * t2;
                textView.setText(total + "");
                text_yuan.setText("元/斤");

            }
        });


    }

    public void ToCalculate() {

        if (L_STATE == 1) {

            textView1.setText(TextUtils.isEmpty(s_variable) ? "0" : s_variable);
            if (I_STATE == 1) {

                float t1 = Float.parseFloat(TextUtils.isEmpty(textView1.getText().toString()) ? "0" : textView1.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float total = t1 * t2;
                textView.setText(total + "");

            }

        } else if (L_STATE == 2) {

            textView2.setText(TextUtils.isEmpty(s_variable) ? "0" : s_variable);
            if (I_STATE == 1) {

                float t1 = Float.parseFloat(TextUtils.isEmpty(textView1.getText().toString()) ? "0" : textView1.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float total = t1 * t2;
                textView.setText(total + "");

            } else {

                float t3 = Float.parseFloat(TextUtils.isEmpty(textView3.getText().toString()) ? "0" : textView3.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float total = t3 * t2;
                textView.setText(total + "");

            }


        } else if (L_STATE == 3) {

            textView3.setText(TextUtils.isEmpty(s_variable) ? "0" : s_variable);
            if (I_STATE == 2) {

                float t3 = Float.parseFloat(TextUtils.isEmpty(textView3.getText().toString()) ? "0" : textView3.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float total = t3 * t2;
                textView.setText(total + "");

            }


        }

    }


    //加入购物车后弹出的输入重量或者价格框
//    private MyDialog mMyDialog;
//    private TextView goods_name, price;
//    private EditText goods_price, goods_weight;
//    private Button cancel, submit;
//    private ViceOrder vo;
//
//    public void popupView(final Goods g,final View v,final int position) {
//
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_shopping, null);
//        mMyDialog = new MyDialog(context, 0, 0, view, R.style.DialogTheme);
//        mMyDialog.setCancelable(true);
//        mMyDialog.show();
//
//        goods_name = view.findViewById(R.id.goods_name);
//        goods_name.setText(g.getG_name());
//        goods_weight = view.findViewById(R.id.goods_weight);
//        goods_weight.setText("1");
//        price = view.findViewById(R.id.price);
//        price.setText(g.getG_price());
//        goods_price = view.findViewById(R.id.goods_price);
//        goods_price.setText(g.getG_price());
//        goods_price.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                Log.e("输入框的价格", "" + editable);
//
//                if (!TextUtils.isEmpty(editable.toString()) || !TextUtils.isEmpty(goods_weight.getText().toString())) {
//
//                    if (!editable.toString().equals(".")) {
//
//                        price.setText(Float.parseFloat(TextUtils.isEmpty(goods_price.getText().toString()) ? "0" : goods_price.getText().toString()) * Float.parseFloat(goods_weight.getText().toString()) + "");
//
//                    }
//
//                }
//            }
//        });
//        goods_weight.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                if (!TextUtils.isEmpty(goods_price.getText().toString()) || !TextUtils.isEmpty(editable.toString())) {
//                    if (!editable.toString().equals(".")) {
//
//                        price.setText(Float.parseFloat(goods_price.getText().toString()) * Float.parseFloat(TextUtils.isEmpty(goods_weight.getText().toString()) ? "0" : goods_weight.getText().toString()) + "");
//                    }
//                }
//
//            }
//        });
//        cancel = view.findViewById(R.id.cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMyDialog.dismiss();
//            }
//        });
//        submit = view.findViewById(R.id.submit);
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //判断价格和重量是否为空
//                if (TextUtils.isEmpty(goods_price.getText().toString())) {
//
//                    Toast.makeText(context, "请输入价格", Toast.LENGTH_LONG).show();
//
//                } else if (TextUtils.isEmpty(goods_weight.getText().toString())) {
//
//                    Toast.makeText(context, "请输入重量", Toast.LENGTH_LONG).show();
//
//                } else if (!Tools.isNumerics(goods_weight.getText().toString()) || !Tools.isNumerics(goods_price.getText().toString())) {
//
//                    Toast.makeText(context, "非法输入", Toast.LENGTH_LONG).show();
//
//                } else {
//                    vo = new ViceOrder();
//                    vo.setGoods_id(Integer.parseInt(g.getG_id()));
//                    vo.setName(g.getG_name());
//                    vo.setPrice(goods_price.getText().toString());
//                    vo.setWeight(goods_weight.getText().toString());
//                    vo.setSubtotal(price.getText().toString());
//                    vo.setImg(g.getG_img());
//                    Message msg = new Message();
//                    msg.what = 4;
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("vo", vo);
//                    msg.setData(bundle);//mes利用Bundle传递数据
//                    handler.sendMessage(msg);//用activity中的handler发送消息
//                    mMyDialog.dismiss();
//                    Log.e("进来了", "进来了");
//                    callback.addAction(v, position);
//                }
//            }
//        });

//    }


    public interface FoodActionCallback {
        void addAction(View view, int position);
    }

}
