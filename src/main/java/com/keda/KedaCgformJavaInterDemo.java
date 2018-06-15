package com.keda;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.dao.WmsFetchdtlDao;
import com.keda.minidao.dao.WmsLocDao;
import com.keda.minidao.dao.WmsStockDao;
import com.keda.minidao.dao.WmsTransDao;
import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.entity.WmsFetchdtl;
import com.keda.minidao.entity.WmsLoc;
import com.keda.minidao.entity.WmsStock;
import com.keda.minidao.service.WmsFetchService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("kedacgformJavaInterDemo")
public class KedaCgformJavaInterDemo implements CgformEnhanceJavaInter {
    static private Log log = LogFactory.getLog(KedaCgformJavaInterDemo.class.getName());
	
	@Override
    public void execute(String tableName,Map map) throws BusinessException {
		
			LogUtil.info("============调用[java增强]成功!========tableName:"+tableName+"===map==="+map);
			if ( (String) map.get("status") == ConstSetBA.FETCHSTATUS_FINISHED || ((String)map.get("status")).equals(ConstSetBA.FETCHSTATUS_FINISHED)) {
				throw new BusinessException("已经完成的订单不能再次提交上架");
			}
			try{
			BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
			WmsFetchService wmsFetchService = (WmsFetchService) factory.getBean("wmsFetchService");
			//启动入库事务
			wmsFetchService.fetchTransactionalInsert(map);
	        }catch (Exception e) {
	        	
	    	e.printStackTrace();

		}	
	}
}