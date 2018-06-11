package com.keda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.common.exception.BusinessException;

import com.keda.KedaCgformJavaInterDemo;

import org.jeecgframework.web.system.sms.service.TSSmsServiceI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antlr.collections.List;

/**
 * 
 * @ClassName:TestSendTask 
 * @Description: 测试定时任务
 * @date 2018-6-1 上午10:30:00
 * 
 */

@Service("kedaPutawaySendTask")
public class KedaPutawaySendTask implements Job{
	
	@Autowired
	private TSSmsServiceI tSSmsService;

	private ArrayList list;

	public void work(){
		long start = System.currentTimeMillis();
		org.jeecgframework.core.util.LogUtil.info("===================定时入库任务开始===================");	
		//连接JDBC
		String driver = "com.mysql.jdbc.Driver";    
    	String URL = "jdbc:mysql://localhost:3306/jeecg376";
		String USER ="root";
		String PASS ="root";
		Connection con = null;
		Statement stmt =null;
		Map<String,Object> map = new HashMap<String,Object>();
		try{
		      Class.forName("com.mysql.jdbc.Driver");
		      con = DriverManager.getConnection(URL, USER, PASS);
		       stmt = con.createStatement();
		      //查询status为0的id 
		      String sql ="SELECT f.id from wms_fetch f,wms_fetchdtl fdtl WHERE f.id=fdtl.fetchid and f.status=0 and f.error_msg is null;";
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	String id =rs.getString("id");
		    	map.put("id", id);
		    	map.put("status", "0");
				KedaCgformJavaInterDemo kedacgformJavaInterDemo = new KedaCgformJavaInterDemo();
				kedacgformJavaInterDemo.execute("",map);
		      }
		      rs.close();
	            stmt.close();
	            con.close();
	        }catch(SQLException se){
	            // 处理 JDBC 错误
	            se.printStackTrace();
	        }catch(Exception e){
	        	throw new BusinessException(e.getMessage());
	        }finally{
	            // 关闭资源
	            try{
	                if(stmt!=null) stmt.close();
	            }catch(SQLException se2){
	            }// 什么都不做
	            try{
	                if(con!=null) con.close();
	            }catch(SQLException se){
	                se.printStackTrace();
	            }
		try {			
			tSSmsService.send();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		org.jeecgframework.core.util.LogUtil.info("===================定时入库任务结束===================");
		long end = System.currentTimeMillis();
		long times = end - start;
		org.jeecgframework.core.util.LogUtil.info("总耗时"+times+"毫秒");
		}
	}
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		work();
	}
}
	
