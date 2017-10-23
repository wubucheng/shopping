package cn.devshare.shopping.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;


/**
 * Created by cheng on 2017/3/20.
 */
public  class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SparseArray<View> viewSparseArray;
    private BaseAdapter.OnItemClickListener listener;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.listener = listener;
        viewSparseArray = new SparseArray<View>();
    }
/*    public TextView getTextView(int viewId){
        return  getView(viewId);
    }
    public Button getButton(int viewId){
        return  getView(viewId);
    }
    public ImageView  getImageView(int viewId){
        return  getView(viewId);
    }
    public  View get*/


    public  <T extends View> T getView(int viewId) {
        View view=viewSparseArray.get(viewId);
        if(view==null){
            view =itemView.findViewById(viewId);
            viewSparseArray.put(viewId,view);
        }
        return  (T) view;
    }

    @Override
    public void onClick(View view){
        if(listener!=null){
            listener.onItemClick(view,getLayoutPosition());
        }
    }
}
