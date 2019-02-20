package com.zmx.mian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.ProcurementAnalysis;
import com.zmx.mian.util.Tools;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-12-09 21:36
 * 类功能：采购分析
 */
public class ProcurementAnalysisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ProcurementAnalysis> mTitle;

    public ProcurementAnalysisAdapter(Context context,List<ProcurementAnalysis> title){

        mContext = context;
        mTitle = title;
        mLayoutInflater=LayoutInflater.from(context);

    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView textView1,textView2,textView3,textView4,textView5,textView6;
        public NormalViewHolder(View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.textView1);
            textView2=itemView.findViewById(R.id.textView2);
            textView3=itemView.findViewById(R.id.textView3);
            textView4=itemView.findViewById(R.id.textView4);
            textView5=itemView.findViewById(R.id.textView5);
            textView6=itemView.findViewById(R.id.textView6);
        }
    }
    //在该方法中我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    @Override
    public ProcurementAnalysisAdapter.NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProcurementAnalysisAdapter.NormalViewHolder(mLayoutInflater.inflate(R.layout.adapter_procurement_analysis,parent,false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProcurementAnalysisAdapter.NormalViewHolder viewholder = (ProcurementAnalysisAdapter.NormalViewHolder) holder;
        viewholder.textView1.setText((position+1)+"");
        viewholder.textView2.setText(mTitle.get(position).getName());
        viewholder.textView3.setText(Tools.priceResult(mTitle.get(position).getAll_total())+"");
        viewholder.textView4.setText(Tools.priceResult(mTitle.get(position).getTotal())+"");
        viewholder.textView5.setText(Tools.priceResult(mTitle.get(position).getWeight())+"");
        viewholder.textView6.setText(Tools.priceResult(mTitle.get(position).getTotal()-mTitle.get(position).getAll_total())+"");
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return mTitle==null ? 0 : mTitle.size();
    }
}
