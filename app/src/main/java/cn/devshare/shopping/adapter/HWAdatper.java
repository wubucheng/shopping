package cn.devshare.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.List;

import cn.devshare.shopping.bean.ShoppingCart;
import cn.devshare.shopping.bean.Wares;
import cn.devshare.shopping.utils.CartProvider;
import cn.devshare.shopping.R;
import cn.devshare.shopping.utils.ToastUtils;

/**
 * Created by cheng on 2017/3/26.
 */

public class HWAdatper extends SimpleAdapter<Wares> {


        CartProvider provider ;

   public HWAdatper(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);//调用父类的方法，将数据传给父类，为了后面的getDatas有数据
        provider = new CartProvider(context);
        }

    //谁调用的？父类的onBindViewHolder中
@Override
protected void convert(BaseViewHolder viewHolder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
       ((TextView)viewHolder.getView(R.id.text_title)).setText(wares.getName());
       ((TextView)viewHolder.getView(R.id.text_price)).setText("￥ "+wares.getPrice());
        Button button =(Button)viewHolder.getView(R.id.add_bt);
    if(button!=null) {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                provider.put(wares);

                ToastUtils.show(context, "已添加到购物车");
            }
        });
    }
        }


/*public ShoppingCart convertData(Wares item){

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        Log.i("item id is ",""+item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
        }*/
public void resetLayout(int layoutId){
    this.layoutResId=layoutId;
    notifyItemRangeChanged(0,getDatas().size());
}
}
