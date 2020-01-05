package com.wd.pydjc.elec.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wd.pydjc.common.utils.DateUtil;
import com.wd.pydjc.influxDB.InfluxDBService;

@Service
public class MonthElectricService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private InfluxDBService influxDBService;
	
	
	/**
	 * 月数据查询平均值
	 * @param date 日期
	 * @return <测点ID，值>
	 */
	public Map<Long,BigDecimal> getMonthElectiricMean(String date){

		Map<Long,BigDecimal> map = new HashMap<Long,BigDecimal>();
	
		/**
		 * 根据日期得到开始时间和结束时间
		 * 转化为UTC 时间
		 */
		String startTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthFirstDay(date),"UTC");
		String endTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthLastDay(date),"UTC");
	
		String sql = "select mean(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' group by id";
		QueryResult queryResult = influxDBService.query(sql);
		List<Result> list = queryResult.getResults();
		for(int i=0;i<list.size();i++){
			Result result = list.get(i);
			List<Series> listSeries = result.getSeries();
			
			if(listSeries != null){
				for(int n=0;n<listSeries.size();n++){
					Series series = listSeries.get(n);
					Map<String,String> tagMap = series.getTags();
					List<List<Object>> ll = series.getValues();
					List<Object> lo = ll.get(0);
					map.put(Long.parseLong(tagMap.get("id")),new BigDecimal(lo.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP));
				}
			}
		}
		return map;
	}

	/**
	 * 日数据查询最大值
	 * @param date 日期  yyyy-MM-dd
	 * @return <测点ID，[时间,最大值]>
	 */
	public Map<Long,List<Object>> getMonthElectiricMax(String date){

		Map<Long,List<Object>> map = new HashMap<Long,List<Object>>();
		
		/**
		 * 根据日期得到开始时间和结束时间
		 * 转化为UTC 时间
		 */
		String startTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthFirstDay(date),"UTC");
		String endTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthLastDay(date),"UTC");
	
		String sql = "select max(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' group by id";
		QueryResult queryResult = influxDBService.query(sql);
		List<Result> list = queryResult.getResults();
		for(int i=0;i<list.size();i++){
			Result result = list.get(i);
			List<Series> listSeries = result.getSeries();
			
			if(listSeries != null){
				for(int n=0;n<listSeries.size();n++){
					Series series = listSeries.get(n);
					Map<String,String> tagMap = series.getTags();
					List<List<Object>> ll = series.getValues();
					List<Object> lo = ll.get(0);
					map.put(Long.parseLong(tagMap.get("id")), lo);
				}
			}
		}
		return map;
	}
	
	/**
	 * 日数据查询最小值
	 * @param date 日期
	 * @return <测点ID，[时间,最小值]>
	 */
	public Map<Long,List<Object>> getMonthElectiricMin(String date){

		Map<Long,List<Object>> map = new HashMap<Long,List<Object>>();
		
		/**
		 * 根据日期得到开始时间和结束时间
		 * 转化为UTC 时间
		 */
		String startTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthFirstDay(date),"UTC");
		String endTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthLastDay(date),"UTC");
	
		String sql = "select min(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' group by id";
		QueryResult queryResult = influxDBService.query(sql);
		List<Result> list = queryResult.getResults();
		for(int i=0;i<list.size();i++){
			Result result = list.get(i);
			List<Series> listSeries = result.getSeries();
			if(listSeries != null){
				for(int n=0;n<listSeries.size();n++){
					Series series = listSeries.get(n);
					Map<String,String> tagMap = series.getTags();
					List<List<Object>> ll = series.getValues();
					List<Object> lo = ll.get(0);
					map.put(Long.parseLong(tagMap.get("id")),lo);
				}
			}
		}
		return map;
	}
	
	
	
	
	/**
	 * 日数据查询曲线
	 * 每小时的电量 = 每小时的差值
	 * @param date 日期
	 * @return map <测点ID,<时间，值>>
	 */
	public Map<Long,Map<String,BigDecimal>> getMonthElectiricEpByTime(String date){

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dfZ.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Map<Long,Map<String,BigDecimal>> map = new HashMap<Long,Map<String,BigDecimal>>();

		/**
		 * 根据日期得到开始时间和结束时间
		 * 转化为UTC 时间
		 */
		String startTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthFirstDay(date),"UTC");
		String endTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthLastDay(date),"UTC");
		
		String sql = "select spread(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' and id='26' group by time(60m),id";
		QueryResult queryResult = influxDBService.query(sql);
		List<Result> list = queryResult.getResults();
		for(int i=0;i<list.size();i++){
			Result result = list.get(i);
			List<Series> listSeries = result.getSeries();
			
			if(listSeries != null){
				for(int n=0;n<listSeries.size();n++){
					Series series = listSeries.get(n);
					Map<String,String> tagMap = series.getTags();
					List<List<Object>> ll = series.getValues();
					
					Map<String,BigDecimal> mapValue = new HashMap<String,BigDecimal>();
					for (int j = 0; j < ll.size(); j++) {
						List<Object> lo = ll.get(j);
					
						if(lo.get(1) != null){
							mapValue.put(DateUtil.stringUTC2Date(lo.get(0).toString()), new BigDecimal(lo.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP));
						}else{
							mapValue.put(DateUtil.stringUTC2Date(lo.get(0).toString()), new BigDecimal(0));
						}
						
					}
					map.put(Long.parseLong(tagMap.get("id")),mapValue);
				}
			}
		}
		return map;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 当月的电量数据查询曲线
	 * 每天的电量 = 每天的差值
	 * @param date 日期
	 * @return map <测点ID,<时间，值>>
	 */
	public Map<Long,Map<Integer,BigDecimal>> getCurrentMonthElectiricEp(){

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		dfZ.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Map<Long,Map<Integer,BigDecimal>> map = new HashMap<Long,Map<Integer,BigDecimal>>();
	
		Calendar cale = Calendar.getInstance();  
        String firstday, lastday;  
        // 获取前月的第一天  
        cale = Calendar.getInstance();  
        cale.add(Calendar.MONTH, 0);  
        cale.set(Calendar.DAY_OF_MONTH, 1);  
        firstday = formatter.format(cale.getTime());  
        
		String startTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthFirstDay(firstday),"UTC");
		String endTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthLastDay(formatter.format(new Date())),"UTC");
	
		String sql = "select spread(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' and id='42' group by time(1440m),id";
		QueryResult queryResult = influxDBService.query(sql);
		List<Result> list = queryResult.getResults();
		for(int i=0;i<list.size();i++){
			Result result = list.get(i);
			List<Series> listSeries = result.getSeries();
			
			if(listSeries != null){
				for(int n=0;n<listSeries.size();n++){
					Series series = listSeries.get(n);
					Map<String,String> tagMap = series.getTags();
					List<List<Object>> ll = series.getValues();
					
					Map<Integer,BigDecimal> mapValue = new HashMap<Integer,BigDecimal>();
					for (int j = 0; j < ll.size(); j++) {
						List<Object> lo = ll.get(j);
						
					
							if(lo.get(1) != null){
								mapValue.put(changeDate(lo.get(0).toString()), new BigDecimal(lo.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP));
							}else{
								mapValue.put(changeDate(lo.get(0).toString()), new BigDecimal(0));
							}
							
					
					}
					map.put(Long.parseLong(tagMap.get("id")),mapValue);
				}
			}
		}
		return map;
	}
	
	
	
	/**
	 * 上月的电量数据查询曲线
	 * 每天的电量 = 每天的差值
	 * @param date 日期
	 * @return map <测点ID,<时间，值>>
	 */
	public Map<Long,Map<Integer,BigDecimal>> getLastMonthElectiricEp(){

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		dfZ.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Map<Long,Map<Integer,BigDecimal>> map = new HashMap<Long,Map<Integer,BigDecimal>>();
	
	
		Calendar cale = Calendar.getInstance();  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        String firstday, lastday;  
        // 获取前月的第一天  
        cale = Calendar.getInstance();  
        cale.add(Calendar.MONTH, -1);  
        cale.set(Calendar.DAY_OF_MONTH, 1);  
        firstday = format.format(cale.getTime());  
		
	    Calendar caleLast = Calendar.getInstance();   
	    caleLast.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
	    
		String startTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthFirstDay(firstday),"UTC");
		String endTime = DateUtil.string2TimezoneDefault(DateUtil.getMonthLastDay(format.format(caleLast.getTime())),"UTC");
	
		String sql = "select spread(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' and id='42' group by time(1440m),id";
		QueryResult queryResult = influxDBService.query(sql);
		List<Result> list = queryResult.getResults();
		for(int i=0;i<list.size();i++){
			Result result = list.get(i);
			List<Series> listSeries = result.getSeries();
			
			if(listSeries != null){
				for(int n=0;n<listSeries.size();n++){
					Series series = listSeries.get(n);
					Map<String,String> tagMap = series.getTags();
					List<List<Object>> ll = series.getValues();
					
					Map<Integer,BigDecimal> mapValue = new HashMap<Integer,BigDecimal>();
					for (int j = 0; j < ll.size(); j++) {
						List<Object> lo = ll.get(j);
						
						
							if(lo.get(1) != null){
								mapValue.put(changeDate(lo.get(0).toString()), new BigDecimal(lo.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP));
							}else{
								mapValue.put(changeDate(lo.get(0).toString()), new BigDecimal(0));
							}
							
					
					}
					map.put(Long.parseLong(tagMap.get("id")),mapValue);
				}
			}
		}
		return map;
	}

	
	/*
	 * 2018-01-10T00:27:53.928Z
	 * 
	 */
	 public static Integer changeDate(String time){
	    	Calendar calendar = Calendar.getInstance();  
	    	DateFormat formatU = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 
			try {
				calendar.setTime(formatU.parse(time));
				calendar.add(Calendar.HOUR_OF_DAY, 8);
				return calendar.get(Calendar.DAY_OF_MONTH);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
	}
	
}
