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


public class Fragment_pro_type extends Fragment{

    private goodsDao gdao;
    private List<Goods> gs = new ArrayList<>();
    private GridView listView;
    private Pro_type_adapter adapter;
    private String typename;

    private List<CommodityPosition> cp;
    private List<String> spinner_item;

    public static final String action = "updateGoods";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro_type, null);

        gdao = new goodsDao();
        listView = view.findViewById(R.id.listView);
        typename = getArguments().getString("typename");
        ((TextView) view.findViewById(R.id.toptype)).setText(typename);
        adapter = new Pro_type_adapter(getActivity(), gs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


                // 通过Intent传递对象给Service
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods", gs.get(arg2));
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

                    //通知界面更新
                    Intent intent = new Intent(action);
                    Fragment_pro_type.this.getActivity().sendBroadcast(intent);
                    break;
            }

        }
    };

    public void setData(List<Goods> gss, String typenames, List<CommodityPosition> cps) {

        spinner_item = new ArrayList<>();
        this.cp = cps;

        for (CommodityPosition p : cp) {

            spinner_item.add(p.getName());

        }

        for (Goods g : gss) {
            if (g.getCp_name().equals(typenames)) {
                gs.add(g);
            }
        }

        h.sendEmptyMessage(1);

    }

}
