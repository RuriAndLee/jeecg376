package com.keda;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keda.KedaCgformJavaInterDemo;
import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.entity.WmsFetch;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.web.system.sms.service.TSSmsServiceI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 
 * @ClassName:TestSendTask 
 * @Description: 测试定时任务
 * @date 2018-6-1 上午10:30:00
 * 
 */

@Service("kedaPutawaySendTask")
public class KedaPutawaySendTask implements Job{
	
	BeanFactory factory;
	
	public KedaPutawaySendTask(){
		factory = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Autowired
	private TSSmsServiceI tSSmsService;



	public void work(){
		long start = System.currentTimeMillis();
		org.jeecgframework.core.util.LogUtil.info("===================定时入库任务开始===================");	
		Map<String,Object> map = new HashMap<String,Object>();
		WmsFetchDao fetchDao = (WmsFetchDao) factory.getBean("wmsFetchDao");
		WmsFetch fetch = new WmsFetch();
		try{
	    	
	    	
	    	List<Map<String, Object>> listone=fetchDao.getMap("0",null );
	        for(int i=0;i<listone.size();i++) {
	        	Map<String,Object> map1=listone.get(i);
	        	String str =String.valueOf( map1.get("id"));	        
	    		map.put("id", str);
	    		map.put("status", "0");
	    		KedaCgformJavaInterDemo kedacgformJavaInterDemo = new KedaCgformJavaInterDemo();
	    		kedacgformJavaInterDemo.execute("",map);
	    		}		      
			}				
		      catch(Exception e){
		    	  e.printStackTrace();
		    	
	        }finally{
	            // 关闭资源
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
	
