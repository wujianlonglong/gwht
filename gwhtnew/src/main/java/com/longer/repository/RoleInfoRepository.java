package com.longer.repository;

import com.longer.domain.RoleInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wujianlong on 2017/5/5.
 */
public interface RoleInfoRepository  extends JpaRepository<RoleInfo,String> {
    List<RoleInfo> findByStatus(Integer status);

    RoleInfo findByRoleNameAndRoleIdNot(String roleName,Long roleId);


    @Transactional
    @Modifying
    @Query(value="update ROLES_INFO set ROLE_NAME=?1,COMMENTS=?2,SHOW_ODER=?3 where ROLE_ID=?4",nativeQuery = true)
    int updateRole(String roleName,String comments,Integer showOder,Long roleId);
}
