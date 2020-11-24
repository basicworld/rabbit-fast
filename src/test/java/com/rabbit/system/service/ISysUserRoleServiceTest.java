package com.rabbit.system.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.domain.SysUserRole;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ISysUserRoleServiceTest {
	@Autowired
	ISysUserRoleService userRoleService;
	private SysUserRole userRole;

	@Before
	public void before() {
		SysUserRole item = new SysUserRole();
		item.setUserId(new Long(1));
		item.setRoleId(new Long(2));
		userRoleService.insertSelective(item);

		this.userRole = item;
	}

	@After
	public void after() {
		if (null != this.userRole && null != this.userRole.getId()) {
			userRoleService.deleteByPrimaryKey(this.userRole.getId());
		}
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.userRole);
		TestCase.assertNotNull(this.userRole.getId());

		this.userRole.setUserId(new Long(3));
		userRoleService.updateSelective(this.userRole);

		SysUserRole existItem = userRoleService.selectByPrimaryKey(this.userRole.getId());

		TestCase.assertNotNull(existItem);
		TestCase.assertEquals(new Long(3), existItem.getUserId());

	}
}
