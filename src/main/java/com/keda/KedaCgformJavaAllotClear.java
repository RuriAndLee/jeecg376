package com.keda;

import java.util.Map;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.dao.WmsSoDao;
import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.entity.WmsSo;

@Service("kedacgformJavaAllotClear")
public class KedaCgformJavaAllotClear implements CgformEnhanceJavaInter {

	BeanFactory factory;
	
	public KedaCgformJavaAllotClear(){
		factory = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	public void execute(String tableName, Map map) throws BusinessException {
		LogUtil.info("============调用[java增强]成功!========tableName:"+tableName+"===map==="+map);
		
		//无异常信息时返回信息
		if ((String) map.get("error_msg") == null||((String) map.get("error_msg")).equals("")){
			
			throw new BusinessException("不需要进行异常清除!");
		}  
 
		try{
	    	WmsSoDao soDao = (WmsSoDao) factory.getBean("wmsSoDao");
			WmsSo so = new WmsSo();
			so = soDao.get((String)map.get("id"));
			so.setError_msg("");
			soDao.update(so);

	        }catch(Exception e){
				e.printStackTrace();
	        }finally{
	            // 关闭资源
	        }
		
	}
}
