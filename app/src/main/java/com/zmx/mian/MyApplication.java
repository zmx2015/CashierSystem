package com.zmx.mian;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zmx.mian.dao.DaoMaster;
import com.zmx.mian.dao.DaoSession;
import com.zmx.mian.ui.MainActivity;
import com.zmx.mian.util.MySharedPreferences;

import cn.jpush.android.api.JPushInterface;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-14 16:27
 * 类功能：
 */

public class MyApplication extends Application{

    private static MyApplication myApplication;
    private static DaoSession daoSession;

    public static String store_id = "";//用户id
    public static String name = "";//手机号码

    public static MyApplication getInstance() {
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(myApplication);
        getInstance();
        //配置数据库
        setupDatabase();

    }


    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "cashier.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {

        return daoSession;

    }

    public static String getStore_id() {
        return MySharedPreferences.getInstance(myApplication).getString(MySharedPreferences.store_id,"");

    }

    public static String getName() {
        return MySharedPreferences.getInstance(myApplication).getString(MySharedPreferences.name,"");

    }


}
