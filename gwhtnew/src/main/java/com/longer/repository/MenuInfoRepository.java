package com.longer.repository;

import com.longer.domain.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wujianlong on 2017/5/4.
 */
public interface MenuInfoRepository extends JpaRepository<MenuInfo,String> {
    List<MenuInfo> findByStatus(Integer status);

    MenuInfo findByMenuId(Integer menuId);


    @Transactional
    int deleteByMenuId(Integer menuId);
}
