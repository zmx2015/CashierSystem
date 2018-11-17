package com.zmx.mian.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.fragment.Fragment_pro_type;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-07-31 21:51
 * 类功能：
 */

public class ShopAdapter extends FragmentPagerAdapter implements Fragment_pro_type.NoticeDismissLoadingView {

    List<CommodityPositionGD> cp ;
    Handler handler;
    public ShopAdapter(FragmentManager fm, List<CommodityPositionGD> cp, Handler handler) {
        super(fm);
        this.cp = cp;
        this.handler = handler;
    }
    @Override
    public Fragment getItem(int arg0) {

        Fragment_pro_type fragment =new Fragment_pro_type();
        fragment.setDismissLoadingView(this);
        Bundle bundle=new Bundle();
        String str=cp.get(arg0).getName();
        String id = cp.get(arg0).getId()+"";
        bundle.putString("t_name",str);
        bundle.putString("CID",id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return cp.size();
    }

    @Override
    public void dismissLoading() {

        handler.sendEmptyMessage(3);

    }
}

