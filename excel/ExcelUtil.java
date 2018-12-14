package excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import java.util.List;

public class ExcelUtil {

    public static void exportUserExcel(List<User> userList, ServletOutputStream out) {
        try {
            //1.创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            //1.1/创建合并单元格对象
            CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, 4);
            //1.2、头标题样式
            HSSFCellStyle headStyle = createCellStyle(workbook, (short) 16);
            //1.3.列标题样式
            HSSFCellStyle colStyle = createCellStyle(workbook, (short) 13);
            //2.创建工作表
            HSSFSheet sheet = workbook.createSheet("用户列表");
            //2.1、加载合并单元格对象
            sheet.addMergedRegion(callRangeAddress);
            //设置默认列宽
            sheet.setDefaultColumnWidth(25);
            //3.创建行
            //3.1、创建头标题行； 并且设置头标题
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            //加载单元格样式
            cell.setCellStyle(headStyle);
            cell.setCellValue("用户列表");

            //3.2创建列标题； 并且设置列标题
            HSSFRow row2 = sheet.createRow(1);
            String[] titles = {"用户名", "账号", "所属部门", "性别", "电子邮箱"};
            for (int i = 0; i < titles.length; i++) {
                HSSFCell cell2 = row2.createCell(i);
                //加载单元格样式
                cell2.setCellStyle(colStyle);
                cell2.setCellValue(titles[i]);
            }

            //4、操作单元格；将用户列表写入excel
            if (userList != null) {
                for (int j = 0; j < userList.size(); j++) {
                    //创建数据行，前面有两行，标题行和列标题行
                    HSSFRow row3 = sheet.createRow(j + 2);
                    HSSFCell nameCell = row3.createCell(0);
                    nameCell.setCellValue(userList.get(j).getName());
                    HSSFCell accountCell = row3.createCell(1);
                    accountCell.setCellValue(userList.get(j).getAccount());
                    HSSFCell deptCell = row3.createCell(2);
                    deptCell.setCellValue(userList.get(j).getDept());
                    HSSFCell genderCell = row3.createCell(3);
                    genderCell.setCellValue(userList.get(j).isGender() ? "男" : "女");
                    HSSFCell emailCell = row3.createCell(4);
                    emailCell.setCellValue(userList.get(j).getEmail());
                }
            }
            //5、输出
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //创建字体
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(fontsize);
        //加载字体
        style.setFont(font);
        return style;
    }
}
