package cn.devshare.shopping.http;
import java.io.IOException;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by cheng on 2017/3/12.
 OKhttp请求回调类 
 */
public abstract class BaseCallBack<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();//获得带有泛型的父类
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;////ParameterizedType参数化类型，即泛型
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]); //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
    }


    public BaseCallBack()
    {
        mType = getSuperclassTypeParameter(getClass());
    }



    public  abstract void onBeforeRequest(Request request);


    public abstract  void onFailure(Request request, Exception e) ;


    /**
     *请求成功时调用此方法
     * @param response
     */
    public abstract  void onResponse(Response response);

    /**
     *
     * 状态码大于200，小于300 时调用此方法
     * @param response
     * @param t
     * @throws IOException
     */
    public abstract void onSuccess(Response response,T t) ;

    /**
     * 状态码400，404，403，500等时调用此方法
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response, int code, Exception e) ;

}
