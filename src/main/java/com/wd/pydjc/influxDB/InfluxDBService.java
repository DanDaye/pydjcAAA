package com.wd.pydjc.influxDB;

import java.io.IOException;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Service;

/**
 * InfluxDB API.
 *
 * @author zhulz
 *
 */
@Service
public class InfluxDBService {

	private static InfluxDB influxDB;

	/**
	 * Create a influxDB connection
	 *
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static InfluxDB getInfluxDB(){
		if(influxDB == null){
			InfluxDBBuilder builder = new InfluxDBBuilder("http://118.31.2.207:8086", "admin", "admin");
			influxDB = builder.build();
			
			influxDB.setLogLevel(LogLevel.NONE);
	        System.out.println("################################################################################## ");
			System.out.println("#  Connected to InfluxDB Version: " + influxDB.version() + " #");
			System.out.println("##################################################################################");
		}
		return influxDB;
	}
	
	/**
	 * 查询数据封装
	 * @param sql
	 * @return List<Series>  返回查询结果的第一个series
	 */
	public QueryResult query(String sql){
		Query query = new Query(sql, "pydjc"); 
		return  getInfluxDB().query(query);
	}

}
