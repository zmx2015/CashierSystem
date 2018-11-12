package com.zmx.mian.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.FeedbackBean;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-19 19:55
 * 类功能：意见反馈的适配器
 */


public class FeedbackAdapter extends BaseAdapter {

    private Context context;
    private List<FeedbackBean> lists;
    private LayoutInflater inflater;

    public FeedbackAdapter(Context context,List<FeedbackBean> lists){

        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 判断使用那个布局
    @Override
    public int getItemViewType(int position) {

        FeedbackBean fb = lists.get(position);

        Log.e("adapter的类型：",fb.getType());

        if (fb.getType() == "0" || fb.getType().equals("0")) {

            return 0;

        }

        return 1;
    }

    // 返回多少个布局
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        FeedbackBean fb = lists.get(position);
        ViewHolder holder = null;

        Log.e("类型：","类型："+fb.getType());

        if (v == null) {

            holder = new ViewHolder();

            if (getItemViewType(position) == 0) {

                v = inflater.inflate(R.layout.chat_user, null);
                holder.mMsg =  v.findViewById(R.id.u_msg);
                holder.head = v
                        .findViewById(R.id.u_head);
                v.setTag(holder);

            } else {

                v = inflater.inflate(R.layout.chat_login, null);
                holder.mMsg = v.findViewById(R.id.l_msg);
                holder.head = v
                        .findViewById(R.id.l_head);
                v.setTag(holder);
            }

        } else {

            holder = (ViewHolder) v.getTag();
        }

        // 设置数据
        holder.mMsg.setText(fb.getMsg());

        //处理刷新数据后闪屏问题
        holder.head.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return v;
    }

    private final class ViewHolder {

        TextView mMsg;
        ImageView head;

    }

}