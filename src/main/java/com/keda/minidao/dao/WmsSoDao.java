package com.keda.minidao.dao;

import java.util.List;
import java.util.Map;

import org.jeecgframework.minidao.annotation.Arguments;
import org.jeecgframework.minidao.annotation.IdAutoGenerator;
import org.jeecgframework.minidao.annotation.Param;
import org.jeecgframework.minidao.annotation.ResultType;
import org.jeecgframework.minidao.annotation.Sql;
import org.jeecgframework.minidao.pojo.MiniDaoPage;
import org.springframework.stereotype.Repository;

import com.keda.minidao.entity.WmsSo;
import com.keda.minidao.entity.WmsStock;


/**
 * 描述：入库单
 * @author：pengwei
 * @since：2018年06月06日 11时11分11秒 星期三 
 * @version:1.0
 */
@Repository
public interface WmsSoDao {

	
	
	/**
	 * 查询返回Java对象
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_so where id = :id")
	WmsSo get(@Param("id") String id);
	
	/**
	 * 查询返回Java对象
	 * @deprecated SQL中采用freemarker语法取值,注意需要手工加上单引号（这种写法有SQL注入风险）
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_so where id = '${id}'")
	WmsSo getF(@Param("id") String id);
	
	/**
	 * 查询返回Java对象
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_stock where status = :status and error_msg is :error_msg")
	List<WmsSo> getSoBy(@Param("status") String status,@Param("error_msg") String error_msg);
	
	/**
	 * 修改数据
	 * @param so
	 * @return
	 */
	int update(@Param("so") WmsSo so);
	
	/**
	 * 插入数据
	 * @param so
	 */
	void insert(@Param("so") WmsSo so);
	
	/**
	 * 插入数据（ID采用自增策略，并返回自增ID）
	 * @param so
	 */
	@IdAutoGenerator(generator="native")
	int insertNative(@Param("so") WmsSo so);

	/**
	 * 通用分页方法，支持（oracle、mysql、SqlServer、postgresql）
	 * @param WmsSo
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResultType(WmsSo.class)
	public MiniDaoPage<WmsSo> getAll(@Param("so") WmsSo so,@Param("page")  int page,@Param("rows") int rows);
	
	/**
	 * 自定义分页
	 * @param so
	 * @param startRow  开始序号
	 * @param pageSize  每页显示条数
	 * @return
	 */
	@ResultType(WmsSo.class)
	@Sql("select * from wms_so order by id asc limit :startRow,:pageSize")
	public List<WmsSo> getPageList(@Param("so") WmsSo so,@Param("startRow")  int startRow,@Param("pageSize") int pageSize);
	
	/**
	 * 删除数据
	 * @param so
	 */
	@Sql("delete from wms_so where id = :id")
	public void delete(@Param("id") String id);
	
	/**
	 * 返回List<Map>类型，全部数据
	 * @param so
	 * @return
	 */
	@Arguments({ "so"})
	@Sql("select * from wms_so")
	List<Map<String,Object>> getAll(WmsSo so);
	
	
	/**
	 * 返回List<Map>类型，全部数据
	 * @param id
	 * @return
	 */
	@Sql("select id  from wms_so where status = :status and  (error_msg = :error_msg || error_msg is null)")
	List<Map<String,Object>> getMap(@Param("status") String status,@Param("error_msg") String error_msg);
	
	/**
	 * 查询分页数量
	 * @return
	 */
	@Sql("select count(*) from wms_so")
	Integer getCount();
}
