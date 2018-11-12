package com.zmx.mian.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zmx.mian.R;
import com.zmx.mian.bean.CommodityPosition;
import com.zmx.mian.bean.CommodityPositionGD;
import com.zmx.mian.bean_dao.CPDao;
import com.zmx.mian.fragment.Fragment_pro_type;
import com.zmx.mian.model.OrderServer;
import com.zmx.mian.presenter.OrderPresenter;
import com.zmx.mian.util.MySharedPreferences;
import com.zmx.mian.util.Tools;
import com.zmx.mian.view.IAddGoodsView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-09-07 18:23
 * 类功能：添加商品
 */
public class AddGoodsActivity extends BaseActivity implements IAddGoodsView{

    private List<CommodityPosition> cp;
    private List<String> spinner_item;
    private Spinner g_fenlei;
    public ArrayAdapter<String> arr_adapter;
    private EditText g_name,g_price,g_vipprice;
    private Button submit;

    private int state_cp = 0;//判断是否选择了类别
    String type_id = "";
    public static final String action = "addGoods";

    private OrderPresenter op;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_goods;
    }

    @Override
    protected void initViews() {

        // 沉浸式状态栏
        setTitleColor(R.id.position_view);
        op = new OrderPresenter(this);
        BackButton(R.id.back_button);
        cp = (List<CommodityPosition>) getIntent().getSerializableExtra("cp");
        Log.e("类别","类别"+cp.size());

        g_name =  findViewById(R.id.add_name);
        g_price = findViewById(R.id.add_price);
        g_vipprice = findViewById(R.id.add_vip_price);

        spinner_item = new ArrayList<>();
        spinner_item.add("请选择类别");
        for(CommodityPosition p:cp){

            spinner_item.add(p.getName());

        }

        g_fenlei = findViewById(R.id.g_fenlei);
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_item);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        g_fenlei.setAdapter(arr_adapter);

        g_fenlei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e("选择的ID",""+i);
                state_cp = i;
                if(state_cp != 0){

                    type_id = cp.get(i-1).getType();
                    type_id = type_id.substring(type_id.indexOf("-") + 1);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        submit = findViewById(R.id.button_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //判断输入是否为空或者非法输入
                if(g_name.getText().toString().equals("")){

                    Toast.makeText(AddGoodsActivity.this,"名称不能为空！",Toast.LENGTH_LONG).show();

                }else if(g_price.getText().toString().equals("") || !Tools.isNumeric(g_price.getText().toString())){
                    Toast.makeText(AddGoodsActivity.this,"零售价不能为空或者非法输入！",Toast.LENGTH_LONG).show();
                }else if(g_vipprice.getText().toString().equals("") || !Tools.isNumeric(g_vipprice.getText().toString())){
                    Toast.makeText(AddGoodsActivity.this,"会员价价不能为空或者非法输入！",Toast.LENGTH_LONG).show();
                }else if(state_cp == 0){

                    Toast.makeText(AddGoodsActivity.this,"请选择类别！",Toast.LENGTH_LONG).show();

                }else{

                    //获取SharedPreferences对象，使用自定义类的方法来获取对象
                    String mid = MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.store_id,"");
                    String name =  MySharedPreferences.getInstance(mActivity).getString(MySharedPreferences.name,"");
                    op.addGoods(mid,name,type_id,g_price.getText().toString(),g_name.getText().toString(),g_vipprice.getText().toString());


                }

            }
        });


    }


    @Override
    public void getAddMessage(String msg) {

        Log.e("添加商品",""+msg);

        try {

            JSONObject jsonObject = new JSONObject(msg);
            String status = jsonObject.getString("status");
            if(status.equals("1")){

                Intent intent = new Intent(action);
                sendBroadcast(intent);

                Toast.makeText(this,jsonObject.getString("content"),Toast.LENGTH_LONG).show();
            }else{

                Toast.makeText(this,jsonObject.getString("content"),Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
