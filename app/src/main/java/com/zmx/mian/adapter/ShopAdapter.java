package com.zmx.mian.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.fragment.Fragment_pro_type;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-07-31 21:51
 * 类功能：
 */

public class ShopAdapter extends FragmentPagerAdapter {

    List<CommodityPosition> cp ;
    public ShopAdapter(FragmentManager fm, List<CommodityPosition> cp) {
        super(fm);
        this.cp = cp;
        Log.e("进来了","初始化商品界面");
    }
    @Override
    public Fragment getItem(int arg0) {

        Fragment_pro_type fragment =new Fragment_pro_type();
        fragment.setData(cp.get(arg0).getList(),cp.get(arg0).getName(),cp);
        Bundle bundle=new Bundle();
        String str=cp.get(arg0).getName();
        bundle.putString("typename",str);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return cp.size();
    }
}

