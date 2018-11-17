package com.zmx.mian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.ui.AboutActivity;
import com.zmx.mian.ui.FeedbackActivity;
import com.zmx.mian.ui.LoginActivity;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-25 1:54
 * 类功能：我的主页
 */

public class MineFragment extends BaseFragment{

    private TextView about_text,mine_name;
    private Button log_ut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);

        mine_name = view.findViewById(R.id.mine_name);
        mine_name.setText("账号："+MyApplication.getName());
        about_text = view.findViewById(R.id.about_text);
        about_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(FeedbackActivity.class);
                startActivity(AboutActivity.class);
            }
        });

        log_ut = view.findViewById(R.id.log_ut);
        log_ut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tools.deleteData(mActivity);//清空本地数据

                Intent intent = new Intent();
                intent.setClass(mActivity, LoginActivity.class);
                startActivity(intent);
                mActivity.finish();

            }
        });
        return view;
    }

    @Override
    protected void initView() {



    }

}
