package com.zmx.mian.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zmx.mian.R;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.ui.util.GlideCircleTransform;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-18 20:51
 * 类功能：前台收银商品列表适配器
 */
public class CCDetailAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<ViceOrder> lists;
    private Handler handler;

    public CCDetailAdapter(Context context, List<ViceOrder> lists, Handler handler){
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return lists.size();
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
    public View getView(final int pos, View view, ViewGroup viewGroup) {

        final HolderView holder;
        if(view == null){

            view = inflater.inflate(R.layout.c_c_detail_adapter,null);
            holder = new HolderView(view);
            view.setTag(holder);

        }else{

            holder = (HolderView) view.getTag();
        }

        ViceOrder vo = lists.get(pos);

        Glide.with(context).load("http://www.yiyuangy.com/uploads/goods/"+vo.getImg()).transform(new GlideCircleTransform(context)).error(R.mipmap.logo).into(holder.goods_image);

        holder.goods_name.setText(vo.getName());
        holder.goods_price.setText("单价："+vo.getPrice());
        holder.goods_total.setText("小计："+vo.getSubtotal());
        holder.goods_weight.setText(vo.getWeight()+" kg/个");

        holder.delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("删除商品");
                builder.setMessage("确定删除该商品？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        lists.remove(pos);
                        handler.sendEmptyMessage(1);
                        notifyData();

                    }
                });
                builder.create().show();

            }
        });

        return view;
    }

    public void notifyData(){
        this.notifyDataSetChanged();
    }

    public class HolderView{

        private ImageView goods_image,delete_image;
        private TextView goods_name,goods_price,goods_total,goods_weight;

        public HolderView(View itemView) {

            goods_image = itemView.findViewById(R.id.goods_image);
            delete_image = itemView.findViewById(R.id.delete_image);
            goods_name = itemView.findViewById(R.id.goods_name);
            goods_price = itemView.findViewById(R.id.goods_price);
            goods_total = itemView.findViewById(R.id.goods_total);
            goods_weight = itemView.findViewById(R.id.goods_weight);

        }

    }

}
