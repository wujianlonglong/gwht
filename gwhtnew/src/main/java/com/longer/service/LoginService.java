package com.longer.service;

import com.longer.business.PowerBusiness;
import com.longer.common.Common;
import com.longer.domain.UserInfo;
import com.longer.repository.UserInfoRepository;
import com.longer.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.longer.common.Common.EncryptionStrBytes;

/**
 * Created by wujianlong on 2017/5/5.
 */
@Service
public class LoginService {

    private static final Logger log= LoggerFactory.getLogger(LoginService.class);

    @Autowired
    PowerBusiness powerBusiness;

    public boolean sureUserInfo(String username, String password, HttpServletRequest request,Model model) {
        boolean result = false;
        UserInfo userInfo = powerBusiness.getUserByUserName(username);
        if (userInfo != null) {
            password = Common.BytesConvertToHexString(EncryptionStrBytes(password, "MD5")).toUpperCase();
            if (password.equals(userInfo.getPassWord())) {
                result = true;
                userInfo.setPassWord(null);
                request.getSession().setAttribute("USER_INFO", JsonUtil.objectToString(userInfo));
                //request.getSession().setMaxInactiveInterval(1200);
             //   model.addAttribute("userInfo",userInfo);
            }
        }
        return result;
    }

}
