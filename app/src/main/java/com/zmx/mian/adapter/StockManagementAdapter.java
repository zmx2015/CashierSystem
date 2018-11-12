package com.zmx.mian.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.bean.StoresMessage;
import com.zmx.mian.bean_dao.StockManagementDetailsDao;
import com.zmx.mian.dao.StockManagementDetailsBeanDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-19 14:38
 * 类功能：进货列表
 */

public class StockManagementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<StockManagementBean> mTitle;

    public StockManagementAdapter(Context context,List<StockManagementBean> title){

        mContext = context;
        mTitle = title;
        mLayoutInflater=LayoutInflater.from(context);

    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView textView1,textView3,textView4;
        public NormalViewHolder(View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.textView1);
            textView3=itemView.findViewById(R.id.textView3);
            textView4=itemView.findViewById(R.id.textView4);
        }
    }
    //在该方法中我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    @Override
    public StockManagementAdapter.NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StockManagementAdapter.NormalViewHolder(mLayoutInflater.inflate(R.layout.stock_management_adapter,parent,false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StockManagementAdapter.NormalViewHolder viewholder = (StockManagementAdapter.NormalViewHolder) holder;


        viewholder.textView1.setText(mTitle.get(position).getRh_time());
        viewholder.textView3.setText(mTitle.get(position).getTotal());

        if (mTitle.get(position).getSm_state().equals("1")){

            viewholder.textView4.setText(Html.fromHtml("已上传"));
        }else{

            viewholder.textView4.setText(Html.fromHtml("<font color='red'>未上传</font>"));
        }



    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return mTitle==null ? 0 : mTitle.size();
    }
}
