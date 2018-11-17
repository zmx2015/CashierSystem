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

import com.zmx.mian.MyApplication;
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


public class Fragment_pro_type extends ViewPagerFragment {

    private List<Goods> gs = new ArrayList<>();
    private GridView listView;
    private Pro_type_adapter adapter;

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

//    /**
//     * 创建新实例
//     *
//     * @param index
//     * @return
//     */
//    public static Fragment_pro_type newInstance(int index, String id,String t_name) {
//        Bundle bundle = new Bundle();
//        bundle.putInt(FRAGMENT_INDEX, index);
//        bundle.putString("CID", id);
//        bundle.putString("t_name", t_name);
//        Fragment_pro_type fragment = new Fragment_pro_type();
//        fragment.setArguments(bundle);
//        return fragment;
//    }

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
        adapter = new Pro_type_adapter(getActivity(), gs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                // 通过Intent传递对象给Service
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("gid",gs.get(arg2).getG_id());
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

                    Log.e("返回的数据", "放回" + msg.obj.toString());

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
                                        ty.getString("store_state"));
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

    public NoticeDismissLoadingView ndv;
    //通知activity关闭加载提示框
    public interface NoticeDismissLoadingView{

        void dismissLoading();
    }
    public void setDismissLoadingView(NoticeDismissLoadingView ndv){
        this.ndv = ndv;
    }

}
