package com.longer.service;

import com.longer.business.PowerBusiness;
import com.longer.common.Common;
import com.longer.domain.MenuInfo;
import com.longer.domain.RoleInfo;
import com.longer.domain.UserInfo;
import com.longer.model.ApiReturnBody;
import com.longer.model.ComparatorRoleInfo;
import com.longer.utils.constant.NormalConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.longer.common.Common.EncryptionStrBytes;

/**
 * Created by wujianlong on 2017/5/10.
 */
@Service
public class PowerService {
    private static final Logger log = LoggerFactory.getLogger(PowerService.class);

    @Autowired
    RedisService redisService;

    @Autowired
    PowerBusiness powerBusiness;

    public List<UserInfo> getUserInfo() {
        List<UserInfo> result = redisService.getUserInfo();
        return result;
    }



    public ApiReturnBody updateUser(UserInfo userInfo) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            int conut = powerBusiness.updateUser(userInfo);

            redisService.clearUserInfo();//从redis清空用户信息的缓存

            result.setMsg("更新用户成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
        } catch (Exception ex) {
            log.error("更新用户失败：" + ex.toString());
            result.setMsg("更新用户失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }


    public ApiReturnBody<UserInfo> addUser(UserInfo userInfo) {
        ApiReturnBody<UserInfo> result = new ApiReturnBody<>();
        try {
            if (isExitUser(userInfo.getUserName())) {
                log.error("新增用户失败：" + userInfo.getUserName() + "已存在该用户名");

                result.setMsg("新增用户失败：" + userInfo.getUserName() + "已存在该用户名");
                result.setCode(NormalConstant.FAILE_CODE);
                result.setSuccess(false);
                return result;
            }

            userInfo.setStatus(NormalConstant.NORMAL_STATUS);
            String password = Common.BytesConvertToHexString(EncryptionStrBytes(userInfo.getPassWord(), "MD5")).toUpperCase();
            userInfo.setPassWord(password);
            UserInfo userInfo1 = powerBusiness.addUser(userInfo);

            redisService.clearUserInfo();//从redis清空用户信息的缓存

            result.setMsg("新增用户成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(userInfo1);
        } catch (Exception ex) {
            log.error("新增用户失败：" + ex.toString());

            result.setMsg("新增用户失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }
        return result;
    }

    public boolean isExitUser(String userName) {
        boolean result = false;
        List<UserInfo> userInfos = redisService.getUserInfo();
        for (UserInfo userInfo : userInfos) {
            if (userInfo.getUserName().equals(userName)) {
                result = true;
                break;
            }
        }
        return result;
    }


    public boolean isExitRole(String roleName) {
        boolean result = false;
        List<RoleInfo> roleInfos = redisService.getRoleInfo(null);
        for (RoleInfo roleInfo : roleInfos) {
            if (roleInfo.getRoleName().equals(roleName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean isExitRoleName(String roleName, Long roleId) {
        boolean result = false;
        RoleInfo roleInfo = powerBusiness.getAnotherSameRole(roleName, roleId);
        if (roleInfo != null) {
            result = true;
        }

        return result;
    }

    public ApiReturnBody<List<RoleInfo>> getRoleInfoByUserId(Long userId) {
        ApiReturnBody<List<RoleInfo>> result = new ApiReturnBody<>();
        try {
            List<Long> roleList = redisService.getUserRoles(userId);
            List<RoleInfo> roleInfos = redisService.getRoleInfo(null);
            ComparatorRoleInfo comparatorRoleInfo = new ComparatorRoleInfo();
            Collections.sort(roleInfos, comparatorRoleInfo);
            if (CollectionUtils.isNotEmpty(roleList)) {
                roleInfos.forEach(role -> {
                    if (roleList.contains(role.getRoleId())) {
                        role.setChooseRole(1);
                    }
                });
            }
            result.setMsg("获取用户 " + userId + " 的角色信息成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(roleInfos);
        } catch (Exception ex) {
            log.error("获取用户 " + userId + " 的角色信息失败：" + ex.toString());

            result.setMsg("获取用户 " + userId + " 的角色信息失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }
        return result;
    }

    public ApiReturnBody<List<MenuInfo>> getMenu(Long roleId) {
        ApiReturnBody<List<MenuInfo>> result = new ApiReturnBody<>();
        try {
            List<Integer> menusList = redisService.getRoleMenus(roleId);//获取该角色所有菜单
            List<MenuInfo> menuInfos = redisService.getDetailMenus(null);//获取所有的菜单信息
            menuInfos=menuInfos.stream().filter(obj->obj.getStatus().equals(NormalConstant.NORMAL_STATUS)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(menusList)) {
                for (MenuInfo menuInfo : menuInfos) {
                    if (menusList.contains(menuInfo.getMenuId()))
                        menuInfo.setChooseMenu(1);
                }
            }
            List<MenuInfo> menuTree = getMenuTreeList(0, menuInfos);

            result.setMsg("获取角色" + roleId + "菜单成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(menuTree);
        } catch (Exception ex) {
            log.error("获取角色" + roleId + "菜单失败：" + ex.toString());

            result.setMsg("获取角色" + roleId + "菜单失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;

        }
        return result;
    }


    /**
     * 递归获取菜单树
     *
     * @param menuId   父菜单id
     * @param allMenus 所有的菜单
     * @return
     */
    public List<MenuInfo> getMenuTreeList(Integer menuId, List<MenuInfo> allMenus) {
        List<MenuInfo> lists = new ArrayList<>();
        List<MenuInfo> filterMenu = allMenus.stream().filter(menu -> menu.getParentMenuId().equals(menuId)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(filterMenu)) {
            filterMenu.sort(Comparator.comparing(MenuInfo::getShowOrder));
            for (MenuInfo menu : filterMenu) {
                menu.setChildMenus(getMenuTreeList(menu.getMenuId(), allMenus));
                lists.add(menu);
            }
        }
        return lists;
    }


    public ApiReturnBody<List<RoleInfo>> getAllRoleInfo() {
        ApiReturnBody<List<RoleInfo>> result = new ApiReturnBody<>();

        try {
            List<RoleInfo> roleInfos = redisService.getRoleInfo(null);
            result.setMsg("获取角色信息成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(roleInfos);
        } catch (Exception ex) {
            log.error("获取用户 角色信息失败：" + ex.toString());

            result.setMsg("获取用户角色信息失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }


    public ApiReturnBody updateUserRole(Long userId, List<Long> roleList) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            powerBusiness.updateUserRole(userId, roleList);
            redisService.clearUserRole();
            result.setMsg("更新用户" + userId + " 角色信息成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
        } catch (Exception ex) {
            log.error("更新用户" + userId + " 角色信息失败：" + ex.toString());

            result.setMsg("更新用户" + userId + " 角色信息失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }
        return result;
    }

    public ApiReturnBody updateRoleMenu(Long roleId, List<Integer> menuList) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            powerBusiness.updateRoleMenu(roleId, menuList);
            redisService.clearRoleMenus();
            result.setMsg("更新角色" + roleId + " 菜单信息成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
        } catch (Exception ex) {
            log.error("更新角色" + roleId + " 菜单信息失败：" + ex.toString());

            result.setMsg("更新角色" + roleId + " 菜单信息失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }


        return result;
    }


    public ApiReturnBody initPassword(Long userId) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            String initPassword = "123456";
            initPassword = Common.BytesConvertToHexString(EncryptionStrBytes(initPassword, "MD5")).toUpperCase();
            int count = powerBusiness.updatePassword(userId, initPassword);
            redisService.clearUserInfo();//从redis清空用户信息的缓存
            result.setMsg("初始化用户" + userId + " 密码成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
        } catch (Exception ex) {
            log.error("初始化用户" + userId + " 密码失败：" + ex.toString());

            result.setMsg("初始化用户" + userId + " 密码失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }


    public ApiReturnBody<RoleInfo> addRole(RoleInfo roleInfo) {
        ApiReturnBody<RoleInfo> result = new ApiReturnBody<>();
        try {

            if (isExitRole(roleInfo.getRoleName())) {
                log.error("新增角色失败：" + roleInfo.getRoleName() + "已存在该角色名");

                result.setMsg("新增角色失败：" + roleInfo.getRoleName() + "已存在该角色名");
                result.setCode(NormalConstant.FAILE_CODE);
                result.setSuccess(false);
                return result;
            }
            roleInfo.setStatus(NormalConstant.NORMAL_STATUS);
            RoleInfo roleInfo1 = powerBusiness.addRole(roleInfo);

            redisService.clearRoleInfo();//从redis清空角色信息的缓存

            result.setMsg("新增角色成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(roleInfo1);

        } catch (Exception ex) {
            log.error("新增角色失败：" + ex.toString());

            result.setMsg("新增角色失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }


    public ApiReturnBody updateRole(RoleInfo roleInfo) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            if (isExitRoleName(roleInfo.getRoleName(), roleInfo.getRoleId())) {
                log.error("更新角色失败：" + roleInfo.getRoleName() + "已存在该角色名");

                result.setMsg("更新角色失败：" + roleInfo.getRoleName() + "已存在该角色名");
                result.setCode(NormalConstant.FAILE_CODE);
                result.setSuccess(false);
                return result;
            }

            int count = powerBusiness.updateRole(roleInfo.getRoleName(), roleInfo.getComments(), roleInfo.getShowOder(), roleInfo.getRoleId());
            redisService.clearRoleInfo();//从redis清空角色信息的缓存

            result.setMsg("更新角色成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);

        } catch (Exception ex) {
            log.error("更新角色失败：" + ex.toString());

            result.setMsg("更新角色失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }

    public ApiReturnBody<List<MenuInfo>> getMenuTree(Integer rootParentId) {
        ApiReturnBody<List<MenuInfo>> result = new ApiReturnBody<>();
        try {
            List<MenuInfo> menuInfos = redisService.getDetailMenus(null);//获取所有的菜单信息
            List<MenuInfo> menuTree = getMenuTreeList(rootParentId, menuInfos);

            result.setMsg("获取菜单成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(menuTree);
        } catch (Exception ex) {
            log.error("获取菜单失败：" + ex.toString());

            result.setMsg("获取菜单失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }

    public ApiReturnBody addMenu(MenuInfo menuInfo) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            Integer menuId = menuInfo.getMenuId();
            if (isExitMenu(menuId)) {
                log.error("新增" + menuId + "菜单失败：该编号已存在！");

                result.setMsg("新增" + menuId + "菜单失败：该编号已存在！");
                result.setCode(NormalConstant.FAILE_CODE);
                result.setSuccess(false);
                return result;
            }

            MenuInfo menuInfo1 = powerBusiness.addMenu(menuInfo);
            redisService.clearMenuInfo();//从redis清空菜单信息的缓存

            result.setMsg("新增菜单成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(menuInfo1);

        } catch (Exception ex) {
            log.error("新增菜单失败：" + ex.toString());

            result.setMsg("新增菜单失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }

    public boolean isExitMenu(Integer menuId) {
        boolean result = false;
         List<MenuInfo> menuInfos = redisService.getDetailMenus(null);
        for (MenuInfo menuInfo : menuInfos) {
            if (menuInfo.getMenuId().equals(menuId)) {
                result = true;
                break;
            }
        }
//        MenuInfo menuInfo = powerBusiness.getMenuById(menuId);
//        if (menuInfo != null) {
//            result = true;
//        }

        return result;
    }


    public ApiReturnBody<MenuInfo> getPaAndChilMenu(Integer menuId) {
        ApiReturnBody<MenuInfo> result = new ApiReturnBody<>();
        try {
            List<MenuInfo> menuInfos = redisService.getDetailMenus(null);
            MenuInfo menuInfo = menuInfos.stream().filter(obj -> obj.getMenuId().equals(menuId)).findFirst().orElse(null);
            if (menuInfo == null) {
                if (menuId.equals(0)) {
                    //root根菜单
                    menuInfo = new MenuInfo();
                    menuInfo.setStatus(NormalConstant.NORMAL_STATUS);
                    menuInfo.setMenuName("root");
                    menuInfo.setMenuId(0);
                } else {
                    log.error("获取菜单失败：没有编号为" + menuId + "的菜单！");

                    result.setMsg("获取菜单失败：没有编号为" + menuId + "的菜单！");
                    result.setCode(NormalConstant.FAILE_CODE);
                    result.setSuccess(false);
                    return result;
                }
            }
            List<MenuInfo> childMenus = menuInfos.stream().filter(obj -> obj.getParentMenuId().equals(menuId)).collect(Collectors.toList());
            childMenus.sort(Comparator.comparing(MenuInfo::getShowOrder));
            menuInfo.setChildMenus(childMenus);

            result.setMsg("获取菜单成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(menuInfo);

        } catch (Exception ex) {
            log.error("获取菜单失败：" + ex.toString());

            result.setMsg("获取菜单失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }


    public ApiReturnBody updateMenu(MenuInfo menuInfo) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            MenuInfo menuInfo1 = powerBusiness.addMenu(menuInfo);
            redisService.clearMenuInfo();//从redis清空菜单信息的缓存

            result.setMsg("更新菜单成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setData(menuInfo1);
        } catch (Exception ex) {
            log.error("更新菜单失败：" + ex.toString());

            result.setMsg("更新菜单失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        return result;
    }



    public ApiReturnBody deleteMenu(Integer menuId){
        ApiReturnBody result=new ApiReturnBody();
        try{
            powerBusiness.deletMenuWithRoleMenu(menuId);
            redisService.clearRoleMenus();//从redis清空角色菜单信息的缓存
            redisService.clearMenuInfo();//从redis清空菜单信息的缓存

            result.setMsg("删除菜单"+menuId+"成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
        }
        catch(Exception ex){
            log.error("删除菜单"+menuId+"失败：" + ex.toString());

            result.setMsg("删除菜单"+menuId+"失败：" + ex.toString());
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;

        }

        return result;
    }


}
