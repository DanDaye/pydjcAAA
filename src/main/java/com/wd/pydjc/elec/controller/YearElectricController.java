package com.wd.pydjc.elec.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.wd.pydjc.elec.service.MonthElectricService;

import io.swagger.annotations.Api;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 日用电
 * @author zhulz
 */
@Api(tags = "月用电")
@RestController
@RequestMapping("/elec/yearElectric")
public class YearElectricController {

	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private MonthElectricService yearElectricService;

	/**
	 * 
	 * @param deviceId
	 * @param date  yyyy-MM-dd
	 * @return
	 */
	@GetMapping("/list")
	public JSONObject list(Long deviceId,String date) {
		
		long nowTime = new Date().getTime();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/**
		 * deviceId 为空时 取根节点
		 */
		if(deviceId == null){
			deviceId = 1L;
		}
	
		Map<Long,BigDecimal> meanMap = yearElectricService.getMonthElectiricMean(date);
		Map<Long,List<Object>> maxMap = yearElectricService.getMonthElectiricMax(date);
		Map<Long,List<Object>> minMap = yearElectricService.getMonthElectiricMin(date);
		Map<Long,Map<String,BigDecimal>> epMap = yearElectricService.getMonthElectiricEpByTime(date);
		
		
		JSONObject rows = new JSONObject();
		JSONArray array = new JSONArray();
		List<Device> list = deviceService.getDeviceIncludeMeasPoint(deviceId);
		for(int i=0; i<list.size(); i++){
			JSONObject jsonObject = new JSONObject();
			Device device = list.get(i);
			List<MeasPoint> measList = device.getMeasPoints();
			
			jsonObject.put("id", device.getId());
			jsonObject.put("name", device.getName());
			jsonObject.put("date", DateUtil.getYearFormat(date));
			
			for (int j = 0; j < measList.size(); j++) {
				MeasPoint measPoint = measList.get(j);
				
				/**
				 * 负荷
				 */
				if("P".equals(measPoint.getMeasTypeCode())){
					List<Object> listObj = maxMap.get(measPoint.getId());
					String maxDate = null;
					BigDecimal maxValue = null;
					if(listObj != null && listObj.size() > 0){
						maxDate= changeDate(listObj.get(0).toString());
						maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("maxP", maxValue);
						jsonObject.put("maxDate", maxDate);
					}
					
					String minDate = null;
					BigDecimal minValue = null;
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minDate = changeDate(listMin.get(0).toString());
						minValue = new BigDecimal(listMin.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("minP", minValue);
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
				}else if("Ep".equals(measPoint.getMeasTypeCode())){
					
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					List<Object> listObj = maxMap.get(measPoint.getId());
					if(listObj != null && listObj.size() > 0){
						maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						
						jsonObject.put("maxEp", maxValue);
					}
					
					//电量
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minValue = new BigDecimal(listMin.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("Ep", maxValue.subtract(minValue));
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
				}else if("Eq".equals(measPoint.getMeasTypeCode())){
					
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					List<Object> listObj = maxMap.get(measPoint.getId());
					if(listObj != null && listObj.size() > 0){
						maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						
					}
					
					//电量
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minValue = new BigDecimal(listMin.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("Eq", maxValue.subtract(minValue));
					}
				}
				/**
				 * 电压
				 */
				else if("Va".equals(measPoint.getMeasTypeCode())){
					
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					List<Object> listObj = maxMap.get(measPoint.getId());
					if(listObj != null && listObj.size() > 0){
						maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("maxVa", maxValue);
					
					}
					
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("minVa", minValue);
					}
				}
				else if("Vb".equals(measPoint.getMeasTypeCode())){
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					List<Object> listObj = maxMap.get(measPoint.getId());
					if(listObj != null && listObj.size() > 0){
						maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("maxVb", maxValue);
					}
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("minVb", minValue);
					}
				}
				else if("Vc".equals(measPoint.getMeasTypeCode())){
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					List<Object> listObj = maxMap.get(measPoint.getId());
					if(listObj != null && listObj.size() > 0){
						maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("maxVc", maxValue);
					}
					
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("minVc", minValue);
					}
				}
				
				/**
				 * 电流
				 */
				else if("Ia".equals(measPoint.getMeasTypeCode())){
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					List<Object> listObj = maxMap.get(measPoint.getId());
					if(listObj != null && listObj.size() > 0){
						maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("maxIa", maxValue);
					}
					
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("minIa", minValue);
					}
				}
				
				else if("Ib".equals(measPoint.getMeasTypeCode())){
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					List<Object> listObj = maxMap.get(measPoint.getId());
					if(listObj != null && listObj.size() > 0){
					    maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("maxIb", maxValue);
					}
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("minIb", minValue);
					}
				}
				
				else if("Ic".equals(measPoint.getMeasTypeCode())){
					BigDecimal maxValue = new BigDecimal(0);
					BigDecimal minValue = new BigDecimal(0);
					List<Object> listObj = maxMap.get(measPoint.getId());
					if(listObj != null && listObj.size() > 0){
					    maxValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("maxIc", maxValue);
					}
					
					List<Object>  listMin = minMap.get(measPoint.getId());
					if(listMin != null && listMin.size() > 0){
						minValue = new BigDecimal(listObj.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
						jsonObject.put("minIc", minValue);
					}
				}
				/**
				 * 功率因素
				 */
				else if("Cos".equals(measPoint.getMeasTypeCode())){
					
					jsonObject.put("meanCos", meanMap.get(measPoint.getId()));
				}
			}
			array.add(jsonObject);		
		}
		rows.put("rows", array);
		return rows;
	}
	
	
//	
//	@GetMapping("/chart")
//	public JSONObject chart(Long deviceId,String date) {
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		DateFormat formatSearch = new SimpleDateFormat("yyyy-MM-dd");
//		/**
//		 * deviceId 为空时 取根节点
//		 */
//		if(deviceId == null){
//			deviceId = 1L;
//		}
//		Date searchDate = new Date();
//		if(date != null || !"".equals(date)){
//			try {
//				searchDate = formatSearch.parse(date);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		JSONObject rows = new JSONObject();
//		Map<Long,List<Object>> maxMap = dayElectricService.getDayElectiricMax(date);
//		Map<Long,List<Object>> minMap = dayElectricService.getDayElectiricMin(date);
//		Map<Long,Map<String,BigDecimal>> chartMap = dayElectricService.getDayElectiricMeanByTime(date);
//		Map<Long,Map<String,BigDecimal>> epMap = dayElectricService.getDayElectiricEpByTime(date);
//		
//		List<Device> list = deviceService.getDeviceIncludeMeasPoint(deviceId);
//		
//		/**
//		 * 设备下的测点信息
//		 */
//		if(list != null && list.size()  == 1){
//			Device device = list.get(0);
//			List<MeasPoint> measList = device.getMeasPoints();
//		
//			/**
//			 * 测点循环
//			 */
//			for (int j = 0; j < measList.size(); j++) {
//				MeasPoint measPoint = measList.get(j);
//			
//				/**
//				 * 负荷
//				 */
//				if("P".equals(measPoint.getMeasTypeCode())){
//					
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					  
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("time", listTime);
//				          rows.put("value", listDate);
//					  }
//					  
//			          /**
//			           * 最大负荷
//			           * 最小负荷
//			           * 平均负荷
//			           */
//			  		  Map<Long,BigDecimal> meanMap = dayElectricService.getDayElectiricMean(date);
//			  		  
//			  		  BigDecimal maxValue = new BigDecimal(0);
//			  		  BigDecimal minValue = new BigDecimal(0);
//				  	  List<Object> listMax = maxMap.get(measPoint.getId());
//					  if(listMax != null && listMax.size() > 0){
//						  String maxDate= changeDate(listMax.get(0).toString());
//						  maxValue = new BigDecimal(listMax.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						  rows.put("maxDate", maxDate);
//						  rows.put("maxValue", maxValue);
//					  }
//					  
//					  List<Object> listMin = minMap.get(measPoint.getId());
//					  if(listMin != null && listMin.size() > 0){
//						  String minDate= changeDate(listMin.get(0).toString());
//						  minValue = new BigDecimal(listMin.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						  rows.put("minDate", minDate);
//						  rows.put("minValue", minValue);
//					  }
//					  
//					  rows.put("meanValue", meanMap.get(measPoint.getId()));
//					  
//					  
//					  /**
//					    * 峰谷差 是最大负荷-最小负荷
//						* 峰谷差率 是   峰谷差/最大负荷
//						* 负荷率  平均负荷/最大负荷
//						*/
//					  BigDecimal meanValue = meanMap.get(measPoint.getId());
//					  if(meanValue != null){
//						  rows.put("maxMinDiffP",maxValue.subtract(minValue).divide(maxValue,3,BigDecimal.ROUND_HALF_UP));
//						  rows.put("avgP",meanValue.divide(maxValue,3,BigDecimal.ROUND_HALF_UP));
//						  rows.put("meanP", meanMap.get(measPoint.getId()));
//					  }
//				
//				/**
//				 * 电量
//				 */
//				}else if("Ep".equals(measPoint.getMeasTypeCode())){
//					
//				      Map<String,BigDecimal> listObj = epMap.get(measPoint.getId());
//						
//				      if(listObj != null){
//					      List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//					      /*
//				           * 通过time 来计算一天的 每隔一小时的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 * 6){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("epTime", listTime);
//				          rows.put("epValue", listDate);
//				      }
//					
//			          Calendar calendar = Calendar.getInstance(); 
//					  BigDecimal outPeakValue = new BigDecimal(0); //峰
//					  BigDecimal outValeValue = new BigDecimal(0); //谷
//					  BigDecimal outPikeValue = new BigDecimal(0); //尖
//					  BigDecimal totleValue = new BigDecimal(0); //峰
//				      Map<String,BigDecimal> epMapTime = epMap.get(measPoint.getId());
//				      if(epMapTime != null){
//						  for (String key : epMapTime.keySet()) {
//								try {
//									calendar.setTime(format.parse(key));
//								} catch (ParseException e) {
//									e.printStackTrace();
//								}
//								int n = calendar.get(Calendar.HOUR_OF_DAY);
//								if(n <= 7 || (n >= 11 && n <=12) || n >= 23){
//									outValeValue = outValeValue.add(epMapTime.get(key));
//								}else if (n >= 19 && n <=21){
//									outPikeValue = outPikeValue.add(epMapTime.get(key));
//								}else{
//									outPeakValue = outPeakValue.add(epMapTime.get(key));
//								}
//						  }
//						  rows.put("outPeakValue", outPeakValue);
//						  rows.put("outValeValue", outValeValue);
//						  rows.put("outPikeValue", outPikeValue);
//						  rows.put("totleValue", outPeakValue.add(outValeValue).add(outPikeValue));
//				      }	
//					  
//				}
//				
//				/**
//				 * 电压
//				 */
//				else if("Va".equals(measPoint.getMeasTypeCode())){
//					  
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("VaTime", listTime);
//				          rows.put("VaValue", listDate);
//					  }
//			          
//			          //
//			  		  BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxVa", maxValue);
//						 rows.put("maxVaDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minVa", minValue);
//						 rows.put("minVaDate", changeDate(minVList.get(0).toString()));
//					  }
//			          
//				}
//				else if("Vb".equals(measPoint.getMeasTypeCode())){
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("VbTime", listTime);
//				          rows.put("VbValue", listDate);
//					  }
//					  
//			          //
//			  		  BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxVb", maxValue);
//						 rows.put("maxVbDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minVb", minValue);
//						 rows.put("minVbDate", changeDate(minVList.get(0).toString()));
//					  }
//			          
//				}
//				else if("Vc".equals(measPoint.getMeasTypeCode())){
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("VcTime", listTime);
//				          rows.put("VcValue", listDate);
//					  }
//					  
//			          //
//			  		  BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxVc", maxValue);
//						 rows.put("maxVcDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minVc", minValue);
//						 rows.put("minVcDate", changeDate(minVList.get(0).toString()));
//					  }
//				}
//				
//				/**
//				 * 电流
//				 */
//				else if("Ia".equals(measPoint.getMeasTypeCode())){
//					  
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("IaTime", listTime);
//				          rows.put("IaValue", listDate);
//					  }
//					  
//			          //
//			  		  BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxIa", maxValue);
//						 rows.put("maxIaDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minIa", minValue);
//						 rows.put("minIaDate", changeDate(minVList.get(0).toString()));
//					  }
//				}
//				else if("Ib".equals(measPoint.getMeasTypeCode())){
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("IbTime", listTime);
//				          rows.put("IbValue", listDate);
//					  }
//			          
//			          //
//			  		  BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxIb", maxValue);
//						 rows.put("maxIbDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minIb", minValue);
//						 rows.put("minIbDate", changeDate(minVList.get(0).toString()));
//					  }
//				}
//				else if("Ic".equals(measPoint.getMeasTypeCode())){
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("IcTime", listTime);
//				          rows.put("IcValue", listDate);
//					  }
//			          
//			          //
//			  		  BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxIc", maxValue);
//						 rows.put("maxIcDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minIc", minValue);
//						 rows.put("minIcDate", changeDate(minVList.get(0).toString()));
//					  }
//				}
//				/**
//				 * 功率因素
//				 */
//				else if("Cos".equals(measPoint.getMeasTypeCode())){
//					
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("CosTime", listTime);
//				          rows.put("CosValue", listDate);
//					  }
//			          
//			          
//			          BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxCos", maxValue);
//						 rows.put("maxCosDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minCos", minValue);
//						 rows.put("minCosDate", changeDate(minVList.get(0).toString()));
//					  }
//				}
//				
//				/**
//				 * 功率因素a
//				 */
//				else if("Ca".equals(measPoint.getMeasTypeCode())){
//					
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("CaTime", listTime);
//				          rows.put("CaValue", listDate);
//					  }
//					  
//			          BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxCa", maxValue);
//						 rows.put("maxCaDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minCa", minValue);
//						 rows.put("minCaDate", changeDate(minVList.get(0).toString()));
//					  }
//				}
//				
//				/**
//				 * 功率因素
//				 */
//				else if("Cb".equals(measPoint.getMeasTypeCode())){
//					
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("CbTime", listTime);
//				          rows.put("CbValue", listDate);
//					  }
//					  
//			          BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxCb", maxValue);
//						 rows.put("maxCbDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minCb", minValue);
//						 rows.put("minCbDate", changeDate(minVList.get(0).toString()));
//					  }
//				}
//				
//				/**
//				 * 功率因素
//				 */
//				else if("Cc".equals(measPoint.getMeasTypeCode())){
//					
//					  Map<String,BigDecimal> listObj = chartMap.get(measPoint.getId());
//					
//					  if(listObj != null){
//				          List<BigDecimal> listDate = new ArrayList<BigDecimal>();
//				          List<String> listTime = new ArrayList<String>();
//				          
//				          /*
//				           * 通过time 来计算一天的 每隔十分钟的数据
//				           */
//				          Long startTimeLong = DateUtil.getStartTimeLong(date);
//				          long endTime = (startTimeLong + 60 * 1000 * 60 * 24);
//				          for(long n=startTimeLong; n<= endTime; n = n + 600000 ){
//				        	  String key = format.format(new Date(n));
//				        	  BigDecimal value = listObj.get(key);
//				        	  listTime.add(key);
//				        	  if(value != null){
//				        		  listDate.add(value);
//				        	  }else{
//				        		  listDate.add(new BigDecimal(0));
//				        	  }
//				          }
//				          
//				          rows.put("CcTime", listTime);
//				          rows.put("CcValue", listDate);
//					  }
//					  
//			          BigDecimal maxValue = new BigDecimal(0);
//					  BigDecimal minValue = new BigDecimal(0);
//					  List<Object> maxVList = maxMap.get(measPoint.getId());
//					  if(maxVList != null && maxVList.size() > 0){
//						 maxValue = new BigDecimal(maxVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("maxCc", maxValue);
//						 rows.put("maxCcDate", changeDate(maxVList.get(0).toString()));
//					  }
//					
//					  List<Object>  minVList = minMap.get(measPoint.getId());
//					  if(minVList != null && minVList.size() > 0){
//						 minValue = new BigDecimal(minVList.get(1).toString()).setScale(3, BigDecimal.ROUND_HALF_UP);
//						 rows.put("minCc", minValue);
//						 rows.put("minCcDate", changeDate(minVList.get(0).toString()));
//					  }
//				}
//			}
//		}
//		List<Integer> listData = new ArrayList<Integer>();
//		List<Integer> epLabel = new ArrayList<Integer>();
//		for (int i = 0; i < 24 ; i++) {
//			for (int j = 0; j < 6; j++) {
//				listData.add(i);
//			}
//			epLabel.add(i);
//		}
//		rows.put("label", listData);
//		rows.put("epLabel", epLabel);
//		
//		return rows;
//	}
	
    public String changeDate(String time){
    	Calendar calendar = Calendar.getInstance();  
    	DateFormat formatU = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); 
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			calendar.setTime(formatU.parse(time));
			calendar.add(Calendar.HOUR_OF_DAY, 8);
			return format.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }

	
}
