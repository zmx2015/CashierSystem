package com.zmx.mian.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.fragment.ConvenientCashierFragment;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-18 14:31
 * 类功能：前台收银用到
 */

public class ConvenientAdapter extends FragmentPagerAdapter implements ConvenientCashierFragment.NoticeDismissLoadingView {

    List<CommodityPositionGD> cp ;
    Handler handler;
    public ConvenientAdapter(FragmentManager fm, List<CommodityPositionGD> cp, Handler handler,ConvenientFoodActionCallback cfc) {
        super(fm);
        this.cp = cp;
        this.handler = handler;
        this.cfc = cfc;
    }
    @Override
    public Fragment getItem(int arg0) {

        ConvenientCashierFragment fragment =new ConvenientCashierFragment();
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

    @Override
    public void setViceOrder(ViceOrder vo) {

        Message msg = new Message();
        msg.what = 4;
        Bundle bundle = new Bundle();
        bundle.putSerializable("vo", vo);
        msg.setData(bundle);//mes利用Bundle传递数据
        handler.sendMessage(msg);//用activity中的handler发送消息


    }

    @Override
    public void addAction(View view, int position) {
        cfc.addAction(view,position);
    }

    ConvenientFoodActionCallback cfc;
    public interface ConvenientFoodActionCallback {
        void addAction(View view, int position);
    }

}

