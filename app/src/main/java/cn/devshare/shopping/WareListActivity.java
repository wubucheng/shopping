package cn.devshare.shopping;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.devshare.shopping.adapter.HWAdatper;
import cn.devshare.shopping.bean.Page;
import cn.devshare.shopping.bean.Wares;
import cn.devshare.shopping.utils.Pager;
import cn.devshare.shopping.widget.DevToolbar;

/**
 * Created by cheng on 2017/3/31.
 */

public class WareListActivity extends AppCompatActivity implements Pager.OnPageListener<Wares>,TabLayout.OnTabSelectedListener,View.OnClickListener{
    private static final String TAG = "WareListActivity";

    public static final int TAG_DEFAULT=0;
    public static final int TAG_PRICE=1;
    public static final int TAG_SALE=2;

    public static final int ACTION_LIST=1;
    public static final int ACTION_GIRD=2;

    private int orderBy = 0;
    private long campaignId = 0;

    private HWAdatper mWaresAdapter;

    private   Pager pager;

    private TabLayout mTablayout;
    private TextView mTxtSummary;
    private RecyclerView mRecyclerview_wares;
    private MaterialRefreshLayout mRefreshLayout;
    private DevToolbar toolbar;
    private Object data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warelist);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        mTxtSummary = (TextView) findViewById(R.id.txt_summary);
        mRecyclerview_wares = (RecyclerView) findViewById(R.id.recycler_view);
        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh_layout);
        toolbar = (DevToolbar) findViewById(R.id.toolbar);
        campaignId=getIntent().getLongExtra(Contants.COMPAINGAIN_ID,0);
        initToolBar();
        getData();
        initTab();
    }

    private void initTab() {
        TabLayout.Tab tab=mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTablayout.addTab(tab);

        tab=mTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTablayout.addTab(tab);

/*        mTablayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/
        mTablayout.setOnTabSelectedListener(this);
    }

    private void initToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });
        toolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        toolbar.getRightButton().setTag(ACTION_LIST);
        toolbar.setRightImageButtonOnClickListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int i=0;
        orderBy= (int) tab.getTag();
        pager.putParam("orderBy",orderBy);
        Log.i("order",""+i++);
        pager.request();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
/*        orderBy= (int) tab.getTag();
        pager.putParam("orderBy",orderBy);
        pager.request();*/
    }

    @Override
    public void onClick(View v) {
        int action= (int) v.getTag();
        if(ACTION_LIST==action){
            toolbar.setRightButtonIcon(R.drawable.icon_list_32);
            toolbar.getRightButton().setTag(ACTION_GIRD);
            mWaresAdapter.resetLayout(R.layout.template_grid_wares);
            //没有显示描述和价格，是因为没有数据穿进去，还需要绑定适配器？
            //mRecyclerview_wares.setLayoutManager(new GridLayoutManager(this,2));
        }
        else if(ACTION_GIRD==action){
            toolbar.setRightButtonIcon(R.drawable.icon_grid_32);
            toolbar.getRightButton().setTag(ACTION_LIST);
            mWaresAdapter.resetLayout(R.layout.template_hot_wares);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mTxtSummary.setText("共有"+totalCount+"件商品");
        if(mWaresAdapter==null){
            mWaresAdapter=new HWAdatper(this,datas);
            mRecyclerview_wares.setAdapter(mWaresAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        }else{
            mWaresAdapter.refreshData(datas);
        }
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        mRecyclerview_wares.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }

    public void getData() {
        pager=Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<Page<Wares>>(){}.getType());

        pager.request();
    }

}
