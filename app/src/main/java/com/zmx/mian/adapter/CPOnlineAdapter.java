package com.zmx.mian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.MallTypeBean;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-15 22:55
 * 类功能：商城分类
 */
public class CPOnlineAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<MallTypeBean> lists;

    public CPOnlineAdapter(Context context,List<MallTypeBean> lists){

        mContext=context;
        this.lists=lists;
        mLayoutInflater=LayoutInflater.from(context);

    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        public NormalViewHolder(View itemView) {
            super(itemView);

            mTextView=itemView.findViewById(R.id.cp_name);

        }
    }
    //在该方法中我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    @Override
    public CPOnlineAdapter.NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CPOnlineAdapter.NormalViewHolder(mLayoutInflater.inflate(R.layout.cp_stores_item,parent,false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CPOnlineAdapter.NormalViewHolder viewholder = (CPOnlineAdapter.NormalViewHolder) holder;
        viewholder.mTextView.setText(lists.get(position).getTname());
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return lists==null ? 0 : lists.size();
    }

}