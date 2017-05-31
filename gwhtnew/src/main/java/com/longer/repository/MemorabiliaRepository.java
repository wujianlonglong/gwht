package com.longer.repository;

import com.longer.domain.Memorabilia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by wujianlong on 2017/5/23.
 */
public interface MemorabiliaRepository  extends JpaRepository<Memorabilia,String> {


    @Transactional
    @Modifying
    @Query(value="update MEMORABILIA set CONTENT=?2,HAPPEN_DATE=?3 where id=?1 ",nativeQuery = true)
    int updateMemorabilia(Integer id, String content , Date happenDate);



    @Transactional
    @Modifying
    @Query(value="update MEMORABILIA set status=?2 where id=?1",nativeQuery=true)
    int updateMemorStatus(Integer id,Integer status);

}
