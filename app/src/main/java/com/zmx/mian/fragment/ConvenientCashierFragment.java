package com.zmx.mian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.ConvenientCashierAdapter;
import com.zmx.mian.adapter.Pro_type_adapter;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.GoodsDetailsActivity;
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
 * 开发时间：2018-11-18 14:25
 * 类功能：前台收银的商品界面
 */
public class ConvenientCashierFragment extends ViewPagerFragment implements ConvenientCashierAdapter.FoodActionCallback {

    private List<Goods> gs = new ArrayList<>();
    private GridView listView;
    private ConvenientCashierAdapter adapter;

    private String cid;//分类id
    private TextView typename;
    private String t_name;

    private ImageView no_data;


    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private View view;
    private static final String FRAGMENT_INDEX = "fragment_index";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_pro_type, null);
            Bundle bundle = getArguments();
            if (bundle != null) {
                cid = bundle.getString("CID");
                t_name = bundle.getString("t_name");
            }
            isPrepared = true;
            lazyLoad();

        }

        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        listView = view.findViewById(R.id.listView);
        typename =  view.findViewById(R.id.toptype);
        typename.setText(t_name);
        no_data = view.findViewById(R.id.no_data);
        adapter = new ConvenientCashierAdapter(getActivity(), gs,h,this);
        listView.setAdapter(adapter);

        return view;
    }

    private Handler h = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    Log.e("返回的数据", "放回" + msg.obj.toString());
                    gs.clear();
                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        //再判断有没有商品
                        if (jsonObject.getString("code").equals("1")) {

                            JSONArray j_data = jsonObject.getJSONArray("list");

                            for (int z = 0; z < j_data.length(); z++) {

                                JSONObject ty = j_data.getJSONObject(z);

                                Goods g = new Goods(ty.getInt("gid") + "",
                                        ty.getString("img"), ty.getString("gds_price"),
                                        ty.getString("name"), "",
                                        ty.getInt("group") + "",
                                        ty.getString("vip_price"),
                                        ty.getString("mall_state"),
                                        ty.getString("store_state"),"");
                                gs.add(g);
                            }
                            ndv.dismissLoading();
                            adapter.notifyDataSetChanged();

                        }else{

                            no_data.setVisibility(View.VISIBLE);
                            ndv.dismissLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.e("解析错误",""+e.toString());

                    }


                    break;

                case 0:
                    LoadData();
                    break;

                case 4:
                    //接收选择的商品
                    ViceOrder vo = (ViceOrder) msg.getData().getSerializable("vo");

                    Log.e("进来了","进来了"+vo.getName());
                    ndv.setViceOrder(vo);

                    break;
                case 404:

                    ndv.dismissLoading();
                    Toast.makeText(ConvenientCashierFragment.this.getContext(),"加载数据失败！",Toast.LENGTH_LONG);

                    break;
            }

        }
    };


    @Override
    protected void lazyLoad() {


        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        h.sendEmptyMessage(0);
    }


    public void LoadData() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("group", cid);
        params.put("admin", MyApplication.getName());
        params.put("mid", MyApplication.getStore_id());
        params.put("pckey", new Tools().getKey(this.getActivity()));
        params.put("account", "0");
        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/goodsList", params, h, 1, 404);

    }

    public ConvenientCashierFragment.NoticeDismissLoadingView ndv;

    @Override
    public void addAction(View view, int position) {
        ndv.addAction(view,position);
    }

    //通知activity关闭加载提示框
    public interface NoticeDismissLoadingView{

        void dismissLoading();
        void setViceOrder(ViceOrder vo);
        void addAction(View view, int position);

    }
    public void setDismissLoadingView(ConvenientCashierFragment.NoticeDismissLoadingView ndv){
        this.ndv = ndv;
    }

}
