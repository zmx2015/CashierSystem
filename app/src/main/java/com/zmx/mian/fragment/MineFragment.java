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
import android.widget.Toast;

import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.ui.AboutActivity;
import com.zmx.mian.ui.FeedbackActivity;
import com.zmx.mian.ui.GlobalConfigurationActivity;
import com.zmx.mian.ui.LoginActivity;
import com.zmx.mian.ui.StoreMessageActivity;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-25 1:54
 * 类功能：我的主页
 */

public class MineFragment extends BaseFragment{

    private TextView about_text,mine_name,global_config,text_feedback,text_bbs,store_message;
    private Button log_ut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);

        mine_name = view.findViewById(R.id.mine_name);
        mine_name.setText("账号："+MyApplication.getName());

        store_message = view.findViewById(R.id.store_message);
        store_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(StoreMessageActivity.class);

            }
        });

        text_bbs = view.findViewById(R.id.text_bbs);
        text_bbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mActivity,"正在马不停蹄的开发中",Toast.LENGTH_LONG).show();

            }
        });


        about_text = view.findViewById(R.id.about_text);
        about_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AboutActivity.class);
            }
        });


        text_feedback = view.findViewById(R.id.text_feedback);
        text_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(mActivity, FeedbackActivity.class);
                startActivity(intent);

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

        global_config = view.findViewById(R.id.global_config);
        global_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mActivity, GlobalConfigurationActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    protected void initView() {



    }

}
