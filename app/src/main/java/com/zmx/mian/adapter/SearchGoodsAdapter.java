package com.zmx.mian.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zmx.mian.R;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.ui.util.GlideCircleTransform;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-10 11:29
 * 类功能：
 */

public class SearchGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Goods> lists;

    public SearchGoodsAdapter(Context context, List<Goods> lists) {
        mContext = context;
        this.lists = lists;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchGoodsAdapter.MyViewHolder(mLayoutInflater.inflate(R.layout.search_goods_adapter, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SearchGoodsAdapter.MyViewHolder viewholder = (SearchGoodsAdapter.MyViewHolder) holder;

        viewholder.goodsName.setText(lists.get(position).getG_name());
        viewholder.price_text.setText("￥"+lists.get(position).getG_price());
        Glide.with(mContext).load("http://www.yiyuangy.com/uploads/goods/"+lists.get(position).getG_img()).transform(new GlideCircleTransform(mContext)).error(R.mipmap.logo).into(viewholder.goods_image);


    }

    @Override
    public int getItemCount() {

        return lists == null ? 0 : lists.size();

    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView goodsName,price_text;
        ImageView goods_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            goodsName = itemView.findViewById(R.id.goods_name);
            price_text = itemView.findViewById(R.id.price_text);
            goods_image = itemView.findViewById(R.id.goods_image);
        }
    }


}
