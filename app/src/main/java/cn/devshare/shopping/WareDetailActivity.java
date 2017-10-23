package cn.devshare.shopping;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.Serializable;

import cn.devshare.shopping.bean.Wares;
import cn.devshare.shopping.utils.CartProvider;
import cn.devshare.shopping.utils.ToastUtils;
import cn.devshare.shopping.widget.DevToolbar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;
import cn.devshare.shopping.R;
/**
 * Created by cheng on 2017/4/5.
 */

public class WareDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    private DevToolbar devToolbar;

    private Wares wares;
    private CartProvider cartProvider;
    private SpotsDialog spotsDialog;

    WebAppInterface webAppInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        webView=(WebView)findViewById(R.id.webView);
        devToolbar=(DevToolbar)findViewById(R.id.toolbar);

       Serializable serializable= getIntent().getSerializableExtra(Contants.WARE);
        if(serializable==null){
            finish();
        }
        spotsDialog=new SpotsDialog(this,"loading...");
        spotsDialog.show();

        wares= (Wares) serializable;
        cartProvider = new CartProvider(this);
        webAppInterface=new WebAppInterface(this);
        initToolBar();
        initWebView();
    }

    private void initWebView() {
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setAppCacheEnabled(true);
        webView.loadUrl("http://112.124.22.238:8081/course_api/wares/detail.html");
        webView.addJavascriptInterface(webAppInterface,"appInterface");
        webView.setWebViewClient(new WVC());
    }

    private void initToolBar() {
        devToolbar.setNavigationOnClickListener(this);
        devToolbar.setRightButtonText("分享");
        devToolbar.setRightImageButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(wares.getName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //下面的是外链的一张图片，如果是内网的图片，新浪微博解析不出来，会出错。
        oks.setImageUrl("http://s12.sinaimg.cn/mw690/001Dyuxqzy79WeJoyqT5b&690");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(wares.getImgUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(wares.getName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ShareSDK.stopSDK(this);
    }

    class WebAppInterface{
        private Context context;

        WebAppInterface(Context context){
            this.context=context;
        }
        @JavascriptInterface
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:showDetail("+wares.getId()+")");
                }
            });
        }
        @JavascriptInterface
        public void buy(long id){

            cartProvider.put(wares);
            ToastUtils.show(context,"已添加到购物车");

        }

        @JavascriptInterface
        public void addFavorites(long id){
        }
    }

    private class WVC extends WebViewClient {
        @Override
        public void onPageFinished(WebView view,String url){
            super.onPageFinished(view,url);
            if(spotsDialog!=null&&spotsDialog.isShowing()){
                spotsDialog.dismiss();
                webAppInterface.showDetail();
            }
        }
    }
}
