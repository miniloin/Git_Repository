package com.java.match.controller;

import com.java.match.entity.User;
import com.java.match.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/query/{id}")
    public String query(Integer id, User user, Model model){
        user = userService.queryOne(1);
        System.out.println(user);
        model.addAttribute("user",user);
        return "user/list";
    }
}
