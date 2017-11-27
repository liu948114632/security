package com.liu.security.controller;

import com.liu.security.dao.PermissionDao;
import com.liu.security.dao.RoleDao;
import com.liu.security.model.Permission;
import com.liu.security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class RoleController {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @RequestMapping("/all/role")
    public Object list(int page, int size){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Page<Role> all = roleDao.findAll(pageable);
        return all;
    }
    @RequestMapping("/find")
    public Object findById(int id){
        return roleDao.findOne(id);
    }

    @RequestMapping("/add")
    public void add(){
        for (int i=0; i<10; i++){
            Role role = new Role();
            role.setName("system"+i);
            roleDao.save(role);
        }
    }

    @RequestMapping("/delete")
    public void delete(int id){
        roleDao.delete(id);
    }

    /**
     * 权限数组
     * @param permissions
     * @param id 角色id
     */
    @RequestMapping("/update")
    public void update(String permissions, int id){
        String[] perm = permissions.split(",");
        Set<Permission> set = new HashSet<>();
        for (String aPerm : perm) {
            Permission permission = permissionDao.findOne(Integer.valueOf(aPerm));
            if (permission != null) {
                set.add(permission);
            }
        }
        Role role = roleDao.findOne(id);
        role.setPermissions(set);
        roleDao.save(role);
    }
}
