package cn.devshare.shopping.utils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.devshare.shopping.bean.Page;
import cn.devshare.shopping.http.OkHttpHelper;
import cn.devshare.shopping.http.SpotsCallBack;

/**
 * Created by cheng on 2017/3/30.
 */

public class Pager {
    private static Builder builder;
    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;

    OkHttpHelper okHttpHelper;
    private Pager(){
        okHttpHelper=OkHttpHelper.getOkInstance();
        initRefreshLayout();
    }



    public void request(){
        requestData();
    }

    private void requestData() {
        String url=buildUrl();
        okHttpHelper.get(url,new RequestCallBack(builder.context));
    }

    private String buildUrl() {
        return builder.url+builderUrlParams()+".html";

    }

    private String builderUrlParams() {
        LinkedHashMap<String,Object>map=builder.params;
        map.put("curPage",builder.pageIndex);
        map.put("pageSize",builder.pageSize);
        StringBuffer sb=new StringBuffer();

        for(Map.Entry<String,Object>entry:map.entrySet()){
            sb.append(entry.getKey()+"="+entry.getValue());
            sb.append("&");
        }

        String url=sb.toString();
        if(url.endsWith("&")){
            url=url.substring(0,url.length()-1);

        }
        return url;
    }


    private void initRefreshLayout() {
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout){
                int index=builder.pageIndex;
                builder.pageIndex=++builder.pageIndex;
                if(builder.pageIndex<builder.totalPage)
                    loadMore();
                else{
                    builder.pageIndex=index;
                    Toast.makeText(builder.context, "无更多数据", Toast.LENGTH_LONG).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    private void loadMore() {
        state=STATE_MORE;
        requestData();
    }

    private void refresh() {
        state=STATE_REFREH;
        builder.pageIndex=1;
        requestData();
    }

    public void putParam(String key,Object value){
        builder.params.put(key,value);
    }

    public static Builder newBuilder(){

        builder = new Builder();
        return builder;
    }

    private <T> void showData(List<T> datas,int totalPage,int totalCount) {
        if(datas==null||datas.size()<=0){
            Toast.makeText(builder.context,"加载不到数据",Toast.LENGTH_LONG).show();
            return;
        }
        if(STATE_NORMAL==state){
            if(builder.onPageListener!=null){
                builder.onPageListener.load(datas,totalPage,totalCount);;
            }
        }
        else if(STATE_REFREH==state){
            builder.mRefreshLayout.finishRefresh();
            if(builder.onPageListener!=null){
                builder.onPageListener.refresh(datas,totalPage,totalCount);;
            }
        }
        else if(STATE_MORE==state){
            builder.mRefreshLayout.finishRefreshLoadMore();
            if(builder.onPageListener!=null){
                builder.onPageListener.loadMore(datas,totalPage,totalCount);;
            }
        }

    }

    public static class Builder{
        private Context context;
        private Type type;
        private String url;

        private MaterialRefreshLayout mRefreshLayout;

        private boolean canLoadMore;


        private int totalPage = 1;
        private int pageIndex = 1;
        private int pageSize = 5;

        private LinkedHashMap<String,Object> params = new LinkedHashMap<>(5);
        private OnPageListener onPageListener;



        public Builder setUrl(String url){
            builder.url = url;
            return builder;
        }

        public Builder setPageSize(int pageSize){
            this.pageSize=pageSize;
            return builder;
        }

        public Builder putParam(String key,Object value){
            Log.i("the value is",""+value);
            params.put(key,value);
            return builder;
        }

        //设置是否可以加载更多
        public Builder setLoadMore(boolean loadMore){
            this.canLoadMore=loadMore;
            return builder;
        }
        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout){
            this.mRefreshLayout=refreshLayout;
            return builder;
        }

        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }

        public Pager build(Context context,Type type){
            this.type=type;
            this.context=context;
            valid();
            return new Pager();
        }

        private void valid() {

            if(this.context==null)
                throw  new RuntimeException("content can't be null");

            if(this.url==null || "".equals(this.url))
                throw  new RuntimeException("url can't be  null");

            if(this.mRefreshLayout==null)
                throw  new RuntimeException("MaterialRefreshLayout can't be  null");
        }

    }

    class RequestCallBack<T> extends SpotsCallBack<Page<T>> {

        public RequestCallBack(Context context) {
            super(context);
            super.mType=builder.type;
        }
        @Override
        public void onFailure(Request request, Exception e) {
            Looper.prepare();
            dismissDialog();
            Looper.loop();
            Toast.makeText(builder.context,"请求出错："+e.getMessage(), Toast.LENGTH_LONG).show();
            if(STATE_REFREH==state)   {
                builder.mRefreshLayout.finishRefresh();
            }
            else  if(STATE_MORE == state){
               builder.mRefreshLayout.finishRefreshLoadMore();
            }

        }
        @Override
        public void onSuccess(Response response, Page<T> page) {
            builder.pageIndex= page.getCurrentPage();
            builder.pageSize= page.getPageSize();
            builder.totalPage = page.getTotalPage();
            showData(page.getList(),builder.totalPage,page.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {
            Toast.makeText(builder.context,"加载数据失败",Toast.LENGTH_LONG).show();
            if(STATE_REFREH==state)   {
                builder.mRefreshLayout.finishRefresh();
            }
            else  if(STATE_MORE == state){

                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }

    }



    public interface OnPageListener<T>{
        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas,int totalPage,int totalCount);

        void loadMore(List<T> datas,int totalPage,int totalCount);
    }
}
