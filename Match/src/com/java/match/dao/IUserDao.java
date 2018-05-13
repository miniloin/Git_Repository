package com.java.match.dao;

import com.java.match.entity.User;
import com.java.match.plugin.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IUserDao extends IBaseDao {

    User login(@Param("username") String username,@Param("password") String password);

    List<User> queryAllCase(Page page);

    int addRole(@Param("uid") Integer uid, @Param("rids") Integer[] rid);

    int addMatchtype(@Param("uid") Integer uid, @Param("matids") Integer[] matid);

    int deleteMatypeByUid(Integer uid);

    List<Object> queryJoin(Integer id);

    int deleteRoleByUid(Integer uid);

    int deleteById(Integer id);

    User queryOneCase(Integer id);

    User loginByUserName(String username);
}
