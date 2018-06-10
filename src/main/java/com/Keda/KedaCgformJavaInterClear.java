package com.Keda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.stereotype.Service;

@Service("kedacgformJavaInterClear")
public class KedaCgformJavaInterClear implements CgformEnhanceJavaInter {

	private String a;

	public void execute(String tableName, Map map) throws BusinessException {
		LogUtil.info("============调用[java增强]成功!========tableName:"+tableName+"===map==="+map);
		//无异常信息时返回信息
		String error_msg =(String)map.get("error_msg");
		if(error_msg=="null"||error_msg.equals("")){
			
			throw new BusinessException("不需要进行异常清除!");
		}  

		//JDBC连接
		try{
			Class.forName("com.mysql.jdbc.Driver");
	    	String URL = "jdbc:mysql://localhost:3306/jeecg376";
			String USER ="root";
			String PASS ="root";
			a = (String) map.get("id");
			String sql ="UPDATE wms_fetch SET error_msg=null WHERE id='"+a+"';";
			Connection con = DriverManager.getConnection(URL, USER, PASS);
			Statement stmt = con.createStatement();	
			stmt.executeUpdate(sql); 
			stmt.close();
			con.close();
			}
         	catch(SQLException se){
	            // 处理 JDBC 错误
	            se.printStackTrace();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }finally{
	            // 关闭资源
	        }
	}
}
