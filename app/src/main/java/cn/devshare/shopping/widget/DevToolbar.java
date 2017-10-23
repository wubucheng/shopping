package cn.devshare.shopping.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import cn.devshare.shopping.R;



/**
 * Created by cheng on 2017/3/2.
 */

public class DevToolbar extends Toolbar
{
    private  View mView;
    private LayoutInflater mInflater;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightImageButton;//右键，将toolbar切换到搜索状态
    public  DevToolbar(Context context){

        this(context,null);
}
    public DevToolbar(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }
    public  DevToolbar(Context context, AttributeSet attrs,int defStyleAttr){
        super(context, attrs, defStyleAttr);
        initView();
        setContentInsetsRelative(10,10);
        //attrs是空的？
        if(attrs!=null){
            Log.i("attrs","not null");
            final TintTypedArray array=TintTypedArray.obtainStyledAttributes(getContext(),attrs, R.styleable.DevToolbar,defStyleAttr,0);
            final Drawable rightIcon=array.getDrawable(R.styleable.DevToolbar_rightButtonIcon);
            if(rightIcon!=null){
                Log.i("rightIcon","no null");
                setRightButtonIcon(rightIcon);
            }
            else{
                Log.i("rightIcon","null");
            }
            boolean isShowSearchView=array.getBoolean(R.styleable.DevToolbar_isShowSearchView,false);
            if(isShowSearchView){
                //如果在布局中设置搜索框的值为TRUE，则将标题隐藏掉且显示搜索框
                Log.i("isShowSearchView","true");
                showSearchView();
                hideTitleView();
            }
            array.recycle();
        }

    }
    private void initView() {
        if (mView==null){
            Log.i("DevToolbar","no view");
            mInflater= LayoutInflater.from(getContext());
            mView= mInflater.inflate(R.layout.toolbar,null);
            mRightImageButton= (Button) mView.findViewById(R.id.toolbar_rightButton);
            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView= (EditText) mView.findViewById(R.id.toolbar_searchview);
            mSearchView.clearFocus();
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView,layoutParams);//动态加子视图
        }
    }
    public void setRightButtonIcon(Drawable rightIcon) {
        if(mRightImageButton!=null){
            mRightImageButton.setBackground(rightIcon);
            mRightImageButton.setVisibility(VISIBLE);
        }
    }

    public void  setRightButtonIcon(int icon){
        setRightButtonIcon(getResources().getDrawable(icon));
    }

    public void setRightImageButtonOnClickListener(OnClickListener li){
        mRightImageButton.setOnClickListener(li);

    }

    public void setRightButtonText(CharSequence text){
        mRightImageButton.setText(text);
        mRightImageButton.setVisibility(VISIBLE);
    }
    public Button getRightButton(){

        return this.mRightImageButton;
    }
    @Override
    public void setTitle(int resId){

        setTitle(getContext().getText(resId));
    }
    @Override
    public void setTitle(CharSequence title){
        initView();
        if(mTextTitle !=null){
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showTitleView() {
        if(mTextTitle !=null){
            mTextTitle.setVisibility(VISIBLE);
        }
    }
    public  void showSearchView(){
        if(mSearchView!=null){
            mSearchView.setVisibility(VISIBLE);
            mSearchView.clearFocus();
        }
    }
    public void hideSearchView(){
        if(mSearchView!=null){
            mSearchView.setVisibility(GONE);
        }
    }
    public void hideTitleView(){
        if(mTextTitle !=null){
            mTextTitle.setVisibility(GONE);
        }
    }

}
