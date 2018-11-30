package com.zmx.mian.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.SearchGoodsAdapter;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.bean_dao.goodsDao;
import com.zmx.mian.fragment.Fragment_pro_type;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.util.MyDialog;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-10 14:56
 * 类功能：查找某个商品
 */
public class SearchGoodsActivity extends BaseActivity {

    private List<Goods> lists = new ArrayList<>();
    private SearchGoodsAdapter adapter;
    private RecyclerView rv;
    private PtrClassicFrameLayout mPtrFrame;
    private RecyclerAdapterWithHF mAdapter;
    private EditText et;
    private ImageView no_data;

    private ImageView saomiao;
    private final static int REQ_CODE = 1028;

    private String state;//判断是否是哪个界面进来的


    private List<ViceOrder> vos = new ArrayList<>();//放进购物车的商品

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_goods;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        rv = findViewById(R.id.search_view);
        no_data = findViewById(R.id.no_data);
        state = getIntent().getStringExtra("state");
        adapter = new SearchGoodsAdapter(this, lists);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rv.setAdapter(mAdapter);
        rv.setNestedScrollingEnabled(false);

        saomiao = findViewById(R.id.saomiao);
        saomiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPoto();
            }
        });

        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                // 通过Intent传递对象给Service
                //如果是1的话就跳到详情去
                if (state.equals("1")) {

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("gid", lists.get(position).getG_id());
                    intent.setClass(mActivity, GoodsDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else {

                    showPhoto(lists.get(position));

                }


            }
        });

        mPtrFrame = findViewById(R.id.rotate_header_list_view_frame);
//下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
//下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
// default is false
        mPtrFrame.setPullToRefresh(false);
// default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return false;
            }
        });

        et = findViewById(R.id.goods_search_edit);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                lists.clear();
                //查询更新
                if (et.getText().toString() != null || !et.getText().toString().equals("")) {


                    adapter.notifyDataSetChanged();
                    searchGoods(et.getText().toString());

                }


            }
        });

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        if (jsonObject.getString("code").equals("1")) {

                            lists.clear();
                            no_data.setVisibility(View.GONE);

                            JSONArray jsonArray = jsonObject.getJSONArray("list");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject j = jsonArray.getJSONObject(i);

                                Goods g = new Goods();
                                g.setG_id(j.getString("gid"));
                                g.setG_img(j.getString("img"));
                                g.setG_name(j.getString("name"));
                                g.setG_price(j.getString("gds_price"));
                                lists.add(g);

                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            no_data.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

            }

        }
    };


    //搜索商品
    public void searchGoods(String name) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("name", name);

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/search", params, handler, 1, 404);


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            // 扫描二维码/条码回传
            if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
                if (data != null) {

                    String numbers = data.getStringExtra(Constant.CODED_CONTENT);
                    searchGoods(numbers);
                }
            }


        }
    }

    public void startPoto() {

        Intent intent = new Intent(this, CaptureActivity.class);

        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为淡蓝色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQ_CODE);

    }


    private TextView textView1, textView2, textView3, textView, text_yuan, goods_name;
    private RelativeLayout rl_layout1, rl_layout2, rl_layout3;
    private String s_variable = "";
    private int L_STATE = 0;//点击哪个布局的状态，0为没有选择，1为点击件数，2为点击单价，3位点击重量，4为点击行费，5位点击押金
    private Dialog modify_dialogs;//弹出框
    private int I_STATE = 1;//斤和件，2为斤，1位件，默认为件

    public void showPhoto(final Goods g) {

        View view;//选择性别的view

        modify_dialogs = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_shopping, null);
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
                rl_layout1.setBackgroundColor(mActivity.getResources().getColor(R.color.tou));
                rl_layout2.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
            }
        });

        rl_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 2;
                s_variable = "";
                rl_layout1.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(mActivity.getResources().getColor(R.color.tou));
                rl_layout3.setBackgroundColor(mActivity.getResources().getColor(R.color.white));

            }
        });

        rl_layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L_STATE = 3;

                s_variable = "";
                rl_layout1.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                rl_layout2.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                rl_layout3.setBackgroundColor(mActivity.getResources().getColor(R.color.tou));

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
                    vos.add(vo);//添加进去

                    //初始化值
                    s_variable = "";
                    I_STATE = 1;
                    L_STATE = 0;
                    Toast("加入购物车成功！");
                    modify_dialogs.dismiss();

                }else{

                    Toast("商品总价不能为零！");

                }


            }

        });


        final Button button_jin = view.findViewById(R.id.button_jin);
        final Button button_jian = view.findViewById(R.id.button_jian);
        button_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_jin.setBackgroundColor(mActivity.getResources().getColor(R.color.main_bg));
                button_jian.setBackgroundColor(mActivity.getResources().getColor(R.color.tou));
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

                button_jin.setBackgroundColor(mActivity.getResources().getColor(R.color.tou));
                button_jian.setBackgroundColor(mActivity.getResources().getColor(R.color.main_bg));
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 设置返回数据
            Bundle bundle = new Bundle();
            bundle.putSerializable("vos", (Serializable) vos);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            // 返回intent
            setResult(RESULT_OK, intent);
            mActivity.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }



}
