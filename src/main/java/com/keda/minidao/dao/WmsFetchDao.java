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

import com.keda.minidao.entity.WmsFetch;
import com.keda.minidao.entity.WmsStock;


/**
 * 描述：入库单
 * @author：pengwei
 * @since：2018年06月06日 11时11分11秒 星期三 
 * @version:1.0
 */
@Repository
public interface WmsFetchDao {
	
	/**
	 * 查询返回Java对象
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_fetch where id = :id")
	WmsFetch get(@Param("id") String id);
	
	/**
	 * 查询返回Java对象
	 * @deprecated SQL中采用freemarker语法取值,注意需要手工加上单引号（这种写法有SQL注入风险）
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_fetch where id = '${id}'")
	WmsFetch getF(@Param("id") String id);
	
	/**
	 * 查询返回Java对象
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_stock where status = :status and error_msg is :error_msg")
	List<WmsFetch> getFetchBy(@Param("status") String status,@Param("error_msg") String error_msg);
	
	/**
	 * 修改数据
	 * @param fetch
	 * @return
	 */
	int update(@Param("fetch") WmsFetch fetch);
	
	/**
	 * 插入数据
	 * @param fetch
	 */
	void insert(@Param("fetch") WmsFetch fetch);
	
	/**
	 * 插入数据（ID采用自增策略，并返回自增ID）
	 * @param fetch
	 */
	@IdAutoGenerator(generator="native")
	int insertNative(@Param("fetch") WmsFetch fetch);

	/**
	 * 通用分页方法，支持（oracle、mysql、SqlServer、postgresql）
	 * @param WmsFetch
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResultType(WmsFetch.class)
	public MiniDaoPage<WmsFetch> getAll(@Param("fetch") WmsFetch fetch,@Param("page")  int page,@Param("rows") int rows);
	
	/**
	 * 自定义分页
	 * @param fetch
	 * @param startRow  开始序号
	 * @param pageSize  每页显示条数
	 * @return
	 */
	@ResultType(WmsFetch.class)
	@Sql("select * from wms_fetch order by id asc limit :startRow,:pageSize")
	public List<WmsFetch> getPageList(@Param("fetch") WmsFetch fetch,@Param("startRow")  int startRow,@Param("pageSize") int pageSize);
	
	/**
	 * 删除数据
	 * @param fetch
	 */
	@Sql("delete from wms_fetch where id = :id")
	public void delete(@Param("id") String id);
	
	/**
	 * 返回List<Map>类型，全部数据
	 * @param fetch
	 * @return
	 */
	@Arguments({ "fetch"})
	@Sql("select * from wms_fetch")
	List<Map<String,Object>> getAll(WmsFetch fetch);
	
	
	/**
	 * 返回List<Map>类型，全部数据
	 * @param id
	 * @return
	 */
	@Sql("select id  from wms_fetch where status = :status and  (error_msg  = :error_msg|| error_msg is null)")
	List<Map<String,Object>> getMap(@Param("status") String status,@Param("error_msg") String error_msg);
	
	/**
	 * 查询分页数量
	 * @return
	 */
	@Sql("select count(*) from wms_fetch")
	Integer getCount();
}
