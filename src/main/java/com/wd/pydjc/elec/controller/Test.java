package com.wd.pydjc.elec.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.alibaba.druid.sql.ast.statement.SQLCreateSequenceStatement;
import com.wd.pydjc.common.utils.DateUtil;

import net.bytebuddy.asm.Advice.This;

public class Test {
	private static DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat formatTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private static DateFormat formatU = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 
	private static DateFormat formatU = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); 
	
	public static void main(String[] args) throws ParseException {
		 Calendar c = Calendar.getInstance();
		
		 
		 for (int i = 1; i <= 19 ; i++) {
			 c.set(Calendar.DAY_OF_MONTH, i);  
			 System.out.println(formatDay.format(c.getTime()));
		 }
		 
	}

	private static String getStartTime(String date) {  
		DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
        Calendar dayStart = Calendar.getInstance();  
        if(date ==null || "".equals(date)){
        	dayStart.setTime(new Date());
        }else{
        	try {
        		dayStart.setTime(formatDay.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			} 
        }
        dayStart.set(Calendar.HOUR, 0);  
        dayStart.set(Calendar.MINUTE, 0);  
        dayStart.set(Calendar.SECOND, 0);  
        dayStart.set(Calendar.MILLISECOND, 0);  
        return formatTime.format(dayStart.getTime());  
    }  
	
	private static String getEndTime(String date) {  
		DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
        Calendar dayEnd = Calendar.getInstance();  
        if(date ==null || "".equals(date)){
        	dayEnd.setTime(new Date());
        }else{
        	try {
        		dayEnd.setTime(formatDay.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			} 
        }
        dayEnd.set(Calendar.HOUR, 23);  
        dayEnd.set(Calendar.MINUTE, 59);  
        dayEnd.set(Calendar.SECOND, 59);  
        dayEnd.set(Calendar.MILLISECOND, 999);  
        return formatTime.format(dayEnd.getTime());   
	}  
   
}
