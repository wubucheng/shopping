package cn.devshare.shopping.utils;

import android.content.Context;
import android.text.TextUtils;

import cn.devshare.shopping.Contants;
import cn.devshare.shopping.bean.User;

/**
 * Created by cheng on 2017/4/10.
 */

public class UserLocalData {
    public static void putUser(Context context,User user){


        String user_json =  JSONUtil.toJson(user);
        PreferencesUtils.putString(context, Contants.USER_JSON,user_json);

    }

    public static void putToken(Context context,String token){

        PreferencesUtils.putString(context, Contants.TOKEN,token);
    }


    public static User getUser(Context context){

        String user_json= PreferencesUtils.getString(context, Contants.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){

            return  JSONUtil.fromJson(user_json,User.class);
        }
        return  null;
    }

    public static  String getToken(Context context){

        return  PreferencesUtils.getString(context,Contants.TOKEN);

    }


    public static void clearUser(Context context){


        PreferencesUtils.putString(context, Contants.USER_JSON,"");

    }

    public static void clearToken(Context context){

        PreferencesUtils.putString(context, Contants.TOKEN,"");
    }
}
