package com.zmx.mian.ui;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.adapter.TabFragmentAdapter;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean_dao.CPDao;
import com.zmx.mian.fragment.cp.CPOnlineFragment;
import com.zmx.mian.fragment.cp.CPStoresFragment;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.http.UrlConfig;
import com.zmx.mian.ui.util.MyDialog;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-14 17:28
 * 类功能：商品列表管理
 */
public class CPManagementActivity extends BaseActivity {

    private Button add_cp;
    private List<String> mPageTitleList = new ArrayList<String>();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<Integer> mBadgeCountList = new ArrayList<Integer>();
    private TabFragmentAdapter mPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private CPOnlineFragment sof;//商城类别的fragment
    private CPStoresFragment oof;//门店类别的fragment

    private int state = 0;//默认为零，1为收银分类，2为商城分类
    private CPDao cpDao;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cpmanagement;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        cpDao = new CPDao();
        add_cp = findViewById(R.id.add_cp);
        add_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();

            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mPageTitleList.add("门店分类");
        mPageTitleList.add("商城分类");
        mBadgeCountList.add(0);
        mBadgeCountList.add(0);
        // 初始化对应的fragment
        oof = CPStoresFragment.getInstance(mPageTitleList.get(0));
        sof = CPOnlineFragment.getInstance(mPageTitleList.get(1));
        mFragmentList.add(oof);
        mFragmentList.add(sof);

        mPagerAdapter = new TabFragmentAdapter(mActivity, getSupportFragmentManager(),
                mFragmentList, mPageTitleList, mBadgeCountList);
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
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

                            Toast(jsonObject.getString("content"));

                            if (state == 1) {
                                //通知更新
                                oof.notifyData();
                            }

                        } else {

                            Toast(jsonObject.getString("content"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

                case 2:

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        if (jsonObject.getString("code").equals("1")) {

                            Toast(jsonObject.getString("content"));
                            if (state == 2) {
                                //通知更新
                                sof.notifyData();
                            }

                        } else {

                            Toast(jsonObject.getString("content"));                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

                case 404:

                    Toast("添加失败");

                    break;

            }

        }
    };


    //    弹出框
    private MyDialog mMyDialog;
    private EditText edit_name;
    private Button submit, cancel;
    private CheckBox cb;
    private RadioGroup radioGroup;

    public void showDialog() {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_add_cp, null);
        mMyDialog = new MyDialog(mActivity, 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);
        mMyDialog.show();

        edit_name = view.findViewById(R.id.edit_name);
        submit = view.findViewById(R.id.submit);
        cancel = view.findViewById(R.id.cancel);
        cb = view.findViewById(R.id.store_sj);
        radioGroup = view.findViewById(R.id.rg);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb1) {

                    state = 1;
                }
                if (checkedId == R.id.rb2) {

                    state = 2;

                }

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //收银
                if (state == 1) {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
                    params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
                    params.put("pckey", new Tools().getKey(mActivity));
                    params.put("account", "0");
                    params.put("id", "0");
                    params.put("execute", "insert");
                    params.put("gname", edit_name.getText().toString());
                    params.put("sort", "0");


                    if (cb.isChecked()) {

                        params.put("state", "1");

                    } else {

                        params.put("state", "2");

                    }

                    OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.UPDATE_CP, params, handler, 1, 404);
                    mMyDialog.dismiss();

                } else if (state == 2) {


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
                    params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
                    params.put("pckey", new Tools().getKey(mActivity));
                    params.put("account", "0");
                    params.put("id", "0");
                    params.put("execute", "insert");
                    params.put("tname", edit_name.getText().toString());
                    params.put("sort", "0");

                    if (cb.isChecked()) {

                        params.put("state", "1");

                    } else {

                        params.put("state", "2");

                    }

                    OkHttp3ClientManager.getInstance().NetworkRequestMode(UrlConfig.UPDATE_MALL, params, handler, 2, 404);
                    mMyDialog.dismiss();

                } else {
                    Toast("请选择要创建商城或者收银的分类");
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


}
