package com.java.match.dao;

import com.java.match.annotation.IParam;
import com.java.match.annotation.ITable;
import com.java.match.plugin.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IBaseDao {

    /**
     * 新增数据
     * 思路：新增的SQL语法
     * insert into 表名（字段...） values(值...)
     * 表名未知，字段和值对应，考虑用list存放
     * @return
     */
    int insert(@Param("tableName") String tableName, @Param("fields") List<String> fields, @Param("values") List<?> values);

    /**
     * 删除数据
     * sql  delete from tableName where id = 1
     * @return
     */
    int deleteById(@Param("tableName") String tableName, @Param("key") String key, @Param("id") Integer id);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("tableName") String tableName, @Param("key") String key, @Param("ids") Integer... ids);

    /**
     * 根据id查询单条数据
     * select * from tableName where id = ?
     * @param id
     * @return
     */

    Map<String, Object> queryOne(@Param("tableName") String tableName, @Param("key") String key, @Param("id") Integer id);

    /**
     * 查询所有记录
     * select * from tableName limit (index,size)
     * @return
     */
    List<Map<String, Object>> queryAll(@Param("tableName") String tableName, Page page);

    /**
     * 修改
     * int update (Entity entity)
     * update tableName set field = value where id = ?
     * update tableName set field = value where name= ?
     */
    int update(@Param("tableName") String tableName,@Param("keyName") String keyName,@Param("keyValue") Object keyValue,@Param("fieldNames") List<String> fieldNames,@Param("fieldValues") List<?> fieldValues);


}
