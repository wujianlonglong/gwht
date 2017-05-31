package com.longer.controller;

import com.longer.common.Common;
import com.longer.domain.Memorabilia;
import com.longer.domain.NewsInfoTest;
import com.longer.domain.RoleInfo;
import com.longer.model.ApiReturnBody;
import com.longer.model.ComparatorRoleInfo;
import com.longer.service.NewsTestService;
import com.longer.utils.JsonUtil;
import com.longer.utils.property.GwhtFileUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by wujianlong on 2017/5/22.
 */
@Controller
//@RequestMapping("/news")
public class NewsTestController {

    @Autowired
    Common common;

    @Autowired
    NewsTestService newsTestService;

    @Autowired
    GwhtFileUrl gwhtFileUrl;

    private static final Logger logger = LoggerFactory.getLogger(NewsTestController.class);

    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");


    @RequestMapping(value = "/getNewsList", method = RequestMethod.GET)
    public String getNewsList(Model model, HttpServletRequest request, int page, int size, String key,String type) {
        if (request.getSession().getAttribute("USER_INFO") == null)
            return "redirect:login";

        PageRequest pageable = new PageRequest(page, size);
        List<NewsInfoTest> newsInfoTests = newsTestService.getAllNewsInfoTest().getData();
        //根据key筛选
        if (!StringUtils.isEmpty(key)) {
            newsInfoTests = newsInfoTests.stream().filter(obj -> obj.getNewsTitle().contains(key)).collect(Collectors.toList());
        }
        if(!type.equals("-1")){
            newsInfoTests=newsInfoTests.stream().filter(obj->obj.getNewType().equals(type)).collect(Collectors.toList());
        }

        newsInfoTests.sort(Comparator.comparing(NewsInfoTest::getCreateTime).reversed());

        int totalCount = newsInfoTests.size();
        int startCount = (page - 1) * size;
//        if(startCount>=totalCount)
//        {
//            model.addAttribute("hasOrder", false);
//            model.addAttribute("erromsg","超出查询数量！");
//            return "power/roleinfotemp";
//        }
        int endCount = totalCount >= page * size ? page * size : totalCount;
        newsInfoTests = newsInfoTests.subList(startCount, endCount);
        if (newsInfoTests.size() == 0 && pageable.getPageNumber() == 0) {
            model.addAttribute("hasOrder", false);
        } else {
            model.addAttribute("hasOrder", true);
            common.returnPageAttrSql(model, pageable, newsInfoTests, totalCount); //分页的页数从0开始
        }

        return "news/newslisttemp";
    }



    @RequestMapping(value = "/getMemorabiliaList", method = RequestMethod.GET)
    public String getMemorabiliaList(Model model, HttpServletRequest request, int page, int size, String key) {
        if (request.getSession().getAttribute("USER_INFO") == null)
            return "redirect:login";

        PageRequest pageable = new PageRequest(page, size);
        List<Memorabilia> memorabilias = newsTestService.getAllMemorabilia().getData();
        //根据key筛选
        if (!StringUtils.isEmpty(key)) {
            memorabilias = memorabilias.stream().filter(obj -> obj.getContent().contains(key)).collect(Collectors.toList());
        }
        memorabilias.sort(Comparator.comparing(Memorabilia::getHappenDate).reversed());

        int totalCount = memorabilias.size();
        int startCount = (page - 1) * size;
//        if(startCount>=totalCount)
//        {
//            model.addAttribute("hasOrder", false);
//            model.addAttribute("erromsg","超出查询数量！");
//            return "power/roleinfotemp";
//        }
        int endCount = totalCount >= page * size ? page * size : totalCount;
        memorabilias = memorabilias.subList(startCount, endCount);
        if (memorabilias.size() == 0 && pageable.getPageNumber() == 0) {
            model.addAttribute("hasOrder", false);
        } else {
            model.addAttribute("hasOrder", true);
            common.returnPageAttrSql(model, pageable, memorabilias, totalCount); //分页的页数从0开始
        }

        return "news/memorabilialisttemp";
    }




    @RequestMapping(value = "/newsedit", method = RequestMethod.GET)
    public String newsedit(Model model, Integer newsId) {
        if (newsId != null && newsId > 0) {
            NewsInfoTest newsInfoTest = newsTestService.getNewsInfoTest(newsId);
            if (newsInfoTest == null) {
                model.addAttribute("istrue", false);
            } else {
                model.addAttribute("istrue", true);
                model.addAttribute("newsTitle", newsInfoTest.getNewsTitle());
                model.addAttribute("newType", newsInfoTest.getNewType());
                model.addAttribute("isTop", newsInfoTest.getIsTop());
                model.addAttribute("showTime", format.format(newsInfoTest.getShowTime()));
                model.addAttribute("showSource", newsInfoTest.getShowSource());
                model.addAttribute("newsContent", newsInfoTest.getNewsContent().replace("src=\"/", "src=\""+gwhtFileUrl.getGwurl()).replace("value=\"/", "value=\""+gwhtFileUrl.getGwurl()));

            }

        }
        model.addAttribute("newsId", newsId);

        return "news/newsedit";
    }



    @RequestMapping(value = "/memorabiliaedit", method = RequestMethod.GET)
    public String memorabiliaedit(Model model, Integer id) {
        if (id != null && id > 0) {
            Memorabilia memorabilia = newsTestService.getmemorabilia(id);
            if (memorabilia == null) {
                model.addAttribute("istrue", false);
            } else {
                model.addAttribute("istrue", true);
                model.addAttribute("happenDate", format.format(memorabilia.getHappenDate()));
                model.addAttribute("content", memorabilia.getContent().replace("src=\"/", "src=\""+gwhtFileUrl.getGwurl()).replace("value=\"/", "value=\""+gwhtFileUrl.getGwurl()));
            }

        }
        model.addAttribute("id", id);

        return "news/memorabiliaedit";
    }



    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody updateStatus(Integer newsId, String status) {
        ApiReturnBody result = new ApiReturnBody();
        if (newsId == null || status == null) {
            result.setMsg("参数不能为空！");
            return result;
        }
        result = newsTestService.updateStatus(newsId, status);
        return result;
    }

    @RequestMapping(value = "/updateMemorStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody updateMemorStatus(Integer id, Integer  status) {
        ApiReturnBody result = new ApiReturnBody();
        if (id == null || status == null) {
            result.setMsg("参数不能为空！");
            return result;
        }
        result = newsTestService.updateMemorStatus(id, status);
        return result;
    }


    /**
     * 上传新闻图片至服务器
     *
     * @param file    被上传的文件
     * @param funcNum
     * @return
     */
    @RequestMapping(value = "imgUpload", method = RequestMethod.POST)
    @ResponseBody
    public String imgUpload(@RequestParam("upload") MultipartFile file, @RequestParam("CKEditorFuncNum") String funcNum) {
        String fileURL = "";
        String errorMsg = "";
        if (!file.isEmpty()) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            logger.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            logger.info("上传的后缀名为：" + suffixName);
            // 文件上传路径
            String filePath = gwhtFileUrl.getGwhtimgurl();
            //String filePath =this.getClass().getClassLoader().getResource("/").getPath()+"static/loadimg/";
            // 解决中文问题，liunx下中文路径，图片显示问题
            fileName = UUID.randomUUID() + suffixName;
            File dest = new File(filePath + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                fileURL = gwhtFileUrl.getGwimgurl() + fileName;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                errorMsg = e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                errorMsg = e.toString();
            }
        }

        String script = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + funcNum + ", '" + fileURL + "', '" + errorMsg + "');</script>";
        return script;
    }

    @RequestMapping(value = "/subNews", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody subNews(@RequestBody NewsInfoTest newsInfoTest) {
        ApiReturnBody result = new ApiReturnBody();
        if (newsInfoTest == null) {
            result.setMsg("请求参数为空！");
            return result;
        }
        if(StringUtils.isEmpty(newsInfoTest.getNewsTitle().trim())){
            result.setMsg("请求参数新闻标题为空！");
            return result;
        }
        if(StringUtils.isEmpty(newsInfoTest.getNewsContent().trim())){
            result.setMsg("请求参数新闻内容为空！");
            return result;
        }
        if(newsInfoTest.getShowTime()==null){
            result.setMsg("请求参数发布日期为空！");
            return result;
        }

        result = newsTestService.subNews(newsInfoTest);
        return result;
    }

    @RequestMapping(value = "/subMemor", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody subMemor(@RequestBody Memorabilia memorabilia) {
        ApiReturnBody result = new ApiReturnBody();
        if (memorabilia == null) {
            result.setMsg("请求参数为空！");
            return result;
        }
        if(StringUtils.isEmpty(memorabilia.getContent())){
            result.setMsg("请求参数大事记内容为空!");
            return result;
        }
        if(memorabilia.getHappenDate()==null){
            result.setMsg("请求参数大事记发生日期为空！");
            return result;
        }

        result = newsTestService.subMemor(memorabilia);
        return result;
    }




    @RequestMapping(value = "/refreshNewList", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody refreshNewList() {
        ApiReturnBody result = new ApiReturnBody();
        result = newsTestService.refreshNewList();
        return result;
    }


    @RequestMapping(value = "/refreshMemor", method = RequestMethod.POST)
    @ResponseBody
    public ApiReturnBody refreshMemor() {
        ApiReturnBody result = new ApiReturnBody();
        result = newsTestService.refreshMemor();
        return result;
    }


}
