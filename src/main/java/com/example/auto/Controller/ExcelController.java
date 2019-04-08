package com.example.auto.Controller;

import com.example.auto.bean.CommomFormBean;
import com.example.auto.bean.RestResult;
import com.example.auto.bean.RestResultGenerator;
import com.example.auto.util.FileUtils;
import com.example.auto.util.ListUtil;
import com.example.auto.util.Md5Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class ExcelController {
    private final static String EXCEL_DATA = "data";
    private final static String EXCEL_HEAD= "head";
    @Resource
    private RedisTemplate<Object,Object> redisTemplate;
    @RequestMapping("uploadExcel")
    public RestResult uploadExcel(MultipartFile file) {
        try {
            FileUtils.checkFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return RestResultGenerator.createFailResult(e.getMessage());

        }
        //获得工作薄对象
        Workbook workbook = FileUtils.getWorkBook(file);
        //每一行数据用数组封装，整体封装到List
        List<List<String>> list = new ArrayList<>();
        //封装第一行数据，类别
         List<String>typeList ;
        if (workbook != null) {
            //默认只支持一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return RestResultGenerator.createFailResult("表格数据为空！");
            }
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            typeList = FileUtils.getRowData(sheet,0);
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) { //为了过滤到第一行因为我的第一行是数据库的列
                //获得当前行
                list.add(FileUtils.getRowData(sheet,rowNum));

            }

        }else{
            return RestResultGenerator.createFailResult("上传表格失败！");
        }
        String fileId = UUID.randomUUID().toString().replaceAll("-","");
        //默认存30分钟
        redisTemplate.opsForValue().set(EXCEL_DATA+fileId,list,1800, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(EXCEL_HEAD+fileId,typeList,1800, TimeUnit.SECONDS);
        CommomFormBean com = ListUtil.getCommForm(1);
        com.getObjects()[0] = typeList;
        com.setId(fileId);
        return RestResultGenerator.createOkResult(com);
    }
    @RequestMapping("getExcelForm")
    public RestResult getExcelForm(String fileId,Integer index,Integer x,Integer y ,Integer valueType){
       List<List<String>> excelList = (List<List<String>>) redisTemplate.opsForValue().get(EXCEL_DATA+fileId);
       List<String> typeList =(List<String> )redisTemplate.opsForValue().get(EXCEL_HEAD+fileId);
       if(excelList ==null || typeList == null){
           return RestResultGenerator.createFailResult("文件已过期，请重新上传！");
       }
       String msg = FileUtils.checkXY(index,x,y);
       if(msg!=null){
           return RestResultGenerator.createFailResult(msg);
       }
        if(x!=null && x == -1){
            x = null;
        }
        if(y!=null && y == -1){
            y = null;
        }
     //  FileUtils.formatDataToNum(excelList,index);
        if(x != null && y != null ){
            try{
                Map<String,Double> map =   ListUtil.groupList(excelList,x,y,valueType);
                CommomFormBean commomFormBean = new CommomFormBean();
                commomFormBean.setObjects(new Object[1]);
                commomFormBean.getObjects()[0] = map;
                commomFormBean.setType("map");
                commomFormBean.setYName(typeList.get(y));
                commomFormBean.setXName(typeList.get(x));
                return RestResultGenerator.createOkResult(commomFormBean);
            }catch (Exception e){
                e.printStackTrace();
                return RestResultGenerator.createFailResult("分割线设置错误！");
            }

        }else {
            try{
                Double[] datalist = ListUtil.getSimpleNumList(excelList,index,valueType);
                List<String> headList = ListUtil.getCategery(typeList,index);
                CommomFormBean commomFormBean = new CommomFormBean();
                commomFormBean.setObjects(new Object[2]);
                commomFormBean.getObjects()[0] = headList;
                commomFormBean.getObjects()[1] = datalist;
                commomFormBean.setType("list");
                return RestResultGenerator.createOkResult(commomFormBean);
            }catch(Exception e){
                e.printStackTrace();
                return RestResultGenerator.createFailResult("分割线设置错误！");
            }

        }
    }
}
