package com.zmx.mian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmx.mian.R;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-23 18:29
 * 类功能：商城订单列表
 */
public class OnlineOrderFragment extends  BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.online_order_fragment,container,false);


        return view;
    }


    @Override
    protected void initView() {

    }



    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";

    public static OnlineOrderFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        OnlineOrderFragment pageFragment = new OnlineOrderFragment();
        pageFragment.setArguments(args);

        return pageFragment;
    }



}
