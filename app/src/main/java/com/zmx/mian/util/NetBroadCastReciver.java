package com.zmx.mian.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zmx.mian.bean.NetUtil;
import com.zmx.mian.ui.BaseActivity;
import com.zmx.mian.ui.util.MyDialog;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-12 16:51
 * 类功能：广播功能
 */
public class NetBroadCastReciver extends BroadcastReceiver {

    public NetEvevt evevt = BaseActivity.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {



        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

            Log.e("网络发生改变","网络发生改变");

            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            evevt.onNetChange(netWorkState);
        }
    }

    // 自定义接口
    public interface NetEvevt {
        void onNetChange(int netMobile);
    }

}

