package cn.devshare.shopping.adapter;

import android.content.Context;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.devshare.shopping.R;
import cn.devshare.shopping.bean.Wares;

/**
 * Created by cheng on 2017/3/22.
 */
public class WaresAdapter extends SimpleAdapter<Wares>{
    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
    }
    @Override
    public void convert(BaseViewHolder holder,Wares item){
        ((TextView)holder.getView(R.id.text_title)).setText(item.getName());
        ((TextView)holder.getView(R.id.text_price)).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView=(SimpleDraweeView)holder.getView(R.id.drawee_view);
        draweeView.setImageURI(item.getImgUrl());

    }
}
