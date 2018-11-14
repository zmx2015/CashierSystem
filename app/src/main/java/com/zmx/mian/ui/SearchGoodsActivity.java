package com.zmx.mian.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.zmx.mian.R;
import com.zmx.mian.adapter.SearchGoodsAdapter;
import com.zmx.mian.bean.Goods;
import com.zmx.mian.bean_dao.goodsDao;
import com.zmx.mian.fragment.Fragment_pro_type;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-10 14:56
 * 类功能：查找某个商品
 */
public class SearchGoodsActivity extends BaseActivity {

    private List<Goods> lists;
    private SearchGoodsAdapter adapter;
    private RecyclerView rv;
    private PtrClassicFrameLayout mPtrFrame;
    private RecyclerAdapterWithHF mAdapter;
    private EditText et;
    private goodsDao gdao;
    private ImageView no_data;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_goods;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        gdao = new goodsDao();
        rv = findViewById(R.id.search_view);
        no_data = findViewById(R.id.no_data);
        lists = gdao.SelectDimGoods("");
        ;
        adapter = new SearchGoodsAdapter(this, lists);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rv.setAdapter(mAdapter);
        rv.setNestedScrollingEnabled(false);

        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {

                // 通过Intent传递对象给Service
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods", lists.get(position));
                intent.setClass(mActivity, GoodsDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        mPtrFrame = findViewById(R.id.rotate_header_list_view_frame);
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

        et = findViewById(R.id.goods_search_edit);
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

                lists.clear();
                //查询更新
                if (et.getText().toString() != null || !et.getText().toString().equals("")) {

                    List<Goods> gs = gdao.SelectDimGoods(et.getText().toString());

                    for (Goods gg : gs) {

                        lists.add(gg);

                    }

                    Log.e("查询到的商品数量=", "" + lists.size());

                }

                handler.sendEmptyMessage(1);

            }
        });

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    Log.e("ssffasd", "fda=" + lists.size());

                    if (lists.size() > 0) {
                        no_data.setVisibility(View.GONE);
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();


                    break;

            }

        }
    };

}
