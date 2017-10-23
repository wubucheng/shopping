package cn.devshare.shopping.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.squareup.okhttp.Response;

import java.util.List;

import cn.devshare.shopping.Contants;
import cn.devshare.shopping.R;

import cn.devshare.shopping.adapter.BaseAdapter;
import cn.devshare.shopping.adapter.CategoryAdapter;
import cn.devshare.shopping.adapter.DividerGridItemDecoration;
import cn.devshare.shopping.adapter.WaresAdapter;
import cn.devshare.shopping.bean.Banner;
import cn.devshare.shopping.bean.Category;
import cn.devshare.shopping.bean.Page;
import cn.devshare.shopping.bean.Wares;
import cn.devshare.shopping.http.OkHttpHelper;
import cn.devshare.shopping.http.SpotsCallBack;


/**
 * Created by cheng on 2017/3/1.
 */

public class CategoryFragment extends Fragment {
    private RecyclerView categoryRV;
    private RecyclerView wareRV;
    private MaterialRefreshLayout materialRefreshLayout;
    private SliderLayout sliderLayout;
    private CategoryAdapter categoryAdapter;
    private WaresAdapter waresAdapter;
    private OkHttpHelper okHttpHelper;

    private int currPage=1;
    private int totalPage=1;
    private int pageSize=5;
    private long category_Id=0;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view=  inflater.inflate(R.layout.fragment_category,container,false);
        categoryRV= (RecyclerView) view.findViewById(R.id.recyclerview_category);
        wareRV= (RecyclerView) view.findViewById(R.id.recyclerview_wares);
        materialRefreshLayout= (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);
        sliderLayout= (SliderLayout) view.findViewById(R.id.slider);
        okHttpHelper=new OkHttpHelper();
        requestCategoryData();
        requestBannerData();
        initRefreshLayout();
        return view;
    }
    public void initRefreshLayout(){
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout){
                currPage=++currPage;//这里有bug,下拉的话会一直加,这样改好像也不对
                if(currPage<=totalPage){
                    loadMoreData();
                }else{
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private  void refreshData(){
        currPage =1;
        state=STATE_REFREH;
        requestWares(category_Id);
    }
    private void loadMoreData() {
        state=STATE_MORE;
        requestWares(category_Id);
    }

    private void requestCategoryData() {
        String categoryUrl= Contants.API.CATEGORY_LIST;
        okHttpHelper.get(categoryUrl, new SpotsCallBack <List<Category>>(getActivity()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                if(categories!=null&&categories.size()>0){
                    showCategoryData(categories);
                    category_Id=categories.get(0).getId();
                    requestWares(category_Id);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void requestWares(long categoryId) {
        String waresURL=Contants.API.WARES_LIST+"curPage="+currPage+"&pageSize="+pageSize+"&categoryId="+category_Id+".html";
        okHttpHelper.get(waresURL, new SpotsCallBack<Page<Wares>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
               if(waresPage!=null){
                   currPage=waresPage.getCurrentPage();
                   totalPage=waresPage.getTotalPage();
                   Log.i("totalPage is ",""+totalPage);
                   showWaresData(waresPage.getList());
               }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showWaresData(List<Wares> wareslist) {
        switch (state){
            case STATE_NORMAL:
                if(waresAdapter==null){
                    waresAdapter=new WaresAdapter(getContext(),wareslist);
                    wareRV.setAdapter(waresAdapter);
                    wareRV.setLayoutManager(new GridLayoutManager(getContext(),2));
                    wareRV.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }else{
                    waresAdapter.clearData();
                    waresAdapter.addData(wareslist);
                }
                break;
            case STATE_REFREH:
                waresAdapter.clearData();
                waresAdapter.addData(wareslist);
                wareRV.scrollToPosition(0);
                materialRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                waresAdapter.addData(wareslist);
                wareRV.scrollToPosition(waresAdapter.getDatas().size());
                materialRefreshLayout.finishRefreshLoadMore();
                break;


        }


    }

    private void showCategoryData(List<Category> categories) {
        categoryAdapter=new CategoryAdapter(getActivity(),categories);
        categoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int positon) {
                Category category=categoryAdapter.getItem(positon);
                category_Id=category.getId();
                currPage=1;
                state=STATE_NORMAL;
                requestWares(category_Id);

            }
        });
        categoryRV.setAdapter(categoryAdapter);
        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRV.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

    }

    private void requestBannerData(){
        String bannerURL=Contants.API.imageBannerUrl;
        okHttpHelper.get(bannerURL, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showSliderViews(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showSliderViews(List<Banner> banners) {
        if(banners!=null){
            for(Banner banner:banners){
                DefaultSliderView sliderView=new DefaultSliderView(getContext());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                sliderLayout.addSlider(sliderView);
            }
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
            sliderLayout.setDuration(3000);
        }
    }



}
