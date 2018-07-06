package com.ge.modules.sys.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ge.modules.sys.mapper.PermissionMapper;
import com.ge.modules.sys.mapper.RolePermissionMapper;
import com.ge.modules.sys.model.Permission;
import com.ge.modules.sys.model.RolePermission;
import com.ge.modules.sys.service.PermissionService;
import com.ge.modules.sys.vo.TreeNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by cuiP on 2017/2/8.
 */
// @CacheConfig(cacheNames = "permission")
@Transactional
@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

	@Resource
	private PermissionMapper permissionMapper;
	@Resource
	private RolePermissionMapper rolePermissionMapper;

	@Transactional(readOnly = true)
	@Override
	public PageInfo<Permission> findPage(Integer pageNum, Integer pageSize, String name) {
		Example example = new Example(Permission.class);
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(name)) {
			criteria.andLike("name", "%" + name + "%");
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Permission> PermissionList = this.selectByExample(example);
		return new PageInfo<Permission>(PermissionList);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Permission> findAll(){
		Example example = new Example(Permission.class);
		example.orderBy("type");
		List<Permission> list = this.selectByExample(example);
		return list;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Permission> findSimilarByName(String permissionName){
		Example example = new Example(Permission.class);
		Criteria criteria = example.createCriteria();
		criteria.andLike("name", "%"+permissionName+"%");
		List<Permission> list = this.selectByExample(example);
		return list;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Permission> findListPermissionByUserId(Long userId) {
		return permissionMapper.findListPermissionByUserId(userId);
	}

	@Cacheable(value = "menuListCache", key = "#userId") // 根据userId缓存对象，当userId更新时清空缓存
	@Transactional(readOnly = true)
	@Override
	public List<Permission> findMenuListByUserId(Long userId) {
		return permissionMapper.findMenuListByUserId(userId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Permission> findListByType(String type) {
		Permission permission = new Permission();
		permission.setType(type);
		return this.findListByWhere(permission);
	}

	@Transactional(readOnly = true)
	@Override
	public List<TreeNode> findTreeList() {
		return permissionMapper.findTreeList();
	}

	@Override
	public Boolean deletePermissionAndRolePermissionByPermissionId(Long permissionId) {
		// 删除权限
		int count1 = this.deleteById(permissionId);

		// 删除该权限和角色的关联信息
		RolePermission rolePermission = new RolePermission();
		rolePermission.setPermissionId(permissionId);
		rolePermissionMapper.delete(rolePermission);
		return count1 == 1;
	}

	@Override
	public List<Permission> findChildPermissionByName(String permissionName) {
		List<Permission> list = permissionMapper.findChildPermissionByName(permissionName);
		return list;
	}
}
