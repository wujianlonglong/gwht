package com.longer.business;

import com.longer.domain.NewsInfoTest;
import com.longer.repository.NewsInfoTestRepository;
import com.longer.utils.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wujianlong on 2017/5/22.
 */
@Component
public class NewsTestBussiness {

    private static Object lockOne = new Object();

    @Autowired
    NewsInfoTestRepository newsInfoTestRepository;

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, List<NewsInfoTest>> newsTestValueOperation;


    public List<NewsInfoTest> getAllNewsInfoTest() {
        String redisKey = RedisConstant.ALL_NEWS_INFO_TEST;
        List<NewsInfoTest> newsInfoTests = newsTestValueOperation.get(redisKey);
        if (CollectionUtils.isEmpty(newsInfoTests)) {
            synchronized (lockOne) {
                newsInfoTests = newsTestValueOperation.get(redisKey);
                if (CollectionUtils.isEmpty(newsInfoTests)) {
                    newsInfoTests = newsInfoTestRepository.findAll();
                    newsTestValueOperation.set(redisKey, newsInfoTests);
                }
            }
        }
        return newsInfoTests;
    }


    public int  updateStatus(Integer newsId,String status){
        return newsInfoTestRepository.updateStatus(newsId,status);
    }


    public NewsInfoTest saveNews( NewsInfoTest newsInfoTest){
        return newsInfoTestRepository.save(newsInfoTest);
    }

    public int updateNews(NewsInfoTest newsInfoTest){
        return newsInfoTestRepository.updateNews(newsInfoTest.getNewsId(),newsInfoTest.getNewsTitle(),newsInfoTest.getIsTop(),newsInfoTest.getNewsContent(),newsInfoTest.getShowSource(),newsInfoTest.getShowTime(),newsInfoTest.getNewType());
    }
}
