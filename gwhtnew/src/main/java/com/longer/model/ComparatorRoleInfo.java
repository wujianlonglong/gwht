package com.longer.model;

import com.longer.domain.RoleInfo;

import java.util.Comparator;

/**
 * Created by wujianlong on 2017/5/16.
 */
public class ComparatorRoleInfo implements Comparator {

    public int compare(Object obj0, Object obj1) {
        RoleInfo roleInfo0 = (RoleInfo) obj0;
        RoleInfo roleInfo1 = (RoleInfo) obj1;
         int flag=roleInfo0.getShowOder().compareTo(roleInfo1.getShowOder());

         return flag;
    }
}
