package com.longer.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wujianlong on 2017/5/5.
 */

@Data
public class RolesMenusPk implements Serializable {

    private Long roleId;


    private Integer menuId;
}
