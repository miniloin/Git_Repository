package com.java.match.entity;

import com.java.match.annotation.IKey;
import com.java.match.annotation.IParam;
import com.java.match.annotation.ITable;

import java.io.Serializable;
@ITable("t_user")
public class User implements Serializable {

    @IKey
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
    @IParam("")
    private String phone;
    @IParam("isadmin")
    private int isadmin ;
    @IParam("area")
    private String area;
    @IParam("pic")
    private String pic;

    public User() {
    }

    public User(Integer id, String username, String password, Integer sex, Integer age, String email, String phone, int isadmin, String area, String pic) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.isadmin = isadmin;
        this.area = area;
        this.pic = pic;
    }

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
                '}';
    }
}
