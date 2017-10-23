package cn.devshare.shopping.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by cheng on 2017/3/22.
 */

public class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder>{


    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder holder, T item) {

    }
}
