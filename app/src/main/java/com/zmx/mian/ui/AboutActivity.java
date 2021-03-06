package com.zmx.mian.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.zmx.mian.R;
import com.zmx.mian.ui.util.CalendarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-17 20:21
 * 类功能：关于我们
 */
public class AboutActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        BackButton(R.id.back_button);

        webView = findViewById(R.id.webView);
        //WebView加载web资源
        webView.loadUrl("http://www.yiyuangy.com/about.html");
//启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

    }


}
