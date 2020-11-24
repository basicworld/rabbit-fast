package com.rabbit.system.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.domain.SysDeptRole;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ISysDeptRoleServiceTest {
	@Autowired
	ISysDeptRoleService deptRoleService;
	private SysDeptRole deptRole;

	@Before
	public void beforeEach() {
		SysDeptRole item = new SysDeptRole();
		item.setDeptId(new Long(1));
		item.setRoleId(new Long(1));
		deptRoleService.insertSelective(item);

		this.deptRole = item;
	}

	@After
	public void afterEach() {
		if (null != this.deptRole && null != this.deptRole.getId()) {
			deptRoleService.deleteByPrimaryKey(this.deptRole.getId());
		}
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.deptRole);
		TestCase.assertNotNull(this.deptRole.getId());
		this.deptRole.setRoleId(new Long(2));
		deptRoleService.updateSelective(this.deptRole);

		SysDeptRole exitItem = deptRoleService.selectByPrimaryKey(this.deptRole.getId());
		TestCase.assertNotNull(exitItem);
		TestCase.assertEquals(new Long(2), exitItem.getRoleId());

	}
}
