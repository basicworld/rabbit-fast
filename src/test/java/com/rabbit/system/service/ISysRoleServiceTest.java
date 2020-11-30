package com.rabbit.system.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.domain.SysRole;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ISysRoleServiceTest {
	@Autowired
	ISysRoleService roleService;
	SysRole role;

	@Before
	public void before() {
		SysRole item = new SysRole();
		item.setName("name");
		item.setCode("code");
		item.setMenuIds(new Long[0]);
		roleService.insertSelective(item);

		this.role = item;
	}

	@After
	public void after() {
		if (null != this.role && null != this.role.getId()) {
			roleService.deleteByPrimaryKey(this.role.getId());
		}
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.role);
		TestCase.assertNotNull(this.role.getId());
		this.role.setName("new name");
		roleService.updateSelective(this.role);

		SysRole existItem = roleService.selectByPrimaryKey(this.role.getId());
		TestCase.assertNotNull(existItem);
		TestCase.assertEquals("new name", existItem.getName());
	}
}
