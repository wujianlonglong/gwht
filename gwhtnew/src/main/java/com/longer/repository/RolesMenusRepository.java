package com.longer.repository;

import com.longer.domain.RolesMenus;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wujianlong on 2017/5/5.
 */
public interface RolesMenusRepository extends JpaRepository<RolesMenus,String> {

    List<RolesMenus> findByStatus(Integer status);

    int deleteByRoleId(Long roleId);


    @Transactional
    int deleteByMenuId(Integer menuId);

}
