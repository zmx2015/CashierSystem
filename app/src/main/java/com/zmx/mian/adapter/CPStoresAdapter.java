package com.zmx.mian.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean.StoresMessage;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-14 19:29
 * 类功能：门店分类管理
 */
public class CPStoresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<CommodityPositionGD> lists;

    public CPStoresAdapter(Context context,List<CommodityPositionGD> lists){

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
    public CPStoresAdapter.NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CPStoresAdapter.NormalViewHolder(mLayoutInflater.inflate(R.layout.cp_stores_item,parent,false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CPStoresAdapter.NormalViewHolder viewholder = (CPStoresAdapter.NormalViewHolder) holder;

        if(lists.get(position).getState().equals("1")){

            viewholder.mTextView.setText(Html.fromHtml(lists.get(position).getName()+"<font color='#BDBDBD'>（显示）</font>"));
        }else{
            viewholder.mTextView.setText(Html.fromHtml(lists.get(position).getName()+"<font color='#BDBDBD'>（隐藏）</font>"));
        }

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return lists==null ? 0 : lists.size();
    }

}