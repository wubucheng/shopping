package cn.devshare.shopping.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 2017/3/20.
 */
                                    //定义BaseAdapter<T,H>
public abstract class BaseAdapter  <T,H extends BaseViewHolder >  extends RecyclerView.Adapter<BaseViewHolder>{
    protected final Context context;
    protected  int layoutResId;
    protected List<T> datas;

    public interface OnItemClickListener{
        void onItemClick(View view, int positon);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        listener=onItemClickListener;
    }
    public BaseAdapter(Context context, int layoutResId) {
      this(context,layoutResId,null);
    }

    public BaseAdapter(Context context,int layoutResId,List<T>datas){
        this.context=context;
        this.datas=datas==null? new ArrayList<T>():datas;
        this.layoutResId=layoutResId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup,int itemType){
        View view=LayoutInflater.from(context).inflate(layoutResId,viewGroup,false);
        BaseViewHolder baseViewHolder=new BaseViewHolder(view,listener);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder,int position){
        //获取数据，并转型,这里调用另一个方法去执行
        T item=getItem(position);
        convert((H)holder,item);
    }
    @Override
    public int getItemCount(){
        if(datas!=null&&datas.size()>0){
            return  datas.size();
        }
        return 0;
    }
    protected abstract void convert(H holder, T item);

    //获取单个数据
    public T getItem(int position) {
        if(position>=datas.size()){
            return null;
        }
        return datas.get(position);
    }
    //获取所有数据
    public List<T> getDatas(){
        return datas;
    }
    //清除数据
    public void clearData(){
        int dataSize=datas.size();
        datas.clear();
        notifyItemRangeRemoved(0,dataSize);
    }

    public void removeItem(T t){
        int position=datas.indexOf(t);
        datas.remove(position);
        notifyItemRemoved(position);
    }

    //增加数据,从头开始加
    public void addData(List<T>datas){
        addData(0,datas);
    }
    //增加数据,从指定位置加
    public void addData(int i, List<T> datas) {
       if(datas!=null&&datas.size()>0){
           this.datas.addAll(datas);
           this.notifyItemRangeInserted(i,datas.size());
       }

    }

    public void refreshData(List<T> list){

        if(list !=null && list.size()>0){

            clearData();
            int size = list.size();
            for (int i=0;i<size;i++){
                datas.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }

    public void loadMoreData(List<T>list){
        if(list!=null&&list.size()>0){
            int size=list.size();
            int begin=datas.size();
            for(int i=0;i<size;i++){
                datas.add(list.get(i));
                notifyItemInserted(begin+i);
            }
        }
    }



}
