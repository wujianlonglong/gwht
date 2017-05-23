package com.longer.business;

import com.longer.domain.*;
import com.longer.repository.*;
import com.longer.utils.constant.NormalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wujianlong on 2017/5/5.
 */
@Component
public class PowerBusiness {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    RolesUsersRepository rolesUsersRepository;

    @Autowired
    RolesMenusRepository rolesMenusRepository;

    @Autowired
    MenuInfoRepository menuInfoRepository;

    @Autowired
    RoleInfoRepository roleInfoRepository;

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, Map<String, UserInfo>> valueOperations;

    public UserInfo getUserByUserId(Long userId){
        return userInfoRepository.findByIdAndStatus(userId,NormalConstant.NORMAL_STATUS);
    }

    public UserInfo getUserByUserName(String userName){
//        String redisKey= RedisConstant.USER_INFO;
//        Map<String,UserInfo> userInfoMap=valueOperations.get(redisKey);
//        if(MapUtils.isNotEmpty(userInfoMap))
//        {
//            return userInfoMap.get(userName);
//        }
//        else {
//             List<UserInfo> userInfoList= userInfoRepository.findAll();
//             Map<String,UserInfo> tempMap=new HashMap<>();
//             userInfoList.stream().filter(obj->obj.getStatus()!= NormalConstant.DELETE_STATUS).forEach(obj->tempMap.put(obj.getUserName(),obj));
//            valueOperations.set(redisKey,tempMap);
//            return tempMap.get(userName);
//        }
        UserInfo userInfo=userInfoRepository.findByUserNameAndStatus(userName,NormalConstant.NORMAL_STATUS);
        return userInfo;
    }

    public List<RolesUsers> getRoleUser(Long userId){
        return rolesUsersRepository.findByUserIdAndStatus(userId,NormalConstant.NORMAL_STATUS);
    }
    public List<RolesUsers> getAllRoleUser(){
        return rolesUsersRepository.findByStatus(NormalConstant.NORMAL_STATUS);
    }

    public List<RolesMenus> getAllRoleMenu(){
       return  rolesMenusRepository.findByStatus(NormalConstant.NORMAL_STATUS);
    }


    public List<MenuInfo> getAllMenus(){
       // return menuInfoRepository.findByStatus(NormalConstant.NORMAL_STATUS);
        return menuInfoRepository.findAll();
    }



    public List<UserInfo> getUserInfo(){
       return userInfoRepository.findByStatus(NormalConstant.NORMAL_STATUS);
    }


    public UserInfo addUser(UserInfo userInfo){
        return  userInfoRepository.save(userInfo);
    }

    public RoleInfo addRole(RoleInfo roleInfo){
        return roleInfoRepository.save(roleInfo);
    }

    public int updateRole(String roleName,String comments,Integer showOder,Long roleId){
        return roleInfoRepository.updateRole(roleName,comments,showOder,roleId);
    }

    public int updateUser(UserInfo userInfo){
        return  userInfoRepository.updateUserById(userInfo.getDepartment(),userInfo.getNickName(),userInfo.getId());
    }

    public List<RoleInfo> getAllRoleInfo(){
        return roleInfoRepository.findByStatus(NormalConstant.NORMAL_STATUS);
    }


    /**
     *  更新用户角色列表
     * @param userId
     * @param roleList
     */
    @Transactional
    public void updateUserRole(Long userId,List<Long> roleList){
        rolesUsersRepository.deleteByUserId(userId);
        List<RolesUsers> rolesUsersList=new ArrayList<>();
        for (Long role : roleList) {
            RolesUsers rolesUsers=new RolesUsers();
            rolesUsers.setUserId(userId);
            rolesUsers.setRoleId(role);
            rolesUsers.setStatus(NormalConstant.NORMAL_STATUS);
            rolesUsersList.add(rolesUsers);
        }
        rolesUsersRepository.save(rolesUsersList);

    }


    /**
     * 删除菜单以及角色菜单
     * @param menuId 菜单编号
     */
    @Transactional
    public void deletMenuWithRoleMenu(Integer menuId){
        rolesMenusRepository.deleteByMenuId(menuId);
        menuInfoRepository.deleteByMenuId(menuId);
    }

    /**
     * 更新角色菜单列表
     * @param roleId
     * @param menuList
     */
    @Transactional
    public void updateRoleMenu(Long roleId,List<Integer> menuList){
        rolesMenusRepository.deleteByRoleId(roleId);
        List<RolesMenus> rolesMenusList=new ArrayList<>();
        for (Integer menuId : menuList) {
            RolesMenus rolesMenus=new RolesMenus();
            rolesMenus.setRoleId(roleId);
            rolesMenus.setMenuId(menuId);
            rolesMenus.setStatus(NormalConstant.NORMAL_STATUS);
            rolesMenusList.add(rolesMenus);
        }
        rolesMenusRepository.save(rolesMenusList);
    }

    /**
     * 更新密码
     * @param userId 用户编号
     * @param password 密码
     * @return
     */
    public int  updatePassword(Long userId,String password){
       return userInfoRepository.updatePassword(password,userId) ;
    }


    /**
     * 查询除了本角色编号外是否有相同角色名
     * @param roleName 角色名
     * @param roleId 角色ID
     */
    public RoleInfo getAnotherSameRole(String roleName,Long roleId){
        return roleInfoRepository.findByRoleNameAndRoleIdNot(roleName,roleId);
    }


    public MenuInfo addMenu(MenuInfo menuInfo){
        return menuInfoRepository.save(menuInfo);
    }


    public MenuInfo getMenuById(Integer menuId){
        return menuInfoRepository.findByMenuId(menuId);
    }


    public int deleteMenu(Integer menuId){
        return menuInfoRepository.deleteByMenuId(menuId);
    }

    public int deleteRoleMenu(Integer menuId){
        return rolesMenusRepository.deleteByMenuId(menuId);
    }

}
