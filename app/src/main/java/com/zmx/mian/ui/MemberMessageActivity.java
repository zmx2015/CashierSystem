package com.zmx.mian.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.zmx.mian.R;
import com.zmx.mian.adapter.ZhiHuFragmentAdapter;
import com.zmx.mian.bean.members.Members;
import com.zmx.mian.bean.members.MembersList;
import com.zmx.mian.bean.members.MembersOrder;
import com.zmx.mian.fragment.memberfragment.MemberExchangeFragment;
import com.zmx.mian.fragment.memberfragment.MemberOrderFragment;
import com.zmx.mian.fragment.memberfragment.MemberPrizeFragment;
import com.zmx.mian.fragment.memberfragment.MembersSignFragment;
import com.zmx.mian.http.OkHttp3ClientManager;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.FragmentViewPager;
import com.zmx.mian.ui.util.GlideCircleTransform;
import com.zmx.mian.ui.util.MyButton;
import com.zmx.mian.util.BlurTransformation;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IMembersMessageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberMessageActivity extends BaseActivity implements IMembersMessageView {

    Toolbar mToolbar;
    AppBarLayout mAppBar;
    ImageView mHeadImage, member_background;
    TextView mTitle;
    TextView tvSearch;
    TextView login_time;

    TabLayout mTabLayout;
    NestedScrollView mScrollView;
    FragmentViewPager mViewpager;
    TabLayout mTabLayoutCopy;
    private String[] titles = {"购买记录", "签到记录", "兑换记录", "抽奖记录"};
    private List<Fragment> fragmentList;

    private MembersList ml;

    private TextView account_text, consumption_text, phone_number_text, consumption_text_1;
    private Button binding;//绑定手机号

    private OrderPresenter op;
    private String account,shoujihao="";
    private Members m;

    private float mSelfHeight = 0;  //用以判断是否得到正确的宽高值
    private float mTitleScale;      //标题缩放值
    private float mHeadImgScale;    //头像缩放值
    private float mTestScaleY;      //测试按钮y轴缩放值
    private float mTestScaleX;      //测试按钮x轴缩放值

    @Override
    protected int getLayoutId() {
        return R.layout.activity_member_message;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        Bundle data = getIntent().getExtras();//从bundle中取出数据
        account = data.getString("account");
        Log.e("account", "account" + account);
        op = new OrderPresenter(this);

        account_text = findViewById(R.id.account_text);
        consumption_text = findViewById(R.id.consumption_text);
        phone_number_text = findViewById(R.id.phone_number_text);
        consumption_text_1 = findViewById(R.id.consumption_text_1);

        binding = findViewById(R.id.binding);
        binding.setOnClickListener(this);

        mToolbar = findViewById(R.id.toolbar);
        mAppBar = findViewById(R.id.app_bar);
        mHeadImage = findViewById(R.id.member_head);
        member_background = findViewById(R.id.member_background);
        mTitle = findViewById(R.id.member_title);
        tvSearch = findViewById(R.id.tv_search);
        login_time = findViewById(R.id.login_time);
        mViewpager = findViewById(R.id.viewPager_zhihu);
        mScrollView = findViewById(R.id.nestedScrollView);
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayoutCopy = findViewById(R.id.tab_layout_copy);
        mTabLayoutCopy.setVisibility(View.GONE);
        final float screenW = getResources().getDisplayMetrics().widthPixels;
        final float toolbarHeight = getResources().getDimension(R.dimen.tool_bar_height);
        final float initHeight = getResources().getDimension(R.dimen.app_bar_height);
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayoutCopy.setupWithViewPager(mViewpager);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                int[] location = new int[2];
                mTabLayout.getLocationInWindow(location);
                mTabLayout.getLocationOnScreen(location);

                if (location[1] < Tools.dp2px(MemberMessageActivity.this, 88)) {
                    mTabLayoutCopy.setVisibility(View.VISIBLE);
                    mTabLayout.setVisibility(View.INVISIBLE);
                } else {
                    mTabLayoutCopy.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        /**
         *   移动效果值／最终效果值 =  移动距离／ 能移动总距离（确定）
         */
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mSelfHeight == 0) {

                    //获取标题高度
                    mSelfHeight = mTitle.getHeight();

                    //得到标题的高度差
                    float distanceTitle = mTitle.getTop() - (toolbarHeight - mTitle.getHeight()) / 2.0f;
                    //得到测试按钮的高度差
                    float distanceTest = login_time.getY() - (toolbarHeight - login_time.getHeight()) / 2.0f;
                    //得到图片的高度差
                    float distanceHeadImg = mHeadImage.getY() - (toolbarHeight - mHeadImage.getHeight()) / 2.0f;
                    //得到测试按钮的水平差值  屏幕宽度一半 - 按钮宽度一半
//                    float distanceSubscribeX = screenW / 2.0f - (login_time.getWidth() / 2.0f);

                    //得到高度差缩放比值  高度差／能滑动总长度 以此类推
                    mTitleScale = distanceTitle / (initHeight - toolbarHeight);
                    mTestScaleY = distanceTest / (initHeight - toolbarHeight) + 1;
                    mHeadImgScale = distanceHeadImg / (initHeight - toolbarHeight) + 1;
//                    mTestScaleX = distanceSubscribeX / (initHeight - toolbarHeight);

                }
                //得到文本框、头像缩放值 不透明 ->透明  文本框x跟y缩放
                float scale = 1.0f - (-verticalOffset) / (initHeight - toolbarHeight);

                tvSearch.setScaleX(scale);
                tvSearch.setScaleY(scale);
                tvSearch.setAlpha(scale);

                mHeadImage.setScaleX(scale);
                mHeadImage.setScaleY(scale);
                login_time.setScaleX(scale);
                login_time.setScaleY(scale);
                //设置头像y轴平移
                mHeadImage.setTranslationY(mHeadImgScale * verticalOffset);
                //设置标题y轴平移
                mTitle.setTranslationY(mTitleScale * verticalOffset);         //设置测试按钮x跟y平移
                login_time.setTranslationY(mTestScaleY * verticalOffset);
                login_time.setTranslationX(-mTestScaleX * verticalOffset);

            }
        });

        String mid = MySharedPreferences.getInstance(this).getString(MySharedPreferences.store_id, "");
        String name = MySharedPreferences.getInstance(this).getString(MySharedPreferences.name, "");


        op.getMembersMessage(mid, account, name);

        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(3);
            }
        });

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ml = m.getList();
            switch (msg.what) {

                case 1:

                    mScrollView.setVisibility(View.VISIBLE);

                    if (Util.isOnMainThread()) {
                        Glide.with(MemberMessageActivity.this).load(ml.getWechatImg()).transform(new GlideCircleTransform(MemberMessageActivity.this)).error(R.drawable.icon_login_account).into(mHeadImage);
                        Glide.with(MemberMessageActivity.this).load(ml.getWechatImg()).bitmapTransform(new BlurTransformation(MemberMessageActivity.this, 23)).error(R.drawable.icon_login_account).into(member_background);

                    }


                    account_text.setText("账号：" + ml.getAccount());

                    if (ml.getWechatName() == null) {

                        mTitle.setText("水果大户");
                    } else {

                        mTitle.setText(ml.getWechatName());
                    }

                    String lasttime = ml.getLasttime() + "";
                    if (lasttime.equals("") || lasttime.equals("")) {

                        login_time.setText("很久没登录了");

                    } else {

                        login_time.setText("最近登录：" + (Tools.refFormatNowDate(ml.getLasttime() + "", 1)));

                    }

                    //消费次数和金额
                    List<MembersOrder> mos = m.getOrders();
                    float total = 0;
                    float f = 0;
                    float discount = 0;
                    for (MembersOrder m : mos) {

                        f = Float.parseFloat(m.getTotal()) - Float.parseFloat(m.getDiscount());
                        discount = Float.parseFloat(m.getDiscount()) + discount;
                        total = total + f;

                    }
                    consumption_text.setText("消费金额：" + (float) (Math.round(total * 100)) / 100 + "元 | 已优惠：" + (Math.round(discount * 100)) / 100 + "元");
                    //绑定的手机号码
                    consumption_text_1.setText("消费次数：" + mos.size() + "次 | 消费均价：" + (float) (Math.round((total / mos.size()) * 100)) / 100 + "元 ");

                    if (ml.getMob() == null || ml.getMob() == "") {

                        phone_number_text.setText("手机号： 未绑定");
                        binding.setText("绑定号码");


                    } else {

                        phone_number_text.setText("手机号：" + ml.getMob());
                        binding.setText("更换号码");

                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orders", (Serializable) mos);
                    bundle.putSerializable("sign", (Serializable) m.getSign());
                    bundle.putSerializable("exchanges", (Serializable) m.getExchanges());
                    bundle.putSerializable("prizes", (Serializable) m.getPrizes());
                    fragmentList = new ArrayList<>();
                    //购买记录
                    MemberOrderFragment mof = new MemberOrderFragment();
                    mof.setArguments(bundle);
                    //积分记录
                    MembersSignFragment sign = new MembersSignFragment();
                    sign.setArguments(bundle);
                    //签到记录
                    MemberExchangeFragment exchange = new MemberExchangeFragment();
                    exchange.setArguments(bundle);
                    //兑换记录
                    MemberPrizeFragment prize = new MemberPrizeFragment();
                    prize.setArguments(bundle);

                    fragmentList.add(mof);
                    fragmentList.add(sign);
                    fragmentList.add(exchange);
                    fragmentList.add(prize);

                    ZhiHuFragmentAdapter adapter = new ZhiHuFragmentAdapter(
                            getSupportFragmentManager(), fragmentList, titles);

                    mViewpager.setAdapter(adapter);

                    break;

                case 2:

                    dismissLoadingView();//隐藏加载框

                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String content = jsonObject.getString("content");
                        String code = jsonObject.getString("code");
                        if(code.equals("1")){

                            phone_number_text.setText("手机号：" + shoujihao);
                            binding.setText("更换号码");

                        }
                        Toast.makeText(mActivity,content,Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 3:

                    imgMax(ml.getWechatImg());
                    break;

                case 404:

                    Toast.makeText(mActivity, "网络连接失败！请检查网络", Toast.LENGTH_LONG).show();
                    dismissLoadingView();//隐藏加载框

                    break;

            }


        }
    };

    @Override
    public void getMembersMessage(Members m) {
        this.m = m;
        handler.sendEmptyMessage(1);

    }

    @Override
    public void ErrerMessage(String state) {

    }

    /**
     * 点击查看大图
     */
    public void imgMax(String url) {

        url = url.substring(0, url.lastIndexOf("/"));
        Log.e("截取的图片路径", "" + url);

        LayoutInflater inflater = LayoutInflater.from(this);
        View imgEntryView = inflater.inflate(R.layout.to_view_image, null);
        // 加载自定义的布局文件
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);
        Glide.with(MemberMessageActivity.this).load(url + "/0").error(R.drawable.icon_login_account).into(img);

        dialog.setView(imgEntryView); // 自定义dialog
        dialog.show();
        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        imgEntryView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.cancel();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            //绑定手机号码
            case R.id.binding:

                alert_edit();

                break;

        }

    }

    public void alert_edit() {


        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.dialog_edit, null);//这里必须是final的
        final EditText et = view.findViewById(R.id.editText);
        builder.setTitle("请输入绑定手机");

        final AlertDialog dialog = builder.create();
        dialog.setView(view);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Tools.isChinaPhoneLegal(et.getText().toString())) {

                    showLoadingView("绑定中...");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("admin", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, ""));
                    params.put("mid", MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, ""));
                    params.put("pckey", new Tools().getKey(mActivity));
                    params.put("account", ml.getAccount());
                    params.put("phone", et.getText().toString());
                    shoujihao = et.getText().toString();
                    OkHttp3ClientManager.getInstance().NetworkRequestMode("http://www.yiyuangy.com/admin/api.user/bindPhone", params, handler, 2, 404);
                    dialog.dismiss();
                } else {

                    Toast.makeText(mActivity, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


}
