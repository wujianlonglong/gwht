package com.longer.service;

import com.longer.business.NewsTestBussiness;
import com.longer.domain.NewsInfoTest;
import com.longer.model.ApiReturnBody;
import com.longer.utils.constant.NormalConstant;
import com.longer.utils.property.GwhtFileUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by wujianlong on 2017/5/22.
 */
@Service
public class NewsTestService {

    @Autowired
    NewsTestBussiness newsTestBussiness;

    @Autowired
    RedisService redisService;

    @Autowired
    GwhtFileUrl gwhtFileUrl;

    public ApiReturnBody<List<NewsInfoTest>> getAllNewsInfoTest() {
        ApiReturnBody<List<NewsInfoTest>> result = new ApiReturnBody<>();
        try {
            List<NewsInfoTest> newsInfoTests = newsTestBussiness.getAllNewsInfoTest();
            result.setMsg("获取新闻列表成功！");
            result.setSuccess(true);
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setData(newsInfoTests);
        } catch (Exception ex) {
            result.setMsg("获取新闻列表失败：" + ex.toString());
            return result;
        }

        return result;
    }

    public NewsInfoTest getNewsInfoTest(Integer newsId) {
        NewsInfoTest result = new NewsInfoTest();
        try {
            List<NewsInfoTest> newsInfoTests = newsTestBussiness.getAllNewsInfoTest();
            result = newsInfoTests.stream().filter(obj -> obj.getNewsId().equals(newsId)).findFirst().orElse(null);
        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    public ApiReturnBody updateStatus(Integer newsId, String status) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            newsTestBussiness.updateStatus(newsId, status);
            redisService.clearNewInfoTest();//清除新闻redis缓存
            refreshGwNews();//清除官网前端新闻缓存
            result.setMsg("更新新闻有效性成功！");
            result.setSuccess(true);
            result.setCode(NormalConstant.SUCCESS_CODE);
        } catch (Exception ex) {
            result.setMsg("更新新闻有效性失败：" + ex.toString());
            return result;
        }

        return result;
    }


    public void refreshGwNews(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        restTemplate.postForObject(gwhtFileUrl.getGwnewsrefreshurl()+"RefreshHrNewsDict",
                entity, Object.class);

        restTemplate.postForObject(gwhtFileUrl.getGwnewsrefreshurl()+"RefreshStoreNewsDict",
                entity, Object.class);
        restTemplate.postForObject(gwhtFileUrl.getGwnewsrefreshurl()+"RefreshCompanyNewsDict",
                entity, Object.class);
    }

    public ApiReturnBody subNews(NewsInfoTest newsInfoTest) {
        ApiReturnBody result = new ApiReturnBody();
        try {
            if (newsInfoTest.getNewsId().equals(0)) {
                newsInfoTest.setNewsId(null);
                NewsInfoTest newsInfoTest1 = newsTestBussiness.saveNews(newsInfoTest);
            } else {
                newsTestBussiness.updateNews(newsInfoTest);
            }
            redisService.clearNewInfoTest();
            refreshGwNews();
            result.setCode(NormalConstant.SUCCESS_CODE);
            result.setSuccess(true);
            result.setMsg("提交新闻成功！");
        } catch (Exception ex) {
            result.setMsg("提交新闻失败：" + ex.toString());
            return result;
        }
        return result;
    }
}
