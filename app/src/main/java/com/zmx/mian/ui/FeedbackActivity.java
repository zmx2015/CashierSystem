package com.zmx.mian.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.zmx.mian.R;
import com.zmx.mian.adapter.FeedbackAdapter;
import com.zmx.mian.bean.FeedbackBean;
import com.zmx.mian.bean_dao.FeedbackDao;
import com.zmx.mian.util.MySharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-19 19:41
 * 类功能：意见反馈界面
 */
public class FeedbackActivity extends BaseActivity {

    private ListView listView;
    private FeedbackAdapter adapter;
    private List<FeedbackBean> list;
    private EditText feedback_edit;
    private Button feedback_btn;

    private FeedbackDao dao;
    private String name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initViews() {

        name = MySharedPreferences.getInstance(this).getString(MySharedPreferences.name,"");
        dao = new FeedbackDao();
        list = new ArrayList<>();
        feedback_edit = findViewById(R.id.feedback_edit);
        feedback_btn = findViewById(R.id.feedback_btn);
        feedback_btn.setOnClickListener(this);
        listView = findViewById(R.id.listview);

        String s = MySharedPreferences.getInstance(this).getString(MySharedPreferences.fb_message,"");
        Log.e("状态","状态"+s);
        if(s.equals("0")){

            FeedbackBean fb = new FeedbackBean();
            fb.setAdmin_name("admin");
            fb.setLogin_name(name);
            fb.setMsg("您好！这里是果上人家的客户服务平台，很高兴为您服务！有什么可以为您效劳的？");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fb.setTime(df.format(new Date()));
            fb.setType("0");

            long l = dao.addFeedbackBean(fb);

            if(l>0){

                MySharedPreferences.getInstance(this).saveKeyObjValue(MySharedPreferences.fb_message,"1");//区分插入客服提示消息

            }

        }

        List<FeedbackBean> l = dao.SelectAllFeedbackBean(name);

        for (FeedbackBean f:l){

            list.add(f);

        }

        info();

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    info();
                    feedback_edit.setText("");
                    break;

            }

        }
    };



    // 更新显示listview
    public void info() {

        if (adapter == null) {

            adapter = new FeedbackAdapter(this, list);
            listView.setAdapter(adapter);

        } else {

            adapter.notifyDataSetChanged();

        }

        listView.setSelection(list.size()-1);//控制消息保持在底部

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.feedback_btn:

                FeedbackBean fb = new FeedbackBean();
                fb.setAdmin_name(MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name,""));
                fb.setMsg(feedback_edit.getText().toString());
                fb.setLogin_name(name);
                fb.setAdmin_name("admin");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                fb.setTime(df.format(new Date()));
                fb.setType("1");

                list.add(fb);
                dao.addFeedbackBean(fb);
                handler.sendEmptyMessage(1);
                break;

        }


    }
}
