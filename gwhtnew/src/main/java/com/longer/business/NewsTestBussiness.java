package com.longer.business;

import com.longer.domain.Memorabilia;
import com.longer.domain.NewsInfoTest;
import com.longer.repository.MemorabiliaRepository;
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

    private static Object lockTwo=new Object();

    @Autowired
    NewsInfoTestRepository newsInfoTestRepository;

    @Autowired
    MemorabiliaRepository memorabiliaRepository;

    @Resource(name = "redisTemplate")
    protected ValueOperations<String, List<NewsInfoTest>> newsTestValueOperation;

    @Resource(name="redisTemplate")
    protected ValueOperations<String,List<Memorabilia>> memorabiliaValueOperation;


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

    public List<Memorabilia> getAllMemorabilia() {
        String redisKey = RedisConstant.All_MEMORABILIA;
        List<Memorabilia> memorabilias = memorabiliaValueOperation.get(redisKey);
        if (CollectionUtils.isEmpty(memorabilias)) {
            synchronized (lockTwo) {
                memorabilias = memorabiliaValueOperation.get(redisKey);
                if (CollectionUtils.isEmpty(memorabilias)) {
                    memorabilias = memorabiliaRepository.findAll();
                    memorabiliaValueOperation.set(redisKey, memorabilias);
                }
            }
        }
        return memorabilias;
    }



    public int  updateStatus(Integer newsId,String status){
        return newsInfoTestRepository.updateStatus(newsId,status);
    }


    public int  updateMemorStatus(Integer id,Integer status){
        return memorabiliaRepository.updateMemorStatus(id,status);
    }



    public NewsInfoTest saveNews( NewsInfoTest newsInfoTest){
        return newsInfoTestRepository.save(newsInfoTest);
    }

    public Memorabilia saveMemorabilia(Memorabilia memorabilia){
        return memorabiliaRepository.save(memorabilia);
    }

    public int updateNews(NewsInfoTest newsInfoTest){
        return newsInfoTestRepository.updateNews(newsInfoTest.getNewsId(),newsInfoTest.getNewsTitle(),newsInfoTest.getIsTop(),newsInfoTest.getNewsContent(),newsInfoTest.getShowSource(),newsInfoTest.getShowTime(),newsInfoTest.getNewType());
    }

    public int updateMemorabilia(Memorabilia memorabilia){
        return memorabiliaRepository.updateMemorabilia(memorabilia.getId(),memorabilia.getContent(),memorabilia.getHappenDate());
    }

}
