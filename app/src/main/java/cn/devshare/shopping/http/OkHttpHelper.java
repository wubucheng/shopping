package cn.devshare.shopping.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.HttpMethod;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.devshare.shopping.ShoppingApplication;

/**
 * Created by cheng on 2017/3/12.
 * 自己封装OkHttp
 * 单例模式
 */

public class OkHttpHelper {
    public static final String TAG="OkHttpHelper";
    private static OkHttpHelper okInstance;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private Handler handler;
    static {
        okInstance=new OkHttpHelper();

    }
    public OkHttpHelper(){
        okHttpClient=new OkHttpClient();
        okHttpClient.setConnectTimeout(10,TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(10,TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(30,TimeUnit.SECONDS);
        gson=new Gson();
        handler=new Handler(Looper.getMainLooper());//表示放到主UI线程去处理,即Handler要与主线程的消息队列关联上，handleMessage才会执行在UI线程，此时才是线程安全的
                                                    //后面利用了post()将消息post到了UI线程
    };
    public static OkHttpHelper getOkInstance(){
        return okInstance;
    }
    //自定义的get方法
    public void get(String url,BaseCallBack callBack){
        Log.i("the url is:",url);
        Request request=buildGetRequest(url);
        request(request,callBack);
    }
    //自定义的post方法
    public void post(String url, Map<String,String>param,BaseCallBack callBack){
        Request request=buildPostRequest(url,param);
        request(request,callBack);
    }
    public void request(final Request request,final BaseCallBack callBack){
        callBack.onBeforeRequest(request);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.onFailure(request,e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callBack.onResponse(response);
                if(response.isSuccessful()){
                    String resultStr=response.body().string();
                    if(callBack.mType==String.class){
                        callbackSuccess(callBack,response,resultStr);
                    }
                    else{
                        //返回的数据不是字符串类型则进行gson解析，这里将要解析成的类型
                        //利用callback封装成了mType,来接收TYPE
                        try {
                            Object obj=gson.fromJson(resultStr,callBack.mType);
                           callbackSuccess(callBack,response,obj);
                        }catch (JsonParseException e){
                            callBack.onError(response,response.code(),e);
                        }
                    }

                }
                else{
                    Looper.prepare();

                    callBack.onError(response,response.code(),null);
                    Looper.loop();
                }
            }
        });
    }
    private void callbackSuccess(final BaseCallBack callBack, final Response response, final Object obj ){
       // 用runnable可以直接传入如何操作的对象，不需要再接收到消息后再去判断message的what然后选择做什么操作
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(response,obj);
            }
        });
    }
    private void callError(final BaseCallBack callBack,final Response response,final Exception e ){
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(response,response.code(),e);
            }
        });
    }



    private Request buildPostRequest(String url,Map<String,String>params){
        return buildRequest(url, HttpMethodType.POST,params);
    }
    private Request buildGetRequest(String url){
        return  buildRequest(url,HttpMethodType.GET,null);
    }
    private Request buildRequest(String url, HttpMethodType methodType, Map<String,String> params) {
        Request.Builder builder=new Request.Builder().url(url);
        if(methodType==HttpMethodType.POST){
            RequestBody body=builderFormData(params);//增加参数
            builder.post(body);
        }
        else if(methodType==HttpMethodType.GET){
            builder.get();
        }
        return  builder.build();
    }

    //构造post请求参数
    private RequestBody builderFormData(Map<String, String> params) {
        FormEncodingBuilder feBuilder=new FormEncodingBuilder();
        if(params!=null){
            for(Map.Entry<String,String>entry:params.entrySet()){
                feBuilder.add(entry.getKey(),entry.getValue());
            }
            ShoppingApplication shoppingApplication=new ShoppingApplication();

            //String token = shoppingApplication.getToken();
/*            if(!TextUtils.isEmpty(token))
                feBuilder.add("token", token);*/
        }
        return  feBuilder.build();//此过程：将content转换为UTF格式且调用了RequestBody.create，返回了一个RequestBody对象给Builder使用
    }

    enum  HttpMethodType{

        GET,
        POST,

    }
}
