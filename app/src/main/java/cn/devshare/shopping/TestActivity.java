package cn.devshare.shopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.devshare.shopping.widget.DevToolbar;

/**
 * Created by cheng on 2017/3/4.
 */

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        DevToolbar toolbar = (DevToolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back_32px);//不能在XML中设置，只能在代码里设置

    }
}
