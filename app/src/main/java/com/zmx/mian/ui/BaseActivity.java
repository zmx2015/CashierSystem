package com.zmx.mian.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.ui.util.LoadingDialog;

import java.lang.reflect.Field;

/**
 * 作者：胖胖祥
 * 时间：2016/8/23 0023 下午 5:36
 * 功能模块：自定义activity
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected View positionView;
    protected Activity mActivity;
    private LinearLayout load_view;//加载提示布局

    protected LoadingDialog mLoadingDialog; //显示正在加载的对话框
    /**
     * 跳转到下一个activity
     **/
    protected static final int REQUEST_ACTIVITY = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_base);
        mActivity = this;

        //找到资源文件的XML
        if (getLayoutId() != 0) {

            View vContent = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
            ((FrameLayout) findViewById(R.id.frame_content)).addView(vContent);

        }
        initViews();
    }

    public void BackButton(int id){

        View v = findViewById(id);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

    }

    @Override
    public void onClick(View v) {


    }
    /**
     * 设置加载提示框
     */
    protected void showLoadingView(){

        if(load_view == null){

            load_view = (LinearLayout)LayoutInflater.from(mActivity).inflate(R.layout.load_view, null);

            ((FrameLayout)findViewById(R.id.frame_content)).addView(load_view);//主布局LinearLayout

        }
        load_view.setVisibility(View.VISIBLE);

    }
    /**
     * 数据加载完成
     */
    protected void dismissLoading(){

        if(load_view != null){

            load_view.setVisibility(View.GONE);

        }

    }

    /**
     * 设置加载提示框
     */
    protected void showLoadingView(String message){

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this,message, false);
        }
        mLoadingDialog.show();

    }
    /**
     * 数据加载完成
     */
    protected void dismissLoadingView(){

        if (mLoadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });

        }

    }

    /**
     * 初始化Activity的常用变量 举例:
     * <b>mLayoutResID=页面XML资源ID 必须存在的</b>
     */
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     **/
    protected abstract void initViews();

    /**
     * 通过Class跳转界面
     *
     * @param cls
     */
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     *
     * @param cls
     * @param bundle
     */
    protected void startActivity(Class<?> cls, Bundle bundle) {
        startActivity(cls, bundle, REQUEST_ACTIVITY);
    }

    /**
     * 通过Class跳转界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    protected void startActivity(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    //获取状态栏的高度
    public int getStatusBarHeight() {

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    //设置一体化
    public void setTitleColor(int id){

        // 沉浸式状态栏
        positionView = findViewById(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);

        }

    }



}
