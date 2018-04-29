package com.java.match.service;

import com.java.match.plugin.Page;

import java.util.List;

public interface IBaseService<T> {

    /**
     * @param id 根据id查询
     * @return 返回单个对象
     */
    T queryOne(Integer id);

    /**
     * 添加
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 修改
     * @param t
     * @return
     */
    int update(T t);

    /**
     * 删除单个对象
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 删除多个对象
     * @param ids
     * @return
     */
    int deleteByIds(Integer ...ids);

    /**
     * 查询全部
     * @param page 分页查询
     * @return
     */
    List<T> queryAll(Page page);

}
