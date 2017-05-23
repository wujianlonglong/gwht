package com.longer.controller;

import com.longer.model.ApiReturnBody;
import com.longer.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wujianlong on 2017/5/9.
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    RedisService redisService;

    @RequestMapping(value="/refreshUserInfo",method= RequestMethod.POST)
    public ApiReturnBody refreshUserInfo(){
        ApiReturnBody result=redisService.clearUserInfo();
        return result;
    }

    @RequestMapping(value="/refreshRoleInfo",method=RequestMethod.POST)
    public ApiReturnBody refreshRoleInfo(){
        ApiReturnBody result=redisService.clearRoleInfo();
        return result;
    }

    @RequestMapping(value="/refreshMenuInfo",method=RequestMethod.POST)
    public ApiReturnBody refreshMenuInfo(){
        ApiReturnBody result=redisService.clearMenuInfo();

        return result;
    }



}
