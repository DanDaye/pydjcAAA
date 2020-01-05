package com.wd.pydjc.common.utils;

import java.text.DateFormat;
import java.text.ParseException;   
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;   
import java.util.GregorianCalendar;   
import java.util.TimeZone;   
import java.util.Vector;

import org.springframework.util.StringUtils;   

/**  
 * 与日期、时间相关的一些常用工具方法.  
 * <p>  
 * 日期(时间)的常用格式(formater)主要有: <br>  
 * yyyy-MM-dd HH:mm:ss <br>  
 *   
 * @author 
 * @version 1.0.0  
 */  
public final class DateUtil {   
  
	private static DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat formatTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat formatU = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); 
	private static DateFormat formatUTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 
    /**  
     * 对日期(时间)中的日进行加减计算. <br>  
     * 例子: <br>  
     * 如果Date类型的d为 2005年8月20日,那么 <br>  
     * calculateByDate(d,-10)的值为2005年8月10日 <br>  
     * 而calculateByDate(d,+10)的值为2005年8月30日 <br>  
     *   
     * @param d  
     *            日期(时间).  
     * @param amount  
     *            加减计算的幅度.+n=加n天;-n=减n天.  
     * @return 计算后的日期(时间).  
     */  
    public static Date calculateByDate(Date d, int amount) {   
        return calculate(d, GregorianCalendar.DATE, amount);   
    }   
       
    public static Date calculateByMinute(Date d, int amount) {   
        return calculate(d, GregorianCalendar.MINUTE, amount);   
    }   
       
    public static Date calculateByYear(Date d, int amount) {   
        return calculate(d, GregorianCalendar.YEAR, amount);   
    }   
  
    /**  
     * 对日期(时间)中由field参数指定的日期成员进行加减计算. <br>  
     * 例子: <br>  
     * 如果Date类型的d为 2005年8月20日,那么 <br>  
     * calculate(d,GregorianCalendar.YEAR,-10)的值为1995年8月20日 <br>  
     * 而calculate(d,GregorianCalendar.YEAR,+10)的值为2015年8月20日 <br>  
     *   
     * @param d  
     *            日期(时间).  
     * @param field  
     *            日期成员. <br>  
     *            日期成员主要有: <br>  
     *            年:GregorianCalendar.YEAR <br>  
     *            月:GregorianCalendar.MONTH <br>  
     *            日:GregorianCalendar.DATE <br>  
     *            时:GregorianCalendar.HOUR <br>  
     *            分:GregorianCalendar.MINUTE <br>  
     *            秒:GregorianCalendar.SECOND <br>  
     *            毫秒:GregorianCalendar.MILLISECOND <br>  
     * @param amount  
     *            加减计算的幅度.+n=加n个由参数field指定的日期成员值;-n=减n个由参数field代表的日期成员值.  
     * @return 计算后的日期(时间).  
     */  
    private static Date calculate(Date d, int field, int amount) {   
        if (d == null)   
            return null;   
        GregorianCalendar g = new GregorianCalendar();   
        g.setGregorianChange(d);   
        g.add(field, amount);   
        return g.getTime();   
    }   
  
    /**  
     * 日期(时间)转化为字符串.  
     *   
     * @param formater  
     *            日期或时间的格式.  
     * @param aDate  
     *            java.util.Date类的实例.  
     * @return 日期转化后的字符串.  
     */  
    public static String date2String(String formater, Date aDate) {   
        if (formater == null || "".equals(formater))   
            return null;   
        if (aDate == null)   
            return null;   
        return (new SimpleDateFormat(formater)).format(aDate);   
    }   
  
    /**  
     * 当前日期(时间)转化为字符串.  
     *   
     * @param formater  
     *            日期或时间的格式.  
     * @return 日期转化后的字符串.  
     */  
    public static String date2String(String formater) {   
        return date2String(formater, new Date());   
    }   
       
    /**  
     * 获取当前日期对应的星期数.  
     * <br>1=星期天,2=星期一,3=星期二,4=星期三,5=星期四,6=星期五,7=星期六  
     * @return 当前日期对应的星期数  
     */  
    public static int dayOfWeek() {   
        GregorianCalendar g = new GregorianCalendar();   
        int ret = g.get(java.util.Calendar.DAY_OF_WEEK);   
        g = null;   
        return ret;   
    }   
  
  
    /**  
     * 获取所有的时区编号. <br>  
     * 排序规则:按照ASCII字符的正序进行排序. <br>  
     * 排序时候忽略字符大小写.  
     *   
     * @return 所有的时区编号(时区编号已经按照字符[忽略大小写]排序).  
     */  
    public static String[] fecthAllTimeZoneIds() {   
        Vector v = new Vector();   
        String[] ids = TimeZone.getAvailableIDs();   
        for (int i = 0; i < ids.length; i++) {   
            v.add(ids[i]);   
        }   
        java.util.Collections.sort(v, String.CASE_INSENSITIVE_ORDER);   
        v.copyInto(ids);   
        v = null;   
        return ids;   
    }   
  
    /**  
     * 将当前的日期时间字符串根据转换为指定时区的日期时间.  
     *   
     * @param srcFormater  
     *            待转化的日期时间的格式.  
     * @param srcDateTime  
     *            待转化的日期时间.  
     * @param dstFormater  
     *            目标的日期时间的格式.  
     * @param dstTimeZoneId  
     *            目标的时区编号.  
     *   
     * @return 转化后的日期时间.  
     */  
    public static String string2Timezone(String srcFormater,   
            String srcDateTime, String dstFormater, String dstTimeZoneId) {   
        if (srcFormater == null || "".equals(srcFormater))   
            return null;   
        if (srcDateTime == null || "".equals(srcDateTime))   
            return null;   
        if (dstFormater == null || "".equals(dstFormater))   
            return null;   
        if (dstTimeZoneId == null || "".equals(dstTimeZoneId))   
            return null;   
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);   
        try {   
            int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);   
            Date d = sdf.parse(srcDateTime);   
            long nowTime = d.getTime();   
            long newNowTime = nowTime - diffTime;   
            d = new Date(newNowTime);   
            return date2String(dstFormater, d);   
        } catch (ParseException e) {
            return null;   
        } finally {   
            sdf = null;   
        }   
    }   
  
    /**  
     * 获取系统当前默认时区与UTC的时间差.(单位:毫秒)  
     *   
     * @return 系统当前默认时区与UTC的时间差.(单位:毫秒)  
     */  
    private static int getDefaultTimeZoneRawOffset() {   
        return TimeZone.getDefault().getRawOffset();   
    }   
  
    /**  
     * 获取指定时区与UTC的时间差.(单位:毫秒)  
     *   
     * @param timeZoneId  
     *            时区Id  
     * @return 指定时区与UTC的时间差.(单位:毫秒)  
     */  
    private static int getTimeZoneRawOffset(String timeZoneId) {   
        return TimeZone.getTimeZone(timeZoneId).getRawOffset();   
    }   
  
    /**  
     * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)  
     *   
     * @param timeZoneId  
     *            时区Id  
     * @return 系统当前默认时区与指定时区的时间差.(单位:毫秒)  
     */  
    private static int getDiffTimeZoneRawOffset(String timeZoneId) {   
        return TimeZone.getDefault().getRawOffset()   
                - TimeZone.getTimeZone(timeZoneId).getRawOffset();   
    }   
  
    /**  
     * 将日期时间字符串根据转换为指定时区的日期时间.  
     *   
     * @param srcDateTime  
     *            待转化的日期时间.  
     * @param dstTimeZoneId  
     *            目标的时区编号.  
     *   
     * @return 转化后的日期时间.  
     * @see #string2Timezone(String, String, String, String)  
     */  
    public static String string2TimezoneDefault(String srcDateTime,   
            String dstTimeZoneId) {   
        return string2Timezone("yyyy-MM-dd HH:mm:ss", srcDateTime,   
                "yyyy-MM-dd'T'HH:mm:ss'Z'", dstTimeZoneId);   
    }   
    
    /**
     * 
     * @param utcTime 格式yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String stringUTC2Date(String utcTime){
    	if(utcTime != null){
	    	Calendar calendar = Calendar.getInstance(); 
	    	try {
	    		calendar.setTime(formatU.parse(utcTime));
	    		calendar.add(Calendar.HOUR_OF_DAY, 8);
	    		return formatTime.format(calendar.getTime());
	    	} catch (ParseException e) {
	    		try {
		    		calendar.setTime(formatUTime.parse(utcTime));
		    		calendar.add(Calendar.HOUR_OF_DAY, 8);
		    		return formatTime.format(calendar.getTime());
		    	} catch (ParseException e1) {
		    		e1.printStackTrace();
		    	}
	    	}
    	}
    	return null;
    }

    
    /**
     * 获取一天的起始时间
     * @param date yyyy-MM-dd
     * @return yyyy-MM-dd 00:00:00
     */
	public static String getStartTime(String date) {
        Calendar dayStart = Calendar.getInstance();
        try{
			 if(date ==null || "".equals(date)){
			 	dayStart.setTime(formatDay.parse(formatDay.format(new Date())));
			 }else{
			 	dayStart.setTime(formatDay.parse(date));
			 }
        } catch (ParseException e) {
			e.printStackTrace();
		} 
        dayStart.set(Calendar.HOUR, 0);  
        dayStart.set(Calendar.MINUTE, 0);  
        dayStart.set(Calendar.SECOND, 0);  
        dayStart.set(Calendar.MILLISECOND, 0);  
        return formatTime.format(dayStart.getTime());  
    }  
	
	/**
	 * 获取一天的结束时间
	 * @param date yyyy-MM-dd
	 * @return yyyy-MM-dd 23:59:59
	 */
	public static String getEndTime(String date) { 
        Calendar dayEnd = Calendar.getInstance();  
        try {
			 if(date ==null || "".equals(date)){
			 	dayEnd.setTime(formatDay.parse(formatDay.format(new Date())));
			 }else{
				 dayEnd.setTime(formatDay.parse(date));
			 }
		} catch (ParseException e) {
			e.printStackTrace();
		} 
        dayEnd.set(Calendar.HOUR, 23);  
        dayEnd.set(Calendar.MINUTE, 59);  
        dayEnd.set(Calendar.SECOND, 59);  
        dayEnd.set(Calendar.MILLISECOND, 999);  
        return formatTime.format(dayEnd.getTime());   
	}  
	
	/**
     * 获取一天的起始时间
     * @param date
     * @return time
     */
	public static Long getStartTimeLong(String date) {  
		long current;
		try {
			if(date ==null || "".equals(date)){
				current = formatDay.parse(formatDay.format(new Date())).getTime() + 1000*3600*24;  
			}else{
				current = formatDay.parse(date).getTime() + 1000*3600*24;
			}
			long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
			return zero;  
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return null;
    }  
	
	/**
	 * 获取一天的结束时间
	 * @param date
	 * @return time
	 */
	public static Long getEndTimeLong(String date) {  
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
        return dayEnd.getTime().getTime();   
	}  
	
	/**
	 * 获取一个月的第一天
	 * @param date
	 * @return time
	 */
	public static String getMonthFirstDay(String date) {  

		Calendar cal=Calendar.getInstance();//获取当前日期 
		try {
			if(date == null || "".equals(date)){
				cal.setTime(new Date());
			}else{
				cal.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		cal.set(Calendar.HOUR, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);  
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}
	
	/**
	 * 获取一个月的最后一天
	 * @param date
	 * @return time
	 */
	public static String getMonthLastDay(String date) {  

		Calendar cal=Calendar.getInstance();//获取当前日期 
		try {
			if(date == null || "".equals(date)){
				cal.setTime(new Date());
			}else{
				cal.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
		cal.set(Calendar.HOUR, 23);  
		cal.set(Calendar.MINUTE, 59);  
		cal.set(Calendar.SECOND, 59);  
		cal.set(Calendar.MILLISECOND, 999);  
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}
	
	
	/**
	 * 获取一年的第一天
	 * @param date
	 * @return time
	 */
	public static String getYearFirstDay(String date) {  

		Calendar cal=Calendar.getInstance();//获取当前日期 
		try {
			if(date == null || "".equals(date)){
				cal.setTime(new Date());
			}else{
				cal.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.MONTH, 0);  
		cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		cal.set(Calendar.HOUR, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);  
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}
	
	/**
	 * 获取一年的最后一天
	 * @param date
	 * @return time
	 */
	public static String getYearLastDay(String date) {  

		Calendar cal=Calendar.getInstance();//获取当前日期 
		try {
			if(date == null || "".equals(date)){
				cal.setTime(new Date());
			}else{
				cal.setTime(formatDay.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.MONTH, 11);  
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
		cal.set(Calendar.HOUR, 23);  
		cal.set(Calendar.MINUTE, 59);  
		cal.set(Calendar.SECOND, 59);  
		cal.set(Calendar.MILLISECOND, 999);  
		String firstDay = formatTime.format(cal.getTime());
		return firstDay;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayFormat(String date) {  
		DateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");

		if(date ==null || "".equals(date)){
			return formatDay.format(new Date());
	    }else{
	    	return date;
	    }
	} 
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthFormat(String date) {  
		DateFormat formatDay = new SimpleDateFormat("yyyy-MM");

		if(date ==null || "".equals(date)){
			return formatDay.format(new Date());
	    }else{
	    	try {
				return formatDay.format(formatDay.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    }
		return null;
	} 
  
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getYearFormat(String date) {  
		DateFormat formatDay = new SimpleDateFormat("yyyy");

		if(date ==null || "".equals(date)){
			return formatDay.format(new Date());
	    }else{
	    	try {
				return formatDay.format(formatDay.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    }
		return null;
	} 
	
	/**
	 * 判断是否为当天
	 * @param date
	 * @return
	 */
	public static boolean isToday(String date){
	     if(StringUtils.isEmpty(date)){
	    	 return true;
	     }
        //格式化为相同格式
        return date.equals(formatDay.format(new Date()));
	}
	
	
    /**
     * 前一天
     * @return
     */
    public static String  getLastDay() {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        Date date = calendar.getTime();  
        return formatDay.format(date); 
    }  
	
    public static void main(String[] argc) {   
        
        String[] ids = fecthAllTimeZoneIds();   
        String nowDateTime =date2String("yyyy-MM-dd HH:mm:ss");   
        System.out.println("The time Asia/Shanhai is " + nowDateTime);//程序本地运行所在时区为[Asia/Shanhai]   
        //显示世界每个时区当前的实际时间   
        for(int i=0;i <ids.length;i++){   
            System.out.println(" * " + ids[i] + "=" + string2TimezoneDefault(nowDateTime,ids[i]));    
        }   
        //显示程序运行所在地的时区   
        System.out.println("TimeZone.getDefault().getID()=" +TimeZone.getDefault().getID());   
    } 
}
