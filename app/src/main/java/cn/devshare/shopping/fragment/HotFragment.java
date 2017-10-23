package cn.devshare.shopping.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;


import java.util.List;

import cn.devshare.shopping.Contants;
import cn.devshare.shopping.R;
import cn.devshare.shopping.WareDetailActivity;
import cn.devshare.shopping.adapter.BaseAdapter;
import cn.devshare.shopping.adapter.HWAdatper;
import cn.devshare.shopping.adapter.HotWaresAdapter;
import cn.devshare.shopping.bean.Page;
import cn.devshare.shopping.bean.Wares;
import cn.devshare.shopping.http.OkHttpHelper;
import cn.devshare.shopping.http.SpotsCallBack;
import cn.devshare.shopping.utils.Pager;


/**
 * Created by cheng on 2017/3/1.
 */

public class HotFragment extends BaseFragment implements Pager.OnPageListener{
    private OkHttpHelper okHttpHelper;
    private int currPage=1;//当前页数
    private int totalPage=1;//总共页数
    private int pageSize=5;//每页条目数
    private List<Wares> waresList;
    private HotWaresAdapter hotWaresAdapters;
    private HWAdatper hwAdatper;
    private static final String TAG="HotFragment";

/*    @ViewInject(R.id.refresh)
    private MaterialRefreshLayout materialRefreshLayout;

    @ViewInject(R.id.recyclerview)
    private RecyclerView recyclerView;*/



    private static final int STATE_NORMAL=0;
    private static final int STATE_REFRESH=1;
    private static final int STATE_MORE=2;
    private int state=STATE_NORMAL;
    private MaterialRefreshLayout materialRefreshLayout;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_hot,container,false);
        //ViewUtils.inject(this);
        //okHttpHelper=new OkHttpHelper();
        materialRefreshLayout= (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        recyclerView= (RecyclerView) view.findViewById(R.id.hotRecyler);
       // initRefreshLayout();
      //  getData();
        init();
        return view;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot,container,false);

    }

    @Override
    public void init(){
        Pager pager= Pager.newBuilder()
                   .setUrl(Contants.API.WARES_HOT)
                   .setLoadMore(true)
                   .setOnPageListener(this)
                   .setPageSize(5)
                   .setRefreshLayout(materialRefreshLayout)
                   .build(getActivity(),new TypeToken<Page<Wares>>(){}.getType());
        pager.request();
    }

/*    private void initRefreshLayout() {
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新
                refreshData();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //上拉加载更多
                currPage=++currPage;
                if(currPage<=totalPage){
                    loadMoreData();
                    Log.i(TAG,"CURRPAGE IS"+currPage);
                }else{
                    materialRefreshLayout.finishRefreshLoadMore();
                }

            }
        });
    }

    private void loadMoreData() {
        *//*currPage=++currPage;*//*
        state=STATE_MORE;
        getData();
    }*/

/*    private void refreshData() {
        currPage=1;
        state=STATE_REFRESH;//更改为正在刷新
        getData();
    }*/

/*    private void getData() {
        String hotWaresUrl= Contants.API.WARES_HOT+"curPage="+currPage+"&pageSize="+pageSize+".html";
        Log.i(TAG,hotWaresUrl);
        okHttpHelper.get(hotWaresUrl, new SpotsCallBack<Page<Wares>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                waresList=waresPage.getList();//获取商品数据
                currPage=waresPage.getCurrentPage();//
                totalPage=waresPage.getTotalPage();
                Log.i(TAG,"TOTALPAGE IS"+totalPage);
                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }*/

   /* private void showData() {
        switch (state){
            case STATE_NORMAL:
                hwAdatper=new HWAdatper(getContext(),waresList);
               // hotWaresAdapters= new HotWaresAdapter(waresList);
                recyclerView.setAdapter(hwAdatper);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                break;
            case STATE_REFRESH:
                hwAdatper.clearData();//定义一个方法为了防止直接使用List的清空方法将新获取的数据清掉，这里只将旧的数据清掉
                hwAdatper.addData(waresList);
                recyclerView.scrollToPosition(0);//回到顶部
                materialRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                hwAdatper.addData(hwAdatper.getDatas().size(),waresList);
                //hotWaresAdapters.addData();
               // hwAdatper.
                recyclerView.scrollToPosition(hwAdatper.getDatas().size());
                materialRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }*/

    @Override
    public void load(List datas, int totalPage, int totalCount) {
        hwAdatper=new HWAdatper(getContext(),datas);
        hwAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Wares wares = hwAdatper.getItem(position);

                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Contants.WARE,wares);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(hwAdatper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        hwAdatper.refreshData(datas);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        hwAdatper.loadMoreData(datas);
        recyclerView.scrollToPosition(hwAdatper.getDatas().size());
    }
}
