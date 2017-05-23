package com.longer.repository;

import com.longer.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wujianlong on 2017/5/4.
 */
public interface UserInfoRepository extends JpaRepository<UserInfo,String> {
    UserInfo findByUserNameAndStatus(String username,Integer status );

    List<UserInfo> findByStatus(Integer status);

    UserInfo findByIdAndStatus(Long userId,Integer status);

    @Transactional
    @Modifying
    @Query(value="update USER_INFO set department=?1,nick_name=?2  where id=?3",nativeQuery = true)
    int updateUserById(String department,String nickName,Long id);

    @Transactional
    @Modifying
    @Query(value="update USER_INFO set PASS_WORD=?1 where id=?2",nativeQuery = true)
    int updatePassword(String password,Long id);

}
