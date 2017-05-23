package com.longer.controller;

import com.longer.common.Common;
import com.longer.domain.MenuInfo;
import com.longer.domain.RoleInfo;
import com.longer.domain.UserInfo;
import com.longer.repository.MenuInfoRepository;
import com.longer.repository.RoleInfoRepository;
import com.longer.repository.UserInfoRepository;
import com.longer.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;

import static com.longer.common.Common.EncryptionStrBytes;

/**
 * Created by wujianlong on 2017/5/4.
 */
@Controller
public class LoginController {
    @Autowired
    LoginService loginService;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        return "login";
    }

    @RequestMapping(value="/loginout",method=RequestMethod.GET)
    public String loginout(HttpServletRequest request){
        //清空session
        Enumeration em = request.getSession().getAttributeNames();
        while(em.hasMoreElements()){
            request.getSession().removeAttribute(em.nextElement().toString());
        }
        return  "redirect:login";

    }


    @RequestMapping(value = "/surelogin", method = RequestMethod.POST)
    public String sureLogin(Model model, String username, String password, HttpServletRequest request) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            model.addAttribute("erroMessage", "账号或密码不能为空！");
            return "login";
        }
        try {
            boolean result = loginService.sureUserInfo(username, password, request, model);

            if (result == true) {
                return "redirect:main";
            } else {
                model.addAttribute("erroMessage", "账号或密码不正确！");
                return "login";
            }
        } catch (Exception ex) {
            model.addAttribute("erroMessage", ex.toString());
            return "login";
        }
    }


}
