package com.rabbit.system.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabbit.Application;
import com.rabbit.system.constant.MenuConstants;
import com.rabbit.system.domain.SysMenu;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)

public class ISysMenuServiceTest {
	@Autowired
	ISysMenuService menuService;

	private SysMenu menu;

	@Before
	public void beforeEach() {
		SysMenu item = new SysMenu();
		item.setName("name");
		item.setIcon("bbbb");
		item.setParentId(MenuConstants.ROOT_MENU_ID);
		item.setPath("path");
		item.setComponent(MenuConstants.DEFAULT_COMPONENT);
		item.setPerms("perms");
		menuService.insertSelective(item);
		this.menu = item;
	}

	@After
	public void afterEach() {
		if (null != this.menu && null != this.menu.getId()) {
			menuService.deleteByPrimaryKey(this.menu.getId());

		}
	}

	@Test
	public void update() {
		TestCase.assertNotNull(this.menu);
		TestCase.assertNotNull(this.menu.getId());
		this.menu.setIcon("aaaa");

		menuService.updateSelective(menu);

		SysMenu exitItem = menuService.selectByPrimaryKey(this.menu.getId());
		TestCase.assertNotNull(exitItem);
		TestCase.assertEquals("aaaa", exitItem.getIcon());
	}
}
