package com.zmx.mian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.TopUpBean;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-12-20 0:02
 * 类功能：充值列表
 */

public class TopUpAdapter extends BaseAdapter{

    private List<TopUpBean> lists;
    private Context context;
    private LayoutInflater inflater;

    public TopUpAdapter(Context context,List<TopUpBean> lists){

        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {

        if (lists != null && lists.size() > 0)
            return lists.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        HolderView holder = null;

        if(view == null){

            view = inflater.inflate(R.layout.adapter_top_up,null);
            holder = new HolderView(view);
            view.setTag(holder);

        }else{

            holder = (HolderView) view.getTag();

        }

        holder.textView1.setText(lists.get(i).getFace());
        holder.textView2.setText("(剩余"+lists.get(i).getNum()+"张)");
        holder.textView3.setText("赠送"+lists.get(i).getGive()+"元");

        return view;
    }

    public class HolderView {

        private TextView textView1, textView2, textView3;

        public HolderView(View itemView) {

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);

        }

    }

}
