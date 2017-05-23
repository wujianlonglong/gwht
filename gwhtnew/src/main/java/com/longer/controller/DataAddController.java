package com.longer.controller;

import com.longer.common.Common;
import com.longer.domain.MenuInfo;
import com.longer.domain.RoleInfo;
import com.longer.domain.RolesUsers;
import com.longer.domain.UserInfo;
import com.longer.repository.MenuInfoRepository;
import com.longer.repository.RoleInfoRepository;
import com.longer.repository.RolesUsersRepository;
import com.longer.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.longer.common.Common.EncryptionStrBytes;

/**
 * Created by wujianlong on 2017/5/8.
 */
@Controller
@RequestMapping("/dataadd")
public class DataAddController {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    MenuInfoRepository menuInfoRepository;

    @Autowired
    RoleInfoRepository roleInfoRepository;

    @Autowired
    RolesUsersRepository rolesUsersRepository;

    @RequestMapping(value = "/uesradd", method = RequestMethod.GET)
    public void uesradd() {
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName("管理员");
        userInfo.setUserName("admin");
        String password = Common.BytesConvertToHexString(EncryptionStrBytes("123456", "MD5")).toUpperCase();
        userInfo.setPassWord(password);
        userInfo.setDepartment("全渠道");
        userInfo.setStatus(1);
        userInfoRepository.save(userInfo);
    }


    @RequestMapping(value = "/menuadd", method = RequestMethod.GET)
    public void menuadd() {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setMenuId(1000);
        menuInfo.setMenuName("测试菜单");
        menuInfo.setMenuUrl("ceshi");
        menuInfo.setParentMenuId(0);
        menuInfo.setShowOrder(1);
        menuInfo.setComments("测试备注");
        menuInfo.setStatus(1);
        menuInfoRepository.save(menuInfo);

    }
    @RequestMapping(value = "/menuaddch", method = RequestMethod.GET)
    public void menuaddch() {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setMenuId(1001);
        menuInfo.setMenuName("测试子菜单");
        menuInfo.setMenuUrl("ceshichild");
        menuInfo.setParentMenuId(1000);
        menuInfo.setShowOrder(1);
        menuInfo.setComments("测试子菜单");
        menuInfo.setStatus(1);
        menuInfoRepository.save(menuInfo);

    }

    @RequestMapping(value = "/roleadd", method = RequestMethod.GET)
    public void roleadd() {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleName("开发");
        roleInfo.setShowOder(3);
        roleInfo.setComments("开发");
        roleInfo.setStatus(1);
        roleInfoRepository.save(roleInfo);
    }

    @RequestMapping(value="/roleuseradd",method = RequestMethod.GET)
    public void roleuseradd(){
        RolesUsers rolesUsers=new RolesUsers();
        rolesUsers.setRoleId(21L);
        rolesUsers.setUserId(5L);
        rolesUsers.setStatus(1);
        rolesUsersRepository.save(rolesUsers);
    }


}
