package com.wd.pydjc.pred.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelImport {
    public static void getDataFromExcel(InputStream  InputStream )
    {

        Workbook wookbook = null;
        try
        {
            //2003版本的excel，用.xls结尾
            wookbook = new HSSFWorkbook(InputStream);//得到工作簿

        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
            try
            {
                //2007版本的excel，用.xlsx结尾

                wookbook = new XSSFWorkbook(InputStream);//得到工作簿
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //得到一个工作表
        Sheet sheet = wookbook.getSheetAt(0);

        //获得表头
        Row rowHead = sheet.getRow(0);

        //判断表头是否正确
        if(rowHead.getPhysicalNumberOfCells()<1 )
        {
            System.out.println("表头的数量不对!");
        }

        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();
        //要获得属性
        Long phoneNumber=null ;
        String deptName=null;
        String userName = "";
        //获得所有数据
        for(int i = 1 ; i <= totalRowNum ; i++)
        {
            //获得第i行对象
            Row row = sheet.getRow(i);

            //获得获得第i行第0列的 String类型对象
            Cell cell = row.getCell((short)0);
            phoneNumber =(long) cell.getNumericCellValue();

            //获得一个字符串类型的数据
            cell = row.getCell((short)1);
            deptName =   cell.getStringCellValue().toString();

            cell = row.getCell((short)2);
            userName =  cell.getStringCellValue().toString();
	          /*
	            cell = row.getCell((short)3);
	            email =  cell.getStringCellValue().toString();

	            cell = row.getCell((short)4);
	            DeptName =  cell.getStringCellValue().toString();

	            cell = row.getCell((short)5);
	            deptId=(int) cell.getNumericCellValue();*/
            System.out.println("name="+userName+",phone="+phoneNumber+",deptName="+deptName);

        }
    }
}


