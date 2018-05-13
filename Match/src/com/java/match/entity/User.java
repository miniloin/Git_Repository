package com.java.match.entity;

import com.java.match.annotation.IKey;
import com.java.match.annotation.IParam;
import com.java.match.annotation.ITable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@ITable("t_user")
public class User implements UserDetails{

    @IKey("id")
    private Integer id;
    @IParam("username")
    private String username ;
    @IParam("password")
    private String password;
    @IParam("sex")
    private Integer sex;
    @IParam("age")
    private Integer age;
    @IParam("email")
    private String email;
    @IParam("phone")
    private String phone;
    @IParam("area")
    private String area;
    @IParam("pic")
    private String pic;
    @IParam("time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;
    @IParam("rid")
    private Integer rid;

    //一个用户对应一个角色吧
    private Role role;

    List<Role> roles;

    //选手-比赛项目（对多）
    private List<MatchType> matchTypes;

    //当前用户所对应的角色所拥有的权限
    List<Privilege> privileges;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<MatchType> getMatchTypes() {
        return matchTypes;
    }

    public void setMatchTypes(List<MatchType> matchTypes) {
        this.matchTypes = matchTypes;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", area='" + area + '\'' +
                ", pic='" + pic + '\'' +
                ", time=" + time +
                ", rid=" + rid +
                ", role=" + role +
                ", matchTypes=" + matchTypes +
                '}';
    }

    /**
     * UserDetails 提供的方法
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 当用户发送请求时,权限校验框架会调用该方法,获取当前登录的用户有哪些角色
     * Role实现接口GrantedAuthority
     * @return 用户所拥有的角色
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

}
