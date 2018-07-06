package com.ge.modules.sys.model;

import com.ge.common.model.BaseEntity;
import com.ge.common.validator.group.All;
import com.ge.common.validator.group.Create;
import com.ge.common.validator.group.Update;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 角色
 * Created by cuiP on 2017/2/8.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

	//@EqualsAndHashCode(callSuper = true)
	//让其生成的方法中调用父类的方法
	
    //超级管理员标识
    public static final String ROLE_TYPE = "ROEL_ADMIN";

    /**
     * 角色名
     */
    @NotNull(message = "角色不能为空!", groups = {Update.class})
    private String name;

    /**
     * 角色标识
     */
    @NotNull(message = "角色标识不能为空!", groups = {Update.class})
    private String perms;

    /**
     * 备注
     */
    @NotNull(message = "备注不能为空!", groups = {Create.class})
    private String remark;
}
