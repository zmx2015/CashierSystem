package com.zmx.mian.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.mian.R;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean.StockManagementDetailsBean;
import com.zmx.mian.bean_dao.StockManagementDetailsDao;
import com.zmx.mian.bean_dao.goodsDao;
import com.zmx.mian.dao.GoodsDao;
import com.zmx.mian.ui.SearchGoodsActivity;
import com.zmx.mian.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-23 12:50
 * 类功能：进货详情
 */

public class StockManagementDetailsAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<StockManagementDetailsBean> lists;
    private int state=0;
    private StockManagementDetailsDao smDao;
    private goodsDao gdao;

    public StockManagementDetailsAdapter(Context mContext,List<StockManagementDetailsBean> lists){
        smDao = new StockManagementDetailsDao();
        gdao = new goodsDao();
        this.mContext = mContext;
        this.lists = lists;
        mLayoutInflater = LayoutInflater.from(mContext);

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
    public View getView(int i, View view, ViewGroup viewGroup) {



        final ViewManHolder holder;
        if(view == null){

            view = mLayoutInflater.inflate(R.layout.stock_details_item,null);
            holder = new ViewManHolder(view);
            view.setTag(holder);

        }else{

            holder = (ViewManHolder) view.getTag();

        }

        final StockManagementDetailsBean bean = lists.get(i);//这个顺序也会导致复写错乱
        //清除焦点
        holder.edit_weight.clearFocus();
        holder.edit_price.clearFocus();
        holder.edit_note.clearFocus();

        holder.edit_weight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        holder.edit_price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //先清除之前的文本改变监听
        if (holder.edit_weight.getTag() instanceof TextWatcher) {
            holder.edit_weight.removeTextChangedListener((TextWatcher) (holder.edit_weight.getTag()));
        }
        if (holder.edit_price.getTag() instanceof TextWatcher) {
            holder.edit_price.removeTextChangedListener((TextWatcher) (holder.edit_price.getTag()));
        }
        if (holder.edit_note.getTag() instanceof TextWatcher) {
            holder.edit_note.removeTextChangedListener((TextWatcher) (holder.edit_note.getTag()));
        }

        //设置数据
        holder.edit_weight.setText(TextUtils.isEmpty(bean.g_weight)? "":bean.g_weight);
        holder.edit_price.setText(TextUtils.isEmpty(bean.g_price)? "":bean.g_price);
        holder.edit_note.setText(TextUtils.isEmpty(bean.g_note)? "":bean.g_note);

        holder.stock_total.setTag(bean);
        holder.stock_total.setText(Float.parseFloat(holder.edit_weight.getText().toString())*Float.parseFloat(holder.edit_price.getText().toString())+"");

        final TextWatcher oneWeightWatcher = new SimpeTextWather() {
            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s)) {

                    bean.setG_weight("0");
                    bean.setG_total("");

                } else {

                    if(Tools.isNumeric(s.toString())){

                        bean.setG_weight(s.toString());
                        holder.stock_total.setText(Float.parseFloat(s.toString())*Float.parseFloat(holder.edit_price.getText().toString())+"");
                        bean.setG_total(holder.stock_total.getText().toString());

                        ou.setUploadState();


                    }else{

                        bean.setG_weight("0");
                        bean.setG_total("0");
                        Toast.makeText(mContext,"非法输入",Toast.LENGTH_LONG).show();

                    }

                    smDao.UpdateStock(bean);


                }


            }
        };
        final TextWatcher onePriceWatcher = new SimpeTextWather() {
            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s)) {

                    bean.setG_price("0");
                    bean.setG_total("0");

                } else {


                    if(Tools.isNumeric(s.toString())){

                        bean.setG_price(s.toString());
                        holder.stock_total.setText(Float.parseFloat(holder.edit_weight.getText().toString())*Float.parseFloat(s.toString())+"");
                        bean.setG_total(holder.stock_total.getText().toString());
                        ou.setUploadState();

                    }else{

                        bean.setG_price("0");
                        bean.setG_total("0");
                        Toast.makeText(mContext,"非法输入",Toast.LENGTH_LONG).show();

                    }

                    smDao.UpdateStock(bean);

                }

            }
        };
        final TextWatcher oneNoteWatcher = new SimpeTextWather() {
            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s)) {

                    bean.setG_note(null);

                } else {

                    bean.setG_note(s.toString());
                    ou.setUploadState();
                    smDao.UpdateStock(bean);

                }

            }
        };
        //吧监听设置到不同的EditText上
        holder.edit_weight.addTextChangedListener(oneWeightWatcher);
        holder.edit_weight.setTag(oneWeightWatcher);
        holder.edit_price.addTextChangedListener(onePriceWatcher);
        holder.edit_price.setTag(onePriceWatcher);
        holder.edit_note.addTextChangedListener(oneNoteWatcher);
        holder.edit_note.setTag(oneNoteWatcher);

        holder.goods_name.setTag(bean);//设置标签

        if(bean.getG_name() == ""){

            holder.goods_name.setText("未选择");

        }else{

            holder.goods_name.setText(bean.getG_name());

        }

        holder.goods_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adab(holder.goods_name);

            }
        });

        //判断是什么模式
        if(state == 1){

            holder.layout.setVisibility(View.GONE);

        }else{

            holder.layout.setVisibility(View.VISIBLE);

        }

        holder.stock_layout.setTag(bean);//设置标签
        //记录颜色

        if(bean.getG_color().equals("1")){
            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button1));

        }else if(bean.getG_color().equals("2")){
            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button2));

        }else if(bean.getG_color().equals("3")){
            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button3));

        }else if(bean.getG_color().equals("4")){
            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button4));

        }else if(bean.getG_color().equals("5")){
            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.button5));

        }else{
            holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(R.color.grey_200));

        }

        return view;
    }
    class ViewManHolder{

        TextView goods_name,stock_total;

        EditText edit_weight,edit_price,edit_note;
        LinearLayout layout,stock_layout;

        public ViewManHolder(View itemView) {

            goods_name = itemView.findViewById(R.id.goods_name);
            edit_note = itemView.findViewById(R.id.edit_note);
            edit_weight = itemView.findViewById(R.id.edit_weight);
            edit_price = itemView.findViewById(R.id.edit_price);
            layout = itemView.findViewById(R.id.stock_item_layout);
            stock_layout = itemView.findViewById(R.id.stock_layout);
            stock_total = itemView.findViewById(R.id.stock_total);

        }
    }

    public void update(int index,ListView listview,int color){

        //得到第一个可见item项的位置
        int visiblePosition = listview.getFirstVisiblePosition();
        //得到指定位置的视图，对listview的缓存机制不清楚的可以去了解下
        View view = listview.getChildAt(index - visiblePosition);
        ViewManHolder holder = (ViewManHolder) view.getTag();
        holder.stock_layout = view.findViewById(R.id.stock_layout);
        setData(holder,index,color);

    }
    private void setData(ViewManHolder holder,int index,int color){

        StockManagementDetailsBean sb = lists.get(index);
        holder.stock_layout.setBackgroundColor(mContext.getResources().getColor(color));

    }

    public void setState(int state){
        this.state = state;
    }

//    //弹出框
    public void adab(final TextView nameGoods) {

        final List<Goods> g = gdao.SelectDimGoods("");
        final SearchGoodsAdapter adapter = new SearchGoodsAdapter(mContext, g);

        final PtrClassicFrameLayout mPtrFrame;
        RecyclerAdapterWithHF mAdapter;

        //修改商品界面属性
        final Dialog search_dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View search_goods = LayoutInflater.from(mContext).inflate(R.layout.search_goods, null);//选择性别的view
        //将布局设置给Dialog
        search_dialog.setContentView(search_goods);
        //获取当前Activity所在的窗体
        Window dialogWindow = search_dialog.getWindow();

        RecyclerView rv = search_goods.findViewById(R.id.search_view);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rv.setAdapter(mAdapter);
        rv.setNestedScrollingEnabled(false);
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                //将用户更改的商品数量更新到服务器
                StockManagementDetailsBean b = (StockManagementDetailsBean) nameGoods.getTag();
                nameGoods.setText(g.get(position).getG_name());
                b.setG_name(g.get(position).getG_name());//更改保存
                b.setG_id(g.get(position).getG_id());
                smDao.UpdateStock(b);
                ou.setUploadState();

                search_dialog.dismiss();

            }
        });
        mPtrFrame = search_goods.findViewById(R.id.rotate_header_list_view_frame);
//下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
//下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
// default is false
        mPtrFrame.setPullToRefresh(false);
// default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return false;
            }
        });

        final EditText et = search_dialog.findViewById(R.id.goods_search_edit);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                Log.e("sss", "输入文本之前的状态");


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("输入文字中的状态", "输入文字中的状态，count是输入字符数");
                Log.e("输入文字中的状态", "sss");

            }

            @Override
            public void afterTextChanged(Editable editable) {

                g.clear();
                //查询更新
                if (et.getText().toString() != null || !et.getText().toString().equals("")) {

                    List<Goods> gs = gdao.SelectDimGoods(et.getText().toString());

                    for (Goods gg : gs) {

                        g.add(gg);

                    }
                    adapter.notifyDataSetChanged();
                    Log.e("查询到的商品数量=", "" + g.size());

                }


            }
        });


        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;//设置Dialog距离底部的距离

//// 以下这两句是为了保证按钮可以水平满屏
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
// 设置显示位置
        search_dialog.onWindowAttributesChanged(lp);
//       将属性设置给窗体
        search_dialog.show();//显示对话框

    }

    public abstract class SimpeTextWather implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }


    public OnClickUpload ou;

    public void setOnClickUpload(OnClickUpload ou){
        this.ou = ou;
    }

    public interface OnClickUpload{

        void setUploadState();//修改上传状态

    }

}

