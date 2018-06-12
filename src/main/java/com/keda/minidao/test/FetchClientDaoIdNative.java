package com.keda.minidao.test;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keda.minidao.dao.WmsFetchdtlDao;
import com.keda.minidao.entity.WmsFetchdtl;


/**
 * 测试ID主键规则： Int 自增
 * （注意：测试该方法需要把数据库ID类型改成int，自增）
 */
public class FetchClientDaoIdNative {
	public static void main(String args[]) {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		WmsFetchdtlDao wmsFetchdtlDao = (WmsFetchdtlDao) factory.getBean("wmsFetchdtlDao");
		
		WmsFetchdtl employee = new WmsFetchdtl();
//		employee.setEmpno("200");
//		employee.setName("scott");
//		employee.setBirthday(new Date());
//		employee.setAge(20);
//		employee.setSalary(new BigDecimal(88888));
		int i = wmsFetchdtlDao.insertNative(employee);
		System.out.println("主键："+i);
	}
}
