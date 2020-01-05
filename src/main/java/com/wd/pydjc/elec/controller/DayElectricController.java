package com.wd.pydjc.elec.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wd.pydjc.bsd.model.Device;
import com.wd.pydjc.bsd.model.MeasPoint;
import com.wd.pydjc.bsd.service.DeviceService;
import com.wd.pydjc.common.utils.DateUtil;
import com.wd.pydjc.elec.service.CommonElectricService;

import io.swagger.annotations.Api;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 日用电
 * @author zhulz
 */
@Api(tags = "日用电")
@RestController
@RequestMapping("/elec/dayElectric")
public class DayElectricController {

	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private CommonElectricService commonElectricService;

	/**
	 * 
	 * @param deviceId
	 * @param date  yyyy-MM-dd
	 * @param measPointType  测点类型
	 * @return
	 */
	@GetMapping("/list")
	public JSONObject list(Long deviceId,String date,String measPointType) {
		
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject rows = new JSONObject();
		JSONArray array = new JSONArray();
		
		String startTime = DateUtil.getStartTime(date);
		String endTime = DateUtil.getEndTime(date);
		
		Map<Long,BigDecimal> meanMap = commonElectricService.getElectiricMean(startTime,endTime,measPointType);
		Map<Long,Map<String,BigDecimal>> maxMap = commonElectricService.getElectiricMax(startTime,endTime,measPointType);
		Map<Long,Map<String,BigDecimal>> minMap = commonElectricService.getElectiricMin(startTime,endTime,measPointType);
		
		Map<Long,Map<String,BigDecimal>> epMap = commonElectricService.getElectiricEp(startTime,endTime,"60m");
	
		List<Device> list = deviceService.getDeviceIncludeMeasPoint(deviceId);
		for(int i=0; i<list.size(); i++){
			JSONObject jsonObject = new JSONObject();
			Device device = list.get(i);
			List<MeasPoint> measList = device.getMeasPoints();
			
			jsonObject.put("id", device.getId());
			jsonObject.put("name", device.getName());
			jsonObject.put("date", DateUtil.getDayFormat(date));
			
			
			for (int j = 0; j < measList.size(); j++) {
				MeasPoint measPoint = measList.get(j);
				
				/**
				 * 负荷
				 */
				if("P".equals(measPointType) && "P".equals(measPoint.getMeasTypeCode())){
					Map<String,BigDecimal> mapMax = maxMap.get(measPoint.getId());
					
					//最大值
					BigDecimal maxValue = null;
					String maxDate = null;
					if(mapMax != null){
						for (String key : mapMax.keySet()) {
							 maxValue = mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							 jsonObject.put("maxP", maxValue);
							 jsonObject.put("maxDate", maxDate);
						}
					}
					
					//最小值
					String minDate = null;
					BigDecimal minValue = null;
					Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
					if(mapMin != null){
						for (String key : mapMin.keySet()) {
							 minValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							 jsonObject.put("minP", minValue);
						}
					}
					
					/**
					 * 峰谷差 是最大负荷-最小负荷
					 * 峰谷差率 是   峰谷差/最大负荷
					 * 负荷率  平均负荷/最大负荷
					 */
					BigDecimal meanValue = meanMap.get(measPoint.getId());
					if(meanValue != null){
						jsonObject.put("maxMinDiffP",maxValue.subtract(minValue).divide(maxValue,3,BigDecimal.ROUND_HALF_UP));
						jsonObject.put("avgP",meanValue.divide(maxValue,3,BigDecimal.ROUND_HALF_UP));
						jsonObject.put("meanP", meanMap.get(measPoint.getId()));
					}
				/**
				 * 电量
				 */
				}else if("E".equals(measPointType) && "Ep".equals(measPoint.getMeasTypeCode()) ){
					
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					Map<String,BigDecimal>  mapMax = maxMap.get(measPoint.getId());
					if(mapMax != null){
						for (String key : mapMax.keySet()) {
							maxValue = mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							jsonObject.put("maxEp", minValue);
						}
					}
					
					
					//电量
					Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
					if(mapMin != null){
						for (String key : mapMin.keySet()) {
							minValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							jsonObject.put("Ep", maxValue.subtract(minValue));
						}
					}
					
					Calendar calendar = Calendar.getInstance(); 
					BigDecimal outPeakValue = new BigDecimal(0); //峰
					BigDecimal outValeValue = new BigDecimal(0); //谷
					BigDecimal outPikeValue = new BigDecimal(0); //尖
					Map<String,BigDecimal> epMapTime = epMap.get(measPoint.getId());
					
					if(epMapTime != null){
						for (String key : epMapTime.keySet()) {
							try {
								calendar.setTime(sdf.parse(key));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							int n = calendar.get(Calendar.HOUR_OF_DAY);
							if(n <= 7 || (n >= 11 && n <=12) || n >= 23){
								outValeValue = outValeValue.add(epMapTime.get(key));
							}else if (n >= 19 && n <=21){
								outPikeValue = outPikeValue.add(epMapTime.get(key));
							}else{
								outPeakValue = outPeakValue.add(epMapTime.get(key));
							}
						}
						
						jsonObject.put("outPeakValue", outPeakValue);
						jsonObject.put("outValeValue", outValeValue);
						jsonObject.put("outPikeValue", outPikeValue);
					}
		
				
				/**
				 * 无功电量
				 */
				}else if("E".equals(measPointType) && "Eq".equals(measPoint.getMeasTypeCode())){
					
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					Map<String,BigDecimal>  mapMax = maxMap.get(measPoint.getId());
					if(mapMax != null){
						for (String key : mapMax.keySet()) {
							maxValue =  mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
						}
					}
					
					//电量
					Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
					if(mapMin != null){
						for (String key : mapMin.keySet()) {
							minValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							jsonObject.put("Eq", maxValue.subtract(minValue));
						}
					}
				}
				/**
				 * 电压
				 */
				else if("U".equals(measPointType) && ("Va".equals(measPoint.getMeasTypeCode()) || 
													  	 "Vb".equals(measPoint.getMeasTypeCode()) ||
													  		"Vc".equals(measPoint.getMeasTypeCode()))){
					
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					Map<String,BigDecimal>  mapMax = maxMap.get(measPoint.getId());
					if(mapMax != null){
						for (String key : mapMax.keySet()) {
							maxValue = mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							jsonObject.put("max" + measPoint.getMeasTypeCode(), maxValue);
						}
					}
					
					Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
					if(mapMin != null){
						for (String key : mapMin.keySet()) {
							minValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							jsonObject.put("min" + measPoint.getMeasTypeCode(), minValue);
						}
					}
				}
				
				/**
				 * 电流
				 */
				else if("I".equals(measPointType) && ("Ia".equals(measPoint.getMeasTypeCode()) || 
													  	 "Ib".equals(measPoint.getMeasTypeCode()) ||
													  		"Ic".equals(measPoint.getMeasTypeCode()))){
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					Map<String,BigDecimal>  mapMax = maxMap.get(measPoint.getId());
					if(mapMax != null){
						for (String key : mapMax.keySet()) {
							maxValue = mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							jsonObject.put("max" + measPoint.getMeasTypeCode(), maxValue);
						
						}
					}
					
					Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
					if(mapMin != null){
						for (String key : mapMin.keySet()) {
							minValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							jsonObject.put("min" + measPoint.getMeasTypeCode(), minValue);
						}
					}
				}
				/**
				 * 功率因素
				 */
				else if("Cos".equals(measPointType) && "Cos".equals(measPoint.getMeasTypeCode())){
					jsonObject.put("meanCos", meanMap.get(measPoint.getId()));
				}
			}
			array.add(jsonObject);		
		}
		rows.put("rows", array);
		return rows;
	}
	
	
	
	@GetMapping("/chart")
	public JSONObject chart(Long deviceId,String date,String measPointType) {
		
		JSONObject rows = new JSONObject();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<Long,Map<String,BigDecimal>> maxMap = commonElectricService.getElectiricMax(DateUtil.getStartTime(date),DateUtil.getEndTime(date),measPointType);
		Map<Long,Map<String,BigDecimal>> minMap = commonElectricService.getElectiricMin(DateUtil.getStartTime(date),DateUtil.getEndTime(date),measPointType);
		
		
		Map<Long,Map<String,BigDecimal>> chartMap = commonElectricService.getElectiricMeanByTime(DateUtil.getStartTime(date),DateUtil.getEndTime(date),measPointType,"10m");
		Map<Long,Map<String,BigDecimal>> epMap = commonElectricService.getElectiricEp(DateUtil.getStartTime(date),DateUtil.getEndTime(date),"60m");
		
		List<Device> list = deviceService.getDeviceIncludeMeasPoint(deviceId);
		
		/**
		 * 设备下的测点信息
		 */
		if(list != null && list.size()  == 1){
			Device device = list.get(0);
			List<MeasPoint> measList = device.getMeasPoints();
		
			/**
			 * 测点循环
			 */
			for (int j = 0; j < measList.size(); j++) {
				MeasPoint measPoint = measList.get(j);
			
				/**
				 * 负荷
				 */
				if("P".equals(measPointType) && "P".equals(measPoint.getMeasTypeCode())){
					
					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
					  
					  if(listObj != null){
				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
				          List<String> listTime = new ArrayList<String>();
				          
				          /*
				           * 通过time 来计算一天的 每隔十分钟的数据
				           */
				          Long startTimeLong = DateUtil.getStartTimeLong(date);
				 		  long endTime = 0L;
				 		  if(DateUtil.isToday(date)){
				 			 endTime = new Date().getTime();
				 		  }else{
				 			 endTime = (startTimeLong + 60 * 1000 * 60 * 24);
				 		  }
				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
				        	  String key = format.format(new Date(n));
				        	  BigDecimal value = listObj.get(key);
				        	  listTime.add(key);
				        	  if(value != null){
				        		  listDate.add(value);
				        	  }else{
				        		  listDate.add(new BigDecimal(0));
				        	  }
				          }
				          
				          rows.put("time", listTime);
				          rows.put("value", listDate);
					  }
					  
			          /**
			           * 最大负荷
			           * 最小负荷
			           * 平均负荷
			           */
			  		  Map<Long,BigDecimal> meanMap = commonElectricService.getElectiricMean(DateUtil.getStartTime(date),DateUtil.getEndTime(date),measPointType);
			  		  
			  		  BigDecimal maxValue = new BigDecimal(0);
			  		  BigDecimal minValue = new BigDecimal(0);
			  		  Map<String,BigDecimal>  mapMax = maxMap.get(measPoint.getId());
			  		  if(mapMax != null){
			  			  for (String key : mapMax.keySet()) {
							  maxValue = mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							  rows.put("maxDate", key);
							  rows.put("maxValue", maxValue);
			  			  }
			  		  }
					  
					  
					  Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
			  		  if(mapMin != null){
			  			  for (String key : mapMin.keySet()) {
							  maxValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
							  rows.put("minDate", key);
							  rows.put("minValue", minValue);
			  			  }
			  		  }
			  		  
					  rows.put("meanValue", meanMap.get(measPoint.getId()));
					  
					  
					  /**
					    * 峰谷差 是最大负荷-最小负荷
						* 峰谷差率 是   峰谷差/最大负荷
						* 负荷率  平均负荷/最大负荷
						*/
					  BigDecimal meanValue = meanMap.get(measPoint.getId());
					  if(meanValue != null){
						  rows.put("maxMinDiffP",maxValue.subtract(minValue).divide(maxValue,3,BigDecimal.ROUND_HALF_UP));
						  rows.put("avgP",meanValue.divide(maxValue,3,BigDecimal.ROUND_HALF_UP));
						  rows.put("meanP", meanMap.get(measPoint.getId()));
					  }
				
				/**
				 * 电量
				 */
				}else if("E".equals(measPointType) && "Ep".equals(measPoint.getMeasTypeCode())){
					
				      Map<String,BigDecimal> listObj = epMap.get(measPoint.getId());
						
				      if(listObj != null){
					      List<BigDecimal> listDate = new ArrayList<BigDecimal>();
				          List<String> listTime = new ArrayList<String>();
					      /*
				           * 通过time 来计算一天的 每隔一小时的数据
				           */
				          Long startTimeLong = DateUtil.getStartTimeLong(date);
				 		  long endTime = 0L;
				 		  if(DateUtil.isToday(date)){
				 			 endTime = new Date().getTime();
				 		  }else{
				 			 endTime = (startTimeLong + 60 * 1000 * 60 * 24);
				 		  }
				          for(long n=startTimeLong; n<= endTime; n = n + 600000 * 6){
				        	  String key = format.format(new Date(n));
				        	  BigDecimal value = listObj.get(key);
				        	  listTime.add(key);
				        	  if(value != null){
				        		  listDate.add(value);
				        	  }else{
				        		  listDate.add(new BigDecimal(0));
				        	  }
				          }
				          
				          rows.put("epTime", listTime);
				          rows.put("epValue", listDate);
				      }
					
			          Calendar calendar = Calendar.getInstance(); 
					  BigDecimal outPeakValue = new BigDecimal(0); //峰
					  BigDecimal outValeValue = new BigDecimal(0); //谷
					  BigDecimal outPikeValue = new BigDecimal(0); //尖
					  BigDecimal totleValue = new BigDecimal(0); //峰
				      Map<String,BigDecimal> epMapTime = epMap.get(measPoint.getId());
				      if(epMapTime != null){
						  for (String key : epMapTime.keySet()) {
								try {
									calendar.setTime(format.parse(key));
								} catch (ParseException e) {
									e.printStackTrace();
								}
								int n = calendar.get(Calendar.HOUR_OF_DAY);
								if(n <= 7 || (n >= 11 && n <=12) || n >= 23){
									outValeValue = outValeValue.add(epMapTime.get(key));
								}else if (n >= 19 && n <=21){
									outPikeValue = outPikeValue.add(epMapTime.get(key));
								}else{
									outPeakValue = outPeakValue.add(epMapTime.get(key));
								}
						  }
						  rows.put("outPeakValue", outPeakValue);
						  rows.put("outValeValue", outValeValue);
						  rows.put("outPikeValue", outPikeValue);
						  rows.put("totleValue", outPeakValue.add(outValeValue).add(outPikeValue));
				      }	
					  
				}
				
				/**
				 * 电压
				 */
				else if("U".equals(measPointType) && ("Va".equals(measPoint.getMeasTypeCode()) || 
													  	 "Vb".equals(measPoint.getMeasTypeCode()) ||
													  		"Vc".equals(measPoint.getMeasTypeCode()))){
					  
					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
					
					  if(listObj != null){
				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
				          List<String> listTime = new ArrayList<String>();
				          
				          /*
				           * 通过time 来计算一天的 每隔十分钟的数据
				           */
				          Long startTimeLong = DateUtil.getStartTimeLong(date);
				 		  long endTime = 0L;
				 		  if(DateUtil.isToday(date)){
				 			 endTime = new Date().getTime();
				 		  }else{
				 			 endTime = (startTimeLong + 60 * 1000 * 60 * 24);
				 		  }
				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
				        	  String key = format.format(new Date(n));
				        	  BigDecimal value = listObj.get(key);
				        	  listTime.add(key);
				        	  if(value != null){
				        		  listDate.add(value);
				        	  }else{
				        		  listDate.add(new BigDecimal(0));
				        	  }
				          }
				          
				          rows.put(measPoint.getMeasTypeCode() + "Time", listTime);
				          rows.put(measPoint.getMeasTypeCode() + "Value", listDate);
					  }
			          
			          //
			  		  BigDecimal maxValue = new BigDecimal(0);
					  BigDecimal minValue = new BigDecimal(0);
					  Map<String,BigDecimal>  mapMax = maxMap.get(measPoint.getId());
					  if(mapMax != null){
							for (String key : mapMax.keySet()) {
								 maxValue = mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
								 rows.put("max"+measPoint.getMeasTypeCode(), maxValue);
								 rows.put("max"+measPoint.getMeasTypeCode()+"Date", key);
							}
					  }
					  
					  Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
					  if(mapMin != null){
							for (String key : mapMin.keySet()) {
								 maxValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
								 rows.put("min" + measPoint.getMeasTypeCode(), minValue);
								 rows.put("min"+ measPoint.getMeasTypeCode() +"Date", key);
							}
					  }
			          
				}
			
				
				/**
				 * 电流
				 */
				else if("I".equals(measPointType) && ("Ia".equals(measPoint.getMeasTypeCode()) || 
													  	 "Ib".equals(measPoint.getMeasTypeCode()) ||
													  		"Ic".equals(measPoint.getMeasTypeCode()))){
					  
					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
					
					  if(listObj != null){
				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
				          List<String> listTime = new ArrayList<String>();
				          
				          /*
				           * 通过time 来计算一天的 每隔十分钟的数据
				           */
				          Long startTimeLong = DateUtil.getStartTimeLong(date);
				 		  long endTime = 0L;
				 		  if(DateUtil.isToday(date)){
				 			 endTime = new Date().getTime();
				 		  }else{
				 			 endTime = (startTimeLong + 60 * 1000 * 60 * 24);
				 		  }
				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
				        	  String key = format.format(new Date(n));
				        	  BigDecimal value = listObj.get(key);
				        	  listTime.add(key);
				        	  if(value != null){
				        		  listDate.add(value);
				        	  }else{
				        		  listDate.add(new BigDecimal(0));
				        	  }
				          }
				          
				          rows.put(measPoint.getMeasTypeCode() + "Time", listTime);
				          rows.put(measPoint.getMeasTypeCode() + "Value", listDate);
					  }
					  
			          //
			  		  BigDecimal maxValue = new BigDecimal(0);
					  BigDecimal minValue = new BigDecimal(0);
					  Map<String,BigDecimal>  mapMax = maxMap.get(measPoint.getId());
					  if(mapMax != null){
							for (String key : mapMax.keySet()) {
								 maxValue = mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
								 rows.put("max"+measPoint.getMeasTypeCode(), maxValue);
								 rows.put("max"+measPoint.getMeasTypeCode()+"Date", key);
							}
					  }
					
					  Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
					  if(mapMin != null){
							for (String key : mapMin.keySet()) {
								 maxValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
								 rows.put("min" + measPoint.getMeasTypeCode(), minValue);
								 rows.put("min"+ measPoint.getMeasTypeCode() +"Date", key);
							}
					  }
				}
				
				/**
				 * 功率因素
				 */
				else if("Cos".equals(measPointType) && ("Cos".equals(measPoint.getMeasTypeCode()) ||
															"Ca".equals(measPoint.getMeasTypeCode()) || 
														  	 "Cb".equals(measPoint.getMeasTypeCode()) ||
														  		"Cc".equals(measPoint.getMeasTypeCode()))){
					
					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
					
					  if(listObj != null){
				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
				          List<String> listTime = new ArrayList<String>();
				          
				          /*
				           * 通过time 来计算一天的 每隔十分钟的数据
				           */
				          Long startTimeLong = DateUtil.getStartTimeLong(date);
				 		  long endTime = 0L;
				 		  if(DateUtil.isToday(date)){
				 			 endTime = new Date().getTime();
				 		  }else{
				 			 endTime = (startTimeLong + 60 * 1000 * 60 * 24);
				 		  }
				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
				        	  String key = format.format(new Date(n));
				        	  BigDecimal value = listObj.get(key);
				        	  listTime.add(key);
				        	  if(value != null){
				        		  listDate.add(value);
				        	  }else{
				        		  listDate.add(new BigDecimal(0));
				        	  }
				          }
				          
				          rows.put(measPoint.getMeasTypeCode() + "Time", listTime);
				          rows.put(measPoint.getMeasTypeCode() + "Value", listDate);
					  }
			          
			          
					  BigDecimal maxValue = new BigDecimal(0);
					  BigDecimal minValue = new BigDecimal(0);
					  Map<String,BigDecimal>  mapMax = maxMap.get(measPoint.getId());
					  if(mapMax != null){
							for (String key : mapMax.keySet()) {
								 maxValue = mapMax.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
								 rows.put("max"+measPoint.getMeasTypeCode(), maxValue);
								 rows.put("max"+measPoint.getMeasTypeCode()+"Date", key);
							}
					  }
					
					  Map<String,BigDecimal>  mapMin = minMap.get(measPoint.getId());
					  if(mapMin != null){
							for (String key : mapMin.keySet()) {
								 maxValue = mapMin.get(key).setScale(3, BigDecimal.ROUND_HALF_UP);
								 rows.put("min" + measPoint.getMeasTypeCode(), minValue);
								 rows.put("min"+ measPoint.getMeasTypeCode() +"Date", key);
							}
					  }
				}
			}
		}
		List<Integer> listData = new ArrayList<Integer>();
		List<Integer> epLabel = new ArrayList<Integer>();
		for (int i = 0; i < 24 ; i++) {
			for (int j = 0; j < 6; j++) {
				listData.add(i);
			}
			epLabel.add(i);
		}
		rows.put("label", listData);
		rows.put("epLabel", epLabel);
		
		return rows;
	}
	
}
