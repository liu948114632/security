package com.liu.security.controller;

import com.liu.security.dao.PermissionDao;
import com.liu.security.dao.RoleDao;
import com.liu.security.model.Permission;
import com.liu.security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Set;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionDao permissionDao ;

    @Autowired
    private RoleDao roleDao;

    @RequestMapping("/addAll")
    public void addAll(){
        for (int i=0; i<10; i++){
            Permission permission = new Permission();
            permission.setName("hello"+i);
            permission.setUrl("hello"+i);
            permissionDao.save(permission);
        }
    }
    @RequestMapping("/add")
    @Transactional
    public void add(String name, String url){

        Permission permission = new Permission();
        permission.setUrl(url);
        permission.setName(name);
//        查询系统管理员，加上新来的权限
        Role role = roleDao.findOne(1);
        Set<Permission> set = role.getPermissions();
        set.add(permission);
        role.setPermissions(set);
//        有效，事物会回滚
        permissionDao.save(permission);
//        int s= 1/0;
        roleDao.save(role);
    }

}
