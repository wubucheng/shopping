package cn.devshare.shopping.adapter;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

import cn.devshare.shopping.bean.Category;
import cn.devshare.shopping.R;
/**
 * Created by cheng on 2017/3/22.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }
    @Override
    protected void convert(BaseViewHolder viewHolder,Category item){
        ((TextView)viewHolder.getView(R.id.textView)).setText(item.getName());
    }
}
