package com.zmx.mian.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zmx.mian.MyApplication;
import com.zmx.mian.R;
import com.zmx.mian.bean.Tab;
import com.zmx.mian.bean_dao.FeedbackDao;
import com.zmx.mian.fragment.GoodsFragment;
import com.zmx.mian.fragment.HomeFragment;
import com.zmx.mian.fragment.MessageFragment;
import com.zmx.mian.fragment.MineFragment;
import com.zmx.mian.fragment.ProcurementFragment;
import com.zmx.mian.ui.util.FragmentTabHost;
import com.zmx.mian.util.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private FragmentTabHost mTabhost;

    private LayoutInflater inFlater;
    private List<Tab> mTabs = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

        //自动检测更新
        Beta.autoCheckUpgrade = true;
        Beta.upgradeCheckPeriod = 60 * 1000;
        Beta.initDelay = 1 * 1000;
        Beta.largeIconId = R.mipmap.i;
        Beta.smallIconId = R.mipmap.i;
        Beta.storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.showInterruptedStrategy = true;
        Bugly.init(this,"77df3b0d1d",false);//
        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        initTab();

    }

    private void initTab() {

        Tab home = new Tab(R.string.home,R.drawable.selector_icon_home,HomeFragment.class);
        Tab procurement = new Tab(R.string.procurement,R.drawable.selector_icon_cart,ProcurementFragment.class);
        Tab dynamic = new Tab(R.string.dynamic,R.drawable.selector_icon_hot,GoodsFragment.class);
        Tab cart = new Tab(R.string.cart,R.drawable.selector_icon_cart,MessageFragment.class);
        Tab mine = new Tab(R.string.mine,R.drawable.selector_icon_mine,MineFragment.class);

        mTabs.add(home);
        mTabs.add(procurement);
        mTabs.add(dynamic);
        mTabs.add(cart);
        mTabs.add(mine);



        inFlater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(R.id.tabhost);
        mTabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for(Tab tab : mTabs){

            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabhost.addTab(tabSpec, tab.getFragment(),null);

        }

        //去除分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);


    }

    private View buildIndicator(Tab tab){

        View view = inFlater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;
    }


    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }


}
