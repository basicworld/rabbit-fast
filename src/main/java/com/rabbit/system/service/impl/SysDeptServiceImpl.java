package com.rabbit.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rabbit.common.core.text.StrFormatter;
import com.rabbit.common.util.StringUtils;
import com.rabbit.common.util.sql.SqlUtil;
import com.rabbit.common.util.valid.ValidResult;
import com.rabbit.framework.web.page.TreeSelect;
import com.rabbit.system.constant.DeptConstants;
import com.rabbit.system.domain.SysDept;
import com.rabbit.system.domain.SysDeptExample;
import com.rabbit.system.domain.SysDeptUser;
import com.rabbit.system.domain.SysRole;
import com.rabbit.system.mapper.SysDeptMapper;
import com.rabbit.system.service.ISysDeptRoleService;
import com.rabbit.system.service.ISysDeptService;
import com.rabbit.system.service.ISysDeptUserService;
import com.rabbit.system.service.ISysRoleService;

@Service
public class SysDeptServiceImpl implements ISysDeptService {
	@Autowired
	SysDeptMapper deptMapper;
	@Autowired
	ISysDeptUserService deptUserService;

	@Autowired
	ISysDeptRoleService deptRoleService;

	@Autowired
	ISysRoleService roleService;

	@Override
	@Transactional
	public Integer insertSelective(SysDept item) {
		item.setCreateTime(new Date());
		item.setDeleted(false);
		item.setId(null);
		Integer count = deptMapper.insertSelective(item);
		// 部门-角色关联
		if (StringUtils.isNotNull(item.getRoleIds())) {
			deptRoleService.insertByDept(item);
		}
		// 部门-用户关联
		if (StringUtils.isNotNull(item.getUserIds())) {
			deptUserService.insertByDept(item);
		}

		return count;
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		deptRoleService.deleteByDeptId(id);
		return deptMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public Integer updateSelective(SysDept item) {
		// 更新部门-角色关联
		if (StringUtils.isNotNull(item.getRoleIds())) {
			deptRoleService.updateByDept(item);
		}
		if (StringUtils.isNotNull(item.getUserIds())) {
			deptUserService.updateByDept(item);
		}
		item.setUpdateTime(new Date());
		return deptMapper.updateByPrimaryKeySelective(item);
	}

	@Override
	public SysDept selectByPrimaryKey(Long id) {
		return deptMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SysDept> listByDept(SysDept dept) {
		// 允许的参数：name fullname orgcode deleted
		SysDeptExample example = new SysDeptExample();
		SysDeptExample.Criteria c1 = example.createCriteria();
		SysDeptExample.Criteria c2 = example.createCriteria();

		if (StringUtils.isNotNull(dept)) {
			// 删除标记
			if (StringUtils.isNull(dept.getDeleted()) || false == dept.getDeleted()) {
				c1.andDeletedEqualTo(false);
				c2.andDeletedEqualTo(false);
			} else {
				c1.andDeletedEqualTo(true);
				c2.andDeletedEqualTo(true);
			}
			// 机构代码
			if (StringUtils.isNotNull(dept.getOrgcode())) {
				String param = SqlUtil.getFuzzQueryParam(dept.getOrgcode());
				c1.andOrgcodeLike(param);
				c2.andOrgcodeLike(param);
			}
			// 机构名称
			if (StringUtils.isNotNull(dept.getName())) {
				String name = SqlUtil.getFuzzQueryParam(dept.getName());
				c1.andNameLike(name);
				c2.andNameLike(name);
				c1.andFullnameLike(name);
				c2.andFullnameLike(name);
			}
		}
		example.or(c2);

		return deptMapper.selectByExample(example);
	}

	@Override
	public List<TreeSelect> buildDeptTreeSelect(List<SysDept> deptList) {
		List<SysDept> deptTrees = buildDeptTree(deptList);
		return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
	}

	@Override
	public List<SysDept> buildDeptTree(List<SysDept> deptList) {
		// https://blog.csdn.net/u011862015/article/details/105009656
		// 数据转为以父ID为key的字典，value是父ID相同的机构列表
		Map<Long, List<SysDept>> deptByParentIdMap = deptList.stream()
				.collect(Collectors.groupingBy(SysDept::getParentId));
		deptList.forEach(dept -> dept.setChildren(deptByParentIdMap.get(dept.getId())));
		return deptList.stream().filter(v -> v.getParentId().equals(DeptConstants.ROOT_DEPT_ID))
				.collect(Collectors.toList());
	}

	@Override
	public ValidResult validCheckBeforeInsert(SysDept dept) {
		// 相同机构全称、机构简称校验
		List<SysDept> deptWithSameNameList = listByNameEqualsTo(dept.getName());
		List<SysDept> deptWithSameFullnameList = listByNameEqualsTo(dept.getFullname());
		if (deptWithSameNameList.size() > 0 || deptWithSameFullnameList.size() > 0) {
			return ValidResult.error("机构全称或机构简称重复");
		}
		// 机构代码重复校验
		List<SysDept> deptWithSameOrgcodeList = listByOrgcodeEqualsTo(dept.getOrgcode());
		if (deptWithSameOrgcodeList.size() > 0) {
			return ValidResult.error("组织机构代码重复，冲突机构全称：" + deptWithSameOrgcodeList.get(0).getFullname());
		}

		return ValidResult.success();
	}

	@Override
	public ValidResult validCheckBeforeUpdate(SysDept dept) {
		if (StringUtils.isNull(dept.getId())) {
			return ValidResult.error("主键不能为空");
		}
		SysDept deptWithSameId = deptMapper.selectByPrimaryKey(dept.getId());
		if (StringUtils.isNull(deptWithSameId)) {
			return ValidResult.error("无法更新不存在的记录");
		}
		// 机构名称重复判断
		List<SysDept> deptWithSameNameList = listByNameEqualsTo(dept.getName());
		List<SysDept> deptWithSameFullnameList = listByNameEqualsTo(dept.getFullname());
		int nameDuplicateCount = deptWithSameNameList.stream().filter(v -> !dept.getId().equals(v.getId()))
				.collect(Collectors.toList()).size();
		int fullnameDuplicateCount = deptWithSameFullnameList.stream().filter(v -> !dept.getId().equals(v.getId()))
				.collect(Collectors.toList()).size();
		if (nameDuplicateCount + fullnameDuplicateCount > 0) {
			return ValidResult.error("机构全称或简称重复");
		}
		// 组织机构代码重复判断
		List<SysDept> deptWithOrgcodeList = listByOrgcodeEqualsTo(dept.getOrgcode());
		int orgcodeDuplicateCount = deptWithOrgcodeList.stream().filter(v -> !dept.getId().equals(v.getId()))
				.collect(Collectors.toList()).size();
		if (orgcodeDuplicateCount > 0) {
			return ValidResult.error("组织机构代码重复");
		}
		return ValidResult.success();
	}

	/**
	 * 删除前校验
	 * 
	 * id 部门id
	 */
	@Override
	public ValidResult validCheckBeforeDelete(Long id) {
		if (StringUtils.isNull(id)) {
			return ValidResult.error("主键不能为空");
		}
		SysDept existDept = selectByPrimaryKey(id);
		if (StringUtils.isNull(existDept)) {
			return ValidResult.error("待删除记录不存在");
		}
		SysDeptExample example = new SysDeptExample();
		SysDeptExample.Criteria c1 = example.createCriteria();
		c1.andParentIdEqualTo(id);
		List<SysDept> children = deptMapper.selectByExample(example);
		if (children.size() > 0) {
			String warning = StrFormatter.format("存在{}个子部门，不能删除", children.size());
			return ValidResult.error(warning);
		}
		// 检查下属用户
		List<SysDeptUser> deptUserList = deptUserService.listByDeptId(id);
		if (deptUserList.size() > 0) {
			String warning = StrFormatter.format("存在{}个用户，不能删除", deptUserList.size());
			return ValidResult.error(warning);
		}
		return ValidResult.success();
	}

	@Override
	public List<SysDept> listByNameEqualsTo(String name) {
		// 机构全称或机构简称
		SysDeptExample nameExample = new SysDeptExample();
		SysDeptExample.Criteria c1 = nameExample.createCriteria();
		c1.andNameEqualTo(name);
		SysDeptExample.Criteria c2 = nameExample.createCriteria();
		c2.andFullnameEqualTo(name);
		nameExample.or(c2);
		return deptMapper.selectByExample(nameExample);
	}

	@Override
	public List<SysDept> listByOrgcodeEqualsTo(String orgcode) {
		// 组织机构代码
		SysDeptExample orgcodeExample = new SysDeptExample();
		orgcodeExample.createCriteria().andOrgcodeEqualTo(orgcode);
		return deptMapper.selectByExample(orgcodeExample);

	}

	@Override
	public Boolean isAdminDept(Long deptId) {
		if (StringUtils.isNull(deptId)) {
			return false;
		}
		List<SysRole> roles = roleService.listByDeptId(deptId);
		return roleService.isContainsAdminRole(roles);
	}

}
