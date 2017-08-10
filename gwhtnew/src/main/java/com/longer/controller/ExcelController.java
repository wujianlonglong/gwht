package com.longer.controller;

import com.longer.domain.StoreInfos;
import com.longer.repository.StoreInfosRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujianlong on 2017/7/13.
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {

    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    StoreInfosRepository storeInfosRepository;

    @RequestMapping(value = "/importstore", method = RequestMethod.POST)
    public String importStore(Model model, @RequestParam("storefile") MultipartFile file) {

        if (file.isEmpty()) {
            model.addAttribute("importMessage", "未查找到导入的文件，请重新选择文件导入！");
            return "store/importstore";
        }
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            model.addAttribute("importMessage", "导入的文件不是excel格式的文件，请选择excel文件导入！");
            return "store/importstore";
        }
        try {
            InputStream inputStream = file.getInputStream();
            Workbook wb = null;
            Sheet sheetMain;

            if (fileName.endsWith(".xls")) {
                wb = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                wb = new XSSFWorkbook(inputStream);
            }
            // 读取第一个Sheet
            sheetMain = wb.getSheetAt(0);
            inputStream.close();
            // 总共的行数
            int rowLens = sheetMain.getLastRowNum();
            int celLens = sheetMain.getRow(0).getLastCellNum();
            // int colLens = 6;
            int sucCnt = 0;
            int errCnt = 0;
            Row row = null;
            Cell cell = null;
            StringBuilder errMsg = new StringBuilder();
            List<StoreInfos> storeInfosList = new ArrayList<>();
            for (int rowCount = 1; rowCount <= rowLens; rowCount++) {
                row = sheetMain.getRow(rowCount);
                if (row != null) {
                    StoreInfos storeInfos = new StoreInfos();
                    boolean istrue = true;
                    for (int colCount = 0; colCount < celLens; colCount++) {
                        cell = row.getCell(colCount);
                        String shopStr = subZeroAndDot(cell.toString());
                        if (StringUtils.isEmpty(shopStr)) {
                            errMsg.append("第" + (rowCount + 1) + "行，第" + (colCount + 1) + "列的数据为空！\r\n");
                            errCnt++;
                            istrue = false;
                            break;
                        } else {
                            switch (colCount) {
                                case 0:
                                    storeInfos.setShopCode(shopStr);
                                    break;
                                case 1:
                                    storeInfos.setShopName(shopStr);
                                    break;
                                case 2:
                                    storeInfos.setShopArea(shopStr);
                                    break;
                                case 3:
                                    storeInfos.setShopPhone(shopStr);
                                    break;
                                case 4:
                                    storeInfos.setShopAdminArea(shopStr);
                                    break;
                                case 5:
                                    storeInfos.setShopAddress(shopStr);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (istrue == true) {
                        storeInfosList.add(storeInfos);
                        sucCnt++;
                    }
                }
            }
            //清除老的数据然后再新增excel的数据
                    storeInfosRepository.deleteAll();
                    storeInfosRepository.save(storeInfosList);
            String resultStr = "成功导入" + sucCnt + "条数据\r\n" + "导入失败" + errCnt + "条数据:\r\n" + errMsg;
            model.addAttribute("importMessage", resultStr);
        } catch (Exception ex) {
            log.error("导入失败：" + ex.toString());
            model.addAttribute("importMessage", "导入失败：" + ex.toString());
            return "store/importstore";
        }
       return "store/importstore";
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
