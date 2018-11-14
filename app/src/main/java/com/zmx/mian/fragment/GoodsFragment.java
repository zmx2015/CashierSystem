package com.zmx.mian.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

import com.zmx.mian.R;
import com.zmx.mian.adapter.ShopAdapter;
import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean_dao.CPDao;
import com.zmx.mian.bean_dao.goodsDao;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.AddGoodsActivity;
import com.zmx.mian.ui.CPManagementActivity;
import com.zmx.mian.ui.SearchGoodsActivity;
import com.zmx.mian.ui.util.LoadingDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IGoodsView;

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
 * 开发时间：2018-06-25 1:54
 * 类功能：商品管理
 */

public class GoodsFragment extends Fragment{

    private TextView toolsTextViews[];
    private View views[];
    private LayoutInflater inflater;
    private ScrollView scrollView;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private ViewPager shop_pager;
    private int currentItem=0;
    private ShopAdapter shopAdapter;
    private ImageView type_icon;
    private Button again_load;
    private RelativeLayout search_btn,cp_management;

    private List<CommodityPosition> cp;
    private String mid;

    private goodsDao gd;
    private CPDao dao;


    protected LoadingDialog mLoadingDialog; //显示正在加载的对话框

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods,container,false);

        again_load = view.findViewById(R.id.again_load);
        again_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", mid);
                OkHttp3ClientManager.getInstance().getStringExecute("http://www.yiyuangy.com/admin/api.line/goods", params, h, 2, 404);

            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        again_load.setVisibility(View.GONE);
        showLoadingView("加载中.....");
        gd = new goodsDao();
        dao = new CPDao();
        cp = new ArrayList<>();

        cp_management = view.findViewById(R.id.cp_management);
        cp_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(GoodsFragment.this.getContext(),CPManagementActivity.class);
                GoodsFragment.this.getContext().startActivity(intent);

            }
        });

        mid = MySharedPreferences.getInstance(this.getActivity()).getString(MySharedPreferences.store_id,"");
        Map<String, String> params = new HashMap<String, String>();
        params.put("mid", mid);

        OkHttp3ClientManager.getInstance().getStringExecute("http://www.yiyuangy.com/admin/api.line/goods", params, h, 2, 404);

        //注册监听广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(AddGoodsActivity.action);
        this.getActivity().registerReceiver(broadcastReceiver, filter);

    }

    private Handler h = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    dismissLoadingView();
                    again_load.setVisibility(View.GONE);

                    if(shopAdapter != null){

                        shopAdapter.notifyDataSetChanged();

                    }else{

                        initPager();
                        showToolsView();

                    }

                    break;

                case 2:
                    getGoodsList(msg.obj.toString());
                    break;

                case 404:

                    dismissLoadingView();
                    Toast.makeText(GoodsFragment.this.getActivity(),"连接网络失败，请检查网络！",Toast.LENGTH_LONG).show();
                    again_load.setVisibility(View.VISIBLE);

                    break;

            }

        }
    };

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView() {

         LinearLayout toolsLayout=(LinearLayout) getActivity().findViewById(R.id.tools);
        toolsTextViews=new TextView[cp.size()];
        views=new View[cp.size()];

        for (int i = 0; i < cp.size(); i++) {

            View view=inflater.inflate(R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView=view.findViewById(R.id.text);
            textView.setText(cp.get(i).getName());
            toolsLayout.addView(view);
            toolsTextViews[i]=textView;
            views[i]=view;

        }
        changeTextColor(0);
    }

    private View.OnClickListener toolsItemListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_pager.setCurrentItem(v.getId());
        }
    };



    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {

        scrollView = getActivity().findViewById(R.id.tools_scrlllview);
        shopAdapter = new ShopAdapter(getFragmentManager(),cp);
        inflater = LayoutInflater.from(getActivity());
        shop_pager= getActivity().findViewById(R.id.goods_pager);
        shop_pager.setAdapter(shopAdapter);
        shop_pager.setOnPageChangeListener(onPageChangeListener);

        type_icon = getActivity().findViewById(R.id.type_icon);
        type_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(GoodsFragment.this.getContext(), AddGoodsActivity.class);
                intent.putExtra("cp", (Serializable) cp);
                startActivity(intent);

            }
        });

        search_btn = getActivity().findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(GoodsFragment.this.getContext(), SearchGoodsActivity.class);
//                intent.putExtra("cp", (Serializable) cp);
                startActivity(intent);

            }
        });

    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if(shop_pager.getCurrentItem()!=arg0)shop_pager.setCurrentItem(arg0);
            if(currentItem!=arg0){
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem=arg0;
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
            // 更新类目
            JSONObject jj = json.getJSONObject("info");
            String arr = jj.toString();
          Log.e("json","json"+json.toString());
            String[] stringArr = arr.split(",");

            //获得类目下的商品
            JSONObject data = json.getJSONObject("data");

            List<Goods> gs = new ArrayList<>();

            for (int i = 0; i < stringArr.length; i++) {

                CommodityPositionGD cpGD = new CommodityPositionGD();
                CommodityPosition c = new CommodityPosition();


                String str = stringArr[i];
                String s_type;
                str = str.replace("{", "");
                str = str.replace("}", "");
                String ts = str;
                str = str.substring(str.indexOf(":") + 1);
                s_type = ts.substring(0,ts.indexOf(":"));
                str = str.replace("\"", "");
                s_type = s_type.replace("\"", "");

                Log.e("类型·",""+s_type);

                c.setType(s_type);
                c.setName(str);

                String type_id = s_type.substring(s_type.indexOf("-") + 1);
                //保存到本地
                cpGD.setId(Long.parseLong(type_id));
                cpGD.setName(str);
                cpGD.setType(s_type);
                long l = dao.insertCp(cpGD);//保存到本地
                Log.e("保存本地状态:"+s_type,"str:"+str+" 保存状态："+l);

                JSONArray array = data.getJSONArray(s_type);
                for (int z = 0; z < array.length(); z++) {

                    JSONObject ty = array.getJSONObject(z);
                    Goods g = new Goods (ty.getInt("gid") + "",
                            ty.getString("img"), ty.getString("gds_price"),
                            ty.getString("name"), ty.getString("gname"),
                            ty.getInt("group") + "",ty.getString("vip_price"),ty.getString("mall_state"),ty.getString("store_state"));

                    gs.add(g);
                    gd.insertCp(g);//保存到本地

                }

                c.setList(gs);

                cp.add(c);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        h.sendEmptyMessage(1);
    }


    /**
     * 改变textView的颜色
     * @param id
     */
    private void changeTextColor(int id) {

        for (int i = 0; i < toolsTextViews.length; i++) {

            if(i!=id){
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff000000);
            }

        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(this.getActivity().getResources().getColor(R.color.tou));
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

    //注册广播监听是否登录
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //处理不同的广播
            //登录更新用户信息
            if (intent.getAction().equals(AddGoodsActivity.action)) {


            }else if(intent.getAction().equals(Fragment_pro_type.action)){

                Log.e("接收", "接收到的广播：修改成功商品" );

            }


        }
    };

    /**
     * 设置加载提示框
     */
    protected void showLoadingView(String message){

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this.getActivity(),message, false);
        }
        mLoadingDialog.show();

    }
    /**
     * 数据加载完成
     */
    protected void dismissLoadingView(){

        if (mLoadingDialog != null) {
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });

        }

    }

}
