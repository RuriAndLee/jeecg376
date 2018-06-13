package com.keda;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.keda.minidao.dao.WmsTransDao;
import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.entity.WmsFetchdtl;
import com.keda.minidao.entity.WmsLoc;
import com.keda.minidao.entity.WmsStock;
import com.keda.minidao.entity.WmsTrans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service("kedacgformJavaInterDemo")
public class KedaTransMgr {
    static private Log log = LogFactory.getLog(KedaTransMgr.class.getName());
	static BeanFactory factory;
	public KedaTransMgr() {
		factory = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	public static void insertStoretrans(Map values) {
		if (values == null) {
			return;
		}
		try {
			WmsTransDao transDao = (WmsTransDao) factory.getBean("wmsTransDao");
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
				trans.setTransdate((Date) values.get("transdate"));
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
				trans.setTransqty((Integer) values.get("transqty"));
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
				trans.setSourceid((String) values.get("sourceid"));
			} else if (values.get("sourceid") == null || "".equals(values.get("sourceid"))) {
				trans.setSourceid(null);
			}
			
			if (values.get("sourcedtlid") != null && !"".equals(values.get("sourcedtlid"))) {
				trans.setSourcedtlid((String) values.get("sourcedtlid"));
			} else if (values.get("sourcedtlid") == null || "".equals(values.get("sourcedtlid"))) {
				trans.setSourcedtlid(null);
			}
			if (values.get("trstatus") != null && !"".equals(values.get("trstatus"))) {
				trans.setTrstatus((String) values.get("trstatus"));
			} else if (values.get("trstatus") == null || "".equals(values.get("trstatus"))) {
				trans.setTrstatus(null);
			}
			transDao.insertNative(trans);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
	}
}