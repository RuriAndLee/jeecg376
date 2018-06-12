package com.keda;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.dao.WmsFetchdtlDao;
import com.keda.minidao.dao.WmsLocDao;
import com.keda.minidao.dao.WmsStockDao;
import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.entity.WmsFetchdtl;
import com.keda.minidao.entity.WmsLoc;
import com.keda.minidao.entity.WmsStock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("kedacgformJavaInterDemo")
public class KedaCgformJavaInterDemo implements CgformEnhanceJavaInter {
	
	BeanFactory factory;
	
	public KedaCgformJavaInterDemo() {
		factory = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	@Override
    public void execute(String tableName,Map map) throws BusinessException {
    	LogUtil.info("============调用[java增强]成功!========tableName:"+tableName+"===map==="+map);
    	if (Long.valueOf((String) map.get("status")) == ConstSetBA.FETCHSTATUS_FINISHED) {
    		throw new BusinessException("已经完成的订单不能再次提交上架");
		}
    	WmsFetchDao fetchDao = (WmsFetchDao) factory.getBean("wmsFetchDao");
		WmsFetch fetch = new WmsFetch();
		fetch = fetchDao.get((String)map.get("id"));
		WmsFetchdtlDao fetchdtlDao = (WmsFetchdtlDao) factory.getBean("wmsFetchdtlDao");
		List<WmsFetchdtl> fetchdtllist = fetchdtlDao.getDtlByFetchId(fetch.getId());
		for(WmsFetchdtl fdtl:fetchdtllist){
			String locno = findLoc(fdtl.getGoodsno());
			//查找货位
			if(locno == null || locno == ""){
				throw new BusinessException("当前无空货位");
			}
			WmsStockDao stockDao = (WmsStockDao) factory.getBean("wmsStockDao");
			WmsStock stock = new WmsStock();
			stock.setGoodsno(fdtl.getGoodsno());
			stock.setStockqty(fdtl.getFetchqty());
			stock.setGoodsunit(fdtl.getGoodsunit());
			stock.setGoodsname(fdtl.getGoodsname());
			stock.setGoodssize(fdtl.getGoodssize());
			stock.setLocno(locno);
			stockDao.insertNative(stock);
            updateFetchStatus((String) map.get("id"));
		}
    }
    
    //查找货位
    public String findLoc(String goodsno) throws BusinessException {
        String locno = null; 
    	WmsStockDao stockDao = (WmsStockDao)factory.getBean("wmsStockDao");
    	WmsStock stock = new WmsStock();
    	stock.setGoodsno(goodsno);
    	//根据goodsno查找当前库存的货位编号
    	List<WmsStock> stocklist= stockDao.getStockByGoodsno(goodsno);//增加顶层标志判断 
    	for(WmsStock s:stocklist){
    		if (s.getLocno() != null) {
				locno = (String)s.getLocno();
				break;
			}
    	}
        if(locno == null || locno == ""){
        	//查找当前空闲的货位
        	WmsLocDao locDao = (WmsLocDao)factory.getBean("wmsLocDao");
        	List<WmsLoc> loclist = locDao.getEmptyLoc();
        	for(WmsLoc loc:loclist){
        		if (loc.getLocno() != null) {
    				locno = (String)loc.getLocno();
    				break;
    			}
        	}
        }
        return locno;
    }       
    
    //修改入库状态
    public void updateFetchStatus(String fetchid) throws BusinessException {
    	try {
    		WmsFetchDao fetchDao = (WmsFetchDao) factory.getBean("wmsFetchDao");
    		WmsFetch fetch = new WmsFetch();
    		fetch = fetchDao.get(fetchid);
    		fetch.setStatus(String.valueOf(ConstSetBA.FETCHSTATUS_FINISHED));
    		fetchDao.update(fetch);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
    }
}