package cn.devshare.shopping.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import cn.devshare.shopping.R;

/**
 * Created by cheng on 2017/4/12.
 */

public class ClearEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher{

    private Drawable clearTextIcon;
    private OnFocusChangeListener onFocusChangeListener;
    private OnTouchListener onTouchListener;

    public ClearEditText(final Context context) {
        super(context);
        init(context);
    }

    public ClearEditText(final Context context,final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearEditText(final Context context, final AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        Drawable drawable= ContextCompat.getDrawable(context, R.drawable.icon_delete_32);
        Drawable wrappedDrawble= DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawble,getCurrentHintTextColor());;
        clearTextIcon=wrappedDrawble;
        //对边界进行处理
        clearTextIcon.setBounds(0,0,clearTextIcon.getIntrinsicHeight(),clearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    private void setClearIconVisible(boolean visible) {
        clearTextIcon.setVisible(visible,false);
        final Drawable[] compundDrawble=getCompoundDrawables();//获取4个位置的图片资源，该资源是由xml中设置android:drawble提供的，下面是将它设置到右边
        setCompoundDrawables(compundDrawble[0],compundDrawble[1],visible?clearTextIcon:null,compundDrawble[3]);
    }
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeLlistener){
        this.onFocusChangeListener=onFocusChangeLlistener;;
    }

    //焦点改变事件
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            setClearIconVisible(getText().length()>0);
        }
        else{
            setClearIconVisible(false);
        }
        if(onFocusChangeListener!=null){
            onFocusChangeListener.onFocusChange(v,hasFocus);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x= (int) event.getX();
        //判断有无按下
        if(clearTextIcon.isVisible()&&x>getWidth()-getPaddingRight()-clearTextIcon.getIntrinsicWidth()){
            if(event.getAction()==MotionEvent.ACTION_UP){
                setError(null);
                setText("");
            }
            return true;
        }
        return (onTouchListener!=null&&onTouchListener.onTouch(v,event))? true:false;
    }

    //有文字时，显示清空控件
    @Override
    public final void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            setClearIconVisible(text.length() > 0);
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //当EditText中重新输入文字的后，sendAfterTextChanged会被调用
    @Override
    public void afterTextChanged(Editable s) {

    }
}
