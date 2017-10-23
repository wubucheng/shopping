package cn.devshare.shopping.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;//要导入V4的包，不是其他的
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.devshare.shopping.Contants;
import cn.devshare.shopping.R;
import cn.devshare.shopping.WareListActivity;
import cn.devshare.shopping.adapter.DividerItemDecortion;
import cn.devshare.shopping.adapter.HomeCategoryAdapter;
import cn.devshare.shopping.bean.Banner;
import cn.devshare.shopping.bean.Campaign;
import cn.devshare.shopping.bean.HomeCampaign;
import cn.devshare.shopping.bean.HomeCategory;
import cn.devshare.shopping.http.BaseCallBack;
import cn.devshare.shopping.http.OkHttpHelper;
import cn.devshare.shopping.http.SpotsCallBack;


/**
 * Created by cheng on 2017/3/1.
 */

public class HomeFragment extends Fragment {
    SliderLayout sliderLayout;
    private  static  final  String TAG="HomeFragment";
    private RecyclerView recylerView;
    private HomeCategoryAdapter homeCategoryAdapter;
    //List<HomeCategory>homeCategoryList;
    List<HomeCampaign> homeCampaigns;
    View view;
    private List<Banner> banner;
    private Gson gson = new Gson();
    private OkHttpHelper httpHelper=new OkHttpHelper();
    //Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_home,container,false);
        sliderLayout= (SliderLayout) view.findViewById(R.id.slider);
        recylerView= (RecyclerView) view.findViewById(R.id.recyclerview);
        requestImages();
        initRecylerView(view);

        return view;
    }

/*    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        sliderLayout= (SliderLayout) view.findViewById(R.id.slider);
        recylerView= (RecyclerView) view.findViewById(R.id.recyclerview);
        return view;
    }*/

/*    @Override
    public void init() {
        requestImages();
        initRecylerView(view);
    }*/

    private void requestImages(){
        String url=Contants.API.imageBannerUrl;
        httpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                banner=banners;
                initImageSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initData(List<HomeCampaign> homeCampaigns) {
        Log.i(TAG,""+homeCampaigns);
        homeCategoryAdapter=new HomeCategoryAdapter(homeCampaigns,getActivity());
															//给CampainClickListener初始化
        homeCategoryAdapter.setOnCampainClickListener(new HomeCategoryAdapter.OnCampainClickListener() {
            
			//CampainClickListener对象调用本身的onClick方法，它会先调用OnClciListener的onClick方法，在来的
			//。
			@Override
            public void onClick(View view, Campaign campaign) {
                Intent intent=new Intent(getActivity(),WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campaign.getId());
                startActivity(intent);
            }
        });
        recylerView.setAdapter(homeCategoryAdapter);
        recylerView.addItemDecoration(new DividerItemDecortion(getContext(), DividerItemDecoration.VERTICAL));
        recylerView.setLayoutManager(new LinearLayoutManager(HomeFragment.this.getActivity()));
    }

    private void initRecylerView(View view) {
        /*recylerView= (RecyclerView) view.findViewById(R.id.recyclerview);*/
/*        OkHttpClient client=new OkHttpClient();
        String url=Contants.API.CAMPAIGN_HOME;
        Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                    String json=response.body().string();
                    Log.i("the campain url is ",json);
                    Gson gson=new Gson();
                    Type type=new TypeToken<List<HomeCampaign>>(){}.getType();
                    homeCampaigns=gson.fromJson(json,type);
                    Log.i("the is "+TAG,homeCampaigns.toString());
                    //initData(homeCampaigns);
                    homeCategoryAdapter=new HomeCategoryAdapter(homeCampaigns,getActivity());
                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
            }*/
        //});



        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallBack<List<HomeCampaign>>() {
        @Override
        public void onBeforeRequest(Request request) {

        }

        @Override
        public void onFailure(Request request, Exception e) {

        }

        @Override
        public void onResponse(Response response) {

        }

        @Override
        public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
            initData(homeCampaigns);
        }


        @Override
        public void onError(Response response, int code, Exception e) {

        }
    });
    }


    private void initImageSlider() {

        if(banner!=null){
            for(Banner mBanner:banner){
                TextSliderView textSliderView=new TextSliderView(getActivity());
                textSliderView.image(mBanner.getImgUrl());
                textSliderView.description(mBanner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                sliderLayout.addSlider(textSliderView);
            }
        }

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());//设置动画
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);//设置旋转效果
        sliderLayout.setDuration(3000);//轮播时间
        sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d(TAG,"onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {

                //Log.d(TAG,"onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.d(TAG,"onPageScrollStateChanged");
            }
        });
    }
    @Override
    public void onStop(){
        sliderLayout.stopAutoCycle();
        super.onStop();
    }
}
