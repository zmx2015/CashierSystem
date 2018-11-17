package com.zmx.mian.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.adapter.TabFragmentAdapter;
import com.zmx.mian.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-25 1:54
 * 类功能：消息
 */

public class MessageFragment extends BaseFragment implements OnlineOrderFragment.UnreadOrderNun{

    private List<String> mPageTitleList = new ArrayList<String>();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<Integer> mBadgeCountList = new ArrayList<Integer>();
    private TabFragmentAdapter mPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private StoresOrderFragment sof;//门店订单的fragment
    private OnlineOrderFragment oof;//商城订单的fragment

    private int none_Size=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        tabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewPager);
        mPageTitleList.add("门店订单");
        mPageTitleList.add("商城订单");
        mBadgeCountList.add(0);
        mBadgeCountList.add(0);
        // 初始化对应的fragment
        sof = StoresOrderFragment.getInstance(mPageTitleList.get(0));
        oof = OnlineOrderFragment.getInstance(mPageTitleList.get(1));
        oof.setUnreadOrderNun(this);
        mFragmentList.add(sof);
        mFragmentList.add(oof);

        mPagerAdapter = new TabFragmentAdapter(mActivity,getFragmentManager(),
                mFragmentList, mPageTitleList, mBadgeCountList);
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        setUpTabBadge();

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

                    mBadgeCountList.set(1, none_Size);
                    setUpTabBadge();

                    break;

            }

        }
    };



    /**
     * 设置Tablayout上的标题的角标
     */
    private void setUpTabBadge() {

        // 2. 最实用
        for (int i = 0; i < mFragmentList.size(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);

            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            tab.setCustomView(mPagerAdapter.getTabItemView(i));
        }

        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
    }

    @Override
    public void setNumber(int num) {

        Log.e("未读数","未读数"+num);
        none_Size = num;
        handler.sendEmptyMessage(1);
    }
}
