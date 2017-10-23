package cn.devshare.shopping.bean;

import java.io.Serializable;

/**
 * Created by cheng on 2017/3/25.
 */

public class ShoppingCart extends Wares implements Serializable{
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private int count;
    private boolean isChecked=true;
}
