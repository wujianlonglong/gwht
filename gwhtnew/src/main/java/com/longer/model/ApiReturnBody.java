package com.longer.model;

import com.longer.utils.constant.NormalConstant;
import lombok.Data;

/**
 * Created by wujianlong on 2017/5/10.
 */
@Data
public class ApiReturnBody<T> {
    private String  msg;

    private boolean success;

    private String code;

    private T Data;

    public ApiReturnBody(){
        this.success=false;
        this.code= NormalConstant.FAILE_CODE;
    }



}
