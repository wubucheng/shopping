package cn.devshare.shopping.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.devshare.shopping.R;
import cn.devshare.shopping.bean.Campaign;
import cn.devshare.shopping.bean.HomeCampaign;
import cn.devshare.shopping.bean.HomeCategory;

/**
 * Created by cheng on 2017/3/9.
 */

public class HomeCategoryAdapter extends  RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {
    private List<HomeCampaign> homeCampaignList;
    private LayoutInflater layoutInflater;
    private  static int VIEW_TYPE_L=0;
    private  static int VIEW_TYPE_R=1;
    private  Context context;
    OnCampainClickListener listener;

    public HomeCategoryAdapter(List<HomeCampaign> homeCampaignList, Context context) {
        this.homeCampaignList= homeCampaignList;
        this.context=context;
    }
    public void setOnCampainClickListener(OnCampainClickListener onCampainClickListener){
        this.listener=onCampainClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(context);
        if(viewType==VIEW_TYPE_R){
            View view=layoutInflater.inflate(R.layout.template_home_cardview2,parent,false);
            ViewHolder viewHolder=new ViewHolder(view);
            return viewHolder;
        }
        else{
            View view=layoutInflater.inflate(R.layout.template_home_cardview,parent,false);
            ViewHolder viewHolder=new ViewHolder(view);
            return viewHolder;
        }
    }
    @Override
    public int getItemViewType(int positon){
        if(positon%2==0){
            return VIEW_TYPE_R;
        }
        else return VIEW_TYPE_L;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeCampaign homeCampaign = homeCampaignList.get(position);
/*        Log.i("homeCampaignList SIZE:",""+homeCampaignList.size());
        Log.i("homeList",homeCampaignList.toString());
        holder.titleText.setText(homeCampaign.getTitle());
        Log.i("HomeCategoryAdapter",""+homeCampaign.getTitle());
        Log.i("HomeCampaign",""+homeCampaign.getCpOne());

            Log.i("campaign is not null",""+homeCampaign.getCpOne().getImgUrl());*/
            holder.titleText.setText(homeCampaign.getTitle());
            Picasso.with(context).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
            Picasso.with(context).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
            Picasso.with(context).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);


    }


    @Override
    public int getItemCount() {
        return homeCampaignList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView titleText;
        private ImageView imageViewBig;
        private ImageView imageViewSmallTop;
        private ImageView imageViewSmallBottom;
        public ViewHolder(View itemView) {
            super(itemView);
            titleText= (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);

        }
        @Override
        public void onClick(View v){
            HomeCampaign homeCampaign=homeCampaignList.get(getLayoutPosition());
            if(listener!=null){
                switch (v.getId()){
                    case R.id.imgview_big:
                        listener.onClick(v,homeCampaign.getCpOne());
                        break;
                    case  R.id.imgview_small_top:
                        listener.onClick(v,homeCampaign.getCpTwo());
                        break;
                    case  R.id.imgview_small_bottom:
                        listener.onClick(v,homeCampaign.getCpThree());
                        break;
                }
            }

        }
        }

    public interface OnCampainClickListener{
        void onClick(View view, Campaign campaign);
    }


    }

