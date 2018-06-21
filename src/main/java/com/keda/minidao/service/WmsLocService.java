package com.keda.minidao.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeecgframework.core.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keda.minidao.dao.WmsLocDao;
import com.keda.minidao.entity.WmsLoc;
import com.keda.minidao.entity.WmsStock;

@Service
public class WmsLocService {
    static private Log log = LogFactory.getLog(WmsLocService.class.getName());
	
	/**
	 * 货位管理
	 */

	//更新货位层数
	public void updateLocLayerByLocid(String locid,Integer num,WmsLocDao locDao) throws BusinessException {
		try {
			WmsLoc loc = new WmsLoc();
			loc = locDao.get(locid);
			loc.setLayer(loc.getLayer() + num);
			locDao.update(loc);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}
	
	//更新货位层数
	public WmsLoc getLocByStock(WmsStock stock,WmsLocDao locDao) throws BusinessException {
		try {
			WmsLoc loc = new WmsLoc();
			loc = locDao.getLocByStock(stock);
			return loc;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}
}
