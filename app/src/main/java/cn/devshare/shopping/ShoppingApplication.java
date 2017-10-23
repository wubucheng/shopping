package cn.devshare.shopping;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;

import cn.devshare.shopping.bean.User;
import cn.devshare.shopping.utils.UserLocalData;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by cheng on 2017/3/19.
 */

public class ShoppingApplication extends Application {
    private User user;

    private static  ShoppingApplication mInstance;


    public static  ShoppingApplication getInstance(){

        return  mInstance;
    }

    @Override
     public void onCreate(){
        super.onCreate();
        Fresco.initialize(this);
        ShareSDK.initSDK(this);
        mInstance = this;
        initUser();
    }

    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }


    public User getUser(){

        return user;
    }


    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }


    public String getToken(){

        return  UserLocalData.getToken(this);
    }



    private Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }
}
