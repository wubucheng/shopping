package cn.devshare.shopping.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import cn.devshare.shopping.MainActivity;
import cn.devshare.shopping.R;
import cn.devshare.shopping.adapter.CartAdapter;
import cn.devshare.shopping.adapter.DividerItemDecortion;
import cn.devshare.shopping.bean.ShoppingCart;
import cn.devshare.shopping.utils.CartProvider;
import cn.devshare.shopping.widget.DevToolbar;


/**
 * Created by cheng on 2017/3/1.
 */

public class CartFragment extends Fragment  implements View.OnClickListener{
    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;
    private RecyclerView recyclerView;
    private CheckBox checkBox;
    private TextView totalTv;
    private Button settleBt;
    private Button delBt;
    private DevToolbar devToolbar;
    private CartAdapter cartAdapter;
    private CartProvider cartProvider;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_cart,container,false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        checkBox= (CheckBox) view.findViewById(R.id.checkbox_all);
        totalTv= (TextView) view.findViewById(R.id.txt_total);
        settleBt= (Button) view.findViewById(R.id.btn_order);
        delBt= (Button) view.findViewById(R.id.btn_del);
        delBt.setOnClickListener(this);
        cartProvider=new CartProvider(getContext());

        showData();
        return  view;
    }

//显示数据
    private void showData() {
        List<ShoppingCart>carts=cartProvider.getAll();
        cartAdapter=new CartAdapter(getContext(),carts,checkBox,totalTv);
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecortion(getContext(), DividerItemDecoration.VERTICAL));

    }

    //刷新数据
    public void refData(){
        Log.i("xxxxxxxxxxx ","");
        cartAdapter.clearData();
        List<ShoppingCart>carts=cartProvider.getAll();
        cartAdapter.addData(carts);
        Log.i("redata is ","");
        cartAdapter.showTotalPrice();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof MainActivity){
            MainActivity mainActivity= (MainActivity) context;
            devToolbar= (DevToolbar) mainActivity.findViewById(R.id.toolbar);
            changeToolBar();
        }
    }

    public void changeToolBar() {
        devToolbar.hideSearchView();
        devToolbar.showTitleView();
        devToolbar.setTitle(R.string.cart);
        devToolbar.getRightButton().setVisibility(View.VISIBLE);
        devToolbar.setRightButtonText("编辑");
        devToolbar.getRightButton().setOnClickListener(this);
        devToolbar.getRightButton().setTag(ACTION_EDIT);

    }

    //显示删除按钮
    private void showDelControl(){
        devToolbar.getRightButton().setText("完成");
        totalTv.setVisibility(View.GONE);
        settleBt.setVisibility(View.GONE);
        delBt.setVisibility(View.VISIBLE);
        devToolbar.getRightButton().setTag(ACTION_CAMPLATE);
        cartAdapter.checkAll_None(false);
        checkBox.setChecked(false);
    }
    private void  hideDelControl(){
        totalTv.setVisibility(View.VISIBLE);
        settleBt.setVisibility(View.VISIBLE);
        delBt.setVisibility(View.GONE);
        devToolbar.setRightButtonText("编辑");
        devToolbar.getRightButton().setTag(ACTION_EDIT);

        cartAdapter.checkAll_None(true);
        cartAdapter.showTotalPrice();
        checkBox.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        int action= (int) v.getTag();
        if(ACTION_EDIT==action){
            showDelControl();
        }
        else if(ACTION_CAMPLATE==action){
            hideDelControl();
        }

    }
}