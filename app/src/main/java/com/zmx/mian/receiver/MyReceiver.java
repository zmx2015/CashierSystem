package com.zmx.mian.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zmx.mian.R;
import com.zmx.mian.bean.FeedbackBean;
import com.zmx.mian.bean_dao.FeedbackDao;
import com.zmx.mian.util.MySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-20 17:43
 * 类功能：负责接收推送消息
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    private Context context;
    private FeedbackDao dao;


    @Override
    public void onReceive(Context context, Intent intent) {

        dao = new FeedbackDao();
        this.context = context;
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.e(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的自定义消息");

            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

            Log.e("title",""+title);
            Log.e("message",""+message);
            Log.e("extras",""+extras);


            FeedbackBean fb = new FeedbackBean();
            fb.setAdmin_name("admin");
            fb.setLogin_name(MySharedPreferences.getInstance(context).getString(MySharedPreferences.name,""));
            fb.setMsg("服务器给你回消息了");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fb.setTime(df.format(new Date()));
            fb.setType("0");

            dao.addFeedbackBean(fb);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            Log.e(TAG, "接受到推送下来的通知");
            Log.e("message",""+message);
            Log.e("title",""+title);
            Log.e("EXTRA_EXTRA",""+extras);

        }else {
            Log.e(TAG, "Unhandled intent - " + intent.getAction());
        }

    }

    private void receivingNotification(Context context, Bundle bundle){

        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        // 使用notification
        // 使用广播或者通知进行内容的显示
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentText(message).setSmallIcon(R.mipmap.i).setContentTitle(JPushInterface.EXTRA_TITLE);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        manager.notify(1,builder.build());

    }

}


