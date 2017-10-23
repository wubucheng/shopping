package cn.devshare.shopping.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cheng on 2017/3/30.
 */

public abstract class BaseFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = createView(inflater,container,savedInstanceState);
        initToolBar();
        init();
        return view;
    }

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public void initToolBar(){

    }
    public abstract void init();
}
