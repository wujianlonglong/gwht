package com.longer.repository;

import com.longer.domain.RolesUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wujianlong on 2017/5/5.
 */
public interface RolesUsersRepository extends JpaRepository<RolesUsers, String> {

    List<RolesUsers> findByUserIdAndStatus(Long userId, Integer status);

    List<RolesUsers> findByStatus(Integer status);

    int deleteByUserId(Long userId);

}
