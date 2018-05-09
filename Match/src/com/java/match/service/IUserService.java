package com.java.match.service;

import com.java.match.entity.User;
import com.java.match.plugin.Page;

import java.util.List;

public interface IUserService extends IBaseService<User> {

    User login(String username,String password);

    List<User> queryAllCase(Page page);

    //查询当前用户所对应的角色
    int selectRole(Integer uid,Integer[] rids);

    //选手和比赛项目
    int selectMatchtype(Integer uid, Integer[] matid);

    List<Object> queryJoin(Integer id);

    int deleteByUId(Integer id);
}
