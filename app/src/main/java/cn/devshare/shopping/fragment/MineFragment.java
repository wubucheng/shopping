package cn.devshare.shopping.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.devshare.shopping.Contants;
import cn.devshare.shopping.LoginActivity;
import cn.devshare.shopping.MainActivity;
import cn.devshare.shopping.R;
import cn.devshare.shopping.ShoppingApplication;
import cn.devshare.shopping.bean.User;
import cn.devshare.shopping.widget.DevToolbar;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by cheng on 2017/3/1.
 */

public class MineFragment extends Fragment implements View.OnClickListener{
    private CircleImageView circleImageView;
    private TextView userNameTv;
    private Button logoutBt;
    DevToolbar devToolbar;
    ShoppingApplication shoppingApplication;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view=  inflater.inflate(R.layout.fragment_mine,container,false);

        circleImageView= (CircleImageView) view.findViewById(R.id.img_head);
        userNameTv= (TextView) view.findViewById(R.id.txt_username);
        logoutBt= (Button) view.findViewById(R.id.btn_logout);
        circleImageView.setOnClickListener(this);
        userNameTv.setOnClickListener(this);

        shoppingApplication= (ShoppingApplication) getActivity().getApplication();

        User user =  shoppingApplication.getUser();
        showUser(user);
        //User user=ShoppingApplication.getInstance().getUser();
        return view;
    }

    private void initToolbar() {
        devToolbar.hideSearchView();
        devToolbar.showTitleView();
        devToolbar.setTitle(R.string.mine);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof MainActivity){
            MainActivity mainActivity= (MainActivity) context;
            devToolbar= (DevToolbar) mainActivity.findViewById(R.id.toolbar);
            initToolbar();
        }
    }
/*    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void init() {
        User user=ShoppingApplication.getInstance().getUser();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_head:
            case R.id.txt_username:
                if(shoppingApplication.getUser() == null) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent, Contants.REQUEST_CODE);
                }
                break;
            case R.id.btn_logout:
                ShoppingApplication.getInstance().clearUser();
                showUser(null);
        }

    }

    private void showUser(User user) {
        if(user!=null){
            if(TextUtils.isEmpty(user.getLogo_url())){
                showHeadImage(user.getLogo_url());
                logoutBt.setVisibility(View.GONE);
            }
        }else{
            userNameTv.setText(R.string.to_login);
            logoutBt.setVisibility(View.VISIBLE);
        }
    }

    private void showHeadImage(String logo_url) {
        Picasso.with(getActivity()).load(logo_url).into(circleImageView);
    }
}
