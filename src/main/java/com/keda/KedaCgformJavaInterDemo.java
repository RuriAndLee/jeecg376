package com.keda;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.keda.minidao.service.WmsFetchService;

import java.util.Map;

@Service("kedacgformJavaInterDemo")
public class KedaCgformJavaInterDemo implements CgformEnhanceJavaInter {

    static private Log log = LogFactory.getLog(KedaCgformJavaInterDemo.class.getName());
	private WmsFetchService wmsFetchService;
	
	@Override
    public void execute(String tableName,Map map)  {

		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		WmsFetchService wmsFetchService = (WmsFetchService) factory.getBean("wmsFetchService");
			LogUtil.info("============调用[java增强]成功!========tableName:"+tableName+"===map==="+map);
			try{ 
				if ( (String) map.get("status") == ConstSetBA.FETCHSTATUS_FINISHED || ((String)map.get("status")).equals(ConstSetBA.FETCHSTATUS_FINISHED)) {
					throw new BusinessException("已经完成的订单不能再次提交上架");
				}

				//启动入库事务
				wmsFetchService.fetchTransactionalInsert(map);
			}catch (Exception e) {
				e.printStackTrace();
				wmsFetchService.updateFetchErrormsg((String) map.get("id"),e.getMessage());
				throw new BusinessException(e.getMessage());

		}
			
	} 
}