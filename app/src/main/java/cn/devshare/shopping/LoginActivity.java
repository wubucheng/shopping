package cn.devshare.shopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import cn.devshare.shopping.bean.User;
import cn.devshare.shopping.http.OkHttpHelper;
import cn.devshare.shopping.http.SpotsCallBack;
import cn.devshare.shopping.msg.LoginRespMsg;
import cn.devshare.shopping.utils.DESUtil;
import cn.devshare.shopping.utils.ToastUtils;
import cn.devshare.shopping.widget.ClearEditText;
import cn.devshare.shopping.widget.DevToolbar;

/**
 * Created by cheng on 2017/4/12.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    DevToolbar devToobar;
    ClearEditText phoneEt;
    ClearEditText pwdEt;
    Button loginBt;
    String phone;
    String pwd;
    OkHttpHelper okHttpHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        okHttpHelper=OkHttpHelper.getOkInstance();
        devToobar= (DevToolbar) findViewById(R.id.toolbar);
        phoneEt= (ClearEditText) findViewById(R.id.etxt_phone);
        pwdEt= (ClearEditText) findViewById(R.id.etxt_pwd);
        loginBt= (Button) findViewById(R.id.btn_login);
        loginBt.setOnClickListener(this);
        initToolBar();
    }

    private void initToolBar() {
        devToobar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }


    @Override
    public void onClick(View v) {
        Log.i("xx","xxx");
        phone=phoneEt.getText().toString();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, "请输入手机号码");
            return;
        }
        pwd=pwdEt.getText().toString();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.show(this,"请输入密码");
            return;
        }
        login();
    }

    public void login(){
        Map<String,String> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));
        okHttpHelper.post(Contants.API.LOGIN,params, new SpotsCallBack<LoginRespMsg<User>>(this) {
            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                ShoppingApplication app=ShoppingApplication.getInstance();
                app.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                if(app.getIntent()==null){
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    app.jumpToTargetActivity(LoginActivity.this);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
