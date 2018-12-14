package excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelTest {

    /**
     * 写入  .xls格式
     *
     * @throws Exception
     */
    @Test
    public void before2007Write() throws Exception {
        //创建新工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作表
        HSSFSheet sheet = workbook.createSheet("hello");
        //创建行，行号作为参数传递给createRow()方法，第一行从0开始计算
        HSSFRow row = sheet.createRow(0);
        //创建单元格，row已经确定行号，列号作为参数传递给createCell(), 第一列从0开始计算
        HSSFCell cell = row.createCell(2);
        //设置单元格的值，即C1的值（第一行，第三列）
        cell.setCellValue("hello sheet");
        //输出到磁盘中
        FileOutputStream fos = new FileOutputStream(new File("C:/Users/Administrator/Desktop/11.xls"));
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    /**
     * 读取  .xls格式
     *
     * @throws Exception
     */
    @Test
    public void before2007Read() throws Exception {

        //创建输入流
        FileInputStream fis = new FileInputStream(new File("C:/Users/Administrator/Desktop/11.xls"));
        //通过构造函数传参
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        //获取工作表
        HSSFSheet sheet = workbook.getSheetAt(0);
        //获取行，行号作为参数传递给GetRow方法，第一行从0开始计算
        HSSFRow row = sheet.getRow(0);
        //获取单元格，row已经确定了行号，列号作为参数传递给getCell,第一列从0开始计算
        HSSFCell cell = row.getCell(2);
        //设置单元格的值，即C1的值（第一行，第三列）
        String cellValue = cell.getStringCellValue();
        System.out.println("A3值为=" + cellValue);
        workbook.close();
        fis.close();
    }

    /**
     * 写入  .xlsx格式
     *
     * @throws Exception
     */

    @Test
    public void after2007Write() throws Exception {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //新建工作表
        XSSFSheet sheet = workbook.createSheet("hello");
        //创建行， 0表第一行
        XSSFRow row = sheet.createRow(0);
        //创建单元格行号由row确定，列号作为参数传递给createCell; 第一列从0开始计算
        XSSFCell cell = row.createCell(2);
        //给单元格赋值
        cell.setCellValue("Hello sheet");
        //创建输出流
        FileOutputStream fos = new FileOutputStream("C:/Users/Administrator/Desktop/11.xlsx");
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    /**
     * 读取  .xlsx格式
     *
     * @throws Exception
     */

    @Test
    public void after2007Read() throws Exception {
        //创建输入流
        FileInputStream fis = new FileInputStream(new File("C:/Users/Administrator/Desktop/11.xlsx"));
        //由输入流得到工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        //得到工作表
        XSSFSheet sheet = workbook.getSheet("hello");
        //得到行，0表示第一行
        XSSFRow row = sheet.getRow(0);
        //创建单元格行号由row确定，列号作为参数传递给createCell; 第一列从0开始计算
        XSSFCell cell = row.getCell(2);
        //获取单元格值
        String cellValue = cell.getStringCellValue();
        System.out.println("C1的值是 " + cellValue);
        workbook.close();
        fis.close();
    }

    /**
     * 读取 .xls 或 .xlsx
     */
    @Test
    public void read03And07() throws Exception {
        //读取03或07的版本
        String filePath = "C:/Users/Administrator/Desktop/11.xlsx";
        if (filePath.matches("^.+\\.(?i)((xls)|(xlsx))$")) { //(?i) 正则表达式修饰符 表示不区分大小写
            FileInputStream fis = new FileInputStream(filePath);
            boolean is03Excel = filePath.matches("^.+\\.(?i)(xls)$") ? true : false;
            Workbook workbook = is03Excel ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(2);
            System.out.println("第一行第三列的数据是： " + cell.getStringCellValue());
        }
    }

}
