package com.java.match.service.impl;

import com.java.match.dao.IBaseDao;
import com.java.match.dao.IUserDao;
import com.java.match.entity.User;
import com.java.match.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    protected IBaseDao getBaseDao() {
       return userDao;
    }

    @Override
    protected Class<User> gettClass() {
        return User.class;
    }

}
