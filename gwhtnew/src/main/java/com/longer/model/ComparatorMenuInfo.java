package com.longer.model;

import com.longer.domain.MenuInfo;

import java.util.Comparator;

/**
 * Created by wjll9 on 2017/5/9.
 */
public class ComparatorMenuInfo implements Comparator {

    public int compare(Object obj0, Object obj1) {
        MenuInfo menuInfo0=(MenuInfo)obj0;
        MenuInfo menuInfo1=(MenuInfo)obj1;
        //首先比较年龄，如果年龄相同，则比较名字

        //升序
        int flag=menuInfo0.getShowOrder().compareTo(menuInfo1.getShowOrder());
        //降序
       // int flag=menuInfo1.getShowOrder().compareTo(menuInfo0.getShowOrder());
        //注释部分为以menuId为排序对象
//        if(flag==0){
//            return menuInfo0.getMenuId().compareTo(menuInfo1.getMenuId());
//        }else{
//            return flag;
//        }
        return flag;
    }

}