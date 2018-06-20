package com.keda.minidao.dao;

import org.jeecgframework.minidao.annotation.Param;
import org.jeecgframework.minidao.annotation.Sql;
import org.springframework.stereotype.Repository;

import com.keda.minidao.entity.WmsStock;
import com.keda.minidao.entity.WmsStockAdjust;


/**
 * 描述：库存
 * @author：pengwei
 * @since：2018年06月07日 14时分17秒 星期三
 * @version:1.0
 */
@Repository
public interface WmsStockAdjustDao {

	/**
	 * 查询返回Java对象
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_stock_adjust where id = :id")
	WmsStockAdjust get(@Param("id") String id);
	
	/**
	 * 修改单据状态
	 * @param stock
	 * @return
	 */
	@Sql("update wms_stock_adjust set adjstatus = :adjstatus where id = :id")
	int updateStockAdjustStatus(@Param("id") String id,@Param("adjstatus") String adjstatus);
}
