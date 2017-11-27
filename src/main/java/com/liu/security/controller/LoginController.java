package com.liu.security.controller;

import com.liu.security.dao.UserDao;
import com.liu.security.model.User;
import com.liu.security.service.SessionManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserDao userDao;

//    @GetMapping("/")
//    public String index(){
//        return "login";
//    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(String name, String password, HttpSession session){
        User user =userDao.findByName(name);
        if(user!=null){
            return "没有该用户";
        }
        if (password.equals(user.getPassword())){
            //            将用户放入session中
            if(session.getAttribute("user") == null){
                session.setAttribute("user",user);
                session.setMaxInactiveInterval(10*1000);
            }
            return "登陆成功";
        }
        return "密码错误";
    }
}
