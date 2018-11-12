package com.zmx.mian.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zmx.mian.R;
import com.zmx.mian.adapter.MembersListAdapter;
import com.zmx.mian.bean.members.Members;
import com.zmx.mian.bean.members.MembersList;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.ui.util.GlideCircleTransform;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.view.IMembersMessageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-04 15:31
 * 类功能：搜索会员
 */
public class MembersSearchActivity extends BaseActivity implements IMembersMessageView {

    private OrderPresenter op;

    private EditText et;
    private ImageView iv,head;
    private TextView name,mumber;
    private LinearLayout load,no_members;
    private RelativeLayout search_tol;

    private Members m;
    private String account;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_members_search;
    }

    @Override
    protected void initViews() {


        // 沉浸式状态栏
        setTitleColor(R.id.position_view);

        op = new OrderPresenter(this);

        no_members = findViewById(R.id.no_members);
        load = findViewById(R.id.search_load);
        search_tol= findViewById(R.id.search_tol);
        search_tol.setVisibility(View.GONE);
        head = findViewById(R.id.member_search_head);
        name = findViewById(R.id.member_search_name);
        mumber = findViewById(R.id.member_search_number);
        et = findViewById(R.id.member_search_edit);
        iv = findViewById(R.id.member_search_btn);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id, "");
                String name = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name, "");

                String account = et.getText().toString();
                load.setVisibility(View.VISIBLE);
                op.getMembersMessage(mid,account,name);
            }
        });

        search_tol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();   //得到一个 bundle对象
                bundle.putString("account", account); // 将获取的edittext 的值通过bundle对象分别给 account 与 password

                startActivity(MemberMessageActivity.class,bundle);

            }
        });

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    load.setVisibility(View.GONE);
                    no_members.setVisibility(View.GONE);
                    search_tol.setVisibility(View.VISIBLE);
                    Glide.with(MembersSearchActivity.this).load(m.getList().getWechatImg()).transform(new GlideCircleTransform(MembersSearchActivity.this)).error(R.drawable.icon_login_account).into(head);
                    mumber.setText("会员账号："+m.getList().getAccount()+"   积分："+m.getList().getIntegral());
                    account = m.getList().getAccount();

                    break;

                case 2:
                    load.setVisibility(View.VISIBLE);
                    break;

                case 3:

                    load.setVisibility(View.GONE);
                    search_tol.setVisibility(View.GONE);
                    no_members.setVisibility(View.VISIBLE);

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
    public void ErrerMessage(String msg) {

        handler.sendEmptyMessage(3);

    }
}
