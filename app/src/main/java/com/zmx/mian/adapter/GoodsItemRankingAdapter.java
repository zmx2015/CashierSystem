package com.zmx.mian.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.GoodsItemRankingBean;
import com.zmx.mian.bean.MainOrder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-08-30 13:35
 * 类功能：商品排行榜的适配器
 */

public class GoodsItemRankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<GoodsItemRankingBean> lists;

    public GoodsItemRankingAdapter(Context context, List<GoodsItemRankingBean> lists) {
        mContext = context;
        this.lists = lists;
        mLayoutInflater = LayoutInflater.from(context);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView goods_item_name, goods_item_zje, goods_item_zzl, goods_item_gmcs, number;
        LinearLayout layout;

        public NormalViewHolder(View itemView) {
            super(itemView);
            goods_item_name = (TextView) itemView.findViewById(R.id.goods_item_name);
            goods_item_zje = (TextView) itemView.findViewById(R.id.goods_item_zje);
            goods_item_zzl = (TextView) itemView.findViewById(R.id.goods_item_zzl);
            goods_item_gmcs = (TextView) itemView.findViewById(R.id.goods_item_gmcs);
            number = itemView.findViewById(R.id.number);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    //在该方法中我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    @Override
    public GoodsItemRankingAdapter.NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsItemRankingAdapter.NormalViewHolder(mLayoutInflater.inflate(R.layout.goods_item_ranking_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GoodsItemRankingAdapter.NormalViewHolder viewholder = (GoodsItemRankingAdapter.NormalViewHolder) holder;
        viewholder.goods_item_name.setText(lists.get(position).getName());
        viewholder.goods_item_zje.setText(lists.get(position).getzMoney());
        viewholder.number.setText((position + 1) + "");
        float mj = Float.parseFloat((lists.get(position).getzWeight())) * 1000 / 500;
        viewholder.goods_item_zzl.setText(mj + "");
        viewholder.goods_item_gmcs.setText(lists.get(position).getzNum() + "次/" + lists.get(position).getNum() + "个");

//        if (position % 2 == 0) {
//            viewholder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.temple_header_color));
//        } else {
//            viewholder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg));
//
//        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }


}
