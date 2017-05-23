package com.longer.service;

import com.longer.business.PowerBusiness;
import com.longer.common.Common;
import com.longer.domain.MenuInfo;
import com.longer.domain.UserInfo;
import com.longer.model.ApiReturnBody;
import com.longer.utils.constant.NormalConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.longer.common.Common.EncryptionStrBytes;

/**
 * Created by wujianlong on 2017/5/9.
 */
@Service
public class MainService {


    @Autowired
    PowerBusiness powerBusiness;

    @Autowired
    RedisService redisService;

    public List<MenuInfo> getRoleMenu(Long userId) {
        List<Integer> allMenusList = new ArrayList<>();
        List<Long> rolesList = redisService.getUserRoles(userId);//获取该用户所有角色
        rolesList.forEach(roleId -> {
            List<Integer> menusList = redisService.getRoleMenus(roleId);//获取该角色所有菜单
            if (CollectionUtils.isNotEmpty(menusList))
                allMenusList.addAll(menusList);
        });
        List<Integer> noSameMenusList = new ArrayList<Integer>(new LinkedHashSet<>(allMenusList));//去重

        List<MenuInfo> menuInfos = redisService.getDetailMenus(noSameMenusList);//获取具体的菜单信息
        menuInfos = menuInfos.stream().filter(obj -> obj.getStatus().equals(NormalConstant.NORMAL_STATUS)).collect(Collectors.toList());
        List<MenuInfo> menuTree = getMenuTreeList(0, menuInfos);
        return menuTree;
//        //key:父菜单
//        Map<Integer, List<MenuInfo>> detailMenusMap = menuInfos.stream().collect(Collectors.groupingBy(MenuInfo::getParentMenuId));
//        List<MenuInfo> parentMenus = detailMenusMap.get(0);//获取父级菜单
//        Map<Integer, MenuInfo> parentMenusMap = new HashMap<>();
//        parentMenus.forEach(p -> {
//            parentMenusMap.put(p.getMenuId(), p);
//        });
//        detailMenusMap.remove(0);//移除父级菜单
//        for (Map.Entry<Integer, List<MenuInfo>> entry : detailMenusMap.entrySet()) {
//            if (parentMenusMap.containsKey(entry.getKey()))
//                parentMenusMap.get(entry.getKey()).getChildMenus().addAll(entry.getValue());
//        }
//
//        //按序号从小到大排序
//        ComparatorMenuInfo comparatorMenuInfo = new ComparatorMenuInfo();
//        Collections.sort(parentMenus, comparatorMenuInfo);
//        parentMenus.forEach(p -> {
//            Collections.sort(p.getChildMenus(), comparatorMenuInfo);
//        });
//
//
//        return parentMenus;

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

    public ApiReturnBody changePassword(String pwd1, String pwd2, String pwd3, Long userId) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            UserInfo userInfo = powerBusiness.getUserByUserId(userId);
            if (userInfo == null) {
                result.setMsg("用户" + userId + "不存在或者被删除！");
//                result.setCode(NormalConstant.FAILE_CODE);
//                result.setSuccess(false);
                return result;
            }
            String password = userInfo.getPassWord();
            pwd1 = Common.BytesConvertToHexString(EncryptionStrBytes(pwd1, "MD5")).toUpperCase();
            if (!password.equals(pwd1)) {
                result.setMsg("原密码不正确！");
//                result.setCode(NormalConstant.FAILE_CODE);
//                result.setSuccess(false);
                return result;
            }
            pwd2= Common.BytesConvertToHexString(EncryptionStrBytes(pwd2, "MD5")).toUpperCase();
            int count = powerBusiness.updatePassword(userId, pwd2);
            redisService.clearUserInfo();//从redis清空用户信息的缓存
            result.setMsg("修改用户" + userId + " 密码成功！");
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);

        } catch (Exception ex) {
            result.setMsg("修改密码失败：" + ex.toString());
//            result.setCode(NormalConstant.FAILE_CODE);
//            result.setSuccess(false);
            return result;
        }
        return result;
    }
}
