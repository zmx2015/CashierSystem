package com.zmx.mian.ui;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.ConvenientAdapter;
import com.zmx.mian.adapter.ShopAdapter;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.fragment.GoodsFragment;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.util.CustomScrollViewPager;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.ui.util.ShoppingCartAnimationView;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.ToastUtil;
import com.zmx.mian.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-18 13:52
 * 类功能：前台收银
 */
public class ConvenientCashierActivity extends BaseActivity implements ConvenientAdapter.ConvenientFoodActionCallback {


    private TextView toolsTextViews[],text_number,text_total;
    private View views[];
    private LayoutInflater inflater;
    private ScrollView scrollView;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private CustomScrollViewPager shop_pager;
    private int currentItem = 0;
    private ConvenientAdapter shopAdapter;
    private Button again_load,button;
    private RelativeLayout search_btn;

    private MainOrder mainOrder;
    private List<ViceOrder> vos = new ArrayList<>();
    private List<CommodityPositionGD> cp;
    private String mid;

    private ImageView no_data;//没有数据的时候显示

    private int DIS=0;//防止重复点击出现加载框


    @Override
    protected int getLayoutId() {
        return R.layout.activity_convenient_cashier;
    }

    @Override
    protected void initViews() {


        //初始化订单
        mainOrder = new MainOrder();
        initMainOrder();//初始订单的最初值

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);
        text_number = findViewById(R.id.text_number);
        text_total = findViewById(R.id.text_total);
        no_data = findViewById(R.id.no_data);
        again_load = findViewById(R.id.again_load);
        again_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", mid);
                OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.line/goods", params, h, 2, 404);

            }
        });

        search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putString("state","2");
                startActivity(SearchGoodsActivity.class,bundle,2);


            }
        });

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(vos.size()<=0){

                    Toast("没有商品,无法结算！");

                }else{

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mo",mainOrder);
                    startActivity(ConvenientCashierDetailActivity.class,bundle,1);

                }


            }
        });


        again_load.setVisibility(View.GONE);
        showLoadingView("加载中.....");
        cp = new ArrayList<>();

        mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(mActivity));
        params.put("account", "0");
        params.put("type", "store");
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.class/typeList", params, h, 2, 404);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){

            case 1:

                Bundle extras = data.getExtras();
                if (extras != null) {

                    String state = extras.getString("state");
                    if(state.equals("0")){

                        initMainOrder();
                        text_number.setText("0");
                        text_total.setText("0.00");

                    }else{

                        MainOrder m = (MainOrder) extras.getSerializable("mo");

                        mainOrder.setTotal(m.getTotal());// 订单总金额
                        mainOrder.setBackmey(m.getBackmey());// 应找金额
                        mainOrder.setDiscount(m.getDiscount());// 订单优惠的金额
                        mainOrder.setReceipts(m.getReceipts());// 订单实收金额
                        mainOrder.setMo_classify(m.getMo_classify());
                        mainOrder.setAccount(m.getAccount());
                        mainOrder.setPayment(m.getPayment());

                        vos.clear();
                        List<ViceOrder> vs = m.getLists();
                        for (ViceOrder v:vs){
                            vos.add(v);
                        }
                        mainOrder.setLists(vos);
                        text_number.setText(vos.size()+"");
                        text_total.setText(m.getTotal()+"");
                    }


                }

                break;

            case 2:
                Bundle extra = data.getExtras();
                if (extra != null) {

                    List<ViceOrder> vs = (List<ViceOrder>) extra.getSerializable("vos");
                    for (ViceOrder v:vs){
                        vos.add(v);
                    }

                    //重新计算订单的总价
                    float total = 0;
                    for (ViceOrder v : vos){
                        total = total+Float.parseFloat(v.getSubtotal());
                    }
                    mainOrder.setTotal(priceResult(total)+"");
                    text_number.setText(vos.size()+"");
                    text_total.setText(total+"");
                    mainOrder.setLists(vos);

                }

                break;

        }


    }

    private Handler h = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    dismissLoadingView();
                    again_load.setVisibility(View.GONE);

                    if (shopAdapter != null) {

                        shopAdapter.notifyDataSetChanged();

                    } else {

                        if (cp.size() > 0) {

                            no_data.setVisibility(View.GONE);
                            initPager();
                            showToolsView();

                        } else {

                            no_data.setVisibility(View.VISIBLE);
                        }


                    }

                    break;

                case 2:
                    getGoodsList(msg.obj.toString());
                    break;

                case 3:
                    dismissLoadingView();
                    break;

                case 4:

                    //接收选择的商品
                    ViceOrder vo = (ViceOrder) msg.getData().getSerializable("vo");

                    vos.add(vo);
                    //重新计算订单的总价
                    float total = 0;
                    for (ViceOrder v : vos){
                        total = total+Float.parseFloat(v.getSubtotal());
                    }
                    mainOrder.setTotal(priceResult(total)+"");
                    text_number.setText(vos.size()+"");
                    text_total.setText(total+"");
                    mainOrder.setLists(vos);

                    break;

                case 404:

                    dismissLoadingView();

                    Toast("连接网络失败，请检查网络！");
                    again_load.setVisibility(View.VISIBLE);

                    break;

            }

        }
    };

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView() {

        LinearLayout toolsLayout = findViewById(R.id.tools);
        toolsTextViews = new TextView[cp.size()];
        views = new View[cp.size()];

        for (int i = 0; i < cp.size(); i++) {

            View view = inflater.inflate(R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = view.findViewById(R.id.text);
            textView.setText(cp.get(i).getName());
            toolsLayout.addView(view);
            toolsTextViews[i] = textView;
            views[i] = view;

        }
        changeTextColor(0);
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(DIS != v.getId()){

                showLoadingView("加载中...");
                DIS = v.getId();

            }
            shop_pager.setCurrentItem(v.getId());


        }
    };


    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {

        scrollView = findViewById(R.id.tools_scrlllview);
        shopAdapter = new ConvenientAdapter(getSupportFragmentManager(), cp, h,this);
        inflater = LayoutInflater.from(mActivity);
        shop_pager = findViewById(R.id.goods_pager);
        shop_pager.setAdapter(shopAdapter);
        shop_pager.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if (shop_pager.getCurrentItem() != arg0) shop_pager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    public void getGoodsList(String goods) {

        cp.clear();
        JSONObject json = null;

        try {
            json = new JSONObject(goods);
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
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        h.sendEmptyMessage(1);
    }


    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id) {

        for (int i = 0; i < toolsTextViews.length; i++) {

            if (i != id) {
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff000000);
            }

        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(this.getResources().getColor(R.color.tou));
    }


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {

        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }

    public String priceResult(double price) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(new BigDecimal(price));

    }

    @Override
    public void addAction(View view, int position) {

        //添加到购物车的动画
        ShoppingCartAnimationView shoppingCartAnimationView = new ShoppingCartAnimationView(this);
        int positions[] = new int[2];
        view.getLocationInWindow(positions);
        shoppingCartAnimationView.setStartPosition(new Point(positions[0], positions[1]));
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        rootView.addView(shoppingCartAnimationView);
        int endPosition[] = new int[2];
        text_number.getLocationInWindow(endPosition);
        shoppingCartAnimationView.setEndPosition(new Point(endPosition[0], endPosition[1]));
        shoppingCartAnimationView.startBeizerAnimation();

    }

    public void initMainOrder(){

        mainOrder.setOrder(new Date().getTime() + "");
        mainOrder.setTotal("0");// 订单总金额
        mainOrder.setBackmey("0");;// 应找金额
        mainOrder.setDiscount("0");// 订单优惠的金额
        mainOrder.setReceipts("0");// 订单实收金额
        mainOrder.setMo_classify("4");
        mainOrder.setAccount("1");
        mainOrder.setPayment(1);
        vos.clear();

    }



}
