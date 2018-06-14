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
import com.keda.minidao.service.WmsSoService;

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

/**
 * 出库业务后台逻辑
 * @author pengwei
 * @version 1.0 
 */

@Service("kedaAllocateRelease")
public class KedaAllocateRelease implements CgformEnhanceJavaInter {
    static private Log log = LogFactory.getLog(KedaAllocateRelease.class.getName());
	
	@Override
    public void execute(String tableName,Map map) throws BusinessException {
		try {
			LogUtil.info("============调用[java增强]成功!========tableName:"+tableName+"===map==="+map);
			if ((String) map.get("status") == ConstSetBA.FETCHSTATUS_FINISHED ||
					((String) map.get("status")).equals(ConstSetBA.FETCHSTATUS_FINISHED)) {
				throw new BusinessException("已经完成的出库单不能再次分配");
			}
			BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
			WmsSoService wmsSoService = (WmsSoService) factory.getBean("wmsSoService");
			//启动出库事务
			wmsSoService.soTransactionalInsert(map);
	    }catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
	}
}