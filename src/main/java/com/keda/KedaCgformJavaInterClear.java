package com.keda;

import java.util.Map;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.entity.WmsFetch;

@Service("kedacgformJavaInterClear")
public class KedaCgformJavaInterClear implements CgformEnhanceJavaInter {

	BeanFactory factory;
	
	public KedaCgformJavaInterClear(){
		factory = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	public void execute(String tableName, Map map) throws BusinessException {
		LogUtil.info("============调用[java增强]成功!========tableName:"+tableName+"===map==="+map);
		//无异常信息时返回信息
		if (Long.valueOf((String) map.get("error_msg")) == null){
			
			throw new BusinessException("不需要进行异常清除!");
		}  


		try{
	    	WmsFetchDao fetchDao = (WmsFetchDao) factory.getBean("wmsFetchDao");
			WmsFetch fetch = new WmsFetch();
			fetch = fetchDao.get((String)map.get("id"));
			fetch.setError_msg("null");
			fetchDao.update(fetch);

	        }catch(Exception e){
				throw new BusinessException(e.getMessage());
	        }finally{
	            // 关闭资源
	        }
	}
}
