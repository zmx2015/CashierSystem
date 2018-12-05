package com.zmx.mian.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.MainOrder;
import com.zmx.mian.bean.ViceOrder;
import com.zmx.mian.util.ListViewUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-06-28 0:40
 * 类功能：订单列表的适配器
 */

public class OrderDataAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private OrderDetailsAdapter adapter;
    private List<ViceOrder> list;


    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<MainOrder> mo;
    public OrderDataAdapter(Context context,List<MainOrder> mo){
        mContext=context;
        this.mo=mo;
        mLayoutInflater=LayoutInflater.from(context);
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView order_number,order_zj,order_zhj,order_yh,order_ss,order_zffs,order_zt,order_time,order_mode;
        CardView mCardView;
        ListView listView;
        public NormalViewHolder(View itemView) {
            super(itemView);
            order_number=(TextView)itemView.findViewById(R.id.order_number);
            order_zj=(TextView)itemView.findViewById(R.id.order_zj);
            order_zhj=(TextView)itemView.findViewById(R.id.order_zhj);
            order_yh=(TextView)itemView.findViewById(R.id.order_yh);
            order_ss=(TextView)itemView.findViewById(R.id.order_ss);
            order_zffs=(TextView)itemView.findViewById(R.id.order_zffs);
            order_zt=(TextView)itemView.findViewById(R.id.order_zt);
            order_mode=(TextView)itemView.findViewById(R.id.order_mode);
            order_time=(TextView)itemView.findViewById(R.id.order_time);
            mCardView=(CardView)itemView.findViewById(R.id.cv_item);
            listView = itemView.findViewById(R.id.listview);
        }
    }
    //在该方法中我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    @Override
    public OrderDataAdapter.NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderDataAdapter.NormalViewHolder(mLayoutInflater.inflate(R.layout.order_data_item,parent,false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderDataAdapter.NormalViewHolder viewholder = (OrderDataAdapter.NormalViewHolder) holder;
        viewholder.order_number.setText("订单编号："+mo.get(position).getOrder());
        viewholder.order_zj.setText(Html.fromHtml("总金额：<font color='#ff0000'>"+mo.get(position).getTotal()+"元</font>"));
        viewholder.order_zhj.setText(Html.fromHtml("折后价：<font color='#ff0000'>"+(Float.parseFloat(mo.get(position).getTotal())-Float.parseFloat(mo.get(position).getDiscount()))+"元</font>"));
        viewholder.order_yh.setText(Html.fromHtml("优惠：<font color='#ff0000'>"+mo.get(position).getDiscount()+"元</font>"));
        viewholder.order_ss.setText(Html.fromHtml("实收：<font color='#ff0000'>"+mo.get(position).getReceipts()+"元</font>"));

        if(mo.get(position).getPayment() == 1){
            viewholder.order_zffs.setText("支付方式：现金");
        }else if(mo.get(position).getPayment() == 2){
            viewholder.order_zffs.setText("支付方式：移动支付");
        }else{
            viewholder.order_zffs.setText("支付方式：会员支付");
        }


        if(mo.get(position).getState() == 3){

            viewholder.order_zt.setText(Html.fromHtml("状态：<font color='#ff0000'>进行中</font>"));

        }else if(mo.get(position).getState() == 2){

            viewholder.order_zt.setText(Html.fromHtml("状态：<font color='#ff0000'>进行中</font>"));

        }else if(mo.get(position).getState() == 1){

            viewholder.order_zt.setText("状态：完成");

        }else{

            viewholder.order_zt.setText("状态：取消");
        }

        if(mo.get(position).getMo_classify().equals("1")){

            viewholder.order_mode.setText("订单类型：商城订单");

        }else if(mo.get(position).getMo_classify().equals("2")){

            viewholder.order_mode.setText("订单类型：商城订单");

        }else{

            viewholder.order_mode.setText("订单类型：门店订单");
        }

        viewholder.order_time.setText("时间："+refFormatNowDate(mo.get(position).getBuytime()));

        list = mo.get(position).getLists();
        adapter = new OrderDetailsAdapter(mContext,list);

        viewholder.listView.setAdapter(adapter);
        ListViewUtility.setListViewHeightBasedOnChildren(viewholder.listView);


    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return mo==null ? 0 : mo.size();
    }


    /**
     * php和java时间戳转换
     *
     * @param time
     * @return
     */
    public static String refFormatNowDate(String time) {

        time = time + "000";
        Date nowTime = new Date(Long.parseLong(time));
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }


}
