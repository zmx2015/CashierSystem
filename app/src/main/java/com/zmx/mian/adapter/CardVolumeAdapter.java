package com.zmx.mian.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.CardVolumeBean;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-21 17:04
 * 类功能：卡卷的适配器
 */

public class CardVolumeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<CardVolumeBean> list;
    public CardVolumeAdapter(Context context,List<CardVolumeBean> list){
        mContext=context;
        this.list=list;
        mLayoutInflater=LayoutInflater.from(context);
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView money_text,conditions_text,describe;
        public NormalViewHolder(View itemView) {
            super(itemView);
            money_text=itemView.findViewById(R.id.money_text);
            conditions_text=itemView.findViewById(R.id.conditions_text);
            describe=itemView.findViewById(R.id.describe);
        }
    }
    //在该方法中我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    @Override
    public CardVolumeAdapter.NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardVolumeAdapter.NormalViewHolder(mLayoutInflater.inflate(R.layout.card_volume_adapter,parent,false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardVolumeAdapter.NormalViewHolder viewholder = (CardVolumeAdapter.NormalViewHolder) holder;
        viewholder.money_text.setText(list.get(position).getQuota()+"");

        if(!list.get(position).getContent().equals("")){

            viewholder.describe.setText(("("+list.get(position).getContent()+")"));
        }else{

            viewholder.describe.setText("");
        }

        viewholder.conditions_text.setText("满"+list.get(position).getTerm()+"元可用，有效天数"+list.get(position).getDays()+"天");
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }
}