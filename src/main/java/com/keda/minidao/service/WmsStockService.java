package com.keda.minidao.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keda.ConstSetBA;
import com.keda.KedaTransMgr;
import com.keda.minidao.dao.WmsSoDao;
import com.keda.minidao.dao.WmsSodtlDao;
import com.keda.minidao.dao.WmsStockAdjustDao;
import com.keda.minidao.dao.WmsLocDao;
import com.keda.minidao.dao.WmsStockDao;
import com.keda.minidao.dao.WmsTransDao;
import com.keda.minidao.entity.WmsSo;
import com.keda.minidao.entity.WmsSodtl;
import com.keda.minidao.entity.WmsLoc;
import com.keda.minidao.entity.WmsStock;
import com.keda.minidao.entity.WmsStockAdjust;
import com.keda.minidao.entity.WmsTrans;


@Service
public class WmsStockService {
    static private Log log = LogFactory.getLog(KedaTransMgr.class.getName());
    @Autowired
	private WmsLocDao locDao;
	@Autowired
	private WmsStockDao stockDao;
	@Autowired
	private WmsStockAdjustDao stockAdjustDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	
	/**
	 * 库存调整
	 */
	@Transactional
	public void stockAdjustTransactionalInsert(Map map){
		
		WmsStockAdjust stockAdjust = new WmsStockAdjust();
		stockAdjust = stockAdjustDao.get((String)map.get("id"));
		
		
		String locno = (String)map.get("orglocno");
		String goodsno = (String)map.get("goodsno");
		Integer orglayer = (Integer.valueOf((String)map.get("orglayer")));
		
		WmsStock stock = new WmsStock();
		stock = stockDao.findStockByAdj(goodsno,locno,orglayer);
		
		stock.setLocno((String)map.get("newlocno"));
		stock.setZoneno((String)map.get("newzoneno"));
		stock.setLayer((Integer)map.get("newlayer"));
		
		stockDao.update(stock);
		
		updateStockAdjustStatus((String) map.get("id"));
		} 

	//修改入库状态
	public void updateStockAdjustStatus(String stockadjustid) throws BusinessException {
		try {
			
			stockAdjustDao.updateStockAdjustStatus(stockadjustid,String.valueOf(ConstSetBA.ADJSTATUS_FIHISHED));
			
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
}
