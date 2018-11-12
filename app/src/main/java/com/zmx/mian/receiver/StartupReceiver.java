package com.zmx.mian.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //  启动一个Service
        Intent serviceIntent = new Intent(context, MyReceiver.class);
        context.startService(serviceIntent);
    }
}