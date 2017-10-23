package cn.devshare.shopping;

/**
 * Created by cheng on 2017/3/15.
 * 存放API地址
 */

public class Contants {
    public static final String  COMPAINGAIN_ID="compaigin_id";
    public static final String  WARE="ware";
    public static final String USER_JSON="user_json";
    public static final String TOKEN="token";

    public  static final String DES_KEY="Cniao5_123456";

    public  static final int REQUEST_CODE=0;
    public static class API{
        public static final String imageBannerUrl="http://172.17.32.127:8080/SHOPPING/imageurl.html";
        public static final String BASE_URL="http://172.17.32.127:8080/SHOPPING/";
        public static final String CAMPAIGN_HOME=BASE_URL+"campaign/recommed.html";
        public static final String WARES_HOT=BASE_URL+"wares/hot/";
        public static final String CATEGORY_LIST=BASE_URL+"category/list/list.html";
        public static final String WARES_LIST=BASE_URL+"wares/list/";
        public static final String WARES_CAMPAIN_LIST=BASE_URL+"wares/campaign/list/";
        public static final String WARES_DETAIL=BASE_URL +"wares/detail.html";
        public static final String LOGIN=BASE_URL +"auth/login";

        public static final String USER_DETAIL=BASE_URL +"user/get?id=1";
    }
}
