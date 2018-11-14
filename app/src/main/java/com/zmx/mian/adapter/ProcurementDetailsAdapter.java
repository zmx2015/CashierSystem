package com.zmx.mian.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.mian.R;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.bean_dao.StockManagementDao;
import com.zmx.mian.bean_dao.StockManagementDetailsDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-25 18:27
 * 类功能：采购详情
 */

public class ProcurementDetailsAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<StockManagementDetailsBean> lists;
    private StockManagementBean smbb;
    private StockManagementDao smd;
    private int state = 0;
    private int I_STATE = 1;//斤和件，2为斤，1位件，默认为件

    public ProcurementDetailsAdapter(Context mContext, List<StockManagementDetailsBean> lists, StockManagementBean smbb) {

        this.mContext = mContext;
        this.lists = lists;
        this.smbb = smbb;
        mLayoutInflater = LayoutInflater.from(mContext);
        smd = new StockManagementDao();

    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewManHolder holder;
        if (view == null) {

            view = mLayoutInflater.inflate(R.layout.procurement_details_adapter, null);
            holder = new ViewManHolder(view);
            view.setTag(holder);
        } else {

            holder = (ViewManHolder) view.getTag();

        }

        final StockManagementDetailsBean sdb = lists.get(i);

        //清除焦点
        holder.note.clearFocus();
        if (holder.note.getTag() instanceof TextWatcher) {
            holder.note.removeTextChangedListener((TextWatcher) (holder.note.getTag()));
        }
        holder.note.setText(TextUtils.isEmpty(sdb.getG_note()) ? "" : sdb.getG_note());
        if (sdb.getG_note().equals("")) {

            holder.note.setHint(sdb.getG_name());

        }

        holder.note.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                    // 此处为失去焦点时的处理内容
                    String s = holder.note.getText().toString();

                    //判断输入框内容是否有改变
                    if(!sdb.getG_note().equals(s)){

                        sdb.setG_note(s);
                        ou.setUploadState();
                    }

                }
            }
        });

//        final TextWatcher oneNoteWatcher = new ProcurementDetailsAdapter.SimpeTextWather() {
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (TextUtils.isEmpty(s)) {
//
//                    sdb.setG_note(null);
//
//                } else {
//
//                    sdb.setG_note(s.toString());
//
//                }
//
//                ou.setUploadState();
//            }
//        };

//        holder.note.addTextChangedListener(oneNoteWatcher);
        holder.note.setTag(i);

        holder.textView1.setText(sdb.getG_name());

        //判断是否是重量或者件数
        if (sdb.getUnita().equals("1") || sdb.getUnita().equals(null)) {

            holder.textView2.setText(sdb.getG_nb()+"件");
            I_STATE = 1;

        } else {

            holder.textView2.setText(sdb.getG_weight()+"斤");
            I_STATE = 2;

        }

        holder.textView3.setText(sdb.getG_price());
        holder.textView4.setText(sdb.getG_total());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPhoto(i);
            }
        });


        //判断是什么模式
        if (state == 1) {

            holder.layout.setVisibility(View.GONE);
            holder.note.setVisibility(View.VISIBLE);

        } else {

            holder.layout.setVisibility(View.VISIBLE);
            holder.note.setVisibility(View.GONE);

        }

        holder.stock_layout.setTag(sdb);//设置标签
        //记录颜色
        if (sdb.getG_color().equals("1")) {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button1));

        } else if (sdb.getG_color().equals("2")) {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button2));

        } else if (sdb.getG_color().equals("3")) {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button3));

        } else if (sdb.getG_color().equals("4")) {
            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button4));

        } else if (sdb.getG_color().equals("5")) {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.grey_200));

        } else {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.grey_200));

        }


        return view;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void update(int index, ListView listview, int color) {

        //得到第一个可见item项的位置
        int visiblePosition = listview.getFirstVisiblePosition();
        //得到指定位置的视图，对listview的缓存机制不清楚的可以去了解下
        View view = listview.getChildAt(index - visiblePosition);
        ViewManHolder holder = (ViewManHolder) view.getTag();
        holder.stock_layout = view.findViewById(R.id.stock_layout);
        setData(holder, index, color);

    }

    private void setData(ViewManHolder holder, int index, int color) {

        holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(color));

    }


    public abstract class SimpeTextWather implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }


    class ViewManHolder {

        TextView textView1, textView2, textView3, textView4;
        EditText note;
        LinearLayout layout, stock_layout;

        public ViewManHolder(View itemView) {

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            note = itemView.findViewById(R.id.note);
            layout = itemView.findViewById(R.id.title_name);
            stock_layout = itemView.findViewById(R.id.stock_layout);


        }
    }


    private TextView textView1, textView2, textView3, textView4, textView5, textView, text_yuan, goods_name, pay_text;
    private RelativeLayout rl_layout1, rl_layout2, rl_layout3, rl_layout4, rl_layout5;
    private String s_variable = "";
    private int L_STATE = 0;//点击哪个布局的状态，0为没有选择，1为点击件数，2为点击单价，3位点击重量，4为点击行费，5位点击押金
    private Dialog modify_dialogs;//弹出框
    private Dialog modify_dialog;//弹出框


    public void showPhoto(final int pos) {

        final StockManagementDetailsBean sb = lists.get(pos);
        View view;//选择性别的view

        modify_dialogs = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        view = LayoutInflater.from(mContext).inflate(R.layout.layout2, null);
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
        modify_dialogs.setCanceledOnTouchOutside(false);
        modify_dialogs.show();//显示对话框

        pay_text = view.findViewById(R.id.pay_text);
        if (sb.getG_payment_mode().equals("1")) {
            pay_text.setText("现金");
        } else if (sb.getG_payment_mode().equals("2")) {
            pay_text.setText("银行卡");
        } else if (sb.getG_payment_mode().equals("3")) {
            pay_text.setText("微信");
        } else if (sb.getG_payment_mode().equals("4")) {
            pay_text.setText("支付宝");
        } else if (sb.getG_payment_mode().equals("5")) {
            pay_text.setText("赊账");
        } else if (sb.getG_payment_mode().equals("6")) {
            pay_text.setText("其他");
        }
        pay_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog(pos);
            }
        });
        goods_name = view.findViewById(R.id.goods_name);
        goods_name.setText("采购：" + sb.getG_name());
        textView1 = view.findViewById(R.id.textView1);
        textView1.setText(sb.getG_nb());
        textView2 = view.findViewById(R.id.textView2);
        textView2.setText(sb.getG_price());
        textView3 = view.findViewById(R.id.textView3);
        textView3.setText(sb.getG_weight());
        textView4 = view.findViewById(R.id.textView4);
        textView4.setText(sb.getG_the_fare());
        textView5 = view.findViewById(R.id.textView5);
        textView5.setText(sb.getG_the_deposit());
        textView = view.findViewById(R.id.textView);
        textView.setText(sb.getG_total());
        text_yuan = view.findViewById(R.id.text_yuan);
        rl_layout1 = view.findViewById(R.id.rl_layout1);
        rl_layout2 = view.findViewById(R.id.rl_layout2);
        rl_layout3 = view.findViewById(R.id.rl_layout3);
        rl_layout4 = view.findViewById(R.id.rl_layout4);
        rl_layout5 = view.findViewById(R.id.rl_layout5);

        rl_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 1;
                s_variable = "";
//                if (!textView1.getText().toString().equals("0")) {
//
//                    s_variable = textView1.getText().toString();
//
//                } else {
//
//                    s_variable = "";
//
//                }
                rl_layout1.setBackgroundColor(mContext.getResources().getColor(R.color.tou));
                rl_layout2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout4.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout5.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            }
        });

        rl_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 2;
                s_variable = "";
//                if (!textView2.getText().toString().equals("0")) {
//                    s_variable = textView2.getText().toString();
//                } else {
//                    s_variable = "";
//                }
                rl_layout1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(mContext.getResources().getColor(R.color.tou));
                rl_layout3.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout4.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout5.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            }
        });

        rl_layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 3;

                s_variable = "";

//                if (!textView3.getText().toString().equals("0")) {
//
//                    s_variable = textView3.getText().toString();
//
//                } else {
//                    s_variable = "";
//                }

                rl_layout1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(mContext.getResources().getColor(R.color.tou));
                rl_layout4.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout5.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            }
        });

        rl_layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 4;
                s_variable = "";
//                if (!textView4.getText().toString().equals("0")) {
//                    s_variable = textView4.getText().toString();
//                } else {
//                    s_variable = "";
//                }
                rl_layout1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout4.setBackgroundColor(mContext.getResources().getColor(R.color.tou));
                rl_layout5.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            }
        });
        rl_layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 5;
                s_variable = "";
//                if (!textView5.getText().toString().equals("0")) {
//                    s_variable = textView5.getText().toString();
//                } else {
//                    s_variable = "";
//                }
                rl_layout1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout4.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                rl_layout5.setBackgroundColor(mContext.getResources().getColor(R.color.tou));

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
                Log.e("点击了", "点击了");
            }
        });

        //点
        TextView btn_price_point = view.findViewById(R.id.btn_price_point);
        btn_price_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("点击了", "点击了");

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

                Log.e("现在值s_variable","："+s_variable);

                if (!s_variable.equals("") && !s_variable.equals("0")) {

                    Log.e("现在值s_variable","：事实上");
                    s_variable = s_variable.substring(0, s_variable.length() - 1);
                    if (!s_variable.equals("")) {

                        ToCalculate();

                    }else{

                        s_variable = "";
                        if (L_STATE == 1) {

                            textView1.setText("0");

                        } else if (L_STATE == 2) {

                            textView2.setText("0");

                        } else if (L_STATE == 3) {

                            textView3.setText("0");

                        } else if (L_STATE == 4) {

                            textView4.setText("0");

                        } else if (L_STATE == 5) {

                            textView5.setText("0");

                        }

                        ToCalculate();
                    }

                }else{

                    s_variable = "";
                    Log.e("现在值s_variable","：反反复复"+s_variable);
                    if (L_STATE == 1) {

                        textView1.setText("0");

                    } else if (L_STATE == 2) {

                        textView2.setText("0");

                    } else if (L_STATE == 3) {

                        textView3.setText("0");

                    } else if (L_STATE == 4) {

                        textView4.setText("0");

                    } else if (L_STATE == 5) {

                        textView5.setText("0");

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

                sb.setG_weight(textView3.getText().toString());
                sb.setG_nb(textView1.getText().toString());
                sb.setG_price(textView2.getText().toString());
                sb.setG_total(textView.getText().toString());
                sb.setG_the_deposit(textView5.getText().toString());
                sb.setG_the_fare(textView4.getText().toString());
                sb.setUnita(I_STATE + "");

                lists.set(pos, sb);
                ou.setUploadState();

                //初始化值
                s_variable = "";
                I_STATE = 1;
                L_STATE = 0;

                modify_dialogs.dismiss();

            }

        });


        final Button button_jin = view.findViewById(R.id.button_jin);
        final Button button_jian = view.findViewById(R.id.button_jian);

        //判断是否是重量或者件数
        if (sb.getUnita().equals("1") || sb.getUnita().equals(null)) {

            button_jin.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg));
            button_jian.setBackgroundColor(mContext.getResources().getColor(R.color.tou));
            I_STATE = 1;

        } else {

            button_jin.setBackgroundColor(mContext.getResources().getColor(R.color.tou));
            button_jian.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg));
            I_STATE = 2;

        }

        button_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_jin.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg));
                button_jian.setBackgroundColor(mContext.getResources().getColor(R.color.tou));
                I_STATE = 1;
                float t1 = Float.parseFloat(TextUtils.isEmpty(textView1.getText().toString())? "0":textView1.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString())? "0":textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString())? "0":textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString())? "0":textView5.getText().toString());
                float total = t1 * t2 + t4 + t5;
                textView.setText(total + "");
                text_yuan.setText("元/件");
            }
        });
        button_jin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_jin.setBackgroundColor(mContext.getResources().getColor(R.color.tou));
                button_jian.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg));
                I_STATE = 2;
                float t3 = Float.parseFloat(TextUtils.isEmpty(textView3.getText().toString())? "0":textView3.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString())? "0":textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString())? "0":textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString())? "0":textView5.getText().toString());
                float total = t3 * t2 + t4 + t5;
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
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString()) ? "0" : textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString()) ? "0" : textView5.getText().toString());
                float total = t1 * t2 + t4 + t5;
                textView.setText(total + "");

            }

        } else if (L_STATE == 2) {

            textView2.setText(TextUtils.isEmpty(s_variable) ? "0" : s_variable);
            if (I_STATE == 1) {

                float t1 = Float.parseFloat(TextUtils.isEmpty(textView1.getText().toString()) ? "0" : textView1.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString()) ? "0" : textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString()) ? "0" : textView5.getText().toString());
                float total = t1 * t2 + t4 + t5;
                textView.setText(total + "");

            } else {

                float t3 = Float.parseFloat(TextUtils.isEmpty(textView3.getText().toString()) ? "0" : textView3.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString()) ? "0" : textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString()) ? "0" : textView5.getText().toString());
                float total = t3 * t2 + t4 + t5;
                textView.setText(total + "");

            }


        } else if (L_STATE == 3) {

            textView3.setText(TextUtils.isEmpty(s_variable) ? "0" : s_variable);
            if (I_STATE == 2) {

                float t3 = Float.parseFloat(TextUtils.isEmpty(textView3.getText().toString()) ? "0" : textView3.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString()) ? "0" : textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString()) ? "0" : textView5.getText().toString());
                float total = t3 * t2 + t4 + t5;
                textView.setText(total + "");

            }


        } else if (L_STATE == 4) {

            textView4.setText(TextUtils.isEmpty(s_variable) ? "0" : s_variable);
            if (I_STATE == 1) {

                float t1 = Float.parseFloat(TextUtils.isEmpty(textView1.getText().toString()) ? "0" : textView1.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString()) ? "0" : textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString()) ? "0" : textView5.getText().toString());
                float total = t1 * t2 + t4 + t5;
                textView.setText(total + "");

            } else {

                float t3 = Float.parseFloat(TextUtils.isEmpty(textView3.getText().toString()) ? "0" : textView3.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString()) ? "0" : textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString()) ? "0" : textView5.getText().toString());
                float total = t3 * t2 + t4 + t5;
                textView.setText(total + "");

            }


        } else if (L_STATE == 5) {

            textView5.setText(TextUtils.isEmpty(s_variable) ? "0" : s_variable);
            if (I_STATE == 1) {

                float t1 = Float.parseFloat(TextUtils.isEmpty(textView1.getText().toString()) ? "0" : textView1.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString()) ? "0" : textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString()) ? "0" : textView5.getText().toString());
                float total = t1 * t2 + t4 + t5;
                textView.setText(total + "");

            } else {

                float t3 = Float.parseFloat(TextUtils.isEmpty(textView3.getText().toString()) ? "0" : textView3.getText().toString());
                float t2 = Float.parseFloat(TextUtils.isEmpty(textView2.getText().toString()) ? "0" : textView2.getText().toString());
                float t4 = Float.parseFloat(TextUtils.isEmpty(textView4.getText().toString()) ? "0" : textView4.getText().toString());
                float t5 = Float.parseFloat(TextUtils.isEmpty(textView5.getText().toString()) ? "0" : textView5.getText().toString());
                float total = t3 * t2 + t4 + t5;
                textView.setText(total + "");

            }


        }


    }

    private int PAY_STATE = 1;//支付方式，默认是现金，1为现金，2为银行卡，3为微信，4为支付宝，5为赊账，6为其他
    private Button pay_button1, pay_button2, pay_button3, pay_button4, pay_button5, pay_button6;

    //弹出支付方式
    public void payDialog(final int pos) {

        final StockManagementDetailsBean sb = lists.get(pos);

        View view;//选择性别的view

        modify_dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        view = LayoutInflater.from(mContext).inflate(R.layout.pay_dialog, null);
        //将布局设置给Dialog
        modify_dialog.setContentView(view);
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
        modify_dialog.setCanceledOnTouchOutside(true);
        modify_dialog.show();//显示对话框

        pay_button1 = view.findViewById(R.id.pay_button1);
        pay_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sb.setG_payment_mode("1");
                pay_text.setText("现金");
                modify_dialog.dismiss();

            }
        });

        pay_button2 = view.findViewById(R.id.pay_button2);
        pay_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb.setG_payment_mode("2");
                pay_text.setText("银行卡");
                modify_dialog.dismiss();
            }
        });

        pay_button3 = view.findViewById(R.id.pay_button3);
        pay_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb.setG_payment_mode("3");
                pay_text.setText("微信");
                modify_dialog.dismiss();
            }
        });

        pay_button4 = view.findViewById(R.id.pay_button4);
        pay_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sb.setG_payment_mode("4");
                pay_text.setText("支付宝");
                modify_dialog.dismiss();
            }
        });


        pay_button5 = view.findViewById(R.id.pay_button5);
        pay_button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb.setG_payment_mode("5");
                pay_text.setText("赊账");
                modify_dialog.dismiss();
            }
        });


        pay_button6 = view.findViewById(R.id.pay_button6);
        pay_button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb.setG_payment_mode("6");
                pay_text.setText("其他");
                modify_dialog.dismiss();
            }
        });


    }


    public OnClickUpload ou;

    public void setOnClickUpload(OnClickUpload ou) {
        this.ou = ou;
    }

    public interface OnClickUpload {

        void setUploadState();//修改上传状态

    }

}
