package com.longer.controller;

import com.longer.domain.StoreInfos;
import com.longer.repository.StoreInfosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujianlong on 2017/7/13.
 */
@Controller
@RequestMapping("/store")
public class StoreController {


    @Autowired
    StoreInfosRepository storeInfosRepository;

    @RequestMapping(value ="/getStoreInfos",method= RequestMethod.GET)
    @ResponseBody
    public List<StoreInfos> getStoreInfos(){
        List<StoreInfos> storeInfosList=new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.ASC, "shopAdminArea");
        storeInfosList=storeInfosRepository.findAll(sort);
        return storeInfosList;
    }
}
