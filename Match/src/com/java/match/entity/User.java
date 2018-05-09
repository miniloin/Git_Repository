package com.java.match.entity;

import com.java.match.annotation.IKey;
import com.java.match.annotation.IParam;
import com.java.match.annotation.ITable;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ITable("t_user")
public class User{

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
    @IParam("isadmin")
    private int isadmin ;
    @IParam("area")
    private String area;
    @IParam("pic")
    private String pic;
    @IParam("time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    //用户-角色（对多）
    private List<Role> roles;

    //选手-比赛项目（对多）
    private List<MatchType> matchTypes;

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

    public int getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(int isadmin) {
        this.isadmin = isadmin;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<MatchType> getMatchTypes() {
        return matchTypes;
    }

    public void setMatchTypes(List<MatchType> matchTypes) {
        this.matchTypes = matchTypes;
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
                ", isadmin=" + isadmin +
                ", area='" + area + '\'' +
                ", pic='" + pic + '\'' +
                ", time=" + time +
                '}';
    }
}
