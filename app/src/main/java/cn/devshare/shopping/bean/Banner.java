package cn.devshare.shopping.bean;

/**
 * Created by cheng on 2017/3/9.
 */
public class Banner extends BaseBean {


    private  String name;
    private  String imageUrl;
    private  String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imageUrl;
    }

    public void setImgUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
