package cn.devshare.shopping.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.devshare.shopping.bean.ShoppingCart;
import cn.devshare.shopping.bean.Wares;

/**
 * Created by cheng on 2017/3/25.
 */

public class CartProvider {
    public static final String KEY="cart_json";
    private SparseArray<ShoppingCart> datas=null;
    private Context context;
/*
    ShoppingCart temp;
*/
    public CartProvider(Context context){
        this.context=context;
        datas=new SparseArray<>(10);
        listToSparse();
    }

    public void put(ShoppingCart cart){
        ShoppingCart temp=datas.get(cart.getId().intValue());
        if(temp!=null){
            //Log.i("temp id is ",""+temp.getId().intValue());
            temp.setCount(temp.getCount()+1);
            Log.i("count is ",""+temp.getCount());

        }
        else {

            temp=cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(),temp);
        Log.i("put intValue is ",""+temp.getId().intValue());
        commit();
    }

    private void commit() {
        List<ShoppingCart>carts=sparseToList();
        PreferencesUtils.putString(context,KEY,JSONUtil.toJson(carts));
    }

    private List<ShoppingCart> sparseToList() {
        int size=datas.size();
        Log.i("size is ",""+size);
        List<ShoppingCart>list=new ArrayList<>(size);
        for(int i=0;i<size;i++){

           list.add(datas.valueAt(i));//根据指定位置返回指定值

        }
        return list;
    }

    private void listToSparse(){
        List<ShoppingCart>carts=getDataFromLocal();
        if(carts!=null&&carts.size()>0){
            for(ShoppingCart cart:carts){
                datas.put(cart.getId().intValue(),cart);
            }
        }
    }

    public void update(ShoppingCart cart){

        datas.put(cart.getId().intValue(),cart);
        Log.i("update id is ",""+cart.getId());
        commit();

    }

    public void sub(ShoppingCart cart){
       // Log.i("sub id is ",""+cart.getId());
        ShoppingCart temp=datas.get(cart.getId().intValue());
        if(temp!=null){
            if(temp.getCount()>1){//减到1不执行
                temp.setCount(temp.getCount()-1);
                Log.i("sub id is ",""+temp.getId());
                Log.i("sub  is ",""+temp.getCount());
            }
            else {
                temp.setCount(1);
            }
        }
        else {
            Log.i("temp  is ","null");
            temp=cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(),temp);
        Log.i("sub setCount is ",""+temp.getCount());//这里就一直为1
        commit();
    }

    public void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }

    public List<ShoppingCart> getAll(){
        return getDataFromLocal();

    }



    public void put(Wares wares){


        ShoppingCart cart = convertData(wares);
        put(cart);
    }
    private List<ShoppingCart> getDataFromLocal() {
        String json=PreferencesUtils.getString(context,KEY);
        List<ShoppingCart>carts=null;
        if(json!=null){
            carts=JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());
        }
        return carts;
    }

    public ShoppingCart convertData(Wares item){
        ShoppingCart cart = new ShoppingCart();
        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());
        return cart;
    }


}
