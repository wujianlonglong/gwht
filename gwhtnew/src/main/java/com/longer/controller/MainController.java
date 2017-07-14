package com.longer.controller;

import com.longer.domain.MenuInfo;
import com.longer.domain.UserInfo;
import com.longer.model.ApiReturnBody;
import com.longer.service.MainService;
import com.longer.utils.JsonUtil;
import com.longer.utils.constant.NormalConstant;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wujianlong on 2017/5/9.
 */
@Controller
public class MainController {

    @Autowired
    MainService mainService;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String getRoleMenu(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USER_INFO") == null) {
//            ScriptEngineManager sem = new ScriptEngineManager();
//            ScriptEngine se = sem.getEngineByName("js");
//            try
//            {
//                String script = "function say(){ alert('session 失效请重新登录！'); }";
//                se.eval(script);
//                Invocable inv2 = (Invocable) se;
//                String res=(String)inv2.invokeFunction("say");
//                System.out.println(res);
//            }
//            catch(Exception e)
//            {
//                e.printStackTrace();
//            }

            return "redirect:login";
        }
        UserInfo userInfo = JsonUtil.jsonToObject((String) request.getSession().getAttribute("USER_INFO"), UserInfo.class);
        Long userId = userInfo.getId();
        List<MenuInfo> parentMenus = mainService.getRoleMenu(userId);

        model.addAttribute("userInfo", userInfo);
        model.addAttribute("menulist", parentMenus);
        return "main";
    }


    /**
     * 修改密码
     *
     * @param pwd1    原密码
     * @param pwd2    新密码
     * @param pwd3    重复新密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody changePassword(String pwd1, String pwd2, String pwd3, HttpServletRequest request) {
        ApiReturnBody result = new ApiReturnBody();
        if (request.getSession().getAttribute("USER_INFO") == null) {
            result.setMsg("Session过期，请退出重新登录！");
//            result.setCode(NormalConstant.FAILE_CODE);
//            result.setSuccess(false);
            return result;
        }
        if (StringUtils.isEmpty(pwd1) || StringUtils.isEmpty(pwd2) || StringUtils.isEmpty(pwd3)) {
            result.setMsg("原密码、新密码、重复新密码不能为空！");
//            result.setCode(NormalConstant.FAILE_CODE);
//            result.setSuccess(false);
            return result;
        } else if (!pwd2.equals(pwd3)) {
            result.setMsg("两次重复的密码不一致！");
//            result.setCode(NormalConstant.FAILE_CODE);
//            result.setSuccess(false);
            return result;
        }
        UserInfo userInfo = JsonUtil.jsonToObject((String) request.getSession().getAttribute("USER_INFO"), UserInfo.class);
        Long userId = userInfo.getId();
        result = mainService.changePassword(pwd1, pwd2, pwd3, userId);
        return result;
    }


    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String homepage() {
        return "homepage";
    }

    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    public String userinfo() {
        return "power/userinfo";
    }


    @RequestMapping(value = "/roleinfo", method = RequestMethod.GET)
    public String roleinfo() {
        return "power/roleinfo";
    }

    @RequestMapping(value = "/menuinfo", method = RequestMethod.GET)
    public String menuinfo() {
        return "power/menuinfo";
    }


    @RequestMapping(value = "/newslist", method = RequestMethod.GET)
    public String newslist() {
        return "news/newslist";
    }


    @RequestMapping(value = "/memorabilialist", method = RequestMethod.GET)
    public String memorabilialist() {
        return "news/memorabilialist";
    }

    @RequestMapping(value = "/importstore", method = RequestMethod.GET)
    public String importstore() {
        return "store/importstore";
    }
}
