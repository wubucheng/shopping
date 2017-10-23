package cn.devshare.shopping;


import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import cn.devshare.shopping.bean.Tab;
import cn.devshare.shopping.fragment.CartFragment;
import cn.devshare.shopping.fragment.CategoryFragment;
import cn.devshare.shopping.fragment.HomeFragment;
import cn.devshare.shopping.fragment.HotFragment;
import cn.devshare.shopping.fragment.MineFragment;
import cn.devshare.shopping.widget.DevToolbar;
import cn.devshare.shopping.widget.FragmentTabHost;

public class MainActivity extends AppCompatActivity {
    FragmentTabHost tabHost;
    private LayoutInflater mInflater;
    private List<Tab> tabList = new ArrayList<Tab>();
    private DevToolbar devToolbar;
    private CartFragment cartFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        devToolbar= (DevToolbar) findViewById(R.id.toolbar);

    }

    private void initTab() {
        Tab hometTab = new Tab(HomeFragment.class, R.string.home, R.drawable.selector_icon_home);
        Tab hotTab = new Tab(HotFragment.class, R.string.hot, R.drawable.selector_icon_hot);
        Tab carTab = new Tab(CartFragment.class, R.string.cart, R.drawable.selector_icon_cart);
        Tab categoryTab = new Tab(CategoryFragment.class, R.string.catagory, R.drawable.selector_icon_category);
        Tab mineTab = new Tab(MineFragment.class, R.string.mine, R.drawable.selector_icon_mine);
        tabList.add(hometTab);
        tabList.add(hotTab);
        tabList.add(carTab);
        tabList.add(categoryTab);
        tabList.add(mineTab);

        mInflater = LayoutInflater.from(this);
        tabHost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (Tab tab : tabList) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(tab.getTitle()));
            View view = builtIndicator(tab);
            tabSpec.setIndicator(view);
            tabHost.addTab(tabSpec,tab.getFragment(),null);
        }

        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        tabHost.setCurrentTab(0);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId==getString(R.string.cart)){
                    refData();
                    Log.i("is cartFragment","xxx");
                }
                else{
                    devToolbar.showSearchView();
                    devToolbar.hideTitleView();
                    devToolbar.getRightButton().setVisibility(View.GONE);
                }
            }
        });
    }

    private void refData() {
        if(cartFragment==null){
            Fragment fragment=getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if(fragment!=null){
                Log.i("fragment is not null","x");
                cartFragment= (CartFragment) fragment;
                cartFragment.refData();
                cartFragment.changeToolBar();
            }

        }
        else{
            cartFragment.refData();
            cartFragment.changeToolBar();
        }
    }


    private View builtIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_tab);
        TextView titleText = (TextView) view.findViewById(R.id.txt_indicator);
        imageView.setBackgroundResource(tab.getIcon());
        titleText.setText(tab.getTitle());
        return view;
    }

}
