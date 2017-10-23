package cn.devshare.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import cn.devshare.shopping.R;
import cn.devshare.shopping.bean.ShoppingCart;
import cn.devshare.shopping.utils.CartProvider;
import cn.devshare.shopping.widget.NumberAddSubView;

/**
 * Created by cheng on 2017/3/25.
 */

public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {
    private CheckBox checkBox;
    private TextView textView;
    private CartProvider cartProvider;
    NumberAddSubView numberAddSubView;
    public CartAdapter(Context context,List<ShoppingCart> datas, final CheckBox checkBox,TextView tv) {
        super(context, R.layout.temp_cart, datas);
        setCheckBox(checkBox);
        setTextView(tv);
        cartProvider=new CartProvider(context);
        setOnItemClickListener(this);
        showTotalPrice();
    }

    @Override
    protected void convert(BaseViewHolder viewHolder,final ShoppingCart item){
        ((TextView)viewHolder.getView(R.id.text_price)).setText("￥"+item.getPrice());
        ((TextView)viewHolder.getView(R.id.text_title)).setText(item.getName());
        SimpleDraweeView drawble=(SimpleDraweeView)viewHolder.getView(R.id.drawee_view);
        drawble.setImageURI(Uri.parse(item.getImgUrl()));
        CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());


        numberAddSubView = (NumberAddSubView) viewHolder.getView(R.id.num_control);

        numberAddSubView.setValue(item.getCount());

        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                item.setCount(value);
              //  numberAddSubView.setValue(item.getCount());
                Log.i("value is ",""+value);
               // cartProvider.sub(item);//?减少还要更新？
                cartProvider.update(item);
                showTotalPrice();
            }
        });
    }

    private float getTotalPrice(){
        float sum=0;
        if(!isNull()){
            return  sum;
        }
        for(ShoppingCart cart:datas){
            if(cart.isChecked()){
                sum+=cart.getCount()*cart.getPrice();
            }
        }
        return sum;
    }

    private boolean isNull() {
        return (datas!=null&&datas.size()>0);
    }

    public void showTotalPrice() {
        float total=getTotalPrice();
        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }


    private void setTextView(TextView tv) {
        this.textView=tv;
    }

    private void setCheckBox(final CheckBox checkBox) {
        this.checkBox=checkBox;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }


    @Override
    public void onItemClick(View view, int positon) {
        ShoppingCart cart=getItem(positon);
        cart.setChecked(!cart.isChecked());//如果没有选中则点击选中
        notifyItemChanged(positon);
        checkListen();
        showTotalPrice();
    }

    //检查全选按钮，如果商品项等于被选中的数目则勾上全选按钮
    private void checkListen() {
        int count=0;
        int checkNum=0;
        if(datas!=null){
            count=datas.size();
            for(ShoppingCart cart:datas){
                if(!cart.isChecked()){
                    checkBox.setChecked(false);
                    break;
                }
                else{
                    checkNum=checkNum+1;
                }
            }
            if(count==checkNum){
                checkBox.setChecked(true);
            }
        }

    }

    //设置全选或全不选
    public void checkAll_None(boolean isChecked){
        if(!isNull()){
            return;
        }
        int i=0;
        for(ShoppingCart cart:datas){
            cart.setChecked(isChecked);//设置商品项是否勾上
            notifyItemChanged(i);
            i++;
        }
    }

    //删除商品
    public void delCart(){
        if(!isNull())
            return;
        for(Iterator iterator=datas.iterator();iterator.hasNext();){
            ShoppingCart cart= (ShoppingCart) iterator.next();
            if(cart.isChecked()){
               int position= datas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }




}
