package com.zmx.mian.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.bean.StockManagementBean;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.bean_dao.StockManagementDao;

import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-20 20:17
 * 类功能：采购清单适配器
 */

public class ProcurementDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<StockManagementDetailsBean> lists;
    private int state = 0;

    public ProcurementDetailAdapter(Context mContext, List<StockManagementDetailsBean> lists) {

        this.mContext = mContext;
        this.lists = lists;
        mLayoutInflater = LayoutInflater.from(mContext);

    }


    public void setState(int state) {
        this.state = state;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4;
        EditText note;
        LinearLayout layout, stock_layout;


        public NormalViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            note = itemView.findViewById(R.id.note);
            layout = itemView.findViewById(R.id.title_name);
            stock_layout = itemView.findViewById(R.id.stock_layout);

        }
    }
    //在该方法中我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.procurement_details_adapter,parent,false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int i) {
        final NormalViewHolder holder = (NormalViewHolder) holders;

        final StockManagementDetailsBean sdb = lists.get(i);

        //清除焦点
        holder.note.clearFocus();
        if (holder.note.getTag() instanceof TextWatcher) {
            holder.note.removeTextChangedListener((TextWatcher) (holder.note.getTag()));
        }
        holder.note.setText(TextUtils.isEmpty(sdb.getG_note()) ? "" : sdb.getG_note());
        if (sdb.getG_note().equals("")) {

            holder.note.setHint(sdb.getG_name());

        }

        holder.note.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                    // 此处为失去焦点时的处理内容
                    String s = holder.note.getText().toString();

                    //判断输入框内容是否有改变
                    if(!sdb.getG_note().equals(s)){

                        sdb.setG_note(s);
                        ou.setUploadState();
                    }

                }
            }
        });

        holder.note.setTag(i);

        holder.textView1.setText(sdb.getG_name());

        //判断是否是重量或者件数
        if (sdb.getUnita().equals("1") || sdb.getUnita().equals(null)) {

            holder.textView2.setText(sdb.getG_nb()+"件");

        } else {

            holder.textView2.setText(sdb.getG_weight()+"斤");

        }

        holder.textView3.setText(sdb.getG_price());
        holder.textView4.setText(sdb.getG_total());
        //判断是什么模式
        if (state == 1) {

            holder.layout.setVisibility(View.GONE);
            holder.note.setVisibility(View.VISIBLE);

        } else {

            holder.layout.setVisibility(View.VISIBLE);
            holder.note.setVisibility(View.GONE);

        }

        holder.stock_layout.setTag(sdb);//设置标签
        //记录颜色
        if (sdb.getG_color().equals("1")) {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button1));

        } else if (sdb.getG_color().equals("2")) {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button2));

        } else if (sdb.getG_color().equals("3")) {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button3));

        } else if (sdb.getG_color().equals("4")) {
            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button4));

        } else if (sdb.getG_color().equals("5")) {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.grey_200));

        } else {

            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.grey_200));

        }
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return lists==null ? 0 : lists.size();
    }






    public ProcurementDetailAdapter.OnClickUpload ou;

    public void setOnClickUpload(ProcurementDetailAdapter.OnClickUpload ou) {
        this.ou = ou;
    }

    public interface OnClickUpload {

        void setUploadState();//修改上传状态

    }

}
