package com.liu.security.controller;

import com.liu.security.dao.RoleDao;
import com.liu.security.dao.UserDao;
import com.liu.security.model.Role;
import com.liu.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @RequestMapping("/userList")
    public Object userList(int page , int size, String order){
        Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, order));
         return userDao.findAll(pageable);
    }

    @RequestMapping("/addRole")
    public void addRole(int userId, int roleId){
        User user = userDao.findOne(userId);
        Role role = roleDao.findOne(roleId);
        user.setRole(role);
        userDao.save(user);
    }

    @RequestMapping("/addUser")
    public void addUser(String name, String password){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userDao.save(user);
    }

    @RequestMapping("/delete")
    public void deleteUser(int id){
        userDao.delete(id);
    }
}
