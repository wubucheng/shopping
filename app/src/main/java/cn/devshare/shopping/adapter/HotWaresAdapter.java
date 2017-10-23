package cn.devshare.shopping.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import cn.devshare.shopping.R;
import cn.devshare.shopping.bean.ShoppingCart;
import cn.devshare.shopping.bean.Wares;
import cn.devshare.shopping.utils.CartProvider;
import cn.devshare.shopping.utils.ToastUtils;

/**
 * Created by cheng on 2017/3/19.
 */

public class HotWaresAdapter extends RecyclerView.Adapter<HotWaresAdapter.HotViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Wares> waresList;
    private CartProvider cartProvider;

    public HotWaresAdapter(List<Wares>waresList){
        this.waresList=waresList;

    }
    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.template_hot_wares,null);
        HotViewHolder hotViewHolder=new HotViewHolder(view);
        return hotViewHolder;
    }


    private ShoppingCart convertData(Wares wares) {
        return null;
    }


    @Override
    public void onBindViewHolder(HotViewHolder holder, int position) {
        //holder.simpleDraweeView=waresList.get(position).getImgUrl();
        Wares wares=waresList.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.titleText.setText(wares.getName());
        holder.priceText.setText("￥"+wares.getPrice());
    }


    @Override
    public int getItemCount() {
        if(waresList!=null&& waresList.size()>0){
            return waresList.size();
        }
        return 0;
    }

    public void addData(List<Wares>datas){
        addData(0,datas);
    }
    //增加数据
    public void addData(int i, List<Wares> datas) {
        if(datas!=null&&datas.size()>0){
            Log.i("adaper","has datas");
            waresList.addAll(datas);
            notifyItemRangeChanged(i,waresList.size());
        }
    }
    //清除数据
    public void clearData(){
        if(waresList!=null&&waresList.size()>0){
           int waresListSize=waresList.size();
            waresList.clear();
            Log.i("adapter ","the waresList is null");
            notifyItemRangeRemoved(0,waresListSize);
        }
    }
   //获取所有数据
    public List<Wares>getDatas(){
        return waresList;
    }
    class HotViewHolder extends RecyclerView.ViewHolder{
       private SimpleDraweeView simpleDraweeView;
        private TextView titleText;
        private TextView priceText;
        public HotViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView= (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            titleText= (TextView) itemView.findViewById(R.id.text_title);
            priceText= (TextView) itemView.findViewById(R.id.text_price);
        }
    }
}
