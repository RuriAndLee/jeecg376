package com.keda.minidao.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.entity.WmsFetch;


@Service
public class WmsFetchService {

	@Autowired
	private WmsFetchDao wmsFetchDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void sayHello(){
		wmsFetchDao.getCount();
	}
	
	/**
	 * 执行存储过程
	 * （minidao不支持，直接调用存储过程，采用springjdbc方式进行存储过程调用）
	 */
	public void doProcedure(){
		jdbcTemplate.execute("call sp_insert_table('100001')");
	}
	
	/**
	 * 自定义分页
	 * @param wmsFetch
	 * @param page  当前页数
	 * @param rows  每页显示条数
	 * @return
	 */
	public MiniDaoPage<WmsFetch> getPageAll(WmsFetch wmsFetch,int page,int pageSize){
		MiniDaoPage<WmsFetch>  wmsFetchPageList = new MiniDaoPage<WmsFetch> ();
		//分页显示条数
		wmsFetchPageList.setRows(pageSize);
		int count = wmsFetchDao.getCount();
		wmsFetchPageList.setTotal(count);
		int startRow = (page -1)*pageSize;
		List<WmsFetch> results = wmsFetchDao.getPageList(wmsFetch, startRow, pageSize);
		wmsFetchPageList.setResults(results);
		return wmsFetchPageList;
	}
	
	/**
	 * 事务一致性测试
	 */
	@Transactional
	public void testTransactionalInsert(){
		WmsFetch wmsFetch = new WmsFetch();
		String id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
		wmsFetch.setId(new Integer(id));
//		wmsFetch.setEmpno("C0000001");
//		wmsFetch.setSalary(new BigDecimal(5000));
//		wmsFetch.setBirthday(new Date());
//		wmsFetch.setName("测试事务一致性");
//		wmsFetch.setAge(30);
		//调用minidao方法
		wmsFetchDao.insert(wmsFetch);
		
		
		int i = Integer.parseInt("ss");
		WmsFetch wmsFetch2 = new WmsFetch();
		String id2 = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
		wmsFetch2.setId(new Integer(id2));
//		wmsFetch2.setEmpno("C0000001");
//		wmsFetch2.setSalary(new BigDecimal(5000));
//		wmsFetch2.setBirthday(new Date());
//		wmsFetch2.setName("测试事务一致性");
//		wmsFetch2.setAge(25);
		//调用minidao方法
		wmsFetchDao.insert(wmsFetch2);
	}
}
