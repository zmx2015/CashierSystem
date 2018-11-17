package com.zmx.mian.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmx.mian.R;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-17 20:21
 * 类功能：关于我们
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

    }
}
