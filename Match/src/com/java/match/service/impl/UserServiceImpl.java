package com.java.match.service.impl;

import com.java.match.dao.IBaseDao;
import com.java.match.dao.IUserDao;
import com.java.match.entity.Role;
import com.java.match.entity.User;
import com.java.match.plugin.Page;
import com.java.match.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService,UserDetailsService {

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

    @Override
    public User login(String username, String password) {
        return userDao.login(username,password);
    }

    @Override
    public List<User> queryAllCase(Page page) {
        return userDao.queryAllCase(page);
    }

    @Override
    @Transactional
    public int selectRole(Integer uid, Integer[] rids) {
        //将原来的关系清除
        userDao.deleteRoleByUid(uid);

        userDao.addRole(uid,rids);
        return 1;
    }

    @Override
    @Transactional
    public int selectMatchtype(Integer uid, Integer[] matid) {
        //将原来的关系清除
        //userDao.deleteMatypeByUid(uid);

        userDao.addMatchtype(uid,matid);
        return 1;
    }

    @Override
    public List<Object> queryJoin(Integer id) {
        return userDao.queryJoin(id);
    }

    @Override
    public int deleteByUId(Integer id) {
        //删除用户表
        userDao.deleteById(id);
        //删除关联表的关系
        userDao.deleteRoleByUid(id);
        return 1;
    }

    @Override
    public User queryOneCase(Integer id) {
        return userDao.queryOneCase(id);
    }

    @Override
    public List<User> queryByUserName() {
        return userDao.queryByUserName();
    }


    /**
     *UserDetailsService提供的方法
     * 每当用户登录时,security框架都会自动调用该方法,传入的参数就是用户输入的用户名
     * 若登录失败不清楚是用户名错了还是密码错了
     * 该接口有 String getPassword();方法,会与用户输入的密码匹配
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loginByUserName(username);
        Role role = user.getRole();
        String realise = role.getRrealise();//别名
        System.out.println("所拥有的角色：" + realise);
        return user;
    }
}
