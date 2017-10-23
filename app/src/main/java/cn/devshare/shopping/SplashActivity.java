package cn.devshare.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by cheng on 2017/4/14.
 */

public class SplashActivity extends AppCompatActivity {
    TextView version;
    TextView author;

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        version= (TextView) findViewById(R.id.version_tv);
        author= (TextView) findViewById(R.id.author_tv);
        handler.sendEmptyMessageDelayed(0,4000);
        }


}
