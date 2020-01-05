package com.wd.pydjc.elec.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wd.pydjc.bsd.dao.MeasTypeDao;
import com.wd.pydjc.bsd.model.MeasType;
import com.wd.pydjc.common.utils.DateUtil;
import com.wd.pydjc.influxDB.InfluxDBService;

/**
 * 通用方法类
 * 
 */
@Service
public class CommonElectricService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private MeasTypeDao measTypeDao;
	
	@Autowired
	private InfluxDBService influxDBService;
	
	/**
	 * 数据查询平均值
	 * @param startTime 开始时间  北京时间  格式 yyyy-MM-dd 00:00:00
	 * @param endTime   结束时间  北京时间  格式 yyyy-MM-dd 23:59:59
	 * @param measPointType 测点类型
	 * @return <测点ID，平均值>
	 */
	public Map<Long,BigDecimal> getElectiricMean(String startTime,String endTime,String measPointType){

		Map<Long,BigDecimal> map = new HashMap<Long,BigDecimal>();
	
		//测点类型
		String whereSql = "";
		if(!StringUtils.isEmpty(measPointType)){
			whereSql += "and (";
			List<String> listCode = this.getMeasPointType(measPointType);
			for (int i = 0; i < listCode.size(); i++) {
				if(i+1 != listCode.size()){
					whereSql += " type='" + listCode.get(i) + "' or" ; 
				}else{
					whereSql += " type='" + listCode.get(i) + "' " ; 
				}
			}
			whereSql += ")";
		}
	
		/**
		 * 转化为UTC 时间
		 */
		startTime = DateUtil.string2TimezoneDefault(startTime,"UTC");
		endTime = DateUtil.string2TimezoneDefault(endTime,"UTC");
		
		String sql = "select mean(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' "+ whereSql +" group by id";
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
	 * 数据查询最大值
	 * @param startTime 开始时间  北京时间  格式 yyyy-MM-dd 00:00:00
	 * @param endTime   结束时间  北京时间  格式 yyyy-MM-dd 23:59:59
	 * @param measPointType 测点类型
	 * @return <测点ID，<时间,最大值>> 北京时间
 	 */
	public Map<Long,Map<String,BigDecimal>> getElectiricMax(String startTime,String endTime,String measPointType){

		Map<Long,Map<String,BigDecimal>> map = new HashMap<Long,Map<String,BigDecimal>>();
		
		//测点类型
		String whereSql = "";
		if(!StringUtils.isEmpty(measPointType)){
			whereSql += "and (";
			List<String> listCode = this.getMeasPointType(measPointType);
			for (int i = 0; i < listCode.size(); i++) {
				if(i+1 != listCode.size()){
					whereSql += " type='" + listCode.get(i) + "' or" ; 
				}else{
					whereSql += " type='" + listCode.get(i) + "' " ; 
				}
			}
			whereSql += ")";
		}
		
		/**
		 * 转化为UTC 时间
		 */
		startTime = DateUtil.string2TimezoneDefault(startTime,"UTC");
		endTime = DateUtil.string2TimezoneDefault(endTime,"UTC");
		
		String sql = "select max(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' "+ whereSql +" group by id";
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
					Map<String, BigDecimal> dMap = new HashMap<String,BigDecimal>();
					//时间，最大值
					dMap.put(DateUtil.stringUTC2Date(lo.get(0).toString()),new BigDecimal(lo.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP));                   
					map.put(Long.parseLong(tagMap.get("id")), dMap);
				}
			}
		}
		return map;
	}
	
	/**
	 * 数据查询最小值
	 * @param startTime 开始时间  北京时间  格式 yyyy-MM-dd 00:00:00
	 * @param endTime   结束时间  北京时间  格式 yyyy-MM-dd 23:59:59
	 * @param measPointType 测点类型
	 * @return <测点ID，<时间,最小值>> 北京时间
	 */
	public Map<Long,Map<String,BigDecimal>> getElectiricMin(String startTime,String endTime,String measPointType){

		Map<Long,Map<String,BigDecimal>> map = new HashMap<Long,Map<String,BigDecimal>>();
		
		//测点类型
		String whereSql = "";
		if(!StringUtils.isEmpty(measPointType)){
			whereSql += "and (";
			List<String> listCode = this.getMeasPointType(measPointType);
			for (int i = 0; i < listCode.size(); i++) {
				if(i+1 != listCode.size()){
					whereSql += " type='" + listCode.get(i) + "' or" ; 
				}else{
					whereSql += " type='" + listCode.get(i) + "' " ; 
				}
			}
			whereSql += ")";
		}
				
		/**
		 * 根据日期得到开始时间和结束时间
		 * 转化为UTC 时间
		 */
		startTime = DateUtil.string2TimezoneDefault(startTime,"UTC");
		endTime = DateUtil.string2TimezoneDefault(endTime,"UTC");
		
		String sql = "select min(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' "+ whereSql +" group by id";
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
					Map<String, BigDecimal> dMap = new HashMap<String,BigDecimal>();
					//时间，最小值
					dMap.put(DateUtil.stringUTC2Date(lo.get(0).toString()),new BigDecimal(lo.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP));                   
					map.put(Long.parseLong(tagMap.get("id")), dMap);
				}
			}
		}
		return map;
	}
	
	/**
	 * 每隔一段时间的平均值
	 * @param startTime 开始时间  北京时间  格式 yyyy-MM-dd 00:00:00
	 * @param endTime   结束时间  北京时间  格式 yyyy-MM-dd 23:59:59
	 * @param measPointType
	 * @Param meanTime 每隔 多久的平均值      10m = 10分钟 
	 * @return map <测点ID,<时间，值>>
	 */
	public Map<Long,Map<String,BigDecimal>> getElectiricMeanByTime(String startTime,String endTime,String measPointType,String meanTime){

		Map<Long,Map<String,BigDecimal>> map = new HashMap<Long,Map<String,BigDecimal>>();
	
		//测点类型
		String whereSql = "";
		if(!StringUtils.isEmpty(measPointType)){
			whereSql += "and (";
			List<String> listCode = this.getMeasPointType(measPointType);
			for (int i = 0; i < listCode.size(); i++) {
				if(i+1 != listCode.size()){
					whereSql += " type='" + listCode.get(i) + "' or" ; 
				}else{
					whereSql += " type='" + listCode.get(i) + "' " ; 
				}
			}
			whereSql += ")";
		}
				
		/**
		 * 转化为UTC 时间
		 */
		startTime = DateUtil.string2TimezoneDefault(startTime,"UTC");
		endTime = DateUtil.string2TimezoneDefault(endTime,"UTC");
		
		String sql = "select mean(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' "+ whereSql +" group by time("+meanTime+"),id";
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
	 * 电量数据,每隔时间的最大值-最小值
	 * 每天的电量 = 每天的差值
	 * @param startTime 开始时间  北京时间  格式 yyyy-MM-dd 00:00:00
	 * @param endTime   结束时间  北京时间  格式 yyyy-MM-dd 23:59:59
	 * @Param time 每隔 多久的平均值      60m = 60分钟    1440m = 24小时
	 * @return map <测点ID,<时间，值>>
	 */
	public Map<Long,Map<String,BigDecimal>> getElectiricEp(String startTime,String endTime,String time){

		Map<Long,Map<String,BigDecimal>> map = new HashMap<Long,Map<String,BigDecimal>>();
		
		startTime = DateUtil.string2TimezoneDefault(startTime,"UTC");
		endTime = DateUtil.string2TimezoneDefault(endTime,"UTC");
	
		String sql = "select spread(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' and type='9010' group by time("+time+",960m),id";
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
	 * 电量数据,每隔时间的最大值-最小值
	 * 每天的电量 = 每天的差值
	 * @param startTime 开始时间  北京时间  格式 yyyy-MM-dd 00:00:00
	 * @param endTime   结束时间  北京时间  格式 yyyy-MM-dd 23:59:59
	 * @Param time 每隔 多久的平均值      60m = 60分钟    1440m = 24小时
	 * @return map <测点ID,<时间，值>>
	 */
	public Map<Long,BigDecimal> getElectiricEp(String startTime,String endTime){

		Map<Long,BigDecimal> map = new HashMap<Long,BigDecimal>();
		
		startTime = DateUtil.string2TimezoneDefault(startTime,"UTC");
		endTime = DateUtil.string2TimezoneDefault(endTime,"UTC");
	
		String sql = "select spread(value) from data where time>='"+ startTime +"'and time<='" + endTime + "' and type='9010' group by id";
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
					
					BigDecimal value = new BigDecimal(0);
					for (int j = 0; j < ll.size(); j++) {
						List<Object> lo = ll.get(j);
						value = new BigDecimal(lo.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
					}
					map.put(Long.parseLong(tagMap.get("id")),value);
				}
			}
		}
		return map;
	}
	
	/**
	 * 得到子测点类型信息
	 * @param measTypeCode
	 * @return
	 */
	public List<String> getMeasPointType(String measTypeCode){
		List<String> list = new ArrayList<String>();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentCode", measTypeCode);
		List<MeasType> listMeasType = measTypeDao.getChildList(params);
		for (int i = 0; i < listMeasType.size(); i++) {
			MeasType measType = listMeasType.get(i);
			list.add(measType.getCode645());
		}
		
		return list;
	}
	
	/*
	 * 2018-01-10T00:27:53.928Z
	 * 
	 */
	 public static String getDay(String time){
	    	Calendar calendar = Calendar.getInstance();  
	    	DateFormat formatU = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 
			try {
				calendar.setTime(formatU.parse(time));
				calendar.add(Calendar.HOUR_OF_DAY, 8);
				return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return "0";
	}
	
}
