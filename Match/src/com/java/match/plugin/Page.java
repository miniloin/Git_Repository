package com.java.match.plugin;

import java.util.ArrayList;
import java.util.List;

public class Page{

    //当前页
    private Integer currentPage;

    //每页显示多少行数据
    private Integer pageSize;

    //总条数
    private Integer totalCount;

    //总页数
    private Integer totalPage;

    //上一页、下一页需要访问的URL
    private String url;

    //分页导航的页码范围
    //首页 上一页 498 499 500 501 502 下一页 尾页
    private List<Integer> indexs;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        match();
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        match();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Integer> getIndexs() {
        return indexs;
    }

    public void setIndexs(List<Integer> indexs) {
        this.indexs = indexs;
    }

    /**
     * 计算当前页应该显示的页码范围 1 2 3 4 5
     */
    private void match(){
        //若当前页和总页数都为空
        if (currentPage == null || totalPage == null){
            return;
        }

        int begin = Math.max(currentPage-2,1);
        int end = Math.min(currentPage+2,totalPage);

        indexs = new ArrayList<>();
        for (int i = begin; i <=end ; i++) {
            indexs.add(i);
        }
    }
}
