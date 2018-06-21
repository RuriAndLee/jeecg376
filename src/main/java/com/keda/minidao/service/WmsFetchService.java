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
import com.keda.minidao.dao.WmsFetchDao;
import com.keda.minidao.dao.WmsFetchdtlDao;
import com.keda.minidao.dao.WmsLocDao;
import com.keda.minidao.dao.WmsStockDao;
import com.keda.minidao.dao.WmsTransDao;
import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.entity.WmsFetchdtl;
import com.keda.minidao.entity.WmsLoc;
import com.keda.minidao.entity.WmsStock;
import com.keda.minidao.entity.WmsTrans;


@Service
public class WmsFetchService {
    static private Log log = LogFactory.getLog(KedaTransMgr.class.getName());
    @Autowired
    private WmsTransDao transDao;
    @Autowired
	private WmsFetchDao fetchDao;
    @Autowired
	private WmsLocDao locDao;
	@Autowired
	private WmsFetchdtlDao fetchdtlDao;
	@Autowired
	private WmsStockDao stockDao;
	@Autowired
	private WmsFetchDao wmsFetchDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

//	public void sayHello(){
//		wmsFetchDao.getCount();
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
	 * 入库上架
	 */
	@Transactional
	public void fetchTransactionalInsert(Map map){
		WmsFetch fetch = new WmsFetch();
		WmsLocService wmsLocService = new WmsLocService();
		fetch = fetchDao.get((String)map.get("id"));
		List<WmsFetchdtl> fetchdtllist = fetchdtlDao.getDtlByFetchId(fetch.getId());
			for(WmsFetchdtl fdtl:fetchdtllist){
				//查找货位
				WmsLoc loc = findLoc(fdtl.getGoodsno());				
				if(loc.getLocno() == null){
					throw new BusinessException("当前无空货位");
				}
				//根据货位查找
				WmsStock stock = new WmsStock();
				
				//记录库存层数
				stock.setLayer(loc.getLayer()+1);
				
				if (stock.getLayer() == ConstSetBA.LAYER_NUMBER) {
					loc.setTopflag(ConstSetBA.TOPFLAG_TRUE);
					locDao.update(loc);
				}
				
				stock.setGoodsno(fdtl.getGoodsno());
				stock.setStockqty(fdtl.getFetchqty());
				stock.setGoodsunit(fdtl.getGoodsunit());
				stock.setGoodsname(fdtl.getGoodsname());
				stock.setGoodssize(fdtl.getGoodssize());
				stock.setCreateDate(new Date());
				stock.setCreateBy(fetch.getCreateBy());
				stock.setLocno(loc.getLocno());
				stock.setZoneno(loc.getZoneno());
				stock.setTopflag(ConstSetBA.STOCK_TOP);
				int newStockid = stockDao.insertNative(stock);
				Map value = genTransValue(fetch,fdtl,stock);
				try {
					insertStoretrans(value);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}
				
				//当前货位层数增加
				wmsLocService.updateLocLayerByLocid(loc.getId().toString(), ConstSetBA.LAYER_ADD,locDao);
				
				//当前货位的其他库存顶层标志清零
				WmsStockService wmsStockService = new WmsStockService();
				wmsStockService.updateLocTopflag(loc.getLocno(),newStockid,stockDao);
			}
			updateFetchStatus((String) map.get("id"));
			
	} 
	//查找货位
	public WmsLoc findLoc(String goodsno) throws BusinessException {
	    String locno = null; 
	    WmsLoc loc = new WmsLoc();
		WmsStock stock = new WmsStock();
		stock.setGoodsno(goodsno);
		//根据goodsno查找当前库存的货位编号
		List<WmsStock> stocklist= stockDao.getStockByGoodsno(goodsno);//增加顶层标志判断 
		for(WmsStock s:stocklist){
			if (s.getLocno() != null && s.getLocno() != "") {
				loc = locDao.getLocByLocno((String)s.getLocno());
				locno = loc.getLocno();
				continue;
			}
		}
	    if(locno == null || locno == ""){
	    	//查找当前空闲的货位
	    	List<WmsLoc> loclist = locDao.getEmptyLoc();
	    	for(WmsLoc l:loclist){
	    		if (l.getLocno() != null) {
					loc = l;
				}
	    	}
	    }
	    return loc;
	}   
	//组装交易信息生成交易记录
	public Map genTransValue(WmsFetch fetch,WmsFetchdtl fdtl,WmsStock stock){
		try {
			Map transvalue = new HashMap();
			transvalue.put("create_name", fetch.getCreateName());
			transvalue.put("create_by",fetch.getCreateBy());
			transvalue.put("create_date",stock.getCreateDate());
			transvalue.put("transno",fetch.getFetchno());
			transvalue.put("transdate",fetch.getCreateDate());
			transvalue.put("transtype",ConstSetBA.TRANSTYPE_IN);
			transvalue.put("goodaname",stock.getGoodsname());
			transvalue.put("transqty",stock.getStockqty());
			transvalue.put("locno",stock.getLocno());
			transvalue.put("zoneno",stock.getZoneno());
			transvalue.put("sourceid",fetch.getId());
			transvalue.put("sourcedtlid",fdtl.getId());
			transvalue.put("trstatus",ConstSetBA.TRANS_STATUS_FINISHED);
			return transvalue;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	//修改入库状态
	public void updateFetchStatus(String fetchid) throws BusinessException {
		try {
			WmsFetch fetch = new WmsFetch();
			fetch = fetchDao.get(fetchid);
			fetch.setStatus(String.valueOf(ConstSetBA.FETCHSTATUS_FINISHED));
			fetchDao.update(fetch);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
	//填入入库异常信息
		public void updateFetchErrormsg (String fetchid,String error_msg) throws BusinessException{
			try{
				WmsFetch fetch = new WmsFetch();
				fetch = fetchDao.get(fetchid);
				fetch.setError_msg(error_msg);
				fetchDao.update(fetch);		
			} catch(Exception e) {
				e.printStackTrace();
			}
		}	

	
	public void insertStoretrans(Map values) throws Exception {
		if (values == null) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//			WmsTransDao transDao = (WmsTransDao) factory.getBean("wmsTransDao");
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
				trans.setTransdate(sdf.parse((String)values.get("transdate")));
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
