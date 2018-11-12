package com.zmx.mian.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttpClient封装,基于Okhttp-3.4.2,Okio-1.11.0 封装
 *
 * Created by tongzhenggang@126.com
 */
public class OkHttp3ClientManager {

    private String TAG = this.getClass().getName();
    private static OkHttp3ClientManager mInstance;
    private OkHttpClient mOkHttpClient;

    private String data="";

    private OkHttp3ClientManager() {
        mOkHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS) // 读取超时
                .connectTimeout(10, TimeUnit.SECONDS) // 连接超时
                .writeTimeout(60, TimeUnit.SECONDS) // 写入超时
                .build();
    }

    public static OkHttp3ClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttp3ClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttp3ClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * post请求参数
     *
     * @param BodyParams
     * @return
     */
    private RequestBody SetRequestBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        // 创建请求的参数body
        FormBody.Builder builder = new FormBody.Builder();

        // 遍历key
        if (null != BodyParams) {
            for (Map.Entry<String, String> entry : BodyParams.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

                builder.add(entry.getKey(), entry.getValue().toString());

            }
        }

        body = builder.build();
        return body;
    }


    /**
     * get方法连接拼加参数
     *
     * @param mapParams
     * @return
     */
    private String setUrlParams(Map<String, String> mapParams) {
        String strParams = "";
        if (mapParams != null) {
            Iterator<String> iterator = mapParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                strParams += "&" + key + "=" + mapParams.get(key);
            }
        }

        return strParams;
    }

    /**
     * POST方法获取实体bean
     *
     * @param reqUrl
     * @param map
     * @param mHandler
     * @param rspClass
     */
    public void postBeanExecute(String reqUrl, Map<String, String> map, final Handler mHandler,
                                final Class<?> rspClass) {
        RequestBody body = SetRequestBody(map);
        Request request = new Request.Builder().url(reqUrl).post(body).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                String Result = arg1.body().string();
//          Result = PUB.decodeBase64(Result);
                Log.d(TAG, "[postBeanExecute]Call===" + arg1.code() + "Response===" + Result);

                Message mess = mHandler.obtainMessage();
                if (arg1.code() == 200) {
                    try {
                        // 转换返回结果信息给BEAN
                        mess.obj = new Gson().fromJson(Result, rspClass);
                    } catch (Exception e) {
                        mess.what = 404;
                        mess.obj = "HTTP数据异常-0002";
                    }
                } else {
                    mess.what = arg1.code();
                    mess.obj = "HTTP状态异常-" + arg1.code();
                }
                mHandler.sendMessage(mess);
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                Log.d(TAG, "[onFailure]Call===" + arg0 + "IOException===" + arg1.toString());

                Message mess = mHandler.obtainMessage();
                mess.what = 404;
                mess.obj = "HTTP通讯错误-0001";
                mHandler.sendMessage(mess);
            }
        });
    }

    /**
     * Get方法获取实体bean
     *
     * @param reqUrl
     * @param map
     * @param mHandler
     * @param rspClass
     */
    public void getBeanExecute(String reqUrl, Map<String, String> map, final Handler mHandler,
                               final Class<?> rspClass) {
        String UrlParams = setUrlParams(map);
        String URL = reqUrl + "?" + UrlParams;
        Log.d(TAG, "[getBeanExecute]URL===" + URL);

        Request request = new Request.Builder().url(URL).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                String Result = arg1.body().string();
//          Result = PUB.decodeBase64(Result);
                Log.d(TAG, "[onResponse]Call===" + arg1.code() + "Response===" + Result);

                Message mess = mHandler.obtainMessage();
                if (arg1.code() == 200) {
                    try {
                        // 转换返回结果信息给BEAN
                        mess.obj = new Gson().fromJson(Result, rspClass);
                    } catch (Exception e) {
                        mess.what = 404;
                        mess.obj = "HTTP数据异常-0002";
                    }
                } else {
                    mess.what = arg1.code();
                    mess.obj = "HTTP状态异常-" + arg1.code();
                }
                mHandler.sendMessage(mess);
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                Log.d(TAG, "[onFailure]Call===" + arg0 + "IOException===" + arg1.toString());

                Message mess = mHandler.obtainMessage();
                mess.what = 404;
                mess.obj = "HTTP通讯错误-0001";
                mHandler.sendMessage(mess);
            }
        });
    }


    /**
     * Post 获取String数据
     *
     * @param reqUrl
     * @param mHandler
     */
    public void postStringExecute(String reqUrl, Map<String, String> map, final Handler mHandler,final int state, final int errorState) {
        RequestBody body = SetRequestBody(map);
        Request request = new Request.Builder().url(reqUrl).post(body).build();
        Log.d(TAG, "[postStringExecute]URL===" + reqUrl);

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                String Result = arg1.body().string();
//          Result = PUB.decodeBase64(Result);
                Log.d(TAG, "[onResponse]Call===" + arg1.code() + "Response===" + Result);

                Message mess = mHandler.obtainMessage();
                if (arg1.code() == 200) {
                    try {
                        // 转换返回结果信息给BEAN
                        mess.what = state;
                        mess.obj = Result;
                    } catch (Exception e) {
                        mess.what = errorState;
                        mess.obj = "HTTP数据异常-0002";
                    }
                } else {
                    mess.what = errorState;
                    mess.obj = "HTTP状态异常-" + arg1.code();
                }
                mHandler.sendMessage(mess);
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                Log.d(TAG, "[onFailure]Call===" + arg0 + "IOException===" + arg1.toString());

                Message mess = mHandler.obtainMessage();
                mess.what = errorState;
                mess.obj = "HTTP通讯错误-0001";
                mHandler.sendMessage(mess);
            }
        });
    }

    /**
     * Get 获取String数据
     *
     * @param reqUrl
     * @param
     */
    public String getStringExecute(String reqUrl, Map<String, String> map) {

        String UrlParams = setUrlParams(map);
        String URL = reqUrl + "?" + UrlParams;

        Log.e("URL",""+URL);

        Request request = new Request.Builder().url(URL).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {

                String Result = arg1.body().string();
                data = Result;

            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {

                data = "404";

            }
        });

        return data;
    }

    /**
     * Get 获取String数据
     *
     * @param reqUrl
     * @param mHandler
     */
    public void getStringExecute(String reqUrl, Map<String, String> map, final Handler mHandler, final int state , final int errorState) {
        String UrlParams = setUrlParams(map);
        String URL = reqUrl + "?" + UrlParams;

        Log.e("URL",""+URL);
        Request request = new Request.Builder().url(URL).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {

                String Result = arg1.body().string();
                Message mess = mHandler.obtainMessage();
                if (arg1.code() == 200) {
                    try {
                        // 转换返回结果信息给BEAN
                        mess.what = state;
                        mess.obj = Result;
                    } catch (Exception e) {
                        mess.what = errorState;
                        mess.obj = "HTTP数据异常-0002";
                    }
                } else {
                    mess.what = errorState;
                    mess.obj = "HTTP状态异常-" + arg1.code();
                }
                mHandler.sendMessage(mess);
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                Log.d(TAG, "[onFailure]Call===" + arg0 + "IOException===" + arg1.toString());

                Message mess = mHandler.obtainMessage();
                mess.what = errorState;
                mess.obj = "HTTP通讯错误-0001";
                mHandler.sendMessage(mess);
            }
        });
    }
}