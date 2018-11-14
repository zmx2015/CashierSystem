package com.zmx.mian.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmx.mian.R;
import com.zmx.mian.adapter.TabFragmentAdapter;
import com.zmx.mian.fragment.cp.CPOnlineFragment;
import com.zmx.mian.fragment.cp.CPStoresFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-14 17:28
 * 类功能：商品列表管理
 */
public class CPManagementActivity extends BaseActivity {

    private List<String> mPageTitleList = new ArrayList<String>();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<Integer> mBadgeCountList = new ArrayList<Integer>();
    private TabFragmentAdapter mPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private CPOnlineFragment sof;//商城类别的fragment
    private CPStoresFragment oof;//门店类别的fragment

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cpmanagement;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);
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

        mPagerAdapter = new TabFragmentAdapter(mActivity,getSupportFragmentManager(),
                mFragmentList, mPageTitleList, mBadgeCountList);
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

}
