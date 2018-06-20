package com.keda;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keda.KedaCgformJavaInterDemo;
import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.dao.WmsSoDao;
import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.dao.WmsFetchdtlDao;
import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.entity.WmsFetchdtl;
import com.keda.minidao.entity.WmsSo;
import com.keda.minidao.service.WmsFetchService;





import com.keda.minidao.service.WmsSoService;

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

@Service("kedaAlootSendTask")
public class KedaAllotSendTask implements Job{
	
	BeanFactory factory;
	
	public KedaAllotSendTask(){
		factory = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Autowired
	private TSSmsServiceI tSSmsService;
	private WmsSoDao soDao;



	public void work(){
		long start = System.currentTimeMillis();
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		WmsSoService wmsSoService = (WmsSoService) factory.getBean("wmsSoService");
		org.jeecgframework.core.util.LogUtil.info("===================定时出库任务开始===================");	
		Map<String,Object> map = new HashMap<String,Object>();
		WmsSoDao soDao = (WmsSoDao) factory.getBean("wmsSoDao");
		WmsSo so = new WmsSo();
		try{
	    	
	    	
	    	List<Map<String, Object>> listone=soDao.getMap("0","");
	        for(int i=0;i<listone.size();i++) {
	        	Map<String,Object> map1=listone.get(i);
	        	String str =String.valueOf( map1.get("id"));	        
	    		map.put("id", str);
	    		map.put("status", "0");
	    		KedaAllocateRelease kedaallocateRelease = new KedaAllocateRelease();
	    		kedaallocateRelease.execute("",map);
	    	}		      
		}				
		     catch(Exception e){
		    	  e.printStackTrace();
		    	  wmsSoService.updateSoErrormsg((String) map.get("id"),e.getMessage());
		    	  
	         }finally{
	            // 关闭资源

		     org.jeecgframework.core.util.LogUtil.info("===================定时出库任务结束===================");
		     long end = System.currentTimeMillis();
		     long times = end - start;
		     org.jeecgframework.core.util.LogUtil.info("总耗时"+times+"毫秒");
	        }
	}
	
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		work();
	}



}
	
