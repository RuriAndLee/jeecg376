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

import com.keda.minidao.entity.WmsSodtl;


/**
 * 描述：入库单细单
 * @author：pengwei
 * @since：2018年06月06日 11时11分11秒 星期三 
 * @version:1.0
 */
@Repository
public interface WmsSodtlDao {

	
	
	/**
	 * 查询返回Java对象
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_sodtl where id = :id")
	WmsSodtl get(@Param("id") Integer id);
	
	/**
	 * 查询返回Java对象
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_sodtl where soid = :id")
	List<WmsSodtl> getDtlBySoId(@Param("id") Integer id);
	/**
	 * 查询返回Java对象
	 * @deprecated SQL中采用freemarker语法取值,注意需要手工加上单引号（这种写法有SQL注入风险）
	 * @param id
	 * @return
	 */
	@Sql("select * from wms_sodtl where id = '${id}'")
	WmsSodtl getF(@Param("id") String id);
	
	/**
	 * 修改数据
	 * @param so
	 * @return
	 */
	int update(@Param("so") WmsSodtl wmsso);
	
	/**
	 * 插入数据
	 * @param so
	 */
	void insert(@Param("so") WmsSodtl wmsso);
	
	/**
	 * 插入数据（ID采用自增策略，并返回自增ID）
	 * @param wmsso
	 */
	@IdAutoGenerator(generator="native")
	int insertNative(@Param("wmsso") WmsSodtl wmsso);

	/**
	 * 通用分页方法，支持（oracle、mysql、SqlServer、postgresql）
	 * @param WmsSodtl
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResultType(WmsSodtl.class)
	public MiniDaoPage<WmsSodtl> getAll(@Param("wmsso") WmsSodtl wmsso,@Param("page")  int page,@Param("rows") int rows);
	
	/**
	 * 自定义分页
	 * @param wmsso
	 * @param startRow  开始序号
	 * @param pageSize  每页显示条数
	 * @return
	 */
	@ResultType(WmsSodtl.class)
	@Sql("select * from wms_sodtl order by id asc limit :startRow,:pageSize")
	public List<WmsSodtl> getPageList(@Param("wmsso") WmsSodtl wmsso,@Param("startRow")  int startRow,@Param("pageSize") int pageSize);
	
	/**
	 * 删除数据
	 * @param wmsso
	 */
	@Sql("delete from wms_sodtl where id = :id")
	public void delete(@Param("id") String id);
	
	/**
	 * 返回List<Map>类型，全部数据
	 * @param wmsso
	 * @return
	 */
	@Arguments({ "wmsso"})
	@Sql("select * from wms_sodtl")
	List<Map<String,Object>> getAll(WmsSodtl wmsso);
	
	/**
	 * 返回Map类型，支持多个参数
	 * @param empno
	 * @param name
	 * @return
	 */
	@Sql("select * from wms_sodtl where empno = :empno and  name = :name")
	Map<String,Object> getMap(@Param("empno") String empno,@Param("name")String name);
	
	/**
	 * 查询分页数量
	 * @return
	 */
	@Sql("select count(*) from wms_sodtl")
	Integer getCount();
}
