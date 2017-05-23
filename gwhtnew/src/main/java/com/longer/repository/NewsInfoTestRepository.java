package com.longer.repository;

import com.longer.domain.NewsInfoTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by wujianlong on 2017/5/19.
 */
public interface NewsInfoTestRepository extends JpaRepository<NewsInfoTest,String> {


    @Transactional
    @Modifying
    @Query(value = "update NEWS_INFO_TEST set IS_VALID=?2 where NEWS_ID=?1",nativeQuery = true)
    int  updateStatus(Integer newsId,String status);


    @Transactional
    @Modifying
    @Query(value="update NEWS_INFO_TEST set NEWS_TITLE=?2,IS_TOP=?3,NEWS_CONTENT=?4,SHOW_SOURCE=?5,SHOW_TIME=?6,NEW_TYPE=?7  where NEWS_ID=?1",nativeQuery = true)
    int updateNews(Integer newsId, String newsTitle, String isTop, String newsContent, String showSource, Date showTime, String newType);

}
