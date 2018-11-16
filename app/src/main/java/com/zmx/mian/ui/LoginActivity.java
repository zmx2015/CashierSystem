package com.zmx.mian.ui;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;
import com.zmx.mian.R;
import com.zmx.mian.bean.StoresMessage;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.util.Base64Utils;
import com.zmx.mian.util.MySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-24 2:30
 * 类功能：登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    //布局内的控件
    private EditText et_name;
    private EditText et_password;
    private Button mLoginBtn;
    private CheckBox checkBox_password;
    private CheckBox checkBox_login;
    private ImageView iv_see_password;

    private void initData() {

        //判断用户第一次登陆
        if (firstLogin()) {
            checkBox_password.setChecked(false);//取消记住密码的复选框
            checkBox_login.setChecked(false);//取消自动登录的复选框
        }
        //判断是否记住密码
        if (remenberPassword()) {
            checkBox_password.setChecked(true);//勾选记住密码
            setTextNameAndPassword();//把密码和账号输入到输入框中
        } else {
            setTextName();//把用户账号放到输入账号的输入框中
        }

        //判断是否自动登录
        if (autoLogin()) {
            checkBox_login.setChecked(true);
            login();//去登录就可以
        }
    }

    /**
     * 把本地保存的数据设置数据到输入框中
     */
    public void setTextNameAndPassword() {
        et_name.setText("" + getLocalName());
        et_password.setText("" + getLocalPassword());
    }

    /**
     * 设置数据到输入框中
     */
    public void setTextName() {
        et_name.setText("" + getLocalName());
    }


    /**
     * 获得保存在本地的用户名
     */
    public String getLocalName() {

        String name =MySharedPreferences.getInstance(this).getString(MySharedPreferences.name,"");
        return name;
    }


    /**
     * 获得保存在本地的密码
     */
    public String getLocalPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        String password =  MySharedPreferences.getInstance(this).getString(MySharedPreferences.password,"");
        return Base64Utils.decryptBASE64(password);   //解码一下
//       return password;   //解码一下

    }

    /**
     * 判断是否自动登录
     */
    private boolean autoLogin() {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        boolean autoLogin = MySharedPreferences.getInstance(this).getBoolean(MySharedPreferences.autoLogin,false);
        return autoLogin;
    }

    /**
     * 判断是否记住密码
     */
    private boolean remenberPassword() {

        boolean remenberPassword = MySharedPreferences.getInstance(this).getBoolean(MySharedPreferences.remenberPassword,false);
        return remenberPassword;
    }

    private void setupEvents() {
        mLoginBtn.setOnClickListener(this);
        checkBox_password.setOnCheckedChangeListener(this);
        checkBox_login.setOnCheckedChangeListener(this);
        iv_see_password.setOnClickListener(this);

    }

    /**
     * 判断是否是第一次登陆
     */
    private boolean firstLogin() {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        boolean first =MySharedPreferences.getInstance(this).getBoolean(MySharedPreferences.first,false);

        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空

            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.first,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.remenberPassword,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.autoLogin,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.name,"");
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.password,"");

            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loadUserName();    //无论如何保存一下用户名
                login(); //登陆
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {



        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        mLoginBtn = (Button) findViewById(R.id.btn_login);
        et_name = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        checkBox_password = (CheckBox) findViewById(R.id.checkBox_password);
        checkBox_login = (CheckBox) findViewById(R.id.checkBox_login);
        iv_see_password = (ImageView) findViewById(R.id.iv_see_password);
        setupEvents();
        initData();

    }

    /**
     * 模拟登录情况
     * 用户名csdn，密码123456，就能登录成功，否则登录失败
     */
    private void login() {

        //先做一些基本的判断，比如输入的用户命为空，密码为空，网络不可用多大情况，都不需要去链接服务器了，而是直接返回提示错误
        if (getAccount().isEmpty()){
            showToast("你输入的账号为空！");
            return;
        }

        if (getPassword().isEmpty()){
            showToast("你输入的密码为空！");
            return;
        }
        //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
        showLoadingView("正在登陆...");//显示加载框

        //请求网络
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", getAccount());
        params.put("password", md5(getPassword()));

        OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.line/login", params, handler, 1, 404);

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    try {

                        JSONObject json = new JSONObject(msg.obj.toString());

                        List<StoresMessage> lists = new ArrayList<>();

                        //获取登录账号的信息
                        if(json.getString("code") == "1" || json.getString("code").equals("1")) {

                            int st = json.getInt("codes");

                            if (st == 1) {

                                JSONArray array = json.getJSONArray("store");

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject j = array.getJSONObject(i);

                                    Gson gson = new Gson();
                                    StoresMessage sm = gson.fromJson(j.toString(), StoresMessage.class);
                                    lists.add(sm);

                                }

                                boolean b = MySharedPreferences.getInstance(mActivity).getBoolean(MySharedPreferences.JPush_state, false);
                                if (!b) {

                                    // 调用 Handler 来异步设置别名
                                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, getAccount()));

                                }

                                showToast("登录成功");
                                loadCheckBoxState();//记录下当前用户记住密码和自动登录的状态;

                                //判断是否已经保存了门店信息了，有就直接跳到主页，没有就跳到门店列表
                                //获取SharedPreferences对象，使用自定义类的方法来获取对象
                                String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
                                Log.e("门店id", "mid" + mid);
                                if (mid.equals("-1") || mid == "") {

                                    MySharedPreferences.getInstance(mActivity).setDataList("store", lists);

                                    // 通过Intent传递对象给Service
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("stores", (Serializable) lists);
                                    intent.setClass(mActivity, StoreListActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();//关闭页面

                                } else {

                                    startActivity(MainActivity.class);
                                    finish();//关闭页面

                                }

                            } else {

                                showToast("没有门店，请在后台添加门店");
                                setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                                dismissLoadingView();//隐藏加载框

                            }
                        }else{
                            showToast("输入的登录账号或密码不正确");
                            dismissLoadingView();//隐藏加载框
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        showToast("登录失败！请联系客服");
                        setLoginBtnClickable(true);  //这里解放登录 按钮，设置为可以点击
                        dismissLoadingView();//隐藏加载框

                    }

                    break;

                case 404:

                    showToast("网络连接失败！请检查网络");
                    setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                    dismissLoadingView();//隐藏加载框

                    break;

            }

        }
    };


    /**
     * 保存用户账号
     */
    public void loadUserName() {
        if (!getAccount().equals("") || !getAccount().equals("请输入登录账号")) {

            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.name,getAccount());
        }

    }

    /**
     * 设置密码可见和不可见的相互转换
     */
    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }

    /**
     * 获取账号
     */
    public String getAccount() {
        return et_name.getText().toString().trim();//去掉空格
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        return et_password.getText().toString().trim();//去掉空格
    }


    /**
     * 保存用户选择“记住密码”和“自动登陆”的状态
     */
    private void loadCheckBoxState() {
        loadCheckBoxState(checkBox_password, checkBox_login);
    }

    /**
     * 保存按钮的状态值
     */
    public void loadCheckBoxState(CheckBox checkBox_password, CheckBox checkBox_login) {

        //如果设置自动登录
        if (checkBox_login.isChecked()) {
            //创建记住密码和自动登录是都选择,保存密码数据
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.remenberPassword,true);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.autoLogin,true);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.password,Base64Utils.encryptBASE64(getPassword()));

        } else if (!checkBox_password.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.remenberPassword,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.autoLogin,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.password,"");

        } else if (checkBox_password.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.remenberPassword,true);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.autoLogin,false);
            MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.password, Base64Utils.encryptBASE64(getPassword()));
            }
    }

    /**
     * 是否可以点击登录按钮
     *
     * @param clickable
     */
    public void setLoginBtnClickable(boolean clickable) {
        mLoginBtn.setClickable(clickable);
    }


    /**
     * CheckBox点击时的回调方法 ,不管是勾选还是取消勾选都会得到回调
     *
     * @param buttonView 按钮对象
     * @param isChecked  按钮的状态
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == checkBox_password) {  //记住密码选框发生改变时
            if (!isChecked) {   //如果取消“记住密码”，那么同样取消自动登陆
                checkBox_login.setChecked(false);
            }
        } else if (buttonView == checkBox_login) {   //自动登陆选框发生改变时
            if (isChecked) {   //如果选择“自动登录”，那么同样选中“记住密码”
                checkBox_password.setChecked(true);
            }
        }
    }


    /**
     * 监听回退键
     */
    @Override
    public void onBackPressed() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    /**
     * 页面销毁前回调的方法
     */
    protected void onDestroy() {

        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
            mLoadingDialog = null;
        }
        super.onDestroy();
    }


    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Md5加密函数
     *
     * @param txt
     * @return
     */
    public String md5(String txt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes("GBK")); // Java的字符串是unicode编码，不受源码文件的编码影响；而PHP的编码是和源码文件的编码一致，受源码编码影响。
            StringBuffer buf = new StringBuffer();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xff));
            }
            return buf.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置极光推送别名
     */

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    MySharedPreferences.getInstance(mActivity).saveKeyObjValue(MySharedPreferences.JPush_state,true);//保存本地
                    Log.e("设置成功", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.e("设置失败，重新设置", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("e", logs);
            }
            Toast.makeText(LoginActivity.this,"logs+getApplicationContext()",Toast.LENGTH_LONG).show();
        }
    };

    private static final int MSG_SET_ALIAS = 1001;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.e("e", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.e("e", "Unhandled msg - " + msg.what);
            }
        }
    };

}

