package com.java.match.service.impl;

import com.java.match.annotation.IKey;
import com.java.match.annotation.IParam;
import com.java.match.annotation.ITable;
import com.java.match.dao.IBaseDao;
import com.java.match.plugin.Page;
import com.java.match.service.IBaseService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T> implements IBaseService<T> {

    //dao层操作对象
    private IBaseDao baseDao;
    //实体类的反射对象
    private Class<T> tClass = gettClass();

    protected abstract IBaseDao getBaseDao();
    protected abstract Class<T> gettClass();


    /**
     * 查询单个对象
     * @param id 根据id查询
     * @return
     */
    @Override
    public T queryOne(Integer id) {

        //获得实体类上的注解(表名)
        String tableName = getTableName();
        //获得实体类对应的表的主键名称
        String idName = getKey();

        //select * from tableName where id = ?
        Map<String, Object> map = getBaseDao().queryOne(tableName, idName, id);

        //map结果集转化成实体
        T t = map2Entity(map);

        return t;
    }

    /**
     * 新增数据
     * @param t
     * @return
     */
    @Override
    public int insert(T t) {
        //获得实体类上的注解（表名）
        String tableName = getTableName();

        //所有字段名和对应的数据
        List<String> fieldNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        //获取所有字段名
        Field[] fields = tClass.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            //获得实体类属性上的注解
            IParam iParam = field.getAnnotation(IParam.class);
            if (iParam != null){
                //获得实体类属性与数据库对应的字段名
                String fieldName = iParam.value();
                fieldNames.add(fieldName);

                //获得该属性的值
                try {
                    Object value = field.get(t);
                    values.add(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //insert into tableName(fields) values(values)
        return getBaseDao().insert(tableName, fieldNames, values);
    }

    /**
     *修改
     * @param t
     * @return
     */
    @Override
    public int update(T t) {
        //获得表名
        String tableName = getTableName();

        //主键名
        String keyName = null;
        //主键值
        Object keyValue = null;

        //存放字段名
        List<String> fieldNames = new ArrayList<>();
        //存放字段名对应的值
        List<Object> fieldValues = new ArrayList<>();

        //通过反射获取所有字段名
        Field[] fields = tClass.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            //判断是否为主键
            IKey iKey = field.getAnnotation(IKey.class);
            if (iKey != null){
                keyName = iKey.value();

                //获得属性值
                try {
                    keyValue = field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            //普通字段
            IParam iParam = field.getAnnotation(IParam.class);
            if (iParam != null){
                //获得字段名
                String fieldName = iParam.value();
                //获得对应的字段值
                Object fieldValue = null;
                try {
                    fieldValue = field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                //添加到集合
                fieldNames.add(fieldName);
                fieldValues.add(fieldValue);
            }
        }

        //update tableName set field = value1 where id = ?
        return getBaseDao().update(tableName,keyName,keyValue,fieldNames,fieldValues);
    }

    /**
     * 删除单个对象
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        //获取表名
        String tableName = getTableName();
        //获取主键名
        String idName = getKey();

        return getBaseDao().deleteById(tableName,idName,id);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Override
    public int deleteByIds(Integer... ids) {
        //获取表名
        String tableName = getTableName();
        //获取主键名
        String idName = getKey();

        return getBaseDao().deleteByIds(tableName,idName,ids);
    }

    /**
     * @param page 分页查询
     * @return
     */
    @Override
    public List<T> queryAll(Page page) {

        //获取表名
        String tableName = getTableName();

        List<Map<String, Object>> mapList = getBaseDao().queryAll(tableName, page);

        //List<Map<String, Object>> ---> 转换成 List<T>
        List<T> tList = map2List(mapList);

        return tList;
    }

//=======================抽取的公共方法=========================
    /**
     * 获得实体类表名
     * @return
     */
    public String getTableName() {
        //通过反射获得表
        ITable iTable = tClass.getAnnotation(ITable.class);
        //表名
        String tableName;
        if (iTable != null){
            tableName = iTable.value();
        }else {
            tableName = tClass.getSimpleName().toLowerCase();
        }
        return tableName;
    }

    /**
     * 获取主键名称
     * @return
     */
    public String getKey() {
        //主键名称名称
        String idName = null;
        //通过反射获得类中的属性
        Field[] fields = tClass.getDeclaredFields();
        for (Field field:fields) {
            //授权
            field.setAccessible(true);
            IKey iKey = field.getAnnotation(IKey.class);
            if (iKey != null){
                idName = iKey.value();
                if (idName.equals("")){
                    idName = field.getName().toLowerCase();
                }
            }
        }
        return idName;
    }

    /**
     *map转换成实体类
     * @param map
     * @return
     */
    private T map2Entity(Map<String, Object> map) {
        //通过反射创建实体类
        T t = null;
        try {
            t = tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //将map转换成T实体类对象
        Field[] fields = tClass.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            //获取字段名
            String fieldName = field.getName();
            //判断当前字段是否包含在map中
            if (map.containsKey(fieldName)){
                //获得查询的结果集
                Object value = map.get(fieldName);

                //将结果集放入实体类中
                try {
                    field.set(t,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

    /**
     * 集合转换
     *
     * @param mapList
     * @return
     */
    private List<T> map2List(List<Map<String, Object>> mapList) {
        //创建List<T>
        List<T> tList = new ArrayList<>();
        for (Map<String, Object> map:mapList){
            T t = map2Entity(map);
            tList.add(t);
        }
        return tList;
    }

}
