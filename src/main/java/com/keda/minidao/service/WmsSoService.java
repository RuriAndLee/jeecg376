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
import com.keda.minidao.dao.WmsLocDao;
import com.keda.minidao.dao.WmsStockDao;
import com.keda.minidao.dao.WmsTransDao;
import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.entity.WmsSo;
import com.keda.minidao.entity.WmsSodtl;
import com.keda.minidao.entity.WmsLoc;
import com.keda.minidao.entity.WmsStock;
import com.keda.minidao.entity.WmsTrans;


@Service
public class WmsSoService {
    static private Log log = LogFactory.getLog(KedaTransMgr.class.getName());
    @Autowired
    private WmsTransDao transDao;
    @Autowired
	private WmsSoDao soDao;
    @Autowired
	private WmsLocDao locDao;
	@Autowired
	private WmsSodtlDao sodtlDao;
	@Autowired
	private WmsStockDao stockDao;
	@Autowired
	private WmsSoDao wmsSoDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

//	public void sayHello(){
//		wmsSoDao.getCount();
//	}
	
	/**
	 * 执行存储过程
	 * （minidao不支持，直接调用存储过程，采用springjdbc方式进行存储过程调用）
	 */
	public void doProcedure(){
		jdbcTemplate.execute("call sp_insert_table('100001')");
	}
	
	/**
	 * 自定义分页
	 * @param wmsSo
	 * @param page  当前页数
	 * @param rows  每页显示条数
	 * @return
	 */
	public MiniDaoPage<WmsSo> getPageAll(WmsSo wmsSo,int page,int pageSize){
		MiniDaoPage<WmsSo>  wmsSoPageList = new MiniDaoPage<WmsSo> ();
		//分页显示条数
		wmsSoPageList.setRows(pageSize);
		int count = wmsSoDao.getCount();
		wmsSoPageList.setTotal(count);
		int startRow = (page -1)*pageSize;
		List<WmsSo> results = wmsSoDao.getPageList(wmsSo, startRow, pageSize);
		wmsSoPageList.setResults(results);
		return wmsSoPageList;
	}
	
	/**
	 * 出库分配
	 */
	@Transactional
	public void soTransactionalInsert(Map map){
		WmsSo so = new WmsSo();
		so = soDao.get((String)map.get("id"));
		List<WmsSodtl> sodtllist = sodtlDao.getDtlBySoId(so.getId());
			//暂时按数量，品种，返回单条库存记录扣减，后续修改为可支持大数量分配多条库存的情况
			for(WmsSodtl sdtl:sodtllist){
				WmsStock stock = new WmsStock();
				//查询满足条件的单条库存记录，等待扣减
				stock = stockDao.findStockBySodtl(sdtl.getGoodsno(),sdtl.getSoqty(),sdtl.getLocno());
				if (stock == null) {
					throw new BusinessException("库存可能已经分配，请重新做单！");
				}
				//根据带扣减库存记录生成交易信息
				Map value = genTransValue(so,sdtl,stock);
				try {
					insertStoretrans(value);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//获取货位
				WmsLocService wmsLocService = new WmsLocService();
				WmsLoc loc = wmsLocService.getLocByStock(stock, locDao);
				//货位的层数-1
//				wmsLocService.updateLocLayerByLocid(loc.getId().toString(), ConstSetBA.LAYER_SUB,locDao);
				loc.setLayer(loc.getLayer()+ConstSetBA.LAYER_SUB);
				
				//如果货位是顶层，则出库时顶层标志清零
				if (loc.getTopflag().equals(ConstSetBA.TOPFLAG_TRUE)) {
					loc.setTopflag(ConstSetBA.TOPFLAG_FALSE);
				}
				locDao.update(loc);	
				
				//位于出库库存下面的库存顶层标志置为1，根据locno，layer-1，定位到下一个库存
				WmsStockService wmsStockService = new WmsStockService();
				wmsStockService.updateStockTopflag(stock, stockDao);
				
				stockDao.delete(stock.getId());
			}
			updateSoStatus((String) map.get("id"));
		} 
  
	//组装交易信息生成交易记录
	public Map genTransValue(WmsSo so,WmsSodtl sdtl,WmsStock stock){
		try {
			Map transvalue = new HashMap();
			transvalue.put("create_name", so.getCreateName());
			transvalue.put("create_by",so.getCreateBy());
			transvalue.put("create_date",stock.getCreateDate());
			transvalue.put("transno",so.getSono());
			transvalue.put("transdate",so.getCreateDate());
			transvalue.put("transtype",ConstSetBA.TRANSTYPE_OUT);
			transvalue.put("goodaname",stock.getGoodsname());
			transvalue.put("transqty",stock.getStockqty());
			transvalue.put("locno",stock.getLocno());
			transvalue.put("zoneno",stock.getZoneno());
			transvalue.put("sourceid",so.getId());
			transvalue.put("sourcedtlid",sdtl.getId());
			transvalue.put("trstatus",ConstSetBA.TRANS_STATUS_FINISHED);
			return transvalue;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	//修改入库状态
	public void updateSoStatus(String soid) throws BusinessException {
		try {
			WmsSo so = new WmsSo();
			so = soDao.get(soid);
			so.setStatus(String.valueOf(ConstSetBA.SOSTATUS_FINISHED));
			soDao.update(so);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
	//填入出库异常信息
	public void updateSoErrormsg (String soid,String error_msg) throws BusinessException{
		try{
			WmsSo so = new WmsSo();
			so = soDao.get(soid);
			so.setError_msg(error_msg);
			soDao.update(so);		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//生成交易信息
	public void insertStoretrans(Map values) throws Exception {
		if (values == null) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		WmsTrans trans = new WmsTrans();
		if (values.get("create_name") != null && !"".equals(values.get("create_name"))) {
			trans.setCreateName((String) values.get("create_name"));
		} else if (values.get("create_name") == null || "".equals(values.get("create_name"))) {
			trans.setCreateName(null);
		}
		
		if (values.get("create_by") != null && !"".equals(values.get("create_by"))) {
			trans.setCreateBy((String) values.get("create_by"));
		} else if (values.get("create_by") == null || "".equals(values.get("create_by"))) {
			trans.setCreateBy(null);
		}
		
		if (values.get("create_date") != null && !"".equals(values.get("create_date"))) {
			trans.setCreateDate((Date)values.get("create_date"));
		} else if (values.get("create_date") == null || "".equals(values.get("create_date"))) {
			trans.setCreateDate(new Date());
		}
		
		if (values.get("transno") != null && !"".equals(values.get("transno"))) {
			trans.setTransno((String) values.get("transno"));
		} else if (values.get("transno") == null || "".equals(values.get("transno"))) {
			trans.setTransno(null);
		}
		
		if (values.get("transdate") != null && !"".equals(values.get("transdate"))) {
			trans.setTransdate((Date)values.get("transdate"));
		} else if (values.get("transdate") == null || "".equals(values.get("transdate"))) {
			trans.setTransdate(new Date());
		}
		
		if (values.get("transtype") != null && !"".equals(values.get("transtype"))) {
			trans.setTranstype((String) values.get("transtype"));
		} else if (values.get("transtype") == null || "".equals(values.get("transtype"))) {
			trans.setTranstype(null);
		}
		
		if (values.get("goodsname") != null && !"".equals(values.get("goodsname"))) {
			trans.setGoodsname((String) values.get("goodsname"));
		} else if (values.get("goodsname") == null || "".equals(values.get("goodsname"))) {
			trans.setGoodsname(null);
		}
		
		if (values.get("transqty") != null && !"".equals(values.get("transqty"))) {
			trans.setTransqty((Integer.valueOf((String)values.get("transqty"))));
		} else if (values.get("transqty") == null || "".equals(values.get("transqty"))) {
			trans.setTransqty(null);
		}
		
		if (values.get("locno") != null && !"".equals(values.get("locno"))) {
			trans.setLocno((String) values.get("locno"));
		} else if (values.get("locno") == null || "".equals(values.get("locno"))) {
			trans.setLocno(null);
		}
		
		if (values.get("zoneno") != null && !"".equals(values.get("zoneno"))) {
			trans.setZoneno((String) values.get("zoneno"));
		} else if (values.get("zoneno") == null || "".equals(values.get("zoneno"))) {
			trans.setZoneno(null);
		}
		
		if (values.get("sourceid") != null && !"".equals(values.get("sourceid"))) {
			trans.setSourceid(String.valueOf((Integer)values.get("sourceid")));
		} else if (values.get("sourceid") == null || "".equals(values.get("sourceid"))) {
			trans.setSourceid(null);
		}
		
		if (values.get("sourcedtlid") != null && !"".equals(values.get("sourcedtlid"))) {
			trans.setSourcedtlid(String.valueOf((Integer)values.get("sourcedtlid")));
		} else if (values.get("sourcedtlid") == null || "".equals(values.get("sourcedtlid"))) {
			trans.setSourcedtlid(null);
		}
		
		if (values.get("trstatus") != null && !"".equals(values.get("trstatus"))) {
			trans.setTrstatus((String) values.get("trstatus"));
		} else if (values.get("trstatus") == null || "".equals(values.get("trstatus"))) {
			trans.setTrstatus(null);
		}

		transDao.insertNative(trans);
	}
}
