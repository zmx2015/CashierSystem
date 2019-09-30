package com.zmx.mian.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zmx.mian.R;

import java.text.NumberFormat;

/**
 * 开发人员：曾敏祥
 * 开发时间：2019-02-20 18:06
 * 类功能：预算价格
 */
public class BudgetPriceActivity extends BaseActivity {

    private EditText editText1,editText2,editText3,editText4;
    private TextView text_view,profit_text,proportion_text;
    private Button calculate;

    private float purchase_price=0;//拿货价
    private float weight=0;//总重
    private float average_price=0;//均价
    private float retail_price=0;//零售价
    private float wastage=0;//耗损
    private float profit=0;//利润
    private float proportion = 0;//比例

    @Override
    protected int getLayoutId() {
        return R.layout.activity_budget_price;
    }

    @Override
    protected void initViews() {

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        text_view = findViewById(R.id.text_view);
        profit_text = findViewById(R.id.profit_text);
        proportion_text = findViewById(R.id.proportion_text);

        //为EditText设置监听，注意监听类型为TextWatcher
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(s.toString())){

                    purchase_price = Float.parseFloat(editText1.getText().toString());//总价

                    if(!TextUtils.isEmpty(editText2.getText().toString())){

                        weight = Float.parseFloat(editText2.getText().toString());//重量

                    }
                    if(!TextUtils.isEmpty(editText3.getText().toString())){

                        retail_price = Float.parseFloat(editText3.getText().toString());//零售价

                    }

                    if(!TextUtils.isEmpty(editText4.getText().toString())){

                        wastage = Float.parseFloat(editText4.getText().toString());//耗损

                    }

                    average_price = purchase_price/weight;//均价
                    profit = retail_price*(weight-wastage)-purchase_price;//利润
                    proportion  = profit/purchase_price;


                    text_view.setText("拿货均价："+average_price+"元");
                    profit_text.setText("预算利润："+profit+"元");

                    // 创建一个数值格式化对象
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(2);

                    proportion_text.setText("利润比例："+numberFormat.format(profit/purchase_price*100)+"%");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(s.toString())){

                    weight = Float.parseFloat(editText2.getText().toString());//重量

                    if(!TextUtils.isEmpty(editText1.getText().toString())){

                        purchase_price = Float.parseFloat(editText1.getText().toString());//总价

                    }

                    if(!TextUtils.isEmpty(editText3.getText().toString())){

                        retail_price = Float.parseFloat(editText3.getText().toString());//零售价
                    }

                    if(!TextUtils.isEmpty(editText4.getText().toString())){

                        wastage = Float.parseFloat(editText4.getText().toString());//耗损
                    }

                    average_price = purchase_price/weight;//均价
                    profit = retail_price*(weight-wastage)-purchase_price;//利润
                    proportion = profit/purchase_price;


                    text_view.setText("拿货均价："+average_price+"元");
                    profit_text.setText("预算利润："+profit+"元");

                    // 创建一个数值格式化对象
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(2);

                    proportion_text.setText("利润比例："+numberFormat.format(profit/purchase_price*100)+"%");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(s.toString())){

                    retail_price = Float.parseFloat(editText3.getText().toString());//零售价
                    if(!TextUtils.isEmpty(editText1.getText().toString())){

                        purchase_price = Float.parseFloat(editText1.getText().toString());//总价

                    }

                    if(!TextUtils.isEmpty(editText2.getText().toString())){

                        weight = Float.parseFloat(editText2.getText().toString());//重量
                    }

                    if(!TextUtils.isEmpty(editText4.getText().toString())){

                        wastage = Float.parseFloat(editText4.getText().toString());//耗损
                    }

                    average_price = purchase_price/weight;//均价
                    profit = retail_price*(weight-wastage)-purchase_price;//利润
                    proportion = profit/purchase_price;


                    text_view.setText("拿货均价："+average_price+"元");
                    profit_text.setText("预算利润："+profit+"元");

                    // 创建一个数值格式化对象
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(2);

                    proportion_text.setText("利润比例："+numberFormat.format(profit/purchase_price*100)+"%");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!TextUtils.isEmpty(s.toString())){

                    wastage = Float.parseFloat(editText4.getText().toString());//耗损

                    if(!TextUtils.isEmpty(editText1.getText().toString())){

                        purchase_price = Float.parseFloat(editText1.getText().toString());//总价

                    }
                    if(!TextUtils.isEmpty(editText2.getText().toString())){

                        weight = Float.parseFloat(editText2.getText().toString());//重量
                    }
                    if(!TextUtils.isEmpty(editText3.getText().toString())){

                        retail_price = Float.parseFloat(editText3.getText().toString());//零售价
                    }

                    average_price = purchase_price/weight;//均价
                    profit = retail_price*(weight-wastage)-purchase_price;//利润
                    proportion = profit/purchase_price;

                    text_view.setText("拿货均价："+average_price+"元");
                    profit_text.setText("预算利润："+profit+"元");

                    // 创建一个数值格式化对象
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(2);

                    proportion_text.setText("利润比例："+numberFormat.format(profit/purchase_price*100)+"%");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        calculate = findViewById(R.id.calculate);
//        calculate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(TextUtils.isEmpty(editText1.getText())){
//
//                    Toast("请输入总价格");
//
//                }else if(TextUtils.isEmpty(editText2.getText())){
//
//                    Toast("请输入重量");
//
//                }else if(TextUtils.isEmpty(editText3.getText())){
//
//                    Toast("请输入零售价");
//
//                }else{
//
//                    purchase_price = Float.parseFloat(editText1.getText().toString());//总价
//                    weight = Float.parseFloat(editText2.getText().toString());//重量
//                    retail_price = Float.parseFloat(editText3.getText().toString());//零售价
//
//                    if(!TextUtils.isEmpty(editText4.getText().toString())){
//
//                        wastage = Float.parseFloat(editText4.getText().toString());//耗损
//                    }
//
//                    average_price = purchase_price/weight;//均价
//                    profit = retail_price*(weight-wastage)-purchase_price;//利润
//                    proportion = profit/purchase_price;
//
//
//                    text_view.setText("拿货均价："+average_price+"元");
//                    profit_text.setText("预算利润："+profit+"元");
//
//                    // 创建一个数值格式化对象
//                    NumberFormat numberFormat = NumberFormat.getInstance();
//                    // 设置精确到小数点后2位
//                    numberFormat.setMaximumFractionDigits(2);
//
//                    proportion_text.setText("利润比例："+numberFormat.format(profit/purchase_price*100)+"%");
////                    float f = (zj+zq)/(wg-(wg*(hs/100)));
////                    retail_price.setText("零售价："+f+"元");
//
//
//                }
//
//            }
//        });

    }


}
