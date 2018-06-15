package com.keda;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecgframework.web.system.sms.service.TSSmsServiceI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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
 * 库存汇总查询-更新表数据
 * @author pengwei
 * @version 1.0 
 */


@Service("kedaCalcStockQty")
public class KedaCalcStockQty implements Job{
	
	BeanFactory factory;
	
	public KedaCalcStockQty(){
		factory = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Autowired
	private TSSmsServiceI tSSmsService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	/**
	 * 执行存储过程
	 * （minidao不支持，直接调用存储过程，采用springjdbc方式进行存储过程调用）
	 */
	public void doProcedure(){
		long start = System.currentTimeMillis();
		org.jeecgframework.core.util.LogUtil.info("===================定时计算库存汇总任务开始===================");	
		
		jdbcTemplate.execute("call calc_stock_qty()");
		
		org.jeecgframework.core.util.LogUtil.info("===================定时计算库存汇总任务结束===================");
		long end = System.currentTimeMillis();
		long times = end - start;
		org.jeecgframework.core.util.LogUtil.info("总耗时"+times+"毫秒");
		}
	
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		doProcedure();
	}
}
	
