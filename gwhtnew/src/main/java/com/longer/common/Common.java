package com.longer.common;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wujianlong on 2017/5/5.
 */
@Service
public class Common {



    public static byte[] EncryptionStrBytes(String str, String algorithm) {
        // 加密之后所得字节数组
        byte[] bytes = null;
        try {
            // 获取MD5算法实例 得到一个md5的消息摘要
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //添加要进行计算摘要的信息
            md.update(str.getBytes());
            //得到该摘要
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("加密算法: " + algorithm + " 不存在: ");
        }
        return null == bytes ? null : bytes;
    }

    /**
     * 把字节数组转化成字符串返回
     *
     * @param bytes
     * @return
     */
    public static String BytesConvertToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            String s = Integer.toHexString(0xff & aByte);
            if (s.length() == 1) {
                sb.append("0" + s);
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }



    /**
     * 添加分页元素属性（数据由sql分页）
     *
     * @param model model
     * @param page  分页参数
     * @param datalist  数据列表
     */
    public void returnPageAttrSql(Model model, Pageable page, Object datalist, Integer totalCount) {
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        model.addAttribute("pageNum", pageNumber);
        model.addAttribute("isFirstPage", pageNumber == 0);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPage", (totalCount + pageSize - 1) / pageSize);
        model.addAttribute("isLastPage", (totalCount + pageSize - 1) / pageSize == pageNumber);
        model.addAttribute("datalist", datalist);
    }

}
