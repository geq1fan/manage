package com.ge.modules.sys.model;

import com.ge.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限中间表
 * @author cuiP
 * Created by JK on 2017/2/13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePermission extends BaseEntity {
    private Long roleId;
    private Long permissionId;
}
