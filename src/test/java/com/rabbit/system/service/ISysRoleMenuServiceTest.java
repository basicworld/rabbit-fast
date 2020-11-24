package com.rabbit.system.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.domain.SysRoleMenu;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ISysRoleMenuServiceTest {
	@Autowired
	ISysRoleMenuService roleMenuService;
	private SysRoleMenu roleMenu;

	@Before
	public void before() {
		SysRoleMenu item = new SysRoleMenu();
		item.setRoleId(new Long(1));
		item.setMenuId(new Long(2));
		roleMenuService.insertSelective(item);

		this.roleMenu = item;
	}

	@After
	public void after() {
		if (null != this.roleMenu && null != this.roleMenu.getId()) {
			roleMenuService.deleteByPrimaryKey(this.roleMenu.getId());
		}
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.roleMenu);
		TestCase.assertNotNull(this.roleMenu.getId());
		this.roleMenu.setMenuId(new Long(3));
		roleMenuService.updateSelective(this.roleMenu);

		SysRoleMenu existItem = roleMenuService.selectByPrimaryKey(this.roleMenu.getId());
		TestCase.assertNotNull(existItem);
		TestCase.assertEquals(new Long(3), existItem.getMenuId());

	}
}
