package com.java.match.controller;

import com.java.match.entity.User;
import com.java.match.plugin.Page;
import com.java.match.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/queryAll")
    public String queryAll(Page page,Model model){
        //List<User> users = userService.queryAll(page);
        List<User> users = userService.queryAllCase(page);
        for (User user:users){
            System.out.println(user);
        }
        model.addAttribute("users",users);
        model.addAttribute("page",page);
        return "user/list";
    }

    @RequestMapping("/insert")
    public String insert(User user){
        //共用一个表单进行新增和修改
        //1.判断提交的操作是新增还是修改
        if (user.getId() == null){
            //新增
            userService.insert(user);
        }else {
            //修改
            userService.update(user);
        }
        return "redirect:/user/queryAll";
    }

    @RequestMapping("/update")
    public String update(User user){
        System.out.println("用户修改");
        int update = userService.update(user);
        System.out.println(update);
        return "index/login";
    }


    @RequestMapping("/deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id){
        userService.deleteByUId(id);
        return "redirect:/user/queryAll";
    }

    @RequestMapping("/queryOne")
    @ResponseBody
    public User queryOne(Integer id,User user){
        user = userService.queryOne(id);
        return user;
    }

    @RequestMapping("/queryOneCase")
    @ResponseBody
    public User queryOneCase(Integer id,User user){
        user = userService.queryOneCase(id);
        System.out.println(user);
        return user;
    }

    /**
     * 设置用户-角色
     * @param uid
     * @param rid
     * @return
     */
    @RequestMapping("/selectRole")
    public String selectRole(Integer uid,Integer[] rid){
        userService.selectRole(uid,rid);
        return "redirect:/user/queryAll";
    }

    /**
     * 设置选手-比赛项目
     * @param uid
     * @param matid
     * @return
     */
    @RequestMapping("/selectMatchtype")
    public String selectMatchtype(Integer uid,Integer[] matid){
        userService.selectMatchtype(uid,matid);
        return "join/join";
    }

}
