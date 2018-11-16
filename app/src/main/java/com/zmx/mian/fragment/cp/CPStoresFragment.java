package com.zmx.mian.fragment.cp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.mian.R;
import com.zmx.mian.adapter.CPStoresAdapter;
import com.zmx.mian.adapter.StoreListAdapter;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean.StoresMessage;
import com.zmx.mian.bean_dao.CPDao;
import com.zmx.mian.fragment.BaseFragment;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.ui.MainActivity;
import com.zmx.mian.ui.StoreListActivity;
import com.zmx.mian.ui.util.MyDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-14 17:29
 * 类功能：门店的类别管理
 */
public class CPStoresFragment extends BaseFragment{

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    private RecyclerAdapterWithHF mAdapter;
    //List数据
    private List<CommodityPositionGD> lists ;
    //RecyclerView自定义Adapter
    private CPStoresAdapter adapter;
    //添加Header和Footer的封装类
    private CPDao cpDao;

    private int positions;
    private String pc_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cp_stores_fragment,container,false);

        mRecyclerView = view.findViewById(R.id.rv_list);
        mPtrFrame = view.findViewById(R.id.rotate_header_list_view_frame);
        cpDao = new CPDao();
        lists = cpDao.queryAll();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new CPStoresAdapter(mActivity, lists);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                positions = position;
                showDialog(lists.get(position));

            }
        });
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

        adapter.notifyDataSetChanged();

        return view;
    }


    @Override
    protected void initView() {

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    Log.e("返回的内容",""+msg.obj.toString());
                    try {

                        JSONObject object = new JSONObject(msg.obj.toString());
                        if(object.getString("status").equals("1")){

                            //更新本地数据
                            CommodityPositionGD cp = lists.get(positions);
                            cp.setName(pc_name);
                            cpDao.UpdateCp(cp);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(mActivity,"修改成功",Toast.LENGTH_LONG).show();

                        }else{

                            Toast.makeText(mActivity,"修改失败",Toast.LENGTH_LONG).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 404:
                    Log.e("返回的内容","失败");
                    break;

            }

        }
    };



    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";

    public static CPStoresFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        CPStoresFragment pageFragment = new CPStoresFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }



    //    弹出框
    private MyDialog mMyDialog;
    private EditText edit_name;
    private Button submit,cancel;
    private CheckBox cb;

    public void showDialog(final CommodityPositionGD cp) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_cp_stores, null);
        mMyDialog = new MyDialog(mActivity, 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);
        mMyDialog.show();

        edit_name = view.findViewById(R.id.edit_name);
        edit_name.setText(cp.getName());
        submit = view.findViewById(R.id.submit);
        cancel = view.findViewById(R.id.cancel);
        cb = view.findViewById(R.id.store_sj);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(edit_name.getText().toString())){

                    Toast.makeText(mActivity,"请输入名称",Toast.LENGTH_LONG).show();

                }else {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
                    params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
                    params.put("pckey", new Tools().getKey(mActivity));
                    params.put("account", "0");
                    params.put("execute", "update");
                    params.put("id", cp.getId() + "");
                    params.put("gname", edit_name.getText().toString());
                    pc_name = edit_name.getText().toString();
                    params.put("sort", "0");

                    if (cb.isChecked()) {

                        params.put("state", "1");

                    } else {

                        params.put("state", "0");

                    }

                    OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.goods/classpc", params, handler, 1, 404);
                    mMyDialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyDialog.dismiss();
            }
        });

    }

    //新增成功后刷新
    public void notifyData(CommodityPositionGD cp){
        Log.e("进来了","进来了");
        lists.add(cp);
        adapter.notifyDataSetChanged();

    }


}
