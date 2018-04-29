package com.java.match.plugin;


import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * 分页插件
 * Signature:
 * type:表示该插件需要增强4大对象中的哪一个对象？
 * method:表示插件需要拦截该对象中的哪一个方法？
 * args:表示方法的参数类型
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class,Integer.class}
        )
})

public class PagePlugin implements Interceptor {
    /**
     * 插件的核心控制方法
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("分页插件...");
        //获得目标的statementHandler
        StatementHandler statementHandler = getStatementHandler(invocation);
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        //获得boundSql对象
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        //获得sql语句
        String sql = boundSql.getSql().toLowerCase().trim();

        //1.判断挡墙是否是select语句
        if(!sql.startsWith("select")){
            //不是查询语句就直接放行
            return invocation.proceed();
        }

        //2.若是查询语句判断是否需要分页（自定义一个规则：查询的方法参数是否有page）
        //获得参数
        //如果只有一个参数，那么Object paramter 就是该参数类型
        //如果有多个参数，则Object paramter 就是Map类型
        Object parameter = boundSql.getParameterObject();
        Page page = null;
        if(parameter instanceof Map){
            Map<String,Object> map = (Map<String, Object>) parameter;
            //遍历所有参数
            for (Map.Entry<String,Object> entry : map.entrySet()){
                //判断参数集合中是否存在page
                if (entry.getValue() instanceof Page){
                        page = (Page) entry.getValue();
                        break;
                }
            }
        }else if (parameter instanceof Page){
           page = (Page) parameter;
        }

        //如果没有找到page对象，则说明无需分页，直接放行
        if (page == null){
            return invocation.proceed();
        }

        //分页操作
        //1.计算总条数
        Integer totalCount = getTotalCount(sql,invocation,metaObject);
        //回设page对象
        page.setTotalCount(totalCount);
        page.setTotalPage(page.getTotalCount() % page.getPageSize() == 0 ?
                            page.getTotalCount() / page.getPageSize():
                            page.getTotalCount() / page.getPageSize() + 1);

        //直接分页sql语句
        //去除最后的分号
        int index = -1;
        if((index = sql.lastIndexOf(";")) != -1 ){
            sql = sql.substring(0,index);
        }
        //拼接
        sql += " limit ?,?";
        System.out.println("分页的sql语句："+sql);

        //回设sql语句
        metaObject.setValue("delegate.boundSql.sql",sql);
        //返回值？ invocation.proceed();
        PreparedStatement ps = (PreparedStatement) invocation.proceed();
        int pCount = ps.getParameterMetaData().getParameterCount();
        //获取参数的总个数（sql语句中？的个数）
        ps.setInt(pCount - 1, (page.getCurrentPage() - 1) * page.getPageSize());
        ps.setInt(pCount, page.getPageSize());

        return ps;
    }

    /**
     * 获得查询的总条数
     * select * from student where age > ? and sex = ? order by age
     * select count(*) from student where age > ? and sex = ?
     * @return
     */
    private Integer getTotalCount(String sql,Invocation invocation, MetaObject metaObject) {
        //1.获得查询所有条数的sql语句
        String countSql = "select count(*) " + sql.substring(sql.indexOf("from"));

        //提升性能，去掉order by
        Integer orderIndex = -1;
        if ((orderIndex = countSql.indexOf("order")) != -1){
            countSql = countSql.substring(0,orderIndex);
        }
        System.out.println("查询总条数的sql语句："+ countSql);

        //获得参数处理器
        ParameterHandler ph = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");

        //通过jdbc查询总条数
        //首先获得数据库连接
        Connection conn = (Connection) invocation.getArgs()[0];
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(countSql);
            //设置参数(通过mybatis本身的参数处理器设置sql语句的参数)
            ph.setParameters(ps);
            //执行sql语句
            rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 剥离目标对象，获得最原始的statementHandler对象
     * @return
     */
    private StatementHandler getStatementHandler(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        //MetaObject类:可能会去修改目标对象的某个方法或者属性
        //剥离代理对象，找到最顶层的StatmentHandler
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        //判断当前对象中是否有一个属性名为h，如果有h这个属性，表示该对象仍然是一个代理对象
        while (metaObject.hasGetter("h")){
            statementHandler = (StatementHandler) metaObject.getValue("h");
            metaObject = SystemMetaObject.forObject(statementHandler);
        }
        return statementHandler;
    }


    /**
     * 生成代理对象方法
     * @param o
     * @return
     */
    @Override
    public Object plugin(Object o) {
        //Plugin.wrap()生成代理对象的方法，
        //参数1：需要代理的对象
        //参数2：对目标对象的增强
        //mybatis运行我们实现自定义的代理过程
        return Plugin.wrap(o,this);
    }

    /**
     * 获得插件初始化参数的方法
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {

    }
}
