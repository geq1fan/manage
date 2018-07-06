package com.ge.modules.sys.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.sys.mapper.RoleMapper;
import com.ge.modules.sys.mapper.RolePermissionMapper;
import com.ge.modules.sys.model.Role;
import com.ge.modules.sys.model.RolePermission;
import com.ge.modules.sys.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 * Created by cuiP on 2017/2/8.
 */
@Transactional
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService{

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;


    @Transactional(readOnly=true)
    @Override
    public Role findByName(String name) {
        Role role = new Role();
        role.setName(name);
        return this.findOne(role);
    }

    @Transactional(readOnly=true)
    @Override
    public Role findByUserId(Long userId) {
        return roleMapper.findByUserId(userId);
    }

    @Override
    public Boolean deleteRoleAndRolePermissionByRoleId(Long roleId) {
        //删除角色
        int count1 = this.deleteById(roleId);

        //级联删除该角色所关联的权限
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        rolePermissionMapper.delete(rolePermission);
        return count1 == 1;
    }
}
