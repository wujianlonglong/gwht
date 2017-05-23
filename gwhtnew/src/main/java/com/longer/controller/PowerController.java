package com.longer.controller;

import com.longer.common.Common;
import com.longer.domain.MenuInfo;
import com.longer.domain.RoleInfo;
import com.longer.domain.UserInfo;
import com.longer.model.ApiReturnBody;
import com.longer.model.ComparatorRoleInfo;
import com.longer.service.PowerService;
import com.longer.utils.constant.NormalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wujianlong on 2017/5/10.
 */
@Controller
public class PowerController {


    @Autowired
    Common common;

    @Autowired
    PowerService powerService;


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public String getUserInfo(Model model, HttpServletRequest request, int page, int size, String key) {
        if (request.getSession().getAttribute("USER_INFO") == null)
            return "redirect:login";

        PageRequest pageable = new PageRequest(page, size);
        List<UserInfo> userInfos = powerService.getUserInfo();
        //根据key筛选
        if (!StringUtils.isEmpty(key)) {
            userInfos = userInfos.stream().filter(obj -> obj.getUserName().contains(key) || obj.getNickName().contains(key) || obj.getDepartment().contains(key)).collect(Collectors.toList());
        }
        int totalCount = userInfos.size();
        int startCount = (page - 1) * size;
//        if(startCount>=totalCount)
//        {
//            model.addAttribute("hasOrder", false);
//            model.addAttribute("erromsg","超出查询数量！");
//            return "power/userinfotemp";
//        }
        int endCount = totalCount >= page * size ? page * size : totalCount;
        userInfos = userInfos.subList(startCount, endCount);
        if (userInfos.size() == 0 && pageable.getPageNumber() == 0) {
            model.addAttribute("hasOrder", false);
        } else {
            model.addAttribute("hasOrder", true);
            common.returnPageAttrSql(model, pageable, userInfos, totalCount); //分页的页数从0开始
        }

        return "power/userinfotemp";
    }


    @RequestMapping(value = "/getRoleInfo", method = RequestMethod.GET)
    public String getRoleInfo(Model model, HttpServletRequest request, int page, int size, String key) {
        if (request.getSession().getAttribute("USER_INFO") == null)
            return "redirect:login";

        PageRequest pageable = new PageRequest(page, size);
        List<RoleInfo> roleInfos = powerService.getAllRoleInfo().getData();
        //根据key筛选
        if (!StringUtils.isEmpty(key)) {
            roleInfos = roleInfos.stream().filter(obj -> obj.getRoleName().contains(key)).collect(Collectors.toList());
        }
        ComparatorRoleInfo comparatorRoleInfo=new ComparatorRoleInfo();
        Collections.sort(roleInfos,comparatorRoleInfo);

        int totalCount = roleInfos.size();
        int startCount = (page - 1) * size;
//        if(startCount>=totalCount)
//        {
//            model.addAttribute("hasOrder", false);
//            model.addAttribute("erromsg","超出查询数量！");
//            return "power/roleinfotemp";
//        }
        int endCount = totalCount >= page * size ? page * size : totalCount;
        roleInfos = roleInfos.subList(startCount, endCount);
        if (roleInfos.size() == 0 && pageable.getPageNumber() == 0) {
            model.addAttribute("hasOrder", false);
        } else {
            model.addAttribute("hasOrder", true);
            common.returnPageAttrSql(model, pageable, roleInfos, totalCount); //分页的页数从0开始
        }

        return "power/roleinfotemp";
    }





    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody<UserInfo> addUser(@RequestBody UserInfo userInfo) {
        ApiReturnBody<UserInfo> result = new ApiReturnBody<>();
        if (userInfo == null) {
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        result = powerService.addUser(userInfo);
        return result;
    }

    @RequestMapping(value="/updateuser",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody updateUser(@RequestBody UserInfo userInfo){
        ApiReturnBody result=new ApiReturnBody();
        if (userInfo == null) {
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }
        result=powerService.updateUser(userInfo);

        return result;
    }


    /**
     * 获取指定用户的角色
     * @param userId 用户编号
     * @return
     */
    @RequestMapping(value="/getRole",method=RequestMethod.GET)
    @ResponseBody
    public ApiReturnBody<List<RoleInfo>> getRole(Long userId){
        ApiReturnBody<List<RoleInfo>> result=new ApiReturnBody<>();
        if(userId==null||userId<=0){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }
        result=powerService.getRoleInfoByUserId(userId);

        return result;
    }

    @RequestMapping(value="/getMenu",method=RequestMethod.GET)
    @ResponseBody
    public ApiReturnBody<List<MenuInfo>> getMenu(Long roleId){
        ApiReturnBody<List<MenuInfo>> result=new ApiReturnBody<>();
        if(roleId==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }
        result=powerService.getMenu(roleId);

        return result;
    }


    /**
     * 获取所有角色
     * @return
     */
    @RequestMapping(value="/getAllRole",method=RequestMethod.GET)
    @ResponseBody
    public  ApiReturnBody<List<RoleInfo>> getAllRole(){
        ApiReturnBody<List<RoleInfo>> result=new ApiReturnBody<>();
        result=powerService.getAllRoleInfo();
        return result;
    }


    /**
     * 更新用户角色
     * @param userId 用户编号
     * @param roleList 角色列表
     * @return
     */
    @RequestMapping(value="/updateUserRole",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody updateUserRole(Long userId,@RequestParam(value = "roleList[]") Long[] roleList){
        ApiReturnBody result=new ApiReturnBody();
        List<Long> l=Arrays.asList(roleList);
        if(userId==null|| CollectionUtils.isEmpty(l)){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }
        result=powerService.updateUserRole(userId,l);
        return result;
    }


    @RequestMapping(value="/updateRoleMenu",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody updateRoleMenu(Long roleId,@RequestParam(value="menuList[]")Integer[] menuList){
        ApiReturnBody result=new ApiReturnBody();
        List<Integer> l=Arrays.asList(menuList);
        if(roleId==null||CollectionUtils.isEmpty(l)){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);

            result.setSuccess(false);
            return result;
        }
        result=powerService.updateRoleMenu(roleId,l);

        return result;
    }

    /**
     * 初始化密码
     * @param userId 用户编号
     */
    @RequestMapping(value="/initPassword",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody initPassword(Long userId){
        ApiReturnBody result=new ApiReturnBody();
        if(userId==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }
        result=powerService.initPassword(userId);

        return result;
    }



    @RequestMapping(value="/addRole",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody<RoleInfo> addRole(@RequestBody RoleInfo roleInfo){
        ApiReturnBody<RoleInfo> result=new ApiReturnBody<>();
        if(roleInfo==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return result;
        }

        result=powerService.addRole(roleInfo);
        return result;
    }


    @RequestMapping(value="/updateRole",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody updateRole(@RequestBody RoleInfo roleInfo){
        ApiReturnBody result=new ApiReturnBody();
        if(roleInfo==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return  result;
        }

        result=powerService.updateRole(roleInfo);

        return result;

    }


    @RequestMapping(value="/getMenuTree",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody<List<MenuInfo>> getMenuTree(Integer  rootParentId){
        ApiReturnBody<List<MenuInfo>> result=new ApiReturnBody<>();
        if(rootParentId==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return  result;

        }
        result=powerService.getMenuTree(rootParentId);

        return result;
    }


    /**
     * 新增菜单
     * @param menuInfo 菜单信息
     * @return
     */
    @RequestMapping(value="/addMenu",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody addMenu(@RequestBody  MenuInfo menuInfo){
        ApiReturnBody result=new ApiReturnBody();
        if(menuInfo==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return  result;
        }
        result=powerService.addMenu(menuInfo);

        return result;
    }


    /**
     * 根据菜单Id获取该菜单和子菜单详情
     * @param menuId
     * @return
     */
    @RequestMapping(value="/getPaAndChilMenu",method=RequestMethod.GET)
    @ResponseBody
    public ApiReturnBody<MenuInfo> getPaAndChilMenu(Integer menuId){
        ApiReturnBody<MenuInfo> result=new ApiReturnBody<>();
        if(menuId==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return  result;
        }
        result=powerService.getPaAndChilMenu(menuId);
        return result;
    }

    /**
     * 更新菜单
     * @param menuInfo
     * @return
     */
    @RequestMapping(value="/updateMenu",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody updateMenu(@RequestBody MenuInfo menuInfo){
        ApiReturnBody result=new ApiReturnBody();
        if(menuInfo==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return  result;
        }
        result=powerService.updateMenu(menuInfo);

        return result;
    }


    /**
     * 删除菜单
     * @param menuId 菜单编号
     * @return
     */
    @RequestMapping(value="/deleteMenu",method=RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody deleteMenu(Integer menuId){
        ApiReturnBody result=new ApiReturnBody();
        if(menuId==null){
            result.setMsg("请求参数为空！");
            result.setCode(NormalConstant.FAILE_CODE);
            result.setSuccess(false);
            return  result;
        }
        result=powerService.deleteMenu(menuId);

        return result;
    }

}
