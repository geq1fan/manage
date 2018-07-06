package com.ge.modules.sys.mapper;

import com.ge.modules.sys.model.Role;
import com.ge.common.mapper.BaseMapper;

/**
 * Created by JK on 2017/2/8.
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID查询角色对象信息
     * @param userId
     * @return
     */
    Role findByUserId(Long userId);
}
