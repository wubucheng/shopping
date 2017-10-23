package cn.devshare.shopping.bean;

import java.io.Serializable;

/**
 * Created by cheng on 2017/3/9.
 */

public class BaseBean implements Serializable {
    protected long id;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
