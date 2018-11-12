package com.zmx.mian.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.mian.R;
import com.zmx.mian.bean.ViceOrder;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 23:51
 * 类功能：商品详情适配器
 */

public class OrderDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<ViceOrder> lists;
    private LayoutInflater inflater;


    public OrderDetailsAdapter(Context context,List<ViceOrder> lists){

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

    @Override
    public View getView(final int position, View v, ViewGroup parent) {

        ViewHolder holder = null;
        if(v == null){

            holder = new ViewHolder();
            v = inflater.inflate(R.layout.order_details_item,null);
            holder.name = v.findViewById(R.id.order_details_item_name);
            holder.dj = v.findViewById(R.id.order_details_item_dj);
            holder.weight = v.findViewById(R.id.order_details_item_weight);
            holder.zj = v.findViewById(R.id.order_details_item_zj);
            v.setTag(holder);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        holder.name.setText(""+lists.get(position).getName());
        holder.dj.setText(""+lists.get(position).getPrice());
        holder.weight.setText(""+lists.get(position).getWeight());
        holder.zj.setText(""+lists.get(position).getSubtotal());

        return v;
    }

    public class ViewHolder{
        TextView name,dj,weight,zj;
    }



}