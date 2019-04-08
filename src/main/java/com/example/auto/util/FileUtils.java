package com.example.auto.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtils {
    private static final Log logger = LogFactory.getLog(FileUtils.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
            throw new IOException(fileName + "不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(xls)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;

    }

    /**
     * 获取文件名，去除后缀
     *
     * @param file
     * @return
     */
    public static String getFileName(MultipartFile file) {
        String name = file.getOriginalFilename();
        return name.substring(0, name.lastIndexOf("."));
    }

    /**
     * 获取每个单元个的数据，结果都为字符串
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //全部转为字符串
        cell.setCellType(CellType.STRING);
       //获取单元格数据
        cellValue = cell.getStringCellValue();
        //判断数据的类型
 /*       switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字,分为日期和字符串
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    // 验证short值
                    if (cell.getCellStyle().getDataFormat() == 14) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd");
                    } else if (cell.getCellStyle().getDataFormat() == 21) {
                        sdf = new SimpleDateFormat("HH:mm:ss");
                    } else if (cell.getCellStyle().getDataFormat() == 22) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    } else {
                        throw new RuntimeException("日期格式错误!!!");
                    }
                    Date date = cell.getDateCellValue();
                    cellValue = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 0) {//处理数值格式
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellValue = String.valueOf(cell.getRichStringCellValue().getString());
                }
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
//                cellValue = String.valueOf(cell.getCellFormula());
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }*/
        return cellValue;
    }

    /**
     * 获取每一行的数据
     *
     * @param sheet
     * @param rowNum
     * @return string[]
     */
    public static List<String> getRowData(Sheet sheet, int rowNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return null;
        }
        //获得当前行的开始列
        int firstCellNum = row.getFirstCellNum();
        //获得当前行的列数
        int lastCellNum = row.getLastCellNum();//为空列获取
        List<String> cells = new ArrayList<>(row.getLastCellNum());
        //循环当前行
        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
            Cell cell = row.getCell(cellNum);
            cells.add(FileUtils.getCellValue(cell));
        }
        return cells;
    }

    /**
     * 按照分割线，将string转为double，
     *
     * @param list
     * @param index
     */
    public static void formatDataToNum(List<Object[]> list, Integer index) {
        //每行数据是一个string数组，根据切割线转换为数字
        for (Object[] row : list) {
            for (int i = index; i < row.length; i++) {
                row[i] = (Double) row[i];
            }

        }

    }

    public static String checkXY(Integer index, Integer x, Integer y) {
        String msg = null;
        if (index == -1 || index == null) {
            msg = "请选择分割线！";
        }
        if (x != -1 && y == -1) {
            msg = "请选择y轴，且y轴不能为默认！";
        }
        if (y != -1 && x == -1) {
            msg = "请选择x轴，且x轴不能为默认！";
        }
        if (x != -1 && x >= index) {
            msg = "x轴数据必须处于分割线前";
        }
        if (y != -1 && y < index) {
            msg = "y轴数据必须处于分割线后";
        }
        return msg;
    }

}
