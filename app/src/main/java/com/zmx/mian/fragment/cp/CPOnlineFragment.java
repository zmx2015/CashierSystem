package com.zmx.mian.fragment.cp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmx.mian.R;
import com.zmx.mian.fragment.BaseFragment;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-14 17:32
 * 类功能：商城类别
 */
public class CPOnlineFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cp_online_fragment,container,false);

        return view;
    }


    @Override
    protected void initView() {

    }

    private static final String PAGE_NAME_KEY = "PAGE_NAME_KEY";

    public static CPOnlineFragment getInstance(String pageName) {
        Bundle args = new Bundle();
        args.putString(PAGE_NAME_KEY, pageName);
        CPOnlineFragment pageFragment = new CPOnlineFragment();
        pageFragment.setArguments(args);

        return pageFragment;
    }


}

