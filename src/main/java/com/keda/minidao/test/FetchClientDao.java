package com.keda.minidao.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.entity.WmsFetch;


public class FetchClientDao {
	public static void main(String args[]) {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		WmsFetchDao fetchDao = (WmsFetchDao) factory.getBean("wmsFetchDao");
		WmsFetch fetch = new WmsFetch();
		String id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
		fetch.setId(new Integer(id));
//		fetch.setEmpno("A0100");
//		fetch.setSalary(new BigDecimal(5000));
//		fetch.setBirthday(new Date());
//		fetch.setName("scott100");
//		fetch.setAge(25);
		//调用minidao方法
		fetchDao.insert(fetch);
		
		
		//自定义分页方法（官方分页因为通用机制，有性能牺牲，数据量大建议自定义分页）
//		EmployeeService employeeService = (EmployeeService) factory.getBean("employeeService");
//		MiniDaoPage<Employee> page = employeeService.getPageAll(null, 2, 5);
//		for(Employee e:page.getResults()){
//			System.out.println(e.getId() + "  " +e.getEmpno());
//		}
		
		
	}
}
