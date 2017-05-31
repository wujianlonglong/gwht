package com.longer.service;

import com.longer.business.PowerBusiness;
import com.longer.domain.*;
import com.longer.model.ApiReturnBody;
import com.longer.repository.RolesUsersRepository;
import com.longer.utils.constant.NormalConstant;
import com.longer.utils.constant.RedisConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.MapUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wujianlong on 2017/5/9.
 */
@Service
public class RedisService {

    private static final Logger log = LoggerFactory.getLogger(RedisService.class);

    private static Object lockOne = new Object();

    private static Object lockTwo = new Object();

    private static Object lockThree = new Object();

    private static Object lockFour = new Object();

    private static Object lockFive = new Object();

    @Autowired
    PowerBusiness powerBusiness;


    @Resource(name = "redisTemplate")
    protected ValueOperations<String, Map<Long, List<Long>>> roleuserOperations;

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, Map<Long, List<Integer>>> rolemenuOperations;

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, List<MenuInfo>> menusOperations;

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, List<UserInfo>> userinfoOperations;

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, List<RoleInfo>> roleinfoOperations;


    @Resource(name = "redisTemplate")
    protected ValueOperations<String, List<NewsInfoTest>> newsTestValueOperation;

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, List<Memorabilia>> memorabiliaValueOperation;

    public List<Long> getUserRoles(Long userId) {
        List<Long> rolesUsersList = new ArrayList<>();
        String redisKey = RedisConstant.USER_ROLES;
        Map<Long, List<Long>> map = roleuserOperations.get(redisKey);
        if (MapUtils.isEmpty(map)) {
            synchronized (lockOne) {
                map = roleuserOperations.get(redisKey);
                if (MapUtils.isEmpty(map)) {
                    List<RolesUsers> list = powerBusiness.getAllRoleUser();
                    map = new HashMap<>();
                    for (RolesUsers obj : list) {
                        if (map.containsKey(obj.getUserId())) {
                            map.get(obj.getUserId()).add(obj.getRoleId());
                        } else {
                            List<Long> l = new ArrayList<>();
                            l.add(obj.getRoleId());
                            map.put(obj.getUserId(), l);
                        }
                    }
                    roleuserOperations.set(redisKey, map);
                }
            }
        }
        // rolesUsersList = map.get(userId.toString());//由于Long类型的存入redis转变成String类型所以userId需用String类型,单第一次存入redis取不到值,所以重新从redis读取map
        rolesUsersList = roleuserOperations.get(redisKey).get(userId.toString());
        return rolesUsersList;
    }


    public List<Integer> getRoleMenus(Long roleId) {
        List<Integer> result = new ArrayList<>();
        String redisKey = RedisConstant.Role_Menus;
        Map<Long, List<Integer>> map = rolemenuOperations.get(redisKey);
        if (MapUtils.isEmpty(map)) {
            synchronized (lockTwo) {
                map = rolemenuOperations.get(redisKey);
                if (MapUtils.isEmpty(map)) {
                    List<RolesMenus> list = powerBusiness.getAllRoleMenu();
                    map = new HashMap<>();
                    for (RolesMenus obj : list) {
                        if (map.containsKey(obj.getRoleId())) {
                            map.get(obj.getRoleId()).add(obj.getMenuId());
                        } else {
                            List<Integer> l = new ArrayList<>();
                            l.add(obj.getMenuId());
                            map.put(obj.getRoleId(), l);
                        }
                    }
                    rolemenuOperations.set(redisKey, map);
                }
            }
        }

        // result = map.get(roleId.toString());
        result = rolemenuOperations.get(redisKey).get(roleId.toString());

        return result;
    }

    public void clearRoleMenus() {
        String redisKey = RedisConstant.Role_Menus;
        rolemenuOperations.set(redisKey, null);
    }


    public List<MenuInfo> getDetailMenus(List<Integer> menuList) {
        String redisKey = RedisConstant.All_Menus;
        List<MenuInfo> menuInfoList = menusOperations.get(redisKey);
        if (CollectionUtils.isEmpty(menuInfoList)) {
            synchronized (lockThree) {
                menuInfoList = menusOperations.get(redisKey);
                if (CollectionUtils.isEmpty(menuInfoList)) {
                    menuInfoList = powerBusiness.getAllMenus();
                    menusOperations.set(redisKey, menuInfoList);
                }
            }
        }
        if (menuList == null)
            return menuInfoList;

        List<MenuInfo> result = menuInfoList.stream().filter(obj -> menuList.contains(obj.getMenuId())).collect(Collectors.toList());
        return result;
    }

    public void clearDetailMenus() {
        String redisKey = RedisConstant.All_Menus;
        menusOperations.set(redisKey, null);
    }


    public List<UserInfo> getUserInfo() {
        String redisKey = RedisConstant.All_USER_INFO;
        List<UserInfo> userInfos = userinfoOperations.get(redisKey);
        if (CollectionUtils.isEmpty(userInfos)) {
            synchronized (lockFour) {
                userInfos = userinfoOperations.get(redisKey);
                if (CollectionUtils.isEmpty(userInfos)) {
                    userInfos = powerBusiness.getUserInfo();
                    userInfos.forEach(obj -> {
                        obj.setPassWord(null);
                    });
                    userinfoOperations.set(redisKey, userInfos);
                }
            }
        }
        return userInfos;
    }

    /**
     * 清空用户redis缓存数据
     *
     * @return
     */
    public ApiReturnBody clearUserInfo() {
        String redisKey = RedisConstant.All_USER_INFO;
        ApiReturnBody result = new ApiReturnBody();
        try {
            userinfoOperations.set(redisKey, null);
            result.setSuccess(true);
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setMsg("清空用户缓存成功！");
        } catch (Exception ex) {
            log.error("清空用户缓存失败：" + ex.toString());
            result.setMsg("清空用户缓存失败!");
            return result;
        }
        return result;
    }


    /**
     * 清空角色缓存
     *
     * @return
     */
    public ApiReturnBody clearRoleInfo() {
        String redisKey = RedisConstant.All_ROLE_INFO;
        ApiReturnBody result = new ApiReturnBody();
        try {
            roleinfoOperations.set(redisKey, null);
            result.setSuccess(true);
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setMsg("清空角色缓存成功！");
        } catch (Exception ex) {
            log.error("清空角色缓存失败：" + ex.toString());
            result.setMsg("清空角色缓存失败!");
            return result;
        }

        return result;
    }


    public List<RoleInfo> getRoleInfo(List<Long> roleList) {
        String redisKey = RedisConstant.All_ROLE_INFO;
        List<RoleInfo> roleInfoList = roleinfoOperations.get(redisKey);
        if (CollectionUtils.isEmpty(roleInfoList)) {
            synchronized (lockFive) {
                roleInfoList = roleinfoOperations.get(redisKey);
                if (CollectionUtils.isEmpty(roleInfoList)) {
                    roleInfoList = powerBusiness.getAllRoleInfo();
                    roleinfoOperations.set(redisKey, roleInfoList);
                }
            }
        }
        if (roleList == null)
            return roleInfoList;

        List<RoleInfo> result = roleInfoList.stream().filter(obj -> roleList.contains(obj.getRoleId())).collect(Collectors.toList());
        return result;
    }

    public ApiReturnBody clearUserRole() {
        String redisKey = RedisConstant.USER_ROLES;
        ApiReturnBody result = new ApiReturnBody();
        try {
            roleuserOperations.set(redisKey, null);
            result.setSuccess(true);
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setMsg("清空用户角色缓存成功！");
        } catch (Exception ex) {
            log.error("清空用户角色缓存失败：" + ex.toString());
            result.setMsg("清空用户角色缓存失败!");
            return result;
        }
        return result;
    }


    public ApiReturnBody clearMenuInfo() {
        String redisKey = RedisConstant.All_Menus;
        ApiReturnBody result = new ApiReturnBody();
        try {
            menusOperations.set(redisKey, null);
            result.setSuccess(true);
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setMsg("清空菜单缓存成功！");
        } catch (Exception ex) {
            log.error("清空菜单缓存失败：" + ex.toString());
            result.setMsg("清空菜单缓存失败!");
            return result;
        }
        return result;
    }


    public ApiReturnBody clearNewInfoTest() {
        String redisKey = RedisConstant.ALL_NEWS_INFO_TEST;
        ApiReturnBody result = new ApiReturnBody();
        try {
            newsTestValueOperation.set(redisKey, null);
            result.setSuccess(true);
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setMsg("清空新闻信息列表缓存成功！");
        } catch (Exception ex) {
            log.error("清空新闻信息列表缓存失败：" + ex.toString());
            result.setMsg("清空新闻信息列表缓存失败!");
            return result;
        }
        return result;
    }

    public ApiReturnBody clearMemorabilia() {
        String redisKey = RedisConstant.All_MEMORABILIA;
        ApiReturnBody result = new ApiReturnBody();
        try {
            memorabiliaValueOperation.set(redisKey, null);
            result.setSuccess(true);
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setMsg("清空大事记列表缓存成功！");
        } catch (Exception ex) {
            log.error("清空大事记列表缓存失败：" + ex.toString());
            result.setMsg("清空大事记列表缓存失败!");
            return result;
        }
        return result;
    }


}
