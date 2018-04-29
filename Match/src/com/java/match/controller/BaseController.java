package com.java.match.controller;

import com.java.match.plugin.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;

public class BaseController {

    /**
     * 该注解注释的方法，会先于controller中的方法执行，该方法用于对普通方法参数的一个绑定操作
     * @param webDataBinder
     * @param request
     */
    @InitBinder
    public void inti(WebDataBinder webDataBinder, final HttpServletRequest request) {
        webDataBinder.registerCustomEditor(Page.class,new PropertyEditorSupport(){
            @Override
            public void setValue(Object value) {
                Page page = (Page) value;
                if (page.getCurrentPage() == null){
                    page.setCurrentPage(1);
                }
                page.setPageSize(5);

                //获取URL
                String url = request.getRequestURI();

                //queryAll?sex=1&age=20?page=2
                //获得url的参数
                String param = request.getQueryString();

                //解决url的双重问号问题
                //queryAll?参数?page=2
                if (param != null && "".equals(param)){
                    url += "?" + param;
                }else {
                    url += "?1=1";
                }

                //处理多个page参数的问题
                int index;
                if ((index = url.indexOf("&currentPage")) != -1){
                    url = url.substring(0,index);
                }
                page.setUrl(url);
                System.out.println("最后的url为：" + url);
                super.setValue(value);
            }
        });
    }
}
